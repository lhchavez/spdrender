/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.primitives;

import spdrender.raytracer.base.*;

/**
 * Implementation of a plane primitive.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class Plane extends Primitive {
    private Vector3D n, p;
   
    /**
     * Creates a new instance of a plane primitive using the standard Point/Normal
     * representation.
     * @param name Name of the plane.
     * @param m Material of the plane.
     * @param pos A position vector of the plane.
     * @param normal Normal vector to the plane.
     */
    public Plane(String name, Material m, Vector3D pos, Vector3D normal){
        super();
        n = normal.normalize();
        p = pos;
        this.name = name;
        this.material = m;
        light = false;
    }
    
    public Vector3D getNormal(Vector3D pos){
        return n;
    }
    
    public double intersect(Ray r){
        double d = r.getDirection().dot(n);
        if(d != 0){
            double t = (p.dot(n) - r.getOrigin().dot(n))/d;
            if(t < 0){
                return Double.POSITIVE_INFINITY;
            }
            return t;
        } else return Double.POSITIVE_INFINITY;
    }
    
    /**
     * Retrieves the position of this plane.
     * @return
     */
    public Vector3D getPosition(){
        return p;
    }
    
    public int getType(){
        return Primitive.PLANE;
    }
    
    @Override
    public String toString(){
        return "Plane: name " + name + ", normal " + n + ", position " + p + "\nMaterial: " + material;
    }

}
