package net.owl_black.tovmgr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SmsBubble extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/* Modifiable properties. */
	private String messageText;
	private String txtDate;
	
	/* Theming. */
	static Color clConversationBg 	= new Color(238, 238, 238); //Grey light
	
	static Color clSentBbl 			= new Color(255, 255, 255); //White
	static Color clSentTxtBbl		= Color.WHITE;
	
	static Color clReceivedBbl 		= new Color(15, 157, 88);   //Green
	static Color clReceivedTxtBbl	= Color.BLACK;
	
	static Color clTime				= new Color(132, 132, 132); //Other grey
	
	static int BBL_RECEIVED = 0;
	static int BBL_SENT 	= 1;
	
	/* Graphical resources. */
	private JLabel lblDate;
	private JLabel lblMessage;
	private JPanel panText;
	
	/* Test function. */
	public static void main(String[] args) {
		JFrame frame = new JFrame("SmsBubble test");
		
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(clConversationBg);
		
		SmsBubble bbl = new SmsBubble("<html>This is my text message. it is super cool to have this kind of super styled tuf"
				+ "with another style of things lorem ipsum</html>", "<html>Dec. 12/2015, 12:41pm<br> </html>");
		
		//FOR DEBUG
		//bbl.setBorder(BorderFactory.createLineBorder(Color.black));
		//bbl.setMaximumSize(new Dimension(300, 150));
		
		frame.add(bbl);
		frame.setVisible(true);
	}
	
	public SmsBubble() {
		this.messageText = "";
	    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    this.setDoubleBuffered(true);
	    
	    //Layout that hold the text and the date.
  		panText = new JPanel(true); //Double buffered
  		panText.setLayout(new BoxLayout(panText, BoxLayout.Y_AXIS));
  		panText.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
  		panText.setAlignmentX(SwingConstants.CENTER);
  		panText.setBackground(new Color(0,0,0,0)); //Set maximum transparency
  		
  		//Create margin/padding around the text:
  		panText.setBorder(BorderFactory.createEmptyBorder(1, 8, 10, 20)); //top, left, bottom, right
  		
  		//Create the date
  		lblDate = new JLabel("", SwingConstants.CENTER);
  	    lblDate.setFont(new Font("Calibri",1,10));
		lblDate.setForeground(clTime);
		lblDate.setDoubleBuffered(true); //TODO: check performances
		panText.add(lblDate);
  		
  		//Create the text label
  	    lblMessage = new JLabel("", SwingConstants.LEFT);
  	    lblMessage.setFont(new Font("Calibri",1,12));
  	    lblMessage.setForeground(clReceivedTxtBbl);
  	    lblMessage.setDoubleBuffered(true);
  	    panText.add(lblMessage);
  	    
  	    this.add(panText);
  	    
  	    //FOR DEBUG
  	    //lblDate.setBorder(BorderFactory.createLineBorder(Color.black, 1));
  	    //lblMessage.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}
	
	public SmsBubble(String messageText) {
		this();
		
		//Update the text
		this.messageText = messageText;
		this.lblMessage.setText(this.messageText);
	}
	
	public SmsBubble(String messageText, String dateText) {
		this(messageText);
		
		//Update the date
		this.setStrDate(dateText);
		this.lblDate.setText(dateText);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D graphBubble = (Graphics2D) g;
		
		//Get dimensions to create a re dimension relativeness bubble
		Dimension dimPanel 		= this.getSize();
		Dimension dimPanelMax 	= this.getMaximumSize();
		
		if(dimPanel.height > dimPanelMax.height)
			dimPanel.height = dimPanelMax.height;
		
		if(dimPanel.width > dimPanelMax.width)
			dimPanel.width = dimPanelMax.width;
		
		//Create the rectangle bubble
	    RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
	    		0, 0, dimPanel.width-15, dimPanel.height, 8, 8); // x pos, y pos, rect width, rect height, corner round
	    
	    graphBubble.setColor(clSentBbl);
	    graphBubble.draw(roundedRectangle);
	    graphBubble.fill(roundedRectangle);
	    
	    //Create the triangle TODO: make it parameterizable
	    int[] xPoints = {dimPanel.width-20, dimPanel.width, dimPanel.width-20};
		int[] yPoints = {0, 0, 20};
		
	    Polygon triangle = new Polygon(xPoints, yPoints, xPoints.length);
	    graphBubble.draw(triangle);
	    graphBubble.fill(triangle);
	    
	    //FOR DEBUG
	    //graphics2.setColor(Color.BLUE);
	};
	
	/*
	 * Setters and getters
	 */
	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
		
		
	}

	public String getStrDate() {
		return txtDate;
	}

	public void setStrDate(String txtDate) {
		this.txtDate = txtDate;
	}
}
