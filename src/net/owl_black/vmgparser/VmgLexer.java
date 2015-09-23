package net.owl_black.vmgparser;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class VmgLexer {

	VmgScanner scanner;
	VmgCharacter vchar;
	VmgCharacter c1;
	VmgCharacter c2;
	
	static char[] symbol = {';', ':', '='};
	
	public VmgLexer(File f, String encoding) throws UnsupportedEncodingException {
		scanner = new VmgScanner(f, encoding);
		getChar();
	}
	
	public VmgToken get() {
		VmgToken token = new VmgToken(vchar);
		
		/* EOF */
		if(vchar.isEOF()) {
			token.type = VmgTokenType.EOF;
			getChar();
			return token;
		}
		
		/* CARRIAGE RETURN */
		else if (vchar.isCarriageReturn()) {
			token.type = VmgTokenType.LINEFEED;
			getChar();
			return token;
		}
		
		/* LINE FEED */
		else if (vchar.isLineFeed()) {
			token.type = VmgTokenType.LINEFEED;
			getChar();
			return token;
		}
		
		/* SPACE */
		else if (vchar.isSpace()) {
			token.type = VmgTokenType.SPACE;
			getChar();
			return token;
		}
		
		/* SYMBOL */
		else if(isSymbol(vchar)) {
			
			switch (vchar.getChar()) {
				case ';' : token.type = VmgTokenType.SYM_SCOLON; break;
				case ':' : token.type = VmgTokenType.SYM_COLON; break;
				case '=' : token.type = VmgTokenType.SYM_EQUAL; break;
				default: System.out.println("ERROR"); //TODO: handle error
			}
			
			getChar();
			return token;
		}
			
		
		/* IDENTIFIER */
		else if(vchar.isLetter()) { //Identifier always start with a letter
			getChar();
			
			while(vchar.isLetterOrDigit() || vchar.isMinus()) {
				token.content += vchar.getChar();
				getChar();
			}
			
			/*Check the kind of the identifier:*/
			String s = token.content;
			if(s.equals("BEGIN")) {
				token.type = VmgTokenType.ID_BEGIN;
			} else if (s.equals("END")) {
				token.type = VmgTokenType.ID_END;
			} else if (s.equals("VERSION")) {
				token.type = VmgTokenType.ID_VERSION;
			} else {
				token.type = VmgTokenType.IDENTIFIER;
			}
			
			return token;
		} 
		
		/* NUMBER */
		else if(vchar.isDigit()) {
			token.type = VmgTokenType.NUMBER;
			getChar();
			
			//Read until a space or a carriage or a tab or a letter.
			while(vchar.isDigit() || vchar.isDot()) {
				token.content += vchar.getChar();
				getChar();
			}
			return token;
		}
		
		/* STRING */
		else {
			token.type = VmgTokenType.STRING;
			getChar();
			
			//Read until a space or a carriage or a tab or a letter.
			while(!vchar.isLineFeed() && 
					!vchar.isCarriageReturn() && 
					!isSymbol(vchar)) {
				token.content += vchar.getChar();
				getChar();
			}
			
			return token;
		}
	}
	
	static boolean isSymbol(VmgCharacter vchar)
	{
	    char vc = vchar.getChar();
		for (char c : symbol) {
			if(vc == c)
				return true;
		}
		
		return false;
	}
	
	public void getChar() {
		try {
			vchar = scanner.get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
