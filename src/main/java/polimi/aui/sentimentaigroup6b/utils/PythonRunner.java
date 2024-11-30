package polimi.aui.sentimentaigroup6b.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;

@Component
public class PythonRunner {

    @Value("${python.executable}")
    private String pythonExecutable;

    @Value("${python.script}")
    private String pythonScript;

    @PostConstruct
    public void printExecutableAndScript() {
        System.out.println("Python Executable: " + this.pythonExecutable);
        checkPath(pythonExecutable);
        System.out.println("Python Script: " + this.pythonScript);
        checkPath(pythonScript);
    }

    public void runPythonFunction(String audioData) {
        try {

            ProcessBuilder pb = new ProcessBuilder(pythonExecutable, pythonScript);

            Process process = pb.start();

            try (OutputStream os = process.getOutputStream()) {
                os.write(audioData.getBytes());
                os.flush();
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Python Output: " + line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Python process exited with code: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPath(String path) {
        File file = new File(path);

        if (file.exists()) {
            if (file.isFile()) {
                System.out.println("The path exists and it is a file.");
            } else if (file.isDirectory()) {
                System.out.println("The path exists and it is a directory.");
            }
        } else {
            System.out.println("The path does not exist: " + path);
        }
    }
}
