package com.spring.framework.handler.security;


    import com.alibaba.fastjson2.JSON;
    import com.spring.framework.domain.ResponseResult;
    import com.spring.framework.domain.enums.AppHttpCodeEnum;
    import com.spring.framework.domain.utils.WebUtils;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.security.access.AccessDeniedException;
    import org.springframework.security.web.access.AccessDeniedHandler;
    import org.springframework.stereotype.Component;

    import java.io.IOException;

    @Component
    public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException,
            ServletException {
    accessDeniedException.printStackTrace();
    ResponseResult result =ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);

    //响应给前端
            WebUtils.renderString(response, JSON.toJSONString(result));
    }
    }