package com.triompha.rpc.common;

import java.util.List;
import java.util.Map;

public class Response extends Message<Response, Response.State>{
    
    public static final Response NOT_FOND = new Response(State.S_NOT_FOND);
    public static final Response SUCCESS = new Response(State.S_SUCCESS);
    public static final Response SYSTEM_ERROR= new Response(State.S_SYSTEM_ERRORE);
    
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
       public static final State S_NOT_FOND = new State(404); 
       public static final State S_SUCCESS = new State(200);
       public static final State S_BIZ_ERROR = new State(201);
       public static final State S_SYSTEM_ERRORE = new State(500);
       public static final State S_FORBIDEN = new State(401);
        
        
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
