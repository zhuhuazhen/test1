package com.cube.exception;

/**
 * http内部服务错误
 * 
 * @description ServerWrongException
 * @author tengyz
 * @version 0.1
 * @date 2014年8月19日
 */
public class RespException extends RuntimeException {

    private static final long serialVersionUID = -3910639417037386503L;

    public RespException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public RespException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super();
    }

    public RespException(String message, Throwable cause) {
        super(message, cause);
    }

    public RespException(String message) {
        super(message);
    }

    public RespException(Throwable cause) {
        super(cause);
    }

}
