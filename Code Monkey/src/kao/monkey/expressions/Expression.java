package kao.monkey.expressions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import kao.monkey.expressions.tree.ILeaf;

/** It's simply some string accepted by some regular expression and with defined scope to create tree hierarchy. 
 * @author k.antczak */
public class Expression implements IExpression, ILeaf{
	
	private List<Expression> children = new ArrayList<Expression>();
	private Expression parent = null;
	private ExpressionDefinition definition = null;
	private String value;
	
	public Expression(String value, ExpressionDefinition definition) {
		this.value = value;
		this.definition = definition;
	}
	
	@Override
	public List<Expression> getChildren() {
		return children;
	}

	@Override
	public ILeaf getParent() {
		return parent;
	}

	@Override
	public void setParent(ILeaf parent) {
		this.parent = (Expression) parent;
		this.parent.getChildren().add(this);
	}
		
	@Override 
	public String toString() {
		return  "[" + definition.getName() + "]" + " " + getValue();
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public ExpressionDefinition getDefinition() {
		return definition;
	}
	
	public boolean equalsValue(String o) {		
			return getValue().equals(o);	
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
	    if (o == this) return true;
	    if (!(o instanceof Expression))return false;
	    Expression e = (Expression)o;

		return getValue().equals(e.getValue()) && getDefinition().equals(e.getDefinition());
	}
	
	/** Comparator for expressions */
	public static class ExpressionComparator implements Comparator<Expression>{
		@Override
		public int compare(Expression arg0, Expression arg1) {
			return arg0.getValue().compareTo(arg1.getValue());
		}};
}
