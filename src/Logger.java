import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	private Readout readout;
	private BufferedWriter writer;
	
	public Logger() {}

	// Convenience method for our logging
	public void log(long time, String event, int stash, int hens, int henEggs) {
		String space = "";
		for (int i = 0; i < 16 - event.length(); i++) {space += " ";}
		
		log("" + time + "\t" + event + space + "Stash: " + stash + " \tHens: " + hens + "(" + henEggs + ")");
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
