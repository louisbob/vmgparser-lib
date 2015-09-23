package net.owl_black.tovmgr;
import java.io.UnsupportedEncodingException;
import java.util.*;
/**
 * Write a description of interface qdv here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface OpenWListener extends EventListener {
    /**Action performed when the user has opened his files
     * 
     */
    public void fireOpened();
}