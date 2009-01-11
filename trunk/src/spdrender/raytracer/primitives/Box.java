/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.primitives;

import spdrender.raytracer.base.*;

/**
 * Implementation of a cubic shape primitive.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class Box extends Primitive {
    private Vector3D p;
    private double w, d, h;
    public Box(String name, Material m, Vector3D pos, double width, double height, double depth){
        super();
        p = pos;
        this.name = name;
        this.material = m;
        light = false;
        w = width;
        d = depth;
        h = height;
    }
    
    public Vector3D getNormal(Vector3D pos){
        if(pos.x - p.x <= Vector3D.EPSILON){
            return new Vector3D(-1,0,0);
        } else if(Math.abs(pos.x - p.x - w) <= Vector3D.EPSILON){
            return new Vector3D(1,0,0);
        } else if(Math.abs(pos.y - p.y) <= Vector3D.EPSILON){
            return new Vector3D(0,-1,0);
        } else if(Math.abs(pos.y - p.y - h) <= Vector3D.EPSILON){
            return new Vector3D(0,1,0);
        } else if(Math.abs(pos.z - p.z) <= Vector3D.EPSILON){
            return new Vector3D(0,0,-1);
        } else {
            return new Vector3D(0,0,1);
        }
    }
    
    /**
     * Retrieves the position of the box.
     * @return A 3D vector representing the lower left corner of this box.
     */
    public Vector3D getPosition(){
        return p;
    }
    
    public double intersect(Ray r){
        Vector3D ro = r.getOrigin();
        Vector3D rd = r.getDirection();
        
        Vector3D v1 = this.p;
        Vector3D v2 = new Vector3D(p.x + w, p.y + h, p.z + d);
        
        double x0, x1, y0, y1, z0, z1;
        double t, t0, t1;
        
        x0 = Math.abs(rd.x) > Vector3D.EPSILON ? (v1.x - ro.x)/rd.x : -1.0f;
        x1 = Math.abs(rd.x) > Vector3D.EPSILON ? (v2.x - ro.x)/rd.x : -1.0f;
        y0 = Math.abs(rd.y) > Vector3D.EPSILON ? (v1.y - ro.y)/rd.y : -1.0f;
        y1 = Math.abs(rd.y) > Vector3D.EPSILON ? (v2.y - ro.y)/rd.y : -1.0f;
        z0 = Math.abs(rd.z) > Vector3D.EPSILON ? (v1.z - ro.z)/rd.z : -1.0f;
        z1 = Math.abs(rd.z) > Vector3D.EPSILON ? (v2.z - ro.z)/rd.z : -1.0f;
        
        if(rd.x < 0){
            t = x0;
            x0 = x1;
            x1 = t;
        }
        if(rd.y < 0){
            t = y0;
            y0 = y1;
            y1 = t;
        }
        if(rd.z < 0){
            t = z0;
            z0 = z1;
            z1 = t;
        }
        
        if(x0 > y0)
            t0 = x0;
        else 
            t0 = y0;
        
        if(z0 > t0)
            t0 = z0;
        
        if(x1 < y1)
            t1 = x1;
        else
            t1 = y1;
        if(z1 < t1)
            t1 = z1;
        
        if(t0 < t1 && t0 > 0.0f)
            return t0 - Vector3D.EPSILON;
        return Double.POSITIVE_INFINITY;
                    
        
    }
    
    public int getType(){
        return Primitive.BOX;
    }
    
    @Override
    public String toString(){
        return "Box: name " + name + ", position " + p + ", width " + w + ", height " + h + " depth " + d + "\nMaterial: " + material;
    }
}
