package com.hcx.bio.chat;

import java.io.*;
import java.net.Socket;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/14 20:41
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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String msg;
            while ((msg = bufferedReader.readLine())!=null){
                //服务端接收到客户端的消息，将消息推送给当前所有在线socket
                sendMsgToAllClient(msg);
            }
        } catch (IOException e) {
            System.out.println("当前有人下线了");
            //从在线socket中移除本socket
            Server.allOnlineSocket.remove(socket);
        }
    }

    /**
     * 把当前客户端发来的消息推送给所有在线socket
     * @param msg
     * @throws IOException
     */
    private void sendMsgToAllClient(String msg) throws IOException {
        for (Socket socket1 : Server.allOnlineSocket) {
            OutputStream outputStream = socket1.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.println(msg);
            printStream.flush();
        }
    }
}
