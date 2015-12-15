package net.owl_black.vmgparser;

import java.util.Calendar;

public class VmgBodyExtended extends VmgBody {
	
	public enum XIrmcStatus {
		UNREAD, READ
	}
	
	public enum XIrmcBox {
		INBOX, OUTBOX, SENTBOX
	}
	
	private XIrmcStatus XIStatus;
	private XIrmcBox 	XIBox;
	private Calendar	date;
	private String		content;
	
	public VmgBodyExtended() {
	}

	@Override
	public String toString() {
		
		String ret = "Status   :    " + XIStatus.name() +
				"\nBox   :    " + XIBox.name() + 
				"\nContent   :" + content;
		
		
		ret += "\n";
		
		return ret;
	}
	
	/*
	 * Getter and Setters
	 */
	
	public XIrmcStatus getXIStatus() {
		return XIStatus;
	}

	public void setXIStatus(XIrmcStatus xIStatus) {
		XIStatus = xIStatus;
	}

	public XIrmcBox getXIBox() {
		return XIBox;
	}

	public void setXIBox(XIrmcBox xIBox) {
		XIBox = xIBox;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
