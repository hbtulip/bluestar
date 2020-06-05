package org.hmx.consumer.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.dubbo.config.annotation.Reference;
import org.hmx.consumer.mq.MessageProducer;
import org.hmx.utils.api.exception.BusinessException;
import org.hmx.utils.api.exception.ResultJson;
import org.hmx.utils.api.service.IDemoService;
import org.hmx.utils.api.vo.DemoResultVO;
import org.hmx.utils.api.vo.WechatCardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Reference(version = "2.7.3",check = false)
    private IDemoService demoService;

	@Reference(version = "2.5.10",check = false)
    private IDemoService demoService2;
	
    @Autowired
    private MessageProducer messageProducer;
    
    @RequestMapping("/demo")
    public String demoUser() throws Exception{
		Date currentTime = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String dateString = formatter.format(currentTime);
        return "演示信息 ：123456ABCDEF," + dateString; 
    }
    
    @RequestMapping("/demo1")
    public ResultJson demoUser1() throws Exception{
    	String strRet = "演示信息 ：123456ABCDEF,";
    	try {
    		DemoResultVO vo = demoService.process(10);   
    		strRet += vo.getCode() + vo.getMsg();
    	} catch(Exception ex) {
    		throw new BusinessException("调用服务报错,BusinessException-> " + ex.getMessage());
    	}

        return ResultJson.getSuccessInstance(strRet); 
    }  
    @RequestMapping("/demo2")
    public ResultJson demoUser2(String storeCode) throws Exception{

    	if(storeCode == null) {
    		storeCode = "d6355ec36d134236a97a0306504adb62";
    	}
    	
    	String url = "";    	
    	try {
    		url = demoService.findStoreCardUrl(storeCode);   
    	} catch(Exception ex) {
    		throw new BusinessException("调用服务报错,BusinessException-> " + ex.getMessage());
    	}

        return ResultJson.getSuccessInstance("调用成功",url); 
    }
    
    @RequestMapping(method = RequestMethod.GET , path = "/list")
    public Map<String,String> listUser(String type){
    	Map<String,String> list = new HashMap<String,String>();
    	list.put("张三","11111");
    	list.put("王四","22222");
    	list.put("赵六","33333");
    	
    	if(type != null && type.equals("all")){
        	list.put("大连","666666");
        	list.put("北京","999999");       	
		}
    	
        return list;
    }
    
    @RequestMapping("/sendmsg1")
    public String sendMsg1(String msg) throws Exception{
        // 将实体实例写入消息队列
        messageProducer.sendMessage1(msg);
        return "Success";
    }
    
    @RequestMapping("/sendmsg2")
    public String sendMsg2(String msg) throws Exception{
        // 将实体实例写入消息队列
    	WechatCardVo card = new WechatCardVo();
    	card.setUrl("https://www.baidu.com/");
        messageProducer.sendMessage2(card);
        return "Success";
    }
}
