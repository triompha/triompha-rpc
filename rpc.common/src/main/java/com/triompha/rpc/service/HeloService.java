package com.triompha.rpc.service;

import com.laiwang.idl.FieldId;
import com.laiwang.idl.Marshal;

public class HeloService {

    public HeloResult say(HeloMessage message){
        HeloResult result = new HeloResult();
        result.message = "hello "+ message.name +" , i recived msg:"+ message.toString();
       return    result;
    }
    
    
    
    
    public static class HeloResult implements Marshal{
        @FieldId(1)
        public String message;

        @Override
        public String toString() {
            return "HeloResult [message=" + message + "]";
        }

        public void decode(int idx, Object value) {
           switch (idx) {
            case 1:
                this.message = (String) value;
                break;
            default:
                break;
        } 
        }
        
    }
    
    public static class HeloMessage implements Marshal{
        @FieldId(1)
        public String name;
        @FieldId(2)
        public Integer age;
        @FieldId(3)
        public int sex;
        
        

        public HeloMessage() {
            super();
        }

        public HeloMessage(String name, Integer age, int sex) {
            super();
            this.name = name;
            this.age = age;
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "HeloMessage [name=" + name + ", age=" + age + ", sex=" + sex + "]";
        }

        public void decode(int idx, Object value) {
            switch (idx) {
                case 1:
                    this.name = (String) value;
                    break;
                case 2:
                    this.age=  (Integer) value;
                    break;
                case 3:
                    this.sex=  (int) value;
                    break;

                default:
                    break;
            }
        }
        
    }
    
    
}
