/**  
 * @Title:hmsdk
 * @Description:TODO
 * @author:disp     
 * @date:2018.6.8
 * @version V1.0 
 *
 */

package com.hmstar.hmstarcc;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hmstar.hmstarcc.Listener.BlockListener;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 合约对象
 *
 */
class HmChaincode {

    //private Logger log = LogManager.getLogger(HmChaincode.class);
    private static Logger log = Logger.getLogger(HmChaincode.class);
    
    private String chaincodeName; 
    private String chaincodeSource; 
    private String chaincodePath;
    private String chaincodePolicy; // policy.yaml
    private String chaincodeVersion; // 1.0
    private ChaincodeID chaincodeID;
    private int proposalWaitTime = 200000; //ms

    void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
        setChaincodeID();
    }

    void setChaincodeSource(String chaincodeSource) {
        this.chaincodeSource = chaincodeSource;
        setChaincodeID();
    }

    void setChaincodePath(String chaincodePath) {
        this.chaincodePath = chaincodePath;
        setChaincodeID();
    }

    void setChaincodePolicy(String chaincodePolicy) {
        this.chaincodePolicy = chaincodePolicy;
    }

    void setChaincodeVersion(String chaincodeVersion) {
        this.chaincodeVersion = chaincodeVersion;
        setChaincodeID();
    }

    private void setChaincodeID() {
        if (null != chaincodeName && null != chaincodePath && null != chaincodeVersion) {
            chaincodeID = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion).setPath(chaincodePath).build();
        }
    }

    /**
     * 安装智能合约
     */
    JSONObject install(HmOrg org) throws ProposalException, InvalidArgumentException {
        /// Send transaction proposal to all peers
        return null;
    }

    /**
     * 实例化智能合约
     */
    JSONObject instantiate(HmOrg org, String[] args) throws ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException {
        /// Send transaction proposal to all peers
        return null;
    }

    /**
     * 升级智能合约
     */
    JSONObject upgrade(HmOrg org, String[] args) throws ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException {
        /// Send transaction proposal to all peers
         return null;
    }

    /**
     * 执行智能合约
     */
    JSONObject invoke(HmOrg org, String fcn, String[] args) throws InvalidArgumentException, ProposalException, IOException {
        /// Send transaction proposal to all peers
        TransactionProposalRequest transactionProposalRequest = org.getClient().newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincodeID);
        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setArgs(args);
        transactionProposalRequest.setProposalWaitTime(proposalWaitTime);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
        tm2.put("result", ":)".getBytes(UTF_8));
        transactionProposalRequest.setTransientMap(tm2);

        long currentStart = System.currentTimeMillis();
        Collection<ProposalResponse> transactionProposalResponses = org.getChannel().get().sendTransactionProposal(transactionProposalRequest, org.getChannel().get().getPeers());
        log.info("chaincode invoke transaction proposal time = " + (System.currentTimeMillis() - currentStart));
        return toOrdererResponse(transactionProposalResponses, org);
    }

    /**
     * 查询智能合约
     */
    JSONObject query(HmOrg org, String fcn, String[] args) throws InvalidArgumentException, ProposalException {
        QueryByChaincodeRequest queryByChaincodeRequest = org.getClient().newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setFcn(fcn);
        queryByChaincodeRequest.setChaincodeID(chaincodeID);
        queryByChaincodeRequest.setProposalWaitTime(proposalWaitTime);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
        queryByChaincodeRequest.setTransientMap(tm2);

        long currentStart = System.currentTimeMillis();
        Collection<ProposalResponse> queryProposalResponses = org.getChannel().get().queryByChaincode(queryByChaincodeRequest, org.getChannel().get().getPeers());
        log.info("chaincode query transaction proposal time = " + (System.currentTimeMillis() - currentStart));
        return toPeerResponse(queryProposalResponses, true);
    }

    /**
     * 获取实例化合约、升级合约以及invoke合约的返回结果集合
     */
    private JSONObject toOrdererResponse(Collection<ProposalResponse> proposalResponses, HmOrg org) throws InvalidArgumentException, UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        Collection<ProposalResponse> successful = new LinkedList<>();
        Collection<ProposalResponse> failed = new LinkedList<>();
        for (ProposalResponse response : proposalResponses) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                successful.add(response);
            } else {
                failed.add(response);
            }
        }

        Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(proposalResponses);
        if (proposalConsistencySets.size() != 1) {
            log.error("Expected only one set of consistent proposal responses but got " + proposalConsistencySets.size());
        }
        if (failed.size() > 0) {
            ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
            log.error("Not enough endorsers for inspect:" + failed.size() + " endorser error: " + firstTransactionProposalResponse.getMessage() + ". Was verified: "
                    + firstTransactionProposalResponse.isVerified());
            jsonObject.put("code", BlockListener.ERROR);
            jsonObject.put("error", firstTransactionProposalResponse.getMessage());
            return jsonObject;
        } else {
            log.info("Successfully received transaction proposal responses.");
            ProposalResponse resp = proposalResponses.iterator().next();
            log.debug("TransactionID: " + resp.getTransactionID());
            byte[] x = resp.getChaincodeActionResponsePayload();
            String resultAsString = null;
            if (x != null) {
                resultAsString = new String(x, "UTF-8");
            }
            log.info("resultAsString = " + resultAsString);
            // org.getChannel().get().sendTransaction(successful).get(transactionWaitTime, TimeUnit.SECONDS);
            org.getChannel().get().sendTransaction(successful);
            jsonObject = parseResult(resultAsString);
            jsonObject.put("code", BlockListener.SUCCESS);
            jsonObject.put("txid", resp.getTransactionID());
            return jsonObject;
        }
    }

    /**
     * 获取安装合约以及query合约的返回结果集合
     */
    private JSONObject toPeerResponse(Collection<ProposalResponse> proposalResponses, boolean checkVerified) {
        JSONObject jsonObject = new JSONObject();
        for (ProposalResponse proposalResponse : proposalResponses) {
            if ((checkVerified && !proposalResponse.isVerified()) || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
                String data = String.format("Failed install/query proposal from peer %s status: %s. Messages: %s. Was verified : %s",
                        proposalResponse.getPeer().getName(), proposalResponse.getStatus(), proposalResponse.getMessage(), proposalResponse.isVerified());
                log.debug(data);
                jsonObject.put("code", BlockListener.ERROR);
                jsonObject.put("error", data);
            } else {
                String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
                log.debug("Install/Query payload from peer: " + proposalResponse.getPeer().getName());
                log.debug("TransactionID: " + proposalResponse.getTransactionID());
                log.debug("" + payload);
                jsonObject = parseResult(payload);
                jsonObject.put("code", BlockListener.SUCCESS);
                jsonObject.put("txid", proposalResponse.getTransactionID());
            }
        }
        return jsonObject;
    }


    void setProposalWaitTime(int proposalWaitTime) {
        this.proposalWaitTime = proposalWaitTime;
    }

    private JSONObject parseResult(String result) {
        JSONObject jsonObject = new JSONObject();
        int jsonVerify = isJSONValid(result);
        switch (jsonVerify) {
            case 0:
                jsonObject.put("data", result);
                break;
            case 1:
                jsonObject.put("data", JSONObject.parseObject(result));
                break;
            case 2:
                jsonObject.put("data", JSONObject.parseArray(result));
                break;
        }
        return jsonObject;
    }

    /**
     * 判断字符串类型
     *
     * @param str 字符串
     * @return 0-string；1-JsonObject；2、JsonArray
     */
    private static int isJSONValid(String str) {
        try {
            JSONObject.parseObject(str);
            return 1;
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(str);
                return 2;
            } catch (JSONException ex1) {
                return 0;
            }
        }
    }

}
