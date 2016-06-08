package com.youngo.msg;

import com.youngo.core.model.msg.MsgServerAddress;
import com.youngo.msg.handler.ClientExceptionHandler;
import com.youngo.msg.handler.ClientLoginHandler;
import com.youngo.msg.handler.ClientServerInfoHandler;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fuchen on 2016/2/15.
 */
public class ServerInfoGetter
{
    private final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private MsgServerAddress address;

    public static NioEventLoopGroup group;

    public ServerInfoGetter()
    {
        address = new MsgServerAddress();
        address.setPriorIP("localhost");
//        address.setPriorIP("47.89.28.120");
//        address.setPriorIP("121.42.32.149");
//        address.setPriorIP("47.89.49.135");//prod
        address.setPort(8081);
    }

    private void connect()
    {
        group = new NioEventLoopGroup();
        try
        {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception
                        {
                            ChannelPipeline p = ch.pipeline();
                            //解码
                            p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(400 * 1024, 0, 4, -4, 0));
                            p.addLast(new SocketResultDecoder());
                            p.addLast(new SocketResultEncoder());
                            //业务逻辑处理
                            p.addLast(new ClientLoginHandler());
                            p.addLast(new ClientServerInfoHandler());
                            p.addLast(new ClientExceptionHandler());
                        }
                    });
            // 连接服务器
            ChannelFuture f = bootstrap.connect(address.getPriorIP(), address.getPort());
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e)
        {
            logger.error("nettyClient,InterruptedException",e);
        }finally
        {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args)
    {
        ServerInfoGetter nettyClient = new ServerInfoGetter();
        nettyClient.connect();
    }
}
