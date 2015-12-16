package net.owl_black.vmgparser;

import static org.junit.Assert.*;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestVmgScanner {
	
	//Test resources
	VmgScanner 	scanner;
	String 		_inputFile;
	
	//Input VMG files for recursive test on files
	@Parameterized.Parameters
	public static Collection inputVMGs() {
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
