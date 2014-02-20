package kao.monkey;

import java.io.File;

import kao.monkey.explorer.ExplorerEvent;
import kao.monkey.explorer.ExplorerFinishEvent;
import kao.monkey.explorer.ExplorerFinishListener;
import kao.monkey.explorer.ExplorerListener;
import kao.monkey.explorer.ExpressionExplorerListener;
import kao.monkey.explorer.FilesExplorer;
import kao.monkey.explorer.ForwardDeclarationFixer;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/** Main class for application 'Code Monkey'. 
 @author k.antczak 
 @version 1.0 */
public class CodeMonkey implements ExplorerListener, ExplorerFinishListener {
	
	@Option(name = "-p", aliases = { "-project" }, usage = "The path for analysed project", required = true)
	private static String projectPath;
	
	@Option(name = "-parser", aliases = { "-parser" }, usage = "Parser filename", required = true)
	private static String parserName;
	
	@Option(name = "-t", aliases = { "-tree" }, usage = "Create tree")
	private boolean tree;
	
	@Option(name = "-fix", aliases = { "-fix" }, usage = "Fix forward declarations")
	private boolean fix;
	
	private ExpressionExplorerListener parserListener;
	
	public static void main(String[] args) {
		new CodeMonkey().run(args);
	}
	
	public void run(String[] args) {
		
		CmdLineParser parser = new CmdLineParser(this);   
		 
		 try {
			 parser.parseArgument(args);
			 
			 if (tree) {
				 parserListener = new ExpressionExplorerListener();
			 } else if (fix) {
				 parserListener = new ForwardDeclarationFixer();
			 } else {
				 throw new CmdLineException("Not enough arguments! use -tree or -fix");
			 }
			 			 
			 parserListener.reset();
			 parserListener.loadParser(parserName);
			 
			 FilesExplorer explorer = new FilesExplorer(new File(projectPath));
			 
			 explorer.addExplorerListener(parserListener);
			 explorer.addExplorerListener(this);
			 explorer.addExplorerFinishListener(parserListener);
			 explorer.addExplorerFinishListener(this);
			 
			 System.out.println("Exploring started!");
			 System.out.println("Operation: " + parserListener);
			 System.out.println("Parser: " + parserName);
			 System.out.println();
			 
			 explorer.explore();
			 
		 } catch (CmdLineException e) {
			 e.printStackTrace();
		 }
	}

	@Override
	public void explorationFinished(ExplorerFinishEvent e) {
		System.out.println(e.getEventMessage());
	}

	@Override
	public void elementExplored(ExplorerEvent e) {
		System.out.println("#"+ ((FilesExplorer)e.getSource()).getExploredElementsCount() + "\t" + e.getExploredElement().getName());
	}
		
}
