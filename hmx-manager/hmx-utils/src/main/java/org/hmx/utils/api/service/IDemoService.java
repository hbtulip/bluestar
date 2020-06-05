package org.hmx.utils.api.service;

import org.hmx.utils.api.vo.DemoResultVO;

public interface IDemoService {
	DemoResultVO process(long id) throws Exception;
	String findStoreCardUrl(String storeCode) throws Exception;

}
