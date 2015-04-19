package com.triompha.rpc.server.handler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.triompha.rpc.common.Message;
import com.triompha.rpc.common.Request;

import  static com.triompha.rpc.server.handler.TRPC.*;

public class MessageWriter {


    public void write(Message<?, ?> message, Writable writable) throws IOException {
        if (message instanceof Request) writable.write(MAGIC + ' ');
        writable.write(message.startLine().toString());
        writable.write(LF);

        for (Map.Entry<String, List<String>> header : message.headers().entrySet()) {
            final String name = header.getKey();
            final List<String> values = header.getValue();
            for (String value : values) {
                writable.write(name);
                writable.write(COL);
                writable.write(value);
                writable.write(LF);
            }
        }

        final byte[] body = message.body().bytes();

        if (body.length > 0) {
            writable.write(CONTENT_LENGTH_HEADER_NAME);
            writable.write(COL);
            writable.write(String.valueOf(body.length));
            writable.write(LF);
            writable.write(LF);
            writable.write(body);
        }

        writable.write(LF);
    }

    public void heartbeat(Writable writable) throws IOException {
        writable.write(LF);
    }

    public interface Writable {
        void write(String value) throws IOException;

        void write(char value) throws IOException;

        void write(byte[] value) throws IOException;
    }

}
