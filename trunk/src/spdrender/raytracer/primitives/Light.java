/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.primitives;

import spdrender.raytracer.base.*;


/**
 * Light abstract class.  Every lighting primitive must inherit this class.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public abstract class Light extends Primitive {
    Color c;
    Vector3D p, d;
    double intensity, size;
    int samples;
    
    /**
     * Creates a new Light primitive.
     * @param name Name of the instance.
     * @param color Light color.
     * @param pos Light position.
     * @param dir Light direction (if applicable).
     * @param intensity A floating point value representing the light intensity.
     * @param samples Number of samples to take for Monte Carlo integration.
     */
    public Light(String name, Color color, Vector3D pos, Vector3D dir, double intensity, int samples){
        super();
        c = color;
        p = pos;
        d = dir;
        this.name = name;
        this.material = null;
        light = true;
        this.intensity = intensity;
        this.samples = samples;
    }

    /**
     * Retrieves the number of samples to take from this light.
     */
    public int getSampleCount(){
        return samples;
    }
    
    /**
     * Retrieves the position of the light source.
     */
    public Vector3D getPosition(){
        return p;
    }

    public Vector3D getNormal(Vector3D v){
        return null;
    }

    /**
     * Retrieves the instance light color.
     */
    public Color getColor(){
        return c;
    }

    /**
     * Retrieves the instance light intensity.
     * @return
     */
    public double getIntensity(){
        return intensity;
    }
    
     @Override
    public String toString(){
        return "name " + name + ", direction " + p + ", direction " + d + ", intensity " + intensity + ", color " + c;
    }
}
