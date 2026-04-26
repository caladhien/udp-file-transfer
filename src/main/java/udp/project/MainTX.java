package udp.project;

import udp.project.sender.Sender;

import java.util.Scanner;

public class MainTX {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter host (for Local 127.0.0.1): ");
        String host = sc.nextLine();

        System.out.print("Enter port: ");
        int port = Integer.parseInt(sc.nextLine());

        System.out.print("Enter delay (ms, 0 = no delay): ");
        int delay = Integer.parseInt(sc.nextLine());

        Sender sender = new Sender(host, port, delay);

        while (true) {

            System.out.print("Enter file path or 'exit': ");
            String file = sc.nextLine();

            if (file.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                sender.sendFile(file);
                System.out.println("File sent: " + file);
            } catch (Exception e) {
                System.out.println("Error sending file: " + e.getMessage());
            }
        }

        sender.close();
        System.out.println("Sender stopped.");
    }
}