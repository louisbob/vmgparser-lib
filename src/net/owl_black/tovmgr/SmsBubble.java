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
import javax.swing.border.Border;

public class SmsBubble extends JPanel {

	private static final long serialVersionUID = 1L;
	
	enum BubbleDirection {
		BBL_RECEIVED__RIGHT,
		BBL_SENT__LEFT
	}
	
	/* Modifiable properties. */
	private String messageText;
	private String txtDate;
	private BubbleDirection bblDirection;
	
	/* Theming. */
	private Color clConversationBg 	= new Color(238, 238, 238); //Grey light
	
	private Color clTime;
	private Color clBbl;
	private Color clTxtBbl;
	
	/* Graphical resources. */
	private JLabel lblDate;
	private JLabel lblMessage;
	private JPanel panText;
	
	/* Test function. */
	public static void main(String[] args) {
		JFrame frame = new JFrame("SmsBubble test");
		
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(238, 238, 238));
		
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		
		SmsBubble bbl1 = new SmsBubble(BubbleDirection.BBL_RECEIVED__RIGHT, "<html>This is my text message. it is super cool to have this kind of super styled tuf"
				+ "with another style of things lorem ipsum</html>", "<html>Dec. 12/2015, 12:41pm<br> </html>");
		
		SmsBubble bbl2 = new SmsBubble(BubbleDirection.BBL_SENT__LEFT, "<html>This is my text message. it is super cool to have this kind of super styled tuf"
				+ "with another style of things lorem ipsum</html>", "<html>Dec. 12/2015, 12:41pm<br> </html>");
		
		SmsBubble bbl3 = new SmsBubble(BubbleDirection.BBL_SENT__LEFT, "<html>This is my text message. it is super cool to have this kind of super styled tuf"
				+ "with another style of things lorem ipsum</html>", "<html>Dec. 12/2015, 12:41pm<br> </html>");
		
		//FOR DEBUG
		//bbl.setBorder(BorderFactory.createLineBorder(Color.black));
		//bbl.setMaximumSize(new Dimension(300, 150));
		
		pan.add(bbl1);
		pan.add(bbl2);
		pan.add(bbl3);
		frame.add(pan);
		
		frame.setVisible(true);
	}
	
	public SmsBubble(BubbleDirection direction, String messageText, String dateText) {
		//Default behavior
		this.bblDirection 	= direction;
		this.messageText 	= messageText;
		this.txtDate 		= dateText;
		
	    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    this.setDoubleBuffered(true);
	    
	    //Set style:
	    Border bordersPan;
	    if(this.bblDirection == BubbleDirection.BBL_RECEIVED__RIGHT) {
	    	clTime 		= new Color(132, 132, 132); //Grey
	    	clBbl 		= new Color(255, 255, 255); //White
		    clTxtBbl 	= Color.BLACK;
		    bordersPan = BorderFactory.createEmptyBorder(1, 8, 10, 20); //top, left, bottom, right
	    } else {
	    	clTime 		= new Color(159, 216, 188);
	    	clBbl 		= new Color(15, 157, 88);   //Green
		    clTxtBbl 	= Color.WHITE;
		    bordersPan  = BorderFactory.createEmptyBorder(1, 28, 10, 8);
	    }
	    
	    //Layout that hold the text and the date.
  		panText = new JPanel(true); //Double buffered
  		panText.setLayout(new BoxLayout(panText, BoxLayout.Y_AXIS));
  		panText.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
  		panText.setAlignmentX(SwingConstants.CENTER);
  		panText.setBackground(new Color(0,0,0,0)); //Set maximum transparency
  		
  		//Create margin/padding around the text:
  		panText.setBorder(bordersPan); 
  		
  		//Create the date
  		lblDate = new JLabel(this.txtDate, SwingConstants.CENTER);
  	    lblDate.setFont(new Font("Calibri",1,10));
  	    lblDate.setForeground(clTime);
		lblDate.setDoubleBuffered(true); //TODO: check performances
		panText.add(lblDate);
  		
  		//Create the text label
  	    lblMessage = new JLabel(this.messageText, SwingConstants.LEFT);
  	    lblMessage.setFont(new Font("Calibri",1,12));
  	    lblMessage.setForeground(clTxtBbl);
  	    lblMessage.setDoubleBuffered(true);
  	    panText.add(lblMessage);

  	    this.add(panText);
  	    
  	    //FOR DEBUG
  	    //lblDate.setBorder(BorderFactory.createLineBorder(Color.black, 1));
  	    //lblMessage.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}
	
	public SmsBubble() {
		this(BubbleDirection.BBL_RECEIVED__RIGHT, "", "");
	}
	
	public SmsBubble(BubbleDirection direction) {
		this(direction, "", "");
	}
	
	public SmsBubble(BubbleDirection direction, String messageText) {
		this(direction, messageText, "");
	}
	
	@Override
	public void paint(Graphics g) {
		
		
		
		Graphics2D graphBubble = (Graphics2D) g;
		
		//Get dimensions to create a re dimension relativeness bubble
		Dimension dimPanel 		= this.getSize();
		Dimension dimPanelMax 	= this.getMaximumSize();
		
		if(dimPanel.height > dimPanelMax.height)
			dimPanel.height = dimPanelMax.height;
		
		if(dimPanel.width > dimPanelMax.width)
			dimPanel.width = dimPanelMax.width;
		
		RoundRectangle2D roundedRectangle;
		
		//Create the rectangle bubble
		if(this.bblDirection == BubbleDirection.BBL_RECEIVED__RIGHT) {
			roundedRectangle = new RoundRectangle2D.Float(
		    		1, 1, dimPanel.width-18, dimPanel.height, 8, 8); // x pos, y pos, rect width, rect height, corner round
		} else {
			roundedRectangle = new RoundRectangle2D.Float(
		    		18, 1, dimPanel.width-19, dimPanel.height, 8, 8); // x pos, y pos, rect width, rect height, corner round
		}
	    
	    
	    graphBubble.setColor(clBbl);
	    graphBubble.draw(roundedRectangle);
	    graphBubble.fill(roundedRectangle);
	    
	    //Create the triangle TODO: make it parameterizable
	    Polygon triangle;
	    
	    if(this.bblDirection == BubbleDirection.BBL_RECEIVED__RIGHT) {
	    	int[] xPoints = {dimPanel.width-20, dimPanel.width, dimPanel.width-20};
		    int[] yPoints = {1, 1, 21};
		    triangle = new Polygon(xPoints, yPoints, xPoints.length);
	    } else {
	    	int[] xPoints = {1, 21, 21};
		    int[] yPoints = {1, 1, 21};
		    triangle = new Polygon(xPoints, yPoints, xPoints.length);
	    }
	    
	    graphBubble.draw(triangle);
	    graphBubble.fill(triangle);
	    
	    super.paint(g);
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
