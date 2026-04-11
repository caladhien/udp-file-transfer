package udp.project.sender;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSender {

    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;

    public UdpSender(String host, int port) throws Exception {
        this.socket = new DatagramSocket();
        this.address = InetAddress.getByName(host);
        this.port = port;
    }

    public void send(byte[] data) throws Exception {
        DatagramPacket packet =
                new DatagramPacket(data, data.length, address, port);

        socket.send(packet);
    }

    public void close() {
        socket.close();
    }
}