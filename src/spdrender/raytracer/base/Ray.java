/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.base;

/**
 * A ray implementation used for the ray tracer algorithm.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class Ray {
    private Vector3D o, d;
    
    /** Creates a null ray located at the origin. */
    public Ray(){
        o = new Vector3D();
        d = new Vector3D();
    }
    
    /** Creates a new ray with the specified origin and the specified direction.
     * 
     * @param origin The origin of this ray.
     * @param direction A normalized vector who represents the direction of this ray.
     */
    public Ray(Vector3D origin, Vector3D direction){
        o = origin;
        d = direction;
    }
    
    /** Creates a new ray with the specified origin and direction coordinates.
     * <b>Note:</b>  The direction vector gets normalized in this constructor.
     * 
     * @param ox X-coordinate of the origin vector.
     * @param oy Y-coordinate of the origin vector.
     * @param oz Z-coordinate of the origin vector.
     * @param dx X-coordinate of the direction vector.
     * @param dy Y-coordinate of the direction vector.
     * @param dz Z-coordinate of the direction vector.
     */
    public Ray(float ox, float oy, float oz, float dx, float dy, float dz){
        o = new Vector3D(ox, oy, oz);
        d = new Vector3D(dx, dy, dz).normalize();
    }
    
    /** Retrieves the ray's origin.
     * 
     * @return A 3d vector representing the origin of this ray.
     */
    public Vector3D getOrigin(){
        return o;
    }
    
    /** Retrieves the ray's direction.
     * 
     * @return A 3d normalized vector representing the direction of this ray.
     */
    public Vector3D getDirection(){
        return d;
    }
    
    /** Sets the origin vector of this ray
     * 
     * @param origin The new origin of this ray.
     */
    public void setOrigin(Vector3D origin){
        o = origin;
    }
    
    /** Sets the direction vector of this ray
     * 
     * @param direction The new direction of this ray.  <b>Note:</b> The vector must be normalized.
     */
    public void setDirection(Vector3D direction){
        d = direction;
    }
    
    @Override
    public String toString(){
        return "Ray: " + o + ", " + d;
    }
}
