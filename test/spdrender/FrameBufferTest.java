/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender;

import java.awt.image.BufferedImage;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class FrameBufferTest {
    public FrameBufferTest() {
    }
    
     /**
     * Test for writing a section of the framebuffer asynchronously. 
     */
    @Test
    public void testWriteSection() throws InterruptedException {
        FrameBufferWindow instance = new FrameBufferWindow(10,10);
        double[][][] sect = new double[5][5][3];
        for(int i = 0; i < 5; ++i){
            for(int j = 0; j < 5; ++j){
                sect[i][j][0] = sect[i][j][1] = sect[i][j][2] = 0.5;
            }
        }
        instance.writeSection(sect, 5, 5, 5, 5);
        BufferedImage img = instance.getRender();
        for(int i = 0; i < 5; ++i){
            for(int j = 0; j < 10; ++j){
                assertEquals(0xFF000000, img.getRGB(i, j));
            }
        }
        for(int i = 5; i < 10; ++i){
            for(int j = 0; j < 5; ++j){
                assertEquals(0xFF000000, img.getRGB(i, j));
            }
        }
        for(int i = 5; i < 10; ++i){
            for(int j = 5; j < 10; ++j){
                assertEquals(0xFF7F7F7F, img.getRGB(i, j));
            }
        }
    }

}
