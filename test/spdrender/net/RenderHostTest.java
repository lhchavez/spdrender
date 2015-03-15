/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.net;

import spdrender.parser.SceneParserException;
import spdrender.raytracer.LoadBalancer;

import java.io.*;
import java.net.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class RenderHostTest {
    private static File inputFile = new File("test/spdrender/net/scene_example.xml");
    private int totalPieces;
    
    private class ClientMock implements Runnable {
        private int clock, threads;
        
        public ClientMock(int threads, int clock){
            this.clock = clock;
            this.threads = threads;
        }
        
        public void run(){
            try {
                Socket s = new Socket("localhost", 1337);
                FileReader fr = new FileReader(inputFile);
                BufferedReader d = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
                int sceneLength = Integer.parseInt(d.readLine());
                assertEquals(sceneLength, inputFile.length());
                int f;
                while((f = fr.read()) != -1){
                    assertEquals(f, d.read());
                }
                pw.println(threads + "," + clock);
                String[] pieces = d.readLine().split(",");
                synchronized (RenderHostTest.this) {
                    RenderHostTest.this.totalPieces += pieces.length;
                }
                // Send a bogus number so that the server finishes.
                pw.println(-1);
                fr.close();
                d.close();
                pw.close();
                s.close();
            } catch(Exception e){
                fail("Exception ocurred inside the client mock: " + e.getMessage());
            }
        }
    }
    
    public RenderHostTest() {
        totalPieces = 0;
    }

    
    /**
     * Basic test for the Distributed Render server.
     */
    @Test
    public void testServer() throws IOException, SceneParserException, InterruptedException {
        Thread client1 = new Thread(new ClientMock(8, 1600)); // 8 Cores, 1.6 Ghz
        Thread client2 = new Thread(new ClientMock(2, 2200)); // 2 Cores, 2.2 Ghz
        
        Thread server = new Thread(new Runnable(){
            public void run(){
                try {
                    RenderHost rh = new RenderHost(null, inputFile, 1337, 2);
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
        Thread.sleep(100);
        client1.start();
        client2.start();
        server.join();
        assertEquals(LoadBalancer.getPieceCount(800, 600), totalPieces);
    }
}
