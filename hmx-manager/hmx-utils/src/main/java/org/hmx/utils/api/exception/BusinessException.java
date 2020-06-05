package org.hmx.utils.api.exception;

public class BusinessException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String code;
	
	public BusinessException() {
	}

	public BusinessException(String message) {
		super(message);
	}
	
	public BusinessException(String code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(String message, Exception e) {
		super(message, e);
	}
	
	public BusinessException(String code, String message, Exception e) {
		super(message, e);
		this.code = code;
	}
	
	/**
	 * 获取错误信息
	 * @param e
	 * @param message
	 * @return
	 */
	public static String getMessage(Exception e,String message){
		String tempMessage = "";
		if(e instanceof BusinessException){
			tempMessage = e.getMessage();
		}
		if(!"".equals(tempMessage)){
			message = tempMessage;
		}
		return message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
