/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.primitives;
import spdrender.raytracer.base.*;

/**
 * Single point omnidirectional light implementation.
 * @author Maximiliano Monterrubio Gutierrez
 */
public class PointLight extends Light {
    
    /**
     * Creates a new instance of a punctual light source.
     * @param name Name of the instance.
     * @param color Light color.
     * @param pos Light source position.
     * @param intensity Light source intensity.
     */
    public PointLight(String name, Color color, Vector3D pos, double intensity){
        super(name, color, pos, null, intensity, 1);
    }
    
    public double intersect(Ray r){
        return Double.POSITIVE_INFINITY;
    }
    
    public int getType(){
        return Primitive.POINT;
    }
    
    @Override
    public String toString(){
        return "Point Light: " + super.toString();
    }
}

