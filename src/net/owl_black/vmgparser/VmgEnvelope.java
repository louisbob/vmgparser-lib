package net.owl_black.vmgparser;

import java.util.ArrayList;

import ezvcard.VCard;

public class VmgEnvelope {
	
	VmgEnvelope 		vEnv;
	VmgBody 			vBody;
	ArrayList<VCard> 	recipients;
	
	public VmgEnvelope() {
		// TODO Auto-generated constructor stub
		recipients = new ArrayList<VCard>();
	}
}
