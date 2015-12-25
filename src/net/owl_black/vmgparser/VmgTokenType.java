package net.owl_black.vmgparser;

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

public class VmgTokenType {

	//Lexer token
	public static VmgTokenType STRING 		= new VmgTokenType("String", 		null);
	public static VmgTokenType IDENTIFIER 	= new VmgTokenType("Identifier", 	null);
	public static VmgTokenType NUMBER 		= new VmgTokenType("Number", 		null);
	public static VmgTokenType CARRIAGE 	= new VmgTokenType("Carriage", 		"\\r"); //0x0d
	public static VmgTokenType LINEFEED 	= new VmgTokenType("LineFeed", 		"\\n"); //0x0a
	public static VmgTokenType CRLF		= new VmgTokenType("CRLF", 			"\\r\\n");
	public static VmgTokenType SPACE	 	= new VmgTokenType("Space", 		"[Space]");
	public static VmgTokenType EOF 		= new VmgTokenType("Eof", 			"[Eof]");
	
	//Parser extra tokens
	public static VmgTokenType SYM_SCOLON 	= new VmgTokenType("Symbol", 		"\";\"");
	public static VmgTokenType SYM_COLON 	= new VmgTokenType("Symbol", 		"\":\"");
	public static VmgTokenType SYM_EQUAL 	= new VmgTokenType("Symbol", 		"\"=\"");
	
	public static VmgTokenType ID_BEGIN 		= new VmgTokenType("Identifier*", 	"BEGIN");
	public static VmgTokenType ID_END 			= new VmgTokenType("Identifier*", 	"END");
	public static VmgTokenType ID_VERSION 		= new VmgTokenType("Identifier*", 	"VMSG Version"); //The star mean 'identified indentifier'
	
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
