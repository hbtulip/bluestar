/**  
 * @Title:hmsdk
 * @Description:TODO
 * @author:disp     
 * @date:2018.6.1
 * @version V1.0 
 *
 */

package com.hmstar.hmstarcc;

import io.netty.util.internal.StringUtil;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.io.*;
import java.util.Set;

/**
 * 联盟用户对象
 *
 */
class HmUser implements User, Serializable {

    private static final long serialVersionUID = 5695080465408336815L;

    //private static Logger log = LogManager.getLogger(HmUser.class);
    private static Logger log = Logger.getLogger(HmUser.class);
    
    private String name;			//名称
    private String skPath;			//sk文件
    private String certificatePath;	//证书文件
    private String caUrl;			//ca地址
    private String caUsername;		//ca登录用户
    private String caPassword;		//ca用户密码
    
    
    private Set<String> roles; 		//背书规则
    private String account;
    private String affiliation;		//从属联盟
    private String organization;	//组织
    private String enrollmentSecret;//注册操作的密密钥
    private String mspId;			//会员id
    private Enrollment enrollment = null;//注册登记操作

    private transient HmUserStore fabricStore;
    private String keyForFabricStoreName;

    HmUser(String leagueName, String orgName, String peerName, String name, Enrollment enrollment) {
        this.name = name;
        this.enrollment = enrollment;
        this.keyForFabricStoreName = getKeyForFabricStoreName(leagueName, orgName, peerName, this.name);
    }
    
    void setFabricStore(HmUserStore fabricStore) {
        this.fabricStore = fabricStore;
    }

    String getSkPath() {
        return skPath;
    }

    public void setSkPath(String skPath) {
        this.skPath = skPath;
    }

    String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    /**
     * 设置账户信息并将用户状态更新至存储配置对象
     */
    void setAccount(String account) {
        this.account = account;
        saveState();
    }

    @Override
    public String getAccount() {
        return this.account;
    }

    /**
     * 设置从属联盟信息并将用户状态更新至存储配置对象，affiliation 从属联盟
     */
    void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
        saveState();
    }

    @Override
    public String getAffiliation() {
        return this.affiliation;
    }

    String getEnrollmentSecret() {
        return enrollmentSecret;
    }

    void setEnrollmentSecret(String enrollmentSecret) {
        this.enrollmentSecret = enrollmentSecret;
    }

    /**
     * 设置注册登记操作信息并将用户状态更新至存储配置对象
     */
    void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
        saveState();
    }

    /**
     * 获取用户注册登记操作后信息
     */
    @Override
    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    /**
     * 设置会员id信息并将用户状态更新至存储配置对象
     */
    void setMspId(String mspID) {
        this.mspId = mspID;
        saveState();
    }

    /**
     * 获取会员id
     */
    @Override
    public String getMspId() {
        return this.mspId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 设置角色规则信息并将用户状态更新至存储配置对象
     */
    void setRoles(Set<String> roles) {
        this.roles = roles;
        saveState();
    }

    /**
     * 获取角色规则信息
     */
    @Override
    public Set<String> getRoles() {
        return this.roles;
    }

    /**
     * 确定是否已注册
     */
    boolean isRegistered() {
        return !StringUtil.isNullOrEmpty(enrollmentSecret);
    }

    boolean isEnrolled() {
        return this.enrollment != null;
    }

    /**
     * 将用户状态保存至存储配置对象
     */
    void saveState() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            fabricStore.setValue(keyForFabricStoreName, Hex.toHexString(bos.toByteArray()));
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从键值存储中恢复该用户的状态(如果找到）
     */
    private void restoreState() {
        String memberStr = fabricStore.getValue(keyForFabricStoreName);
        if (null != memberStr) {
            // 用户在键值存储中被找到，因此恢复状态
            byte[] serialized = Hex.decode(memberStr);
            ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                HmUser state = (HmUser) ois.readObject();
                if (state != null) {
                    this.name = state.name;
                    this.roles = state.roles;
                    this.account = state.account;
                    this.affiliation = state.affiliation;
                    this.organization = state.organization;
                    this.enrollmentSecret = state.enrollmentSecret;
                    this.enrollment = state.enrollment;
                    this.mspId = state.mspId;
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("Could not restore state of member %s", this.name), e);
            }
        }
    }

    /**
     * 得到联盟存储配置对象key，user.Org1User1.Org1
     */
    static String getKeyForFabricStoreName(String name, String org) {
        log.debug("toKeyValStoreName = " + "user." + name + org);
        return "user." + name + org;
    }

    /**
     * 得到联盟存储配置对象key，user.Org1User1.Org1
     *
     */
    static String getKeyForFabricStoreName(String leagueName, String orgName, String peerName, String name) {        // System.out.println(String.format("toKeyValStoreName = user.%s%s%s", name, skPath, certificatePath));
        String key = String.format("toKeyValStoreName = user.%s%s%s%s", leagueName, orgName, peerName, name);
        log.debug(key);
        return key;
    }

}
