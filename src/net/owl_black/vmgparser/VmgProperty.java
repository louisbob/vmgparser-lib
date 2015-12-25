package net.owl_black.vmgparser;

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

public class VmgProperty implements IVisitable {
	
	ParamType 	params;
	String		name;
	String		value;

	public VmgProperty(String name) {
		this.name = name;
		this.value = "";
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
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
