package net.owl_black.tovmgr;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.io.File;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.EventListenerList;

/**
 * This class is used to manage VMGs: you can add VMGs from an entire folder or a list of files.
 * 
 * @author Louisbob
 * @version 26/06/2012 - v0.3
 */
public class OpenWizard implements ActionListener, ItemListener
{
    Container centerCon;
    JFrame wizardWindow;
    JButton openB, cancelB, addInB, addOutB;
    JCheckBox DBchkB;
    JLabel instructions;
    JList listInbox, listOutbox;
    JScrollPane scrollListIn, scrollListOut;
    private ArrayList<File> inboxToOpen = new ArrayList<File>(); //Array for the lists
    private ArrayList<File> outboxToOpen = new ArrayList<File>();
    boolean addToCurrentDB = true;
    File lastDir;
    
    private Boolean inBoxClick, outBoxClick; //let us know if we add files to Inbox or Outbox

    /**
     * Constructor for objects of class openWizard
     */
    public OpenWizard(boolean isDBempty, File lDir)
    {
        //To set the last used dir:
        lastDir = lDir;
        
        //If the DB isn't empty, add the option to add files to the current DB
        
        inBoxClick = false;
        outBoxClick = false;
            
        //Window configurations
        wizardWindow = new JFrame();
        wizardWindow.setTitle("Owl Wizard Opening");
        wizardWindow.setSize(400, 500);
        wizardWindow.setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/ressources/owlvmg_128x128x32.png")));
        
        //Buttons instanciation
        openB = new JButton("Open !");
        addInB = new JButton("+");
        addOutB = new JButton("+");
        cancelB = new JButton("Cancel");
        
        //Checkbox instanciation
        DBchkB = new JCheckBox("Add to the current DB.");
        if(isDBempty)
        {
            DBchkB.setEnabled(false);
            DBchkB.setSelected(false);
        }
        else
        {
            DBchkB.setEnabled(true);
            DBchkB.setSelected(true);
        }
        //Labels instanciation
        instructions = new JLabel();

        //Adding JList, list of files to open
        listInbox = new JList();
        listOutbox = new JList();
        
        //Configuring borders to the JList and add titles
        TitledBorder titleIn, titleOut;
        //Border blackline = BorderFactory.createLineBorder(Color.black);
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        
            //Configuring title of borders
        titleIn = BorderFactory.createTitledBorder(loweredetched, "Inbox VMG(s)");
        titleIn.setTitleJustification(TitledBorder.LEFT);
        
        titleOut = BorderFactory.createTitledBorder(loweredetched, "Outbox VMG(s)");
        titleOut.setTitleJustification(TitledBorder.RIGHT);
        
        //Set size of the lists
        //listInbox.setPreferredSize(new Dimension(100, 200));
        //listOutbox.setPreferredSize(new Dimension(100, 200));
        
        //EXAMPLE CONTENT
        
        //We are creating a layout to place all graphics components
        wizardWindow.setLayout(new GridBagLayout());
       
        //GridBag Constraints
        GridBagConstraints c = new GridBagConstraints();
        centerCon = wizardWindow.getContentPane();
        
        c.fill = GridBagConstraints.NONE; //Don't fit horizontaly the button
        c.weightx = 0.5;
        c.gridx = 1;    //The row place
        c.gridy = 2;    //The column place
        addInB.setPreferredSize(new Dimension(45, 35));
        centerCon.add(addInB, c); //Add the button to the container, with constraints defined by c
        
        //Add the Inboxlist
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 1;
        scrollListIn = new JScrollPane(listInbox);  //Make the list scrollable
        scrollListIn.setPreferredSize(new Dimension(100, 200));
        scrollListIn.setBorder(titleIn);
        centerCon.add(scrollListIn, c);
        
        //add the Outboxlist
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 1;
        scrollListOut = new JScrollPane(listOutbox);  //Make the list scrollable
        scrollListOut.setPreferredSize(new Dimension(100, 200));
        scrollListOut.setBorder(titleOut);
        centerCon.add(scrollListOut, c);
        
        c.fill = GridBagConstraints.NONE; //Don't fit horizontaly the button
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 2;
        addOutB.setPreferredSize(new Dimension(45, 35));
        centerCon.add(addOutB, c);
        
        //Add the checkbox
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 1;
        c.gridy = 3;
        c.ipadx = 2;
        centerCon.add(DBchkB, c);
        
        //Add Cancel Button
        
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 1;
        c.gridy = 4;
        c.ipadx = 2;
        cancelB.setPreferredSize(new Dimension(80, 35));
        centerCon.add(cancelB, c);
        
        //Add Open Button
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 2;
        c.gridy = 4;
        c.ipadx = 2;
        openB.setPreferredSize(new Dimension(80, 35));
        centerCon.add(openB, c);
        
        //Adding instructions
        JLabel instructions = new JLabel("<html><p>Welcome to the opening wizard. Please select if you want to open an entire folder to read,"
        + "or if you want manually select files.</p></html>");
        
        c.fill = GridBagConstraints.HORIZONTAL;
        instructions.setPreferredSize(new Dimension(100, 30));
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 0;
        centerCon.add(instructions, c);
        
        //Add listeners on "add" buttons
        addInB.addActionListener(this);
        addInB.addMouseListener(new PopClickListener());
        openB.addActionListener(this);
        cancelB.addActionListener(this);
        
        addOutB.addActionListener(this);
        addOutB.addMouseListener(new PopClickListener());
        
        //Add listener to the checkbox
        DBchkB.addActionListener(this);
        
        //Show the window
        wizardWindow.setLocationRelativeTo(null); //Center on the screen
        wizardWindow.setVisible(true);
    }
    
    /* Accesseur */
    public boolean getAddToDB() {
            return addToCurrentDB;
    }
    
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();

        if (source == DBchkB) {
            if(DBchkB.isSelected())
                addToCurrentDB = true;
            else
                addToCurrentDB = false;
        }
    }
    
    /**
     * Simply close the wizard when asked
     * 
     */
    public void closeOpenW() {
        wizardWindow.setVisible(false);
        wizardWindow.dispose();
    }
    
    public ArrayList<File> getInboxFiles() {
        return inboxToOpen;
    }
    
    public ArrayList<File> getOutboxFiles() {
        return outboxToOpen;
    }
    
    private void refreshFilelist(File[] filesToAdd) {
        ArrayList<String> fileList = new ArrayList<String>();
        
        if(filesToAdd != null){
        //Adding files to inbox array files or outbox array files
            if(inBoxClick && !outBoxClick){
                for(int j=0; j < filesToAdd.length; j++) {
                    inboxToOpen.add(filesToAdd[j]);
                }
                
                for(int j=0; j < inboxToOpen.size(); j++) {
                   fileList.add(inboxToOpen.get(j).getName());
                }
                listInbox.setListData(fileList.toArray()); 
            }
            
            if(!inBoxClick && outBoxClick){
                for(int j=0; j < filesToAdd.length; j++) {
                    outboxToOpen.add(filesToAdd[j]);
                }
                
                for(int j=0; j < inboxToOpen.size(); j++) {
                   fileList.add(outboxToOpen.get(j).getName());
                }
                listOutbox.setListData(fileList.toArray());
            }
        
        }
        
    }
    
    class addVMGPopup extends JPopupMenu implements ActionListener {
        
        JMenuItem addFiles, addFolder;
        
        /**
         * Show a little context menu that ask if you want to open multiples files, or directly a folder
         * 
         * @return     nothing
         */
        
        public addVMGPopup(){
            //Add entries into the popup menu
            addFiles = new JMenuItem("Add files...");
            addFiles.addActionListener(this);
            this.add(addFiles);
            
            addFolder = new JMenuItem("Add folder...");
            addFolder.addActionListener(this);
            this.add(addFolder);
        }
        
        /**
         * Action performed when an item is selected into the popup menu
         * 
         * @param  e   ActionEvent
         * @return     nothing
         */
        
        public void actionPerformed(ActionEvent e) {
            multipleFileChooser omf;
            folderFileChooser ffc;
            String titleOpenWin = "?";
            File[] filesToAdd, listF;
            ArrayList<File> fileList;
            File folderToAdd;
            
            //We get the source of the event
            JMenuItem source = (JMenuItem)(e.getSource());
            
            //We verify if we add VMG to inbox or outbox
            if(inBoxClick) titleOpenWin = "Add VMGs to INBOX";
            if(outBoxClick) titleOpenWin = "Add VMGs to OUTBOX";
            
            //What appends then...
            if( source.getText() == "Add files..."){
                omf = new multipleFileChooser(titleOpenWin, lastDir);
                refreshFilelist(omf.getSelectedFiles());
                lastDir = omf.getLastDir();
            }
            
            if( source.getText() == "Add folder..."){
                ffc = new folderFileChooser(titleOpenWin, lastDir);
                folderToAdd = ffc.getSelectedFolder();
                
                if(folderToAdd.isDirectory()) {
                	fileList = new ArrayList<File>();
                	folderToAdd = ffc.getSelectedFolder();
                	lastDir = ffc.getLastDir();
                	listF = folderToAdd.listFiles();
                	
                	for(File f : listF) {
                		if(getExtension(f).indexOf("vmg") != -1)
                		{
                			fileList.add(f);
                		}
                	}
                	filesToAdd = new File[fileList.size()];
                	fileList.toArray(filesToAdd);
                	if(filesToAdd != null)
                		refreshFilelist(filesToAdd);
                }
                
            }
            
        }
    }
    
    class PopClickListener extends MouseAdapter {
        
        public void mousePressed(MouseEvent e){
            //if(SwingUtilities.isLeftMouseButton(e)) doPop(e);
        }
    
        public void mouseReleased(MouseEvent e){
            if(SwingUtilities.isLeftMouseButton(e)) doPop(e);
        }
    
        private void doPop(MouseEvent e){
            addVMGPopup menu = new addVMGPopup();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * ActionListener actions when a button is pushed
     * 
     */
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == addInB) {
            inBoxClick = true;
            outBoxClick = false;
        }
       
        if (e.getSource() == addOutB) {
            outBoxClick = true;
            inBoxClick = false;
        }
        
        if (e.getSource() == openB){
            fireOpenGUI();
        }
        
        if (e.getSource() == cancelB) {
            closeOpenW();
        }
        
         if (e.getSource() == DBchkB) {
            if(DBchkB.isSelected())
                addToCurrentDB = true;
            else
                addToCurrentDB = false;
        }
    }
    
    JPopupMenu popup;
    public void showPopup() {
      popup.show(addInB, 0, 0);
    }
    
    /**
     *      LISTENER CONFIGURATION
     * 
     */
    // un seul objet pour tous les types d'écouteurs
    private final EventListenerList listeners = new EventListenerList();
    
    public void addOpenWListener(OpenWListener listener) {
        listeners.add(OpenWListener.class, listener);
    }
    
    public void removeOpenWListener(OpenWListener listener) {
        listeners.remove(OpenWListener.class, listener);
    }
    
    public OpenWListener[] getContactListeners() {
        return listeners.getListeners(OpenWListener.class);
    }
    

    // This method is called each time the user changes the set of selected items
    public void fireOpenGUI() {
                
        for ( OpenWListener listener : getContactListeners() )
        listener.fireOpened();
    }
    
    public File getLastDir()
    {
        return lastDir;
    }
    
    public static String getExtension(File f)
    {
	    String ext = null;
	    String s = f.getName();
	    int i = s.lastIndexOf('.');
	
	    if (i > 0 && i < s.length() - 1)
	    ext = s.substring(i+1).toLowerCase();
	
	    if(ext == null)
	    return "";
	    return ext;
    }
}
