package com.triompha.rpc.common;

public class ServiceException extends Exception{
    private static final long serialVersionUID = 2411947277135984478L;
    private String            errorCode;

    public ServiceException(String message){
        super(message);
    }

    public ServiceException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(String msg, Throwable cause){
        super(msg, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage(){
       return this.getMessage(); 
    }
   
}
