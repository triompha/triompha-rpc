package com.triompha.rpc.server.handler;

import com.triompha.rpc.common.Request;
import com.triompha.rpc.common.Response;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class MessageHandler extends ChannelDuplexHandler {
    
    public static Invoker invoker = new ServiceInvoker();

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Response) {
            Response response = (Response) msg;
            System.out.println("recived response:"+ response);
            System.out.println("recived response:"+ new String(response.body().bytes()));
            return;
        }

        //收到request 的时候给个应答
        if (msg instanceof Request) {
            final Request request = (Request) msg;
            System.out.println("recived request:"+ request);
            Response response = invoker.invoke(request);
            ctx.writeAndFlush(response);
        }

    }
}
