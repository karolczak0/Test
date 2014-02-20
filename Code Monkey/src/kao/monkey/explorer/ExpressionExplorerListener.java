package kao.monkey.explorer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Stack;

import kao.monkey.expressions.Expression;
import kao.monkey.expressions.ExpressionDefinition;
import kao.monkey.expressions.ExpressionTree;
import kao.monkey.expressions.tree.ILeaf;
import kao.monkey.expressions.xml.XMLExpressionDefinitionReader;

/** Parses all explored files using expressions */ 
public class ExpressionExplorerListener extends LineReadedExplorerListener implements ExplorerFinishListener {
	
	/** Stack for parsed expressions*/
	Stack<Expression> parserStack = new Stack<Expression>();
	
	/** Last parsed expression */
	Expression currentExpresion;
	
	/** Formal grammar containing all accepted expressions */
	List<ExpressionDefinition> parserDefinitions;
	
	/** Expression tree created from parsing */
	ExpressionTree expressionTree = new ExpressionTree(); 
	
	public ExpressionExplorerListener() {
		reset();
	}
	
	/** Loads expression parser from file */
	public void loadParser(String parserFileName) {
		parserDefinitions = new XMLExpressionDefinitionReader(parserFileName).getDefinitions();
	}

	@Override
	protected void lineRead(File f, String l) {
		
		String text = l;
		int endIndex, startIndex;
									
		for (ExpressionDefinition definition: parserDefinitions) {
						
			if (definition.tryParse(text)) {
				// Add new child to current peek expression
				currentExpresion = new Expression(text.trim(), definition);
				currentExpresion.setParent(parserStack.peek());
			}
		}
		
		if (((startIndex = currentExpresion.getDefinition().isInput(text)) != -1)&&(parserStack.peek()!=currentExpresion)) {
			// Set new parser stack
			parserStack.push(currentExpresion);
			lineRead(f,text.substring(startIndex + 1, text.length()));
		}
		
		if ((endIndex = parserStack.peek().getDefinition().isOutput(text)) != -1 ) {
			// Leave current parser stack
			parserStack.pop();
			lineRead(f,text.substring(endIndex + 1, text.length()));
		}
	}
	
	@Override
	public void elementExplored(ExplorerEvent e) {
		
		File exploredElement = e.getExploredElement();
		updateDirectoryStructure(exploredElement);
		super.elementExplored(e);
	}
		
	@Override 
	public String toString() {
		return "Create tree structure for project";
	}
	
	/** Updates directory structure */
	protected void updateDirectoryStructure(File f) {
		
		ExpressionDefinition definition =  f.isFile() ? ExpressionDefinition.FILE_DEFINITION : ExpressionDefinition.DIRECTORY_DEFINITION;
		currentExpresion = new Expression(f.getName(),definition);
		
		ILeaf parent = expressionTree.get(new Expression(f.getParentFile().getName(),ExpressionDefinition.DIRECTORY_DEFINITION));
		
		if (parent!=null) {
			parserStack.setSize(parserStack.lastIndexOf(parent)+1);			
		} else {
			parserStack.setSize(1);
		}

		currentExpresion.setParent(parserStack.peek());
		parserStack.push(currentExpresion);
	}

	/** Save parsed expression tree to file */
	@Override
	public void explorationFinished(ExplorerFinishEvent e) {
		
		PrintWriter out = null;
		try {
			e.setEventMessage("\nTree structure saved to file 'tree.txt.'\n");
			out = new PrintWriter("tree.txt");
			out.println(expressionTree);
			System.out.println(expressionTree);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			if (out!=null)
				out.close();
		}
	
	}
	
	/** Resets listener */
	public void reset() {
		currentExpresion = new Expression("", ExpressionDefinition.ROOT_DEFINITION);
		expressionTree.setRoot(currentExpresion);
		parserStack.clear();
		parserStack.push(currentExpresion);
	}

}
