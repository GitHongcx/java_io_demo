package com.hcx.nio.selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/19 14:42
 */
public class Server {

    public static void main(String[] args) throws Exception {
        //获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //设置为非阻塞模式
        ssChannel.configureBlocking(false);
        //绑定连接端口
        ssChannel.bind(new InetSocketAddress(9999));
        //获取选择器selector
        Selector selector = Selector.open();
        //将通道注册到选择器 并监听接收事件
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        //使用selector轮询已经就绪好的事件
        while (selector.select() > 0) {
            //从选择器中获取所有注册好的通道的就绪事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //判断当前事件
                //接收事件
                if (selectionKey.isAcceptable()) {
                    //获取当前接入的客户端通道
                    SocketChannel sChannel = ssChannel.accept();
                    //设置为非阻塞
                    sChannel.configureBlocking(false);
                    //将客户端通道注册到选择器 并监听读取事件
                    sChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    //读取事件
                    //获取当前选择器上的读就绪事件
                    SocketChannel sChannel = (SocketChannel) selectionKey.channel();
                    //读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = sChannel.read(buffer)) > 0) {
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, len));
                        buffer.clear();
                    }
                }
                //处理完毕移除当前事件
                iterator.remove();
            }
        }
    }
}
