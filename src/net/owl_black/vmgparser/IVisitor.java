package net.owl_black.vmgparser;

public interface IVisitor {
	void visit(IVisitable e);
	void visit(VmgObj e);
	void visit(VmgProperty e);
	void visit(VmgEnvelope e);
	void visit(VmgBody e);
	void visit(VmgBodyExtended e);
}
