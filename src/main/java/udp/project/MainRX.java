package udp.project;


import udp.project.receiver.Receiver;

public class MainRX {

    public static void main(String[] args) {

        int port = 9000;

        try {
            Receiver receiver = new Receiver(port);
            receiver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}