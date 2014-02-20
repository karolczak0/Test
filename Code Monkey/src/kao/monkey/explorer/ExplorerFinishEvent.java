package kao.monkey.explorer;

import java.io.File;
import java.util.EventObject;

/** Event for finishing work of files explorer. 
* @author k.antczak */
@SuppressWarnings("serial")
public class ExplorerFinishEvent extends EventObject {
	
	private String _eventMessage;
	
	private File _exploredDir;

	public ExplorerFinishEvent(Object source, File exploredDir) {
		super(source);
		setExploredDir(exploredDir);
		setEventMessage("Exploration finished");
	}

	/** Returns event message */
	public String getEventMessage() {
		return _eventMessage;
	}

	/** Sets event message */
	public void setEventMessage(String eventMessage) {
		this._eventMessage = eventMessage;
	}

	/** Returns explored root directory */
	public File getExploredDir() {
		return _exploredDir;
	}

	/** Sets explored directory */
	public void setExploredDir(File _exploredDir) {
		this._exploredDir = _exploredDir;
	}

}
