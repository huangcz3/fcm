package com.asiainfo.fcm.util;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by LiuJH on 2018/6/20.
 */
public class SocketTest1 {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 8080);

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
                    System.out.println("socket2 说: "+s);
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


        System.out.println("聊天结束");


    }
}
