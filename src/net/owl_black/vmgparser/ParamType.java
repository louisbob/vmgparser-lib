package net.owl_black.vmgparser;

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
				
		// TODO Auto-generated method stub
		return ret;
	}

}
