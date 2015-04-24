package com.triompha.rpc.server.handler;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.triompha.rpc.common.Content;
import com.triompha.rpc.common.HeaderNames;
import com.triompha.rpc.common.PathConverter;
import com.triompha.rpc.common.Request;
import com.triompha.rpc.common.Response;
import com.triompha.rpc.common.ServiceError;
import com.triompha.rpc.common.ServiceException;
import com.triompha.rpc.common.Response.State;
import com.triompha.rpc.common.Serializer;
import com.triompha.rpc.context.RpcContext;
import com.triompha.rpc.context.RpcContext.ServiceTone;

import static com.triompha.rpc.common.PathConverter.toServiceMethod;

public class ServiceInvoker implements Invoker {

    public static final Logger logger = LoggerFactory.getLogger(ServiceInvoker.class);

    public Response invoke(Request request) {
        
        //运行时数据
        String path = request.startLine().getPath();
        String serviceMethod = toServiceMethod(path);
        String serviceName =
                StringUtils.substringBefore(serviceMethod, PathConverter.METHOD_SEPARATOR);
        String methodName =
                StringUtils.substringAfter(serviceMethod, PathConverter.METHOD_SEPARATOR);
        ServiceTone service = RpcContext.getInstance().getService(serviceName, methodName);
        if (service == null) {
            return Response.NOT_FOND;
        }
        String contentType = request.peek(HeaderNames.CONTEN_TYPE);
        Serializer serializer = RpcContext.getInstance().getSerializer(contentType);
        Method method = service.getMethods().get(methodName);

        boolean needReturn = true;
        Class<?> returnType = method.getReturnType();
        if (returnType.equals(Void.class) || returnType.equals(void.class)) {
            needReturn = false;
        }

        Object[] params = null;
        try {
            params = serializer.deSerialize(request.body().bytes(), method.getParameterTypes());
        } catch (Exception e) {
            logger.error("deSerialize content error , request:" + request, e);
            return Response.SYSTEM_ERROR;
        }
        
        
        //反射调用方法
        Object invoke = null;
        try {
            invoke = method.invoke(service.getService(), params);
        } catch (Throwable t) {
            if (t instanceof ServiceException) {
                invoke =
                        new ServiceError(((ServiceException) t).getErrorCode(),
                                ((ServiceException) t).getErrorMessage());
            } else {
                logger.error("invoke method error , request:" + request, t);
                return Response.SYSTEM_ERROR;
            }
        }

        
        //返回结果
        if (needReturn) {
            //序列化返回参数
            byte[] rst = null;
            try {
                rst = serializer.serialize(invoke);
            } catch (Exception e) {
                logger.error("serialize error request:" + request + ",obj:" + invoke, e);
                return Response.SYSTEM_ERROR;
            }
            return new Response((invoke instanceof ServiceError) ? State.S_BIZ_ERROR
                    : State.S_SUCCESS, Content.content(rst));
        } else {
            return Response.SUCCESS;
        }

    }

}
