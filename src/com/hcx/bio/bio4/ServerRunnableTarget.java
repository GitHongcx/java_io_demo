package com.hcx.bio.bio4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/14 15:34
 */
public class ServerRunnableTarget implements Runnable{

    private Socket socket;
    public ServerRunnableTarget(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        //处理接收到的客户端socket通信
        try {
            //从socket中获取字节输入流
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String msg;
            if((msg = bufferedReader.readLine())!=null){
                System.out.println("服务端接收到："+msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
