package com.alteredmechanism.rune;

import com.alteredmechanism.javax.swing.ImageIconLoader;
import com.alteredmechanism.javax.swing.LookAndFeelManager;
import com.alteredmechanism.rune.actions.SaveAction;
import com.alteredmechanism.rune.actions.ZoomInAction;
import com.alteredmechanism.rune.actions.ZoomOutAction;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.util.List;

// TODO - Implement vi key bindings
// TODO - Go to line
// TODO - Syntax highlighting
// TODO - Tool bar
// TODO - Status bar
// TODO - Reload
// TODO - Recent files
// TODO - Right-click cut, copy, paste

/**
 * Writbred - A writing tablet Hreodwrit - A reed for writing
 * 
 * @author Bill Chatfield
 */
public class Rune extends JFrame implements ActionListener, MouseListener,
        ChangeListener, KeyListener, CaretListener {

    private static final long serialVersionUID = 1L;

    public static final String ZOOM_IN_KEY_KEY = "zoomIn";
    public static final String ZOOM_OUT_KEY_KEY = "zoomOut";

    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_FAILURE = 1;

    public static final String UNTITLED = "Untitled";
    public static final String MODIFIED = " [Modified]";
    public static final String USER_FACING_APP_NAME = "Rune";

    private int nextEmptyTabNumber = 1;

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu file = new JMenu("File");
    private JMenu editMenu = new JMenu("Edit");
    private JMenu viewMenu = new JMenu("View");
    private JMenu helpMenu = new JMenu("Help");

    private JMenu selectLookAndFeelMenu = new JMenu("Select Look & Feel...");

    private JMenuItem newTabItem = new JMenuItem("New Tab");
    private JMenuItem openMenuItem = new JMenuItem("Open...");
    public JMenuItem saveMenuItem = new JMenuItem("Save");
    private JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
    private JMenuItem closeMenuItem = new JMenuItem("Close Tab");
    private JMenuItem exitMenuItem = new JMenuItem("Exit");
    private JMenuItem undoMenuItem = new JMenuItem("Undo");
    private JMenuItem redoMenuItem = new JMenuItem("Redo");
    private JMenuItem cutMenuItem = new JMenuItem("Cut");
    private JMenuItem copyMenuItem = new JMenuItem("Copy");
    private JMenuItem pasteMenuItem = new JMenuItem("Paste");
    private JMenuItem copyFileNameMenuItem = new JMenuItem(
            "Copy Full Name of File in Editor");
    private JMenuItem selectFontMenuItem = new JMenuItem("Select Font...");
    private JMenuItem aboutMenuItem = new JMenuItem("About...");
    private JMenuItem zoomInMenuItem = new JMenuItem("Zoom In");
    private JMenuItem zoomOutMenuItem = new JMenuItem("Zoom Out");
    private JCheckBoxMenuItem lineWrapMenuItem = new JCheckBoxMenuItem("Wrap Lines");

    private JFontChooser fontChooser = null;
    private FileDialog fileChooser = null;
    private static Messenger messenger = Messenger.getInstance();
    private AboutDialog aboutDialog = null;
    private LookAndFeelManager lafManager = null;
    public final JTabbedPane bufferTabs = new JTabbedPane(JTabbedPane.TOP);
    private final StatusBar statusBar = new StatusBar(this);

    private Font bufferFont = null;
    private ImageIconLoader loader = null;
    protected SaveAction save = new SaveAction(this);
    protected ZoomInAction zoomIn = new ZoomInAction(this);
    protected ZoomOutAction zoomOut = new ZoomOutAction(this);

    /**
     * A constructor that does nothing is needed so that the switch from
     * static to object context can be made before configuration starts.
     */
    public Rune() {

    }
    public void init(File f) throws FontFormatException, IOException {
        init();
        open(f);
    }

    public void init() throws FontFormatException, IOException {
        this.setTitle(USER_FACING_APP_NAME);

        int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        getContentPane().add(statusBar, BorderLayout.SOUTH);
        getContentPane().add(bufferTabs, BorderLayout.CENTER);
        bufferTabs.addChangeListener(this);

        loader = new ImageIconLoader(getMessenger());
        List<Image> icons = loader.loadAll("vegvisir");
        if (!icons.isEmpty()) {
            this.setIconImage(icons.get(0));
        }

        appendNewTab();

        this.setJMenuBar(this.menuBar);

        file.setMnemonic(KeyEvent.VK_F);
        editMenu.setMnemonic(KeyEvent.VK_E);
        viewMenu.setMnemonic(KeyEvent.VK_V);
        helpMenu.setMnemonic(KeyEvent.VK_H);

        newTabItem.setIcon(loader.getNewIcon());
        newTabItem.addActionListener(this);
        newTabItem.setMnemonic(KeyEvent.VK_T);
        newTabItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, shortcutKeyMask));
        file.add(newTabItem);

        openMenuItem.setIcon(loader.getOpenIcon());
        openMenuItem.addActionListener(this);
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcutKeyMask));
        file.add(openMenuItem);

//        ComponentInputMap inputMap = new KeyBindings(bufferTabs).getInputMap();
//        bufferTabs.setInputMap(JComponent.WHEN_FOCUSED, inputMap);
//        bufferTabs.setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap);
//        bufferTabs.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, inputMap);

//        bufferTabs.getActionMap().put(KeyBindings.SAVE_ACTION_ID, save);

        saveMenuItem.setAction(save);
//        saveMenuItem.setIcon(loader.getSaveIcon());
//        saveMenuItem.addActionListener(this);
//        saveMenuItem.setMnemonic(KeyEvent.VK_S);
//        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKeyMask));
        file.add(saveMenuItem);

        saveAsMenuItem.setIcon(loader.getSaveAsIcon());
        saveAsMenuItem.addActionListener(this);
        saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, shortcutKeyMask));
        file.add(saveAsMenuItem);

        closeMenuItem.setIcon(loader.getDeleteIcon());
        closeMenuItem.addActionListener(this);
        closeMenuItem.setMnemonic(KeyEvent.VK_C);
        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, shortcutKeyMask));
        file.add(closeMenuItem);

        exitMenuItem.setIcon(loader.getExitIcon());
        exitMenuItem.addActionListener(this);
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        file.add(exitMenuItem);

        // Undo menu item
        undoMenuItem.setIcon(loader.getUndoIcon());
        undoMenuItem.setMnemonic(KeyEvent.VK_U);
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortcutKeyMask));
        undoMenuItem.addActionListener(this);
        editMenu.add(undoMenuItem);

        // Undo menu item
        redoMenuItem.setIcon(loader.getRedoIcon());
        redoMenuItem.setMnemonic(KeyEvent.VK_R);
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, shortcutKeyMask));
        redoMenuItem.addActionListener(this);
        editMenu.add(redoMenuItem);

        editMenu.addSeparator();

        // Cut menu item
        cutMenuItem.setIcon(loader.getCutIcon());
        cutMenuItem.setMnemonic(KeyEvent.VK_T);
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, shortcutKeyMask));
        cutMenuItem.addActionListener(this);
        editMenu.add(cutMenuItem);

        // Copy menu item
        copyMenuItem.setIcon(loader.getCopyIcon());
        copyMenuItem.setMnemonic(KeyEvent.VK_C);
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, shortcutKeyMask));
        copyMenuItem.addActionListener(this);
        editMenu.add(copyMenuItem);

        // Paste menu item
        pasteMenuItem.setIcon(loader.getPasteIcon());
        pasteMenuItem.setMnemonic(KeyEvent.VK_P);
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, shortcutKeyMask));
        pasteMenuItem.addActionListener(this);
        editMenu.add(pasteMenuItem);

        editMenu.addSeparator();
        copyFileNameMenuItem.setIcon(loader.getCopyIcon());
        copyFileNameMenuItem.addActionListener(this);
        editMenu.add(copyFileNameMenuItem);

        zoomInMenuItem.setAction(zoomIn);
        viewMenu.add(zoomInMenuItem);
        zoomOutMenuItem.setAction(zoomOut);
        viewMenu.add(zoomOutMenuItem);
        bindControlKey(KeyEvent.VK_UP, zoomIn);
        bindControlKey(KeyEvent.VK_DOWN, zoomOut);

        // Line wrap menu item
        lineWrapMenuItem.setIcon(loader.getZoomOutIcon());
        lineWrapMenuItem.setMnemonic(KeyEvent.VK_W);
        lineWrapMenuItem.addActionListener(this);
        viewMenu.add(lineWrapMenuItem);

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

        // Overriding getPreferredSize and calling pack works better than
        // calling setPreferredSize.
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        getSelectedBuffer().requestFocusInWindow();
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    /**
     * Bind a key, modified by the platform shortcut key (Control for Windows,
     * Command for Mac) to an action.
     * @param key    A key code as specified by one of KeyEvent.VK_*
     * @param action The action to invoke when the key is pressed
     */
    public void bindControlKey(int key, Action action) {
        String actionMapKeyKey = (String) action.getValue(Action.NAME);
        KeyStroke keySequence = KeyStroke.getKeyStroke(key, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        bufferTabs.getInputMap(JComponent.WHEN_FOCUSED).put(keySequence, actionMapKeyKey);
        bufferTabs.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keySequence, actionMapKeyKey);
        bufferTabs.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keySequence, actionMapKeyKey);
        bufferTabs.getActionMap().put(actionMapKeyKey, action);
    }

    public ImageIconLoader getLoader() {
        if (loader == null) {
            loader = new ImageIconLoader(getMessenger());
        }
        return loader;
    }

    public void open(File f) {
        if (bufferTabs.getTabCount() == 0
                || !bufferTabs.getTitleAt(bufferTabs.getSelectedIndex())
                        .startsWith(UNTITLED)
                || getSelectedBuffer().getText().length() > 0) {
            appendNewTab();
        }
        openIntoSelectedTab(f);
    }

    public String readFileContents(File f) throws IOException {
        StringBuilder s = new StringBuilder();
        BufferedReader reader = null;
        final int END_OF_STREAM = -1;
        try {
            reader = new BufferedReader(new FileReader(f));
            char[] chunk = new char[8192];
            int count;
            while ((count = reader.read(chunk)) != END_OF_STREAM) {
                s.append(chunk, 0, count);
            }
        } finally {
            close(reader);
        }
        return s.toString();
    }

    public void openIntoSelectedTab(File f) {
        // Tell the change listener not to mark the tab as unsaved
        // because this is the initial setting of the contents
        // of a file that hasn't yet been changed.
        setSelectedTabTitle(null);

        try {
            getSelectedBuffer().setText(readFileContents(f));
            getSelectedBuffer().setCaretPosition(0);
            setSelectedTabTitle(f.getName());
            setSelectedTabToolTip(f.getAbsolutePath());
            updateTitle();
        } catch (Exception e) {
            getSelectedBuffer().setText("");
            getSelectedBuffer().setCaretPosition(0);
            setSelectedTabTitle("EMPTY");
            setSelectedTabToolTip("EMPTY");
            updateTitle();
            getMessenger().showError("Failed to load file into tab", e);
        }
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
        } else if (e.getSource() == this.closeMenuItem) {
            if (getSelectedTabTitle().endsWith(MODIFIED)) {
                File selectedFile = new File(getSelectedTabToolTip());
                int response = JOptionPane.showConfirmDialog(this, "Save "
                        + selectedFile.getName() + "?", "Save Before Close",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                switch (response) {
                case JOptionPane.YES_OPTION:
                    save.actionPerformed(e);
                    break;
                case JOptionPane.NO_OPTION:
                    break;
                case JOptionPane.CANCEL_OPTION:
                    return;
                }
            }
            int tabIndex = bufferTabs.getSelectedIndex();
            bufferTabs.removeTabAt(tabIndex);
            tabIndex = bufferTabs.getSelectedIndex(); // Might be different
                                                      // after removal
            getSelectedBuffer().requestFocusInWindow();
        } else if (e.getSource() == this.newTabItem) {
            appendNewTab();
        } else if (e.getSource() == this.openMenuItem) {
            try {
                getFileChooser().setTitle("Open File");
                getFileChooser().setMode(FileDialog.LOAD);
                getFileChooser().setVisible(true);
                String fileName = getFileChooser().getFile();
                String dir = getFileChooser().getDirectory();
                if (fileName != null && dir != null) {
                    File selectedFile = new File(dir, fileName);
                    open(selectedFile);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                getMessenger().showError(ex);
            }
        } else if (e.getSource() == this.saveMenuItem) {
            save.actionPerformed(e);
        } else if (e.getSource() == this.saveAsMenuItem) {
            saveAs();
        } else if (e.getSource() == this.cutMenuItem) {
            getSelectedBuffer().cut();
        } else if (e.getSource() == this.copyMenuItem) {
            getSelectedBuffer().copy();
        } else if (e.getSource() == this.pasteMenuItem) {
            getSelectedBuffer().paste();
        } else if (e.getSource() == this.selectFontMenuItem) {
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
            } catch (Exception ex) {
                getMessenger().showError(ex);
                ex.printStackTrace();
            } finally {
                setCursor(frameCursor);
                getSelectedBuffer().setCursor(bufferCursor);
            }
        } else if (e.getSource() == this.aboutMenuItem) {
            getAboutDialog().setLocationRelativeTo(this);
            getAboutDialog().setVisible(true);
        } else if (e.getSource() == this.copyFileNameMenuItem) {
            File f = getSelectedBufferFile();
            if (f == null) {
                JOptionPane.showMessageDialog(this,
                        "The editor does not contain a file.", "Unknown File",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                StringSelection content = new StringSelection(f.getAbsolutePath());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(content, content);
            }
        } else if (e.getSource() == this.undoMenuItem) {
            getSelectedBuffer().undo();
        } else if (e.getSource() == this.redoMenuItem) {
            getSelectedBuffer().redo();
        } else if (e.getSource() == this.lineWrapMenuItem) {
            setLineWrap(lineWrapMenuItem.isSelected());
        }
    }

    public void setLineWrap(boolean isSelected) {
        int tabCount = bufferTabs.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            getBufferAt(i).setLineWrap(isSelected);
        }
    }

    public boolean getLineWrap() {
        return lineWrapMenuItem.isSelected();
    }

    public void setBufferFont(Font font) {
        int tabCount = bufferTabs.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            getBufferAt(i).setFont(font);
        }
        bufferFont = font;
    }

    /**
     * Must throw exceptions. Otherwise, we'll end up returning null.
     * 
     * @return The font chooser
     * @throws IOException If a bad thing happens
     * @throws FontFormatException If a bad thing happens
     */
    private JFontChooser getFontChooser() throws FontFormatException,
            IOException {
        if (fontChooser == null) {
            fontChooser = new JFontChooser(getMessenger());
            LookAndFeelManager.getInstance().addComponentToUpdate(fontChooser);
        }
        return fontChooser;
    }

    private FileDialog getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new FileDialog(this);
            // fileChooser.setMultiSelectionEnabled(true);
            LookAndFeelManager.getInstance().addComponentToUpdate(fileChooser);
        }
        return fileChooser;
    }

    public Messenger getMessenger() {
        if (messenger == null) {
            messenger = Messenger.getInstance();
        }
        return messenger;
    }

    private AboutDialog getAboutDialog() {
        if (aboutDialog == null) {
            aboutDialog = new AboutDialog(this);
            LookAndFeelManager.getInstance().addComponentToUpdate(aboutDialog);
        }
        return aboutDialog;
    }

    public void saveAs() {
        boolean fileConfirmed = false;
        boolean operationCancelled = false;
        File fileToSave = null;
        while (!fileConfirmed && !operationCancelled) {
            getFileChooser().setTitle("Save File");
            getFileChooser().setMode(FileDialog.SAVE);
            getFileChooser().setVisible(true);
            String fileName = getFileChooser().getFile();
            String dir = getFileChooser().getDirectory();
            if (fileName != null && dir != null) {
                fileToSave = new File(dir, fileName);
                if (fileToSave.exists()) {
                    int response = JOptionPane
                            .showConfirmDialog(
                                    Rune.this,
                                    "This file already exists. Are you sure you want to overwrite it?",
                                    "Confirm Overwrite of Existing File",
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
                    switch (response) {
                    case JOptionPane.YES_OPTION:
                        fileConfirmed = true;
                        break;
                    case JOptionPane.NO_OPTION:
                        case JOptionPane.CANCEL_OPTION:
                            operationCancelled = true;
                        break;
                    }
                } else {
                    fileConfirmed = true;
                }
            } else {
                operationCancelled = true;
            }
        }
        if (fileConfirmed) {
            save.save(fileToSave);
            setSelectedTabTitle(fileToSave.getName());
            setSelectedTabToolTip(fileToSave.getAbsolutePath());
            updateTitle();
        }
    }


    private void close(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception e) {
                getMessenger().showError(e);
            }
        }
    }

    public void close(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (Exception e) {
                getMessenger().showError(e);
            }
        }
    }

    public void dispose() {
        super.dispose();
    }

    /**
     * This is a thing that happens on older versions of MacOS for programs that
     * use the Carbon framework. This is true for Java 1.5 on MacOS 10.4.11. A
     * command line argument in the form "-psn_X_XXXXXXX" where each X is a
     * digit is passed to the program.
     * 
     * @param s
     *            The string to check to see if it is a process serial number
     * @return true If <code>s</code> is a process serial number false If not
     */
    public static boolean isMacCarbonProcessSerialNumber(String s) {
        return System.getProperty("os.name").startsWith("Mac")
                && s.startsWith("-psn_");
    }

    public static void main(String[] args) {
        // TODO - Don't override properties the user put on the command line.
        // This does not work here. Only in the REAL main.
        System.setProperty("sun.java2d.uiScale", "2");
        try {
            SystemPropertyConfigurator.autoConfigure(); // System properties should be set first.
            LookAndFeelManager.getInstance().setMessenger(messenger);//.setOptimalLookAndFeel();

            Rune n = new Rune();
            n.getMessenger().setParent(n);
            n.init();
            for (String arg : args) {
                if (!isMacCarbonProcessSerialNumber(arg)) {
                    try {
                        n.open(new File(arg));
                    } catch (Exception e) {
                        n.getMessenger().showError( "Failed to open file specified on command line: " + arg, e);
                    }
                }
            }
        } catch (Exception e) {
            Messenger.getInstance().showError(e);
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
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == this.selectLookAndFeelMenu) {
            if (this.selectLookAndFeelMenu.getMenuComponentCount() == 0) {
                LookAndFeelManager.getInstance().initChooserMenuItems(selectLookAndFeelMenu);
                LookAndFeelManager.getInstance().addComponentToUpdate(this);
            }
        }
    }

    public void mouseExited(MouseEvent e) {
    }

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
            getMessenger().showError(e);
        }
        text.getDocument().addDocumentListener(
                new BufferChangedListener(bufferTabs, MODIFIED, saveMenuItem,
                        saveIcon));
        text.requestFocus();
    }

    private String getNextEmptyTabName() {
        return UNTITLED + " " + nextEmptyTabNumber++;
    }

    public RuneTextArea getSelectedBuffer() {
        JScrollPane scroll = (JScrollPane) bufferTabs.getSelectedComponent();
        JViewport view = (JViewport) scroll.getComponent(0);
        return (RuneTextArea) view.getComponent(0);
    }

    protected RuneTextArea getBufferAt(int index) {
        JScrollPane scroll = (JScrollPane) bufferTabs.getComponentAt(index);
        JViewport view = (JViewport) scroll.getComponent(0);
        return (RuneTextArea) view.getComponent(0);
    }

    public String getSelectedTabToolTip() {
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
            } else {
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
        } else if (System.getProperty("os.name").startsWith("Mac")) {
            f = newFont("Monaco", Font.PLAIN, 12);
        }
        if (f == null) {
            f = new Font("Monospaced", Font.PLAIN, 12);
        }
        return f;
    }

    public Font newFont(String family, int face, int size) {
        Font f = new Font(family, face, size);
        if (!f.getFamily().equals(family)) {
            f = null;
        }
        return f;
    }

    public Font findExistingFont(String[] fontFamiliesToSearch) {
        Font f = null;
        boolean foundFont = false;
        for (String familiesToSearch : fontFamiliesToSearch) {
            f = new Font(familiesToSearch, Font.PLAIN, 12);
            if (f.getFamily().equals(familiesToSearch)) {
                // The font we asked for actually exists.
                foundFont = true;
                break;
            }
        }
        if (!foundFont) {
            f = null;
        }
        return f;
    }

    public void keyTyped(KeyEvent e) {
        // System.out.println("keyTyped: " + e);
        // if (e.getKeyChar() == '+' && e.getModifiers() > 0) {
        // JTextArea buffer = getSelectedBuffer();
        // Font currentFont = buffer.getFont();
        // int upSize = currentFont.getSize() + 1;
        // Font upFont = currentFont.deriveFont(upSize);
        // buffer.setFont(upFont);
        // }
        // else if (e.getKeyChar() == '-' && e.getModifiers() > 0) {
        // JTextArea buffer = getSelectedBuffer();
        // Font currentFont = buffer.getFont();
        // int downSize = currentFont.getSize() + 1;
        // Font downFont = currentFont.deriveFont(downSize);
        // buffer.setFont(downFont);
        // }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {

    }

    public void caretUpdate(CaretEvent e) {
        RuneTextArea textArea = (RuneTextArea) e.getSource();
        statusBar.setCursorPosition(textArea.getLineAtCaret(),
                textArea.getColumnAtCaret());
    }
}
