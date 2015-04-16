package com.triompha.rpc.register;


import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZookeeperRegister implements Register {

    public static final Logger logger = LoggerFactory.getLogger(ZookeeperRegister.class);

    ZkClient zkClient = new ZkClient("127.0.0.1:2181");

    public void regist(RegistConfig config) {
        try {
            zkClient.createPersistent(config.getUri(), true);
        } catch (Exception e) {
            logger.warn("regist service error:" + config, e);
        }
        try {
            zkClient.createEphemeral(config.getUri() + "/" + config.getHost(), config.getParams());
        } catch (Exception e) {
            logger.warn("regist host error:" + config, e);
        }
    }

    public List<RegistConfig> getRegistries(String uri) {
        List<RegistConfig> rst = new ArrayList<>();
        List<String> childrens = zkClient.getChildren(uri);
        if (childrens != null) {
            for (String s : childrens) {
                rst.add(new RegistConfig(uri, s, (String) zkClient.readData(uri + "/" + s)));
            }
        }
        return rst;
    }
    
    public void unRegist(RegistConfig config){
        try {
            zkClient.delete(config.getUri() + "/" + config.getHost());
        } catch (Exception e) {
            logger.warn("unRegist error:" + config, e);
        }
    }


    public static void main(String[] args) {
        Register register = new ZookeeperRegister();
        String service = "/com/triompha/rpc/xxx";
        RegistConfig config = new RegistConfig(service, "1.2.3.4:2333", null);
        register.regist(config);
        System.out.println(register.getRegistries(config.getUri()));
    }

}
