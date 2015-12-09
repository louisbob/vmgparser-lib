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

public class VmgParser {

	VmgLexer 		lexer;
	VmgToken 		c_tok; //current token
	VmgToken 		p_tok; //previous token
	Stack<String> 	env_stack; //Stack for storing environnement.
	Stack<String>	opt_stack; //Stack for storing options
	String 			vcard_txt;
	VCard			vcard;
	
	private static Logger log = Logger.getLogger(VmgParser.class.getName());
	
	public VmgParser(File f, String encoding) throws UnsupportedEncodingException {
		lexer = new VmgLexer(f, encoding);
		env_stack = new Stack<String>();
		opt_stack = new Stack<String>();
	}
	
	public void expect(VmgTokenType toktype) {
		if(found(toktype)) {
			return;
		} else
			log.severe("I expected " + toktype._name + " but got " + c_tok.type._name );
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
	
	public VmgToken vmg_begin() {
		
		VmgToken retTok;
		
		if(!found(VmgTokenType.ID_BEGIN)) 
			return null;
		
		//We found the "BEGIN" keyword, start to process the beacon.
		expect(VmgTokenType.SYM_COLON);
		expect(VmgTokenType.IDENTIFIER);
		
		//Store the token that we will return
		retTok = p_tok;

		//TODO: handle errors
		if(!found(VmgTokenType.CRLF))
			expect(VmgTokenType.LINEFEED);
		
		//Some VMGs are not well formated. (tento test)
		found(VmgTokenType.LINEFEED);
		
		return retTok;
		
	}
	
	public VmgProperty vmg_property() {
		
		if (!found(VmgTokenType.IDENTIFIER))
			return null;
		
		VmgProperty vProp;
		
		String 		params = null;
		String 		id_name;
		ParamType 	param_t = null;
		
		//contentline = [group "."] name *(";" param) ":" value CRLF
		vProp = new VmgProperty(p_tok.content);
		
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
			vProp.params = process_params(params);
			
		} else if (found(VmgTokenType.SYM_COLON)) {
			//DO nothing
		} else {
			//We have text:
			System.out.println("Non standard Vbody TEXT");
		}
			
		//READ value
		while(true) {
			//Check previous character
			if(found_nostep(VmgTokenType.CRLF)) { //Retrieve data until cariage return
				if(p_tok.type != VmgTokenType.SYM_EQUAL)
					break;
				else if (!vProp.params.quoted_printable)
					break;
			}
			
			if(found_nostep(VmgTokenType.LINEFEED))
				break;
			
			getToken();
			vProp.value += p_tok.content;
		}
		
		getToken();
		
		return vProp;
		
	}
	
	public VCard vmg_vcard() {
		
		//TODO: add check for the number of originator.
		//Get the entire vcard:
		vcard_txt = "BEGIN:VCARD\n";
		
		while(!found(VmgTokenType.ID_END)) {
			vcard_txt += c_tok.content;
			getToken();
		}
		
		expect(VmgTokenType.SYM_COLON);
		expect(VmgTokenType.IDENTIFIER);
		
		if(!p_tok.content.equals("VCARD")) {
			log.severe(" Vcard is not closed properly ("+ p_tok.content +")");
		}
		
		if(!found(VmgTokenType.CRLF)) {
			expect(VmgTokenType.LINEFEED);
		}
			
		
		//Parse vcard:
		vcard_txt += "END:VCARD";
		return Ezvcard.parse(vcard_txt).first();
	}
	
	public VmgEnvelope vmg_envelope() {
		
		/*"BEGIN:VENV"<CRLF>
		 * {
		 * 		[<vmessage-recipient>]*
		 *		<vmessage-envelope> * | <vmessage-content>
		 * }
		 * "END:VENV"<CRLF> */
		
		VCard vC;
		VmgEnvelope vE = new VmgEnvelope();
		VmgBody 	vB;
		VmgToken 	tok;
		
		//Check the next environnement:
		tok = vmg_begin();
		
		if (tok.content.equals("VCARD")) { //[<vmessage-recipient>]*
			while(true) {

				vC = vmg_vcard();
				
				if(vC != null) {
					System.out.println("[VCard]");
					for (Telephone phone : vC.getTelephoneNumbers()) {
						System.out.println(phone.getText());
					}
					System.out.println("end [VCard]");
				}
				
				if(found(VmgTokenType.LINEFEED))
					System.out.println("Lineffed detected after the vcard. This VMG does not respect the RFC norm!");
				
				tok = vmg_begin();
				if(!tok.content.equals("VCARD")) {
					if(tok.content.equals("VENV"))
						vmg_envelope();
					break;
				}
					
			}
		}  
		
		if (tok.content.equals("VBODY")) { // <vmessage-content>
			//Handle vbody
			vB = vmg_body();
			
			if(vB != null) {
				System.out.println("[VBody]");
				//TODO: verbose
			}
		}
		
		return vE;
	}
	
	
	
	public VmgBody vmg_body() {
		
		VmgProperty vP;
		while( (vP = vmg_property()) != null) {
			System.out.println(vP.toString());
		}
		
		//Get the entire vcard:
		return null;
	}
	
	
	
	public void vmg_object() {
		
		VmgProperty vP;
		VCard 		vC;
		VmgEnvelope vE;
		VmgToken 	tok;
		/*
		 * <vmessage-object> ::= {
		 * 	"BEGIN:VMSG" <CRLF>
		 * 	<vmessage-property>*
		 * 	[<vmessage-originator>]*
		 * 	<vmessage-envelope>
		 * 	"END:VMSG" <CRLF>
		 * }
		 */
		
		// "BEGIN:VMSG"
		
		if( (tok = vmg_begin()) == null) {
			//TODO: handle error
			System.out.println("Error!");
		}
		
		if(!tok.content.equals("VMSG")) {
			//TODO: handle error
			System.out.println("Error!");
		}
		
		//VERSION:1.1
		expect(VmgTokenType.ID_VERSION);
		expect(VmgTokenType.SYM_COLON);
		expect(VmgTokenType.NUMBER);
		if(!p_tok.content.equals("1.1")) {
			log.severe(" VMG version is invalid.");
			return;
		}
		
		if(!found(VmgTokenType.CRLF))
			expect(VmgTokenType.LINEFEED);
		
		System.out.println("[VMSG]");
		
		// <vmessage-property>*
		while( (vP = vmg_property()) != null) {
			System.out.println(vP.toString());
		}
		
		// [<vmessage-originator>]* (make it optional because of brackets. [])
		while(true) {
			tok = vmg_begin();
			if(!tok.content.equals("VCARD"))
				break;
			
			vC = vmg_vcard();
			
			if(vC != null) {
				System.out.println("[VCard]");
				for (Telephone phone : vC.getTelephoneNumbers()) {
					System.out.println(phone.getText());
				}
				System.out.println("end [VCard]");
			}
			
			while(found(VmgTokenType.LINEFEED) || found(VmgTokenType.CRLF))
				System.out.println("Lineffed detected after the vcard. This VMG does not respect the RFC norm!");
			
		}
		
		if(!tok.content.equals("VENV")) {
			//TODO: handle error
			System.out.println("error");
		}
		
		//<vmessage-envelope>
		vE = vmg_envelope();
		
		return;
		
		/*
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
				
				
//				expect(VmgTokenType.CRLF);
//				expect(VmgTokenType.ID_VERSION);
//				expect(VmgTokenType.SYM_COLON);
//				expect(VmgTokenType.NUMBER);
//				//STORE VERSION NUMBER HERE.
//				
//				//Todo: regarding version of vcard, differences...
//				expect(VmgTokenType.CRLF);
				
				
				
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
		*/
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
		//while(!found(VmgTokenType.EOF))
		
		vmg_object();
		
		System.out.println("Parsing finished.");
	}
	
	public void getToken() {
		p_tok = c_tok;
		c_tok = lexer.get();
	}
}
