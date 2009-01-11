/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer;

import spdrender.raytracer.primitives.*;
import java.util.LinkedList;

/**
 * Class container for a 3D Scene.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class Scene {
    private int w, h, aa;
    private LinkedList<Primitive> objs;
    private String name;
    private Camera c;
    private boolean sealed;
    private Primitive[] primitives;
    /**
     * Creates a new instance of a 3D Scene.
     * @param name Name of the Scene.
     * @param width Width of the rendered image in pixels.
     * @param height Height of the rendered image in pixels.
     * @param aaPasses Number of the oversampling passes used for antialiasing.
     * @param camera The representation of the position and direction of the camera.
     */
    public Scene(String name, int width, int height, int aaPasses, Camera camera){
        w = width;
        h = height;
        aa = aaPasses;
        objs = new LinkedList<Primitive>();
        this.name = name;
        c = camera;
        sealed = false;
        primitives = null;
    }
    
    /**
     * Returns the number of primitives stored in this scene.
     * @return Number of primitives.
     */
    public int primitiveCount(){
        if(sealed){
            return primitives.length;
        } else {
            return objs.size();
        }
    }
    
    /**
     * Retrieves the camera of this scene.
     * @return An object representation of the camera of this scene.
     */
    public Camera getCamera(){
        return c;
    }
    
    /**
     * Adds a new primitive to the actual scene.
     * @param p Primitive to add.
     * @throws IllegalStateException If the scene has been already sealed.
     */
    public void addPrimitive(Primitive p) throws IllegalStateException {
        if(!sealed){
            objs.add(p);
        } else throw new IllegalStateException("The scene has been already sealed");
    }
    
    /** Returns an array of all the primitives on this scene.
     *  NOTE: The scene MUST be sealed before you can get the primitive array.
     *  @throws IllegalStateException In case the scene has not been sealed.
     */
    public Primitive[] getPrimitives() throws IllegalStateException {
        if(!sealed){
            throw new IllegalStateException("Cannot return a primitive array when the scene has not been sealed");
        }
        return primitives;
    }
    
    /**
     * Seals this scene so you can no further add primitives.
     */
    public void seal(){
        sealed = true;
        primitives = new Primitive[objs.size()];
        objs.toArray(primitives);
        objs.clear();
        objs = null;
    }
    
    /**
     * Determines if this Scene instance is sealed or not.
     * @return <code>true</code> if the instance is sealed.
     */
    public boolean isSealed(){
        return sealed;
    }
    
    /**
     * Returns the width of the image in pixels.
     */
    public int getRenderWidth(){
        return w;
    }
    
    /**
     * Returns the height of the image in pixels.
     */
    public int getRenderHeight(){
        return h;
    }
    
    /**
     * Retrieves the number of oversampling passes per pixel.
     */
    public int getAAPasses(){
        return aa;
    }

    /** 
     * Retrieves the name of the scene.
     */
    public String getSceneName(){
        return name;
    }
    
    @Override
    public String toString(){
        return "Scene: name " + name + ", width " + w + ", height " + h + " aapases " + aa + "\n" + c;
    }
}
