package com.zhph.controller.demo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SessionTestController {

    @RequestMapping("/uid/{age}")
    @ResponseBody
    public String firstResp (HttpServletRequest request,@PathVariable String age){  
    	request.getSession().setAttribute("age", age);
        String a = (String) request.getSession().getAttribute("age");
        return a;
    }  
    
    
}
