/**  
 * @Title:hmsdk
 * @Description:TODO
 * @author:disp     
 * @date:2018.6.1
 * @version V1.0 
 *
 */

package com.hmstar.hmstarcc;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import com.hmstar.hmstarcc.Listener.BlockListener;
import com.hmstar.hmstarcc.Listener.ChaincodeEventListener;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class HmOrg {

    //private Logger log = LogManager.getLogger(HmOrg.class);
    private static Logger log = Logger.getLogger(HmOrg.class);
    
    // 组织属性
    private String username;
    private List<HmOrderer> orderers = new LinkedList<>();
    private String orgMSPID;
    private List<HmPeer> peers = new LinkedList<>();
    private boolean openTLS;
    private HmChannel channel;
    private HmChaincode chaincode;
    private String eventNames;
    
    // 事件监听 
    private BlockListener blockListener;
    private ChaincodeEventListener chaincodeEventListener;
    private HFClient client;
    private Map<String, User> userMap = new HashMap<>();
    private HmUserStore fabricStore;

    HmUserStore getFabricStore() {
        return fabricStore;
    }

    void setFabricStore(HmUserStore fabricStore) {
        this.fabricStore = fabricStore;
    }

    void setUsername(String username) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }

    void addOrderer(String name, String location, String serverCrtPath, String clientCertPath, String clientKeyPath) {
        orderers.add(new HmOrderer(name, location, serverCrtPath, clientCertPath, clientKeyPath));
    }

    List<HmOrderer> getOrderers() {
        return orderers;
    }

    void setOrgMSPID(String orgMSPID) {
        this.orgMSPID = orgMSPID;
    }

    void addPeer(String peerName, String peerLocation, String peerEventHubLocation, String serverCrtPath, String clientCertPath, String clientKeyPath) {
        peers.add(new HmPeer(peerName, peerLocation, peerEventHubLocation, serverCrtPath, clientCertPath, clientKeyPath));
    }

    List<HmPeer> getPeers() {
        return peers;
    }

    void setChannel(HmChannel channel) {
        this.channel = channel;
    }

    HmChannel getChannel() {
        return channel;
    }

    void setChainCode(HmChaincode chaincode) {
        this.chaincode = chaincode;
    }

    HmChaincode getChainCode() {
        return chaincode;
    }

    void setBlockListener(BlockListener blockListener) {
        this.blockListener = blockListener;
    }

    BlockListener getBlockListener() {
        return blockListener;
    }

    void setChaincodeEventListener(String eventNames, ChaincodeEventListener chaincodeEventListener) {
        this.eventNames = eventNames;
        this.chaincodeEventListener = chaincodeEventListener;
    }

    ChaincodeEventListener getChaincodeEventListener() {
        return chaincodeEventListener;
    }

    public String getEventNames() {
        return eventNames;
    }

    void openTLS(boolean openTLS) {
        this.openTLS = openTLS;
    }


    boolean openTLS() {
        return openTLS;
    }

    void addUser(String leagueName, String orgName, String peerName, HmUser user, HmUserStore fabricStore) {
        try {
            userMap.put(user.getName(), fabricStore.getMember(leagueName, orgName, peerName, user.getName(), orgMSPID, user.getEnrollment()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    User getUser(String username) {
        return userMap.get(username);
    }

    void setClient(HFClient client) throws CryptoException, InvalidArgumentException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.client = client;
        log.info("Create instance of HFClient");
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        log.info("Set Crypto Suite of HFClient");
    }

    HFClient getClient() {
        return client;
    }

}