package org.hmx.utils.api.enums;

/** 
 * @Description: TODO
 * @author: disp
 * @date: 2020年2月20日  
 */
public enum DivDetailStatusEnum {
	CREATED(200, "记录创建成功"),
	ACCEPT(210, "分账登记受理中"),
	SUCCESS(220, "分账成功"),	
	FAILURE(230, "分账失败");	
	
    private int code;
    private String name;

    DivDetailStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
