package org.hmx.provider.mapper;

import java.util.Map;

public class ExtWechatCardSqlProvider {

    public String selectCardVo (Map<String, Object> params){

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT");
        sql.append("	wc.id id,");
        sql.append("	wc.card_id cardId,");
        sql.append("	wc.store_code storeCode,");
        sql.append("	wc.url url,");
        sql.append("	wc.create_time createTime,");
        sql.append("	wc.update_time updateTime,");
        sql.append("	w.user_name wecahtNum ");
        sql.append("FROM");
        sql.append("	t_wechat_card wc");
        sql.append("	LEFT JOIN t_store_wechat w ON w.CODE = wc.store_code ");
        sql.append("WHERE");
        sql.append("	wc.store_code = #{storeCode,jdbcType=VARCHAR}");

        return sql.toString();
    }

}
