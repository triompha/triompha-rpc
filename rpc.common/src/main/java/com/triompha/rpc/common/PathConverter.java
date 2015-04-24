package com.triompha.rpc.common;

import org.apache.commons.lang.StringUtils;

import com.triompha.rpc.service.HeloService;

public class PathConverter {

    public static final String SLASH = "/";
    public static final String POINT = ".";
    public static final String SERVICE_SUBFIX = "Service";
    
    public static final String METHOD_SEPARATOR = "::";

    /***
     * com.triompha.rpc.service.HelloService     To  /com/triompha/rpc/service/hello 
    * @desc 
    * @author zhiyong.zzy(尽安)
    * @time  1:36:54 pm  
    * @param clazz
    * @return
     */
    public static String toPath(Class clazz) {
        String simpleName = clazz.getName();
        String path =
                StringUtils.substringBeforeLast(simpleName, POINT)
                        + POINT
                        + StringUtils.substringBeforeLast(StringUtils.uncapitalize(StringUtils
                                .substringAfterLast(simpleName, POINT)), SERVICE_SUBFIX);
        path = SLASH+StringUtils.replace(path, POINT, SLASH);
        return path;
    }
    
    /***
     * com.triompha.rpc.service.HelloService     To  hello  
    * @desc 
    * @author zhiyong.zzy(尽安)
    * @time  1:37:58 pm  
    * @param clazz
    * @return
     */
    public static String toSimplePath(Class clazz){
      return StringUtils.uncapitalize(StringUtils.substringBeforeLast(clazz.getSimpleName(), SERVICE_SUBFIX)) ; 
    }
   
    /***
     * /com/triompha/rpc/service/hello   To hello
    * @desc 
    * @author zhiyong.zzy(尽安)
    * @time  1:43:06 pm  
    * @param path
    * @return
     */
    public static String toSimplePath(String path){
       return StringUtils.substringAfterLast(path, SLASH); 
    }

    /***
     *  /com/triompha/rpc/service/hello/say  To  com.triompha.rpc.service.HelloService::say
     * 
    * @desc 
    * @author zhiyong.zzy(尽安)
    * @time  1:44:16 pm  
    * @param path
    * @return
     */
    public static String toServiceMethod(String path) {
        if (path.charAt(0) == '/') {
            path = path.substring(1);
        }
        String methodName = StringUtils.substringAfterLast(path, SLASH);
        path = StringUtils.substringBeforeLast(path, SLASH);

        return StringUtils.replace(
                StringUtils.substringBeforeLast(path, SLASH) + "/"
                        + StringUtils.capitalize(StringUtils.substringAfterLast(path, SLASH)),
                SLASH, POINT)
                + SERVICE_SUBFIX + METHOD_SEPARATOR + methodName;
    }


    public static void main(String[] args) {
        String path = toPath(HeloService.class);
        String simplePath = toSimplePath(HeloService.class);
        System.out.println(toSimplePath(path).equals(simplePath));
        String serviceP = toServiceMethod(path+"/say");
        System.out.println(serviceP);

    }
}
