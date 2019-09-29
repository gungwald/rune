package com.alteredmechanism.notepad;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

public class FontManager {

    public static final String[] bundledFontPaths = new String[] {
    		"fonts/anonymous_pro/Anonymous Pro B.ttf",
    		"fonts/anonymous_pro/Anonymous Pro BI.ttf",
            "fonts/anonymous_pro/Anonymous Pro I.ttf",
            "fonts/anonymous_pro/Anonymous Pro.ttf",
            "fonts/monofur/monof55.ttf",
            "fonts/monofur/monof56.ttf",
            "fonts/proggy/ProggySquare.ttf"};

    private Messenger messenger;
    private FontRenderContext fontRenderer = null;
    private Map monospaceFamilies = new TreeMap(); // familyName -> Font sample

    public FontManager(Messenger messenger) {
        this.messenger = messenger;
    }

    /**
     * This uses registerFont which isn't available until Java 6.
     */
    public void loadBundledFonts() {
        for (int i = 0; i < bundledFontPaths.length; i++) {
        	String fontPath = bundledFontPaths[i];
            InputStream stream = null;
            try {
                stream = ClassLoader.getSystemClassLoader().getResourceAsStream(fontPath);
                if (stream == null) {
                    System.err.println("Failed to find font file: " + fontPath);
                } else {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
                    if (monospaceFamilies.containsKey(font.getFamily())) {
                    	System.err.println("Font file " + fontPath + " overwriting existing font family " + font.getFamily());
                    }
                    monospaceFamilies.put(font.getFamily(), font);
                }
            } catch (Exception e) {
                e.printStackTrace();
                String message = "Failed to load font: " + fontPath;
                System.err.println(message);
                if (messenger != null) {
                    messenger.showError(message, e);
                }
            } finally {
                close(stream);
            }
        }
    }
    
    private void close(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public FontRenderContext getFontRenderer() {
    	if (fontRenderer == null) {
    		fontRenderer = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB).createGraphics().getFontRenderContext();
    	}
    	return fontRenderer;
    }
    
    public boolean isMonospace(Font f) {
    	boolean isMonospace;
    	FontRenderContext renderer = getFontRenderer();
        Rectangle2D iBounds = f.getStringBounds("i", renderer);
        Rectangle2D mBounds = f.getStringBounds("M", renderer);
        if (iBounds.getWidth() == mBounds.getWidth()) {
        	isMonospace = true;
        } else {
        	isMonospace = false;
        }
    	return isMonospace;
    }
    
    public void loadMonospaceSystemFamilies() {
        String[] familyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (int i = 0; i < familyNames.length; i++) {
        	String familyName = familyNames[i];
        	Font testFont = new Font(familyName, Font.PLAIN, 12);
        	if (isMonospace(testFont)) {
        		monospaceFamilies.put(familyName, testFont);
        	}
        }
    }
    
    /**
     * Provides a Map of family names to sample Fonts.
     */
    public Map getMonospaceFamilies() {
    	if (monospaceFamilies.size() == 0) {
    		loadBundledFonts();
    		loadMonospaceSystemFamilies();
    	}
    	return monospaceFamilies;
    }
}

    