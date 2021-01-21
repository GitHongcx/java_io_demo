package com.hcx.aio;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/20 16:23
 */
public class Client {

    final String LOCALHOST = "localhost";
    final int DEFAULT_PORT = 9999;
    AsynchronousSocketChannel socketChannel;

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                System.out.println("关闭" + closeable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(){
        try{
            //创建channel
            socketChannel = AsynchronousSocketChannel.open();
            Future<Void> future = socketChannel.connect(new InetSocketAddress(LOCALHOST, DEFAULT_PORT));
            future.get();

            //读取用户输入
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                String msg = bufferedReader.readLine();
                byte[] bytes = msg.getBytes();
                ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

                Future<Integer> writeResult = socketChannel.write(byteBuffer);
                writeResult.get();
                byteBuffer.flip();

                Future<Integer> readResult = socketChannel.read(byteBuffer);
                readResult.get();
                String str = new String(byteBuffer.array());
                byteBuffer.clear();
                System.out.println(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(socketChannel);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

}
