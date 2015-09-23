package net.owl_black.tovmgr;
import javax.swing.*;
import java.awt.*;
import javax.swing.BorderFactory; 
import javax.swing.border.*;
import java.util.*;
import javax.swing.event.*;


/**
 * This class is used to display the contact list. When clicking on a phone number, it will generates 
 * automatically an event, with the number that has been clicked.
 * 
 * @author Louisbob
 * @version v0.2 - 16/07/2012
 */
public class ContactGUI
{
    /* Environment */
	
		//User-interface objects declaration
	 	JScrollPane scrollContactList;
	    JList list;
	
	    //Databases variables
	    private ArrayList<String> phoneNumbers = new ArrayList<String>();  //Phone number database
	    private ArrayList<mySms> smsConversation = new ArrayList<mySms>(); //
	    private ArrayList<mySms> smsDB = new ArrayList<mySms>();
	    
	    //Litening variable
	    private final EventListenerList listeners = new EventListenerList(); //Used to interract with other GUIs elements

     /**
      * Constructor of the ContacGUI class. It fills a JPanel provided in parameter with 
      * a list of phone numbers that are able to be selected.
     * @param database Database to analyze
     * @param contactPan Panel where to include user-interface
     */
    public ContactGUI(ArrayList<mySms> database, JPanel contactPan) {
      
      //Plug the database
      smsDB = database;
      
      //Fill the phone number database using the SMS database (parameter)
      phoneNumbers = getPhoneOrName(database);
      
      //JList instantiation
      list = new JList();
      
      //Allow only one line to be selected
      list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      
      //Add selection listener      
      list.addListSelectionListener(new ListSelListener());

      //Fill the JList
      list.setListData(phoneNumbers.toArray());
      
      //Add scroller to the list
      scrollContactList = new JScrollPane(list);
      scrollContactList.setPreferredSize(new Dimension(150, 300));
      
      //Configuring borders of the list
      Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      
      //Adding border to a title object
      TitledBorder title;
      title = BorderFactory.createTitledBorder(loweredetched, "Contact List");
      title.setTitleJustification(TitledBorder.LEFT);
      
      //Add border to the scrollable list
      scrollContactList.setBorder(title); 
      
      //Add the scrollable list to the panel (parameter)
      contactPan.setLayout(new BorderLayout());
      contactPan.add(scrollContactList,BorderLayout.EAST);
    }
    
    /**
     * Refresh the phone number array list with a new MySms database.
     * @param database The new SMS database to scan.
     */
    public void refresh(ArrayList<mySms> database) {
    	
    	//Flush all local databases
        smsDB = null;
        phoneNumbers = null;
        smsConversation = null;
        
        //Plug the new database (parameter)
        smsDB = database;
        
        //Renew phone number list
        phoneNumbers = getPhoneOrName(smsDB);
        
        //Display all the phone numbers
        list.setListData(phoneNumbers.toArray());
    }
    
    /**
     * Create a conversation stream of a specific name or phone number.
     * @param phoneOrName The phone number or the name to display
     */
    public void fillConversation(String phoneOrName) {
        
        //Environment
        boolean alrdyAdded = false;
        
        //Algorithm
        
        //We clear the conversation:
        smsConversation = null;
        smsConversation = new ArrayList<mySms>();
        
        //We scan the database to find messages that match with the phone number or the name
        for(mySms currentSms : smsDB) {
            
            //We check if the name match
            if(currentSms.getSenderName() != null) {
                    
                if (currentSms.getSenderName().equals(phoneOrName) ){
                   smsConversation.add(currentSms) ;
                   alrdyAdded = true;
                }
            }
            
            //Otherwise, we'll try with the phone number
            if((currentSms.getPhoneNum() != null) && (!alrdyAdded)) {
                
                if( currentSms.getPhoneNum().equals(phoneOrName) )
                	smsConversation.add(currentSms);
            }
            alrdyAdded = false;
        }
    }
    

    /**
     * Method that looks for name (or phone number) inside the MySms database.
     * @param database The complete MySms database.
     * @return List of name (or phone if there is no name)
     */
    public ArrayList<String> getPhoneOrName(ArrayList<mySms> database){

        ArrayList<String> phoneNum = new ArrayList<String>();
        Boolean alrdyExist = false;

        for(int j=0; j < database.size(); j++) {
            
            //Gets the sms from the database
            mySms sms = database.get(j);
            
            //Get the name:
            String phone = sms.getSenderName();
            
            //Gets the phonenumber corresponding
            if(phone == null)
            	phone = sms.getPhoneNum();
            
            //scan the existing array to see if the number already exist
            for(int k=0; k < phoneNum.size(); k++) {
                String pn = phoneNum.get(k);
                if( pn.equals(phone) ) alrdyExist = true;
            }
            
            //Add or not the phone number to the list
            if( !alrdyExist ) {
                phoneNum.add(phone);
            }
            else alrdyExist = false;
        }
        
        return phoneNum;
    }
    
 
	/**
     *      LISTENER CONFIGURATION
     * 
     */
    
    /**
     * @param listener Object to add to the listener list.
     */
    public void addListClickListener(ListClickListener listener) {
        listeners.add(ListClickListener.class, listener);
    }
    
    /**
     * @param listener Object to remove from the listener list.
     */
    public void removeListClickListener(ListClickListener listener) {
        listeners.remove(ListClickListener.class, listener);
    }
    
    /**
     * @return List of listeners.
     */
    public ListClickListener[] getContactListeners() {
        return listeners.getListeners(ListClickListener.class);
    }
    
   
    /**
     * Class that cares about JList selection
     * @author LouisBob
     * @version 0.1 - 16/07/2012
     */
    class ListSelListener implements ListSelectionListener 
    {
    	
        /* This method is called each time the user changes the set of selected items
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent evt) {
            // When the user release the mouse button and completes the selection,
            // getValueIsAdjusting() becomes false
            if (!evt.getValueIsAdjusting()) {
                JList list = (JList)evt.getSource();
    
                // Get the string that has been selected
                String phone = phoneNumbers.get(list.getSelectedIndex());
                //System.out.println(phone);
                fillConversation(phone);
                
                for ( ListClickListener listener : getContactListeners() )
                    listener.update(phone);
            }
        }
    }
}
