/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package spdrender.parser;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * XML Parse error handler for 3D Scene XML Parsing.
 * @author Maximiliano Monterrubio Gutierrez.
 */
public class SPDErrorHandler implements ErrorHandler {
    private boolean f;
    private String m;
    
    public SPDErrorHandler(){
        f = false;
        m = null;
    }

    /**
     * Evaluates if the parsing of the XML document failed.
     * @return <code>true</code> in case a failure occurs.
     */
    public boolean failed(){
        return f;
    }
    
    /**
     * Returns a descriptive error message in case of.
     * @return A string with a brief explanation of the error, <code>null</code> in case no error occurred.
     */
    public String getMessage(){
        return m;
    }
    
    public void fatalError(SAXParseException e){
        f = true;
        m = "FATAL: " + e.getMessage();
    }
    
    public void error(SAXParseException e){
        f = true;
        m = e.getMessage();
    }
    
    public void warning(SAXParseException e){
        f = true;
        m = "WARNING: " + e.getMessage();
    }
}
