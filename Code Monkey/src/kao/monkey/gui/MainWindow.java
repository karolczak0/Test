package kao.monkey.gui;

import java.awt.EventQueue;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import kao.monkey.explorer.ExplorerEvent;
import kao.monkey.explorer.ExplorerFinishEvent;
import kao.monkey.explorer.ExplorerFinishListener;
import kao.monkey.explorer.ExplorerListener;
import kao.monkey.explorer.ForwardDeclarationFixer;
import kao.monkey.explorer.FilesExplorer;
import kao.monkey.explorer.ExpressionExplorerListener;
import kao.monkey.expressions.xml.XMLExpressionDefinitionReader;

import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultCaret;

/** Main application window 
  * @author k.antczak */
public class MainWindow implements ActionListener, ExplorerListener, ExplorerFinishListener {

	private JFrame mainFrame;
	private JTextField pathTextField;
	private JLabel pathLabel;
	private JButton pathButton;
	private JComboBox methodComboBox;
	private JComboBox languageComboBox;
	private JLabel methodLabel;
	private JButton processButton;
	private JTextArea infoTextArea;
	private JDirChooser dirChooser;
	private JScrollPane infoScrollPane;
	
	private File currentPath;
	private ExpressionExplorerListener[] parserListeners = {new ExpressionExplorerListener(), new ForwardDeclarationFixer() };

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainWindow window = new MainWindow();
				} catch (Exception e) {	e.printStackTrace(); }
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		pathTextFieldChanged(null);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		
		mainFrame.setTitle("Code Monkey");
		mainFrame.setResizable(false);
		mainFrame.setBounds(100, 100, 599, 370);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		pathLabel = new JLabel("Project path:");
		pathLabel.setBounds(10, 11, 89, 14);
		mainFrame.getContentPane().add(pathLabel);
		
		pathTextField = new JTextField();
		pathTextField.addActionListener(this);
		pathTextField.setBounds(90, 8, 460, 20);
		mainFrame.getContentPane().add(pathTextField);
		pathTextField.setColumns(10);
	      pathTextField.setText(new File("").getAbsolutePath());
		
		pathButton = new JButton("...");
		pathButton.addActionListener(this);
		pathButton.setBounds(560, 7, 23, 23);
		mainFrame.getContentPane().add(pathButton);
		
		processButton = new JButton("Process");
		processButton.addActionListener(this);
		processButton.setBounds(475, 70, 108, 23);
		mainFrame.getContentPane().add(processButton);
		
		methodComboBox = new JComboBox(parserListeners);
		methodComboBox.setSelectedIndex(0);
		methodComboBox.setBounds(90, 39, 493, 20);
		mainFrame.getContentPane().add(methodComboBox);
		
		methodLabel = new JLabel("Action: ");
		methodLabel.setBounds(10, 42, 89, 14);
		mainFrame.getContentPane().add(methodLabel);
		
		infoScrollPane = new JScrollPane();
		infoScrollPane.setBounds(10, 104, 573, 227);
		mainFrame.getContentPane().add(infoScrollPane);
		
		infoTextArea = new JTextArea();
		infoScrollPane.setViewportView(infoTextArea);
		infoTextArea.setLineWrap(true);
		infoTextArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		DefaultCaret caret = (DefaultCaret)infoTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JLabel languageLabel = new JLabel("Language: ");
		languageLabel.setBounds(10, 74, 89, 14);
		mainFrame.getContentPane().add(languageLabel);
		
		languageComboBox = new JComboBox(XMLExpressionDefinitionReader.getDefinitionFiles(new File(".")));
		languageComboBox.setSelectedIndex(0);
		languageComboBox.setBounds(90, 71, 375, 20);
		mainFrame.getContentPane().add(languageComboBox);
		
		dirChooser = new JDirChooser();
		mainFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();		
		
		if (source==pathButton) 
			pathButtonClicked(e);
		else if (source==pathTextField)
			pathTextFieldChanged(e);
		else if (source==processButton) {
		
			ExpressionExplorerListener parserListener = (ExpressionExplorerListener) methodComboBox.getSelectedItem();
						
			if (parserListener instanceof ForwardDeclarationFixer) {
				if (JOptionPane.showConfirmDialog(null, "This will modify explored files! Do you want to continue?",
						"Code monkey", JOptionPane.YES_NO_OPTION) ==1)
					return;
			}
			
			parserListener.reset();
			parserListener.loadParser((String) languageComboBox.getSelectedItem() + ".xml");
			
			FilesExplorer parser = new FilesExplorer(currentPath);
			parser.addExplorerListener(parserListener);
			parser.addExplorerListener(this);
			parser.addExplorerFinishListener(parserListener);
			parser.addExplorerFinishListener(this);
			parser.explore();
		}
	}
	
	private void pathButtonClicked(ActionEvent e) {
		int res = dirChooser.showOpenDialog(mainFrame);
		if( res == JFileChooser.APPROVE_OPTION ) {   
		      File f = dirChooser.getSelectedFile();   
		      pathTextField.setText(f.getPath());
		      pathTextFieldChanged(e);
		    }   
	}
	
	private void pathTextFieldChanged(ActionEvent e) {
		currentPath = new File(pathTextField.getText());
	}

	@Override
	public void elementExplored(ExplorerEvent e) {
		infoTextArea.append("#"+ ((FilesExplorer)e.getSource()).getExploredElementsCount() + "\t" + e.getExploredElement().getName() + "\n");
	}
	
	@Override
	public String toString() {
		return "File browser";
	}

	@Override
	public void explorationFinished(ExplorerFinishEvent e) {
		infoTextArea.append(e.getEventMessage());
	}
}
