package com.alteredmechanism.notepad;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import java.awt.*;

public class RuneTextArea extends AntiAliasedJTextArea {

    private static final long serialVersionUID = 1L;
    
    private JScrollPane scroller;
    private Rune creator;
    private UndoManager undoManager;

    public RuneTextArea(Rune creator) {
        init(creator);
    }

    public RuneTextArea(Rune creator, String text) {
        super(text);
        init(creator);
    }

    public RuneTextArea(Rune creator, Document doc) {
        super(doc);
        init(creator);
    }

    public RuneTextArea(Rune creator, int rows, int columns) {
        super(rows, columns);
        init(creator);
    }

    public RuneTextArea(Rune creator, String text, int rows, int columns) {
        super(text, rows, columns);
        init(creator);
    }

    public RuneTextArea(Rune creator, Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
        init(creator);
    }

    private void init(Rune creator) {
        this.creator = creator;
        setFont(this.creator.getBufferFont());
        setTabSize(8);
        setBorder(new EmptyBorder(new Insets(3, 3, 3, 3)));
        Color notepadPlusPlusBackground = new Color(242, 244, 255);
        this.setBackground(notepadPlusPlusBackground);
        scroller = new JScrollPane(this, 
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        undoManager = new UndoManager();
        getDocument().addUndoableEditListener(undoManager);
    }
    
    public JScrollPane getScrollPane() {
        return scroller;
    }

    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        } else {
            creator.getMessenger().showInfo("Nothing to undo.");
        }
    }
    
    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        } else {
            creator.getMessenger().showInfo("Nothing to redo.");
        }
    }
}
