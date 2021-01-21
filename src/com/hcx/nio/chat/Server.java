package com.hcx.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/19 20:01
 */
public class Server {
    //选择器
    private Selector selector;
    //服务端通道
    private ServerSocketChannel ssChannel;
    //端口
    private static final int PORT = 9999;

    //初始化
    public Server() {
        try {
            //创建选择器
            selector = Selector.open();
            //获取通道
            ssChannel = ServerSocketChannel.open();
            //绑定客户端连接的端口
            ssChannel.bind(new InetSocketAddress(PORT));
            //设置非阻塞
            ssChannel.configureBlocking(false);
            //把通道注册到选择器 指定接收连接事件
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听客户端的消息
     */
    private void listen() {
        try {
            while (selector.select() > 0) {
                //获取选择器中所有注册通道的就绪事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey sk = iterator.next();
                    //判断事件类型
                    if (sk.isAcceptable()) {
                        //接入事件
                        //获取当前客户端通道
                        SocketChannel sChannel = ssChannel.accept();
                        //设置为非阻塞模式
                        sChannel.configureBlocking(false);
                        //将客户端通道注册到选择器 并监听读取事件
                        sChannel.register(selector, SelectionKey.OP_READ);
                    } else if (sk.isReadable()) {
                        //可读事件
                        //处理客户端消息 接收并转发
                        readClientData(sk);
                    }
                    //处理完毕移除当前事件
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收当前客户端通道的消息并转发到其他所有客户端通道
     *
     * @param sk
     */
    private void readClientData(SelectionKey sk) {
        SocketChannel sChannel = null;
        try {
            sChannel = (SocketChannel) sk.channel();
            //创建缓冲区对象开始接收客户端通道的数据
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = sChannel.read(buffer);
            if (count > 0) {
                buffer.flip();
                //获取读到的信息
                String msg = new String(buffer.array(), 0, buffer.remaining());
                System.out.println("接收到了客户端消息：" + msg);
                //把消息推送给其他所有客户端
                sendMsgToAllClient(msg, sChannel);
            }
        } catch (Exception e) {
            try {
                System.out.println("有人离线了：" + sChannel.getRemoteAddress());
                //取消注册
                sk.cancel();
                sChannel.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     * 把当前客户端的消息推送给全部在线注册的channel
     *
     * @param msg
     * @param sChannel
     */
    private void sendMsgToAllClient(String msg, SocketChannel sChannel) throws IOException {
        System.out.println("服务端开始转发消息：当前处理的线程：" + Thread.currentThread().getName());
        for (SelectionKey key : selector.keys()) {
            Channel channel = key.channel();
            //排除自己
            if (channel instanceof SocketChannel && channel != sChannel) {
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                ((SocketChannel) channel).write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        //创建服务端对象
        Server server = new Server();
        //监听客户端的消息事件：连接消息 群聊消息 离线消息
        server.listen();
    }


}
