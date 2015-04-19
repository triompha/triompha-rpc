package com.triompha.rpc.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.triompha.rpc.common.Content;
import com.triompha.rpc.common.Request;
import com.triompha.rpc.server.handler.MessageDecoder;
import com.triompha.rpc.server.handler.MessageEncoder;
import com.triompha.rpc.server.handler.MessageHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyRpcClient {
   public void run() throws InterruptedException{
      Bootstrap bootstrap = new Bootstrap().channel(NioSocketChannel.class).group(new NioEventLoopGroup(1));
      bootstrap.handler(new ChannelInitializer<Channel>() {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("decoder", new MessageDecoder());
            pipeline.addLast("encoder", new MessageEncoder() );
            pipeline.addLast("message", new MessageHandler() );
        }});
      
      ChannelFuture future = bootstrap.connect("127.0.0.1", 8090) ;
      Thread.currentThread().sleep(300);
     Map<String, List<String>> headers  = new HashMap<String, List<String>>();
      List<String> xxb = new ArrayList<>();
      xxb.add("head xxb");
      headers.put("xxb", xxb);
      Request request = new Request(new Request.Path("/com/triompha/rpc/service/Helo/say"),headers, Content.content("你好吗?") );
      Channel channel = future.channel();      
      ChannelFuture writeAndFlush = channel.writeAndFlush(request);
      writeAndFlush.sync(); 
      System.out.println("client start up");
         }
   public static void main(String[] args) throws InterruptedException {
       new NettyRpcClient().run();
       Thread.currentThread().sleep(10000);
}

}
