/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.raytracer.base;

/**
 * Class implementation for a material in a 3D Scene.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class Material {
    /** The specular component of this material. */
    public float specular; 
    /** The diffuse component of this material. */
    public float diffuse;  
    /** The specular hardness of this material. */
    public float spechard; 
    /** The reflectivity of this material. */
    public float reflect;  
    /** The amount of refraction of the material. */
    public float refract;
    /** The index of refraction of the material. */
    public float ior;
    /** The color of this material. */
    public Color color;    
    
    /** Creates a completely dark material.
     */
    public Material(){
        specular = diffuse = spechard = reflect = refract = 0;
        ior = 1.0f;
        color = new Color();
    }
    
    /** Creates a new material with the specified parameters.
     * 
     * @param color The color of the material.
     * @param specular The specular light reflectance of the material.
     * @param diffuse The diffuse light reflectance of the material.
     * @param spechard The hardness of the specular component of the material. (Higher values gives smaller, more
     * concentrated specular components).
     * @param reflect The 'mirror' reflectivity of the material.
     */
    public Material(Color color, float specular, float diffuse, float spechard, float reflect, float refract, float ior){
        this.color = color;
        this.specular = specular;
        this.spechard = spechard;
        this.reflect = reflect;
        this.diffuse = diffuse;
        this.refract = refract;
        this.ior = ior;
    }
    
    @Override
    public String toString(){
        return "Material: color " + color + ", specular " + specular + ", diffuse " + diffuse + 
                ", hardness " + spechard + ", reflectivity " + reflect + ", refraction " + refract + ", ior " + ior;
    }
}
