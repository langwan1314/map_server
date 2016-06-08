package com.youngo.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fuchen on 2016/2/16.
 * 压力测试
 */
public class PTest
{
    private static final Logger logger = LoggerFactory.getLogger(PTest.class);
    private static final int count = 2500;
    private static AtomicInteger _count= new AtomicInteger();
    public static void main(String[] args) throws InterruptedException {
        logger.debug(" starting..... ");
        for(int i=0;i<count;i++)
        {
            NettyClient client = new NettyClient();
//            Thread.sleep(100);
            client.connect();
            logger.error("create client: " + _count.incrementAndGet());
//            new InnerTester().start();
        }
    }

    public static class InnerTester extends Thread
    {
        @Override
        public void run()
        {
//            NettyClient client = new NettyClient();
//            client.connect();
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
