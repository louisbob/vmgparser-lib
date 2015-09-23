package net.owl_black.tovmgr;
import java.util.*;
/**
 * Write a description of interface eafe here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface ListClickListener extends EventListener {
	
    /** Method called by the contactGUI class, to refresh the conversation
     * when another phone number is selected
     * @param phoneNumber The phone number corresponding to sms to display.
     */
    public void update(String phoneNumber);
}