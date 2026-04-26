package udp.project.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class FileAssembler {

    private static final Logger log = LoggerFactory.getLogger(FileAssembler.class);

    public void buildFile(Map<Integer, byte[]> chunks, String outputPath, int maxSeq) throws IOException {

        log.info("Building file '{}' from {} packets...", outputPath, chunks.size());

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {

            for (int seq = 1; seq <= maxSeq; seq++) {

                byte[] data = chunks.get(seq);

                if (data == null) {
                    throw new IOException("Missing DATA packet seq=" + seq);
                }

                log.debug("Writing chunk seq={}", seq);

                fos.write(data);
            }
        }

        log.info("File assembly completed: {}", outputPath);
    }
}