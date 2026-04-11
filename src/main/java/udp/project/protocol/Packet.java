package udp.project.protocol;

public class Packet {

    private short transmissionId;
    private int sequenceNumber;
    private int maxSequenceNumber;

    private byte[] data;
    private String fileName;
    private byte[] md5;

    public Packet() {
    }

    public short getTransmissionId() {
        return transmissionId;
    }

    public void setTransmissionId(short transmissionId) {
        this.transmissionId = transmissionId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getMaxSequenceNumber() {
        return maxSequenceNumber;
    }

    public void setMaxSequenceNumber(int maxSequenceNumber) {
        this.maxSequenceNumber = maxSequenceNumber;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getMd5() {
        return md5;
    }

    public void setMd5(byte[] md5) {
        this.md5 = md5;
    }
}