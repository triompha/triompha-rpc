package com.triompha.rpc.common;

import java.util.List;
import java.util.Map;

public class Response extends Message<Response, Response.State>{
    
    public Response(State startLine, Map<String, List<String>> headers, Content body) {
        super(startLine, headers, body);
    }
    
    public Response(State startLine, Content body) {
        super(startLine,  body);
    }
    
    public Response(State startLine){
       super(startLine, Content.NO_CONTENT); 
    }
    
    public Response(int code){
       super(new State(code), Content.NO_CONTENT); 
    }

    public static class State{
        int stateCode;
        public State(int state){
            this.stateCode = state;
        }
        public int state(){
           return this.stateCode; 
        }
        @Override
        public String toString() {
            return stateCode+"";
        }
        
    }

    @Override
    protected Response copy(State startLine, Map<String, List<String>> headers, Content body) {
        return null;
    }



}
