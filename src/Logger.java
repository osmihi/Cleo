import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	private Readout readout;
	private BufferedWriter writer;
	
	public Logger() {}

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
