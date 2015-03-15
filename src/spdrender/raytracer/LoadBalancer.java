/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer;

import java.util.Random;

/**
 * Simple load balancer engine to partition the render job in order to distribute over local or
 * remote threads.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class LoadBalancer {
    private int[] pieces;
    private int w, h;
    
    /**
     * Returns the number of render tiles for the given image size.
     * @param width The width in pixels of the framebuffer.
     * @param height The height in pixels of the frambuffer.
     * @return Number of tiles needed to render the scene.
     */
    public static int getPieceCount(int width, int height){
        return getTileCount(width)*getTileCount(height);
    }
    
    /** 
     * Returns the number of tiles needed to fill the specified amount of pixels.
     * @param size Number of pixels to fill.
     * @return Number of tiles.
     */
    public static int getTileCount(int size){
        int hs;
        hs = size % RayTracer.TILE_SIZE == 0 ? 0 : 1;
        hs += size / RayTracer.TILE_SIZE;
        return hs;
    }
    
    /**
     * Creates a new load balancer with the specified image size.
     * @param width The width in pixels.
     * @param height The height in pixels.
     */
    public LoadBalancer(int width, int height){
        w = width;
        h = height;
        int hs = getTileCount(width);
        int vs = getTileCount(height);
        pieces = new int[hs*vs];
        for(int i = 0; i < pieces.length; ++i){
            pieces[i] = i;
        }
        // Perform a Knuth shuffle on the pieces.
        Random rand = new Random();
        for(int i = pieces.length - 1; i >= 0; --i){
            int r = rand.nextInt(i + 1);
            int t = pieces[i];
            pieces[i] = pieces[r];
            pieces[r] = t;
        }
    }
    
    /** 
     * Retrieves a randomized list of tile id's to render.  
     * Use it to consense a number of nodes with the same randomized list.
     * @return A randomized list of tile id's.
     */
    public int[] getPieces(){
        return pieces;
    }
    
    /**
     * Sets an already generated list of tile ids to this load balancer.
     * @param pieces List of tile ids.
     */
    public void setPieces(int[] pieces){
        this.pieces = pieces;
    }
    
    /**
     * Returns a list of intervals with a uniform load each.
     * @param jobs Number of intervals.
     * @return A list of intervals.  The first index of the array is the i-th interval and the second
     * index has is 0 for the start of the interval and 1 for the end of the interval.
     */
    public int[][] getUniformSegments(int jobs){
        int[][] segments = new int[jobs][2];
        int dx = pieces.length/jobs;
        int i;
        for(i = 0; i < jobs; i++){
            segments[i][0] = dx*i;
            segments[i][1] = dx*(i+1);
        }
        segments[i-1][0] = dx*(i-1);
        segments[i-1][1] = pieces.length;
        return segments;
    }
    
    /**
     * Returns a proportional load intervals for the stored tile set.
     * @param nodeconfig The configuration of the nodes.  The first entry of the array represents 
     * the node identifier, and the second index: 0 for the number of threads to use for that node, and
     * 1 for the processor speed in Mhz.
     * @return
     */
    public int[][] getProportionalSegments(int[][] nodeconfig){
        int offset = 0;
        int power = 0;
        int[][] segments = new int[nodeconfig.length][2];
        
        int pc = LoadBalancer.getPieceCount(w, h);
        for(int i = 0; i < nodeconfig.length; ++i){
            power += nodeconfig[i][0] * nodeconfig[i][1];
        }
        for(int i = 0; i < nodeconfig.length - 1; ++i){
            int segmentCount = (nodeconfig[i][0] * nodeconfig[i][1] * pc)/power;
            segments[i][0] = offset;
            offset += segmentCount;
            segments[i][1] = offset;
        }
        
        segments[segments.length - 1][0] = offset;
        segments[segments.length - 1][1] = pc;
        
        return segments;
    }
}
