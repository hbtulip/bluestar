package org.hmx.utils.api.exception;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class ResultJson implements Serializable{

	//返回码
	private String code;
	//返回提示
	private String msg;
	//返回内容
	private Object data;
	
	public ResultJson(){		
	}
	
	public ResultJson(String msg){
		this.code = "1";
		this.msg = msg;
	}
	
	public ResultJson(Object data){
		this.code = "0";
		this.msg = "成功";
		this.data = data;
	}
	
	public ResultJson(String code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public ResultJson(String code, String msg, Object data){
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	@JSONField(serialize = false)
	public boolean isSuccess() {
		return "0".equals(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public static ResultJson getSuccessInstance(){
		return new ResultJson("0", "操作成功!", null);
	}
	
	public static ResultJson getSuccessInstance(String msg){
		return new ResultJson("0", msg, null);
	}

	public static ResultJson getSuccessInstance(Object data){
		return new ResultJson("0", "操作成功!", data);
	}
	
	public static ResultJson getSuccessInstance(String msg, Object data){
		return new ResultJson("0", msg, data);
	}
	
	public static ResultJson getFailInstance(String msg){
		return new ResultJson("1", msg, null);
	}

	public static ResultJson getFailInstance(String msg, Object data){
		return new ResultJson("1", msg, data);
	}
	
	/**
	 * TODO: 自定义返回异常信息
	 * @Auhor: RICK
	 * @Date : 2017年6月26日
	 * @param code 99 表示没有获取到经纬度信息
	 * @param msg
	 * @return
	 */
	public static ResultJson getFailInstance(String code, String msg){
		return new ResultJson(code, msg, null);
	}

	public static ResultJson getFailInstance(String code, String msg, Object data){
		return new ResultJson(code, msg, data);
	}

	public static ResultJson getCustomizeInstance(String code, String msg){
		return new ResultJson(code, msg, null);
	}

	public static ResultJson getCustomizeInstance(String code, String msg, Object data){
		return new ResultJson(code, msg, data);
	}
}
