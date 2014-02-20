package kao.monkey.explorer;

import java.io.File;
import java.util.EventObject;

/** Event for files explorer. 
* @author k.antczak */
@SuppressWarnings("serial")
public class ExplorerEvent extends EventObject {
	
	/** Currently parsed element */
	File exploredElement;

	public ExplorerEvent(Object source) {
		super(source);
	}
	
	ExplorerEvent(Object source, File exploredElement) {
		super(source);
		this.exploredElement = exploredElement;
	}
	
	/** Returns currently explored file or directory */
	public File getExploredElement() {
		return exploredElement;
	}

}
