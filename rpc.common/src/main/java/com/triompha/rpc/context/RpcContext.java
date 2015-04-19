package com.triompha.rpc.context;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class RpcContext {

    Map<String, ServiceTone> serviceMap = new HashMap<String, ServiceTone>();

    public void addService(Object service) {
        String key = service.getClass().getName();
        if (key.endsWith("service")) {
            key =StringUtils.substringBeforeLast(key, "service");
        }
        serviceMap.put(key, new ServiceTone(service));
    }

    public ServiceTone getService(String key) {
        return serviceMap.get(key);
    }

    public static class ServiceTone {
        Object service;
        Map<String, Method> methods = new HashMap<>();

        public ServiceTone(Object service) {
            this.service = service;
            Method[] declaredMethods = service.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                methods.put(method.getName(), method);
            }
        }

        /**
         * @return the service
         */
        public Object getService() {
            return service;
        }

        /**
         * @return the methods
         */
        public Map<String, Method> getMethods() {
            return methods;
        }



    }

    private static RpcContext instance = new RpcContext();

    private RpcContext() {}

    public static RpcContext getInstance() {
        return instance;
    }
}
