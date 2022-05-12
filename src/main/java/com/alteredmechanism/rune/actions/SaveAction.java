package com.alteredmechanism.rune.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import com.alteredmechanism.rune.Rune;

import static com.alteredmechanism.rune.Rune.UNTITLED;

public class SaveAction extends AbstractAction implements Action {
    Rune rune;
    public SaveAction(Rune app) {
        this.rune = app;
        this.putValue(NAME, "Save");
        this.putValue(SHORT_DESCRIPTION, "Save the current file");
        this.putValue(LONG_DESCRIPTION, "Save the current file");
        this.putValue(ACCELERATOR_KEY, "Control-S");
        this.putValue(MNEMONIC_KEY, "S");
        try {
            this.putValue(SMALL_ICON, rune.loader.getSaveIcon());
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
        save();
    }
    protected void save() {
        String absFileName = rune.getSelectedTabToolTip();
        if (absFileName == null || absFileName.trim().length() == 0 || absFileName.startsWith(UNTITLED)) {
            rune.saveAs();
        } else {
            save(new java.io.File(absFileName));
        }
    }

    protected void save(java.io.File f) {
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
