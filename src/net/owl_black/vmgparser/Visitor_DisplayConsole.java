package net.owl_black.vmgparser;

import java.util.Collection;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;

/* Copyright (c) 2012-2015, Louis-Paul CORDIER
 * All rights reserved.
 * 
 * This file is part of vmgparser library.
 * Vmgparser library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Vmgparser library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with vmgparser library.  If not, see <http://www.gnu.org/licenses/>. */

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
			//tab_number++;
			println("[VCARD]");
			String test = Ezvcard.write(vcardList).version(this.vcard_version).go();
			print(test);
			println("[\\VCARD]");
			//tab_number--;
		}
		
		VmgBody vB = e.getvBody();
		if(vB != null)
			vB.accept(this);
		
		VmgEnvelope vE = e.getvEnv();
		if(vE != null)
			vE.accept(this);
			
		tab_number--;
		println("[\\VENV]");
	}

	@Override
	public void visit(VmgBody e) {
		println("[VBODY]");
		tab_number++;
		
		String text = e.getContent();
		if(text != null)
			println(text);
		
		tab_number--;
		println("[\\VBODY]");
	}
	
	@Override
	public void visit(VmgBodyExtended e) {
		println("[VBODY]");
		tab_number++;
		
		for (VmgProperty vP : e.getvProp()) {
			vP.accept(this);
		}
		
		tab_number--;
		println("[\\VBODY]");
	}

}
