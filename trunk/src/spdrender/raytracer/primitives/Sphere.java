/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.primitives;

import spdrender.raytracer.base.*;

/**
 * Simple implementation of a spherical primitive.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class Sphere extends Primitive {
    private Vector3D p;
    private double r;
    
    /**
     * Creates a new instance of a sphere primitive.
     * @param name Name of the instance.
     * @param m Sphere material.
     * @param radius Radius of the sphere.
     * @param pos Center of the sphere.
     */
    public Sphere(String name, Material m, double radius, Vector3D pos){
        super();
        p = pos;
        r = radius;
        this.name = name;
        this.material = m;
        light = false;
    }
    
    public Vector3D getNormal(Vector3D pos){
        return pos.sub(p).normalize();
    }
    
    public double intersect(Ray r){
        
        Vector3D v = r.getOrigin().sub(p);
        double b = 2.0f*v.dot(r.getDirection());
        double c = v.sqrNorm() - this.r*this.r;
        double disc = b*b - 4*c;
        if(disc < 0){
            return Double.POSITIVE_INFINITY;
        }
        disc = (double)(Math.sqrt(disc));
        double x1 = disc*-1.0f - b;
        double x2 = disc - b;
        x1 /= 2.0f;
        x2 /= 2.0f;
        if(x1 < 0.0f && x2 < 0.0f){
            return Double.POSITIVE_INFINITY;
        }
        if(x1 > 0.0f && x2 > 0.0f){
            return Math.min(x1, x2);
        } else {
            return Math.max(x2, x1);
        }
    }
    
    public int getType(){
        return Primitive.SPHERE;
    }

    /**
     * Retrieves the radius of the sphere.
     * @return
     */
    public double getRadius(){
        return r;
    }
    
    @Override
    public String toString(){
        return "Sphere: name " + name + ", position " + p + ", radius " + r + "\nMaterial: " + material;
    }
}
