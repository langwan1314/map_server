package com.youngo.mobile.controller.push;

import com.youngo.core.model.msg.ChatConstants;
import com.youngo.core.model.msg.MsgServerAddress;
import com.youngo.netty.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private MsgServerAddress address;

    public static NioEventLoopGroup group;

    private void connect()
    {
        group = new NioEventLoopGroup();
        try
        {
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
                            p.addLast("socketResultDecoder", new SocketResultDecoder());
                            p.addLast("socketResultEncoder", new SocketResultEncoder());
                            //业务逻辑处理
                            p.addLast(new ClientLoginHandler(ChatConstants.MOBILE_SERVER_NETTY_PASSPORT));
                            p.addLast(new ClientHeartBeatHandler());
                            p.addLast(new ClientBuddyHandler());
                        }
                    });
            // 连接服务器
            ChannelFuture f = bootstrap.connect(address.getPriorIP(), address.getPort());
            Channel channel = f.channel();
            NettyContext.setChannel(channel);
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e)
        {
            logger.error("nettyClient,InterruptedException",e);
        }finally
        {
            group.shutdownGracefully();
            executor.execute(new Reconnector(10000));
        }
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

}
