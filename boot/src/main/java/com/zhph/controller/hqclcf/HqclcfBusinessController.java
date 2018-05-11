package com.zhph.controller.hqclcf;

import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.controller.base.BaseController;
import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.vo.ResultVo;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfBusinessService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(ConstantCtl.HQCLC_BUSINESS)
public class HqclcfBusinessController extends BaseController {
	@Autowired
	private HqclcfBusinessService hqclcfBusinessService;

	@Autowired
	private BaseService baseService;
	/**
	 * 数据信息查询
	 *
	 * @return
	 */
	@RequestMapping(ConstantCtl.LIST)
	@ResponseBody
	public Grid<HqclcfBusiness> list(PageBean pageBean,  HqclcfBusiness hqclcfBusiness) throws Exception{
		Grid<HqclcfBusiness> grid = hqclcfBusinessService.getBusinessByCondition(pageBean, hqclcfBusiness);
		return grid;
	}
	/**
	 * 页面初始化并且跳转到职务页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(ConstantCtl.INIT)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView model = new ModelAndView();
		baseService.addObject(model, Constant.RANK_CODE_LIST, Constant.RANK_TYPE);//职级类别初始化下拉展示
		model.addObject("rankList",hqclcfBusinessService.getRankList());//职级类别
		model.setViewName(ConstantCtl.HQCLC_BUSINESS_INITVIEW_NAME);
		return model;
	}

	/**
	 * 新增职务
	 * 
	 * @param hqclcfBusiness
	 * @return
	 */
	@RequestMapping(ConstantCtl.ADD)
	@ResponseBody
	public ResultVo insert(HqclcfBusiness hqclcfBusiness) throws Exception{
		return hqclcfBusinessService.insert(hqclcfBusiness);
	}

	/**
	 * 删除职务
	 * 
	 * @param hqclcfBusiness
	 * @return
	 */
	@RequestMapping(ConstantCtl.DEL)
	@ResponseBody
	public Object deleteByPrimaryKey(HqclcfBusiness hqclcfBusiness) throws Exception{
		return hqclcfBusinessService.deleteByPrimaryKey(hqclcfBusiness);
	}

	/**
	 * 修改职务信息
	 *
	 * @return
	 */
	@RequestMapping(ConstantCtl.EDIT)
	@ResponseBody
	public Object updateByPrimaryKeySelective(HqclcfBusiness hqclcfBusiness) throws Exception{
		return hqclcfBusinessService.updateByPrimaryKeySelective(hqclcfBusiness);
	}

	/**
	 * 通过职务编码获取职级code
	 * @param no
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryBusinessByNo")
	@ResponseBody
	public HqclcfBusiness queryBusinessByNo(@Param("no") String no)throws Exception{
		return hqclcfBusinessService.queryBusinessByNo(no);
	}

	/**
	 * 判断职务是否正在被使用
	 * @param posCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ConstantCtl.HQCLCFPOST_STATUS_SWITCH,method = RequestMethod.POST)
	@ResponseBody
	public Object judgeStatus(@Param("posCode") String posCode,@Param("value") String value) throws Exception{
		return hqclcfBusinessService.judgeStatus(posCode,value);
	}


	/**
	 * 判断职务名是否被使用ajax
	 * @param posName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/checkPosName")
	@ResponseBody
	public Object checkPosName(@Param("posName") String posName,@Param("posNo") String posNo) throws Exception{
		return hqclcfBusinessService.checkPosName(posName,posNo);
	}

}
