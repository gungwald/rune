package com.alteredmechanism.rune;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.KeyStroke.getKeyStroke;

public class KeyBindings {

    public static final String SAVE_ACTION_ID = "save";

    ComponentInputMap inputMap;

    public KeyBindings(JComponent componentWithMap) {
        inputMap = new ComponentInputMap(componentWithMap);
        String os = System.getProperty("os.name");
        inputMap.put(getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), SAVE_ACTION_ID);
    }

    public ComponentInputMap getInputMap() {
        return inputMap;
    }
}
