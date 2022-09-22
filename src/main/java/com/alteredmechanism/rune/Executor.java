package com.alteredmechanism.rune;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

public class Executor {

	public static void copyLines(InputStream in, PrintStream out) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		while (true) {
			String line = bufferedReader.readLine();
			if (line == null)
				break;
			else
				out.println(line);
		}
		out.flush();
	}

	public static int exec(String[] cmd) throws IOException, InterruptedException {
		Process mvn = Runtime.getRuntime().exec(cmd);
		copyLines(mvn.getInputStream(), System.out);
		copyLines(mvn.getErrorStream(), System.err);
		return mvn.waitFor();
	}
	
	public static int exec(List<String> cmd) throws IOException, InterruptedException {
		return exec((String[]) cmd.toArray(new String[cmd.size()]));
	}
}
