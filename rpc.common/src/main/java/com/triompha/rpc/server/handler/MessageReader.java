package com.triompha.rpc.server.handler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.triompha.rpc.common.Content;
import com.triompha.rpc.common.ListMaps;
import com.triompha.rpc.common.Message;
import com.triompha.rpc.common.Request;
import com.triompha.rpc.common.Response;

import static com.triompha.rpc.common.Content.content;
import static java.util.Collections.singletonList;
import  static com.triompha.rpc.server.handler.TRPC.*;

public class MessageReader {

    private static final Map<String, List<String>> NO_HEADER = Collections.emptyMap();

    private State state = new Ready();

    public void read(Readable in, Callback callback) throws IOException {
        for (State s = state.read(in); ; s = s.read(in)) {

            if (s instanceof Break) {
                state = s.read(in);
                return;
            }

            if (s instanceof Done) {
                if (!callback.on(s.message())) return;
            }

        }
    }

    protected boolean isContentLength(String name) {
        return CONTENT_LENGTH_HEADER_NAME.equals(name);
    }

    protected String magic() {
        return MAGIC;
    }

    /***/
    public interface Callback {
        /** Return true means continue read, or break. */
        boolean on(Message<?, ?> message);

        /** Return true means continue read, or break. */
        boolean onHeartbeat();
    }

    /***/
    public interface Readable {
        String readLine() throws IOException;

        byte[] readBytes(int length) throws IOException;
    }

    /***/
    abstract class State {
        abstract State read(Readable in) throws IOException;

        Message<?, ?> message() {return null;}
    }

    /***/
    class Ready extends State {
        @Override
        State read(Readable in) throws IOException {
            String line = in.readLine();
            if (line == null) return new Break(this);
            return new ReadHeaders(line.startsWith(magic()) ?
                                  new Request.Path(line.substring(magic().length() + 1, line.length())) :
                                  new Response.State(Integer.parseInt(line)));
        }

    }


    /***/
    class ReadHeaders extends State {
        final Object                    startLine;
        final Map<String, List<String>> headers;
        final int                       contentLength;

        ReadHeaders(Object startLine) {
            this(startLine, NO_HEADER, 0);
        }

        ReadHeaders(Object startLine, Map<String, List<String>> headers, int contentLength) {
            this.startLine = startLine;
            this.headers = headers;
            this.contentLength = contentLength;
        }

        @Override
        State read(Readable in) throws IOException {
            String line = in.readLine();

            if (line == null) return new Break(this);

            if (line.length() == 0) {
                return contentLength > 0 ? new ReadBody(startLine, headers, contentLength) : new Done(startLine, headers);
            }

            final int i = line.indexOf(':');
            if (i == line.length() - 1) return this;
            String name = line.substring(0, i).trim();
            String value = (i == line.length() - 1) ? "" : line.substring(i + 1).trim();

            if (isContentLength(name)) {
                return new ReadHeaders(startLine, headers, Integer.parseInt(value));
            } else {
                return new ReadHeaders(startLine, concat(headers, name, value), contentLength);
            }
        }

        private Map<String, List<String>> concat(Map<String, List<String>> headers, String name, String value) {
            return ListMaps.put(ListMaps.newListMap(headers), name, ListMaps.append(headers.get(name), singletonList(value)));
        }
    }


    /***/
    class ReadBody extends State {
        final Object                    startLine;
        final Map<String, List<String>> headers;
        final int                       contentLength;

        ReadBody(Object startLine, Map<String, List<String>> headers, int contentLength) {
            this.startLine = startLine;
            this.headers = headers;
            this.contentLength = contentLength;
        }

        @Override
        State read(Readable in) throws IOException {
            byte[] body = in.readBytes(contentLength);
            if (body == null) return new Break(this);
            else return new Done(startLine, headers, content(body));
        }
    }

    /***/
    class Done extends State {
        final Object                    startLine;
        final Map<String, List<String>> headers;
        final Content                   body;

        Done(Object startLine, Map<String, List<String>> headers, Content body) {
            this.startLine = startLine;
            this.headers = headers;
            this.body = body;
        }

        public Done(Object startLine, Map<String, List<String>> headers) {
            this(startLine, headers, Content.NO_CONTENT);
        }

        @Override
        State read(Readable in) throws IOException {return new Ready().read(in);}

        @Override
        Message<?, ?> message() {
            if (startLine instanceof Request.Path) {
                return new Request((Request.Path) startLine, headers, body);
            } else {
                return new Response((Response.State) startLine, headers, body);
            }
        }
    }

    /***/
    class Break extends State {
        final State last;

        public Break(State state) { last = state; }

        @Override
        State read(Readable in) {return last;}
    }

}