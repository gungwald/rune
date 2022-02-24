package com.alteredmechanism.rune.awt;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import com.alteredmechanism.java.awt.TabPanel;

public class Notepad implements WindowListener, ActionListener{

    public static final String USER_FACING_APP_NAME = "Gorgious";
    private Frame frame;
    private MenuBar menubar;
    private Menu fileMenu;
    private MenuItem openItem;
    private TextArea editor = new TextArea();
    private TabPanel tabPanel = new TabPanel();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Notepad window = new Notepad();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Notepad() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new Frame("Notepad");
        frame.setBounds(100, 100, 450, 300);
        frame.addWindowListener(this);
        
        menubar = new MenuBar();
        frame.setMenuBar(menubar);
        
        fileMenu = new Menu("File");
        openItem = new MenuItem("Open");
        
        menubar.add(fileMenu);
        fileMenu.add(openItem);
        
        openItem.addActionListener(this);
        
        Map<TextAttribute,Object> fontAttributes = new HashMap<TextAttribute,Object>();
        fontAttributes.put(TextAttribute.FAMILY, "Monospaced");
        fontAttributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        fontAttributes.put(TextAttribute.SIZE, new Float(16));
        Font editorFont = new Font(fontAttributes);
        System.out.println("editorFont:" + editorFont);
        editor.setFont(editorFont);
        System.out.println("editorFont:" + editor.getFont().getFamily());
        
        frame.add(tabPanel, BorderLayout.CENTER);
        
        editor.setText("Blah");
        
        Panel editorPanel = new Panel();
        editorPanel.add("Editor1", editor);
        
        TextArea editor2 = new TextArea();
        Panel editorPanel2 = new Panel();
        editorPanel2.add("Editor2", editor2);
        
        editor2.setText("Fred");
        
        tabPanel.addPanel(editorPanel, "Unknown1");
        tabPanel.addPanel(editorPanel2, "Unknown2");
       
    }

    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub
    }

    /**
     * This event is sent when the user clicks the X in the window title.
     */
    public void windowClosing(WindowEvent e) {
        System.out.println("windowClosing: " + e.toString());
        frame.dispose();
    }

    public void windowClosed(WindowEvent e) {
        System.out.println("windowClosed: " + e.toString());
    }

    public void windowIconified(WindowEvent e) {
        System.out.println("windowIconified: " + e.toString());
        // TODO Auto-generated method stub
        
    }

    public void windowDeiconified(WindowEvent e) {
        System.out.println("windowDeiconified: " + e.toString());
        // TODO Auto-generated method stub
        
    }

    public void windowActivated(WindowEvent e) {
        System.out.println("windowActivated: " + e.toString());
        // TODO Auto-generated method stub
        
    }

    public void windowDeactivated(WindowEvent e) {
        System.out.println("windowDeactivated: " + e.toString());
        // TODO Auto-generated method stub
        
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("actionPerformed: " + e.toString());
        // TODO Auto-generated method stub
        
    }

}
