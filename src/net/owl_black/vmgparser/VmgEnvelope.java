package net.owl_black.vmgparser;

import java.util.List;

import ezvcard.VCard;

public class VmgEnvelope implements IVisitable {
	VmgEnvelope 		vEnv;
	private VmgBody 	vBody;
	private List<VCard>	vOriginator; //[Optionnal]
	
	public VmgEnvelope() {
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	/*
	 * Setter/Getter.
	 */
	public VmgBody getvBody() {
		return vBody;
	}

	public void setvBody(VmgBody vBody) {
		this.vBody = vBody;
	}
	
	public List<VCard> getvOriginator() {
		return vOriginator;
	}

	public void setvOriginator(List<VCard> vOriginator) {
		this.vOriginator = vOriginator;
	}
}
