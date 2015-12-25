package net.owl_black.vmgparser;

import java.io.File;
import java.io.IOException;
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
public class TestVmgScanner {
	
	//Test resources
	VmgScanner 	scanner;
	String 		_inputFile;
	
	//Input VMG files for recursive test on files
	@Parameterized.Parameters
	public static Collection<Object[]> inputVMGs() {
		return Arrays.asList(new Object[][] {
	         {"D:\\louisbob\\programming\\Resources\\VMG files\\+351253471692_Tento_2.vmg", VmgScanner.UTF8},
	         {"D:\\louisbob\\programming\\Resources\\VMG files\\chinese_filename.vmg", VmgScanner.UTF16_LITTLE_ENDIAN},
	         {"D:\\louisbob\\programming\\Resources\\VMG files\\Cal TW\\博涵 _2010-11-08(2).vmg", VmgScanner.UTF16_LITTLE_ENDIAN}
	      });
	}
	
	//Constructor
	public TestVmgScanner(String file_path, String file_encoding) throws UnsupportedEncodingException {
		_inputFile = file_path;
		File f = new File(file_path);
		scanner = new VmgScanner(f, file_encoding);
	}
	
	@Test
	public void testVmgScanner() throws IOException {
		
		System.out.println("=== Testing following file: " + _inputFile + "=== ");
		VmgCharacter vc;
		do {
			vc = scanner.get();
			System.out.println(vc.toString());
		} while(!vc.isEOF());
		
		System.out.println("\n");
	}
}
