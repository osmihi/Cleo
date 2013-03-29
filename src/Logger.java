import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	private Readout readout;
	private BufferedWriter writer;
	
	public Logger() {}

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

		FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
		writer = new BufferedWriter(fw);
	}
}
