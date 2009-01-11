/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.parser;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import spdrender.raytracer.primitives.Primitive;

import org.junit.Test;
import static org.junit.Assert.*;
import spdrender.raytracer.Scene;

/**
 * Unit test for the XML 3D Scene Parser.
 * @author Maximiliano Monterrubio Gutierrez
 */
public class SceneParserTest {

    public SceneParserTest() {
    }

    /**
     * Test of parseScene method, of class SceneParser.
     */
    @Test
    public void testParseScene() throws IOException, SceneParserException {
        System.out.println("parseScene");
        SceneParser instance = new SceneParser(new File("test/spdrender/parser/scene_example.xml"));
        Scene result = instance.parseScene();
        assertEquals(1, result.getAAPasses());
        assertEquals(600, result.getRenderHeight());
        assertEquals(800, result.getRenderWidth());
        assertEquals("TestScene", result.getSceneName());
        Primitive[] prims = result.getPrimitives();
        System.out.println(result);
        for(int i = 0; i < prims.length; ++i){
            System.out.println(prims[i]);
        }
    }

}