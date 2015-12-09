package net.owl_black.vmgparser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.Key;
import ezvcard.property.Telephone;

public class VmgParser2 {

	VmgLexer 		lexer;
	VmgToken 		c_tok; //current token
	VmgToken 		p_tok; //previous token
	Stack<String> 	env_stack; //Stack for storing environnement.
	Stack<String>	opt_stack; //Stack for storing options
	String 			vcard_txt;
	VCard			vcard;
	
	private static Logger log = Logger.getLogger(VmgParser2.class.getName());
	
	public VmgParser2(File f, String encoding) throws UnsupportedEncodingException {
		lexer = new VmgLexer(f, encoding);
		env_stack = new Stack<String>();
		opt_stack = new Stack<String>();
	}
	
	public void expect(VmgTokenType toktype) {
		if(found(toktype)) {
			return;
		} else
			log.severe("I expected " + toktype._name);
	}
	
	public boolean found(VmgTokenType toktype) {
		if(c_tok.type == toktype) {
			getToken();
			return true;
		}
		return false;
	}
	
	public boolean found_nostep(VmgTokenType toktype) { //Same as found, but without any token step.
		if(c_tok.type == toktype) {
			return true;
		}
		return false;
	}
	
	public void rules() {
		
		if(found(VmgTokenType.ID_BEGIN)) {
			expect(VmgTokenType.SYM_COLON);
			
			//Next token will be the environnement name (ex : VMSG, VCARD, VENV, etc...). Store the name into the stack.
			expect(VmgTokenType.IDENTIFIER);
			
			env_stack.push(p_tok.content);
			
			//check if vmsg
			if (p_tok.content.equals("VMSG")) {
				
				if(!found(VmgTokenType.CRLF))
					expect(VmgTokenType.LINEFEED);
				
				expect(VmgTokenType.ID_VERSION);
				expect(VmgTokenType.SYM_COLON);
				expect(VmgTokenType.NUMBER);
				
				//Check version number
				
				if(p_tok.content.equals("1.1"))
					
						
				
				if(!found(VmgTokenType.CRLF))
					expect(VmgTokenType.LINEFEED);
				//STORE VERSION NUMBER HERE.
				
				System.out.println("_VMSG_");
			} else 
				
			if (p_tok.content.equals("VCARD")) {
				
				//Get the entire vcard:
				vcard_txt = "BEGIN:VCARD";
				
				while(!found(VmgTokenType.ID_END)) {
					vcard_txt += c_tok.content;
					getToken();
				}
				
				expect(VmgTokenType.SYM_COLON);
				
				//Check that we are closing the correct environnement (tracing is done using a stack)
				expect(VmgTokenType.IDENTIFIER);
				if(!p_tok.content.equals("VCARD")) {
					System.out.println("ERROR while VCARdin' ");
				}
				
				System.out.println("_VCARD_");
				
				vcard_txt += "END:VCARD";
				
				//Parse vcard:
				vcard = Ezvcard.parse(vcard_txt).first();
				
				for (Telephone phone : vcard.getTelephoneNumbers()) {
					System.out.println(phone.getText());
				}
				
				//We close the environnement so we must pop the vcard attribute:
				env_stack.pop();
				
				/*
				expect(VmgTokenType.CRLF);
				expect(VmgTokenType.ID_VERSION);
				expect(VmgTokenType.SYM_COLON);
				expect(VmgTokenType.NUMBER);
				//STORE VERSION NUMBER HERE.
				
				//Todo: regarding version of vcard, differences...
				expect(VmgTokenType.CRLF);
				*/
				
				
			} else {
				if(!found(VmgTokenType.CRLF))
					expect(VmgTokenType.LINEFEED);
			}

			return;
			
		} else 
		
		if(found(VmgTokenType.ID_END)) {
			expect(VmgTokenType.SYM_COLON);
			
			//Check that we are closing the correct environnement (tracing is done using a stack)
			expect(VmgTokenType.IDENTIFIER);
			String s = env_stack.pop();
			if(!s.equals(p_tok.content)) {
				System.out.println("FATAL ERROR: not closing the right environnement. (" 
					+ s 
					+ " instead of " 
					+ p_tok.content 
					+ ")" );
			}
			
			if(s.equals("VCARD")) 
				expect(VmgTokenType.CRLF);
			else
				if(!found(VmgTokenType.CRLF))
					expect(VmgTokenType.LINEFEED);
			return;
			
		} else 
			
		if(found(VmgTokenType.LINEFEED) || found(VmgTokenType.CRLF)) {
			//Simply pass the character : nothing interesting to process here.
			return;
		} else 
			
		if(found(VmgTokenType.IDENTIFIER)) {
			String params = null;
			String id_name;
			ParamType param = null;
			
			//contentline = [group "."] name *(";" param) ":" value CRLF
			id_name = p_tok.content;
			System.out.println("IDENT  :    " + id_name);
			
			//Check for parameters
			if(found(VmgTokenType.SYM_SCOLON)) {
				params = c_tok.content;
				getToken();
				
				//Get the param list
				while(!found(VmgTokenType.SYM_COLON)) { //Retrieve data until cariage return
					params += c_tok.content;
					getToken();
				}
				
				//Process params:
				param = process_params(params);
				
			} else {
				expect(VmgTokenType.SYM_COLON);
			}
				
			
			//READ value
			String id_val  = "";
			
			while(true) {
				//Check previous caracter
				if(found_nostep(VmgTokenType.CRLF)) { //Retrieve data until cariage return
					if(p_tok.type != VmgTokenType.SYM_EQUAL)
						break;
					else if (!param.quoted_printable)
						break;
				}
				
				if(found_nostep(VmgTokenType.LINEFEED))
					break;
				
				getToken();
				id_val += p_tok.content;
			}
			
			getToken();
			
			//Display result:
			System.out.println("VALUE  :    " + id_val);
			
			if(params != null)
				System.out.println("PARAMS :    " + params);
			
			System.out.println("\n");

			return;
		}
	}
	
	private ParamType process_params(String param_string) {
		ParamType param = new ParamType();
		
		//Split string into params (separated by ; scolon)
		String[] params = param_string.split(";");
		
		for (String string : params) {			
			switch(string) { //TODO: make this proper.
				case "ENCODING=QUOTED-PRINTABLE":
					param.quoted_printable = true;
					break;
					
				case "CHARSET=UTF-8" :
					param.charset_utf8 = true;
					break;
					
				case "CHARSET=ISO-8859-1" :
					param.charset_iso8859_1 = true;
					break;
					
				default :
					System.out.println("UNKNOWN PARAMETER");
			}
		}
		
		return param;
	}
	
	public void parse() {
		
		getToken();
		
		//Do rules
		while(!found(VmgTokenType.EOF))
			rules();
		
		System.out.println("Parsing finished.");
	}
	
	public void getToken() {
		p_tok = c_tok;
		c_tok = lexer.get();
	}
}