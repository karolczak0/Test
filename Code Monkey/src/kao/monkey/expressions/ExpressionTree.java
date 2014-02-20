package kao.monkey.expressions;

import java.util.ArrayList;
import java.util.List;

import kao.monkey.expressions.tree.Tree;

/** Tree for expressions 
 * @author k.antczak */
public class ExpressionTree extends Tree<Expression> {

	/** Returns list of all leafs of given type */
	public List<Expression> getAllByType(String typeName) {
		return getAll(typeName, null, getRoot());
	}
	
	/** Returns list of all leafs of given type and value */
	public  List<Expression> getAll(String typeName, String value) {
		return getAll(typeName, value, getRoot());
	}
	
	/** Returns list of all leaf of given type and value, going recursively from root */
	private List<Expression> getAll(String typeName, String value, Expression root) {
		List<Expression> expressions = new ArrayList<Expression>();
		
		for (Expression child: root.getChildren()) {
			if (child.getDefinition().getName().equals(typeName)) {
				if ((value==null)||(child.getValue().equals(value))) {
					expressions.add(child);
				}
			} 
			
			expressions.addAll(getAll(typeName,value, child));
		}
		
		return expressions;
	}
	
	/** Returns path to given leaf from root */
	public List<Expression> getPathTo(Expression l) {
		
		List<Expression> path = new ArrayList<Expression>();
		Expression current = l;
				
		while (!current.getDefinition().equals(ExpressionDefinition.ROOT_DEFINITION)) {
			path.add(0, current);
			current = (Expression) current.getParent();
		}
		path.add(0, current);
		
		return path;
	}

}
