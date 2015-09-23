package net.owl_black.vmgparser;

public class VmgTokenType {

	//Lexer token
	static VmgTokenType STRING 		= new VmgTokenType("String", 		null);
	static VmgTokenType IDENTIFIER 	= new VmgTokenType("Identifier", 	null);
	static VmgTokenType NUMBER 		= new VmgTokenType("Number", 		null);
	static VmgTokenType CARRIAGE 	= new VmgTokenType("Carriage", 		"\\r"); //0x0d
	static VmgTokenType LINEFEED 	= new VmgTokenType("LineFeed", 		"\\n"); //0x0a
	static VmgTokenType SPACE	 	= new VmgTokenType("Space", 		" ");
	static VmgTokenType EOF 		= new VmgTokenType("Eof", 			"Eof");
	
	//Parser extra tokens
	static VmgTokenType SYM_SCOLON 	= new VmgTokenType("Symbol", 		null);
	static VmgTokenType SYM_COLON 	= new VmgTokenType("Symbol", 		null);
	static VmgTokenType SYM_EQUAL 	= new VmgTokenType("Symbol", 		null);
	
	static VmgTokenType ID_BEGIN 		= new VmgTokenType("Identifier*", 	"{");
	static VmgTokenType ID_END 			= new VmgTokenType("Identifier*", 	"}");
	static VmgTokenType ID_VERSION 		= new VmgTokenType("Identifier*", 	null); //The star mean 'identified indentifier'
	
	/*
	static VmgTokenType ID_NOKIA_DATE 	= new VmgTokenType("Identifier*", 	null);
	static VmgTokenType ID_IRMC_BOX 	= new VmgTokenType("Identifier*", 	null);
	static VmgTokenType ID_N 			= new VmgTokenType("Identifier*", 	null);
	static VmgTokenType ID_TEL 			= new VmgTokenType("Identifier*", 	null);
	static VmgTokenType ID_DATE 		= new VmgTokenType("Identifier*", 	null);
	static VmgTokenType ID_TEXT 		= new VmgTokenType("Identifier*", 	null);*/
	
	final String _def;
	final String _name;
	
	VmgTokenType(String def, String name) {
		this._def = def;
		this._name = name;
	}
	
}
