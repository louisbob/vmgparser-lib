package net.owl_black.tovmgr;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.util.*;

import java.io.File;

/**
 * This class is the core of the OwlVMGReader application
 * @author LouisBob
 * @version 0.92 - 16/06/2012
 * 
 */

public class CoreGUI implements ActionListener ,ExportListener, ListClickListener, OpenWListener , BadCharListener{
    
	/* Environment */
	
		//User-interface objects declaration
	    JPanel contactPanel, conversPanel;
	    JFrame mainWindow;
	    MenuButton about;
	    JMenuItem openAction;
	    JMenuItem backupAction;
	    JMenuItem settingsAction;
	    JMenuItem exitAction;
	    JMenuItem exportAction;
	    JMenuItem cleanCharAction;
    
	    //Custom classes
	    ContactGUI contactArea;
	    ConversGUI conversArea;
	    OpenWizard opW;
	    Export ExportDialog;
	    BadChar bc;
	    vmgRead vmgProcess;
	    File lastDir; //Save the last used directory
    
	    //Create a new list of String that will store phone numbers
	    ArrayList<String> phoneNumbers = new ArrayList<String>();
	    
	    //coreGUI SMS database
	    private ArrayList<mySms> smsDB = new ArrayList<mySms>();
    
    /**
     *  Main method of the core. It has to be called to launch application.
     */
    public static void main(String[] args) {
	     new CoreGUI();
	}

	/**
	 * 	Constructor of the coreGUI class: set the look, instantiate all element of the user-interface,
	 *  and initialize the core database.
	 */
	public CoreGUI() {
        
        //To get MS Windows look
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception e) {
            System.out.println("Unable to load Windows look and feel");
        }
        
        /* Main window (frame) configuration */
        mainWindow = new JFrame();
        mainWindow.setTitle("The Owl VMG Reader");
        mainWindow.setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/owlvmg_128x128x32.png")));

        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        mainWindow.setSize(800, 500);
        mainWindow.setLocationRelativeTo(null); //Center the window on the screen
        
        /* Panel instantiation */ 
        contactPanel = new JPanel();
        conversPanel = new JPanel();
        
        /* Menubar set-up */	
        JMenuBar menuBar = new JMenuBar(); // Creates a menubar for the mainWindow
        
        // Define and add two drop down menu to the menubar
        JMenu fileMenu = new JMenu("File");
        JMenu toolsMenu = new JMenu("Tools");
        JMenu viewMenu = new JMenu("View");
        about = new MenuButton("About"); //Special class, set an empty menu button in the menubar
        
        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(viewMenu);
        menuBar.add(about);
        
        mainWindow.setJMenuBar(menuBar); // Add the menubar to the frame
        
        // Create and add simple menu item to one of the drop down menu
        openAction = new JMenuItem("Open VMG Files...");
        backupAction = new JMenuItem("Backup as *.mvmg-NA");
        backupAction.setEnabled(false);
        settingsAction = new JMenuItem("Settings-NA");
        exitAction = new JMenuItem("Exit");
        exportAction = new JMenuItem("Export");
        cleanCharAction = new JMenuItem("Clean text...");
        
        JMenuItem cutAction = new JMenuItem("Cut");
        JMenuItem copyAction = new JMenuItem("Copy");
        JMenuItem pasteAction = new JMenuItem("Paste");
        
        //Add action listeners
        openAction.addActionListener(this);
        exportAction.addActionListener(this);
        cleanCharAction.addActionListener(this);
        
        fileMenu.add(openAction);
        //fileMenu.add(backupAction);
        //fileMenu.add(settingsAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        
        toolsMenu.add(exportAction);
        toolsMenu.add(cleanCharAction);
        
        viewMenu.add(cutAction);
        viewMenu.add(copyAction);
        viewMenu.add(pasteAction);
        viewMenu.addSeparator();
        
        //Create a new object contactGUI, that will fill the panel contactPanel with good informations
        contactArea = new ContactGUI(smsDB, contactPanel);
        conversArea = new ConversGUI(conversPanel, "Please open VMGs to view the conversation");
        
        //Place contactPanel and conversPanel into the main window
        mainWindow.setLayout(new BorderLayout());
        mainWindow.add(contactPanel, BorderLayout.WEST);
        mainWindow.add(conversPanel, BorderLayout.CENTER);
        mainWindow.setVisible(true);
        
        /* Action-Listeners */
        about.addActionListener(this);
        contactArea.addListClickListener(this);
        
        //Database init:
        vmgProcess = new vmgRead();
    }
	
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
    	//File->Open button
        if(e.getSource() == openAction) {
            //If the database is empty, don't ask the user to add these files to the current opened list
            if(smsDB.size() == 0)
                opW = new OpenWizard(true, lastDir);
            else
                opW = new OpenWizard(false, lastDir);
                
            opW.addOpenWListener(this);
        }
        
        //Tools->Export button
        if(e.getSource() == exportAction) {
        	ExportDialog = new Export();
        	ExportDialog.addExportListener(this);
        }
        
        //Tools->Clean text...
        if(e.getSource() == cleanCharAction) {
        	bc = new BadChar();
        	bc.addBadCharListener(this);
        }
    }
    
    /* (non-Javadoc)
     * @see ListClickListener#update(java.lang.String)
     */
    public void update(String phoneNumber) {
	    conversArea.refresh(smsDB, phoneNumber);
	}

	
	/* (non-Javadoc)
	 * @see OpenWListener#fireOpened()
	 */
	public void fireOpened() {
		//Define two List of file: one for inbox, one more for outbox
	    ArrayList<File> inboxToOpen = new ArrayList<File>();
	    ArrayList<File> outboxToOpen = new ArrayList<File>();
	    
	    //First, save the last browsed directory
	    lastDir = opW.getLastDir();
	    
	    //Get the 'File' from the open wizard window
	    inboxToOpen = opW.getInboxFiles();
	    outboxToOpen = opW.getOutboxFiles();
	    
	    //Check if the user want to add sms to the current DB or not. 
	    //In the 'FALSE' case, just flush the DB
	    if(!opW.getAddToDB()) 
	        vmgProcess.eraseDB();
	    
	    //Now process all the files in the VMG algorithm
	    if(inboxToOpen != null) {
	        vmgProcess.processArrayFile(inboxToOpen, 1);
	    }
	    if(outboxToOpen != null) {
	        vmgProcess.processArrayFile(outboxToOpen, 2);
	    }
	    
	    //Refresh the core database
	    smsDB = vmgProcess.getSmsDB();
	    
	    //Then refresh the contact list with new names
	    contactArea.refresh(smsDB);
	    
	    //Process the database to sort the conversation messages by date
	    smsDB = conversArea.sortByDate(smsDB);
	    
	    //Refresh conversation with the new database
	    conversArea.refresh(smsDB);
	    
	    //Close the opening wizard window
	    opW.closeOpenW();
	    opW = null;
	}

	
    /* (non-Javadoc)
     * @see ExportListener#fireExport()
     */
    public void fireExport() {
    	
    	//Prepare exportation object
    	CSVExport CsvExpt = new CSVExport();
    	
    	//Get the File where we will save the output CSV.
    	File outputFile = ExportDialog.getFileToSave();
    	
    	//Get the option to know if the algorithm must merge the date with the time
    	boolean merge = ExportDialog.getChkBMerge();
        
    	//Check if the user want to split its CSV outputs
    	if(ExportDialog.getChkBsplit()) {
    		int nb;
    		try{
    			nb = Integer.parseInt(ExportDialog.getNbSplit());
    			if(nb < 0) {
    				JOptionPane.showMessageDialog(mainWindow,
    					    "Provided value of sms by CSV is negative! It must be positive",
    					    "You have no soul!",
    					    JOptionPane.ERROR_MESSAGE);
    				return;
    			}
    		}
    		catch(NumberFormatException e){
    			e.printStackTrace();
    			JOptionPane.showMessageDialog(mainWindow,
					    "Error during parsing of the number of sms per CSV. It must be a positive number.",
					    "Engine just crashed!",
					    JOptionPane.ERROR_MESSAGE);
    			return;
    		}
    		
    		//Finally export the database
    		CsvExpt.exportToCSV(smsDB, outputFile, merge, nb);
    	}
    	else
    		//In the case of the user won't split, just send -1 value to the exportCSV method
    		CsvExpt.exportToCSV(smsDB, outputFile, merge, -1);
    }
    
    
    
    /* (non-Javadoc)
     * @see BadCharListener#fireBadChar()
     */
    public void fireBadChar()  {
    	
    	//Process all the database by adding encoding option in the _encode variable of each _encode attribute
    	for(mySms sms:smsDB) {
    		sms.setEncode(bc.getEncodeOption());
    	}
    	
    	//Refresh the user-interface
    	conversArea.refresh(smsDB);
    	
    	bc.closeFrame();
    	bc = null;
    }
}