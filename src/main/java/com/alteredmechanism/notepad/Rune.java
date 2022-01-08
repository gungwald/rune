package com.alteredmechanism.notepad;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.alteredmechanism.javax.swing.ImageIconLoader;

// TODO - Implement vi key bindings
// TODO - Go to line
// TODO - Syntax highlighting
// TODO - Tool bar
// TODO - Status bar
// TODO - Reload
// TODO - Recent files
// TODO - Right-click cut, copy, paste

/**
 * Writbred - A writing tablet 
 * Hreodwrit - A reed for writing
 *
 * @author Bill Chatfield
 */
public class Rune extends JFrame implements ActionListener, MouseListener, ChangeListener, KeyListener {

    private static final long serialVersionUID = 1L;

    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_FAILURE = 1;

    public static final String UNTITLED = "Untitled";
    public static final String MODIFIED = " [Modified]";
    public static final String USER_FACING_APP_NAME = "Rune";

    private int nextEmptyTabNumber = 1;

    private JMenuBar menuBar = new JMenuBar();
    private JMenu file = new JMenu("File");
    private JMenu editMenu = new JMenu("Edit");
    private JMenu viewMenu = new JMenu("View");
    private JMenu helpMenu = new JMenu("Help");

    private JMenu selectLookAndFeelMenu = new JMenu("Select Look & Feel...");

    private JMenuItem newTabItem = new JMenuItem("New Tab");
    private JMenuItem openMenuItem = new JMenuItem("Open...");
    private JMenuItem saveMenuItem = new JMenuItem("Save");
    private JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
    private JMenuItem closeMenuItem = new JMenuItem("Close Tab");
    private JMenuItem exitMenuItem = new JMenuItem("Exit");
    private JMenuItem undoMenuItem = new JMenuItem("Undo");
    private JMenuItem redoMenuItem = new JMenuItem("Redo");
    private JMenuItem cutMenuItem = new JMenuItem("Cut");
    private JMenuItem copyMenuItem = new JMenuItem("Copy");
    private JMenuItem pasteMenuItem = new JMenuItem("Paste");
    private JMenuItem copyFileNameMenuItem = new JMenuItem("Copy Full Name of File in Editor");
    private JMenuItem selectFontMenuItem = new JMenuItem("Select Font...");
    private JMenuItem aboutMenuItem = new JMenuItem("About...");
    private JMenuItem zoomInMenuItem = new JMenuItem("Zoom In");
    private JMenuItem zoomOutMenuItem = new JMenuItem("Zoom Out");

    private JFontChooser fontChooser = null;
    private JFileChooser fileChooser = null;
    private Messenger messenger = null;
    private AboutDialog aboutDialog = null;
    private LookAndFeelManager lafManager = null;
    private final JTabbedPane bufferTabs = new JTabbedPane(JTabbedPane.TOP);

    private Font bufferFont = null;
    private ImageIconLoader loader = null;

    public Rune(File f) throws FontFormatException, IOException {
        this();
        open(f);
    }

    public Rune() throws FontFormatException, IOException {
        super(USER_FACING_APP_NAME);
        this.setSize(600, 400);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.getContentPane().setLayout(new BorderLayout());

        getContentPane().add(bufferTabs, BorderLayout.CENTER);
        bufferTabs.addChangeListener(this);

        loader = new ImageIconLoader(getMessenger());
        List<Image> icons = loader.loadAll("writbred");
        if (icons.size() > 0) {
            this.setIconImage((Image) icons.get(0));
        }

        // Zoom in
        Action zoomIn = new AbstractAction() {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                zoom(1);
            }
        };
        KeyStroke plus = KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK);
        bufferTabs.getInputMap(JComponent.WHEN_FOCUSED).put(plus, "zoomIn");
        bufferTabs.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(plus, "zoomIn");
        bufferTabs.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(plus, "zoomIn");
        bufferTabs.getActionMap().put("zoomIn", zoomIn);

        // Zoom out
        Action zoomOut = new AbstractAction() {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                zoom(-1);
            }
        };
        KeyStroke minus = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK);
        bufferTabs.getInputMap(JComponent.WHEN_FOCUSED).put(minus, "zoomOut");
        bufferTabs.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(minus, "zoomOut");
        bufferTabs.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(minus, "zoomOut");
        bufferTabs.getActionMap().put("zoomOut", zoomOut);

        appendNewTab();

        this.setJMenuBar(this.menuBar);

        file.setMnemonic(KeyEvent.VK_F);
        editMenu.setMnemonic(KeyEvent.VK_E);
        viewMenu.setMnemonic(KeyEvent.VK_V);
        helpMenu.setMnemonic(KeyEvent.VK_H);

        newTabItem.setIcon(loader.getNewIcon());
        newTabItem.addActionListener(this);
        newTabItem.setMnemonic(KeyEvent.VK_T);
        newTabItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK));
        file.add(newTabItem);

        openMenuItem.setIcon(loader.getOpenIcon());
        openMenuItem.addActionListener(this);
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        file.add(openMenuItem);

        saveMenuItem.setIcon(loader.getSaveIcon());
        saveMenuItem.addActionListener(this);
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        file.add(saveMenuItem);

        saveAsMenuItem.setIcon(loader.getSaveAsIcon());
        saveAsMenuItem.addActionListener(this);
        saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        file.add(saveAsMenuItem);

        closeMenuItem.setIcon(loader.getDeleteIcon());
        closeMenuItem.addActionListener(this);
        closeMenuItem.setMnemonic(KeyEvent.VK_C);
        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        file.add(closeMenuItem);

        exitMenuItem.setIcon(loader.getExitIcon());
        exitMenuItem.addActionListener(this);
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        file.add(exitMenuItem);

        // Undo menu item
        undoMenuItem.setIcon(loader.getUndoIcon());
        undoMenuItem.setMnemonic(KeyEvent.VK_U);
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        undoMenuItem.addActionListener(this);
        editMenu.add(undoMenuItem);
        
        // Undo menu item
        redoMenuItem.setIcon(loader.getRedoIcon());
        redoMenuItem.setMnemonic(KeyEvent.VK_R);
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        redoMenuItem.addActionListener(this);
        editMenu.add(redoMenuItem);
        
        editMenu.addSeparator();
        
        // Cut menu item
        cutMenuItem.setIcon(loader.getCutIcon());
        cutMenuItem.setMnemonic(KeyEvent.VK_T);
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        cutMenuItem.addActionListener(this);
        editMenu.add(cutMenuItem);

        // Copy menu item
        copyMenuItem.setIcon(loader.getCopyIcon());
        copyMenuItem.setMnemonic(KeyEvent.VK_C);
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        copyMenuItem.addActionListener(this);
        editMenu.add(copyMenuItem);

        // Paste menu item
        pasteMenuItem.setIcon(loader.getPasteIcon());
        pasteMenuItem.setMnemonic(KeyEvent.VK_P);
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        pasteMenuItem.addActionListener(this);
        editMenu.add(pasteMenuItem);
        
        editMenu.addSeparator();
        copyFileNameMenuItem.setIcon(loader.getCopyIcon());
        copyFileNameMenuItem.addActionListener(this);
        editMenu.add(copyFileNameMenuItem);

        zoomInMenuItem.setIcon(loader.getZoomInIcon());
        zoomInMenuItem.setMnemonic(KeyEvent.VK_I);
        zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK));
        zoomInMenuItem.addActionListener(this);
        viewMenu.add(zoomInMenuItem);
        
        zoomOutMenuItem.setIcon(loader.getZoomOutIcon());
        zoomOutMenuItem.setMnemonic(KeyEvent.VK_O);
        zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK));
        zoomOutMenuItem.addActionListener(this);
        viewMenu.add(zoomOutMenuItem);
        
        // Select Font menu item
        selectFontMenuItem.setIcon(loader.getPreferencesIcon());
        selectFontMenuItem.addActionListener(this);
        selectLookAndFeelMenu.setIcon(loader.getPreferencesIcon());
        selectLookAndFeelMenu.addMouseListener(this);

        viewMenu.addSeparator();
        viewMenu.add(selectLookAndFeelMenu);
        viewMenu.add(selectFontMenuItem);

        aboutMenuItem.setIcon(loader.getAboutIcon());
        aboutMenuItem.addActionListener(this);
        aboutMenuItem.setMnemonic(KeyEvent.VK_A);
        helpMenu.add(aboutMenuItem);

        menuBar.add(file);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        // pack();
        setLocationRelativeTo(null);
        setVisible(true);
        getSelectedBuffer().requestFocusInWindow();
    }

    public void zoom(int magnitude) {
        System.out.println("zoom " + magnitude);
        RuneTextArea buffer = getSelectedBuffer();
        Font currentFont = getBufferFont();
        int adjustedSize = currentFont.getSize() + magnitude;
        Font adjustedFont = currentFont.deriveFont((float) adjustedSize);
        setBufferFont(adjustedFont);
        int width = (int) (this.getWidth() * Math.abs(0.05 + magnitude));
        int height = (int) (this.getHeight() * Math.abs(0.05 + magnitude));
        System.out.println(width + "," + height);
        this.setSize(width, height);
        this.repaint();
    }
    
    public void open(File f) throws IOException {
        if (bufferTabs.getTabCount() == 0 || !bufferTabs.getTitleAt(bufferTabs.getSelectedIndex()).startsWith(UNTITLED) || getSelectedBuffer().getText().length() > 0) {
            appendNewTab();
        }
        openIntoSelectedTab(f);
    }
    
    public String readFileContents(File f) throws IOException {
    	StringBuffer s = new StringBuffer();
        BufferedReader reader = null;
        final int END_OF_STREAM = -1;
        try {
            reader = new BufferedReader(new FileReader(f));
            char[] chunk = new char[8192];
            int count;
            while ((count = reader.read(chunk)) != END_OF_STREAM) {
            	s.append(chunk, 0, count);
            }
        }
        finally {
        	close(reader);
        }
    	return s.toString();
    }

    public void openIntoSelectedTab(File f) throws IOException {
        // Tell the change listener not to mark the tab as unsaved
        // because this is the initial setting of the contents
        // of a file that hasn't yet been changed.
        setSelectedTabTitle(null);

        getSelectedBuffer().setText(readFileContents(f));
        getSelectedBuffer().setCaretPosition(0);
        setSelectedTabTitle(f.getName());
        setSelectedTabToolTip(f.getAbsolutePath());
        updateTitle();
    }

    private void setSelectedTabToolTip(String text) {
        bufferTabs.setToolTipTextAt(bufferTabs.getSelectedIndex(), text);
    }

    private void setSelectedTabTitle(String name) {
        bufferTabs.setTitleAt(bufferTabs.getSelectedIndex(), name);
    }

    private String getSelectedTabTitle() {
        return bufferTabs.getTitleAt(bufferTabs.getSelectedIndex());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.exitMenuItem) {
            this.dispose();
        }
        else if (e.getSource() == this.closeMenuItem) {
            if (getSelectedTabTitle().endsWith(MODIFIED)) {
                File selectedFile = new File(getSelectedTabToolTip());
            	int response = JOptionPane.showConfirmDialog(this, "Save " + selectedFile.getName() + "?", "Save Before Close", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                switch(response) {
                case JOptionPane.YES_OPTION:
                	save();
                	break;
                case JOptionPane.NO_OPTION:
                    break;
                case JOptionPane.CANCEL_OPTION:
                	return;
                }
            }
            int tabIndex = bufferTabs.getSelectedIndex();
            bufferTabs.removeTabAt(tabIndex);
            tabIndex = bufferTabs.getSelectedIndex(); // Might be different after removal
            getSelectedBuffer().requestFocusInWindow();
        }
        else if (e.getSource() == this.newTabItem) {
            appendNewTab();
        }
        else if (e.getSource() == this.openMenuItem) {
        	try {
	            getFileChooser().setDialogTitle("Open File");
	            if (getFileChooser().showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	                File[] selectedFiles = getFileChooser().getSelectedFiles();
	                for (int i = 0; i < selectedFiles.length; ++i) {
	                	open(selectedFiles[i]);
	                }
	            }
        	} catch (Exception ex) {
        		ex.printStackTrace();
        		getMessenger().showError(ex);
        	}
        }
        else if (e.getSource() == this.saveMenuItem) {
            save();
        }
        else if (e.getSource() == this.saveAsMenuItem) {
            saveAs();
        }
        else if (e.getSource() == this.cutMenuItem) {
        	getSelectedBuffer().cut();
        }
        else if (e.getSource() == this.copyMenuItem) {
        	getSelectedBuffer().copy();
        }
        else if (e.getSource() == this.pasteMenuItem) {
        	getSelectedBuffer().paste();
        }
        else if (e.getSource() == this.selectFontMenuItem) {
            Cursor frameCursor = getCursor();
            Cursor bufferCursor = getSelectedBuffer().getCursor();
	    Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
            try {
                setCursor(waitCursor);
		getSelectedBuffer().setCursor(waitCursor);
                getFontChooser().setSelectedFont(getSelectedBuffer().getFont());
                int result = getFontChooser().showDialog(this);
                if (result == JFontChooser.OK_OPTION) {
                    Font font = getFontChooser().getSelectedFont();
                    System.out.println("Selected font: " + font);
                    setBufferFont(font);
                }
            }
            catch (Exception ex) {
                getMessenger().showError(ex);
                ex.printStackTrace();
            }
            finally {
                setCursor(frameCursor);
		getSelectedBuffer().setCursor(bufferCursor);
            }
        }
        else if (e.getSource() == this.aboutMenuItem) {
            getAboutDialog().setLocationRelativeTo(this);
            getAboutDialog().setVisible(true);
        }
        else if (e.getSource() == this.copyFileNameMenuItem) {
            File f = getSelectedBufferFile();
            if (f == null) {
                JOptionPane.showMessageDialog(this, "The editor does not contain a file.", "Unknown File", JOptionPane.WARNING_MESSAGE);
            } else {
                StringSelection content = new StringSelection(f.getAbsolutePath());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(content, content);
            }
        }
        else if (e.getSource() == this.undoMenuItem) {
            getSelectedBuffer().undo();
        }
        else if (e.getSource() == this.redoMenuItem) {
            getSelectedBuffer().redo();
        }
    }
    
    public void setBufferFont(Font font) {
        int tabCount = bufferTabs.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            getBufferAt(i).setFont(font);
        }
        bufferFont = font;
    }

    protected void save() {
        String absFileName = getSelectedTabToolTip();
        if (absFileName == null || absFileName.trim().length() == 0 || absFileName.startsWith(UNTITLED)) {
            saveAs();
        }
        else {
            save(new File(absFileName));
        }
    }

    /**
     * Must throw exceptions. Otherwise we'll end up returning null.
     *
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
            fileChooser.setMultiSelectionEnabled(true);
            getLafManager().addComponentToUpdate(fileChooser);
        }
        return fileChooser;
    }

    public Messenger getMessenger() {
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
        boolean fileConfirmed = false;
        boolean operationCancelled = false;
        File fileToSave = null;
        while (! fileConfirmed && !operationCancelled) {
            getFileChooser().setDialogTitle("Save File");
            int option = getFileChooser().showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                fileToSave = getFileChooser().getSelectedFile();
                if (fileToSave.exists()) {
                    int response = JOptionPane.showConfirmDialog(Rune.this, "This file already exists. Are you sure you want to overwrite it?", "Confirm Overwrite of Existing File", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    switch (response) {
                        case JOptionPane.YES_OPTION:
                            fileConfirmed = true;
                            break;
                        case JOptionPane.NO_OPTION:
                            operationCancelled = true;
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            operationCancelled = true;
                            break;
                    }
                } else {
                    fileConfirmed = true;
                }
            }
            else {
            	operationCancelled = true;
            }
        }
        if (fileConfirmed && fileToSave != null) {
            save(fileToSave);
            setSelectedTabTitle(fileToSave.getName());
            setSelectedTabToolTip(fileToSave.getAbsolutePath());
            updateTitle();
        }
    }

    protected void save(File f) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(f));
            out.write(getSelectedBuffer().getText());
            int selectedIndex = bufferTabs.getSelectedIndex();
            bufferTabs.setIconAt(selectedIndex, null);
            saveMenuItem.setEnabled(false);
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
        try {
            SystemPropertyConfigurator.autoConfigure();
            Rune n = new Rune();
            for (int i = 0; i < args.length; i++) {
                n.open(new File(args[i]));
            }
        }
        catch (Exception e) {
            new Messenger(Rune.class).showError(e);
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

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == this.selectLookAndFeelMenu) {
            if (this.selectLookAndFeelMenu.getMenuComponentCount() == 0) {
                getLafManager().initChooserMenuItems(selectLookAndFeelMenu);
                getLafManager().addComponentToUpdate(this);
            }
        }
    }

    public void mouseExited(MouseEvent e) {}

    public void stateChanged(ChangeEvent event) {
        // If a tab was clicked
        if (event.getSource() == bufferTabs) {
            // Focus on the text itself.
            getSelectedBuffer().requestFocusInWindow();
            updateTitle();
        }
    }

    protected void updateTitle() {
        File f = getSelectedBufferFile();
        if (f == null) {
            setTitle(USER_FACING_APP_NAME);
        } else {
            setTitle(USER_FACING_APP_NAME + " - " + f.getName());
        }
    }
    
    private void appendNewTab() {
        RuneTextArea text = new RuneTextArea(this);
        String tabTitle = getNextEmptyTabName();
        bufferTabs.addTab(tabTitle, null, text.getScrollPane(), tabTitle);
        int tabIndex = bufferTabs.getTabCount() - 1;
        bufferTabs.setSelectedIndex(tabIndex);
        Icon saveIcon = null;
        try {
            saveIcon = loader.getSaveIcon();
        } catch (Exception e) {
            System.err.println(e);
        }
        text.getDocument().addDocumentListener(new BufferChangedListener(bufferTabs, MODIFIED, saveMenuItem, saveIcon));
        text.requestFocus();
    }

    private String getNextEmptyTabName() {
        return UNTITLED + " " + nextEmptyTabNumber++;
    }

    protected RuneTextArea getSelectedBuffer() {
        JScrollPane scroll = (JScrollPane) bufferTabs.getSelectedComponent();
        JViewport view = (JViewport) scroll.getComponent(0);
        RuneTextArea text = (RuneTextArea) view.getComponent(0);
        return text;
    }

    protected JTextArea getBufferAt(int index) {
        JScrollPane scroll = (JScrollPane) bufferTabs.getComponentAt(index);
        JViewport view = (JViewport) scroll.getComponent(0);
        JTextArea text = (JTextArea) view.getComponent(0);
        return text;
    }

    private String getSelectedTabToolTip() {
        return bufferTabs.getToolTipTextAt(bufferTabs.getSelectedIndex());
    }
    
    protected File getSelectedBufferFile() {
        String toolTip = getSelectedTabToolTip();
        File f;
        if (toolTip != null && !toolTip.startsWith(UNTITLED)) {
            f = new File(toolTip);
        } else {
            f = null;
        }
        return f;
    }

    public Font getBufferFont() {
        if (bufferFont == null) {
            // Don't build the fontChooser just for this because it would slow
            // down the startup time.
            if (fontChooser == null) {
                bufferFont = getSystemDefaultFont();
            }
            else {
                bufferFont = fontChooser.getSelectedFont();
            }
        }
        return bufferFont;
    }

    public Font getSystemDefaultFont() {
        Font f = null;
        if (System.getProperty("os.name").startsWith("Windows")) {
            if ((f = newFont("Consolas", Font.PLAIN, 12)) == null) {
                if ((f = newFont("Terminal", Font.PLAIN, 11)) == null) {
                    if ((f = newFont("Fixedsys", Font.PLAIN, 11)) == null) {
                        f = newFont("Lucidia Console", Font.PLAIN, 12);
                    }
                }
            }
        }
        else if (System.getProperty("os.name").startsWith("Mac")) {
            f = newFont("Monaco", Font.PLAIN, 12);
        }
        if (f == null) {
            f = new Font("Monospaced", Font.PLAIN, 12);
        }
        return f;
    }

    public Font newFont(String family, int face, int size) {
        Font f = new Font(family, face, size);
        if (! f.getFamily().equals(family)) {
            f = null;
        }
        return f;
    }
    public Font findExistingFont(String[] fontFamiliesToSearch) {
        Font f = null;
        boolean foundFont = false;
        for (int i = 0; i < fontFamiliesToSearch.length; i++) {
            f = new Font(fontFamiliesToSearch[i], Font.PLAIN, 12);
            if (f.getFamily().equals(fontFamiliesToSearch[i])) {
                // The font we asked for actually exists.
                foundFont = true;
                break;
            }
        }
        if (! foundFont) {
            f = null;
        }
        return f;
    }

    public void keyTyped(KeyEvent e) {
//        System.out.println("keyTyped: " + e);
//        if (e.getKeyChar() == '+' && e.getModifiers() > 0) {
//            JTextArea buffer = getSelectedBuffer();
//            Font currentFont = buffer.getFont();
//            int upSize = currentFont.getSize() + 1;
//            Font upFont = currentFont.deriveFont(upSize);
//            buffer.setFont(upFont);
//        }
//        else if (e.getKeyChar() == '-' && e.getModifiers() > 0) {
//            JTextArea buffer = getSelectedBuffer();
//            Font currentFont = buffer.getFont();
//            int downSize = currentFont.getSize() + 1;
//            Font downFont = currentFont.deriveFont(downSize);
//            buffer.setFont(downFont);
//        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        
    }
}
