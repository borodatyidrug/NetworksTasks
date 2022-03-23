package jassenger.jassenger.client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    public static void main(String [] args) {
        //String host = args[0];
        String host = "netology.homework";
        //int port = Integer.parseInt(args[1]);
        int port = 9500;
        try (Socket serverSocket = new Socket(host, port);
                DataOutputStream out = new DataOutputStream(serverSocket.getOutputStream());
                DataInputStream in = new DataInputStream(serverSocket.getInputStream());
                Scanner sc = new Scanner(System.in)) {
          
            System.out.println("Подключаюсь к " + host + " на порт " + port);
            System.out.println("Адрес удаленного хоста: " + serverSocket.getRemoteSocketAddress());
            out.writeUTF(serverSocket.getLocalSocketAddress().toString());
            
            while(!serverSocket.isClosed()) {
                String serverResponce = in.readUTF();
                if (serverResponce.isEmpty() || serverResponce.isBlank()) {
                    continue;
                } 
                System.out.println(serverResponce);
                if (serverResponce.endsWith("<end>")) {
                    break;
                }
                out.writeUTF(sc.nextLine());
            }
            System.out.println("Конец сеанса связи с " + serverSocket.getRemoteSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
