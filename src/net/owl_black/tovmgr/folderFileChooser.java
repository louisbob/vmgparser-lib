package net.owl_black.tovmgr;
import javax.swing.JFileChooser;
import java.io.File;

/**
 * This class is used for opening a File folder. When you instantiate this class, a window will be opened
 * and prompt the user to select its folder.
 * 
 * @author Louisbob
 * @version v0.1 - 16/07/2012
 */
public class folderFileChooser
{
    // instance variables
    JFileChooser openDiag = new JFileChooser();
    File currentDir;
    int isSelected = 0;

    /**
     * FolderFileChooser constructor.
     * @param winName Windows title
     * @param setDir Set the default directory to browse
     */
    public folderFileChooser(String winName, File setDir)
    {   
        //Disable multiple file selection
        openDiag.setMultiSelectionEnabled(false);
        
        //Add name to the titlebar
        openDiag.setDialogTitle(winName); 
        
        //Open the latest directory
        openDiag.setCurrentDirectory(setDir);
        
        openDiag.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        isSelected = openDiag.showOpenDialog(null);
    }
        

    /**
     * This method allows you to get the file that have been selected with the open window.
     * @return File variable of the selected folder.
     */
    public File getSelectedFolder() {
        
        if (isSelected == JFileChooser.APPROVE_OPTION) {
            //Save the current directory path
            currentDir = openDiag.getSelectedFile();
        return currentDir;
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
}
