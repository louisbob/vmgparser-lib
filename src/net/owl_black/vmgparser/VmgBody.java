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
