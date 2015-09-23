package net.owl_black.tovmgr;
import java.util.*;
/**
 * Write a description of interface qdv here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface ExportListener extends EventListener {
    /**
     *  Launch the export action.
     */
    public void fireExport();
}