package org.hmx.provider.mapper;


import org.apache.ibatis.annotations.SelectProvider;
import org.hmx.utils.api.vo.WechatCardVo;

import java.util.List;
import java.util.Map;

public interface ExtWechatCardMapper {


    @SelectProvider(method = "selectCardVo", type = ExtWechatCardSqlProvider.class)
    List<WechatCardVo> selectCardVoByCode (Map<String, Object> params);
}
