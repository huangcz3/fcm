package com.asiainfo.fcm.util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by LiuJH on 2018/6/20.
 */
public class SocketTest2 {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);
        Socket socket = serverSocket.accept();

        OutputStream outputStream = socket.getOutputStream();

        PrintWriter pw = new PrintWriter(outputStream);

        Scanner scanner = new Scanner(System.in);
        new Thread(() -> {
            try {
                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = null;
                System.out.println("start");
                while ((s = bufferedReader.readLine()) != null) {
                    System.out.println("socket1 说: " + s);
                    if (s.equals("Bye")) {
                        break;
                    }
                }
                socket.shutdownOutput();
                System.out.println("聊天结束");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        while (true) {
            if (!socket.isOutputShutdown()&& scanner.hasNext()) {
                String next = scanner.next();
                pw.println(next);
                pw.flush();
            }else {
                break;
            }
        }

    }
}
