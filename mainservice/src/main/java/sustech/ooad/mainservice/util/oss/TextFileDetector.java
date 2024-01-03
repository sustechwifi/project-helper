package sustech.ooad.mainservice.util.oss;

import java.io.*;

public class TextFileDetector {
    public static boolean isTextFile(byte[] data) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // do nothing, just read lines from the byte array
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}