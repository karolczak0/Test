package kao.monkey.explorer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kao.monkey.expressions.Expression;
import kao.monkey.expressions.ExpressionDefinition;
import kao.monkey.utils.Constants;

/** Solves Klocwork problem with forward declarations. 
 *  It simply switches forward declaration with appropriate #include directive.  */
/**
 * @author k.antczak */
public class ForwardDeclarationFixer extends ExpressionExplorerListener {
	
	private ExplorerFinishEvent explorerFinishEvent; 
		
	@Override
	public void explorationFinished(ExplorerFinishEvent e) {
		
		explorerFinishEvent = e;
		
		List<Expression> forwardDeclarations =  expressionTree.getAllByType("CLASS DEC");
		Collections.sort(forwardDeclarations, new Expression.ExpressionComparator());	
		List<Expression> classDefinitions =  expressionTree.getAllByType("CLASS DEF");
		
		try {
			int fixedNo = fixForwardDeclarations(forwardDeclarations,classDefinitions);
			e.setEventMessage(Constants.NEW_LINE + "Finished removing forward declarations!"+Constants.NEW_LINE+"Fixed forward declarations: "  + fixedNo + "/"+forwardDeclarations.size() + Constants.NEW_LINE + "List of found declarations saved to file 'declarations.txt'\n");
		} catch (IOException e1) {
			e1.printStackTrace();
			e.setEventMessage(Constants.NEW_LINE + "Finished removing forward declarations!\nError while trying to save result file." + Constants.NEW_LINE);
		}
	}
	
	/** Fixes all forward declarations, by deleting them and 
	 * adding appropriate #include directive to file with declaration 
	 * @return number of fixed declarations */
	private int fixForwardDeclarations(List<Expression> declarations, List<Expression> definitions) throws IOException {
				
		PrintWriter out = new PrintWriter("declarations.txt");
		out.println("Found forward declarations: " + declarations.size() );
		out.println();
		out.println("No      Class Name                          Declared at:                                                                         Defined at:");
	    out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
	
		String[] definitionNames = new String[definitions.size()];
		
		for (int j =0; j<definitions.size(); j++) {
			definitionNames[j] = definitions.get(j).getValue().split(" ")[1].replace('{', ' ').trim();			
		}
		
		int fixedDeclarationsCount = 0;
						
 		for (int i =0; i<declarations.size(); i++) {
			Expression declaration = declarations.get(i);
			
			String declaredClassName = declaration.getValue().split(" |:")[1].replace(';', ' ').trim();
		
			Expression foundDefinition = null;
			
			for (int j =0; j<definitions.size(); j++) {
				if (declaredClassName.equals(definitionNames[j])) {
					foundDefinition = definitions.get(j);
					break;
				}
			}
			
			if (foundDefinition!=null) {
				fixedDeclarationsCount++;
				String head = "#" + (i+1) + "\t" + declaredClassName;
				String dec =  getRelativePathTo(declaration);
				String def =  getRelativePathTo(foundDefinition);
				
				out.println(head + String.format("%" +(40-head.length()) + "s",  "") + dec + String.format("%" + (85 - dec.length()) + "s", "") + def);
				fixForwardDeclaration(declaration,foundDefinition);
			} else {
				String head = "#" + (i+1) + "\t" + declaredClassName;
				String dec = getRelativePathTo(declaration);
				out.println(head + String.format("%" + (40-head.length()) + "s",  "") + dec);
			}
		}
 		out.close();
 		return fixedDeclarationsCount;
	}
	
	/** Fixes single forward declaration. */
	private void fixForwardDeclaration(Expression declaration, Expression definition) throws IOException {
		String path = getAbsolutePathTo(declaration);
		File file = new File(path);
		
		removeExpression(file, declaration);
		appendHeader(file, definition);
	}
	
	/** Appends header to given file */ 
	private void appendHeader(File f, Expression definition) throws IOException {
		
		String[] definitionPath = getAbsolutePathTo(definition).split("/");
		String definitionFile = definitionPath[definitionPath.length-1];
		
		RandomAccessFile  file = new RandomAccessFile(f, "rw");
		 byte[] text = new byte[(int) file.length()];
		    file.readFully(text);
		    file.seek(0);
		    file.writeBytes("#include \"" + definitionFile + "\";\t\t// LINE ADDED AUTOMATICALLY USING CODEMONKEY" + Constants.NEW_LINE);
		    file.write(text);
		    file.close(); 
	}
	
	/** Removes expression from file, by commenting it 
	 * @throws IOException */
	private void removeExpression(File f, Expression e) throws IOException {
		RandomAccessFile  file = new RandomAccessFile(f, "rw");

		List<String> lines = new ArrayList<String>();
		String line;
		int lineNumber = -1;
		
		for (int i = 0; (line  = file.readLine())!=null; i++) {
			lines.add(line);
			if (e.equalsValue(line)) {
				lineNumber = i;
			}
		}
		
		file.seek(0);
		
		for (int i = 0; i< lines.size(); i++) {
			if (i == lineNumber) {
				file.writeBytes("// " + lines.get(i) +  "\t\t// LINE COMMENTED AUTOMATICALLY USING CODEMONKEY" + Constants.NEW_LINE);
			} else {
				file.writeBytes(lines.get(i) + Constants.NEW_LINE);
			}
		}
		
	    file.close(); 
	}
	
	private String getAbsolutePathTo(Expression e) {
		return explorerFinishEvent.getExploredDir().getAbsolutePath() + getRelativePathTo(e);
	}
	
	/** Returns path description */
	private String getRelativePathTo(Expression e) {

		StringBuilder pathDesc = new StringBuilder();
		List<Expression> path = expressionTree.getPathTo(e);
		
		for (int i =0; i < path.size()-1; i++ ) {
			
			Expression pathElement = path.get(i);		
			pathDesc.append(pathElement.getValue());
			
			if (pathElement.getDefinition().equals(ExpressionDefinition.FILE_DEFINITION))
				break;
			else 
				pathDesc.append("/");
		}
		return pathDesc.toString();
	}
	
	@Override
	public String toString() {
		return "Forward declarations fix (C++)";
	}

}
