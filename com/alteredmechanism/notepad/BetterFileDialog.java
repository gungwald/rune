package com.alteredmechanism.notepad;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

public class BetterFileDialog extends FileDialog {

	private static final long serialVersionUID = 1L;

	public BetterFileDialog(Frame parent) {
		super(parent);
	}

	public BetterFileDialog(Frame parent, String title) {
		super(parent, title);
	}

	public BetterFileDialog(Frame parent, String title, int mode) {
		super(parent, title, mode);
	}

	/**
	 * This is necessary because getFile does not include the
	 * directory it is in.
	 * 
	 * @return An object representing the selected file
	 */
	public File getSelectedFile() {
		File file = null;
		String dirName = this.getDirectory();
		if (dirName != null) {
			File dir = new File(dirName);
			String fileName = this.getFile();
			if (fileName != null) {
				File simpleFile = new File(fileName);
				file = new File(dir, simpleFile.getName());
			}
		}
		return file;
	}
}
