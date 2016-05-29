package net.owl_black.vmgparser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

public class VmgBodyExtended extends VmgBody {
	
	private List<VmgProperty> vProp;
	
	
	private LocalDateTime	date;
	private String			content;
	
	public VmgBodyExtended() {
		setvProp(new ArrayList<VmgProperty>());
	}

	@Override
	public String toString() {
		
		String ret = "Content   :" + content;
		
		
		ret += "\n";
		
		return ret;
	}
	
	/*
	 * Getter and Setters
	 */

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<VmgProperty> getvProp() {
		return vProp;
	}

	public void setvProp(List<VmgProperty> vProp) {
		this.vProp = vProp;
	}
}
