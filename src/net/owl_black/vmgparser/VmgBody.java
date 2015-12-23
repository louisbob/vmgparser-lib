package net.owl_black.vmgparser;

public class VmgBody implements IVisitable {
	private String content;

	public VmgBody() {
	}
	
	public VmgBody(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return content;
	};
	
	@Override
	public void accept(IVisitor visitor) {
		if(this instanceof VmgBodyExtended)
			visitor.visit((VmgBodyExtended) this);
		else
			visitor.visit(this);
	}
	
	/*
	 * Getter/setter
	 */
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
}
