package kao.monkey.expressions.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import kao.monkey.explorer.FilesExplorer;
import kao.monkey.expressions.*;

/** Reads and parses ExpressionDefinition from XML file 
 * @author k.antczak */
public class XMLExpressionDefinitionReader {

	File file; 
	
	public XMLExpressionDefinitionReader(String fileName) { 
		file = new File(fileName);
	}
	
	/** Returns list of all expression definitions found in xml file */
	public List<ExpressionDefinition> getDefinitions() {
		List<ExpressionDefinition> definitions = new ArrayList<ExpressionDefinition>();
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder= dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			
			NodeList nodesList = doc.getElementsByTagName("definition");
			
			for (int i =0; i< nodesList.getLength(); i++) {
				Node node = nodesList.item(i);
				
				if (node.getNodeType() == Node.ELEMENT_NODE) {		
					ExpressionDefinition definition =  ExpressionDefinition((Element) node);
					definitions.add(definition);
				}
			}
		} catch (Exception e) {
			System.out.println("Error loading file: " + file);
		}

		return definitions;
	}
	
	/** Creates new expression definition based on given xml element */
	protected ExpressionDefinition ExpressionDefinition(Element element) {
		
		String name = element.getAttribute("name");
		String regex = element.getAttribute("regex");
		String startToken = element.getAttribute("start_token");
		String endToken = element.getAttribute("end_token");
		
		name = name.isEmpty() ? null : name;
		regex = regex.isEmpty() ? null : regex;
		startToken = startToken.isEmpty() ? null : startToken;
		endToken = endToken.isEmpty() ? null : endToken;
		
		return new ExpressionDefinition(name,regex,startToken,endToken);
	}
		
	/** Returns all parser definition files in directory */
	public static String[] getDefinitionFiles(File dir) {
		File[] filesAndDirs = dir.listFiles();
		ArrayList<String> fileNames = new ArrayList<String>();
		
		for (File f: filesAndDirs) {
			if (FilesExplorer.getFileExtension(f).equals("xml") )
				fileNames.add(f.getName().substring(0, f.getName().lastIndexOf('.')));
		}
		
		String[] fileNamesArray = new String[fileNames.size()];
		
		return fileNames.toArray(fileNamesArray);
		
	}
}
