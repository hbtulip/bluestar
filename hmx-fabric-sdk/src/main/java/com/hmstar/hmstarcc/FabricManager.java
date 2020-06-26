/**  
 * @Title:hmsdk
 * @Description:TODO
 * @author:disp     
 * @date:2018.6.8
 * @version V1.0 
 *
 */

package com.hmstar.hmstarcc;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.StringUtils;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.io.IOException;

/**
 * 描述：区块链网络服务管理器
 *
 */
public class FabricManager {

    private HmOrg org;

    FabricManager(HmOrg org) {
        this.org = org;
    }

    /**
     * 执行智能合约
     *
     * @param fcn  方法名
     * @param args 参数数组
     */
    public JSONObject invoke(String fcn, String[] args) throws InvalidArgumentException, ProposalException, IOException {
        return org.getChainCode().invoke(org, fcn, args);
    }

    /**
     * 查询智能合约
     *
     * @param fcn  方法名
     * @param args 参数数组
     */
    public JSONObject query(String fcn, String[] args) throws InvalidArgumentException, ProposalException {
        return org.getChainCode().query(org, fcn, args);
    }

    /**
     * 在指定频道内根据transactionID查询区块
     *
     * @param txID transactionID
     */
    public JSONObject queryBlockByTransactionID(String txID) throws ProposalException, IOException, InvalidArgumentException {
        return org.getChannel().queryBlockByTransactionID(txID);
    }

    /**
     * 在指定频道内根据hash查询区块
     *
     * @param blockHash hash
     */
    public JSONObject queryBlockByHash(byte[] blockHash) throws ProposalException, IOException, InvalidArgumentException {
        return org.getChannel().queryBlockByHash(blockHash);
    }

    /**
     * 在指定频道内根据区块高度查询区块
     *
     * @param blockNumber 区块高度
     */
    public JSONObject queryBlockByNumber(long blockNumber) throws ProposalException, IOException, InvalidArgumentException {
        return org.getChannel().queryBlockByNumber(blockNumber);
    }

    /**
     * Peer加入频道
     *
     * @param peerName             当前指定的组织节点域名
     * @param peerLocation         当前指定的组织节点访问地址
     * @param peerEventHubLocation 当前指定的组织节点事件监听访问地址
     */
    public JSONObject joinPeer(String peerName, String peerLocation, String peerEventHubLocation, String serverCrtPath, String clientCertPath, String clientKeyPath) throws ProposalException, InvalidArgumentException {
        return org.getChannel().joinPeer(new HmPeer(peerName, peerLocation, peerEventHubLocation, serverCrtPath, clientCertPath, clientKeyPath));
    }

    /** 
     * 查询当前频道的链信息，包括链长度、当前最新区块hash以及当前最新区块的上一区块hash 
     * 
     * */
    public JSONObject getBlockchainInfo() throws ProposalException, InvalidArgumentException {
        return org.getChannel().getBlockchainInfo();
    }

}
