/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer;

import spdrender.raytracer.base.Vector3D;
import spdrender.FrameBufferWindow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class RayTracerTest {

    public RayTracerTest() {
    }

    /**
     * Test of render method, of class RayTracer.
     */
    @Test
    public void testRender() {
        System.out.println("render");
        
        Camera c1 = new Camera(new Vector3D(0,0,5), new Vector3D( 1, 1, 2).normalize()); // PPP
        Scene s1 = new Scene("Test Scene 1", 800, 600, 1, c1);
        FrameBufferWindow fb = new FrameBufferWindow(800, 600);
        s1.seal();
        RayTracer i1 = new RayTracer(s1, 1);
        i1.render(1, fb);
        fb.setVisible(true);
        try {
            Thread.sleep(10000);
        } catch(InterruptedException e){}
    }

}