package kao.monkey.expressions;

/** Single expression in context of formal grammar 
 * @author k.antczak */
public interface IExpression {

	/** Returns value of expression */
	String getValue();

	/** Return definition of this expression */
	ExpressionDefinition getDefinition(); 
}
