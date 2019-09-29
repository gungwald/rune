package com.alteredmechanism.notepad;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

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
public class Notepad extends JFrame implements ActionListener, MouseListener {

    private static final long serialVersionUID = 1L;

    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_FAILURE = 1;

    private AntiAliasedJTextArea textArea = new AntiAliasedJTextArea(24, 80);
    private JScrollPane textScrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private JMenuBar menuBar = new JMenuBar();
    private JMenu file = new JMenu("File");
    private JMenu editMenu = new JMenu("Edit");
    private JMenu viewMenu = new JMenu("View");
    private JMenu helpMenu = new JMenu("Help");

    private JMenu selectLookAndFeelMenu = new JMenu("Select Look & Feel...");
    
    private JMenuItem openMenuItem = new JMenuItem("Open...");
    private JMenuItem saveMenuItem = new JMenuItem("Save");
    private JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
    private JMenuItem close = new JMenuItem("Close");
    private JMenuItem cutMenuItem = new JMenuItem("Cut");
    private JMenuItem copyMenuItem = new JMenuItem("Copy");
    private JMenuItem pasteMenuItem = new JMenuItem("Paste");
    private JMenuItem selectFontMenuItem = new JMenuItem("Select Font...");
    private JMenuItem aboutMenuItem = new JMenuItem("About...");
    
    private JFontChooser fontChooser = null;
    private JFileChooser fileChooser = null;
    private Messenger messenger = null;
    private AboutDialog aboutDialog = null;
	private LookAndFeelManager lafManager = null;

    public Notepad(File f) throws FontFormatException, IOException {
        this();
        open(f);
    }

    public Notepad() throws FontFormatException, IOException {
        super("Notepad");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textArea.setTabSize(8);
        textArea.setBorder(new EmptyBorder(new Insets(3,3,3,3)));

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(textScrollPane);
        this.setJMenuBar(this.menuBar);

        ImageIconLoader loader = new ImageIconLoader(getMessenger());
        List icons = loader.loadAll("writbred");
        this.setIconImage((Image) icons.get(0));

        file.setMnemonic(KeyEvent.VK_F);
        editMenu.setMnemonic(KeyEvent.VK_E);
        
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
        selectLookAndFeelMenu.addMouseListener(this);

        viewMenu.add(selectLookAndFeelMenu);
        viewMenu.add(selectFontMenuItem);

        aboutMenuItem.addActionListener(this);
        helpMenu.add(aboutMenuItem);
        
        menuBar.add(file);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void open(File f) {
        textArea.setText("");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
            textArea.setCaretPosition(0);
            this.setTitle(f.getName() + " - Notepad");
        }
        catch (Exception ex) {
            getMessenger().showError(ex);
        }
        finally {
            close(reader);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.close) {
            this.dispose();
        }
        else if (e.getSource() == this.openMenuItem) {
            getFileChooser().setDialogTitle("Choose a file to open");
            getFileChooser().showOpenDialog(this);
            File selectedFile = getFileChooser().getSelectedFile();
            if (selectedFile != null) {
                open(selectedFile);
            }
        }
        else if (e.getSource() == this.saveMenuItem) {
            if (getFileChooser().getSelectedFile() == null) {
                saveAs();
            }
            else {
                save(getFileChooser().getSelectedFile());
            }
        }
        else if (e.getSource() == this.saveAsMenuItem) {
            saveAs();
        }
        else if (e.getSource() == this.selectFontMenuItem) {
            try {
				getFontChooser().setSelectedFont(textArea.getFont());
	            int result = getFontChooser().showDialog(this);
	            if (result == JFontChooser.OK_OPTION) {
	                Font font = getFontChooser().getSelectedFont();
	                System.out.println("Selected Font : " + font);
	                textArea.setFont(font);
	                textArea.setTabSize(8);
	            }
			} catch (Exception ex) {
				getMessenger().showError(ex);
				ex.printStackTrace();
			}
        }
        else if (e.getSource() == this.aboutMenuItem) {
            getAboutDialog().setLocationRelativeTo(this);
        	getAboutDialog().setVisible(true);
        }
    }

    /**
     * Must throw exceptions. Otherwise we'll end up returning null.
     * @return
     * @throws IOException 
     * @throws FontFormatException 
     */
    private JFontChooser getFontChooser() throws FontFormatException, IOException {
    	if (fontChooser == null) {
			fontChooser = new JFontChooser(getMessenger());
			getLafManager().addComponentToUpdate(fontChooser);
    	}
		return fontChooser;
	}

    private JFileChooser getFileChooser() {
    	if (fileChooser == null) {
			fileChooser = new JFileChooser();
			getLafManager().addComponentToUpdate(fileChooser);
    	}
    	return fileChooser;
    }
    
    private Messenger getMessenger() {
    	if (messenger == null) {
			messenger = new Messenger(this);
    	}
    	return messenger;
    }
    
    private AboutDialog getAboutDialog() {
    	if (aboutDialog == null) {
			aboutDialog = new AboutDialog(this);
			getLafManager().addComponentToUpdate(aboutDialog);
    	}
    	return aboutDialog;
    }
    
    private LookAndFeelManager getLafManager() {
    	if (lafManager == null) {
    		lafManager = new LookAndFeelManager(getMessenger());
    	}
    	return lafManager;
    }
    
	protected void saveAs() {
        getFileChooser().setDialogTitle("Choose the name of the file to save");
        int option = getFileChooser().showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            save(getFileChooser().getSelectedFile());
        }
    }

    protected void save(File f) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(f));
            out.write(textArea.getText());
        }
        catch (Exception ex) {
            getMessenger().showError(ex);
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
                getMessenger().showError(e);
            }
        }
    }

    private void close(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            }
            catch (Exception e) {
                getMessenger().showError(e);
            }
        }
    }

    public void dispose() {
        super.dispose();
    }

    public static void main(String args[]) {
        List instances = new ArrayList();
        try {
            SystemPropertyConfigurator.autoConfigure();
            if (args.length == 0) {
                instances.add(new Notepad());
            } else {
                for (int i = 0; i < args.length; i++) {
                    instances.add(new Notepad(new File(args[i])));
                }
            }
        }
        catch (Exception e) {
            new Messenger(Notepad.class).showError(e);
            for (int i = 0; i < instances.size(); i++) {
                ((Notepad) instances.get(i)).dispose();
            }
            System.exit(EXIT_FAILURE);
        }
    }
    
    /**
     * @deprecated - Use the built-in setLocationRelativeTo instead
     */
	public void center() {
		Point center = new Point();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		center.x = dim.width / 2 - this.getWidth() / 2;
		center.y = dim.height / 2 - this.getHeight() / 2;
        this.setLocation(center);
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
        if (e.getSource() == this.selectLookAndFeelMenu) {
        	if (this.selectLookAndFeelMenu.getMenuComponentCount() == 0) {
	            getLafManager().initChooserMenuItems(selectLookAndFeelMenu);
	            getLafManager().addComponentToUpdate(this);
        	}
        }
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
