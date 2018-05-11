package com.zhph.controller.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.zhph.model.SalaryP2pEmp;
import com.zhph.model.vo.BsgridVo;
import com.zhph.model.vo.ResultVo;
import com.zhph.service.demo.SalaryP2pEmpService;
import com.zhph.service.demo.SexService;


@Controller
@RequestMapping("/emp")
public class SalaryP2pEmpController {

	@Autowired
	private SalaryP2pEmpService salaryP2pEmpService;
	
	@Autowired
	private SexService sexService;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request,HttpServletResponse response){
		
		return  "/pages/sysuser/list";
	}

	@RequestMapping("/getAll")
	@ResponseBody
	public Object getAll(Integer pageSize, Integer curPage, HttpServletRequest req){
 
		BsgridVo<SalaryP2pEmp> bsgridVo = new BsgridVo<SalaryP2pEmp>();
		try {
			// 后台数据取得
			Page<SalaryP2pEmp> page = this.salaryP2pEmpService.getAll( pageSize,  curPage);
			bsgridVo.setCurPage(curPage.longValue());
			bsgridVo.setData(page);
			bsgridVo.setSuccess(true);
			bsgridVo.setTotalRows(page.getTotal());
		} catch (Exception e) {
			e.printStackTrace();
			bsgridVo.setSuccess(false);
		}
		return bsgridVo;
	}
	
	@RequestMapping("/forInsert")
	public String  forInsert(HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("sexList", sexService.getAll());
		return "/pages/sysuser/insert";
	}
	
	
	
	@ResponseBody
	@RequestMapping("/insert")
	public Object insert(
			HttpServletRequest request, HttpServletResponse response,SalaryP2pEmp salaryP2pEmp) {
		ResultVo resultVo = new ResultVo();
		try {

			salaryP2pEmpService.insert(salaryP2pEmp);
			resultVo.setStatus(1);
			resultVo.setInfo("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			resultVo.setStatus(0);
			resultVo.setInfo("保存异常！");
		}
		return resultVo;
	}
}
