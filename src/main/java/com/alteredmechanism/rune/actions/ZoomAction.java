package com.alteredmechanism.rune.actions;

import com.alteredmechanism.rune.Rune;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public abstract class ZoomAction extends AbstractAction {

    private static final String CLASS_NAME = ZoomAction.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    Rune rune;

    public ZoomAction(Rune rune) {
        this.rune = rune;
    }

    public void zoom(int magnitude) {
        logger.info("zoom " + magnitude);
        // RuneTextArea buffer = getSelectedBuffer();
        Font currentFont = rune.getBufferFont();
        int adjustedSize = currentFont.getSize() + magnitude;
        Font adjustedFont = currentFont.deriveFont((float) adjustedSize);
        rune.setBufferFont(adjustedFont);
        int width = (int) (rune.getWidth() * Math.abs(0.05 + magnitude));
        int height = (int) (rune.getHeight() * Math.abs(0.05 + magnitude));
        logger.info(width + "," + height);
        rune.setSize(width, height);
        rune.repaint();
    }

}
