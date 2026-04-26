package udp.project.utils;

import java.io.FileInputStream;
import java.security.MessageDigest;

public class Md5Util {

    // Der INHALT der Datei wird gehasht
    public static byte[] calculateFile(String filePath) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");

        try (FileInputStream fis = new FileInputStream(filePath)) {

            byte[] buffer = new byte[4096];
            int read;

            while ((read = fis.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }

        return md.digest();
    }
}