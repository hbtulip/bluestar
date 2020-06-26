package org.hmx.utils.api.exception;

/**
 * Created by wangwei on 2018/9/14
 */
public class CustomizeException extends RuntimeException {

    private static final long serialVersionUID = 6065346873758695203L;

    private String errCode;
    private String errMsg;

    public CustomizeException() {
    }

    public CustomizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomizeException(String message) {
        super(message);
    }

    public CustomizeException(Throwable cause) {
        super(cause);
    }

    public CustomizeException(String errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {

        return this.errMsg;
    }
}
