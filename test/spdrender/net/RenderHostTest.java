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
    private static String inputFile = "test/spdrender/net/scene_example.xml";
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
                FileInputStream fis = new FileInputStream(inputFile);
                InputStream in = s.getInputStream();
                PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
                int f;
                while((f = in.read()) != '\n'){
                    System.out.print((char)f);
                }
                System.out.println();
                while((f = fis.read()) != -1){
                    assertEquals(f, in.read());
                }
                pw.println(threads + "," + clock);
                BufferedReader d = new BufferedReader(new InputStreamReader(in));
                System.out.println("Calculating piece count...");
                RenderHostTest.this.totalPieces += d.readLine().split(",").length;
                pw.println(-1);
                in.close();
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
                    RenderHost rh = new RenderHost(null, new File(inputFile), 1337, 2);
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
        Thread.sleep(1000);
        assertEquals(LoadBalancer.getPieceCount(800, 600), totalPieces);
    }
}