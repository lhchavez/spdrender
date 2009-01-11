/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.parser;

import spdrender.raytracer.Camera;
import spdrender.raytracer.Scene;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import org.w3c.dom.*;

import spdrender.raytracer.base.*;
import spdrender.raytracer.primitives.*;

/**
 * 3D Scene XML parser for the ray tracer engine.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class SceneParser {
    private Document d;
    
    /**
     * Creates a new Parser engine for the specified input file.
     * @param doc An XML document file storing the scene configuration.
     * @throws java.io.IOException In case an I/O error occurs while reading the file.
     * @throws spdrender.parser.SceneParserException In case the XML document is invalid.
     */
    public SceneParser(File doc) throws IOException, SceneParserException {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        dbf.setIgnoringComments(true);
        try {
            SPDErrorHandler eh = new SPDErrorHandler();
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(eh);
            Document document = null;
            document = db.parse((File)doc);
            if(eh.failed()){
                throw new SceneParserException(eh.getMessage());
            }
            d = document;
        } catch(ParserConfigurationException e){
            throw new SceneParserException("Internal error, parser configuration: " + e.getMessage());
        } catch(SAXException e){
            throw new SceneParserException("SAX Exception: " + e.getMessage());
        }

    }
    
    /** 
     * Parses the scene.
     * @return A sealed <code>Scene</code> class containing all the needed information to render
     * the scene with the ray tracer engine.
     */
    public Scene parseScene(){
        Scene ns;
        Element r = d.getDocumentElement();
        int w = Integer.valueOf(r.getAttribute("width"));
        int h = Integer.valueOf(r.getAttribute("height"));
        int aa = Integer.valueOf(r.getAttribute("aa"));
        String sceneName = r.getAttribute("name");
        float cox, coy, coz, cdx, cdy, cdz;
        String[] camo = r.getAttribute("campos").split(",");
        String[] camd = r.getAttribute("camdir").split(",");
        cox = Float.valueOf(camo[0]);
        coy = Float.valueOf(camo[1]);
        coz = Float.valueOf(camo[2]);
        cdx = Float.valueOf(camd[0]);
        cdy = Float.valueOf(camd[1]);
        cdz = Float.valueOf(camd[2]);
        Camera cam = new Camera(cox, coy, coz, cdx, cdy, cdz);
        ns = new Scene(sceneName, w, h, aa, cam);
        NodeList children = r.getChildNodes();
        int size = children.getLength();
        for(int i = 1; i < size; i += 2){
            Node n = children.item(i);
            Element node = (Element) n;
            String name = n.getNodeName();
            if(name.equals("light")){
                float ox, oy, oz, dx, dy, dz, intensity, red, green, blue, ssize;
                int ls;
                String lName;
                Color color;
                lName = node.getAttribute("name");
                String[] origin = node.getAttribute("pos").split(",");
                String[] dir = node.getAttribute("dir").split(",");
                String[] rgb = node.getAttribute("color").split(",");
                ssize = Float.valueOf(node.getAttribute("size"));
                red = Float.valueOf(rgb[0]);
                green = Float.valueOf(rgb[1]);
                blue = Float.valueOf(rgb[2]);
                color = new Color(red, green, blue);
                ox = Float.valueOf(origin[0]);
                oy = Float.valueOf(origin[1]);
                oz = Float.valueOf(origin[2]);
                dx = Float.valueOf(dir[0]);
                dy = Float.valueOf(dir[1]);
                dz = Float.valueOf(dir[2]);
                intensity = Float.valueOf(node.getAttribute("intensity"));
                ls = Integer.valueOf(node.getAttribute("samples"));
                String lightType = node.getAttribute("type");
                Vector3D pos = new Vector3D(ox, oy, oz);
                Vector3D dr = new Vector3D(dx, dy, dz);
                if(lightType.equals("surface")){
                    SurfaceLight surf = new SurfaceLight(lName, color, pos, dr, intensity, ssize, ls);
                    ns.addPrimitive(surf);
                } else if(lightType.equals("point")){
                    PointLight pl = new PointLight(lName, color, pos, intensity);
                    ns.addPrimitive(pl);
                } 
            } else if(name.equals("box")){
                float ox, oy, oz, spec, diff, refl, hard, cr, cg, cb, refr, ior;
                float bh, bw, bd;
                String bname;
                String[] origin = node.getAttribute("pos").split(",");
                String[] color = node.getAttribute("color").split(",");
                ox = Float.valueOf(origin[0]);
                oy = Float.valueOf(origin[1]);
                oz = Float.valueOf(origin[2]);
                cr = Float.valueOf(color[0]);
                cg = Float.valueOf(color[1]);
                cb = Float.valueOf(color[2]);
                bname = node.getAttribute("name");
                spec = Float.valueOf(node.getAttribute("specular"));
                diff = Float.valueOf(node.getAttribute("diffuse"));
                hard = Float.valueOf(node.getAttribute("spechard"));
                refl = Float.valueOf(node.getAttribute("reflect"));
                bd = Float.valueOf(node.getAttribute("depth"));
                bw = Float.valueOf(node.getAttribute("width"));
                bh = Float.valueOf(node.getAttribute("height"));
                refr = Float.valueOf(node.getAttribute("refract"));
                ior = Float.valueOf(node.getAttribute("ior"));
                Color col = new Color(cr, cg, cb);
                Material m = new Material(col, spec, diff, hard, refl, refr, ior);
                Box b = new Box(bname, m, new Vector3D(ox, oy, oz), bw, bh, bd);
                ns.addPrimitive(b);
            } else if(name.equals("sphere")){
                float ox, oy, oz, spec, diff, refl, hard, esize, cr, cg, cb, refr, ior;
                String bname;
                String[] origin = node.getAttribute("pos").split(",");
                String[] color = node.getAttribute("color").split(",");
                ox = Float.valueOf(origin[0]);
                oy = Float.valueOf(origin[1]);
                oz = Float.valueOf(origin[2]);
                cr = Float.valueOf(color[0]);
                cg = Float.valueOf(color[1]);
                cb = Float.valueOf(color[2]);
                bname = node.getAttribute("name");
                spec = Float.valueOf(node.getAttribute("specular"));
                diff = Float.valueOf(node.getAttribute("diffuse"));
                hard = Float.valueOf(node.getAttribute("spechard"));
                refl = Float.valueOf(node.getAttribute("reflect"));
                esize = Float.valueOf(node.getAttribute("radius"));
                refr = Float.valueOf(node.getAttribute("refract"));
                ior = Float.valueOf(node.getAttribute("ior"));
                Color col = new Color(cr, cg, cb);
                Material m = new Material(col, spec, diff, hard, refl, refr, ior);
                Sphere s = new Sphere(bname, m, esize, new Vector3D(ox, oy, oz));
                ns.addPrimitive(s);
            } else if(name.equals("plane")){
                float ox, oy, oz, spec, diff, refl, hard, cr, cg, cb, refr, ior;
                float nx, ny, nz;
                String bname;
                String[] origin = node.getAttribute("pos").split(",");
                String[] color = node.getAttribute("color").split(",");
                String[] normal = node.getAttribute("normal").split(",");
                ox = Float.valueOf(origin[0]);
                oy = Float.valueOf(origin[1]);
                oz = Float.valueOf(origin[2]);
                nx = Float.valueOf(normal[0]);
                ny = Float.valueOf(normal[1]);
                nz = Float.valueOf(normal[2]);
                cr = Float.valueOf(color[0]);
                cg = Float.valueOf(color[1]);
                cb = Float.valueOf(color[2]);
                bname = node.getAttribute("name");
                spec = Float.valueOf(node.getAttribute("specular"));
                diff = Float.valueOf(node.getAttribute("diffuse"));
                hard = Float.valueOf(node.getAttribute("spechard"));
                refl = Float.valueOf(node.getAttribute("reflect"));
                refr = Float.valueOf(node.getAttribute("refract"));
                ior = Float.valueOf(node.getAttribute("ior"));
                Color col = new Color(cr, cg, cb);
                Material m = new Material(col, spec, diff, hard, refl, refr, ior);
                Plane pl = new Plane(bname, m, new Vector3D(ox, oy, oz), new Vector3D(nx, ny, nz));
                ns.addPrimitive(pl);
            }
        }
        ns.seal();
        return ns;
    }
}
