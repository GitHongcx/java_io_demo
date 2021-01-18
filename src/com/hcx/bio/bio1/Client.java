package com.hcx.bio.bio1;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 传统BIO
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
        //此处没有换行，而服务端是一行一行的读取数据 等不到换行就会报错
        //printStream.print("hello,server");
        printStream.println("hello,server");
        printStream.flush();
    }
}
