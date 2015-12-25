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

public class ParamType {
	
	public boolean quoted_printable = false;
	public boolean charset_utf8 = false;
	public boolean charset_iso8859_1 = false;
	
	@Override
	public String toString() {
		String ret = "";
		
		if(quoted_printable) ret += "quoted_printable ";
		if(charset_utf8) ret += "charset_utf8 ";
		if(charset_iso8859_1) ret += "charset_iso8859_1";
				
		return ret;
	}

}
