package org.hmx.provider.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.Service;
import org.hmx.provider.mapper.ExtWechatCardMapper;
import org.hmx.utils.api.service.IDemoService;
import org.hmx.utils.api.vo.DemoResultVO;
import org.hmx.utils.api.vo.WechatCardVo;
import org.hmx.utils.storage.domain.Division;
import org.hmx.utils.storage.mapper.DivisionMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Service(timeout = 5000, version = "2.7.3", interfaceClass = IDemoService.class)
@Component
public class DemoService implements IDemoService {

	Logger logger =LoggerFactory.getLogger(DemoService.class);
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;

	private DivisionMapper getDivisionMapper() {
		return sqlSessionTemplate.getMapper(DivisionMapper.class);
	}
	
    private ExtWechatCardMapper getExtWechatCardMapper (){
        return this.sqlSessionTemplate.getMapper(ExtWechatCardMapper.class);
    }

	
	public DemoResultVO process(long id) throws Exception {
		// TODO Auto-generated method stub
		Date currentTime = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String dateString = formatter.format(currentTime);
	    
	    DivisionMapper dm = getDivisionMapper();
	    Division div = dm.selectByPrimaryKey(1L);
	    
		DemoResultVO vo = new DemoResultVO();
		vo.setCode(100);
		vo.setMsg(" [Dubbo 2.7.3]服务调用成功！" + dateString + " 分账表信息：" + div.getOilCosttotal() + "," +div.getOilMassLitre());			

		logger.debug(vo.getMsg());
		logger.info(vo.getMsg());		
		return vo;
	}

	public String findStoreCardUrl(String storeCode) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("storeCode :" + storeCode);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("storeCode",storeCode);
        List<WechatCardVo> wechatCardVos = this.getExtWechatCardMapper().selectCardVoByCode(params);

        if(wechatCardVos != null && wechatCardVos.size()>0){
            return wechatCardVos.get(0).getUrl();
        }else{
            return null;
        }
	}

}
