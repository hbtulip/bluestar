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

public interface ChaincodeEventListener {

    /**
     * 收到一个chaincode事件。ChaincodeEventListener不应该存在太长时间，因为它们会占用线程资源。
     * @param handle 处理产生此事件的链码事件监听器的句柄
     * @param jsonObject blockEvent包含链码事件的块事件信息
     */
    void received(String handle, JSONObject jsonObject, String eventName, String chaincodeId, String txId);
}
