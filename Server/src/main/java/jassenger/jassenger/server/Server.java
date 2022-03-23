package jassenger.jassenger.server;

import java.net.*;
import java.io.*;

public class Server extends Thread {
    private final ServerSocket serverSocket;
    private final String[] STORY = {
            "Жил да был брадобрей\n- Не найти никого добрей!\n"
                            + "Брадобрей стриг и брил зверей...",
            "После той чудесной стрижки\n"
                + "Кошки были словно мышки,\n"
                + "Даже глупые мартышки\n"
                + "Походили на людей!\n",
            "Это было прошлым летом,\n"
                + "В середине января,\n"
                + "В тридесятом королевстве,\n"
                + "Там где нет в помине короля!\n"
        };
   
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(60000);
    }

    @Override
    public void run() {
        int currentStoryPart = 0;
        String clientAddress = null;
        try (Socket clientSocket = serverSocket.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());) {

            clientAddress = in.readUTF();
            System.out.println("Установлено новое соединение c " + clientAddress);
            out.writeUTF("Хотите ли, я расскажу вам сказочку? (y/n)");
            
            while (currentStoryPart < STORY.length) {
                String responce = in.readUTF();
                if (!responce.equals("y") && !responce.equals("n")) {
                    out.writeUTF("Некорректный ответ. Сказочку или слушают, "
                            + "или - не слушают!");
                    continue;
                } else if (responce.equals("n")) {
                    out.writeUTF("Тогда нам больше не о чем говорить. Прощайте, "
                            + "Ибрагим-паша из " + clientAddress + "! <end>");
                    clientSocket.close();
                    System.out.println("Прекращено соединение c " + clientAddress);
                    return;
                } else if (responce.equals("y")) {
                    out.writeUTF(STORY[currentStoryPart] + ".../* Дальше? (y/n)*/");
                    currentStoryPart++;
                }
            }
            out.writeUTF("Вот и сказочки конец, а кто слушал, тот - Ибрагим. "
                    + "Ибрагим из Парги. Раб, насильно обращенный в ислам в детстве...<end>");
        } catch (SocketTimeoutException s) {
            System.out.println("Время сокета истекло!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Прекращено соединение c " + clientAddress);
    }
   
   public static void main(String [] args) {
        //int port = Integer.parseInt(args[0]);
        int port = 9500;
        try {
            Thread t = new Server(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
