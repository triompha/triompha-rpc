package com.triompha.rpc.register;

public class RegistConfig {
    /***
     * 注册的服务的域名的整体uri 如 com.triompha.biz,AService， 则，uri ： com/triompha/biz/A
     */
    private String uri;
    // 注册的ip地址
    private String host;
    // 注册的参数信息
    private String params;

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the params
     */
    public String getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "RegistConfig [uri=" + uri + ", host=" + host + ",  params=" + params + "]";
    }
    
    

    public RegistConfig() {
        super();
    }

    public RegistConfig(String uri, String host, String params) {
        super();
        this.uri = uri;
        this.host = host;
        this.params = params;
    }


}
