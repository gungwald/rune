package com.alteredmechanism.notepad;

import javax.swing.*;
import javax.swing.text.Element;
import java.awt.*;

/**
 * I didn't write this code. It was written by
 * <a href="https://tips4java.wordpress.com/2009/05/23/text-component-line-number">Rob Camick</a>
 * and <a href="https://stackoverflow.com/a/35583276">BullyWiiPlaza</a>.
 */
public class LineNumberingTextArea extends JTextArea
{
    protected final static String LINE_SEPARATOR = System.getProperty("line.separator");
    private JTextArea textArea;

    public LineNumberingTextArea(JTextArea textArea)
    {
        this(textArea, Color.LIGHT_GRAY);
    }
    public LineNumberingTextArea(JTextArea textArea, Color background)
    {
        this.textArea = textArea;
        setBackground(background);
        setEditable(false);
        // This unfortunately causes the vertical spacing of the lines in the line numbering
        // text area to be shorter than that of the main text area.
//        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    public void updateLineNumbers()
    {
        String lineNumbersText = getLineNumbersText();
        setText(lineNumbersText);
    }

    private String getLineNumbersText()
    {
        int caretPosition = textArea.getDocument().getLength();
        Element root = textArea.getDocument().getDefaultRootElement();
        StringBuilder lineNumbersTextBuilder = new StringBuilder();
        lineNumbersTextBuilder.append("1").append(LINE_SEPARATOR);

        for (int elementIndex = 2; elementIndex < root.getElementIndex(caretPosition) + 2; elementIndex++)
        {
            lineNumbersTextBuilder.append(elementIndex).append(LINE_SEPARATOR);
        }

        return lineNumbersTextBuilder.toString();
    }
}
