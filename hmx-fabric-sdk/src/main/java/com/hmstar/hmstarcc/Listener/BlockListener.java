/**  
 * @Title:hmsdk
 * @Description:TODO
 * @author:disp     
 * @date:2018.6.1
 * @version V1.0 
 *
 */

package com.hmstar.hmstarcc.Listener;

import com.alibaba.fastjson.JSONObject;

/**
 * 描述：BlockListener监听返回map集合
 *
 */
public interface BlockListener {

    int SUCCESS = 200;
    int ERROR = 500;

    void received(JSONObject jsonObject);
}
