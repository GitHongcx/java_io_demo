package com.hcx.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/19 20:01
 */
public class Client {

    private Selector selector;
    private static int PORT = 9999;
    private SocketChannel socketChannel;

    //初始化客户端
    public Client() {
        try {
            //创建选择器
            selector = Selector.open();
            //连接服务端
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", PORT));
            //设置非阻塞
            socketChannel.configureBlocking(false);
            //监听读事件 读取服务端发来的消息
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        //定义线程专门负责监听服务端发送的读消息事件
        new Thread(() -> {
            try {
                client.readInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            client.sendToServer(s);
        }
    }

    /**
     * 发送消息到服务端
     * @param s
     */
    private void sendToServer(String s) {
        try {
            socketChannel.write(ByteBuffer.wrap(("小红说："+s).getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取服务端发送过来的消息
     */
    private void readInfo() throws IOException {
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    sc.read(buffer);
                    System.out.println(new String(buffer.array()).trim());
                }
                iterator.remove();
            }
        }
    }
}
