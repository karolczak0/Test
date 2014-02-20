/** */
package kao.monkey.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** Parser which goes through every file and directory inside given directory.
 * @author k.antczak */
public class FilesExplorer {
	
	/** Directory to start exploring from */
	protected File startDirectory; 
	
	/** Explored elements count */
	private int _exploredElementsCount = 0;
	
	/** Explorer listeners */
	private List<ExplorerListener> _listeners = new ArrayList<ExplorerListener>();
	
	/** Explorer finish listeners */
	private List<ExplorerFinishListener> _finishListeners = new ArrayList<ExplorerFinishListener>();
	
	/** All accepted file extensions */ 
	private String[] _acceptedExtensions = {"c", "cpp", "h", "hpp", "java"};
		
	public FilesExplorer(String startDirectory) {
		this(new File(startDirectory));
	}
	
	public FilesExplorer(File startDirectory) {
		this.startDirectory = startDirectory;
	}
	
	/** Goes recursively through every file and directory from start directory */
	public synchronized void explore() {
		_exploredElementsCount = 0;
		new Thread( new Runnable () {
			@Override
			public void run() {
				try {
				explore(startDirectory);
				fireExplorerFinishEvent();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}}).start();
	}
	
	public synchronized void reset() {
		_exploredElementsCount = 0;
		_listeners.clear();
		_finishListeners.clear();
	}
	
	/** Goes recursively through every file and directory inside startDir */
	protected synchronized void explore(File startDir) {
		File[] filesAndDirs = startDir.listFiles();
				
		if (filesAndDirs==null)
			return;
		
		// Go through directory content 
		for (File file: filesAndDirs) {
			
			if (isFileAccepted(file)) {
				_exploredElementsCount++;
				fireExplorerEvent(file);
			}

			if (file.isFile()) {
				// Process file
			} else {
				// Process directory
				explore(file);
			}
			
		}
	}
	
	/** Adds specified ParserListener to invoke during exploring elements */
	public synchronized void addExplorerListener(ExplorerListener listener) {
		_listeners.add(listener);
	}
	
	/** Removes specified ExplorerListener */
	public synchronized void removeExplorerListener(ExplorerListener listener) {
		_listeners.remove(listener);
	}
	
	/** Informs listeners that element is being explored */
	private synchronized void fireExplorerEvent(File exploredElement) {
		
		ExplorerEvent event = new ExplorerEvent(this, exploredElement);
		
		for (ExplorerListener listener: _listeners) {
			listener.elementExplored(event);
		}
	}
	
	/** Adds specified ExplorerFinishListener to invoke after exploring last element */
	public synchronized void addExplorerFinishListener(ExplorerFinishListener listener) {
		_finishListeners.add(listener);
	}
	
	/** Removes specified ParserListener */
	public synchronized void removeExplorerFinishListener(ExplorerFinishListener listener) {
		_finishListeners.remove(listener);
	}
	
	/** Informs listeners that parsing is finished */
	private synchronized void fireExplorerFinishEvent() {
		
		ExplorerFinishEvent event = new ExplorerFinishEvent(this, startDirectory);
		
		for (ExplorerFinishListener listener: _finishListeners) {
			listener.explorationFinished(event);
		}
	}
	
	/** Returns all explored elements count */
	public synchronized int getExploredElementsCount() {
		return _exploredElementsCount;
	}
	
	/** Checks if file is acceptable */
	private boolean isFileAccepted(File f) {
		
		boolean isAcceptable = false;
		
		if (f.isFile()) {
			String extension = FilesExplorer.getFileExtension(f);	
			for (String acceptedExt : _acceptedExtensions) {
				if (extension.equals(acceptedExt)) {
					isAcceptable = true;
					break;
				}
			}
		} else {
			isAcceptable = true;
		}
		return isAcceptable;
	}
	
	/** Helper method, returns file extension */
	public static String getFileExtension(File f) {
		
		String extension = "";
		String fileName = f.getName();

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		}
		
		return extension;
	}

	/** Returns all file extensions accepted by this parser */
	public String[] getAcceptedExtensions() {
		return _acceptedExtensions;
	}
	
	/** Sets all file extensions accepted by this parser */
	public void setAcceptedExtensions(String[] acceptedExtensions) {
		_acceptedExtensions = acceptedExtensions;
	}
}
