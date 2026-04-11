package udp.project.receiver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class FileAssembler {

    private final Map<Integer, byte[]> chunks = new TreeMap<>();

    public void addChunk(int seqNr, byte[] data) {
        System.out.println("[ASSEMBLER] storing seq=" + seqNr);
        chunks.put(seqNr, data);
    }

    public void buildFile(String outputPath) throws IOException {

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {

            for (Map.Entry<Integer, byte[]> entry : chunks.entrySet()) {
                fos.write(entry.getValue());
            }
        }
    }

}