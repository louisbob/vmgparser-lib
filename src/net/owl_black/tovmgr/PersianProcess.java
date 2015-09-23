package net.owl_black.tovmgr;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Byte;
import java.nio.ByteBuffer;

public class PersianProcess {
	
	public static void main(String[] args) {
		try {
			persianProcess("C:\\Users\\LouisBob\\Localdata\\Bureau\\m_fin.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static byte[] persianProcess(String arabText) {
    	
    	//Init beacons
    	String D9 = "D93D";
    	String D8 = "D83D";
    	String DB = "DB3D";
    	String DA = "DA3D";
    	
    	//Convert string to byte array
    	
    	try {
	    	byte[] data = arabText.getBytes();
	
	    	
	    	/*
	    	//Open file
	    	File fd = new File(filename);
	    	
	    	//Get raw data (byte)
	    	byte[] data = readFileToByteArray(fd);
			*/
	    	
	    	//Convert it into a string to be exploited
	    	String hex = getHex(data);
	    	
	    	int pos = 0;
	    	while(pos+2 < data.length) {
	    		
	    		byte[] hex1 = new byte[1];
	    		hex1[0] = data[pos];
	    		
	    		byte[] hex2 = new byte[1];
	    		hex2[0] = data[pos+1];
	    		
	    		//Get the hex code of the two bytes
	    		String sHex = getHex(hex1) + getHex(hex2);
	    		
	    		//Detect if bytes match with the D8 OR D9 pattern:
	    		if(	    
	    				(sHex.indexOf(D8) != -1) || 
	    				(sHex.indexOf(D9) != -1) || 
	    				(sHex.indexOf(DB) != -1) ||
	    				(sHex.indexOf(DA) != -1)   ) 
	    		{
	    			
	    			//Remove the equal character
	    			data = removeByteAt(data, pos+1);
	    			
	    			//Get the next two bytes (! data hasn't the same size as beginning!)
	    			byte[] hexC1 = new byte[1]; 
	    			byte[] hexC2 = new byte[1];
	    			
	    			hexC1[0] = data[pos+1];
	    			hexC2[0] = data[pos+2];
	    			
	    			//We have two bytes for each number. At the end, we will get
	    			//only one byte. It's why we need to delete one byte once again:
	    			data = removeByteAt(data, pos+1);
	    			
	    			//Get value of each byte in UTF8:
	    			String str = new String(hexC1, "UTF-8");
	    			String str2 = new String(hexC2, "UTF-8");
	    			
	    			//Now recreate the correct hex value
	    			str = str + str2;
	    			
	    			//And parse this hex value into byte...
	    			byte newByte = hexStringToByte(str);
	    			
	    			//...and finally add the modification to the data array:
	    			data[pos+1] = newByte;
	    		}
	    		pos++;
	    	}
	    	
	    	String hex2 = getHex(data);
	    	
	    	//byteArrayToFile(data);
	    	System.out.println("Ok!");
	    	//data = addUTF8Beacon(data);
	    	String s = new String(data);
	    	
	    	return data;
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
    
    
	public static byte[] readFileToByteArray(File file) throws IOException {
	    ByteArrayOutputStream ous = null;
	    InputStream ios = null;
	    try {
	        byte[] buffer = new byte[4096];
	        ous = new ByteArrayOutputStream();
	        ios = new FileInputStream(file);
	        int read = 0;
	        while ((read = ios.read(buffer)) != -1)
	            ous.write(buffer, 0, read);
	    } finally {
	        try {
	            if (ous != null)
	                ous.close();
	        } catch (IOException e) {
	            // swallow, since not that important
	        }
	        try {
	            if (ios != null)
	                ios.close();
	        } catch (IOException e) {
	            // swallow, since not that important
	        }
	    }
	    return ous.toByteArray();
	}
	
	public static byte[] removeByteAt(byte[] bArray, int pos) {
		
		//create a new byte array
		byte[] buf = new byte[bArray.length-1];
		
		if(pos < bArray.length) {
			//Copy the first part of the array
			System.arraycopy(bArray, 0, buf, 0, pos);
			
			//Copy the last part of the array
			System.arraycopy(bArray, pos+1, buf, pos, bArray.length-1-pos);
		}
		
		return buf;
	}
	
	static final String HEXES = "0123456789ABCDEF";
	
	public static String getHex( byte [] raw ) {
		
		if ( raw == null ) {
	      return null;
	    }
	    final StringBuilder hex = new StringBuilder( 2 * raw.length );
	    for ( final byte b : raw ) {
	      hex.append(HEXES.charAt((b & 0xF0) >> 4))
	         .append(HEXES.charAt((b & 0x0F)));
	    }
	    return hex.toString();
	}
	
	public static String removeCharAt(String s, int pos) {
		   StringBuffer buf = new StringBuffer( s.length() - 1 );
		   buf.append( s.substring(0,pos) ).append( s.substring(pos+1) );
		   return buf.toString();
	}
	
	public static byte hexStringToByte(String s) {
	    
		if(s.length() > 2) {
			return (Byte) null;
		}
		
	    byte octet = (byte) ((Character.digit(s.charAt(0), 16) << 4)
	                             + Character.digit(s.charAt(1), 16));
	    return octet;
	}
	
	public static void byteArrayToFile(byte[] byteArray) {
		 String strFilePath = "C:\\Users\\LouisBob\\Localdata\\Bureau\\output.txt";
		   
	     try
	     {
		      FileOutputStream fos = new FileOutputStream(strFilePath);
		      
		      // To write byte array to a file, use
		      // void write(byte[] bArray) method of Java FileOutputStream class.
		      // This method writes given byte array to a file.   
		       fos.write(byteArray);
		       fos.close();
	     }
	     catch(FileNotFoundException ex)
	     {
	    	 System.out.println("FileNotFoundException : " + ex);
	     }
	     catch(IOException ioe)
	     {
	    	 System.out.println("IOException : " + ioe);
	     }
	}
	
	public static byte[] addUTF8Beacon(byte[] data) {
		
		
		//create a new byte array
		byte[] buf = new byte[data.length+3];
		byte[] utf8 = {(byte)239, (byte)187,(byte)191};
		
		byte[] dat1 = data.clone();
			//Copy the first part of the array
		System.arraycopy(utf8, 0, buf, 0, 3);
			
		//Copy the last part of the array
		int test = dat1.length;
		System.arraycopy(data, 0, buf, 3, dat1.length);
		
		return buf;
	}
	
	public static String UTF8Beacon() {
		
		byte[] utf8 = {(byte)239, (byte)187,(byte)191};
		String s = new String(utf8);
	
	return s;
	}
}
