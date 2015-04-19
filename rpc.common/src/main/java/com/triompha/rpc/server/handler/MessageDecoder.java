package com.triompha.rpc.server.handler;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.triompha.rpc.common.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {

    private final MessageReader reader = new MessageReader();
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, final List<Object> out) throws Exception {
        reader.read(new ByteBufReadable(in), new Callback(out));
    }

    static class ByteBufReadable implements MessageReader.Readable {
        
        static final byte LF = '\n';

        private final ByteBuf in;

        public ByteBufReadable(ByteBuf in) {this.in = in;}

        @Override
        public final String readLine() {
            final int length = in.bytesBefore(LF);
            if (length < 0) {
                readPendingLine(in.readableBytes());
                return null;
            }

            if (length == 0) {
                in.skipBytes(1);
                return "";
            }

            readMore(length + 1);

            final byte[] bytes = new byte[length];
            in.readBytes(bytes);
            in.skipBytes(1);
            try {
                return new String(bytes, "UTF8").trim(); // trim CR
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public final byte[] readBytes(int length) {
            readMore(length);

            if (in.readableBytes() < length) return null;
            final byte[] bytes = new byte[length];
            in.readBytes(bytes);
            return bytes;
        }

        protected void readMore(int length) { }

        protected void readPendingLine(int length) { }
    }
    
    public static class Callback implements MessageReader.Callback{
        
        final List<Object> out; 
        public Callback(List<Object> out){
           this.out = out; 
        }

        @Override
        public boolean on(Message<?, ?> message) {
            out.add(message);
            return true;
        }

        @Override
        public boolean onHeartbeat() {
            return true;
        }
    
    }

}
