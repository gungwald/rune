package com.alteredmechanism.notepad.awt;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Notepad implements WindowListener, ActionListener{

    public static final String USER_FACING_APP_NAME = "Gorgious";
    private Frame frame;
    private MenuBar menubar;
    private Menu fileMenu;
    private MenuItem openItem;

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
