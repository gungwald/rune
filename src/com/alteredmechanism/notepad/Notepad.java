package com.alteredmechanism.notepad;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import com.alteredmechanism.javax.swing.ImageIconLoader;

// TODO - Link current font with selector
// TODO - Link current file with selector
// TODO - Limit selectable fonts to monospaced fonts
// TODO - Register included fonts
// TODO - Provide font selector option to limit to included fonts
// TODO - Implement vi key bindings
// TODO - Go to line
// TODO - Tabs
// TODO - Syntax highlighting
// TODO - Tool bar
// TODO - Status bar
// TODO - New
// TODO - Reload
// TODO - Save as
// TODO - Recent files
// TODO - Right-click cut, copy, paste

/**
 * Writbred - A writing tablet Hreodwrit - A reed for writing
 * 
 * @author Bill Chatfield
 */
public class Notepad extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private AntiAliasedJTextArea textArea = new AntiAliasedJTextArea(24, 80);
	private JScrollPane textScrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JMenuBar menuBar = new JMenuBar();
	private JMenu file = new JMenu("File");
	private JMenu editMenu = new JMenu("Edit");
	private JMenu lookAndFeelMenu = new JMenu("Look & Feel");
	private JMenuItem openMenuItem = new JMenuItem("Open...");
	private JMenuItem saveMenuItem = new JMenuItem("Save");
	private JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
	private JMenuItem close = new JMenuItem("Close");
	private JMenuItem cutMenuItem = new JMenuItem("Cut");
	private JMenuItem copyMenuItem = new JMenuItem("Copy");
	private JMenuItem pasteMenuItem = new JMenuItem("Paste");
	private JMenuItem selectFontMenuItem = new JMenuItem("Select Font...");
	private JFontChooser fontChooser = new JFontChooser();
	private JFileChooser fileChooser = new JFileChooser();
	private Messenger messenger = new Messenger(this);

	public Notepad() {
		super("Writbred - It means \"writing tablet\" in Old English");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(textScrollPane);
		this.setJMenuBar(this.menuBar);

		ImageIconLoader loader = new ImageIconLoader(messenger);
		List icons = loader.loadAll("writbred");
		this.setIconImage((Image) icons.get(0));

		file.setMnemonic(KeyEvent.VK_F);
		System.out.println("Menu Font = " + file.getFont());
		openMenuItem.addActionListener(this);
		openMenuItem.setMnemonic(KeyEvent.VK_O);
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		file.add(openMenuItem);
		
		saveMenuItem.addActionListener(this);
		saveMenuItem.setMnemonic(KeyEvent.VK_S);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		file.add(saveMenuItem);
		
		saveAsMenuItem.addActionListener(this);
		file.add(saveAsMenuItem);
		close.setMnemonic(KeyEvent.VK_W);
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
		close.addActionListener(this);
		file.add(close);

		// Cut menu item
		cutMenuItem.setMnemonic(KeyEvent.VK_X);
		cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
		cutMenuItem.addActionListener(this);
		editMenu.add(cutMenuItem);
		
		// Copy menu item
		copyMenuItem.setMnemonic(KeyEvent.VK_C);
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
		copyMenuItem.addActionListener(this);
		editMenu.add(copyMenuItem);
		
		// Paste menu item
		pasteMenuItem.setMnemonic(KeyEvent.VK_V);
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
		pasteMenuItem.addActionListener(this);
		editMenu.add(pasteMenuItem);
		
		// Select Font menu item
		selectFontMenuItem.addActionListener(this);
		editMenu.add(selectFontMenuItem);

		LookAndFeelManager lafMgr = new LookAndFeelManager(messenger);
		lafMgr.initChooserMenuItems(lookAndFeelMenu, new Component[] { this, fileChooser, fontChooser });

		menuBar.add(file);
		menuBar.add(editMenu);
		menuBar.add(lookAndFeelMenu);

		pack();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.close) {
			this.dispose();
		}
		else if (e.getSource() == this.openMenuItem) {
			fileChooser.setDialogTitle("Choose a file to open");
			fileChooser.showOpenDialog(this);
			File selectedFile = fileChooser.getSelectedFile();
			if (selectedFile != null) {
				textArea.setText("");
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(selectedFile));
					String line = null;
					while ((line = reader.readLine()) != null) {
						textArea.append(line + "\n");
					}
					this.setTitle(selectedFile.getName() + " - Writbred");
				}
				catch (Exception ex) {
					messenger.showError(ex);
				}
				finally {
					close(reader);
				}
			}
		}
		else if (e.getSource() == this.saveMenuItem) {
			if (fileChooser.getSelectedFile() == null) {
				saveAs();
			}
			else {
				save(fileChooser.getSelectedFile());
			}
		}
		else if (e.getSource() == this.saveAsMenuItem) {
			saveAs();
		}
		else if (e.getSource() == this.selectFontMenuItem) {
			fontChooser.setSelectedFont(textArea.getFont());
			int result = fontChooser.showDialog(this);
			if (result == JFontChooser.OK_OPTION) {
				Font font = fontChooser.getSelectedFont();
				System.out.println("Selected Font : " + font);
				textArea.setFont(font);
			}
		}
	}

	protected void saveAs() {
		fileChooser.setDialogTitle("Choose the name of the file to save");
		int option = fileChooser.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			save(fileChooser.getSelectedFile());
		}
	}
			
	protected void save(File f) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(f));
			out.write(textArea.getText());
		}
		catch (Exception ex) {
			messenger.showError(ex);
		}
		finally {
			close(out);
		}
	}
	
	private void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			}
			catch (Exception e) {
				messenger.showError(e);
			}
		}
	}

	private void close(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			}
			catch (Exception e) {
				messenger.showError(e);
			}
		}
	}

	public static void main(String args[]) {
		try {
			SystemPropertyConfigurator.autoConfigure();
			Notepad app = new Notepad();
			app.setVisible(true);
		}
		catch (Exception e) {
			new Messenger(Notepad.class).showError(e);
		}
	}
}