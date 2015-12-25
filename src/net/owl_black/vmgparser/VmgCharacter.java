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

public class VmgCharacter {
	private char _cargo;
	private int _srcidx;
	private int _line;
	private int _col;
	private char EOF = (char) -1; //65535
	
	public VmgCharacter(char c, int srcidx, int line, int col) {
		this._cargo 	= c;
		this._srcidx 	= srcidx;
		this._line 		= line;
		this._col 		= col;
	}
	
	public char getChar() {
		return _cargo;
	}
	
	public boolean isEOF() {
		return _cargo == EOF ? true : false; 
	}
	
	public boolean isLetter() {
		return Character.isLetter(_cargo);
	}
	
	public boolean isDigit() {
		return Character.isDigit(_cargo);
	}
	
	public boolean isLetterOrDigit() {
		return Character.isLetterOrDigit(_cargo);
	}
	
	public boolean isMinus() {
		return _cargo == '-' ? true : false;
	}
	
	public boolean isDot() {
		return _cargo == '.' ? true : false;
	}
	
	public boolean isSemiColon() {
		return _cargo == ';' ? true : false;
	}
	
	public boolean isColon() {
		return _cargo == ':' ? true : false;
	}
	
	public boolean isEquality() {
		return _cargo == '=' ? true : false;
	}
	
	public boolean isLineFeed() {
		return _cargo == '\n' ? true : false;
	}
	
	public boolean isCarriageReturn() {
		return _cargo == '\r' ? true : false;
	}
	
	public boolean isSpace() {
		return ((_cargo == ' ') || (_cargo == '\t')) ? true : false;
	}
	
	public int getLine() {
		return _line;
	}
	
	public int getCol() {
		return _col;
	}
	
	@Override
	public String toString() {		
		String cargo;
		
		if (_cargo == ' ')
			cargo = "   space";
		else if (_cargo == EOF)
			cargo = "   eof";
		else if (_cargo == '\n')
			cargo = "   linefeed";
		else if (_cargo == '\r')
			cargo = "   carriage_return";
		else if (_cargo == '\t')
			cargo = "   tab";
		else
			cargo = Character.toString(_cargo);
				
		return String.format("%6d", _line) + String.format("%6d", _col) + "   " + cargo;
	}
	
}
