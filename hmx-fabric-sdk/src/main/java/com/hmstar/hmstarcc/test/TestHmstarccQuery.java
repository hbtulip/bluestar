/**  
 * @Title:hmsdk
 * @Description:TODO
 * @author:disp     
 * @date:2018.6.1
 * @version V1.0 
 *
 */

package com.hmstar.hmstarcc.test;

import com.alibaba.fastjson.JSONObject;
import com.hmstar.hmstarcc.FabricManager;

public class TestHmstarccQuery {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FabricManager fm = FabricHelper.getInstance().createFabricManager();
		JSONObject ret = fm.query("QueryUserInfo", new String[]{"A1000"});
		System.out.println("ret: "+ret.toJSONString());

	}

}
