package com.alteredmechanism.notepad;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FontManager {

    public static final String BASE_DIR = "fonts";

    public static final String[] fontFiles = new String[] {"anonymous_pro/Anonymous Pro B.ttf", "anonymous_pro/Anonymous Pro BI.ttf",
            "anonymous_pro/Anonymous Pro I.ttf", "anonymous_pro/Anonymous Pro.ttf", "monofur/monof55.ttf", "monofur/monof56.ttf", "proggy/ProggySquare.ttf"};

    private Map bundledFonts = new HashMap();
    private Messenger messenger;

    public FontManager(Messenger messenger) {
        this.messenger = messenger;
        StringBuffer path = new StringBuffer();

        for (int i = 0; i < fontFiles.length; i++) {
            path.setLength(0);
            path.append(BASE_DIR);
            path.append('/');
            path.append(fontFiles[i]);
            InputStream stream = null;
            try {
                stream = ClassLoader.getSystemClassLoader().getResourceAsStream(path.toString());
                if (stream == null) {
                    System.err.println("Failed to find font file: " + path.toString());
                } else {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
                    bundledFonts.put(font.getName(), font);
                }
            } catch (Exception e) {
                e.printStackTrace();
                String message = "Failed to load font: " + path.toString();
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

    public Map getBundledFonts() {
        return bundledFonts;
    }

    public Map getSystemMonospaceFonts() {
        Font fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        Map monoFonts = new TreeMap();
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        FontRenderContext frc = graphics2D.getFontRenderContext();
        for (int i = 0; i < fonts.length; i++) {
            Font font = fonts[i];
            Font testFont = new Font(font.getName(), font.getStyle(), 12);
            Rectangle2D iBounds = testFont.getStringBounds("i", frc);
            Rectangle2D mBounds = testFont.getStringBounds("m", frc);
            Rectangle2D sBounds = testFont.getStringBounds(" ", frc);
            if (iBounds.getWidth() == mBounds.getWidth() && sBounds.getWidth() == iBounds.getWidth()) {
                monoFonts.put(testFont.getName(), testFont);
            }
        }
        return monoFonts;
    }
    
    public Map getAllMonospaceFonts() {
        Map monoFonts = getSystemMonospaceFonts();
        monoFonts.putAll(getBundledFonts());
        return monoFonts;
    }
    
    public String[] getAllMonospaceFamilyNames() {
        return (String[]) getAllMonospaceFonts().keySet().toArray();
    }
}
