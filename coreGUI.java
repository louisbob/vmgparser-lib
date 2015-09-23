import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.awt.*;
import javax.swing.BorderFactory; 
import javax.swing.border.*;
import java.util.*;
import javax.swing.event.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.ComponentEvent;

public class coreGUI implements ActionListener ,ExportListener, ListClickListener, OpenWListener , BadCharListener{
    
	/* Environment */
	
		//Object's user-interface instantiation
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
	    
	    //coreGUI sms database
	    private ArrayList<mySms> smsDB = new ArrayList<mySms>();
    
    public void update(String phoneNumber) {
        conversArea.refresh(smsDB, phoneNumber);
    }
    
    //Action performed when the user has opened his files
    public void fireOpened() {
        ArrayList<File> inboxToOpen = new ArrayList<File>(); //Array for the lists
        ArrayList<File> outboxToOpen = new ArrayList<File>(); //Array for the lists
        
        //First, save the last browsed dir:
        lastDir = opW.getLastDir();
        
        inboxToOpen = opW.getInboxFiles();
        outboxToOpen = opW.getOutboxFiles();
        
        //Check if the user want to add to the current DB or not. In the FALSE case, just flush the DB
        if(!opW.getAddToDB()) 
            vmgProcess.eraseDB();
        
        
        //Ok now we process all the files
        
        if(inboxToOpen != null) {
            vmgProcess.processArrayFile(inboxToOpen, 1);
        }
        if(outboxToOpen != null) {
            vmgProcess.processArrayFile(outboxToOpen, 2);
        }
        
        //Refresh the DB
        smsDB = vmgProcess.getSmsDB();
        
        //Refresh Contactlist
        contactArea.refresh(smsDB);
        
        //Cleaning bad characters:
        for(int j=0; j < smsDB.size(); j++) {
            mySms tmpSms = smsDB.get(j);
            smsDB.remove(j);
            tmpSms.setText(
                            mySms.replaceNonUTF8( tmpSms.getText() )
                            );
            smsDB.add(tmpSms);
        }
        
        //mySms tempSms = smsDB.get(0);
        //Sort database:
        smsDB = conversArea.sortByDate(smsDB);
        
        //Refresh conversation
        conversArea.refresh(smsDB);
        
        opW.closeOpenW();
        opW = null;
    }
    
    //Called when the user have selected browsing etc...
    public void fireExport() {
    	
    	CSVExport Cexport = new CSVExport();
    	File outputFile = ExportDialog.getFileToSave();
    	boolean merge = ExportDialog.getChkBMerge();
        
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
    		
    		Cexport.exportToCSV(smsDB, outputFile, merge, nb);
    	}
    	else
    		Cexport.exportToCSV(smsDB, outputFile, merge, -1);
    }
    
    public void fireBadChar()  {
    	
    	for(mySms sms:smsDB) {
    		sms.setEncode(bc.getEncodeOption());
    	}
    	
    	conversArea.refresh(smsDB);
    	
    	bc.closeFrame();
    	bc = null;
    }
    
    public coreGUI() {
        
        //To get windows look
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception e) {
            System.out.println("Unable to load Windows look and feel");
        }
        
        mainWindow = new JFrame();
        contactPanel = new JPanel();
        conversPanel = new JPanel();
        
        mainWindow.setTitle("The Owl VMG Reader");
        mainWindow.setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/ressources/owlvmg_128x128x32.png")));

        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        mainWindow.setSize(800, 500);
        mainWindow.setLocationRelativeTo(null); //Center the window on the screen
        
        //Permet de créer une fenetre contenant 1 colonne et 3 lignes
        //mainWindow.setLayout(new GridLayout(1,2)); 
        //mainWindow.setLayout(new GridLayout(1,2));
        
        // Creates a menubar for a JFrame
        JMenuBar menuBar = new JMenuBar();
        
        // Add the menubar to the frame
        mainWindow.setJMenuBar(menuBar);
        
        // Define and add two drop down menu to the menubar
        JMenu fileMenu = new JMenu("File");
        JMenu toolsMenu = new JMenu("Tools");
        JMenu viewMenu = new JMenu("View");
        //about = new JButton("About");
        about = new MenuButton("About");
        about.addActionListener(this);
        
        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(viewMenu);
        menuBar.add(about);
        
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
        //about.addChangeListener(this);
        
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
        contactArea.addListClickListener(this);
        
        //conversGUI conversArea = new conversGUI(conversPanel);
        conversArea = new ConversGUI(conversPanel, "Please open VMGs to view the conversation");
        
        //Permet d'ajouter les conteneurs 'pan1', 'pan2', 'pan3' dans le conteneur global 'fenetre'
        mainWindow.setLayout(new BorderLayout());
        mainWindow.add(contactPanel, BorderLayout.WEST);
        mainWindow.add(conversPanel, BorderLayout.CENTER);
        mainWindow.setVisible(true);
        
        //Database init:
        vmgProcess = new vmgRead();
    }
    
    public static void main(String[] args) {
        coreGUI window = new coreGUI();
        //window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == openAction) {
            
            //If the database is empty, don't ask the user to add these files to the current opened list
            if(smsDB.size() == 0)
                opW = new OpenWizard(true, lastDir);
            else
                opW = new OpenWizard(false, lastDir);
                
            opW.addOpenWListener(this);
        }
        
        if(e.getSource() == exportAction) {
            
        	ExportDialog = new Export();
        	ExportDialog.addExportListener(this);
        }
        
        if(e.getSource() == cleanCharAction) {
           
        	bc = new BadChar();
        	bc.addBadCharListener(this);
        	/*
        	int result = JOptionPane.showConfirmDialog(cleanCharAction,
                    "This tool will clean all mis-formated characters. Continue?", "Characters replacement",
                    JOptionPane.YES_NO_OPTION);
            switch (result) {
            case JOptionPane.YES_OPTION:
            	for(mySms sms:smsDB) {
            		String ln = sms.getText();
            		ln = mySms.replaceNonUTF8(ln);
            		sms.setText(ln);
            	}
            	
            	conversArea.refresh(smsDB);
            	
            case JOptionPane.CANCEL_OPTION:
            	break;
            }
            */
        }
        
    }
    
    public class MenuButton extends JMenu{

    	private boolean startedIn = false;
    	private ActionListener action;

    	public MenuButton(String title){
    	    super(title);       
    	    this.addMouseListener(new MenuButtonListener());
    	}

    	public void addActionListener(ActionListener a){
    	    action = a;
    	}

    	public class MenuButtonListener extends MouseAdapter{

    	    @Override
    	    public void mouseClicked(MouseEvent arg0) {
    	        if(action != null){
    	            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    	            MenuButton.this.setSelected(false);
    	            About abt = new About();
    	        	abt.show();
    	        }
    	    }
    	}
    	}
}