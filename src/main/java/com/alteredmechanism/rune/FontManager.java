package com.alteredmechanism.rune;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

public class FontManager {

	public static final String[] bundledFontPaths = new String[] { "fonts/anonymous_pro/Anonymous Pro.ttf",
			"fonts/monofur/monof55.ttf", "fonts/proggy/ProggySquare.ttf" };

	private Messenger messenger;
	private FontRenderContext fontRenderer = null;
	private Map monospacedFamilies = new TreeMap(new CaseInsensitiveComparator()); // familyName -> Font sample

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
					if (monospacedFamilies.containsKey(font.getFamily())) {
						System.err.println(
								"Font file " + fontPath + " overwriting existing font family " + font.getFamily());
					}
					monospacedFamilies.put(font.getFamily(), font);
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
			fontRenderer = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB).createGraphics()
					.getFontRenderContext();
		}
		return fontRenderer;
	}

	public boolean isMonospaced(Font f) {
		boolean isMonospaced;
		FontRenderContext renderer = getFontRenderer();
		Rectangle2D iBounds = f.getStringBounds("i", renderer);
		Rectangle2D mBounds = f.getStringBounds("M", renderer);
		if (iBounds.getWidth() == mBounds.getWidth()) {
			isMonospaced = true;
		} else {
			isMonospaced = false;
		}
		return isMonospaced;
	}

	public void loadMonospacedSystemFamilies() {
		String[] familyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i = 0; i < familyNames.length; i++) {
			String familyName = familyNames[i];
			System.out.print("Evaluating font family " + familyName + "...");
			if (familyName.startsWith("Noto") && familyName.indexOf("Mono") < 0) {
				// These fonts are very slow when trying to determine if they're
				// monospaced, so we can eliminate them quickly this way instead.
				System.out.println("skipping due to slow monospace test");
				continue;
			}
			if (familyName.startsWith("Noto Sans Mono CJK")) {
				System.out.println("avoiding failed assertion crash in t2kstrm.c");
				continue;
			}
			Font testFont = new Font(familyName, Font.PLAIN, 12);
			if (testFont.canDisplay('A')) {
				if (isMonospaced(testFont)) {
					System.out.println("good monospaced font");
					monospacedFamilies.put(familyName, testFont);
				} else {
					System.out.println("not monospaced - ignoring");
				}
			} else {
				System.out.println("can't display normal characters - ignoring");
			}
		}
	}

	/**
	 * Provides a Map of family names to sample Fonts.
	 */
	public Map getMonospacedFamilies() {
		if (monospacedFamilies.size() == 0) {
			loadBundledFonts();
			loadMonospacedSystemFamilies();
		}
		return monospacedFamilies;
	}
}
