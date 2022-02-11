package com.alteredmechanism.notepad;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class StatusBar extends JPanel {
    protected Rune creator;
    protected JLabel cursorPosition;

    public StatusBar(Rune creator) {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setPreferredSize(new Dimension(creator.getWidth(), 16));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        cursorPosition = new JLabel("1:1");
        cursorPosition.setHorizontalAlignment(SwingConstants.RIGHT);
        add(cursorPosition);
    }

    public void setCursorPosition(int row, int column) {
        cursorPosition.setText(row + ":" + column);
    }
}
