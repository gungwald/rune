package com.alteredmechanism.rune;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BufferChangedListener implements DocumentListener {
    
    private JTabbedPane tabPane;
    private String textToAppendOnModify;
    private JMenuItem saveItem;
    private Icon saveIcon;

    public BufferChangedListener(JTabbedPane tabPane, String textToAppendOnModify, JMenuItem saveItem, Icon saveIcon) {
        this.tabPane = tabPane;
        this.textToAppendOnModify = textToAppendOnModify;
        this.saveItem = saveItem;
        this.saveIcon = saveIcon;
    }

    public void insertUpdate(DocumentEvent e) {
        showModified();
    }

    public void removeUpdate(DocumentEvent e) {
        showModified();
    }

    public void changedUpdate(DocumentEvent e) {
        showModified();
    }

    protected void showModified() {
        int tabIndex = tabPane.getSelectedIndex();
        String title = tabPane.getTitleAt(tabIndex);
        if (title != null) {
            // If the title hasn't been set then this is the event for
            // assigning the initial contents of a file.
            tabPane.setIconAt(tabIndex, saveIcon);
            saveItem.setEnabled(true);
        }
    }
}
