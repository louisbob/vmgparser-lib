package net.owl_black.tovmgr;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;

import javax.swing.border.*;
import java.util.*;

/**
 * Text area that display the conversation stream of a specific contact.
 * @author LouisBob
 * @version 0.1 - 16/07/2012
 */
class ConversGUI extends JTextPane{
	/* Environment */
	
		//User-interface objects declaration
	    StyledDocument doc = (StyledDocument)this.getDocument(); // get the panes document
	    Style style = doc.addStyle(null, null);
	
	    private static final long serialVersionUID = 1L;

	    //Sms to display
	    ArrayList<mySms> toDisplaySms = new ArrayList<mySms>();
    
    /**
     * Constructor that fills a JPanel.
     * @param conversPan JPanel to fill
     * @param basicMsg Default message to display
     */
    public ConversGUI(JPanel conversPan, String basicMsg) {
        
      //instantiate the scroll pan
      JScrollPane scroller = new JScrollPane(this);
      scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      
      //Configuring borders of the list
      Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      
      //Adding border to the title object
      TitledBorder title;
      title = BorderFactory.createTitledBorder(loweredetched, "Conversation");
      title.setTitleJustification(TitledBorder.CENTER);
      
      //Adding the title-bordered to the scroll list
      scroller.setBorder(title);
      
      //Add default text provided in parameter
      this.append(basicMsg, "black");

      //Add the scrollable conversation to the panel (parameter)
      conversPan.setLayout(new BorderLayout());
      conversPan.add(scroller,BorderLayout.CENTER);
    }
    
    /**
     * Simply refresh the stream conversation with a new database.
     * @param database The database to renew
     */
    public void refresh(ArrayList<mySms> database){
        this.setText("");
        for(mySms sms : database) {
            displaySms(sms);
        }
    }
    
    /**
     * Refresh the database and display the specified phone number conversation stream.
     * @param database Database to renew
     * @param phoneNumber Phone number to display
     */
    public void refresh(ArrayList<mySms> database, String phoneNumber){
    	
        this.setText("");
        for(mySms sms : database) {
            if(sms.getSenderName() != null){
	            if(sms.getSenderName().equals(phoneNumber)) 
	            	displaySms(sms);
            }
	        else
	           if(sms.getPhoneNum() != null){
			            if(sms.getPhoneNum().equals(phoneNumber)) 
			            	displaySms(sms);
	            }
        }
    }
    
    /**
     * Sort the stream conversation by sorting the order of sms in the database.
     * @param db	
     * @return Database of sorted sms
     */
    public ArrayList<mySms> sortByDate(ArrayList<mySms> database) {
        
        ArrayList<mySms> sorted_db = new ArrayList<mySms>();
        
        while(database.size() != 0) {
            long min=99999999999999L;
            int min_index = -1;
            
            for(int j=0; j< database.size(); j++){
                
                mySms CurrentSms = database.get(j);
                long currentDate;
                
                if(j==0) {
                min = CurrentSms.getSec() + 
                            100*CurrentSms.getMin() + 
                            10000*CurrentSms.getHour() + 
                            1000000*CurrentSms.getDay() + 
                            100000000*CurrentSms.getMonth() + 
                            (10000000000L)*CurrentSms.getYear();
                            
                min_index = 0;
                }
                else
                {
                    currentDate = CurrentSms.getSec() + 
                            100*CurrentSms.getMin() + 
                            10000*CurrentSms.getHour() + 
                            1000000*CurrentSms.getDay() + 
                            100000000*CurrentSms.getMonth() + 
                            (10000000000L)*CurrentSms.getYear();
                            
                    if(currentDate < min){
                        min = currentDate;
                        min_index = j;
                    }
                }
            }
            
            sorted_db.add(database.get(min_index));
            database.remove(min_index);
        }
        return sorted_db;
    }
    
    
    /**
     * Add a SMS to the stream conversation, format by adding date, time, and text content.
     * @param sms The SMS to add
     */
    public void displaySms(mySms sms) {
        //Adding sms details like date and time
        appendNewLine("","");
        appendNewLine("","");
        String fstLine;
        fstLine = "";
        
        //Add the date
        //DAY
        if((sms.getDay()/10) == 0)
            fstLine += "0" + Integer.toString( sms.getDay());
        else
            fstLine += Integer.toString(sms.getDay());
        
        fstLine += "/";
        
        //MONTH
        if((sms.getMonth()/10) == 0)
            fstLine += "0" + Integer.toString( sms.getMonth());
        else
            fstLine += Integer.toString(sms.getMonth());
            
        fstLine += "/";
        
        //YEAR
        fstLine += Integer.toString(sms.getYear());
        
        fstLine += " - ";
        
        //HOURS
        if((sms.getHour()/10) == 0)
            fstLine += "0" + Integer.toString( sms.getHour() );
        else
            fstLine += Integer.toString( sms.getHour() );
            
        fstLine +=":";
        
        //MINS
        if((sms.getMin()/10) == 0)
            fstLine += "0" + Integer.toString( sms.getMin());
        else
            fstLine += Integer.toString(sms.getMin());
        
        fstLine +=":"; 
        //SEC
        if((sms.getSec()/10) == 0)
            fstLine += "0" + Integer.toString( sms.getSec());
        else
            fstLine += Integer.toString(sms.getSec());
                
        //Align left if is received
        	if(sms.getReceived()) {
            append(fstLine, "black", "italic", "left");
            //Adding sms text
            appendNewLine("","");
        	} 
        	else {
        		append(fstLine, "black", "italic", "right");
                appendNewLine("","");
        	}
            
            String s = "";
            
            switch(sms.getEncode()) {
            
            case mySms.UTF8_DEFAULT : 
            	
            	s = sms.getText();
            	break;
            	
            case mySms.UTF8_CLASSICAL :
            	
            	s = mySms.replaceNonUTF8(sms.getText());
            	break;
            	
            case mySms.UTF8_ARABIC :
            	
            	byte[] sBinary = PersianProcess.persianProcess(sms.getText());
            	
            	try {
					 s = new String(sBinary, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
            	break;
            	
            default :
            	
            	s = sms.getText();
            	break;
            }
            
            if(sms.getReceived())
            	append(s, "blue", "", "left");
            else
            	append(s, "red", "", "right");
    }
    
    
    /**
     * Add provided text at the end of the conversation.
     * @param message Text to add
     * @param color Color of the text
     */
    public void appendNewLine(String message, String color){
        if (getText().equals("")){
            append(message, color);
        }
        else{
            append("\n" + message, color);
        }
    }
    
    /**
     * Add provided text at the end of the StyledDocument doc.
     * @param message Text to add
     * @param color Color of the text
     */
    private void append(String message, String color){
        try {
            /* other options */
            //StyleConstants.setBold(style, true);
            //StyleConstants.setFontFamily(style, "SansSerif");
            //StyleConstants.setFontSize(style, 30);
            //StyleConstants.setBackground(style, Color.blue);
            //StyleConstants.setForeground(style, Color.red);
            
            style = chgColor(style, color);
            
            // Append to document
            doc.insertString(doc.getLength(), message, style);
            
            // be sure it scrolls down
            setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
        System.out.println("Test");
        }
    }
    
    public void append(String message, String color, String format){
        try {
            // other options
            
            //StyleConstants.setBold(style, true);
            //StyleConstants.setFontFamily(style, "SansSerif");
            //StyleConstants.setFontSize(style, 30);
            //StyleConstants.setBackground(style, Color.blue);
            //StyleConstants.setForeground(style, Color.red);
            
            style = chgColor(style, color);
            style = chgFormat(style, format);
            
            // Append to document
            doc.insertString(doc.getLength(), message, style);
            
            // be sure it scrolls down
            setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
        System.out.println("Test");
        }
    }
    
    public void append(String message, String color, String format, String align){
        try {
            // other options
            
            //StyleConstants.setBold(style, true);
            //StyleConstants.setFontFamily(style, "SansSerif");
            //StyleConstants.setFontSize(style, 30);
            //StyleConstants.setBackground(style, Color.blue);
            //StyleConstants.setForeground(style, Color.red);

            style = chgColor(style, color);
            style = chgFormat(style, format);
            style = chgAlign(style, align);
            
            // Append to document
            this.setParagraphAttributes(style,true);  
            doc.insertString(doc.getLength(), message, style);
            
            // be sure it scrolls down
            setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
        System.out.println("Test");
        }
    }
    
    private Style chgColor(Style myStyle, String color ){
            //Color
            if (color.equals("black")){
                StyleConstants.setForeground(myStyle, Color.black);
            }
            else if (color.equals("red")){
                StyleConstants.setForeground(myStyle, Color.red);
            }
            else if (color.equals("green")){
                StyleConstants.setForeground(myStyle, Color.green);
            }
            else if (color.equals("purple")){
                StyleConstants.setForeground(myStyle, Color.magenta);
            }
            else if (color.equals("orange")){
                StyleConstants.setForeground(myStyle, Color.orange);
            }
            else if (color.equals("blue")){
                StyleConstants.setForeground(myStyle, Color.blue);
            }
            
            return myStyle;
    }
    
    private Style chgFormat(Style myStyle, String myFormat) {
            if (myFormat.equals("bold")){
                StyleConstants.setBold(myStyle, true);
            }
            else if (myFormat.equals("italic")) {
                StyleConstants.setItalic(myStyle, true);
            }
            else {
                StyleConstants.setItalic(myStyle, false);
                StyleConstants.setBold(myStyle, false);
            }
            
            return myStyle;
    }
    
    private Style chgAlign(Style myStyle, String align) {
            //Align
            if (align.equals("left")){
                StyleConstants.setAlignment(myStyle, StyleConstants.ALIGN_LEFT);
            }
            else if (align.equals("right")) {
                StyleConstants.setAlignment(myStyle, StyleConstants.ALIGN_RIGHT);
            }
            else if (align.equals("center")) {
                StyleConstants.setAlignment(myStyle, StyleConstants.ALIGN_CENTER);
            }
            
            return myStyle;
    }
}