package com.hcx.bio.file;

import java.io.*;
import java.net.Socket;

/**
 * BIO模式下的文件上传
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/14 17:02
 */
public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputStream);
            //发送上传文件的后缀给服务端
            dos.writeUTF(".txt");
            ///Users/hongcaixia/Documents/11.txt
            InputStream is = new FileInputStream("/Users/hongcaixia/Documents/11.txt");
            //发送数据给服务端
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer))>0){
                dos.write(buffer,0,len);
            }
            dos.flush();
            //通知服务端数据发送完毕
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
