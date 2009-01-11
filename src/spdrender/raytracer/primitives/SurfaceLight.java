/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.primitives;
import spdrender.raytracer.base.*;
/**
 * Multisampled unidirectional surface light implementation.
 * @author Maximiliano Monterrubio Gutierrez
 */
public class SurfaceLight extends Light {
    Vector3D hd, vd, corner;
    
    /**
     * Creates a new instance of a surface light.
     * @param name Name of the instance.
     * @param color Light color.
     * @param pos Position of the upper left corner of the light plane.
     * @param dir Light direction.
     * @param intensity Light source intensity.
     * @param size Size of the light plane.
     * @param samples Number of samples to take for the Monte Carlo algorithm.
     */
    public SurfaceLight(String name, Color color, Vector3D pos, Vector3D dir, double intensity, double size, int samples){
        super(name, color, pos, dir, intensity, samples);
        dir = dir.normalize();
        Vector3D leftBorder = new Vector3D(size/2, size/2, 0);
        Vector3D rightBorder = new Vector3D(-1.0f*size/2, size/2, 0);
        Vector3D bottomlBorder = new Vector3D(size/2, -1.0f*size/2, 0);
        Vector3D bottomrBorder = new Vector3D(-1.0f*size/2, -1.0f*size/2, 0);
        
        Vector3D cw = d;
        Vector3D up = new Vector3D(0,1,0);
        Vector3D cu = up.cross(cw).normalize();
        Vector3D cv = cu.cross(cw.inv());
        
        TransformMatrix tm = new TransformMatrix(cu, cv, cw);
        tm = tm.transpose();
        
        leftBorder = tm.apply(leftBorder);
        rightBorder = tm.apply(rightBorder);
        bottomlBorder = tm.apply(bottomlBorder);
        bottomrBorder = tm.apply(bottomrBorder);
        
        leftBorder = leftBorder.add(p);
        rightBorder = rightBorder.add(p);
        bottomlBorder = bottomlBorder.add(p);
        bottomrBorder = bottomrBorder.add(p);
        
        hd = rightBorder.sub(leftBorder);
        vd = bottomlBorder.sub(leftBorder);
        
        corner = bottomrBorder;
    }
    
    public double intersect(Ray r){
        return Double.POSITIVE_INFINITY;
    }

    public int getType(){
        return Primitive.SURFACE;
    }
    
    /**
     * Retrieves the upper left corner of the square light source.
     */
    public Vector3D getCorner(){
        return corner;
    }
    
    /**
     * Returns the direction of the Horizontal side of the light plane.
     */
    public Vector3D getHSide(){
        return hd;
    }
    
    /** 
     * Retrieves the direction of the vertical side of the light plane.
     */
    public Vector3D getVSide(){
        return vd;
    }
    
    @Override
    public String toString(){
        return "Surface light: " + super.toString() + " size " + size;
    }
}
