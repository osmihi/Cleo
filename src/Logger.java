/* Othman Smihi - ICS 462 Program 2
 * 
 * Logger.java
 * 
 * This is the class that takes care of our logging operations.
 * This includes writing to a file and sending the message to the gui.
 * The GUI interaction is facilitated via the Readout interface, which is 
 * implemented by the GUI.
 * 
 * it should be able to operated for the GUI + file, or just one.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	private Readout readout;
	private BufferedWriter writer;
	private File file;
	
	public Logger() {}

	// A method to format strings to a fixed length
	private String space(String str, int len) {
		String space = "";
		for (int i = 0; i < len - str.length(); i++) {space += " ";}
		return str + space;
	}
	
	// Convenience method for our logging
	public void log(long time, String event, int stash, int hens, int henEggs) {
		
		log(space("" + time, 7) + space(event, 18) + "Stash: " + space("" + stash, 5) + "Hens: " + space("" + hens + "(" + henEggs + ")", 10));
	}
	
	public void log(String logStr) {
		if (readout != null) {
			readout.writeText(logStr);
		}
		if (writer != null) {
			try {
				writer.write(logStr);
				writer.write(System.getProperty("line.separator"));
				writer.flush();
			} catch (IOException e) {}
		}
	}
	
	public void setReadout(Readout ro) {
		readout = ro;
	}
	
	public void setLogFile(File outFile) throws IOException {
		if (!outFile.exists()) {
			outFile.createNewFile();
		}

		file = outFile;
		
		FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
		writer = new BufferedWriter(fw);
	}
	
	// This should open the log file with the default application in Windows.
	// This could certainly be more robust and support more OSes, but it's just a simple "bonus" feature.
	public void showLog() throws IOException {
		// open the log file in Windows
		Runtime.getRuntime().exec(new String[] {"rundll32", "url.dll,FileProtocolHandler", file.getAbsolutePath()});
	}
}
