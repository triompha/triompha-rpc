package com.triompha.rpc.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RpcSpringContextListener implements ApplicationContextAware, InitializingBean, DisposableBean {

    public void destroy() throws Exception {
        // TODO Auto-generated method stub
        
    }

    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // TODO Auto-generated method stub
        
    }

}
