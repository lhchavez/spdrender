/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author max
 */
public class LoadBalancerTest {
    private static int w, h;
    
    public LoadBalancerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        w = h = RayTracer.TILE_SIZE*5;
    }

    /**
     * Test of getPieceCount method, of class LoadBalancer.
     */
    @Test
    public void testGetPieceCount() {
        System.out.println("getPieceCount");
        int expResult = 25;
        int result = LoadBalancer.getPieceCount(w, h);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPieces method, of class LoadBalancer.
     */
    @Test
    public void testGetPieces() {
        System.out.println("getPieces");
        LoadBalancer instance = new LoadBalancer(w, h);
        int sum = 0;
        int expectedSum = 300;
        int[] result = instance.getPieces();
        for(int i = 0; i < result.length; ++i){
            System.out.println(result[i]);
            sum += result[i];
        }
        assertEquals(expectedSum, sum);
    }

    /**
     * Test of getUniformSegments method, of class LoadBalancer.
     */
    @Test
    public void testGetUniformSegments() {
        System.out.println("getUniformSegments");
        int jobs = 5;
        LoadBalancer instance = new LoadBalancer(w, h);
        int[][] result = instance.getUniformSegments(jobs);
        for(int i = 0; i < 5; ++i){
            System.out.println(result[i][0] + ", " + result[i][1]);
            assertEquals(i*5, result[i][0]);
            assertEquals((i+1)*5, result[i][1]);
        }
    }

    /**
     * Test of getProportionalSegments method, of class LoadBalancer.
     */
    @Test
    public void testGetProportionalSegments() {
        System.out.println("getProportionalSegments");
        int[][] nodeconfig = {
            { 4, 100 }, // 4 cores / 100 Mhz per core
            { 2, 150 }, // 2 cores / 150 Mhz per core
            { 1, 500 }  // 1 core  / 500 Mhz per core
        };
        LoadBalancer instance = new LoadBalancer(w, h);
        int[][] expResult = {
            { 0, 8  },
            { 8, 14 },
            { 14, 25 }
        };
        int[][] result = instance.getProportionalSegments(nodeconfig);
        for(int i = 0; i < result.length; ++i){
            for(int j = 0; j < result[i].length; ++j){
                assertEquals(expResult[i][j], result[i][j]);
            }
        }
    }

}