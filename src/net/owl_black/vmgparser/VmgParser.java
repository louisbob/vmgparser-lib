package net.owl_black.vmgparser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class VmgParser {

	VmgLexer 		lexer;
	VmgToken 		c_tok; //current token
	VmgToken 		p_tok; //previous token
	Stack<String> 	env_stack; //Stack for storing environnement.
	Stack<String>	opt_stack; //Stack for storing options
	
	public VmgParser(File f, String encoding) throws UnsupportedEncodingException {
		lexer = new VmgLexer(f, encoding);
		env_stack = new Stack<String>();
		opt_stack = new Stack<String>();
	}
	
	/*
	public void first_pass() {
		//TODO: re-apply rules until no modification on the list is done.
		for(int i = 0; i < l_token.size()-1; i++) {
			VmgToken c_tok = l_token.get(i);
			VmgToken n_tok = l_token.get(i+1);
			
			//Rule 1 : IDENTIFIER := IDENTIFIER STRING
			if(c_tok.type == VmgTokenType.IDENTIFIER && n_tok.type == VmgTokenType.STRING) {
				c_tok.content += n_tok.content;
				l_token.remove(i+1);
			}
		}
		
		//VERBOSE
		System.out.println("=== FIRST PASS");
		for (VmgToken vmgToken : l_token) {
			System.out.println(vmgToken.toString());
		}
	}*/
	
	public void expect(VmgTokenType toktype) {
		if(found(toktype)) {
			return;
		} else
			System.out.println("ERROR WHILE PARSING : EXPCTED " + toktype._name);
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
	
	/*
	public void statement () {
		if (found(VmgTokenType.IDENTIFIER)) {
			expect(VmgTokenType.SYMBOL)
		}
	}*/
	
	public void rules() {
		
		if(found(VmgTokenType.ID_BEGIN)) {
			expect(VmgTokenType.SYM_COLON);
			
			//Next token will be the environnement name (ex : VMSG, VCAR, VENV, etc...). Store the name into the stack.
			expect(VmgTokenType.IDENTIFIER);
			env_stack.push(p_tok.content);
			expect(VmgTokenType.LINEFEED);
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
			expect(VmgTokenType.LINEFEED);
			return;
			
		} else 
			
		if(found(VmgTokenType.LINEFEED)) {
			//Simply pass the character : nothing interesting to process here.
			return;
		} else 
		
		if(found(VmgTokenType.ID_VERSION)) {
			expect(VmgTokenType.SYM_COLON);
			//Do some stuff
			expect(VmgTokenType.NUMBER);
			expect(VmgTokenType.LINEFEED);
			return;
		} else 
			
		if(found(VmgTokenType.IDENTIFIER)) {
			
			//Check if it is simple identifier ( UID:VALUE)
			if(found_nostep(VmgTokenType.SYM_COLON)) {
				//Store the identifier name:
				String id_name = p_tok.content;
				String id_val  = "";
				getToken();
				
				//Check if we have content (could have case with IDENTIFIER:\r)
				
				while(!found(VmgTokenType.LINEFEED)) { //Retrieve data until cariage return
					getToken();
					id_val += p_tok.content;
				}
				//TODO : add count iteration or eof checker to avoid infinite loop.
				
				System.out.println(id_name + "<=" + id_val);
				
				//expect(VmgTokenType.LINEFEED) handled by found option
				
			} else 
			
			//Check if it is a composed identifier (UID;CHARSET=UTF-8:VALUE)
			if(found_nostep(VmgTokenType.SYM_SCOLON)) {
				String id_name = p_tok.content;
				String id_val  = "";
				getToken();
				
				while(!found(VmgTokenType.LINEFEED)) { //Retrieve data until cariage return
					getToken();
					id_val += p_tok.content;
				}
				
				//TODO process options:
				
				//Display result:
				System.out.println(id_name + "<=" + id_val);

			}  else 
				
			//Check if it is a text content (MYTEXTMESSAGE)
			if(found_nostep(VmgTokenType.STRING)) {
				String id_val  = p_tok.content;
				
				//Check if we have content (could have case with IDENTIFIER:\r)
				
				while(!found(VmgTokenType.LINEFEED)) { //Retrieve data until cariage return
					getToken();
					id_val += p_tok.content;
				}
				
				System.out.println("TEXTMSG" + ":=" + id_val);
			}
			
			else {
				System.out.println("ERROR parsing identifier"); //TODO: handle correctly error
			}
			
			//Do some stuff
			return;
		}
	}
	
	public void parse() {
		
		//Get all token and store them
		
		//Apply rules:
		//first_pass();
		
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
