package com.netflix.infix.lang.infix.antlr;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;

public class TimeMillisValueTreeNode extends PredicateBaseTreeNode implements ValueTreeNode {

	@Override
	public String getValue() {
		return (String)((ValueTreeNode)getChild(1)).getValue(); 
	}

	public String getValueFormat() {
		return (String)((ValueTreeNode)getChild(0)).getValue();
	}
	
	public TimeMillisValueTreeNode(Token t) {
		super(t);
	} 

	public TimeMillisValueTreeNode(TimeMillisValueTreeNode node) {
		super(node);
	} 

	public Tree dupNode() {
		return new TimeMillisValueTreeNode(this);
	} 
}
