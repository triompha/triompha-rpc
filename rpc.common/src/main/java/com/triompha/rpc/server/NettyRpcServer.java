package com.triompha.rpc.server;

import com.triompha.rpc.context.RpcContext;
import com.triompha.rpc.server.handler.MessageDecoder;
import com.triompha.rpc.server.handler.MessageEncoder;
import com.triompha.rpc.server.handler.MessageHandler;
import com.triompha.rpc.service.HeloService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyRpcServer {
    
    public void run() throws InterruptedException{
       RpcContext.getInstance().addService(new HeloService()); 
        
       ServerBootstrap serverBootstrap = new ServerBootstrap().group(new NioEventLoopGroup(1), new NioEventLoopGroup(10)).channel(NioServerSocketChannel.class);
       serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("decoder", new MessageDecoder());
            pipeline.addLast("encoder", new MessageEncoder() );
            pipeline.addLast("message", new MessageHandler() );
            
        }});
       System.out.println("server start up"); 
       ChannelFuture sync = serverBootstrap.bind(8090).sync();
       sync.channel(); 
    }
    
    public static void main(String[] args) throws InterruptedException {
        new NettyRpcServer().run();
    }

}
