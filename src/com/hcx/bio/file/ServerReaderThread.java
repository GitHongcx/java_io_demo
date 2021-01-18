package com.hcx.bio.file;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/14 17:49
 */
public class ServerReaderThread extends Thread{

    private Socket socket;
    public ServerReaderThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            //数据输入流读取客户端发送过来的数据
            DataInputStream dis = new DataInputStream(inputStream);
            //读取客户端发送过来的文件类型
            String suffix = dis.readUTF();
            System.out.println("服务端成功接收到了文件类型："+suffix);
            //使用字节输出管道把客户端发来的文件写出去
            OutputStream ops = new FileOutputStream("/Users/hongcaixia/Documents/"+ UUID.randomUUID().toString()+suffix);
            //从数据输入流中读取数据，写到字节输出流
            byte[] buffer = new byte[1024];
            int len;
            while ((len = dis.read(buffer))>0){
                ops.write(buffer,0,len);
            }
            ops.close();
            System.out.println("服务端接收文件并保存成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
