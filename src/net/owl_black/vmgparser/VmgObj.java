package net.owl_black.vmgparser;

import java.util.ArrayList;
import java.util.List;

import ezvcard.VCard;

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

public class VmgObj implements IVisitable {
	private List<VmgProperty>	vProp;
	private List<VCard>			vOriginator; //[Optionnal]
	private VmgEnvelope			vEnv;
	
	public VmgObj() {
		//In a vmg file, there is at least one property : the version number.
		//It is why we instantiate this object.
		setvProp(new ArrayList<VmgProperty>()); 
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

	/*
	 * Getter and Setters
	 */
	public List<VmgProperty> getvProp() {
		return vProp;
	}

	public void setvProp(List<VmgProperty> vProp) {
		this.vProp = vProp;
	}
	
	public List<VCard> getvOriginator() {
		return vOriginator;
	}

	public void setvOriginator(List<VCard> vOriginator) {
		this.vOriginator = vOriginator;
	}

	public VmgEnvelope getvEnv() {
		return vEnv;
	}

	public void setvEnv(VmgEnvelope vEnv) {
		this.vEnv = vEnv;
	}
}
