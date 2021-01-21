package com.hcx.nio.channel;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/19 09:27
 */
public class ChannelTest {

    /**
     * 写数据到文件中
     */
    @Test
    public void writeFile() {
        try {
            //字节输出流通向目标文件
            FileOutputStream fos = new FileOutputStream("/Users/hongcaixia/a.txt");
            //得到字节输出流对应的通道channel
            FileChannel channel = fos.getChannel();
            //分配缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("hello,channel".getBytes());
            //把缓冲区切换成写模式
            buffer.flip();
            channel.write(buffer);
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中读取数据
     */
    @Test
    public void readFile() {
        try {
            //定义文件字节输入流与文件接通
            FileInputStream fis = new FileInputStream("/Users/hongcaixia/a.txt");
            //获取文件字节输入流的文件通道channel
            FileChannel channel = fis.getChannel();
            //定义缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //读取数据到缓冲区
            channel.read(buffer);
            //将缓冲区的界限设置为当前位置，并将当前位置置为 0
            buffer.flip();
            //读取缓冲区的数据
            String str = new String(buffer.array(), 0, buffer.remaining());
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件
     */
    @Test
    public void copyFile() throws Exception {
        //源文件
        File srcFile = new File("/Users/hongcaixia/Documents/image/psc.jpg");
        //目标文件
        File destFile = new File("/Users/hongcaixia/Documents/image/pscNew.jpg");
        //获取字节输入输出流
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        //获取文件通道channel
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();
        //分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            //清空缓冲区
            buffer.clear();
            //读取数据
            int read = fisChannel.read(buffer);
            if(read==-1){
                break;
            }
            //将缓冲区设置为可读模式
            buffer.flip();
            //写出数据
            fosChannel.write(buffer);
        }
        fisChannel.close();
        fosChannel.close();
    }

    /**
     * 使用分散和聚集复制文件
     */
    @Test
    public void copyFile1() throws Exception {
        //获取字节输入输出流
        FileInputStream fis = new FileInputStream("/Users/hongcaixia/a.txt");
        FileOutputStream fos = new FileOutputStream("/Users/hongcaixia/b.txt");
        //获取文件通道channel
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();
        //分散读入
        ByteBuffer buffer1 = ByteBuffer.allocate(5);
        ByteBuffer buffer2 = ByteBuffer.allocate(1024);
        ByteBuffer[] buffers = {buffer1,buffer2};
        //从通道中读取数据分散到各个缓冲区
        fisChannel.read(buffers);
        for (ByteBuffer buffer : buffers) {
            //切换到读数据模式
            buffer.flip();
        }
        //聚集写出
        fosChannel.write(buffers);
        fisChannel.close();
        fosChannel.close();
    }

    /**
     * 使用transferFrom复制文件
     */
    @Test
    public void testTransferFrom() throws Exception{
        //获取字节输入输出流
        FileInputStream fis = new FileInputStream("/Users/hongcaixia/a.txt");
        FileOutputStream fos = new FileOutputStream("/Users/hongcaixia/b.txt");
        //获取文件通道channel
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();
        //复制数据
        fosChannel.transferFrom(fisChannel,fisChannel.position(),fisChannel.size());
        fisChannel.close();
        fosChannel.close();
    }

    /**
     * 使用transferTo复制文件
     */
    @Test
    public void testTransferTo() throws Exception{
        //获取字节输入输出流
        FileInputStream fis = new FileInputStream("/Users/hongcaixia/a.txt");
        FileOutputStream fos = new FileOutputStream("/Users/hongcaixia/b.txt");
        //获取文件通道channel
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();
        //复制数据
        fisChannel.transferTo(fisChannel.position(),fisChannel.size(),fosChannel);
        fisChannel.close();
        fosChannel.close();
    }

}
