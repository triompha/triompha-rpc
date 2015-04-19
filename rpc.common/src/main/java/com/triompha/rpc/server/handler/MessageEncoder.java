package com.triompha.rpc.server.handler;


import java.io.UnsupportedEncodingException;

import com.triompha.rpc.common.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message<?, ?>>{
    
    private MessageWriter writer = new MessageWriter();

    @Override
    protected void encode(ChannelHandlerContext ctx, Message<?, ?> msg, final ByteBuf out)
            throws Exception {
        writer.write(msg, new MessageWriter.Writable() {
            @Override
            public void write(String value) throws UnsupportedEncodingException {
                out.writeBytes(value.getBytes("UTF-8"));
            }

            @Override
            public void write(char value) {
                out.writeByte(value);
            }

            @Override
            public void write(byte[] value) {
                out.writeBytes(value);
            }
        });
    } 

   

}
