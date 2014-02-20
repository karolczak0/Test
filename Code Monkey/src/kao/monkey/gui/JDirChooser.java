package kao.monkey.gui;

import java.io.File;

import javax.swing.JFileChooser;

/** Directory choose dialog. 
 * @author k.antczak */
@SuppressWarnings("serial")
public class JDirChooser extends JFileChooser {

	public JDirChooser() {
		super();
		setCurrentDirectory(new File("."));
		setFileFilter(new DirFilter());
		this.setFileSelectionMode(DIRECTORIES_ONLY);
		removeChoosableFileFilter( getAcceptAllFileFilter() );  	
		this.setName("Select project directory");
	}

}


