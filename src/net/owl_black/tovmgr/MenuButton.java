package net.owl_black.tovmgr;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;

public class MenuButton extends JMenu{
	
	/* Environment */
		private static final long serialVersionUID = 1L;
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