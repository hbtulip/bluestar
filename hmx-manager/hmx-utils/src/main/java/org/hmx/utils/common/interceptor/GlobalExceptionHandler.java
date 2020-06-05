package org.hmx.utils.common.interceptor;

import com.alibaba.fastjson.JSON;

import org.hmx.utils.api.exception.BusinessException;
import org.hmx.utils.api.exception.ResultJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 全局异常处理
 */
@Configuration
public class GlobalExceptionHandler implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String code = "1";
        String msg;
        Object data = null;
        if( ex instanceof BusinessException){
            msg = ex.getMessage();
        } else {
            msg = "操作异常!";
        }
        logger.error(msg, ex);
        ex.printStackTrace();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(JSON.toJSON(ResultJson.getFailInstance(code, msg, data)).toString());
        } catch (Exception e) {
            logger.error("系统异常!", e);
        } finally {
            //IOUtils.closeQuietly(out);
        	if(out != null) {
	        	out.flush();
	        	out.close();        		
        	}

        }
        return null;
    }

}
