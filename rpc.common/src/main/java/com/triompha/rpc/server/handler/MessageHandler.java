package com.triompha.rpc.server.handler;

import com.triompha.rpc.common.Request;
import com.triompha.rpc.common.Response;
import com.triompha.rpc.common.Serializer;
import com.triompha.rpc.context.RpcContext;
import com.triompha.rpc.service.HeloService.HeloResult;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class MessageHandler extends ChannelDuplexHandler {
    
    public static Invoker invoker = new ServiceInvoker();

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Response) {
            Response response = (Response) msg;
            System.out.println("recived response:"+ response);
             
            Serializer serializer = RpcContext.getInstance().getSerializer("lwp");
            
            System.out.println("recived response:"+ serializer.deserialize(response.body().bytes(), HeloResult.class));
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
