package com.youngo.msg;

import com.youngo.core.model.msg.MsgServerAddress;
import com.youngo.msg.handler.ClientExceptionHandler;
import com.youngo.msg.handler.ClientHeartBeatHandler;
import com.youngo.msg.handler.ClientLoginHandler;
import com.youngo.msg.handler.RandomMsgHandler;
import com.youngo.netty.SocketResultDecoder;
import com.youngo.netty.SocketResultEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by fuchen on 2016/1/25.
 * netty客户端<br>
 * 连接netty服务器，通信异常自动重连
 */
@Service
public class NettyClient implements InitializingBean
{
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private MsgServerAddress address;
    public static AtomicLong receiveMsgCount = new AtomicLong();

    public static NioEventLoopGroup group;

    public NettyClient()
    {
        address = new MsgServerAddress();
        address.setPriorIP("192.168.10.50");
//        address.setPriorIP("47.89.28.120");
//        address.setPriorIP("121.42.32.149");
        address.setPort(8081);
    }

    public void connect()
    {
        group = new NioEventLoopGroup();
//        try
//        {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            //解码
                            p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(400 * 1024, 0, 4, -4, 0));
                            p.addLast(new SocketResultDecoder());
                            p.addLast(new SocketResultEncoder());
                            //业务逻辑处理
                            p.addLast(new ClientLoginHandler());
//                            p.addLast(new ClientHeartBeatHandler());
                            p.addLast(new RandomMsgHandler());
                            p.addLast(new ClientExceptionHandler());
                        }
                    });
            // 连接服务器
            ChannelFuture f = bootstrap.connect(address.getPriorIP(), address.getPort());
            // Wait until the server socket is closed.
            f.channel().closeFuture().addListener(new GenericFutureListener()
            {
                @Override
                public void operationComplete(Future future) throws Exception
                {
                    group.shutdownGracefully();
                    executor.execute(new Reconnector(10000));
                }
            });
//        }
//        catch (InterruptedException e)
//        {
//            logger.error("nettyClient,InterruptedException",e);
//            group.shutdownGracefully();
//        }
//        finally
//        {
//            group.shutdownGracefully();
//            executor.execute(new Reconnector(10000));
//        }
    }

    private class Reconnector implements Runnable
    {
        private long delay;

        private Reconnector(long delay)
        {
            this.delay = delay;
        }
        @Override
        public void run()
        {
            try
            {
                Thread.sleep(delay);
                connect();
            } catch (InterruptedException e)
            {
                logger.error("reConnectDelay,InterruptedException", e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        executor.execute(new Reconnector(2000));
    }

    public static void shutdown()
    {
        group.shutdownGracefully(1,1,TimeUnit.MICROSECONDS);
        executor.shutdownNow();
    }

    public static void main(String[] args)
    {
        NettyClient nettyClient = new NettyClient();
        nettyClient.connect();
    }

}
