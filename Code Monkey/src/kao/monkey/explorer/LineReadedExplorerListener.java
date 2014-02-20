package kao.monkey.explorer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/** Listener which performs some operations on every line inside every parsed files 
 * @author k.antczak */
public abstract class LineReadedExplorerListener implements ExplorerListener {
	
	@Override
	public void elementExplored(ExplorerEvent e) {
		File element = e.getExploredElement();
		if (element.isFile()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(element));
				String line;
				while ((line=br.readLine())!=null) {
					lineRead(element, line);
				}
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/** Invoked, when next line in file is being read */
	protected abstract void lineRead(File f, String l) ;
}
