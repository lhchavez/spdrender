/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.base;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class TransformMatrixTest {

    public TransformMatrixTest() {
    }

    /**
     * Test of invert method, of class TransformMatrix.
     */
    @Test
    public void testInvert() {
        System.out.println("invert");
        Vector3D v = new Vector3D(1.0f, 0.0f, 0.0f);
        Vector3D w = new Vector3D(3.0f, 5.0f, 0.0f);
        Vector3D x = new Vector3D(3.0f, 6.0f, 9.0f);
        Vector3D vect = new Vector3D(5.0f, 17.0f, 6.5f);
        TransformMatrix instance = new TransformMatrix(v,w,x);
        TransformMatrix inv = instance.invert();
        Vector3D vect2 = instance.apply(vect);
        Vector3D ret = inv.apply(vect2);
        assertEquals(true, ret.equals(vect));
    }

    /**
     * Test of apply method, of class TransformMatrix.
     */
    @Test
    public void testApply() {
        System.out.println("apply");
        Vector3D v = new Vector3D(1,3,2);
        TransformMatrix instance = new TransformMatrix(
                new Vector3D(1,1,1),
                new Vector3D(1,1,1),
                new Vector3D(1,1,1));
        Vector3D expResult = new Vector3D(6,6,6);
        Vector3D result = instance.apply(v);
        assertEquals(true, expResult.equals(result));
    }

    /** Test for compose method.
     * 
     */
   @Test
   public void testCompose(){
       System.out.println("compose");
       Vector3D v = new Vector3D(1.0f, 3.0f, 3.0f);
       Vector3D w = new Vector3D(0.0f, 5.0f, 6.0f);
       Vector3D x = new Vector3D(0.0f, 0.0f, 9.0f);
       TransformMatrix t = new TransformMatrix(v, w, x);
       TransformMatrix i = t.invert();
       TransformMatrix identity = new TransformMatrix(
               new Vector3D(1,0,0),
               new Vector3D(0,1,0),
               new Vector3D(0,0,1));
       TransformMatrix c = t.compose(i);
       assertEquals(true, c.equals(identity));
   }
}