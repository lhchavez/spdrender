/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.primitives;

import spdrender.raytracer.base.*;
/**
 * Abstract class for scene primitives.
 * @author Maximiliano Monterrubio Gutierrez
 */
public abstract class Primitive {
    /** Represents a Box primitive. */
    public static final int BOX       = 0;
    /** Represents an Sphere primitive. */
    public static final int SPHERE     = 1;
    /** Represents a Plane primitive. */
    public static final int PLANE      = 2;
    /** Represents a Triangle primitive. */
    public static final int TRIANGLE   = 3;
    /** Represents a Surface Light primitive. */
    public static final int SURFACE    = 4;
    /** Represents a Sunlight primitive. */
    public static final int SUN        = 5;
    /** Represents a Point Light primitive. */
    public static final int POINT      = 6;
    
    /** Symbolic constant that represents a ray is inside a primitive. */
    public static final byte INSIDE  = -1;  
    /** Symbolic constant that represents a ray doesn't hit a primitive. */
    public static final byte MISS    =  0;     
    /** Symbolic constant that represents a ray hits a primitive. */
    public static final byte HIT     =  1;      
    
    /** The name of the primitive */
    protected String name;        
    /** Is this primitive a light? */
    protected boolean light;      
    /** The material associated with this primitive */
    protected Material material;  
    
    /** Specifies if this primitive intersects the given ray at the specified distance.
     * 
     * @param ray Ray to test.
     * @return A <code>float</code> value representing the intersection distance.
     * In case the object doesn't intersect the day, the returning value must be
     * <code>Double.POSITIVE_INFINITY</code>.
     */
    public abstract double intersect(Ray ray);
    
    /** Return the normal of this primitive for the given position
     * @param pos Position to test.
     * @return A normalized normal vector relative to this primitive.
     */
    public abstract Vector3D getNormal(Vector3D pos);
    
    /** Returns an integer representing the type of primitive.
     * 
     * @return A unique integer who identifies the type of primitive.
     */
    public abstract int getType();
    
    /** Sets the name of the primitive
     * 
     * @param name Name of the primitive.
     */
    public void setName(String name){
        this.name = name;
    }
    
    /** Sets the material associated to this primitive
     * 
     * @param m Material that will be used for this primitive.
     */
    public void setMaterial(Material m){
        material = m;
    }
    
    /** Specifies if this primitive is a light source
     * 
     * @return <code>true</code>In case this primitive represents a light source.
     */
    public boolean isLight(){
        return light;
    }
    
    /** Retrieves the name of this primitive
     * 
     * @return The name of the primitive.
     */
    public String getName(){
        return name;
    }
    
    /** Retrieves the material who shades this primitive.
     * 
     * @return The associated material to this primitive.
     */
    public Material getMaterial(){
        return material;
    }
}
