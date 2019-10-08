package com.alteredmechanism.notepad;

import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BufferChangedListener implements DocumentListener {
    
    private JTabbedPane tabPane;
    private int tabIndex;
    private String textToAppendOnModify;
    private JMenuItem saveItem;

    public BufferChangedListener(JTabbedPane tabPane, int tabIndex, String textToAppendOnModify, JMenuItem saveItem) {
        this.tabPane = tabPane;
        this.tabIndex = tabIndex;
        this.textToAppendOnModify = textToAppendOnModify;
        this.saveItem = saveItem;
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
        String tabTitle = tabPane.getTitleAt(tabIndex);
        if (!tabTitle.endsWith(textToAppendOnModify)) {
            tabPane.setTitleAt(tabIndex, tabTitle + textToAppendOnModify);
        }
        saveItem.setEnabled(true);
    }

}
