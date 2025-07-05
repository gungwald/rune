package com.alteredmechanism.rune.actions;

import com.alteredmechanism.rune.Rune;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class ZoomInAction extends ZoomAction implements Action {

    private static final String CLASS_NAME = ZoomInAction.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    public ZoomInAction(Rune app) {
        super(app);
        logger.warning("ZoomInAction constructor");
        this.putValue(NAME, "Zoom In");
        this.putValue(SHORT_DESCRIPTION, "Make the font bigger");
        this.putValue(LONG_DESCRIPTION, "Make the font bigger");
        this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_UP, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_I)); // Conversion to Integer required for retrotranslator
        try {
            this.putValue(SMALL_ICON, app.getLoader().getZoomInIcon());
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
        logger.info("ZoomInAction.actionPerformed");
        zoom(1);
    }
}
