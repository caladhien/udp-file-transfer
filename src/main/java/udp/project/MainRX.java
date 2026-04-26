package udp.project;

import udp.project.receiver.Receiver;

import java.util.Scanner;

public class MainRX {

    public static void main(String[] args) throws Exception {

        int port;

        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        } else {
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter port: ");
            port = Integer.parseInt(sc.nextLine());
        }

        new Receiver(port).start();
    }
}