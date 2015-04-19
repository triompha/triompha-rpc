package com.triompha.rpc.server.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

import com.triompha.rpc.common.Content;
import com.triompha.rpc.common.Request;
import com.triompha.rpc.common.Response;
import com.triompha.rpc.common.Response.State;
import com.triompha.rpc.context.RpcContext;
import com.triompha.rpc.context.RpcContext.ServiceTone;

public class ServiceInvoker implements Invoker{
    
    public static final Response NOT_FOND = new Response(404);

    public Response invoke(Request request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
       String path = request.startLine().getPath(); 
       String simpleClass = StringUtils.substringBeforeLast(path, "/");
       String methodS = StringUtils.substringAfter(path, simpleClass+"/");
       simpleClass =  StringUtils.replace(StringUtils.replaceOnce(simpleClass, "/", ""), "/", ".")+"Service";
       ServiceTone service = RpcContext.getInstance().getService(simpleClass); 
       if(service==null){
           return NOT_FOND;
       }
       if(service.getMethods().get(methodS)==null){
           return NOT_FOND;
       }
       String param = new String( request.body().bytes());
       
       Method method = service.getMethods().get(methodS); 
       Object invoke = method.invoke(service.getService(), param);
        return new Response(new State(200), Content.content(invoke.toString())); 
    }

}
