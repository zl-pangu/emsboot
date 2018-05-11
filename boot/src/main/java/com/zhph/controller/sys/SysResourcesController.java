package com.zhph.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhph.commons.Constant;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysResources;
import com.zhph.model.sys.SysUser;
import com.zhph.model.sys.vo.SysResourcesUpdateVo;
import com.zhph.model.vo.ResultVo;
import com.zhph.model.vo.TreeVo;
import com.zhph.service.sys.SysConfigTypeService;
import com.zhph.service.sys.SysResourcesService;
import com.zhph.util.CommonUtil;

/**
 * 权限资源controllerr
 * 
 * @ClassName ResourcesController
 * @author Jianglinghao
 * @date 2017年8月17日
 *
 */
@Controller
@RequestMapping("/sys/resources")
public class SysResourcesController {

	@Autowired
	private SysResourcesService resourcesService;

	@Autowired
	private SysConfigTypeService sysConfigTypeService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {

		List<SysConfigType> list = sysConfigTypeService.getConfigTypesByPSysCode(Constant.SYSTEM_TYPE);

		request.setAttribute("systemTypes", list);

		return "/pages/sys/resources/sysResourcesIndex";
	}

	@ResponseBody
	@RequestMapping("/tree")
	public TreeVo getResourcesTree(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "1") long id, @RequestParam(defaultValue = "1") int check_state, @RequestParam(defaultValue = "false") boolean checked) {

		TreeVo tree = resourcesService.getZtreeRoot(id, check_state, checked);

		return tree;
	}

	@ResponseBody
	@RequestMapping("/findTreeBySysType")
	public TreeVo treeByRole(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "0") int resourcesSysType) {

		TreeVo tree = resourcesService.findTreeBySysType(resourcesSysType);

		return tree;
	}

	@ResponseBody
	@RequestMapping(value = "/find")
	public SysResources findById(@RequestParam Long id) {
		SysResources resources = resourcesService.findById(id);
		return resources;
	}

	@RequestMapping("/forInsert")
	public String forInsert(HttpServletRequest request, HttpServletResponse response, @RequestParam long resourceId) {
		request.setAttribute("parent", resourcesService.findById(resourceId));

		List<SysConfigType> list = sysConfigTypeService.getConfigTypesByPSysCode(Constant.SYSTEM_TYPE);
		request.setAttribute("systemTypes", list);

		return "/pages/sys/resources/sysResourcesAdd";
	}

	@ResponseBody
	@RequestMapping(value = "/insert")
	public ResultVo insert(@RequestBody SysResources resources, HttpServletRequest req) {

		ResultVo resultVo = new ResultVo();
		try {
			SysUser user = CommonUtil.getOnlineUser(req);
			resourcesService.insert(resources,user.getId());
			resultVo.setStatus(1);
			resultVo.setInfo("添加资源信息成功！");
			resultVo.setData(resources);
		} catch (Exception e) {
			e.printStackTrace();
			resultVo.setStatus(0);
			resultVo.setInfo("添加资源信息异常！");
		}
		return resultVo;
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public ResultVo update(@RequestBody SysResourcesUpdateVo resourcesUpdateVo, HttpServletRequest request) {

		ResultVo resultVo = new ResultVo();
		try {
			SysUser user = CommonUtil.getOnlineUser(request);
			resultVo = resourcesService.update(resourcesUpdateVo, user.getId());

		} catch (Exception e) {
			e.printStackTrace();
			resultVo.setStatus(0);
			resultVo.setInfo("修改资源信息异常！");
		}
		return resultVo;

	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public ResultVo delete(@RequestParam Long resourcesId, HttpServletRequest req) {

		ResultVo vo = new ResultVo();
		try {
			vo = resourcesService.delete(resourcesId);

		} catch (Exception ex) {
			ex.printStackTrace();
			vo.setInfo("删除资源异常！");
			vo.setStatus(0);
		}
		return vo;
	}
	
	

	@ResponseBody
	@RequestMapping("/checkUserName")
	public Object checkUserName(HttpServletRequest request, HttpServletResponse response) {
		ResultVo resultVo = new ResultVo();
		try {
			
			String resourcesName = request.getParameter("param");
			String inputName = request.getParameter("name");//前台页面上  input的name属性的值
			if(resourcesService.checkNameRepeat(resourcesName)==0){
				resultVo.setInfo("该名称可以使用");
				resultVo.setStatus("y");
			}else{
				if(resourcesName.equals(inputName)){
					resultVo.setInfo("该名称可以使用");
					resultVo.setStatus("y");
				}else{
					resultVo.setInfo("该名称已经被使用");
					resultVo.setStatus("n");
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			resultVo.setStatus(0);
			resultVo.setInfo("验证异常！");
		}
		return resultVo;
	}
	
}
