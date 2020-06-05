package org.hmx.utils.api.vo;

import java.io.Serializable;

public class DemoResultVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long code;
	private String msg;
	/**
	 * @return the code
	 */
	public long getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(long code) {
		this.code = code;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
