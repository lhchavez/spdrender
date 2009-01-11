/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.base;

/**
 * Simple floating point representation for a 3D vector.
 * @author Maximiliano Monterrubio Gutierrez
 */
public class Vector3D {
    /** Represent a threshold value to evaluate 2 vectors for 
     * equality.
     */
    public static double EPSILON = 1e-10;
    /** The x coordinate of this vector. */
    public double x; 
    /** The y coordinate of this vector. */
    public double y; 
    /** The z coordinate of this vector. */
    public double z; 
    
    /** Creates a new null vector (0,0,0) */
    public Vector3D(){
        x = y = z = 0.0;
    }
    
    /** Creates a new vector with the specified coordinates
     * 
     * @param x The x coordinate of this vector.
     * @param y The y coordinate of this vector.
     * @param z The z coordinate of this vector.
     */
    public Vector3D(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /** Returns the cross product between this vector and <code>v</code>
     * 
     * @param v Another vector.
     * @return The cross product between them.
     */
    public Vector3D cross(Vector3D v){
        return new Vector3D(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - v.x*y);
    }
    
    /** Calculates the dot product of this vector and another one.
     * 
     * @param v Another vector.
     * @return The cross product as a double.
     */
    public double dot(Vector3D v){
        return x * v.x + y * v.y + z * v.z;
    }
    
    /** Adds this vector with another one
     * 
     * @param v The other vector.
     * @return The sum of this vector and v.
     */
    public Vector3D add(Vector3D v){
        return new Vector3D(x + v.x, y + v.y, z + v.z);
    }
    
    /** Subtracts this vector and another one.
     * 
     * @param v The other vector.
     * @return this - v.
     */
    public Vector3D sub(Vector3D v){
        return this.add(v.inv());
    }
    
    /** Inverts the direction of this vector (i.e. multiplies it by -1).
     * 
     * @return <code>-1.0f * v</code>
     */
    public Vector3D inv(){
        return this.scalarProd(-1.0f);
    }
    
    /** Computes the scalar product of a double against this vector
     * 
     * @param c A constant factor as a double.
     * @return The scaled vector.
     */
    public Vector3D scalarProd(double c){
        return new Vector3D(x * c, y * c, z * c);
    }
    
    /** Computes the vector norm of this instance.
     * 
     * @return The euclidean norm of this vector.
     */
    public double norm(){
        return (double) Math.sqrt(this.sqrNorm());
    }
    
    /** Returns de squared norm of this instance
     * 
     * @return The squared euclidean norm of this vector.
     */
    public double sqrNorm(){
        return this.dot(this);
    }
    
    /** Returns a vector with the same direction with a norm equal to 1.
     * 
     * @return Normalized vector.
     */
    public Vector3D normalize(){
        double n = norm();
        if(n != 0.0){
            return this.scalarProd(1/n);
        } else {
            return new Vector3D();
        }
    }
    
    /** Tests if this vector is normal to anohter.
     * 
     * @param v The other vector
     * @return <code>true</code> in case vectors are normal.
     */
    public boolean isNormalTo(Vector3D v){
        return this.dot(v) == 0.0f;
    }
    
    /** Tests if this vector is collinear with another one.
     * 
     * @param v The other vector
     * @return <code>true</code> in case both vectors have the same direction.
     */
    public boolean isCollinearTo(Vector3D v){
        Vector3D c = this.cross(v);
        return c.x == c.y && c.y == c.z && c.z == 0.0f;
    }
    
    /** Tests if another vector is equal to this instance.
     * 
     * @param v The other vector
     * @return <code>true</code> in case both vectors match coordinates.
     */
    public boolean equals(Vector3D v){
        double d1 = Math.abs(this.x - v.x);
        double d2 = Math.abs(this.y - v.y);
        double d3 = Math.abs(this.z - v.z);
        return d1 < Vector3D.EPSILON && d2 < Vector3D.EPSILON && d3 < Vector3D.EPSILON;
    }
    
    @Override
    public String toString(){
        return "<" + this.x + "," + this.y + "," + this.z + ">";
    }
}
