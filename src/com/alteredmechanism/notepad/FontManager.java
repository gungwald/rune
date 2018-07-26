package com.alteredmechanism.notepad;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {

	public static final String BASE_DIR = "fonts";

	public static final String[] fontFiles = new String[] { "anonymous_pro/Anonymous Pro B.ttf",
			"anonymous_pro/Anonymous Pro BI.ttf", "anonymous_pro/Anonymous Pro I.ttf",
			"anonymous_pro/Anonymous Pro.ttf", 
			"monofur/monof55.ttf", "monofur/monof56.ttf",
			"proggy/ProggySquare.ttf"};

	private Map fonts = new HashMap();
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
				Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
				fonts.put(font.getName(), font);
			}
			catch (Exception e) {
				this.messenger.showError("Failed to load font: " + fontFiles[i], e);
			}
			finally {
				close(stream);
			}
		}
	}

	private void close(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Map getFonts() {
		return fonts;
	}

}
