package com.alteredmechanism.notepad;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Messenger {

    private Component parent;
    private String applicationName;

    public Messenger(Frame parent) {
        this.parent = parent;
        String className = parent.getClass().getName();
        int startOfBaseName = 0;
        int lastPeriod = className.lastIndexOf('.');
        if (lastPeriod >= 0) {
            startOfBaseName = lastPeriod + 1;
        }
        applicationName = className.substring(startOfBaseName);
    }

    public Messenger(String applicationName) {
        parent = new JFrame();
        this.applicationName = applicationName;
    }

    public Messenger(Frame frame, String applicationName) {
        this.parent = frame;
        this.applicationName = applicationName;
    }

    public Messenger(Class applicationClass) {
        String className = applicationClass.getName();
        int startOfBaseName = 0;
        int lastPeriod = className.lastIndexOf('.');
        if (lastPeriod >= 0) {
            startOfBaseName = lastPeriod + 1;
        }
        applicationName = className.substring(startOfBaseName);
    }

    protected void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(parent, message, applicationName + " Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(parent, message, applicationName + " Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        showErrorDialog(message);
        System.err.println(message);
    }

    public void showInfo(String message) {
        showInfoDialog(message);
        System.out.println(message);
    }

    public void showError(Exception e) {
        String message = e.getLocalizedMessage();
        if (message == null || message.length() == 0) {
            message = e.toString();
        }
        showErrorDialog(message);
        e.printStackTrace();
    }

    public void showError(String message, Exception e) {
        String exceptionMessage = e.getLocalizedMessage();
        if (exceptionMessage == null || exceptionMessage.length() == 0) {
            exceptionMessage = e.toString();
        }
        showErrorDialog(message + ": " + exceptionMessage);
        e.printStackTrace();
    }

}
