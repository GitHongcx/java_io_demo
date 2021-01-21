package com.hcx.aio;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/20 16:23
 */
public class Server {

    final String LOCALHOST = "localhost";
    final int DEFAULT_PORT = 9999;
    AsynchronousServerSocketChannel serverSocketChannel;

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

    public void start() {
        try {
            //绑定监听端口
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(LOCALHOST, DEFAULT_PORT));
            System.out.println("启动服务器，监听端口：" + DEFAULT_PORT);
            while (true) {
                serverSocketChannel.accept(null, new AcceptHandler());
                System.in.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(serverSocketChannel);
        }
    }

    private class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

        @Override
        public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
            if (serverSocketChannel.isOpen()) {
                serverSocketChannel.accept(null, this);
            }
            if (socketChannel != null && socketChannel.isOpen()) {
                ClientHandler handler = new ClientHandler(socketChannel);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                Map<String, Object> map = new HashMap<>();
                map.put("type", "read");
                map.put("buffer", buffer);
                socketChannel.read(buffer, map, handler);
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {

        }
    }

    private class ClientHandler implements CompletionHandler<Integer, Object> {

        private AsynchronousSocketChannel socketChannel;

        public ClientHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void completed(Integer result, Object attachment) {
            Map<String, Object> map = (Map<String, Object>) attachment;
            String type = (String) map.get("type");
            if ("read".equals(type)) {
                ByteBuffer buffer = (ByteBuffer) map.get("buffer");
                buffer.flip();
                map.put("type", "write");
                socketChannel.write(buffer, map, this);
                buffer.clear();
            } else if ("write".equals(type)) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                map.put("type", "read");
                map.put("buffer", byteBuffer);
                socketChannel.read(byteBuffer, map, this);
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {

        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
