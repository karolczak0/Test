package kao.monkey.explorer;

import java.util.EventListener;

/** Handles exploring file or directory 
 * @author k.antczak */
public interface ExplorerListener extends EventListener {

	/** Invoked, when some element is being explored */
	public void elementExplored(ExplorerEvent e);
}
