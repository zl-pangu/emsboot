package com.zhph.controller.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhph.model.Sex;
import com.zhph.service.demo.SexService;


@Controller
public class RedisController {
	
	@Autowired
	private SexService sexService;
	
	@RequestMapping("/redisTest")
	@ResponseBody
	public String test(){
		Sex loaded = sexService.findById(1);
		System.out.println("loaded="+loaded.toString());
		Sex cached = sexService.findById(1);
		System.out.println("cached="+cached.toString());
		Sex loaded2 = sexService.findById(2);
		System.out.println("loaded2="+loaded2.toString());
		return "ok";
	}
	
    @RequestMapping("/test1")
    @ResponseBody
    public String test1(){
    	sexService.test();
        System.out.println("DemoInfoController.test1()");
        return"ok";
    }
 
}
