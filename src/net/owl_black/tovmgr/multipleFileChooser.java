package net.owl_black.tovmgr;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;
//For arraylist
import java.util.*;

/**
 * This class is used for opening VMG files. When you instantiate this class, a window will be opened
 * and prompt the user to select VMG files.
 * 
 * @author Louisbob
 * @version 26/06/2012 - v0.3
 */
public class multipleFileChooser
{
    // instance variables
    JFileChooser openDiag = new JFileChooser();
    File currentDir;
    int isSelected = 0;

    /**
     * Constructor for objects of class fileChooser
     */
    public multipleFileChooser(String winName, File setDir)
    {
        //Define file extensions that can be opened
        FileFilter vmg = new FiltreSimple("VMG files (*.vmg)",".vmg");
        
        //Allow multiple file selection
        openDiag.setMultiSelectionEnabled(true);
        
        //Add the VMG extension
        openDiag.addChoosableFileFilter(vmg);
        
        //Set vmg file to default filter
        openDiag.setFileFilter(vmg);
        
        //Add name to the titlebar
        openDiag.setDialogTitle(winName); 
        
        //Open the latest directory
        openDiag.setCurrentDirectory(setDir);
        
        isSelected = openDiag.showOpenDialog(null);
    }
    /**
     * As smsDB of this object is private & static, this method reset the database.
     * 
     * @return     void
     */
    public void openDiag() {
        FileFilter vmg = new FiltreSimple("VMG files (*.vmg)",".vmg");
        
        //Allow multiple file selection
        openDiag.setMultiSelectionEnabled(true);
        
        //Add the VMG extension
        openDiag.addChoosableFileFilter(vmg);
        
        //Set vmg file to default filter
        openDiag.setFileFilter(vmg);
        
        isSelected = openDiag.showOpenDialog(null);
    }
        
    /** This method allows you to get the list of files that have been opened with the open window.
     * 
     * @return     File array
     */
    public File[] getSelectedFiles() {
        
        if (isSelected == JFileChooser.APPROVE_OPTION) {
            //Save the current directory path
            currentDir = openDiag.getCurrentDirectory();
        return openDiag.getSelectedFiles();
        }
        
        return null;
    }
    
     /** This method return the latest directory that have been browsed.
     * 
     * @return     File currentDir
     */
    public File getLastDir()
    {
        return currentDir;
    }
    
    //Methode de test
    public ArrayList<mySms> smsDB2;
    public void launchTestAnalyse(){
        vmgRead myR = new vmgRead();
        smsDB2 = myR.readMultipleVmg( myR.openFileArray(openDiag.getSelectedFiles()) );
        
    }
    
}
