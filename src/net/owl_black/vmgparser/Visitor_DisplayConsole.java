package net.owl_black.vmgparser;

import java.util.Collection;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;

public class Visitor_DisplayConsole implements IVisitor{

	private VCardVersion vcard_version;
	private int tab_number;
	private int tab_value;
	
	public Visitor_DisplayConsole(VCardVersion version, int tab_value) {
		this.vcard_version = version;
		this.tab_value = tab_value;
		tab_number = 0;
	}
	
	private void println(String x) {
		String spaces;
		
		if(tab_number == 0)
			spaces = "";
		else
			spaces = String.format("%"+tab_number*tab_value+"s", "");
		
		System.out.println(spaces + x);
	}
	
	private void print(String x) {
		System.out.print(x);
	}
	
	@Override
	public void visit(IVisitable e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(VmgObj e) {
		println("[VMG]");
		tab_number++;
		println("");

		//Display properties
		for (VmgProperty vP : e.getvProp()) {
			vP.accept(this);
		}
		
		//Display vCard in the VMG header. We can't implement visitor as its part of the ezvcard library.
		println("[VCARD]");
		String test = Ezvcard.write(e.getvOriginator()).version(this.vcard_version).go();
		print(test);
		println("[\\VCARD]\n");
		
		//Display vEnvelope
		e.getvEnv().accept(this);
		
		tab_number--;
		println("[\\VMG]");
	}

	@Override
	public void visit(VmgProperty e) {
		
		println("PROP    :    " + e.name);
		println("VALUE   :    " + e.value);

		if(e.params != null)
			println("PARAMS :    " + e.params);
		
		println("");
	}

	@Override
	public void visit(VmgEnvelope e) {
		println("[VENV]");
		tab_number++;
		
		Collection<VCard> vcardList = e.getvOriginator();
		if(vcardList != null) {
			String test = Ezvcard.write(vcardList).version(this.vcard_version).go();
			println(test);
		}
		
		VmgBody vB = e.getvBody();
		if(vB != null)
			vB.accept(this);
			
		tab_number--;
		println("[\\VENV]");
	}

	@Override
	public void visit(VmgBody e) {
		println("[VBODY]");
		tab_number++;
		
		String text = e.getContent();
		if(text != null)
			print(text);
		
		tab_number--;
		println("[\\VBODY]");
	}
	@Override
	public void visit(VmgBodyExtended e) {
		// TODO Auto-generated method stub
		
	}

}
