package com.youngo.msg.controller;

import com.youngo.msg.controller.handler.*;
import com.youngo.netty.SocketResultDecoder;
import com.youngo.netty.SocketResultEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Service;

/**
 * Created by fuchen on 2015/12/18.
 * 客户端服务端通信主线程
 */
@Service
public class NettyServer
{
    private static final int port = 8081;

    public void start() throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100).
                    option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).
                    handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel ch) throws Exception
                {
                    ChannelPipeline p = ch.pipeline();
                    //解码
                    p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(400 * 1024, 0, 4, -4, 0));
                    p.addLast("socketResultDecoder",  new SocketResultDecoder());
                    p.addLast("socketResultEncoder", new SocketResultEncoder());
                    //业务逻辑处理
                    p.addLast("authHandler",new AuthHandler());//登录
                    p.addLast("idleStateHandler",new IdleDisConnectHandler());
                    p.addLast("messageHandler",new MessageHandler());//消息推送,测试用
                    p.addLast("heartBeatHandler",new HeartBeatHandler());//心跳
                    p.addLast("buddyHandler",new BuddyHandler());//联系人，好友申请消息推送
                    p.addLast("serverInfoHandler",new ServerInfoHandler());//获取服务器状态信息
                    p.addLast("paperPlaneHandler",new PaperPlaneHandler());//获取服务器状态信息
                    p.addLast("kickUserHandler",new KickUserHandler());//用户踢出
                    p.addLast("warningHandler",new WarningHandler());//用户警告
                    p.addLast("exceptionHandler",new ExceptionHandler());//异常处理
                }
            });
            // Start the server.
            ChannelFuture f = b.bind(port).sync();
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally
        {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
