package com.alteredmechanism.notepad;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FontManager {

	public static final String BASE_DIR = "fonts";
	
	public static final String[] fontFiles = new String[] {
			"anonymous-pro/Anonymous Pro B.ttf",
			"anonymous-pro/Anonymous Pro BI.ttf",
			"anonymous-pro/Anonymous Pro I.ttf",
			"anonymous-pro/Anonymous Pro.ttf",
			"monofur/monof_tt.txt",
			"monofur/monof55.txt",
			"monofur/monof56.txt"
	};
	
	private List fonts = new ArrayList();
	
	public FontManager() throws FontFormatException, IOException {
		StringBuffer path = new StringBuffer();
		
		for (int i = 0; i < fontFiles.length; i++) {
			path.setLength(0);
			path.append(BASE_DIR);
			path.append('/');
			path.append(fontFiles[i]);
			InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(path.toString());
			Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
			fonts.add(font);
		}
	}
	
	public List getFonts() {
		return fonts;
	}
	
	public List getFontFamilies() {
		List families = new ArrayList();
        try {
			List fonts = new FontManager().getFonts();
			for (int i = 0; i < fonts.size(); i++) {
				families.add(((Font) fonts.get(i)).getFamily());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
        return families;
	}
}