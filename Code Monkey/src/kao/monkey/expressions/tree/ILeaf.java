package kao.monkey.expressions.tree;

import java.util.List;

/** Leaf of tree data structure.
 * @author k.antczak */
public interface ILeaf {
	
	/** Returns list of leaf's children */
	List<? extends ILeaf> getChildren();
	/** Returns leaf's parent */
	ILeaf getParent();
	/** Sets leaf's parent */
	void setParent(ILeaf parent);
}
