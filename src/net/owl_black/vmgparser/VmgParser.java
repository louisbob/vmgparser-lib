package net.owl_black.vmgparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.util.org.apache.commons.codec.DecoderException;
import ezvcard.util.org.apache.commons.codec.net.QuotedPrintableCodec;

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

public class VmgParser {

	VmgLexer 		lexer;
	VmgToken 		c_tok; //current token
	VmgToken 		p_tok; //previous token
	Stack<String> 	env_stack; //Stack for storing environnement.
	Stack<String>	opt_stack; //Stack for storing options
	VCard			vcard;
	boolean			parseVBody;
	
	private static Logger log = Logger.getLogger(VmgParser.class.getName());
	
	public VmgParser(File f, String encoding) throws UnsupportedEncodingException {
		lexer = new VmgLexer(f, encoding);
		env_stack = new Stack<String>();
		opt_stack = new Stack<String>();
	}
	
	private void expect(VmgTokenType toktype) {
		if(found(toktype)) {
			return;
		} else {
			log.severe("Line: " +c_tok.getLineNb() +  " - I expected " + toktype._name + " but got " + c_tok.type._name );
			System.err.flush(); //TODO: fix that shit
		}
			
	}
	
	private boolean found(VmgTokenType toktype) {
		if(c_tok.type == toktype) {
			getToken();
			return true;
		}
		return false;
	}
	
	private boolean found_nostep(VmgTokenType toktype) { //Same as found, but without any token step.
		if(c_tok.type == toktype) {
			return true;
		}
		return false;
	}
	
	private void vmg_linefeed() {
		
		boolean already_warn = false;
		
		if(!found(VmgTokenType.CRLF))
			expect(VmgTokenType.LINEFEED);
		
		while(found(VmgTokenType.LINEFEED) || found(VmgTokenType.CRLF)) {
			if(!already_warn) {
				log.warning("Line: " + p_tok.getLineNb() +" - Extra linefeed detected. This VMG does not respect the RFC norm!");
				already_warn = true;
			}
		}
		
		
	}
	
	private VmgToken vmg_begin() {
		
		VmgToken retTok;
		
		if(!found(VmgTokenType.ID_BEGIN)) 
			return null;
		
		//We found the "BEGIN" keyword, start to process the beacon.
		expect(VmgTokenType.SYM_COLON);
		expect(VmgTokenType.IDENTIFIER);
		
		//Store the token that we will return
		retTok = p_tok;

		//TODO: handle errors
		vmg_linefeed();
		
		return retTok;
	}
	
	private VmgToken vmg_end() {
		
		VmgToken retTok;
		
		if(!found(VmgTokenType.ID_END)) 
			return null;
		
		//We found the "BEGIN" keyword, start to process the beacon.
		expect(VmgTokenType.SYM_COLON);
		expect(VmgTokenType.IDENTIFIER);
		
		//Store the token that we will return
		retTok = p_tok;

		//TODO: handle errors
		vmg_linefeed();
		
		return retTok;
	}
	
	private VmgProperty vmg_property(boolean isInVbody) {
		
		if (!found(VmgTokenType.IDENTIFIER))
			return null;
		
		VmgProperty vProp;
		String 		params = null;
		
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
			//We have full text. This can happen only when we are inside the vbody beacon.
			//If it is not the case, simply return an error.
			if(isInVbody) {
				vProp.value = vProp.name + vProp.value; 
				
				//Change the property to text
				vProp.name = "TEXT";
				
			} else //TODO: add line number in the backlog.
				log.severe("Non standard property. VMG is not well formatted.");
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
	
	private VCard vmg_vcard() {
		
		VmgToken tok;
		String vcard_txt;
		
		//TODO: add check for the number of originator.
		//Get the entire vcard:
		vcard_txt = "BEGIN:VCARD\n";
		
		while(!found_nostep(VmgTokenType.ID_END)) {
			vcard_txt += c_tok.content;
			getToken();
		}
		
		if( (tok = vmg_end()) == null) {
			log.severe("End of the vcard is mis-formated.");
			//TODO: stop algorithm.
		}
		
		if(!tok.content.equals("VCARD")) {
			log.severe(" Vcard is not closed properly ("+ p_tok.content +")");
		}	
		
		//Parse vcard:
		vcard_txt += "END:VCARD";
		return Ezvcard.parse(vcard_txt).first();
	}
	
	private VmgEnvelope vmg_envelope() {
		
		/*"BEGIN:VENV"<CRLF>
		 * {
		 * 		[<vmessage-recipient>]*
		 *		<vmessage-envelope> * | <vmessage-content>
		 * }
		 * "END:VENV"<CRLF> */
		
		VmgEnvelope vE = new VmgEnvelope();
		VmgToken 	tok;
		List<VCard>	vOriginator = null;
		
		
		while(true) {
			//Check the next environment:
			if( (tok = vmg_begin()) == null) {
				log.severe("Bad VMG formatting: I expected \"BEGIN\"");
				return null;
			}
			
			if (!tok.content.equals("VCARD")) 
				break;
			
			//[<vmessage-recipient>]*
			VCard vC = vmg_vcard();
			
			if(vC != null) {
				
				if(vOriginator == null) {
					vOriginator = new ArrayList<VCard>();
				}
				
				vOriginator.add(vC);
				
			} else {
				log.severe("EzVcard library wasn't able to parse correctly the vcard.");
				return null;
			}
			
			//TODO: handle correctly linefeed
			if(found(VmgTokenType.LINEFEED))
				System.out.println("Linefeed detected after the vcard. This VMG does not respect the RFC norm!");
		}
		
		//Fullfill the vEnv with the new list of originator.
		vE.setvOriginator(vOriginator);
		
		//<vmessage-envelope>*
		if(tok.content.equals("VENV")) {
			vE.setvEnv(vmg_envelope()); //recursively enter into the venveloppe.
		}
		else if (tok.content.equals("VBODY")) { // <vmessage-content>
			//Handle vbody
			VmgBody vB;
			
			if(parseVBody) 
				vB = vmg_body_extended();
			else
				vB = vmg_body();
			
			if(vB != null) {
				vE.setvBody(vB);
//				System.out.println("[VBody]");
//				System.out.print(vB.toString());
//				System.out.println("end [VBody]");
			}
		}
		else {
			//error
			log.severe("TO HANDLE");
		}
		
		return vE;
	}
	
	
	
	private VmgBody vmg_body() {
		
		String bodyContent = "";
		VmgToken tok;
		
		while(!found_nostep(VmgTokenType.ID_END)) {
			bodyContent += c_tok.content;
			getToken();
			//TODO: handle correctly the \n that can occurs at the end of the string.
			//MUST be handled in the extended version of the VmgBody
		}
		
		if( (tok = vmg_end()) == null) {
			log.severe("End of the vbody is mis-formated.");
			return null;
			//TODO: stop algorithm.
		}
		
		if(!tok.content.equals("VBODY")) {
			log.severe(" Vbody is not closed properly ("+ p_tok.content +")");
			return null;
		}
		
		//Create a new vbody object
		return new VmgBody(bodyContent);
	}
	
	/*
	private String streamToString(InputStream in) throws IOException {
		StringBuilder out = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		for(String line = br.readLine(); line != null; line = br.readLine())
			out.append(line);
		br.close();
		return out.toString();
	}*/
	
	private VmgBodyExtended vmg_body_extended() {
		VmgProperty vP = null;
		
		VmgBodyExtended vBe = new VmgBodyExtended();
		List<VmgProperty> properties = vBe.getvProp();
		QuotedPrintableCodec decoder = new QuotedPrintableCodec();
		
		while( (vP = vmg_property(true)) != null) {
			
			if((vP.params != null) && (vP.params.quoted_printable)) {
				//Process quoted printable:
				try {
					vP.value = decoder.decode(vP.value);
				} catch (DecoderException e) {
					// TODO Auto-generated catch block
					log.warning("Impossible to decode the property containing quoted printable text.");
				}
			}
			
			if(vP.name.equals("TEXT"))
				vBe.setContent(vP.value);
			
			properties.add(vP);
			//System.out.println(vP.toString());
		}
		
		return vBe;
	}
	
	
	
	public VmgObj vmg_object(boolean parseBody) {
		
		VmgObj				vmg;
		VmgProperty 		vP;
		List<VmgProperty> 	vObjPro = null;
		VCard 				vC;
		List<VCard>			vObjVcards = null;
		VmgEnvelope 		vE;
		VmgToken 			tok;
		
		this.parseVBody = parseBody;
		
		//Instantiate a new VmgObject:
		vmg = new VmgObj();
		
		/*
		 * <vmessage-object> ::= {
		 * 	"BEGIN:VMSG" <CRLF>
		 * 	<vmessage-property>*
		 * 	[<vmessage-originator>]*
		 * 	<vmessage-envelope>
		 * 	"END:VMSG" <CRLF>
		 * }
		 */
		
		//Read the first token:
		getToken();
		
		// "BEGIN:VMSG" : check the version of the vMessage 
		if( (tok = vmg_begin()) == null) {
			//TODO: handle error
			System.out.println("Error!");
		}
		
		if(!tok.content.equals("VMSG")) {
			//TODO: handle error
			System.out.println("Error!");
		}
		
		//Check that we have the correct version of VMG, that is VERSION:1.1
		expect(VmgTokenType.ID_VERSION);
		expect(VmgTokenType.SYM_COLON);
		expect(VmgTokenType.NUMBER);
		if(!p_tok.content.equals("1.1")) {
			log.severe(" VMG version is invalid. Only version 1.1 is supported.");
			return null;
		}
		
		//The version is fine, let's instantiate the first property.
		vP = new VmgProperty("VERSION");
		vP.value = "1.1";
		vObjPro = vmg.getvProp();
		vObjPro.add(vP);
		
		//Prevent bad linefeed formatting.
		vmg_linefeed();
		
		//If the VMSG respect the vCard syntax starting from version 4.0, \r\n is replaced
		//by a single \n
		log.fine("[VMSG]");
		
		// <vmessage-property>*
		//Get the list of properties in order to modify it. There is at least one property: the version.
		
		while( (vP = vmg_property(false)) != null) {
			vObjPro.add(vP);
			//System.out.println(vP.toString());
		}
		
		//Fullfill the VMG with the new list of originator. TODO: is it useful?
		vmg.setvProp(vObjPro);
		
		
		// [<vmessage-originator>]* (make it optional because of brackets. [])
		while(true) {
			if( (tok = vmg_begin()) == null) {
				log.severe("Bad VMG formatting: I expected \"BEGIN\"");
				return null;
			}
			if(!tok.content.equals("VCARD"))
				break;
			
			vC = vmg_vcard();
			
			if(vC != null) {
				//We have successfully retrieved a vCard. If it's the first vcard we encounter, initialize it.
				if(vObjVcards == null) {
					vObjVcards = new ArrayList<VCard>();
				}
				
				vObjVcards.add(vC);
				
				//System.out.println("[VCard]");
				//for (Telephone phone : vC.getTelephoneNumbers()) {
				//	System.out.println(phone.getText());
				//}
				//System.out.println("end [VCard]");
			} else {
				log.severe("EzVcard library wasn't able to parse correctly the vcard.");
				return null;
			}
			
			if(found(VmgTokenType.LINEFEED))
				System.out.println("Linefeed detected after the vcard. This VMG does not respect the RFC norm!");
		}
		
		//Fullfill the VMG with the new list of originator.
		vmg.setvOriginator(vObjVcards);
		
		//<vmessage-envelope>
		if(!tok.content.equals("VENV")) {
			log.severe("Bad VMG formatting: I expected an enveloppe, and got \"" + tok.content + "\" instead.");
			return null;
		}
		
		if( (vE = vmg_envelope()) == null) {
			log.severe("Bad VMG formatting: the returned enveloppe is not valid.");
			return null;
		}
		
		vmg.setvEnv(vE);
		
		return vmg;
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
	
	private void getToken() {
		p_tok = c_tok;
		c_tok = lexer.get();
	}
}
