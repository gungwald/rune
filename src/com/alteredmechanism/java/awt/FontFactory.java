package com.alteredmechanism.java.awt;

import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;

public class FontFactory {

	public Font create(String name, int style, int size) {
		return new Font(name, style, (int) pointToPixel(size));
	}
	
	public Font derive(Font f) {
		return f.deriveFont(pointToPixel(f.getSize()));
	}

    public float pointToPixel(float pointSize) {
        float ppi = Toolkit.getDefaultToolkit().getScreenResolution();
        float pixelHeight = Math.round(pointSize / (72f / ppi));
        System.out.println("Converted font point size " + pointSize + " to pixel height = " + pixelHeight);
        return pixelHeight;
    }

	public Font derive(Component c) {
		return derive(c.getFont());
	}

}
