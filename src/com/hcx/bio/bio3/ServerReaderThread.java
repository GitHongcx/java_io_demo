package com.hcx.bio.bio3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/14 11:00
 */
public class ServerReaderThread extends Thread {

    private Socket socket;

    public ServerReaderThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String msg;
            while ((msg = bufferedReader.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
