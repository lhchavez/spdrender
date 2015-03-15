/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.net;

import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author max
 */
public class RenderNodeTest {
    private static String inputFile = "test/spdrender/net/scene_example.xml";
    
    public RenderNodeTest() {
    }

    /**
     * Test of renderAndSend method, of class RenderNode.
     */
    @Test
    public void testRenderAndSend() throws Exception {
        Thread server = new Thread(new Runnable(){
            public void run(){
                try {
                    RenderHost rh = new RenderHost(null, new File(inputFile), 1337, 1);
                    rh.startServer();
                    if(rh.errors()){
                        fail("And error ocurred in the server: " + rh.getErrorMsg());
                    }
                } catch(Exception e){
                    fail("Exception ocurre while trying to start the server: " + e.getMessage());
                }

            }
        });
        server.start();
        Thread.sleep(500);
        Thread t = new Thread(new Runnable(){
            public void run(){
                try {
                    RenderNode instance = new RenderNode("localhost", 1337, 2200, 2);
                    instance.renderAndSend();
                } catch(Exception e){
                    fail("Exception found: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        t.start();
        t.join();
        server.join();
    }
}
