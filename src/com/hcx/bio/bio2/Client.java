package com.hcx.bio.bio2;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 多发和多收
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/13 17:16
 */
public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("===客户端启动===");
        Socket socket = new Socket("127.0.0.1", 9999);
        OutputStream outputStream = socket.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("请输入：");
            String msg = scanner.nextLine();
            printStream.println(msg);
            printStream.flush();
        }
    }
}
