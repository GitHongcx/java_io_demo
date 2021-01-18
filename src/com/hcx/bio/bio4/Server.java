package com.hcx.bio.bio4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 伪异步IO
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/14 15:11
 */
public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);

//            while (true) {
//                Socket socket = serverSocket.accept();
//                new ThreadPoolExecutor(3, 1, 120, TimeUnit.SECONDS,
//                        new ArrayBlockingQueue<>(3)).execute(() -> {
//                    //处理接收到的客户端socket通信
//                    try {
//                        //从socket中获取字节输入流
//                        InputStream inputStream = socket.getInputStream();
//                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                        String msg;
//                        if ((msg = bufferedReader.readLine()) != null) {
//                            System.out.println("服务端接收到：" + msg);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }

            //初始化线程池对象
            HandlerSocketServerPool pool = new HandlerSocketServerPool(5, 10);

            //循环接收客户端socket连接请求
            while (true) {
                Socket socket = serverSocket.accept();
                //把socket封装成任务对象交给线程池处理
                Runnable runnable = new ServerRunnableTarget(socket);
                pool.execute(runnable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
