package com.alteredmechanism.notepad;

import java.awt.ComponentOrientation;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class StatusBar extends JPanel {
    private static final long serialVersionUID = 1L;
    protected Rune creator;
    protected JLabel cursorPosition;

    public StatusBar(Rune creator) {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        //setPreferredSize(new Dimension(creator.getWidth(), 16));
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        cursorPosition = new JLabel("1:1", SwingConstants.RIGHT);
        Border positionBorder = new EmptyBorder(new Insets(3, 3, 3, 3));
        cursorPosition.setBorder(positionBorder);
        add(cursorPosition);
    }

    public void setCursorPosition(int row, int column) {
        cursorPosition.setText(row + ":" + column);
    }
}
