package net.owl_black.vmgparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
