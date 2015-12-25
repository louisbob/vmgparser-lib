package net.owl_black.vmgparser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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

@RunWith(Parameterized.class)
public class TestVmgLexer {
	
	//Test resources
	private String 		_inputFile;
	private VmgLexer 	_lexer;
	
	//Input VMG files for recursive test on files TODO: merge files among different test
	@Parameterized.Parameters
	public static Collection<Object[]> inputVMGs() {
		return Arrays.asList(new Object[][] {
	         //{"D:\\louisbob\\programming\\Resources\\VMG files\\+351253471692_Tento_2.vmg", VmgScanner.UTF8},
	         //{"D:\\louisbob\\programming\\Resources\\VMG files\\+351253471692_Tento.vmg", VmgScanner.UTF8},
	         {"D:\\louisbob\\programming\\Resources\\VMG files\\chinese_filename.vmg", VmgScanner.UTF16_LITTLE_ENDIAN},
	         //{"D:\\louisbob\\programming\\Resources\\VMG files\\Cal TW\\博涵 _2010-11-08(2).vmg", VmgScanner.UTF16_LITTLE_ENDIAN}
	      });
	}
	
	public TestVmgLexer(String file_path, String file_encoding) throws UnsupportedEncodingException {
		_inputFile = file_path;
		
		File f = new File(file_path);
		_lexer  = new VmgLexer(f, file_encoding);
	}
	
	@Test
	public void lexerTest1() throws UnsupportedEncodingException {

		System.out.println("============= " + _inputFile +"\n\n");
		
		VmgToken tok = _lexer.get();
		System.out.println(tok.toString());
		
		do {
			tok = _lexer.get();
			if(tok == null)
				fail(); //Something have not be handled correctly in the VMG file.
			
			System.out.println(tok.toString());
		} while(!tok.type.equals(VmgTokenType.EOF));
		
		
	}
}
