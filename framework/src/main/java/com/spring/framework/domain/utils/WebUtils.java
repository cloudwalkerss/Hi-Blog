package com.spring.framework.domain.utils;


import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
//WebUtils 是一个工具类，提供了将字符串渲染到客户端和设置文件下载头的方法。
@Component
public class WebUtils
    {
        /**
        * 将字符串渲染到客户端
        * 
        * @param response 渲染对象
        * @param string 待渲染的字符串
        * @return null
        */
        public static void renderString(HttpServletResponse response, String string) {
            try
            {
                response.setStatus(200);
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().print(string);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }


        public static void setDownLoadHeader(String filename, ServletContext context, HttpServletResponse response) throws UnsupportedEncodingException {
            String mimeType = context.getMimeType(filename);//获取文件的mime类型
            response.setHeader("content-type",mimeType);
            String fname= URLEncoder.encode(filename,"UTF-8");
            response.setHeader("Content-disposition","attachment; filename="+fname);
        }
    }