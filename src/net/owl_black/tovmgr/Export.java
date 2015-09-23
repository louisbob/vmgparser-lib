package net.owl_black.tovmgr;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileFilter;


public class Export extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	/* Variable environnement */
	
	private String _path = "";
	File fileToSave;
	
	//Object's user-interface instantiation
	JButton btnOk, btnCancel, btnBrowse;
	JCheckBox chkBmergeDate, chkBsplit;
	JLabel lblHelp, lblPath;
	JPanel pan1, pan2, pan3;
	JTextField txtNbSplit;
	Container con;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Export dialog = new Export("test");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launch the dialog. (constructor)
	 */
	public Export() {
		_createGUI(_path);
	}
	
	/**
	 * Launch the dialog, with path option (constructor)
	 */
	
	public Export(String defaultPath) {
		_path = defaultPath;
		_createGUI(_path);
	}
	
	/**
	 * Create the dialog, with path option (method)
	 */
	
	private void _createGUI(String pathToDisplay) {
		//To get windows look
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception e) {
            System.out.println("Unable to load Windows look and feel");
        }
        
        //Window configurations
        new JFrame();
        this.setTitle("Owl Wizard Opening");
        this.setSize(300, 200);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/ressources/owlvmg_128x128x32.png")));
        this.setLocationRelativeTo(null); //Center on the screen
        
        //Buttons & label & checkbox instantiation
        btnOk = new JButton("Ok");
        btnOk.setEnabled(false);
        btnCancel = new JButton ("Cancel");
        btnBrowse = new JButton ("Browse...");
        
        lblHelp = new JLabel("Simply select the path where you want to export:");
        lblPath = new JLabel(pathToDisplay);
        
        chkBmergeDate = new JCheckBox("Merge date & time together");
        chkBmergeDate.setSelected(false);
        
        chkBsplit = new JCheckBox("Limit number of sms by CSV to: ");
        chkBsplit.setSelected(false);
        
        txtNbSplit = new JTextField();
        txtNbSplit.setPreferredSize(new Dimension(40, 20));
        txtNbSplit.setEnabled(false);
        
        
        //Configure Gridbag Layout
        this.setLayout(new GridBagLayout());
        con = this.getContentPane();
        GridBagConstraints gbc = new GridBagConstraints();
        
        //Configure panels
        
        pan1 = new JPanel();
        pan1.add(lblPath);
        pan1.add(btnBrowse);
        
        pan2 = new JPanel();
        pan2.setAlignmentX(LEFT_ALIGNMENT);
        pan2.add(btnOk);
        pan2.add(btnCancel);
        
        pan3 = new JPanel();
        pan3.add(chkBsplit);
        pan3.add(txtNbSplit);
        
        //Add help label
        gbc.fill = GridBagConstraints.LINE_START; //Don't fit horizontally the button
        gbc.weightx = 0.5;
        gbc.weighty = 0.25;
        gbc.gridx = 0;    //The row place
        gbc.gridy = 0;    //The column place
        con.add(lblHelp, gbc); //Add the button to the container, with constraints defined by c
        
        //Add path label and browse button (contained in a JPanel)
        gbc.fill = GridBagConstraints.LINE_START;
        gbc.weightx = 0.5;
        gbc.weighty = 0.25;
        gbc.gridx = 0; 
        gbc.gridy = 1;
        con.add(pan1, gbc);
        
        //Add checkboxs
        gbc.fill = GridBagConstraints.CENTER;
        gbc.weightx = 0.5;
        gbc.weighty = 0.25;
        gbc.gridx = 0; 
        gbc.gridy = 2;
        con.add(chkBmergeDate, gbc);
        
        gbc.fill = GridBagConstraints.CENTER;
        gbc.weightx = 0.5;
        gbc.weighty = 0.25;
        gbc.gridx = 0; 
        gbc.gridy = 3;
        con.add(pan3, gbc);
        
        //Add the OK and Cancel buttons
        gbc.fill = GridBagConstraints.LINE_START;
        //gbc.weightx = 0.5;
        gbc.weighty = 0.25;
        gbc.gridx = 0;
        gbc.gridy = 4;
        con.add(pan2, gbc);
        
        //Add listener to each button:
  		btnOk.addActionListener(this);
  		btnCancel.addActionListener(this);
  		btnBrowse.addActionListener(this);
  		
  		chkBsplit.addActionListener(this);
		
		try {
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Actionlistener method (method)
	 */
	
	public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnOk) {
        	if (fileToSave.exists()) {
                int result = JOptionPane.showConfirmDialog(this,
                        "The file exists, overwrite?", "Existing file",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                switch (result) {
                case JOptionPane.YES_OPTION:
                	closeDialog();
            		fireLaunchExport();
                case JOptionPane.CANCEL_OPTION:
                }
            }
        	else{
        		closeDialog();
        		fireLaunchExport();
        	}
        }
       
        if (e.getSource() == btnCancel) {
        	closeDialog();
        }
        
        if (e.getSource() == btnBrowse){
        	_browse(_path, "Save export file as...");
        }
        
        if (e.getSource() == chkBsplit){
        	if(chkBsplit.isSelected()) txtNbSplit.setEnabled(true);
        	else txtNbSplit.setEnabled(false);
        }
    }
	
	private void _browse(String pathToBrowse, String winTitle) {
		
		int returnVal=0;
    	JFileChooser fc;
    	
        //Define file extensions that can be opened
        FileFilter Csv = new FiltreSimple("CSV file (*.csv)",".csv");
        
        //Define default path and instantiate
        fc = new JFileChooser(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
 
        //Allow multiple file selection
        fc.setMultiSelectionEnabled(false);
        
        //Add the CSV extension
        fc.addChoosableFileFilter(Csv);
        
        //Set csv file to default filter
        fc.setFileFilter(Csv);
       
    	returnVal = fc.showSaveDialog(null);
    	
    	if (returnVal == JFileChooser.APPROVE_OPTION) {
    		
    		//Add the extension
    		  fileToSave = fc.getSelectedFile();
    		  String path = fileToSave.getAbsolutePath();

    		  if(!path.endsWith(".csv"))
    		  {
    		    fileToSave = new File(path + ".csv");
    		  }

            lblPath.setText(fileToSave.getPath());
            btnOk.setEnabled(true);
            this.pack();
        }
	}
	
	public File getFileToSave(){
		return fileToSave;
	}
	
	public boolean getChkBMerge() {
		return chkBmergeDate.isSelected();
	}
	
	public boolean getChkBsplit() {
		return chkBsplit.isSelected();
	}
	
	public String getNbSplit() {
		return txtNbSplit.getText();
	}
	
    public void closeDialog() {
        this.setVisible(false);
        this.dispose();
    }
    
    /**
     *      LISTENER CONFIGURATION
     * 
     */
    // un seul objet pour tous les types d'écouteurs
    private final EventListenerList listeners = new EventListenerList();
    
    public void addExportListener(ExportListener listener) {
        listeners.add(ExportListener.class, listener);
    }
    
    public void removeExportListener(ExportListener listener) {
        listeners.remove(ExportListener.class, listener);
    }
    
    public ExportListener[] getExportListeners() {
        return listeners.getListeners(ExportListener.class);
    }
    

    // This method is called each time the user changes the set of selected items
    public void fireLaunchExport() {
        for ( ExportListener listener : getExportListeners() )
        listener.fireExport();
    }
}
