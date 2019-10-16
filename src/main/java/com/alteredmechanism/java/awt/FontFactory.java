package com.alteredmechanism.java.awt;

import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;

public class FontFactory {
    
    protected boolean convertPointsToPixels = false;

	public Font create(String name, int style, int size) {
		return transform(new Font(name, style, size));
	}
	
	public Font derive(Font f) {
		return transform(f);
	}

	public Font derive(Component c) {
		return transform(c.getFont());
	}

    public Font transform(Font f) {
        Font xformedFont = f;
        if (convertPointsToPixels) {
            xformedFont = f.deriveFont(pointToPixel(f.getSize()));
        }
        return xformedFont;
    }
    
    public float pointToPixel(float pointSize) {
        float ppi = Toolkit.getDefaultToolkit().getScreenResolution();
        float pixelHeight = Math.round(pointSize / (72f / ppi));
        System.out.println("Converted font point size " + pointSize + " to pixel height = " + pixelHeight);
        return pixelHeight;
    }
}
