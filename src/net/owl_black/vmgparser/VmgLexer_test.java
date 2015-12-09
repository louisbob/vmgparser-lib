package net.owl_black.vmgparser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class VmgLexer_test {
	
	//Test resources
	private String 		_inputFile;
	private VmgLexer 	_lexer;
	
	//Input VMG files for recursive test on files TODO: merge files among different test
	@Parameterized.Parameters
	public static Collection inputVMGs() {
		return Arrays.asList(new Object[][] {
	         //{"D:\\louisbob\\programming\\Resources\\VMG files\\+351253471692_Tento_2.vmg", VmgScanner.UTF8},
	         //{"D:\\louisbob\\programming\\Resources\\VMG files\\+351253471692_Tento.vmg", VmgScanner.UTF8},
	         {"D:\\louisbob\\programming\\Resources\\VMG files\\chinese_filename.vmg", VmgScanner.UTF16_LITTLE_ENDIAN},
	         //{"D:\\louisbob\\programming\\Resources\\VMG files\\Cal TW\\博涵 _2010-11-08(2).vmg", VmgScanner.UTF16_LITTLE_ENDIAN}
	      });
	}
	
	public VmgLexer_test(String file_path, String file_encoding) throws UnsupportedEncodingException {
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
