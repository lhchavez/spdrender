/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.base;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author max
 */
public class Vector3DTest {

    public Vector3DTest() {
    }

    /**
     * Test of cross method, of class Vector3D.
     */
    @Test
    public void testCross() {
        Vector3D v = new Vector3D(1.0,1.0,1.0);
        Vector3D instance = new Vector3D(0.0,1.0,0.0);
        Vector3D expResult = new Vector3D(1.0,0.0,-1.0);
        Vector3D result = instance.cross(v);
        assertEquals(true, expResult.equals(result));
    }

    /**
     * Test of dot method, of class Vector3D.
     */
    @Test
    public void testDot() {
        Vector3D v = new Vector3D(1.0,2.0,3.0);
        Vector3D instance = new Vector3D(3.0,2.0,1.0);
        double expResult = 10.0;
        double result = instance.dot(v);
        assertEquals(expResult, result, 1e-6);
    }

    /**
     * Test of add method, of class Vector3D.
     */
    @Test
    public void testAdd() {
        Vector3D v = new Vector3D(1.0,2.0,3.0);
        Vector3D instance = new Vector3D(0.0,-1.0,3.0);
        Vector3D expResult = new Vector3D(1.0,1.0,6.0);
        Vector3D result = instance.add(v);
        assertEquals(true, expResult.equals(result));
    }

    /**
     * Test of sub method, of class Vector3D.
     */
    @Test
    public void testSub() {
        Vector3D v = new Vector3D(1.0,5.0,2.0);
        Vector3D instance = new Vector3D(1.0,5.0,2.0);
        Vector3D expResult = new Vector3D();
        Vector3D result = instance.sub(v);
        assertEquals(true, expResult.equals(result));
    }

    /**
     * Test of inv method, of class Vector3D.
     */
    @Test
    public void testInv() {
        Vector3D instance = new Vector3D(1.0,5.0,3.0);
        Vector3D expResult = new Vector3D(-1.0, -5.0, -3.0);
        Vector3D result = instance.inv();
        assertEquals(true, expResult.equals(result));
    }

    /**
     * Test of scalarProd method, of class Vector3D.
     */
    @Test
    public void testScalarProd() {
        double c = 5.0;
        Vector3D instance = new Vector3D(6.0,4.0,2.0);
        Vector3D expResult = new Vector3D(30.0, 20.0, 10.0);
        Vector3D result = instance.scalarProd(c);
        assertEquals(true, expResult.equals(result));
    }

    /**
     * Test of norm method, of class Vector3D.
     */
    @Test
    public void testNorm() {
        Vector3D instance = new Vector3D(1.0,1.0,1.0);
        double expResult = Math.sqrt(3.0);
        double result = instance.norm();
        assertEquals(expResult, result, 1e-6);
    }

    /**
     * Test of sqrNorm method, of class Vector3D.
     */
    @Test
    public void testSqrNorm() {
        Vector3D instance = new Vector3D(3.0,4.0,5.0);
        double expResult = 50.0;
        double result = instance.sqrNorm();
        assertEquals(expResult, result, 1e-6);
    }

    /**
     * Test of normalize method, of class Vector3D.
     */
    @Test
    public void testNormalize() {
        Vector3D instance = new Vector3D(0.0,3.0,4.0);
        Vector3D expResult = new Vector3D(0.0,3.0/5.0,4.0/5.0);
        Vector3D result = instance.normalize();
        assertEquals(true, expResult.equals(result));
    }

    /**
     * Test of isNormalTo method, of class Vector3D.
     */
    @Test
    public void testIsNormalTo() {
        Vector3D v = new Vector3D(0.0,0.0,1.0);
        Vector3D instance = new Vector3D(1.0,0.0,0.0);
        boolean expResult = true;
        boolean result = instance.isNormalTo(v);
        assertEquals(expResult, result);
    }

    /**
     * Test of isCollinearTo method, of class Vector3D.
     */
    @Test
    public void testIsCollinearTo() {
        Vector3D v = new Vector3D(1.0,1.0,1.0);
        Vector3D instance = new Vector3D(5.0,5.0,5.0);
        boolean expResult = true;
        boolean result = instance.isCollinearTo(v);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Vector3D.
     */
    @Test
    public void testEquals() {
        Vector3D v = new Vector3D(1.0,4.0,3.0);
        Vector3D instance = new Vector3D(1.0,4.0,3.0);
        boolean expResult = true;
        boolean result = instance.equals(v);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Vector3D.
     */
    @Test
    public void testToString() {
        Vector3D instance = new Vector3D(6.0,5.0,2.0);
        String expResult = "<6.0,5.0,2.0>";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

}
