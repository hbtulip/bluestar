/**  
 * @Title:hmsdk
 * @Description:TODO
 * @author:disp     
 * @date:2018.6.8
 * @version V1.0 
 *
 */

package com.hmstar.hmstarcc;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.helper.Utils;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;

import com.hmstar.hmstarcc.Listener.BlockListener;
import com.hmstar.hmstarcc.Listener.ChaincodeEventListener;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivateKey;
import java.security.Security;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 描述：组织管理器
 *
 */
public class HmOrgManager {

    private static Logger log = Logger.getLogger(HmOrgManager.class);
    
    private Map<String, HmOrg> orgMap;
    private HmUserStore fabricStore;
    private String cc;

    public HmOrgManager() {
        orgMap = new LinkedHashMap<>();
        File storeFile = new File(String.format("%s/HFCStore.properties", System.getProperty("java.io.tmpdir")));
        fabricStore = new HmUserStore(storeFile);
    }

    /**
     * 初始化管理器
     */
    public HmOrgManager init(String cc, String orgMSPID, boolean openTLS) {
        this.cc = cc;
        if (orgMap.get(cc) != null) {
            throw new RuntimeException(String.format("OrgManager had the same cc of %s", cc));
        } else {
            orgMap.put(cc, new HmOrg());
        }
        orgMap.get(cc).setOrgMSPID(orgMSPID);
        orgMap.get(cc).openTLS(openTLS);
        orgMap.get(cc).setFabricStore(fabricStore);
        return this;
    }

    /**
     * 方法已废弃，请使用setUser4Local代替
     */    
    @Deprecated
    public HmOrgManager setUser(String leagueName, String orgName, String peerName,String username,String skPath, String certificatePath) throws IOException {
        PrivateKey privateKey = getPrivateKeyFromBytes(skPath);
        Enrollment enrollment = new StoreEnrollment(privateKey, certificatePath);
        
        HmUser user = new HmUser(leagueName, orgName, peerName, username, enrollment);
        orgMap.get(cc).addUser(leagueName, orgName, peerName, user, fabricStore);
        return this;
    }


    /**
     * 设置用户（Admin／User）
     *
     * @param username        用户名
     * @param skPath          带有节点私钥文件
     * @param pemPath 带有节点的X.509证书的PEM文件
     *
     */
    public HmOrgManager setUser4Local(String leagueName, String orgName, String peerName,String username,String skPath, String pemPath) throws UnsupportedEncodingException, FileNotFoundException, IOException {

        String privatekeyTemp = new String(IOUtils.toByteArray(new FileInputStream(new File(skPath))), "UTF-8");   
        String certificate = new String(IOUtils.toByteArray(new FileInputStream(new File(pemPath))), "UTF-8");
    	
        PrivateKey privateKey = getPrivateKeyFromBytes(privatekeyTemp);
        Enrollment enrollment = new StoreEnrollment(privateKey, certificate);
        
        HmUser user = new HmUser(leagueName, orgName, peerName, username, enrollment);
        orgMap.get(cc).addUser(leagueName, orgName, peerName, user, fabricStore);
        return this;
        
    }

    /**
     * 设置用户（Admin／User）
     *
     * @param caUrl
     * @param caUsername
     * @param caPassword
     * @throws InvocationTargetException 
     * @throws NoSuchMethodException 
     * @throws InvalidArgumentException 
     * @throws CryptoException 
     * @throws ClassNotFoundException 
     * @throws InstantiationException 
     * @throws IllegalAccessException 
     * @throws org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException 
     * @throws EnrollmentException 
     *
     */
    public HmOrgManager setUser4FabricCA(String leagueName, String orgName, String peerName,String username,String caUrl, String caUsername, String caPassword ) throws UnsupportedEncodingException, FileNotFoundException, IOException, IllegalAccessException, InstantiationException, ClassNotFoundException, CryptoException, InvalidArgumentException, NoSuchMethodException, InvocationTargetException, EnrollmentException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException {

        CryptoSuite cryptosuite = CryptoSuite.Factory.getCryptoSuite();
        HFCAClient caclient = HFCAClient.createNewInstance(caUrl, null);

        caclient.setCryptoSuite(cryptosuite);
        Enrollment enrollment = caclient.enroll(caUsername, caPassword);
  	
        HmUser user = new HmUser(leagueName, orgName, peerName, username, enrollment);
        orgMap.get(cc).addUser(leagueName, orgName, peerName, user, fabricStore);
        return this;
        
    }
    

    public void addOrderer(String name, String location, String serverCrtPath, String clientCertPath, String clientKeyPath) {
        //orgMap.get(cc).addOrderer(name, String.format("%s%s", "grpc://", location), serverCrtPath, clientCertPath, clientKeyPath);
        orgMap.get(cc).addOrderer(name, location, serverCrtPath, clientCertPath, clientKeyPath);
        log.debug("location="+location);
    }

    public void addPeer(String peerName, String peerLocation, String peerEventHubLocation, String serverCrtPath, String clientCertPath, String clientKeyPath) {
        //orgMap.get(cc).addPeer(peerName, String.format("%s%s", "grpc://", peerLocation), String.format("%s%s", "grpc://", peerEventHubLocation), serverCrtPath, clientCertPath, clientKeyPath);
        //修正EventHub为空时，在 HmChannel.Init 中判读错误的bug
    	orgMap.get(cc).addPeer(peerName, peerLocation, peerEventHubLocation, serverCrtPath, clientCertPath, clientKeyPath);
        log.debug("peerLocation="+peerLocation);
        log.debug("peerEventHubLocation="+peerEventHubLocation);
    }

    /**
     * 设置智能合约
     *
     * @param chaincodeName    智能合约名称
     * @param chaincodePath    智能合约路径
     * @param chaincodeSource  智能合约安装路径所在路径
     * @param chaincodePolicy  智能合约背书文件路径
     * @param chaincodeVersion 智能合约版本
     * @param proposalWaitTime 单个提案请求的超时时间以毫秒为单位
     */
    public HmOrgManager setChainCode(String chaincodeName, String chaincodePath, String chaincodeSource, String chaincodePolicy, String chaincodeVersion, int proposalWaitTime) {
        HmChaincode chaincode = new HmChaincode();
        chaincode.setChaincodeName(chaincodeName);
        chaincode.setChaincodeSource(chaincodeSource);
        chaincode.setChaincodePath(chaincodePath);
        chaincode.setChaincodePolicy(chaincodePolicy);
        chaincode.setChaincodeVersion(chaincodeVersion);
        chaincode.setProposalWaitTime(proposalWaitTime);
        orgMap.get(cc).setChainCode(chaincode);
        return this;
    }

    /**
     * 设置频道
     *
     * @param channelName 频道名称
     *
     */
    public HmOrgManager setChannel(String channelName) {
        HmChannel channel = new HmChannel();
        channel.setChannelName(channelName);
        orgMap.get(cc).setChannel(channel);
        return this;
    }

    public void setBlockListener(BlockListener blockListener) {
        orgMap.get(cc).setBlockListener(blockListener);
    }

    public void setChaincodeEventListener(String eventNames, ChaincodeEventListener chaincodeEventListener) {
        orgMap.get(cc).setChaincodeEventListener(eventNames, chaincodeEventListener);
    }

    public void applyFitout() {
        if (orgMap.get(cc).getPeers().size() == 0) {
            throw new RuntimeException("peers is null or peers size is 0");
        }
        if (orgMap.get(cc).getOrderers().size() == 0) {
            throw new RuntimeException("orderers is null or orderers size is 0");
        }
        if (orgMap.get(cc).getChainCode() == null) {
            throw new RuntimeException("chaincode must be instantiated");
        }

        // 根据TLS开启状态循环确认Peer节点各服务的请求grpc协议
        for (int i = 0; i < orgMap.get(cc).getPeers().size(); i++) {
            orgMap.get(cc).getPeers().get(i).setPeerLocation(grpcTLSify(orgMap.get(cc).openTLS(), orgMap.get(cc).getPeers().get(i).getPeerLocation()));
            orgMap.get(cc).getPeers().get(i).setPeerEventHubLocation(grpcTLSify(orgMap.get(cc).openTLS(), orgMap.get(cc).getPeers().get(i).getPeerEventHubLocation()));
        }
        // 根据TLS开启状态循环确认Orderer节点各服务的请求grpc协议
        for (int i = 0; i < orgMap.get(cc).getOrderers().size(); i++) {
            orgMap.get(cc).getOrderers().get(i).setOrdererLocation(grpcTLSify(orgMap.get(cc).openTLS(), orgMap.get(cc).getOrderers().get(i).getOrdererLocation()));
        }
    }

    public FabricManager getFabricManager(String cc, String username) throws Exception {
    	log.info("Get Fabric Manager." + "("+cc+","+username+")");
        HmOrg org = orgMap.get(cc);
        org.setUsername(username);
        org.setClient(HFClient.createNewInstance());
        org.getChannel().init(org);
        return new FabricManager(org);
    }

    private String grpcTLSify(boolean openTLS, String location) {
        location = location.trim();
        /*
        //检查grpcurl
        Exception e = Utils.checkGrpcUrl(location);
        if (e != null) {
            throw new RuntimeException(String.format("Bad TEST parameters for grpc url %s", location), e);
        }
        */
        
        return openTLS ? location.replaceFirst("^grpc://", "grpcs://") : location;

    }

    private String httpTLSify(boolean openCATLS, String location) {
        location = location.trim();
        return openCATLS ? location.replaceFirst("^http://", "https://") : location;
    }
    
    

    /**
     * 通过字节数组信息获取私钥
     *
     * @param data 字节数组
     * @return 私钥
     */
    private PrivateKey getPrivateKeyFromBytes(String data) throws IOException {
        final Reader pemReader = new StringReader(data);
        final PrivateKeyInfo pemPair;
        try (PEMParser pemParser = new PEMParser(pemReader)) {
            pemPair = (PrivateKeyInfo) pemParser.readObject();
        }
        return new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);
    }

    //设置加密方式
    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义注册登记操作类
     */
    static final class StoreEnrollment implements Enrollment, Serializable {

        private static final long serialVersionUID = 6965341351799577442L;

        /** 私钥 */
        private final PrivateKey privateKey;
        /** 授权证书 */
        private final String certificate;

        StoreEnrollment(PrivateKey privateKey, String certificate) {
            this.certificate = certificate;
            this.privateKey = privateKey;
        }

        @Override
        public PrivateKey getKey() {
            return privateKey;
        }

        @Override
        public String getCert() {
            return certificate;
        }
    }   

}
