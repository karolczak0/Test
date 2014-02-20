package kao.monkey.expressions.tree;

import kao.monkey.utils.Constants;

/** Tree data structure. 
 * @author k.antczak */
public class Tree<LeafType extends ILeaf> {
		
	private LeafType root;
	
	public LeafType getRoot() {
		return root;
	}

	public void setRoot(LeafType root) {
		this.root = root;
	}
	
	@Override
	public String toString() {
		return toString (root, 0);
	}
	
	/** Recursively builds string with tree structure */
	private String toString(ILeaf currentLeaf, int depth) {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i =0; i<depth; i++) {
			stringBuilder.append("      ");
		}

		stringBuilder.append(currentLeaf.toString());
		stringBuilder.append(Constants.NEW_LINE);
				
		for (ILeaf child : currentLeaf.getChildren()) {
			stringBuilder.append(toString(child,depth+1));
		}
		
		return stringBuilder.toString();
	}
	
	/** Returns first leaf which equals l. */ 
	public ILeaf get(ILeaf l) {
		return get(root,l);
	}
	
	/** Returns first leaf inside root which equals l. */
	public static ILeaf get(ILeaf root, ILeaf l) {
		if (root.equals(l)) {
			return l;
		}else {
			for (ILeaf child :root.getChildren()) {
				ILeaf found = get(child,l);
				
				if (found!=null) {
					return found;
				}
			}
			return null;
		}
	}

}
