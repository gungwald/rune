package com.alteredmechanism.notepad;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextArea;
import javax.swing.text.Document;

public class AntiAliasedJTextArea extends JTextArea {

	private static final long serialVersionUID = 1L;

	public AntiAliasedJTextArea() {
	}

	public AntiAliasedJTextArea(String text) {
		super(text);
	}

	public AntiAliasedJTextArea(Document doc) {
		super(doc);
	}

	public AntiAliasedJTextArea(int rows, int columns) {
		super(rows, columns);
	}

	public AntiAliasedJTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
	}

	public AntiAliasedJTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
	}

	// Anti-aliasing fonts in Java 1.3 and 1.4 to get rid of the jaggies.
	// Override the paintComponent method of the JComponent you
	// want to anti-alias.

	/**
	 * called whenever system has a slice to render
	 *
	 * @param g
	 *            Graphics defining where and region to paint.
	 */
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// for anti-aliasing geometric shapes
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// for anti-aliasing text
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// to go for quality over speed
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		super.paintComponent(g2d);
	}
}
