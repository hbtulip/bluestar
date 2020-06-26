/**  
 * @Title:hmsdk
 * @Description:TODO
 * @author:disp     
 * @date:2018.6.1
 * @version V1.0 
 *
 */

package com.hmstar.hmstarcc;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;

import java.io.*;
import java.security.PrivateKey;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 联盟存储配置对象
 *
 */
class HmUserStore {

    private String file;
    /**
     * 用户信息集合
     */
    private final Map<String, HmUser> members = new HashMap<>();

    HmUserStore(File file) {
        this.file = file.getAbsolutePath();
    }

    void setValue(String name, String value) {
        Properties properties = loadProperties();
        try (OutputStream output = new FileOutputStream(file)) {
            properties.setProperty(name, value);
            properties.store(output, "");
        } catch (IOException e) {
            System.out.println(String.format("Could not save the keyvalue store, reason:%s", e.getMessage()));
        }
    }


    String getValue(String name) {
        Properties properties = loadProperties();
        return properties.getProperty(name);
    }

    /**
     * 加载配置文件
     */
    private Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(file)) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            System.out.println(String.format("Could not find the file \"%s\"", file));
        } catch (IOException e) {
            System.out.println(String.format("Could not load keyvalue store from file \"%s\", reason:%s", file, e.getMessage()));
        }
        return properties;
    }

    /**
     * 用给定的名称获取用户
     *
     * @param name            用户名称（User1）
     * @param mspId           会员id
     * @return user 		  用户
     */
    HmUser getMember(String leagueName, String orgName, String peerName, String name, String mspId, Enrollment enrollment) throws IOException {
        // 尝试从缓存中获取User状态
        HmUser user = members.get(HmUser.getKeyForFabricStoreName(leagueName, orgName, peerName, name));
        if (null != user) {
            System.out.println("尝试从缓存中获取User状态 User = " + user);
            return user;
        }
        // 创建User，并尝试从键值存储中恢复它的状态(如果找到的话)
        user = new HmUser(leagueName, orgName, peerName, name, enrollment);
        user.setFabricStore(this);
        user.setMspId(mspId);
        user.saveState();
        
        members.put(HmUser.getKeyForFabricStoreName(leagueName, orgName, peerName, name), user);
        return user;
    }


}
