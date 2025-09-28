package com.alteredmechanism.java.awt;

import java.awt.*;

public class FontStringId {

    public static String buildStringIdFor(Font f) {
        return f.getFamily() + ":" + convertStyleToString(f.getStyle()) + ":" + f.getSize();
    }

    public static Font lookupFontFrom(String id) {
        String[] parts = id.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid font id: " + id);
        }
        String family = parts[0];
        int style = convertStringToStyle(parts[1]);
        int size = Integer.parseInt(parts[2]);
        return new Font(family, style, size);
    }

    public static String convertStyleToString(int style) {
        switch (style) {
            case Font.PLAIN:
                return "PLAIN";
            case Font.BOLD:
                return "BOLD";
            case Font.ITALIC:
                return "ITALIC";
            case Font.BOLD | Font.ITALIC:
                return "BOLD ITALIC";
            default:
                return "UNKNOWN";
        }
    }

    public static int convertStringToStyle(String style) {
        if (style.toUpperCase().equals("PLAIN")) {
            return Font.PLAIN;
        } else if (style.toUpperCase().equals("BOLD")) {
            return Font.BOLD;
        } else if (style.toUpperCase().equals("ITALIC")) {
            return Font.ITALIC;
        } else if (style.toUpperCase().equals("BOLD ITALIC")) {
            return Font.BOLD | Font.ITALIC;
        }
        throw new IllegalArgumentException("Invalid font style: " + style);
    }
}

