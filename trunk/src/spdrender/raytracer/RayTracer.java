/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer;

import spdrender.raytracer.base.*;
import spdrender.raytracer.primitives.*;

import spdrender.FrameBufferWindow;
import java.util.Random;
import java.io.PrintWriter;

/**
 * Ray Tracer implementation.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class RayTracer {
    /** The tile size for partitioning the framebuffer. */
    public static final int   TILE_SIZE      = 64;
    /** The distance of the camera relative to the screen plane */
    public static final double V_DISTANCE     = 5.0;
    /** The maximum trace distance in the scene. */
    public static final double DISTANCE_LIMIT = 100000.0;
    /** The depth recursion for the ray tracing algorithm. */
    public static final int   TRACE_DEPTH    = 1;
    
    private int planeLeft, planeRight, planeBottom, planeTop;
        
    private int w, h, s;
    private int depth;
    private Vector3D leftBorder, rightBorder, bottomlBorder, bottomrBorder;
    private Vector3D ax, ay;
    private double dist;
    
    private long start, end;
    
    private double[][][] fb;
    private FrameBufferWindow v;
    private Scene sc;
    private int progress;
    private int totalPieces;
    private Random r;
    
    /**
     * Implementation for the threaded ray tracing.
     */
    class RenderThread implements Runnable {
        private int pieces[];
        private int start, end;
        private PrintWriter pw;
        
        /**
         * Creates a new render thread with the specified tile list and the interval to work on.
         * @param pieces Tile set.
         * @param start Start index of the tile set.
         * @param end End index of the tile set.
         */
        public RenderThread(int pieces[], int start, int end){
            this.pieces = pieces;
            this.start = start;
            this.end = end;
            pw = null;
        }

        /**
         * Generates a new distributed render thread.
         * @param pieces The tile set.
         * @param start Start index of the tile set to work with.
         * @param end End index of the tile set to work with.
         * @param pw A <code>PrintWriter</code> used to gradually write the rendering results.
         */
        public RenderThread(int[] pieces, int start, int end, PrintWriter pw){
            this.pieces = pieces;
            this.start = start;
            this.end = end;
            this.pw = pw;
        }
        
        public void run(){
            Vector3D co = sc.getCamera().getOrigin();
            int cols = LoadBalancer.getTileCount(w);
            for(int u = start; u < end; ++u){
                int vs = (pieces[u]/cols)*RayTracer.TILE_SIZE;
                int ve = (pieces[u]/cols + 1)*RayTracer.TILE_SIZE;
                int hs = (pieces[u] % cols)*RayTracer.TILE_SIZE;
                int he = (pieces[u] % cols + 1)*RayTracer.TILE_SIZE;
                ve = ve > h ? h : ve;
                he = he > w ? w : he;
                for(int j = vs; j < ve; ++j){
                    for(int i = hs; i < he ; ++i){
                        for(int aa1 = 0; aa1 < s; ++aa1){
                            for(int aa2 = 0; aa2 < s ;++aa2){
                                Vector3D left = leftBorder.add(ay.scalarProd(j*1.0/(h-1) + aa1*1.0/((h-1)*s) + Vector3D.EPSILON));
                                Vector3D ad1 = left.add(ax.scalarProd(i*1.0/(w-1) + aa2*1.0/((w-1)*s) + Vector3D.EPSILON));
                                Vector3D ad = ad1.sub(co);
                                Ray r = new Ray(co, ad.normalize());                
                                Color c = new Color();
                                rayTrace(r, c, 0);
                                double cr, cg, cb;
                                cr = c.getR();
                                cg = c.getG();
                                cb = c.getB();
                                cr = cr > 1.0 ? 1.0 : cr;
                                cg = cg > 1.0 ? 1.0 : cg;
                                cb = cb > 1.0 ? 1.0 : cb;
                                fb[i][j][0] += cr/(s*s);
                                fb[i][j][1] += cg/(s*s);
                                fb[i][j][2] += cb/(s*s);
                            }
                        }
                    }   
                }
                ++progress;
                if(pw == null){
                    v.writeSection(fb, hs, vs, he - hs, ve - vs);
                } else {
                    synchronized(pw){
                        pw.println(pieces[u]);
                        for(int i = vs; i < ve; ++i){
                            int j;
                            for(j = hs; j < he-1; ++j){
                                pw.print(fb[j][i][0] + "|" + fb[j][i][1] + "|" + fb[j][i][2] + ",");
                            }
                            pw.println(fb[j][i][0] + "|" + fb[j][i][1] + "|" + fb[j][i][2]);
                        }
                    }
                }
            }
            if(v != null){
                long time = System.currentTimeMillis();
                if(RayTracer.this.end < time){
                    RayTracer.this.end = time;
                    v.timeLabel.setText((RayTracer.this.end - RayTracer.this.start)/1000 + " secs");
                }
            }
        }
    }

    /**
     * Creates a new Ray Tracer instance.
     * @param scn A <b>sealed</b> scene to render.
     * @param depth Number of light bounces.
     * @throws IllegalArgumentException in case the given scene is not sealed.
     */
    public RayTracer(Scene scn, int depth) throws IllegalArgumentException {
        start = System.currentTimeMillis();
        if(!scn.isSealed()){
            throw new IllegalArgumentException("The ray tracer requires a sealed scene");
        }
        w = scn.getRenderWidth();
        h = scn.getRenderHeight();
        s = scn.getAAPasses();
        sc = scn;
        this.fb = new double[w][h][3];
        this.depth = depth; 
        progress = totalPieces = 0;
        r = new Random();
    }
    
    /**
     * Performs a multithreaded local render of the scene.
     * @param threads Number of render threads to spawn.
     * @param fbw The framebuffer visualizer that will be used to show the results.
     */
    public void render(int threads, FrameBufferWindow fbw){
        v = fbw;
        LoadBalancer lb = new LoadBalancer(w, h);
        totalPieces = lb.getPieces().length;
        render(lb.getPieces(), lb.getUniformSegments(threads), threads);
    }
    
    /**
     * Performs a multithreaded render of a partial part of the scene.
     * @param pieces The tile set.
     * @param segments An interval list.  The first index represents the interval index, and the second 
     * one stores 2 ints: index-0 for the start of the interval, index-1 for the end of the interval.
     * @param threads Number of threads to spawn.
     */
    private void render(int[] pieces, int[][] segments, int threads){
        initRender();
        Runnable rt[] = new Runnable[threads];
        for(int i = 0; i < threads; ++i){
            rt[i] = new RenderThread(pieces, segments[i][0], segments[i][1]);
            new Thread(rt[i]).start();
        }
    }
    
    /**
     * Performs a multithreaded and distributed render of a partial part of the scene.
     * @param pieces The tile set.
     * @param segments The interval list.
     * @param threads Number of threads to spawn.
     * @param pw A <code>PrintWriter</code> used to remotely write the results for another application 
     * or network node.
     */
    public void render(int[] pieces, int[][] segments, int threads, PrintWriter pw){
        initRender();
        Runnable rt[] = new Runnable[threads];
        for(int i = 0; i < threads; ++i){
            rt[i] = new RenderThread(pieces, segments[i][0], segments[i][1], pw);
            totalPieces += segments[i][1] - segments[i][0];
            new Thread(rt[i]).start();
        }
    }
    
    /**
     * Returns the framebuffer of the scene in a single precision, floating point format.
     * 
     * @return A tridimensional array that represents the framebuffer. The first and second indexes
     * represent the x, y position in pixels, and the third index represents the RGB (in this order)
     * components represented in a [0,1] interval.
     */
    public double[][][] getFrameBuffer(){
        return fb;
    }
    
    /**
     * Informs the progress of the current render.
     * @return A single precision doubleing point number between 0 and 100.
     */
    public double getProgress(){
        return (progress*100.f)/totalPieces;
    }
    
    private void rayTrace(Ray r, Color c, int depth){
        if(depth > this.depth){
            return;
        } else {
            Primitive p = nearest(r);
            Vector3D ip = r.getOrigin().add(r.getDirection().scalarProd(dist));
            if(p == null){
                return;
            }
            if(!p.isLight()){
                Primitive[] primitives = sc.getPrimitives();
                Vector3D ln = p.getNormal(ip);
                for(int g = 0; g < primitives.length; ++g){
                    Primitive prim = primitives[g];
                    if(prim.isLight()){
                        Light lite = (Light) prim;
                        Vector3D ld = calcLightDir(lite, ip);
                        double shade = calcShade(lite, ld, ip);
                        if(shade > 0.0){
                            double diff = ld.dot(ln) * p.getMaterial().diffuse * lite.getIntensity() * shade;
                            if(diff > Vector3D.EPSILON){
                                Color dlc = new Color(p.getMaterial().color);
                                dlc.multiply(lite.getColor());
                                dlc.scale(diff);
                                c.add(dlc);
                            }
                            Color specular = getSpecularColor(p, ld, ip, r);
                            specular.scale(shade);
                            c.add(specular);
                        }
                    }
                }
                if(p.getMaterial().reflect > Vector3D.EPSILON){
                       Color rc = getReflectColor(p, ip, r, depth);
                       Color rc2 = new Color(rc);
                       rc2.scale(0.5f);
                       rc.multiply(p.getMaterial().color);
                       rc.scale(0.5f);
                       c.add(rc);
                       c.add(rc2);
                }
                
                if(p.getMaterial().refract > Vector3D.EPSILON){
                    Color rc = getRefractionColor(p, ip, r, depth);
                    rc.scale(p.getMaterial().refract);
                    c.add(rc);
                }
            } else {
                Light l = (Light) p;
                c.setR(l.getColor().getR());
                c.setG(l.getColor().getG());
                c.setB(l.getColor().getB());
            }
        }
    }
    
    private double calcShade(Light l, Vector3D ld, Vector3D ip){
        switch(l.getType()){
            case Primitive.SURFACE:
                double lsc = l.getSampleCount();
                SurfaceLight sl = (SurfaceLight)l;
                double retval = 0.0;
                for (int x = 0; x < lsc; x++){
                    for ( int y = 0; y < lsc; y++ ){
                        Vector3D left = sl.getPosition().add(sl.getVSide().scalarProd((r.nextDouble() + x)*(1.0/lsc) + Vector3D.EPSILON));
                        Vector3D ad = left.add(sl.getHSide().scalarProd((r.nextDouble() + y)*(1.0/lsc) + Vector3D.EPSILON));
			Vector3D dir = ad.sub(ip);
			dir = dir.normalize();
			Primitive si = shadowIntersect(new Ray(ip.add(dir.scalarProd(Vector3D.EPSILON)), dir), l);
				if (si == null) retval += 1.0 / (lsc*lsc);
                    }
		}
                return retval;
            case Primitive.POINT:
                Ray lr = new Ray(ip, ld);
                Primitive si = shadowIntersect(lr, l);
                return (si == null) ? 1.0 : 0.0;
            default:
                throw new IllegalArgumentException("Unsupported light primitive");
        }
    }
    
    private Vector3D calcLightDir(Light l, Vector3D ip){
        switch(l.getType()){
            case Primitive.SURFACE:
                SurfaceLight sl = (SurfaceLight)l;
                return sl.getPosition().add(sl.getCorner().scalarProd(0.5f)).sub(ip).normalize();
            case Primitive.POINT:
                return l.getPosition().sub(ip).normalize();
            default:
                throw new IllegalArgumentException("Unsupported light primitive");
        }
    }
    
    private Color getSpecularColor(Primitive p, Vector3D ld, Vector3D ip, Ray r){
        Vector3D n = p.getNormal(ip);
        Vector3D rv = ld.sub(n.scalarProd(2.0*ld.dot(n)));
        double specular = rv.dot(r.getDirection());
        double spec = 0.0;
        if(specular > 0){
            spec = (double)(Math.pow(specular ,p.getMaterial().spechard)) * p.getMaterial().specular * n.dot(ld);
        }
        Color slc = new Color(1.0,1.0,1.0);
        slc.scale(spec);
        return slc;
    }
    
    private Color getReflectColor(Primitive p, Vector3D ip, Ray r, int depth){
        Color rc = new Color();
        Vector3D n = p.getNormal(ip);
        double rs = 2.0*n.dot(r.getDirection());
        Vector3D rr = r.getDirection().sub(n.scalarProd(rs)).normalize();
        Ray rray = new Ray(ip.add(rr.scalarProd(Vector3D.EPSILON)), rr);
        rayTrace(rray, rc, depth+1);
        rc.scale(p.getMaterial().reflect);
        return rc;
    }
    
    private Primitive nearest(Ray r){
        Primitive[] prims = sc.getPrimitives();
        Primitive best = null;
        double nearest = RayTracer.DISTANCE_LIMIT;
        for(int i = 0; i < prims.length; ++i){
            Primitive act = prims[i];
            double d = act.intersect(r);
            if(d < nearest){
                nearest = d;
                best = act;
            }
        }
        if(nearest >= RayTracer.DISTANCE_LIMIT)
            return null;
        else {
            dist = nearest;
            return best;
        }
    }
  
    private Color getRefractionColor(Primitive p, Vector3D ip, Ray r, int depth){
        double ior = p.getMaterial().ior;
        double rc;
        Vector3D d = r.getDirection();
        Vector3D n = p.getNormal(ip);
        if(n.dot(d) < 0){
            rc = 1.0/ior;
        } else {
            rc = ior;
            n = n.inv();
        }
        double cost1 = d.dot(n)*-1.0;
        double cost2s = 1.0 - rc*rc*(1 - cost1*cost1);
        double cost2 = ((double)Math.sqrt(cost2s));
        Vector3D t = d.add(n.scalarProd(cost1)).scalarProd(rc).sub(n.scalarProd(cost2));
        Color rcol = new Color();
        rayTrace(new Ray(ip.add(t.scalarProd(Vector3D.EPSILON)), t), rcol, depth+1);
        return rcol;
    }
    
    private Primitive shadowIntersect(Ray r, Light l){
        Primitive[] prims = sc.getPrimitives();
        Primitive best = null;
        double nearest = l.getPosition().sub(r.getOrigin()).norm();
        for(int i = 0; i < prims.length; ++i){
            Primitive act = prims[i];
            double d = act.intersect(r);
            if(d < nearest && d > Vector3D.EPSILON){
                nearest = d;
                best = act;
            }
        }
        if(nearest >= RayTracer.DISTANCE_LIMIT)
            return null;
        else {
            dist = nearest;
            return best;
        }
    }
    
    private void initRender(){
        Camera cam = sc.getCamera();
        
        int a = sc.getRenderWidth(); int b = sc.getRenderHeight();
        
        /* Retrieves the gcd of the width and height to get the aspect ratio 
         * of the image.
         */
        while(b != 0){
            int t = b;
            b = a % b;
            a = t;
        }
        
        planeLeft = sc.getRenderWidth()/a;
        planeTop = sc.getRenderHeight()/a;
        planeRight = -1*planeLeft;
        planeBottom = -1*planeTop;
         
        /* Now create an XY coplanar rectangle for the focal plane */
        leftBorder = new Vector3D(planeLeft, planeTop, 0.0);
        rightBorder = new Vector3D(planeRight, planeTop, 0.0);
        bottomlBorder = new Vector3D(planeLeft, planeBottom, 0.0);
        bottomrBorder = new Vector3D(planeRight, planeBottom, 0.0);

        /* Build an orthonormal base with the camera direction */
        Vector3D cw = cam.getDirection();
        Vector3D up = new Vector3D(0,1,0);
        Vector3D cu = up.cross(cw).normalize();
        Vector3D cv = cu.cross(cw.inv());

        /* Compute the focal plane rotation matrix */
        TransformMatrix tm = new TransformMatrix(cu, cv, cw);
        tm = tm.transpose();
        
        /* Apply the matrix */
        leftBorder = tm.apply(leftBorder);
        rightBorder = tm.apply(rightBorder);
        bottomlBorder = tm.apply(bottomlBorder);
        bottomrBorder = tm.apply(bottomrBorder);
        
        /* Translate the camera to the specified origin */
        leftBorder = leftBorder.add(cam.getOrigin());
        rightBorder = rightBorder.add(cam.getOrigin());
        bottomlBorder = bottomlBorder.add(cam.getOrigin());
        bottomrBorder = bottomrBorder.add(cam.getOrigin());
        
        /* Move the camera origin by the number of units of the
         * observer from the focal plane */
        Vector3D newOrigin = new Vector3D(0,0, -1.0*V_DISTANCE);
        newOrigin = tm.apply(newOrigin);
        newOrigin = newOrigin.add(cam.getOrigin());
        cam.setOrigin(newOrigin);
        
        /* Compute de sides of the focal plane used to interpolate
         * when rendering */
        ax = rightBorder.sub(leftBorder);
        ay = bottomlBorder.sub(leftBorder);
        
    }
}