package com.triompha.rpc.server.handler;


import com.triompha.rpc.common.Request;
import com.triompha.rpc.common.Response;

public interface Invoker {
    public Response invoke(Request request);
}
