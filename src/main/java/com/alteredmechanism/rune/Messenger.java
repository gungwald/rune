package com.alteredmechanism.rune;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.*;

public class Messenger {

    private Component parent;
    private String applicationName;

    /**
     * It needs to be volatile to prevent cache incoherence issues.
     * I don't know what that means, but
     * <a href="https://www.baeldung.com/java-singleton-double-checked-locking">Baeldung</a>
     * says so.
     */
    private static volatile Messenger instance;

    /**
     * Double-checked locking implementation of a singleton
     * @return That bitch
     */
    public static Messenger getInstance() {
        if (instance == null) {
            synchronized (Messenger.class) {
                if (instance == null) {
                    instance = new Messenger();
                }
            }
        }
        return instance;
    }

    private Messenger() {
        super();
        applicationName = Rune.USER_FACING_APP_NAME;
        parent = new Frame();
    }

    public Messenger setParent(Frame parent) {
        this.parent = parent;
        return this;
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
        if (message == null || message.length() <= 1) {
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
