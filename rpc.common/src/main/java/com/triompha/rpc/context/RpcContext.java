package com.triompha.rpc.context;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.triompha.rpc.common.HeaderNames;
import com.triompha.rpc.common.MessagePackSerializer;
import com.triompha.rpc.common.Serializer;

public class RpcContext {

    Map<String, ServiceTone> serviceMap = new HashMap<String, ServiceTone>();
    
    Map<String, Serializer> serializers = new HashMap<>();
    
    private boolean allowShortURI = true;
    
    
    public Serializer getSerializer(String type){
        Serializer serializer = serializers.get(type);
        if(serializer==null){
            return serializers.get(HeaderNames.DefaultHeaderValues.CT_MP);
        }
       return  serializer;
    }

    public void addService(Object service) {
        String key = service.getClass().getName();
        if (key.endsWith("service")) {
            key =StringUtils.substringBeforeLast(key, "service");
        }
        serviceMap.put(key, new ServiceTone(service));
    }
    
    public ServiceTone searchSubService(String serviceName){
       for(Entry<String, ServiceTone> entry : serviceMap.entrySet()){
          if(entry.getKey().contains(serviceName)){
              return entry.getValue();
          }
       }
       return null;
    }

    public ServiceTone getService(String serviceName,String methodName) {
        ServiceTone tone = serviceMap.get(serviceName);
        if(tone==null && allowShortURI){
            tone = searchSubService(serviceName);
            if(tone  == null){
                return null;
            }
        }
        if(!tone.getMethods().containsKey(methodName))
            return null;
        return tone;
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

    private RpcContext() {
        serializers.put(HeaderNames.DefaultHeaderValues.CT_MP, new MessagePackSerializer());
    }

    public static RpcContext getInstance() {
        return instance;
    }
}
