package com.alteredmechanism.notepad;

import javax.swing.*;
import javax.swing.text.Element;
import java.awt.*;

public class LineNumberingTextArea extends JTextArea {

	private static final long serialVersionUID = 1L;
	protected final static String LINE_SEPARATOR = System.getProperty("line.separator");
    private JTextArea textArea;

    public LineNumberingTextArea(JTextArea textArea) {
        this(textArea, Color.LIGHT_GRAY);
    }

    public LineNumberingTextArea(JTextArea textArea, Color background) {
        this.textArea = textArea;
        setBackground(background);
        setEditable(false);
        // This unfortunately causes the vertical spacing of the lines in the line numbering
        // text area to be shorter than that of the main text area.
//        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    public void updateLineNumbers() {
        String lineNumbersText = getLineNumbersText();
        setText(lineNumbersText);
    }

    public String rightJustify(int n, int maxDigits) {
        StringBuilder justified = new StringBuilder();
        String digits = String.valueOf(n);
        int spaceCount = maxDigits - digits.length();
        for (int i = 0; i < spaceCount; i++) {
            justified.append(' ');
        }
        justified.append(digits);
        return justified.toString();
    }

    private String getLineNumbersText() {
        final int windowWidth = textArea.getColumns();
        int characterCount = textArea.getDocument().getLength();
        Element root = textArea.getDocument().getDefaultRootElement();
        StringBuilder lineNumbers = new StringBuilder();
        int lineCount = root.getElementIndex((characterCount)) + 2;
        int maxDigits = String.valueOf(lineCount).length();
        lineNumbers.append(rightJustify(1, maxDigits)).append(LINE_SEPARATOR);

        int lineLength = root.getElement(0).toString().length();

        for (int lineNumber = 2; lineNumber < lineCount; lineNumber++) {
            lineNumbers.append(rightJustify(lineNumber, maxDigits)).append(LINE_SEPARATOR);
	    if (windowWidth > 0) {
	            int rowsConsumedByWrappedLine = lineLength / windowWidth;
        	    for (int i = 0; i < rowsConsumedByWrappedLine; i++) {
                	lineNumbers.append(LINE_SEPARATOR);
	            }
	    }
        }
        return lineNumbers.toString();
    }
}
