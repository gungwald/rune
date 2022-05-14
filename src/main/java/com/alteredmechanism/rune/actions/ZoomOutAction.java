package com.alteredmechanism.rune.actions;

import com.alteredmechanism.rune.Rune;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class ZoomOutAction extends ZoomAction implements Action {

    private static final String CLASS_NAME = ZoomOutAction.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    public ZoomOutAction(Rune app) {
        super(app);
        logger.warning("ZoomOutAction constructor");
        this.putValue(NAME, "Zoom Out");
        this.putValue(SHORT_DESCRIPTION, "Make the font smaller");
        this.putValue(LONG_DESCRIPTION, "Make the font smaller");
        this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_O)); // Conversion to Integer required for retrotranslator
        try {
            this.putValue(SMALL_ICON, app.getLoader().getZoomOutIcon());
        } catch (FileNotFoundException e) {
            app.getMessenger().showError(e);
        }
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e The triggering event
     */
    public void actionPerformed(ActionEvent e) {
        logger.info("ZoomOutAction.actionPerformed");
        zoom(-1);
    }
}
