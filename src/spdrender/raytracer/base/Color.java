
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.base;

/**
 * Class representantion for a floating point RGB color component.
 * @author Maximiliano Monterrubio Gutierrez
 */
public class Color {
    private Vector3D c;
   
    /** Creates a duplicate of the giving color instance.
     * 
     * @param c Instance to copy.
     */
    public Color(Color c){
        this.c = new Vector3D(c.c.x, c.c.y, c.c.z);
    }
    
    /** Creates a new color completely desaturated (black).  */
    public Color(){
        c = new Vector3D();
    }
    
    /** Creates a new color with the specified components.
     * 
     * @param r The red component.
     * @param g The green component.
     * @param b The blue component.
     */
    
    public Color(double r, double g, double b){
        c = new Vector3D(r,g,b);
    }
    
    /** Retrieves the red component of this color.
     * 
     * @return double in [0,1] representing the red component.
     */
    public double getR(){
        return c.x;
    }
    
    /** Retrieves the green component of this color.
     * 
     * @return double in [0,1] representing the green component.
     */
    public double getG(){
        return c.y;
    }
    
    /** Retrieves the blue component of this color.
     * 
     * @return double in [0,1] representing the blue component.
     */
    public double getB(){
        return c.z;
    }
    
    /** Sets the red component of this color.
     * 
     * @param r The new red component intensity.  It must be between [0,1].
     */
    public void setR(double r){
        c.x = r;
    }
    
    /** Sets the green component of this color.
     * 
     * @param g The new green component intensity.  It must be between [0,1].
     */
    public void setG(double g){
        c.y = g;
    }
    
    /** Sets the blue component of this color.
     * 
     * @param b The new blue component intensity.  It must be between [0,1].
     */
    public void setB(double b){
        c.z = b;
    }

    /** Multiplies this color with another one and stores the result in the calling instance.
     * 
     * @param c The color to multiply with the actual instance.
     * @return A new color resulting of multiplying the components this instance and another color.
     */
    public void multiply(Color c){
        this.c.x *= c.c.x;
        this.c.y *= c.c.y;
        this.c.z *= c.c.z;
    }
    
    /** Adds the components of the calling instance and another color.
     * 
     * @param c Instance to add.
     */
    public void add(Color c){
        this.c.x += c.c.x;
        this.c.y += c.c.y;
        this.c.z += c.c.z;
    }
    
    /** Scales the intensity of the color by the given factor.
     * 
     * @param factor A <code>double</code> between 0.0f and 1.0f representing the scale factor.
     */
    public void scale(double factor){
        this.c = this.c.scalarProd(factor);
    }
    
    @Override
    public String toString(){
        return "Color: " + c;
    }
}
