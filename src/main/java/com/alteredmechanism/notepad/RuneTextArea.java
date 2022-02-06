package com.alteredmechanism.notepad;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;

public class RuneTextArea extends AntiAliasedJTextArea {

    private static final long serialVersionUID = 1L;

    private JScrollPane scroller;
    private Rune creator;
    private UndoManager undoManager;
    private LineNumberingTextArea lineNumberingTextArea;

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
        this.addCaretListener(creator);

        // Set up line numbers. Yea!
        lineNumberingTextArea = new LineNumberingTextArea(this);
        scroller.setRowHeaderView(lineNumberingTextArea);
        this.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(DocumentEvent documentEvent)
            {
                lineNumberingTextArea.updateLineNumbers();
            }

            public void removeUpdate(DocumentEvent documentEvent)
            {
                lineNumberingTextArea.updateLineNumbers();
            }

            public void changedUpdate(DocumentEvent documentEvent)
            {
                lineNumberingTextArea.updateLineNumbers();
            }
        });
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

    /**
     * Attempt to center the line containing the caret at the center of the
     * scroll pane.
     */
    public void centerLineInScrollPane() {
        Container container = SwingUtilities.getAncestorOfClass(JViewport.class, this);

        if (container == null) return;

        try {
            Rectangle r = this.modelToView(this.getCaretPosition());
            JViewport viewport = (JViewport) container;
            int extentHeight = viewport.getExtentSize().height;
            int viewHeight = viewport.getViewSize().height;

            int y = Math.max(0, r.y - ((extentHeight - r.height) / 2));
            y = Math.min(y, viewHeight - extentHeight);

            viewport.setViewPosition(new Point(0, y));
        } catch (BadLocationException e) {
            creator.getMessenger().showError(e);
        }
    }

    /**
     *  Return the column number at the Caret position.
     *
     *  The column returned will only make sense when using a
     *  Monospaced font.
     */
    public int getColumnAtCaret() {
        //  Since we assume a monospaced font we can use the width of a single
        //  character to represent the width of each character

        FontMetrics fm = this.getFontMetrics(this.getFont());
        int characterWidth = fm.stringWidth("0");
        int column = 0;

        try {
            Rectangle r = this.modelToView(this.getCaretPosition());
            int width = r.x - this.getInsets().left;
            column = width / characterWidth;
        } catch (BadLocationException e) {
            creator.getMessenger().showError(e);
        }
        return column + 1;
    }

    /**
     *  Return the line number at the Caret position.
     */
    public int getLineAtCaret() {
        int caretPosition = this.getCaretPosition();
        Element root = this.getDocument().getDefaultRootElement();

        return root.getElementIndex(caretPosition) + 1;
    }

    /**
     *  Return the number of lines of text in the Document
     */
    public int getLines() {
        Element root = this.getDocument().getDefaultRootElement();
        return root.getElementCount();
    }

    /**
     *  Position the caret at the start of a line.
     */
    public void gotoStartOfLine(RuneTextArea runeTextArea, int line) {
        Element root = this.getDocument().getDefaultRootElement();
        line = Math.max(line, 1);
        line = Math.min(line, root.getElementCount());
        int startOfLineOffset = root.getElement(line - 1).getStartOffset();
        this.setCaretPosition(startOfLineOffset);
    }

    /**
     *  Position the caret on the first word of a line.
     */
    public void gotoFirstWordOnLine(int line) {
        gotoStartOfLine(this, line);

        //  The following will position the caret at the start of the first word

        try {
            int position = this.getCaretPosition();
            String first = this.getDocument().getText(position, 1);

            if (Character.isWhitespace(first.charAt(0))) {
                this.setCaretPosition(Utilities.getNextWord(this, position));
            }
        } catch (Exception e) {
            creator.getMessenger().showError(e);
        }
    }

    /**
     *  Return the number of lines of text, including wrapped lines.
     */
    public int getWrappedLines() {
        View view = this.getUI().getRootView(this).getView(0);
        int preferredHeight = (int) view.getPreferredSpan(View.Y_AXIS);
        int lineHeight = this.getFontMetrics(this.getFont()).getHeight();
        return preferredHeight / lineHeight;
    }

    /**
     *  Return the number of lines of text, including wrapped lines.
     */
//	public int getWrappedLines(/* JTextComponent */)
//	{
//		int lines = 0;
//		View view = this.getUI().getRootView(this).getView(0);
//		int paragraphs = view.getViewCount();
//
//		for (int i = 0; i < paragraphs; i++)
//		{
//			lines += view.getView(i).getViewCount();
//		}
//		return lines;
//	}

}
