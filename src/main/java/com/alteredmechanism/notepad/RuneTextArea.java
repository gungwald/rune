package com.alteredmechanism.notepad;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
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
        this.addCaretListener(creator);
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
    
    public int getLineNumber() {
    	return getDocument().getRootElements().length + 1;
    }

	/**
	 *  Attempt to center the line containing the caret at the center of the
	 *  scroll pane.
	 *
	 *  @param component the text component in the sroll pane
	 */
	public void centerLineInScrollPane()
	{
		Container container = SwingUtilities.getAncestorOfClass(JViewport.class, this);

		if (container == null) return;

		try
		{
			Rectangle r = this.modelToView(this.getCaretPosition());
			JViewport viewport = (JViewport)container;
			int extentHeight = viewport.getExtentSize().height;
			int viewHeight = viewport.getViewSize().height;

			int y = Math.max(0, r.y - ((extentHeight - r.height) / 2));
			y = Math.min(y, viewHeight - extentHeight);

			viewport.setViewPosition(new Point(0, y));
		}
		catch(BadLocationException e) {
			creator.getMessenger().showError(e);
		}
	}

	/*
	 *  Return the column number at the Caret position.
	 *
	 *  The column returned will only make sense when using a
	 *  Monospaced font.
	 */
	public static int getColumnAtCaret(JTextComponent component)
	{
		//  Since we assume a monospaced font we can use the width of a single
		//  character to represent the width of each character

		FontMetrics fm = component.getFontMetrics( component.getFont() );
		int characterWidth = fm.stringWidth( "0" );
		int column = 0;

		try
		{
			Rectangle r = component.modelToView( component.getCaretPosition() );
			int width = r.x - component.getInsets().left;
			column = width / characterWidth;
		}
		catch(BadLocationException ble) {}

		return column + 1;
	}

	/*
	 *  Return the line number at the Caret position.
	 */
	public static int getLineAtCaret(JTextComponent component)
	{
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();

		return root.getElementIndex( caretPosition ) + 1;
	}

	/*
	 *  Return the number of lines of text in the Document
	 */
	public static int getLines(JTextComponent component)
	{
		Element root = component.getDocument().getDefaultRootElement();
		return root.getElementCount();
	}

	/*
	 *  Position the caret at the start of a line.
	 */
	public static void gotoStartOfLine(JTextComponent component, int line)
	{
		Element root = component.getDocument().getDefaultRootElement();
		line = Math.max(line, 1);
		line = Math.min(line, root.getElementCount());
		int startOfLineOffset = root.getElement( line - 1 ).getStartOffset();
		component.setCaretPosition( startOfLineOffset );
	}

	/*
	 *  Position the caret on the first word of a line.
	 */
	public static void gotoFirstWordOnLine(final JTextComponent component, int line)
	{
		gotoStartOfLine(component, line);

		//  The following will position the caret at the start of the first word

		try
		{
			int position = component.getCaretPosition();
			String first = component.getDocument().getText(position, 1);

			if (Character.isWhitespace(first.charAt(0)))
			{
				component.setCaretPosition(Utilities.getNextWord(component, position));
			}
		}
		catch(Exception e) {}
	}

	/*
	 *  Return the number of lines of text, including wrapped lines.
	 */
	public static int getWrappedLines(JTextArea component)
	{
		View view = component.getUI().getRootView(component).getView(0);
		int preferredHeight = (int)view.getPreferredSpan(View.Y_AXIS);
		int lineHeight = component.getFontMetrics( component.getFont() ).getHeight();
		return preferredHeight / lineHeight;
	}

	/*
	 *  Return the number of lines of text, including wrapped lines.
	 */
	public static int getWrappedLines(JTextComponent component)
	{
		int lines = 0;

		View view = component.getUI().getRootView(component).getView(0);

		int paragraphs = view.getViewCount();

		for (int i = 0; i < paragraphs; i++)
		{
			lines += view.getView(i).getViewCount();
		}

		return lines;
	}

}
