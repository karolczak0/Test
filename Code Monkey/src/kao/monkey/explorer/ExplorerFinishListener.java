package kao.monkey.explorer;

import java.util.EventListener;

/** Handles finishing of exploring process
 * @author k.antczak */
public interface ExplorerFinishListener extends EventListener {

	/** Invoked, when exploration is finished */
	public void explorationFinished(ExplorerFinishEvent e);
}
