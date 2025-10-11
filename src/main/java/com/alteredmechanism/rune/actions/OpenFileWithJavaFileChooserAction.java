package com.alteredmechanism.rune.actions;

import com.alteredmechanism.rune.Rune;
import org.apache.tools.ant.input.InputHandler;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.logging.Logger;

public class OpenFileWithJavaFileChooserAction extends AbstractAction {
    private static final String CLASS_NAME = OpenFileWithJavaFileChooserAction.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    private final Rune rune;
    private final JFileChooser fileChooser = new JFileChooser();

    public OpenFileWithJavaFileChooserAction(Rune rune) {
        super();
        logger.entering(CLASS_NAME, "OpenFileWithJavaFileChooserAction");
        this.rune = rune;
        this.putValue(NAME, "OpenFileWithJavaFileChooserAction");
        this.putValue(SHORT_DESCRIPTION, "Use the Java file chooser to open a file");
        this.putValue(LONG_DESCRIPTION, "Use the Java file chooser to open a file");
        this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_DOWN_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        //noinspection UnnecessaryBoxing
        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_O)); // Conversion to Integer required for retrotranslator

        logger.exiting(CLASS_NAME, "OpenFileWithJavaFileChooserAction");
    }

    public void actionPerformed(ActionEvent e) {
        fileChooser.setDialogTitle("Open File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileHidingEnabled(false);
        fileChooser.setMultiSelectionEnabled(true);
        centerOnComponent(fileChooser, rune);
        int selectedOption = fileChooser.showOpenDialog(rune);
        if (selectedOption == JFileChooser.APPROVE_OPTION) {
            try {
                rune.open(Arrays.asList(fileChooser.getSelectedFiles()));
            } catch (Exception ex) {
                rune.getMessenger().showError("Failed to open files", ex);
            }
        }
    }

    public void centerOnParent(Component c) {
        Point centerPoint = new Point();
        Point parentPosition = c.getParent().getLocation();
        Dimension parentDimension = c.getParent().getSize();
        centerPoint.x = parentPosition.x + parentDimension.width / 2 - c.getWidth() / 2;
        centerPoint.y = parentPosition.y + parentDimension.height / 2 - c.getHeight() / 2;
        c.setLocation(centerPoint);
    }

    public void centerOnComponent(Component c, Component parent) {
        Point centerPoint = new Point();
        Point parentPosition = parent.getLocation();
        Dimension parentDimension = parent.getSize();
        centerPoint.x = parentPosition.x + parentDimension.width / 2 - c.getWidth() / 2;
        centerPoint.y = parentPosition.y + parentDimension.height / 2 - c.getHeight() / 2;
        c.setLocation(centerPoint);
    }
}
