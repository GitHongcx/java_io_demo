package com.hcx.nio.buffer;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * buffer常用api
 *
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/18 16:32
 */
public class BufferTest {

    @Test
    public void test01() {
        //分配一个缓冲区，容量为10
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println("缓冲区当前位置：" + buffer.position());//0
        System.out.println("缓冲区界限：" + buffer.limit());//10
        System.out.println("缓冲区容量：" + buffer.capacity());//10

        System.out.println("==============");
        //往缓冲区添加数据
        String name = "hongcx";
        buffer.put(name.getBytes());
        System.out.println("缓冲区当前位置：" + buffer.position());//6
        System.out.println("缓冲区界限：" + buffer.limit());//10
        System.out.println("缓冲区容量：" + buffer.capacity());//10

        System.out.println("==============");
        //为将缓冲区的界限设置为当前位置，并将当前位置置为0 切换为可读模式
        buffer.flip();
        System.out.println("缓冲区当前位置：" + buffer.position());//0
        //前6个位置可读
        System.out.println("缓冲区界限：" + buffer.limit());//6
        System.out.println("缓冲区容量：" + buffer.capacity());//10

        System.out.println("==============");
        //使用get读取数据
        char c = (char) buffer.get();
        System.out.println("读取到的字符是：" + c);//h
        System.out.println("缓冲区当前位置：" + buffer.position());//1
        System.out.println("缓冲区界限：" + buffer.limit());//6
        System.out.println("缓冲区容量：" + buffer.capacity());//10
    }

    @Test
    public void test02() {
        //clear
        ByteBuffer buffer = ByteBuffer.allocate(10);
        String name = "hongcx";
        buffer.put(name.getBytes());
        //清楚缓冲区中的数据 只是把position变成了0，数据还存在，直到后续添加了新的数据 才会覆盖掉
        buffer.clear();
        System.out.println(buffer.position());//0
        System.out.println((char) buffer.get());//h

        System.out.println("=======================");
        //flip
        ByteBuffer buffer2 = ByteBuffer.allocate(10);
        String name2 = "hongcx";
        buffer2.put(name2.getBytes());
        buffer2.flip();
        byte[] bytes = new byte[2];
        buffer2.get(bytes);
        String str = new String(bytes);
        System.out.println(str);//ho
        System.out.println("缓冲区当前位置：" + buffer2.position());//2
        System.out.println("缓冲区界限：" + buffer2.limit());//6
        System.out.println("缓冲区容量：" + buffer2.capacity());//10

        System.out.println("=======================");
        //标记此刻的位置 2 标记之后 后续可以回到此处
        buffer2.mark();
        byte[] bytes1 = new byte[3];
        buffer2.get(bytes1);
        System.out.println(new String(bytes1));//ngc
        System.out.println("缓冲区当前位置：" + buffer2.position());//5
        System.out.println("缓冲区界限：" + buffer2.limit());//6
        System.out.println("缓冲区容量：" + buffer2.capacity());//10

        //reset 回到标记位置
        buffer2.reset();
        if(buffer2.hasRemaining()){
            System.out.println(buffer2.remaining());
        }
    }

    @Test
    public void test03(){
        //创建一个直接内存缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        //判断是否是直接内存
        System.out.println(buffer.isDirect());
    }
}
