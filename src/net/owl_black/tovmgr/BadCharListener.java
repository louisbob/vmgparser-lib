package net.owl_black.tovmgr;
import java.util.EventListener;


/**
 * Interface for bad character correction
 * 
 * @author Louisbob 
 * @version 0.1 - 16/7/2012
 */
public interface BadCharListener extends EventListener {
	/**
	 * Launch the character correction algorithm
	 */
	public void fireBadChar();
}
