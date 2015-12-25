package net.owl_black.vmgparser;

import static org.junit.Assert.*;

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
public class TestVmgParser {
	
	
	//Test resources
		VmgParser 	parser;
		String 		_inputFile;
		boolean		_test_must_success;
		
		//Input VMG files for recursive test on files
		@Parameterized.Parameters
		public static Collection<Object[]> inputVMGs() {
			return Arrays.asList(new Object[][] {/*
				{"junit\\1_wrong_vmg_version.vmg", VmgScanner.UTF8, false},
				{"junit\\2_bad_linefeed.vmg", VmgScanner.UTF8, true},
				{"junit\\2_bad_linefeed2_+351253471692_Tento.vmg", VmgScanner.UTF8, true},
				{"D:\\louisbob\\programming\\Resources\\smspapa\\+33608898623_Bonjo_001.vmg", VmgScanner.UTF8, true},*/
				{"D:\\louisbob\\programming\\Resources\\VMG files\\chinese_filename.vmg", VmgScanner.UTF16_LITTLE_ENDIAN, true},
				
		         //{"D:\\louisbob\\programming\\Resources\\VMG files\\+351253471692_Tento_2.vmg", VmgScanner.UTF8},
				/*
		         {"D:\\louisbob\\programming\\Resources\\VMG files\\chinese_filename.vmg", VmgScanner.UTF16_LITTLE_ENDIAN},
		         {"D:\\louisbob\\programming\\Resources\\VMG files\\Cal TW\\博涵 _2010-11-08(2).vmg", VmgScanner.UTF16_LITTLE_ENDIAN},
		         {"D:\\louisbob\\programming\\Resources\\VMG files\\+351253471692_Tento.vmg", VmgScanner.UTF8},
		         {"D:\\louisbob\\programming\\Resources\\VMG files\\918985992080_Good _008.vmg", VmgScanner.UTF8},
		         {"D:\\louisbob\\programming\\Resources\\VMG files\\1105151213_+2280136788.vmg", VmgScanner.UTF8},
		         {"D:\\louisbob\\programming\\Resources\\VMG files\\aabid\\20130403153459_+919870046620.vmg", VmgScanner.UTF8}*/
		      });
		}
		
		//Constructor
		public TestVmgParser(String file_path, String file_encoding, boolean test_must_success) throws UnsupportedEncodingException {
			_inputFile = file_path;
			File f = new File(file_path);
			parser = new VmgParser(f, file_encoding);
			_test_must_success = test_must_success;
		}
		
		@Test
		public void testVmgParser() throws IOException {
			
			System.out.println("============= PARSING : " + _inputFile + "\n");
			VmgObj myVmg = parser.vmg_object(true);
			
			//Flush the error logger buffer.
			System.err.flush();
			
			//Check if the test must success or not.
			if(myVmg == null) {
				if(_test_must_success) {
					//Parametrized test indicate that this test must pass, 
					//and has no vmg file has been generated, just make the test fails.
					fail(); 
				}
				else return;
			} else {
				if(!_test_must_success)
					fail();
			}
			
			//Dispay the content of the obtained vmg by using the visitor design pattern.
			Visitor_DisplayConsole displayer = new Visitor_DisplayConsole(ezvcard.VCardVersion.V3_0, 4);
			myVmg.accept(displayer);
			
			System.out.println("Parsing finished.\n\n");
		}
}
