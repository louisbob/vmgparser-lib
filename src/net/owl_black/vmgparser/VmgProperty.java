package net.owl_black.vmgparser;

import java.util.ArrayList;

public class VmgProperty {
	
	ParamType 	params;
	String		name;
	String		value;

	public VmgProperty(String name) {
		this.name = name;
		this.value = "";
	}
	
	@Override
	public String toString() {
		
		String ret = "PROP   :    " + this.name +
				"\nVALUE  :    " + this.value;
		
		if(params != null)
			ret += "\nPARAMS :    " + this.params;
		
		ret += "\n";
		
		return ret;
	}
}
