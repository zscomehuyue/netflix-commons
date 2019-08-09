package com.netflix.infix.lang.infix.antlr;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;

public class NumberTreeNode extends PredicateBaseTreeNode implements ValueTreeNode {

	@Override
	public Number getValue() {
		return Double.valueOf(getText());
	}

	public NumberTreeNode(Token t) {
		super(t);
	} 

	public NumberTreeNode(NumberTreeNode node) {
		super(node);
	} 

	public Tree dupNode() {
		return new NumberTreeNode(this);
	} 
}
