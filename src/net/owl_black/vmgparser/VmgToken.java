package net.owl_black.vmgparser;

public class VmgToken {
	
	String 			content;
	VmgTokenType 	type;
	private int 	_lineidx;
	private int 	_colidx;
	
	//static String IDENTIFIER
	
	public VmgToken(VmgCharacter startChar) {
		this.content 	= new String();
		this.content 	+= startChar.getChar();
		this._lineidx 	= startChar.getLine();
		this._colidx 	= startChar.getCol();
	}
	
	public int getLineNb() {
		return _lineidx;
	}
	
	@Override
	public String toString() {
		//If the displayable name is null, that means there is no specific way to display the token content
		if(type._name == null) {
			 return String.format("%6d", _lineidx) + String.format("%6d", _colidx) 
					+ "   " 
					+ String.format("%-15s", type._def) 
					+ ":  "
					+ content;
		} else {
			return String.format("%6d", _lineidx) + String.format("%6d", _colidx) 
					+ "   " 
					+ String.format("%-15s", type._def) 
					+ ":  "
					+ type._name;
		}
	}
	
	
	
}
