package net.owl_black.tovmgr;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JTextPane;


/**
 * Simple About screen for the Owl VMG Reader
 * @author LouisBob
 * @version 0.1 - 16/07/2012
 */
public class About {

	private JFrame frmAbout;

	/**
	 * Display the About window
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					About window = new About();
					window.frmAbout.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor
	 */
	public About() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmAbout = new JFrame();
		frmAbout.setTitle("About");
		frmAbout.setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/owlvmg_128x128x32.png")));
		frmAbout.setBounds(100, 100, 395, 631);
		frmAbout.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAbout.getContentPane().setLayout(null);
		frmAbout.setLocationRelativeTo(null); //Center on the screen
		
		JPanel panel = new JPanel();
		
		ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/owlblack3.png"))); 
		JLabel label = new JLabel(); 
		label.setIcon(icon); 
		panel.add(label); 
		
		panel.setBounds(10, 11, 357, 395);
		frmAbout.getContentPane().add(panel);
		
		JLabel lblTheOwlVmg = new JLabel("The Owl VMG Reader");
		lblTheOwlVmg.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTheOwlVmg.setBounds(10, 417, 170, 19);
		frmAbout.getContentPane().add(lblTheOwlVmg);
		
		JLabel lbljavaVersion = new JLabel(" (JAVA version)");
		lbljavaVersion.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lbljavaVersion.setBounds(20, 436, 74, 19);
		frmAbout.getContentPane().add(lbljavaVersion);
		
		JLabel lblVersion = new JLabel("- version 0.92 (17 July 2012)");
		lblVersion.setBounds(197, 419, 170, 19);
		frmAbout.getContentPane().add(lblVersion);
		
		JLabel lblDeveloppedBy = new JLabel("Developped by: ");
		lblDeveloppedBy.setBounds(10, 466, 84, 14);
		frmAbout.getContentPane().add(lblDeveloppedBy);
		
		JLabel lblContact = new JLabel("Contact: ");
		lblContact.setBounds(10, 488, 57, 14);
		frmAbout.getContentPane().add(lblContact);
		
		JLabel lblWebsite = new JLabel("Website: ");
		lblWebsite.setBounds(10, 574, 46, 14);
		frmAbout.getContentPane().add(lblWebsite);
		
		JLabel lblLouisbob = new JLabel("Louisbob");
		lblLouisbob.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLouisbob.setBounds(104, 466, 125, 14);
		frmAbout.getContentPane().add(lblLouisbob);
		
		JLabel lblManzhackgmailcom = new JLabel("manzhack@gmail.com");
		lblManzhackgmailcom.setBounds(71, 488, 158, 14);
		frmAbout.getContentPane().add(lblManzhackgmailcom);
		
		JLabel lblWww = new JLabel("All rights reserved. \u00A9 OwlBlack Soft");
		lblWww.setBounds(190, 574, 177, 14);
		frmAbout.getContentPane().add(lblWww); 
		
		JLabel lblWwwowlblacknet = new JLabel("www.owl-black.net");
		lblWwwowlblacknet.setBounds(71, 574, 119, 14);
		frmAbout.getContentPane().add(lblWwwowlblacknet);
		
		JTextPane txtpnInTotalThis = new JTextPane();
		txtpnInTotalThis.setText("This software is the result of many months of work. If you like it, please donate, or just let me a message! \r\nwww.owl-black.net/the-owl-vmg-reader/");
		txtpnInTotalThis.setEditable(false);
		txtpnInTotalThis.setOpaque(false);
		txtpnInTotalThis.setBounds(10, 513, 357, 48);
		frmAbout.getContentPane().add(txtpnInTotalThis);
	}
	
	/**
	 * Show the About window.
	 */
	public void show(){
		frmAbout.setVisible(true);
	}
}
