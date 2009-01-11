/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.parser;

/**
 * Custom exception for the Scene XML Parser.
 * @author Maximiliano Monterrubio Gutierrez
 */
public class SceneParserException extends Exception {
    private String m;
    
    /**
     * Creates a new instance with the specified error message.
     * @param message Error message to store.
     */
    public SceneParserException(String message){
        m = message;
    }

    @Override
    public String getMessage(){
        return "An error ocurred when trying to parse the scene file. " + m;
    }
}
