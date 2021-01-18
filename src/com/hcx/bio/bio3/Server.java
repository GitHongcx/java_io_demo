package com.hcx.bio.bio3;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 接收多个客户端
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/14 10:57
 */
public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            //循环不断接收客户端的socket连接请求
            while (true){
                Socket socket = serverSocket.accept();
                //创建独立的线程与客户端socket通信
                new ServerReaderThread(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
