package net.owl_black.vmgparser;

import java.util.ArrayList;
import java.util.List;

import ezvcard.VCard;

public class VmgObj implements IVisitable {
	private List<VmgProperty>	vProp;
	private List<VCard>			vOriginator; //[Optionnal]
	private VmgEnvelope			vEnv;
	
	public VmgObj() {
		//In a vmg file, there is at least one property : the version number.
		//It is why we instantiate this object.
		setvProp(new ArrayList<VmgProperty>()); 
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

	/*
	 * Getter and Setters
	 */
	public List<VmgProperty> getvProp() {
		return vProp;
	}

	public void setvProp(List<VmgProperty> vProp) {
		this.vProp = vProp;
	}
	
	public List<VCard> getvOriginator() {
		return vOriginator;
	}

	public void setvOriginator(List<VCard> vOriginator) {
		this.vOriginator = vOriginator;
	}

	public VmgEnvelope getvEnv() {
		return vEnv;
	}

	public void setvEnv(VmgEnvelope vEnv) {
		this.vEnv = vEnv;
	}
}
