package net.owl_black.tovmgr;
import java.awt.Choice;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;


/**
 * Simple user-interface window that asks the user to select correction algorithm. 
 * @author LouisBob
 * @version 0.1 - 16/07/2012
 */
public class BadChar extends JFrame implements ActionListener, ItemListener{
	
	/* Environment */
		private static final long serialVersionUID = 1L;
		
		//User-interface objects declaration
		JLabel lblInstruction;
		JTextArea lblHelp;
		JButton btnOk, btnCancel;
		Choice cAlgo;
		JPanel pan1;
		Container con;
		
		//Explanation of each algorithm
		final private String _utfClassical = "If your conversation stream contains " +
				"weird things like \"Hello=0D=0A\", you must use this algorithm." +
				"It often append on non-anglophone language with accent characters, like" +
				"French, German or Spanish. Work fine with Samsung Wave I & II."; 
		final private String _utfArabic = "This algorithm provide a support for Arabic & Persian " +
				"languages style. It also add UTF-8 beacon on exported files to avoid " +
				"miss-encoding files.";

	/**
	 * Launch a bad character processing window.
	 */
	public static void main(String[] args) {
		new BadChar();
	}
	
	/**
	 * Configure and display the dialog.
	 */
	public BadChar() {
		_createGUI();
	}
	
	/**
	 * Configure and display the dialog.
	 */
	private void _createGUI() {
		//To get the MS Windows look
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception e) {
            System.out.println("Unable to load Windows look and feel");
        }
        
        /* Window configurations */
        new JFrame();
        this.setTitle("Bad characters correction");
        this.setSize(400, 490);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/ressources/owlvmg_128x128x32.png")));
        this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        /* Configure object interfaces and instantiate */
        
        //Configuring borders of the list
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        
        //Adding border to the contact area
        TitledBorder helpBorder;
        helpBorder = BorderFactory.createTitledBorder(loweredetched, "Help");
        helpBorder.setTitleJustification(TitledBorder.LEFT);
        
        //Instructions
        lblInstruction = new JLabel("Please choose a correction algorithm to apply:");
        
        //Choice list
        cAlgo = new Choice();
        cAlgo.addItem("");
        cAlgo.addItem("Classical UTF-8");  
        cAlgo.addItem("Persian/arabic characters"); 
        cAlgo.select(null);
        
        //Help label
        lblHelp = new JTextArea("Select an algorithm in the list to get some help from the wise Black Owl :)");
        lblHelp.setLineWrap(true);
        lblHelp.setOpaque(false);
        lblHelp.setEditable(false);
        lblHelp.setWrapStyleWord(true);
        lblHelp.setPreferredSize(new Dimension(300, 350));
        lblHelp.setBorder(helpBorder);
        
        //Buttons
        btnOk = new JButton("Ok");
        btnCancel = new JButton("Cancel");
        
        //Panel configuration
        pan1 = new JPanel();
        pan1.add(btnOk);
        pan1.add(btnCancel);
        
        //Configure Gridbag Layout
        this.setLayout(new GridBagLayout());
        con = this.getContentPane();
        GridBagConstraints gbc = new GridBagConstraints();
		
        //Add instruction label
        gbc.fill = GridBagConstraints.LINE_START;
        gbc.weightx = 0.5;
        gbc.weighty = 0.25;
        gbc.gridx = 0;   
        gbc.gridy = 0;    
        con.add(lblInstruction, gbc);
        
        //Add expendable list
        gbc.fill = GridBagConstraints.LINE_START;
        gbc.weightx = 0.5;
        gbc.weighty = 0.25;
        gbc.gridx = 0;
        gbc.gridy = 1;
        con.add(cAlgo, gbc); 
        
        //Add help area
        gbc.fill = GridBagConstraints.LINE_START;
        gbc.weightx = 0.5;
        gbc.weighty = 0.25;
        gbc.gridx = 0;
        gbc.gridy = 2;
        con.add(lblHelp, gbc);
        
        //Add Ok and Cancel buttons
        gbc.fill = GridBagConstraints.LINE_START;
        gbc.weightx = 0.5;
        gbc.weighty = 0.25;
        gbc.gridx = 0;
        gbc.gridy = 3;
        con.add(pan1, gbc);
        
        /* Action Listeners */
        cAlgo.addItemListener(this);
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        
        //Show the window
		this.setVisible(true);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
    {
		if(e.getSource() == btnOk) {
			fireBadCharAlgoritm();
		}
		
		if(e.getSource() == btnCancel) {
			closeFrame();
		}
		
    }
	
	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent item) {

		if (cAlgo.getSelectedIndex() == 0) {
			lblHelp.setText("(No algorithm)");
		}
		if (cAlgo.getSelectedIndex() == 1) {
			lblHelp.setText(_utfClassical);
		}
		
		if (cAlgo.getSelectedIndex() == 2) {
			lblHelp.setText(_utfArabic);
		}
	}
	
	/** Return the user-choice concerning SMS text encoding.
	 * @return The selected encoding option
	 */
	public int getEncodeOption() {
		return cAlgo.getSelectedIndex();
	}
	
    /**
     * Simply close the bad character window.
     */
    public void closeFrame() {
        this.setVisible(false);
        this.dispose();
    }
	
	/**
     *      LISTENER CONFIGURATION
     * 
     */
    //Create the list of all the listeners
    private final EventListenerList listeners = new EventListenerList();
    
    /**
     * @param listener Object to add to the listener list.
     */
    public void addBadCharListener(BadCharListener listener) {
        listeners.add(BadCharListener.class, listener);
    }
    
    /**
     * @param listener Object to remove from the listener list.
     */
    public void removeBadCharListener(BadCharListener listener) {
        listeners.remove(BadCharListener.class, listener);
    }
    
    /**
     * @return List of listeners.
     */
    public BadCharListener[] getBadCharListeners() {
        return listeners.getListeners(BadCharListener.class);
    }
    
    // This method is called in the listeners classes. It must be defined in these classes.
    public void fireBadCharAlgoritm() {
        for ( BadCharListener listener : getBadCharListeners() )
        listener.fireBadChar();
    }
}
