package udp.project.receiver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UdpReceiver {

    private final DatagramSocket socket;

    private static final int BUFFER_SIZE = 2048;

    public UdpReceiver(int port) throws Exception {
        this.socket = new DatagramSocket(port);
    }

    public byte[] receive() throws Exception {

        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);

        socket.receive(packet);

        int length = packet.getLength();

        return Arrays.copyOf(buffer, length);
    }

    public void close() {
        socket.close();
    }
}