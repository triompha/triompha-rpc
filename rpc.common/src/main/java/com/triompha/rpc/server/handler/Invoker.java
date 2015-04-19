package com.triompha.rpc.server.handler;

import java.lang.reflect.InvocationTargetException;

import com.triompha.rpc.common.Request;
import com.triompha.rpc.common.Response;

public interface Invoker {
    public Response invoke(Request request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}
