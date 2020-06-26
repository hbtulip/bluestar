/**  
 * @Title:hmsdk
 * @Description:TODO
 * @author:disp     
 * @date:2018.6.1
 * @version V1.0 
 *
 */

package com.hmstar.hmstarcc;


class HmPeer {

    private String peerName; 				// peer0.org1.example.com
    private String peerLocation; 			// grpc://xxx:7051
    private String peerEventHubLocation; 	// grpc://xxx:7053

    private String serverCrtPath;
    private String clientCertPath;
    private String clientKeyPath;

    /**
     * 初始化中继Peer对象
     *
     * @param peerName             当前指定的组织节点域名
     * @param peerLocation         当前指定的组织节点访问地址
     * @param peerEventHubLocation 当前指定的组织节点事件监听访问地址
     */
    HmPeer(String peerName, String peerLocation, String peerEventHubLocation, String serverCrtPath, String clientCertPath, String clientKeyPath) {
        this.peerName = peerName;
        this.peerLocation = peerLocation;
        this.peerEventHubLocation = peerEventHubLocation;
        this.serverCrtPath = serverCrtPath;
        this.clientCertPath = clientCertPath;
        this.clientKeyPath = clientKeyPath;
    }

    /**
     * 设置组织节点访问地址
     *
     * @param peerLocation 组织节点访问地址
     */
    void setPeerLocation(String peerLocation) {
        this.peerLocation = peerLocation;
    }

    /**
     * 设置组织节点事件监听访问地址
     *
     * @param peerEventHubLocation 组织节点事件监听访问地址
     */
    void setPeerEventHubLocation(String peerEventHubLocation) {
        this.peerEventHubLocation = peerEventHubLocation;
    }

    String getPeerName() {
        return peerName;
    }


    String getPeerLocation() {
        return peerLocation;
    }

    String getPeerEventHubLocation() {
        return peerEventHubLocation;
    }

    String getServerCrtPath() {
        return serverCrtPath;
    }

    public String getClientCertPath() {
        return clientCertPath;
    }

    public String getClientKeyPath() {
        return clientKeyPath;
    }
}
