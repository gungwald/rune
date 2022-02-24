import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import com.alteredmechanism.rune.Rune;

public class RuneLauncher {

	// The problem with this is that the output of the attempt to run Java can't
	// be captured without waiting on the process to exit. And this keeps 2 JVMs
	// in memory for the life time of the process. So it uses twice the memory
	// than is needed. If we exit immediately after starting the process, then
	// it is not possible to capture any failure that occurs so that it can be
	// displayed for the user. So it is a silent failure, if it fails, and the
	// user never knows what happened and then hates Java applications because
	// they don't work and don't even display an error message when the fail.

	public static void main(String[] args) {
		List<String> cmd = new ArrayList<String>(Arrays.asList(args));
		cmd.add(0, "-classpath");
		cmd.add(1, System.getProperty("java.class.path"));
		cmd.add(2, Rune.class.getName());
		if (System.getProperty("os.name").startsWith("Mac OS")) {
			cmd.add(0, "-Xdock:name=Rune");
			// TODO - argList.add(1, "-Xdock:icon=" + iconFile);
		}
		// TODO - Add out-of-memory handler
		cmd.add(0, "java");
		System.out.println(cmd);
		try {
			Executor.exec(cmd);
		} catch (Exception e) {
			if (GraphicsEnvironment.isHeadless()) {
				e.printStackTrace();
			} else {
				JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Failed to start application", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
