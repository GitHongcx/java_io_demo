package com.hcx.bio.bio1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 传统BIO
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/13 16:21
 */
public class Server {
    public static void main(String[] args) {
        try {
            System.out.println("===服务端启动===");
            //服务端端口注册
            ServerSocket serverSocket = new ServerSocket(9999);
            //监听客户端的socket连接请求
            Socket socket = serverSocket.accept();
            //从socket管道中获得字节输入流对象
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String msg;
            if ((msg = bufferedReader.readLine()) != null) {
                System.out.println("服务端接收到：" + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
