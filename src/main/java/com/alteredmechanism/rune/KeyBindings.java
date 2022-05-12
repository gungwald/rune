package com.alteredmechanism.rune;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.KeyStroke.getKeyStroke;

public class KeyBindings {

    public static final String SAVE_ACTION_ID = "save";

    InputMap inputMap = new InputMap();

    public KeyBindings() {
        String os = System.getProperty("os.name");
        if (os.startsWith("Mac")) {
            inputMap.put(getKeyStroke("Command-S"), SAVE_ACTION_ID);
        } else {
            inputMap.put(getKeyStroke("Control-S"), SAVE_ACTION_ID);
        }
    }

    public InputMap getInputMap() {
        return inputMap;
    }
}
