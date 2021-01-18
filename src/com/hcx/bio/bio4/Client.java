package com.hcx.bio.bio4;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 伪异步IO
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/14 11:13
 */
public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",9999);
            OutputStream outputStream = socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            Scanner scanner = new Scanner(System.in);
            //循环发送消息
            while (true){
                System.out.println("请说：");
                String msg = scanner.nextLine();
                printStream.println(msg);
                printStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
