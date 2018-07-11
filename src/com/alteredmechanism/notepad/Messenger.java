package com.alteredmechanism.notepad;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Messenger {

	private Component parent;
	
	public Messenger() {
		parent = new JFrame();
	}
	public Messenger(Frame frame) {
		this.parent = frame;
	}

    protected void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(parent, message, "Notepad Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showError(String message) {
    	showErrorDialog(message);
    	System.err.println(message);
    }
    
    public void showError(Exception e) {
    	showErrorDialog(e.getLocalizedMessage());
        e.printStackTrace();    	
    }
    
}
