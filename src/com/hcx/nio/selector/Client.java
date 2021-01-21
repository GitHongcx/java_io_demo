package com.hcx.nio.selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/19 15:18
 */
public class Client {
    public static void main(String[] args) throws Exception{
        //获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9999));
        //设置为非阻塞
        socketChannel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //发送数据到服务端
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("请输入：");
            String msg = scanner.nextLine();
            buffer.put(("小红："+msg).getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
    }
}
