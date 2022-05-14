package com.alteredmechanism.rune.actions;

import com.alteredmechanism.rune.Rune;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import static com.alteredmechanism.rune.Rune.UNTITLED;

public class SaveAction extends AbstractAction implements Action {

    private static final String CLASS_NAME = SaveAction.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    private final Rune rune;

    public SaveAction(Rune app) {
        logger.warning("SaveAction constructor");
        this.rune = app;
        this.putValue(NAME, "Save");
        this.putValue(SHORT_DESCRIPTION, "Save the current file");
        this.putValue(LONG_DESCRIPTION, "Save the current file");
        this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S)); // Conversion to Integer required for retrotranslator
        try {
            this.putValue(SMALL_ICON, rune.getLoader().getSaveIcon());
        } catch (FileNotFoundException e) {
            rune.getMessenger().showError(e);
        }
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e The triggering event
     */
    public void actionPerformed(ActionEvent e) {
        logger.info("SaveAction.actionPerformed");
        save();
    }

    public void save() {
        logger.info("SaveAction.save");
        String absFileName = rune.getSelectedTabToolTip();
        if (absFileName == null || absFileName.trim().length() == 0 || absFileName.startsWith(UNTITLED)) {
            rune.saveAs();
        } else {
            save(new java.io.File(absFileName));
        }
    }

    public void save(java.io.File f) {
        logger.info("SaveAction.save(f)");
        java.io.BufferedWriter out = null;
        try {
            out = new java.io.BufferedWriter(new java.io.FileWriter(f));
            out.write(rune.getSelectedBuffer().getText());
            int selectedIndex = rune.bufferTabs.getSelectedIndex();
            rune.bufferTabs.setIconAt(selectedIndex, null);
            rune.saveMenuItem.setEnabled(false);
        } catch (Exception ex) {
            rune.getMessenger().showError(ex);
        } finally {
            rune.close(out);
        }
    }
}
