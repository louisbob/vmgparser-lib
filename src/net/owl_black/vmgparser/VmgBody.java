package net.owl_black.vmgparser;

import java.util.Calendar;

public class VmgBody {
	
	public enum XIrmcStatus {
		UNREAD, READ
	}
	
	public enum XIrmcBox {
		INBOX, OUTBOX, SENTBOX
	}
	
	XIrmcStatus XIStatus;
	XIrmcBox 	XIBox;
	Calendar	date;
	String		content;
	
	public VmgBody() {
	}

	@Override
	public String toString() {
		
		String ret = "Status   :    " + XIStatus.name() +
				"\nBox   :    " + XIBox.name() + 
				"\nContent   :" + content;
		
		
		ret += "\n";
		
		return ret;
	}
}
