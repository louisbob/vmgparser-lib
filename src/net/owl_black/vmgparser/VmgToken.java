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

public class VmgToken {
	
	String 				content;
	public VmgTokenType type;
	private int 		_lineidx;
	private int 		_colidx;
	
	//static String IDENTIFIER
	
	public VmgToken(VmgCharacter startChar) {
		this.content 	= new String();
		this.content 	+= startChar.getChar();
		this._lineidx 	= startChar.getLine();
		this._colidx 	= startChar.getCol();
	}
	
	public int getLineNb() {
		return _lineidx;
	}
	
	@Override
	public String toString() {
		//If the displayable name is null, that means there is no specific way to display the token content
		if(type._name == null) {
			 return String.format("%6d", _lineidx) + String.format("%6d", _colidx) 
					+ "   " 
					+ String.format("%-15s", type._def) 
					+ ":  "
					+ content;
		} else {
			return String.format("%6d", _lineidx) + String.format("%6d", _colidx) 
					+ "   " 
					+ String.format("%-15s", type._def) 
					+ ":  "
					+ type._name;
		}
	}
	
	
	
}
