package LevelEditor;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class TempSaver {
	private File tempDir;
	private String name;
	public TempSaver(String _name) throws IOException {
		name = _name;
		String tempPath = System.getProperty("java.io.tmpdir") + _name;
		tempDir = new File(tempPath);
		if(tempDir.mkdir()){
			updateTempConfig();
		}
	}

	public void updateTempConfig() throws IOException {
		List<String> lines = Arrays.asList(name, "This is a test!");
		System.out.println(tempDir.getAbsolutePath() +name);
		Files.write(Paths.get(tempDir.getAbsolutePath() + "\\" + name + ".txt" ), lines, StandardCharsets.UTF_8);
	}
}

