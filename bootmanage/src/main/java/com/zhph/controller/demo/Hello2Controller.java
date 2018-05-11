package com.zhph.controller.demo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhph.model.vo.TreeVo;
import com.zhph.service.sys.SysResourcesService;

@Controller
public class Hello2Controller {

	@Autowired
	private SysResourcesService resourcesService;
	
	@RequestMapping("/login")
	public String login(HttpServletRequest req){
		
		return "login";
	}
	
	
	@RequestMapping("/index")
	public String index(HttpServletRequest req){
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		System.out.println("username:"+username+",password:"+password);
		
		
		
		List<Long> resourcesIds = new ArrayList<Long>();
		resourcesIds.add(2l);
		resourcesIds.add(4l);
		resourcesIds.add(3l);
		resourcesIds.add(5l);
		resourcesIds.add(6l);
		resourcesIds.add(6l);
		resourcesIds.add(61l);
		resourcesIds.add(62l);


		TreeVo vo =resourcesService.findMenuByResourcesId(resourcesIds);
		
		req.setAttribute("resources", vo.getChildren());
		
		
		return "index";
		
	}
	
}
