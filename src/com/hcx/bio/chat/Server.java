package com.hcx.bio.chat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * BIO模式下的端口转发
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/14 20:35
 */
public class Server {
    //存储所有在线socket
    public static List<Socket> allOnlineSocket = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while (true){
                Socket socket = serverSocket.accept();
                //把登陆的客户端socket存到集合中
                allOnlineSocket.add(socket);
                //为每个socket分配独立的线程来处理
                new ServerReaderThread(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
