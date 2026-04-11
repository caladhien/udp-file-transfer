package udp.project.sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileChunker {

    private final int chunkSize;

    public FileChunker(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public List<byte[]> splitFile(File file) throws IOException {

        List<byte[]> chunks = new ArrayList<>();

        try (FileInputStream stream = new FileInputStream(file)) {

            byte[] buffer = new byte[chunkSize];
            int bytesRead;

            while ((bytesRead = stream.read(buffer)) != -1) {

                byte[] chunk = new byte[bytesRead];

                System.arraycopy(buffer, 0, chunk, 0, bytesRead);

                chunks.add(chunk);
            }
        }

        return chunks;
    }
}