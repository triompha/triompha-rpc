package com.triompha.rpc.common;

import java.io.IOException;
import java.lang.reflect.Type;


public interface Serializer {
    
    public Object deserialize(byte[] bytes, Type t) throws IOException, Exception ;

    public Object[] deSerialize(byte[] bytes, Type[] ts) throws IOException, Exception ;
    
    public byte[] serialize(Object obj) throws IOException, Exception ;
    
//    packWriter = new MessageWriter();
//    packWriter.write(value);
//    byte[] bytes = packWriter.toByteArray();
    
}
