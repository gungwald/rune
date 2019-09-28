package com.alteredmechanism.notepad;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    private String[] monospaceFamilyNames = null;

    public FontManager(Messenger messenger) {
        this.messenger = messenger;
    }

    /**
     * This uses registerFont which isn't available until Java 6.
     */
    public void registerBundledFonts() {
    	GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (int i = 0; i < bundledFontPaths.length; i++) {
        	String fontPath = bundledFontPaths[i];
            InputStream stream = null;
            try {
                stream = ClassLoader.getSystemClassLoader().getResourceAsStream(fontPath);
                if (stream == null) {
                    System.err.println("Failed to find font file: " + fontPath);
                } else {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
                    // The registerFont method isn't available until Java 6.
                    genv.registerFont(font);
                }
            } catch (Exception e) {
                e.printStackTrace();
                String message = "Failed to register font: " + fontPath;
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

    public String[] getMonospaceFamilyNames() {
    	if (monospaceFamilyNames == null) {
	        String[] familyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	        List monoFonts = new ArrayList();
	        FontRenderContext renderer = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB).createGraphics().getFontRenderContext();
	        for (int i = 0; i < familyNames.length; i++) {
	        	String familyName = familyNames[i];
	            Font testFont = new Font(familyName, Font.PLAIN, 12);
	            Rectangle2D iBounds = testFont.getStringBounds("i", renderer);
	            Rectangle2D mBounds = testFont.getStringBounds("M", renderer);
	            if (iBounds.getWidth() == mBounds.getWidth()) {
	                monoFonts.add(testFont.getName());
	            }
	        }
	        monospaceFamilyNames = (String[]) monoFonts.toArray(new String[monoFonts.size()]);
    	}
    	return monospaceFamilyNames;
    }
}

    