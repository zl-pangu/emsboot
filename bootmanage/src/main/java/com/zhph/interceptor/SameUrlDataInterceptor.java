package com.zhph.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhph.annotation.SameUrlData;
import com.zhph.util.Json;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouliang on 2018/1/18.
 */
public class SameUrlDataInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.hasMethodAnnotation(SameUrlData.class)){
                if(repeatDataValidator(request)){
                    Json json=new Json();
                    ObjectMapper mapper=new ObjectMapper();
                    PrintWriter writer = null;
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/json;charset=utf-8");
                    try {
                        writer = response.getWriter();
                        json.setMsg("不要重复提交！");
                        json.setSuccess(false);
                        writer.print(mapper.writeValueAsString(json));
                    } catch (Exception e) {
                        if (writer!=null){
                            writer.close();
                        }
                        e.printStackTrace();
                    }
                    return false;
                }else{
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    public boolean repeatDataValidator(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        String params = JSONObject.toJSONString(parameterMap);
        String url= request.getRequestURI();
        Map<String,String> map=new HashMap<>();
        map.put(url,params);
        String nowUrlParams = map.toString();
        Object preUrlParams = request.getSession().getAttribute("repeatData");
        if (preUrlParams==null){
            request.getSession().setAttribute("repeatData",nowUrlParams);
            return  false;
        }else{
            if (preUrlParams.toString().equals(nowUrlParams)){
                return true;
            }else{
                request.getSession().setAttribute("repeatData",nowUrlParams);
                return false;
            }
        }
        }
    }
