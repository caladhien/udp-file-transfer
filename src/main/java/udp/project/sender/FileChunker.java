package udp.project.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileChunker {

    private static final Logger log = LoggerFactory.getLogger(FileChunker.class);

    private final int chunkSize;

    public FileChunker(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    /**
     * Zerlegt eine Datei in ByteChunks fester Größe.
     */
    public List<byte[]> splitFile(File file) throws IOException {

        List<byte[]> chunks = new ArrayList<>();

        log.info("Splitting file '{}' into chunks...", file.getName());
        log.info("File size: {} bytes, chunk size: {} bytes", file.length(), chunkSize);

        try (FileInputStream stream = new FileInputStream(file)) {

            byte[] buffer = new byte[chunkSize];
            int bytesRead;
            int index = 0;

            while ((bytesRead = stream.read(buffer)) != -1) {

                byte[] chunk = new byte[bytesRead];
                System.arraycopy(buffer, 0, chunk, 0, bytesRead);

                chunks.add(chunk);

                index++;

                // debug — если нужно посмотреть детали
                log.debug("Chunk {} created (size={} bytes)", index, bytesRead);
            }
        }

        log.info("Finished splitting: {} chunks created", chunks.size());

        return chunks;
    }

    public int getChunkSize() {
        return chunkSize;
    }
}