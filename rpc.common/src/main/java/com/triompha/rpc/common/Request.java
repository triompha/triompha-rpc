package com.triompha.rpc.common;

import java.util.List;
import java.util.Map;

public class Request extends Message<Request, Request.Path> {
    public Request(Path startLine, Map<String, List<String>> headers, Content body) {
        super(startLine, headers, body);
    }
    

    public static class Path{
        private String path;
        /**
         * @return the path
         */
        public String getPath() {
            return path;
        }

        /**
         * @param path the path to set
         */
        public void setPath(String path) {
            this.path = path;
        }

        public Path(String path){
            this.path = path;
        }
        @Override
        public String toString() {
            return this.path;
        }
    }

    @Override
    protected Request copy(Path startLine, Map<String, List<String>> headers, Content body) {
        return null;
    }

}
