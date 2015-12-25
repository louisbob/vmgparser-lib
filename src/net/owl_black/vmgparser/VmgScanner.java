package net.owl_black.vmgparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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

public class VmgScanner {
	
	private int srcidx;
	private int lineidx;
	private int colidx;
	private BufferedReader bufRin;
	
	static String UTF8 = "UTF-8";
	static String UTF16 = "UTF-16";
	static String UTF16_LITTLE_ENDIAN = "UTF-16LE";
	static String UTF16_BIG_ENDIAN = "UTF-16BE";
	
	public VmgScanner(File f, String encoding) throws UnsupportedEncodingException {
		
		lineidx = 0;
		colidx = -1;
		
		try {
			//Open the file
			bufRin = new BufferedReader(
			           new InputStreamReader(
			                      new FileInputStream(f), encoding));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public VmgCharacter get() throws IOException{
		
		//char is 16bit in java
		char c = (char) bufRin.read();
		
		//hold counts
		if (c == '\n') {
			lineidx += 1;
			colidx = -1;
		}
		
		colidx += 1;
		
		return new VmgCharacter(c, srcidx, lineidx, colidx);
	}
	
	 
}
