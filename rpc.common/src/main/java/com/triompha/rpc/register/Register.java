package com.triompha.rpc.register;

import java.util.List;

public interface Register {
    public void regist(RegistConfig config) ;
    public List<RegistConfig> getRegistries(String uri); 
}
