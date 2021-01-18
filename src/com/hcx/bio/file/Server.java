package com.hcx.bio.file;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 接收客户端的文件并保存到磁盘
 *
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/14 17:02
 */
public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerReaderThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
