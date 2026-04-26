package udp.project.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class PacketSerializer {

    private static final int TRANSMISSION_ID_SIZE = 2;
    private static final int SEQUENCE_NUMBER_SIZE = 4;
    private static final int MAX_SEQUENCE_NUMBER_SIZE = 4;
    private static final int MD5_SIZE = 16;

    public byte[] serialize(Packet packet) {
        if (packet.getType() == PacketType.FIRST) return serializeFirst(packet);
        if (packet.getType() == PacketType.LAST) return serializeLast(packet);
        return serializeData(packet);
    }

    public Packet deserialize(byte[] bytes) {

        if (bytes.length < TRANSMISSION_ID_SIZE + SEQUENCE_NUMBER_SIZE) {
            throw new IllegalArgumentException("Packet too small");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);

        Packet p = new Packet();

        short transId = buffer.getShort();
        int seqNr = buffer.getInt();

        p.setTransmissionId(transId);
        p.setSequenceNumber(seqNr);

        if (seqNr == 0) {
            p.setType(PacketType.FIRST);
            deserializeFirst(buffer, p);
        }
        else if (buffer.remaining() == MD5_SIZE) {
            p.setType(PacketType.LAST);
            deserializeLast(buffer, p);
        }
        else {
            p.setType(PacketType.DATA);
            deserializeData(buffer, p);
        }

        return p;
    }

    /**
     * =============================== FIRST ===============================
     */
    private byte[] serializeFirst(Packet p) {

        byte[] fileNameBytes = p.getFileName().getBytes(StandardCharsets.UTF_8);

        int size = TRANSMISSION_ID_SIZE + SEQUENCE_NUMBER_SIZE + MAX_SEQUENCE_NUMBER_SIZE + fileNameBytes.length;

        ByteBuffer buffer = createBuffer(size);

        buffer.putShort(p.getTransmissionId());
        buffer.putInt(p.getSequenceNumber());
        buffer.putInt(p.getMaxSequenceNumber());
        buffer.put(fileNameBytes);

        return buffer.array();
    }

    private void deserializeFirst(ByteBuffer buffer, Packet p) {

        if (buffer.remaining() < MAX_SEQUENCE_NUMBER_SIZE) {
            throw new IllegalArgumentException("Invalid FIRST packet");
        }

        int maxSeq = buffer.getInt();

        byte[] nameBytes = new byte[buffer.remaining()];
        buffer.get(nameBytes);

        p.setMaxSequenceNumber(maxSeq);
        p.setFileName(new String(nameBytes, StandardCharsets.UTF_8));
    }

    /**
     *  =============================== DATA ===============================
     */
    private byte[] serializeData(Packet p) {

        byte[] data = p.getData();

        int size = TRANSMISSION_ID_SIZE + SEQUENCE_NUMBER_SIZE + data.length;

        ByteBuffer buffer = createBuffer(size);

        buffer.putShort(p.getTransmissionId());
        buffer.putInt(p.getSequenceNumber());
        buffer.put(data);

        return buffer.array();
    }

    private void deserializeData(ByteBuffer buffer, Packet p) {

        if (buffer.remaining() == 0) {
            throw new IllegalArgumentException("Empty DATA packet");
        }

        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        p.setData(data);
    }

    /**
     * =============================== LAST ===============================
     */

    private byte[] serializeLast(Packet p) {

        int size = TRANSMISSION_ID_SIZE + SEQUENCE_NUMBER_SIZE + MD5_SIZE;

        ByteBuffer buffer = createBuffer(size);

        buffer.putShort(p.getTransmissionId());
        buffer.putInt(p.getSequenceNumber());
        buffer.put(p.getMd5());

        return buffer.array();
    }

    private void deserializeLast(ByteBuffer buffer, Packet p) {

        if (buffer.remaining() != MD5_SIZE) {
            throw new IllegalArgumentException("Invalid LAST packet");
        }

        byte[] md5 = new byte[MD5_SIZE];
        buffer.get(md5);

        p.setMd5(md5);
    }

    /**
     * =============================== BUFFER ===============================
      */
    private ByteBuffer createBuffer(int size) {
        return ByteBuffer.allocate(size).order(ByteOrder.BIG_ENDIAN);
    }
}