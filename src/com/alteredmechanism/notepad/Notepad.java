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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
public class Notepad extends JFrame implements ActionListener, MouseListener, ChangeListener, KeyListener {

    private static final long serialVersionUID = 1L;

    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_FAILURE = 1;

    public static final String UNTITLED = "Untitled";
    public static final String USER_FACING_APP_NAME = "Hreodrit";

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
    private final JTabbedPane bufferTabs = new JTabbedPane(JTabbedPane.TOP);

    public Notepad(File f) throws FontFormatException, IOException {
        this();
        open(f);
    }

    public Notepad() throws FontFormatException, IOException {
        super(USER_FACING_APP_NAME);
        this.setSize(600, 400);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.getContentPane().setLayout(new BorderLayout());

        getContentPane().add(bufferTabs, BorderLayout.CENTER);

        // Zoom in
        Action zoomIn = new AbstractAction() {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                System.out.println("zoomIn");
                JTextArea buffer = getSelectedBuffer();
                Font currentFont = buffer.getFont();
                int upSize = currentFont.getSize() + 1;
                Font upFont = currentFont.deriveFont(upSize);
                buffer.setFont(upFont);
            }
        };
        KeyStroke plus = KeyStroke.getKeyStroke('+', InputEvent.CTRL_MASK);
        bufferTabs.getInputMap(JComponent.WHEN_FOCUSED).put(plus, "zoomIn");;
        bufferTabs.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(plus, "zoomIn");;
        bufferTabs.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(plus, "zoomIn");;
        bufferTabs.getActionMap().put("zoomIn", zoomIn);

        // Zoom out
        Action zoomOut = new AbstractAction() {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                System.out.println("zoomOut");
                JTextArea buffer = getSelectedBuffer();
                Font currentFont = buffer.getFont();
                int downSize = currentFont.getSize() + 1;
                Font downFont = currentFont.deriveFont(downSize);
                buffer.setFont(downFont);
            }
        };
        KeyStroke minus = KeyStroke.getKeyStroke('+', InputEvent.CTRL_MASK);
        bufferTabs.getInputMap(JComponent.WHEN_FOCUSED).put(minus, "zoomOut");;
        bufferTabs.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(minus, "zoomOut");;
        bufferTabs.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(minus, "zoomOut");;
        bufferTabs.getActionMap().put("zoomOut", zoomOut);

        appendNewTab();

        this.setJMenuBar(this.menuBar);

        ImageIconLoader loader = new ImageIconLoader(getMessenger());
        List icons = loader.loadAll("writbred");
        if (icons.size() > 0) {
            this.setIconImage((Image) icons.get(0));
        }

        file.setMnemonic(KeyEvent.VK_F);
        editMenu.setMnemonic(KeyEvent.VK_E);
        viewMenu.setMnemonic(KeyEvent.VK_V);
        helpMenu.setMnemonic(KeyEvent.VK_H);

        newTabItem.addActionListener(this);
        newTabItem.setMnemonic(KeyEvent.VK_T);
        newTabItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK));
        file.add(newTabItem);

        openMenuItem.addActionListener(this);
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        file.add(openMenuItem);

        saveMenuItem.addActionListener(this);
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        file.add(saveMenuItem);

        saveAsMenuItem.addActionListener(this);
        saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        file.add(saveAsMenuItem);

        close.addActionListener(this);
        close.setMnemonic(KeyEvent.VK_W);
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        file.add(close);

        // Cut menu item
        cutMenuItem.setMnemonic(KeyEvent.VK_T);
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        cutMenuItem.addActionListener(this);
        editMenu.add(cutMenuItem);

        // Copy menu item
        copyMenuItem.setMnemonic(KeyEvent.VK_C);
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        copyMenuItem.addActionListener(this);
        editMenu.add(copyMenuItem);

        // Paste menu item
        pasteMenuItem.setMnemonic(KeyEvent.VK_P);
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        pasteMenuItem.addActionListener(this);
        editMenu.add(pasteMenuItem);

        // Select Font menu item
        selectFontMenuItem.addActionListener(this);
        selectLookAndFeelMenu.addMouseListener(this);

        viewMenu.add(selectLookAndFeelMenu);
        viewMenu.add(selectFontMenuItem);

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
    }

    public void open(File f) {
        if (!bufferTabs.getTitleAt(bufferTabs.getSelectedIndex()).startsWith(UNTITLED) || getSelectedBuffer().getText().length() > 0) {
            appendNewTab();
        }
        openIntoSelectedTab(f);
    }

    public void openIntoSelectedTab(File f) {
        getSelectedBuffer().setText("");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = reader.readLine()) != null) {
                getSelectedBuffer().append(line + "\n");
            }
            getSelectedBuffer().setCaretPosition(0);
            this.setTitle(f.getName() + " - Notepad");
            setSelectedTabTitle(f.getName());
            setSelectedTabToolTip(f.getAbsolutePath());
        }
        catch (Exception ex) {
            getMessenger().showError(ex);
        }
        finally {
            close(reader);
        }
    }

    private void setSelectedTabToolTip(String text) {
        bufferTabs.setToolTipTextAt(bufferTabs.getSelectedIndex(), text);
    }

    private void setSelectedTabTitle(String name) {
        bufferTabs.setTitleAt(bufferTabs.getSelectedIndex(), name);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.close) {
            this.dispose();
        }
        else if (e.getSource() == this.newTabItem) {
            appendNewTab();
        }
        else if (e.getSource() == this.openMenuItem) {
            getFileChooser().setDialogTitle("Choose a file to open");
            if (getFileChooser().showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = getFileChooser().getSelectedFile();
                if (selectedFile != null) {
                    open(selectedFile);
                }
            }
        }
        else if (e.getSource() == this.saveMenuItem) {
            String absFileName = getSelectedTabToolTip();
            if (absFileName == null || absFileName.trim().length() == 0) {
                saveAs();
            }
            else {
                save(new File(absFileName));
            }
        }
        else if (e.getSource() == this.saveAsMenuItem) {
            saveAs();
        }
        else if (e.getSource() == this.selectFontMenuItem) {
            try {
                getFontChooser().setSelectedFont(getSelectedBuffer().getFont());
                int result = getFontChooser().showDialog(this);
                if (result == JFontChooser.OK_OPTION) {
                    Font font = getFontChooser().getSelectedFont();
                    System.out.println("Selected Font : " + font);
                    getSelectedBuffer().setFont(font);
                    getSelectedBuffer().setTabSize(8);
                }
            }
            catch (Exception ex) {
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
            File fileToSave = getFileChooser().getSelectedFile();
            save(fileToSave);
            this.setTitle(fileToSave.getName());
            setSelectedTabTitle(fileToSave.getName());
            setSelectedTabToolTip(fileToSave.getAbsolutePath());
        }
    }

    protected void save(File f) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(f));
            out.write(getSelectedBuffer().getText());
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
            Notepad n = new Notepad();
            for (int i = 0; i < args.length; i++) {
                n.open(new File(args[i]);
            }
        }
        catch (Exception e) {
            new Messenger(Notepad.class).showError(e);
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

    public void stateChanged(ChangeEvent e) {}

    private void appendNewTab() {
        System.out.println("appendNewTab called");
        AntiAliasedJTextArea text = new AntiAliasedJTextArea();
        text.setFont(getSelectedFont());
        text.setTabSize(8);
        text.setBorder(new EmptyBorder(new Insets(3, 3, 3, 3)));
        text.addKeyListener(this);
        JScrollPane scroller = new JScrollPane(text, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        String tabTitle = getNextEmptyTabName();
        bufferTabs.addTab(tabTitle, null, scroller, tabTitle);
        bufferTabs.setSelectedIndex(bufferTabs.getTabCount()-1);
    }

    private String getNextEmptyTabName() {
        return UNTITLED + " " + nextEmptyTabNumber++;
    }

    private JTextArea getSelectedBuffer() {
        JScrollPane scroll = (JScrollPane) bufferTabs.getSelectedComponent();
        JViewport view = (JViewport) scroll.getComponent(0);
        JTextArea text = (JTextArea) view.getComponent(0);
        return text;
    }

    private String getSelectedTabToolTip() {
        return bufferTabs.getToolTipTextAt(bufferTabs.getSelectedIndex());
    }

    private Font getSelectedFont() {
        // Don't build the fontChooser just for this because it would slow
        // down the startup time.
        Font f;
        if (fontChooser == null) {
            f = getSystemDefaultFont();
        }
        else {
            f = fontChooser.getSelectedFont();
        }
        return f;
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
            f = new Font("Monospace", Font.PLAIN, 12);
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
