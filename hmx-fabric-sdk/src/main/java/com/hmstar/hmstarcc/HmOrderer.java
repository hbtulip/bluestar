/**  
 * @Title:hmsdk
 * @Description:TODO
 * @author:disp     
 * @date:2018.6.1
 * @version V1.0 
 *
 */

package com.hmstar.hmstarcc;

class HmOrderer {

    private String ordererName;		//域名
    private String ordererLocation;	//地址
    private String serverCrtPath;	//tls证书
    private String clientCertPath;
    private String clientKeyPath;

    HmOrderer(String ordererName, String ordererLocation, String serverCrtPath, String clientCertPath, String clientKeyPath) {
        super();
        this.ordererName = ordererName;
        this.ordererLocation = ordererLocation;
        this.serverCrtPath = serverCrtPath;
        this.clientCertPath = clientCertPath;
        this.clientKeyPath = clientKeyPath;
    }

    String getOrdererName() {
        return ordererName;
    }

    void setOrdererLocation(String ordererLocation) {
        this.ordererLocation = ordererLocation;
    }

    String getOrdererLocation() {
        return ordererLocation;
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
