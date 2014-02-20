package kao.monkey.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/** Directory filter for directory chooser. 
 * @author k.antczak */
class DirFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		return f.isDirectory();
	}

	@Override
	public String getDescription() {
		return "Directories" ;
	}
	
}