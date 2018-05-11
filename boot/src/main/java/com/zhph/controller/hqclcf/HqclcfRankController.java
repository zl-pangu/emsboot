package com.zhph.controller.hqclcf;

import com.zhph.commons.ConstantCtl;
import com.zhph.model.hqclcf.HqclcfRank;
import com.zhph.model.vo.ResultVo;
import com.zhph.service.hqclcf.HqclcfRankService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(ConstantCtl.HQCLC_RANK)
public class HqclcfRankController{
	@Autowired
	private HqclcfRankService hqclcfRankService;
	/**
	 * 根据查询条件进行查询再分页
	 * @return
	 */
	@RequestMapping(ConstantCtl.LIST)
	@ResponseBody
	public Grid<HqclcfRank> list(PageBean pageBean, HqclcfRank hqclcfRank) throws Exception {
		Grid<HqclcfRank> grid = hqclcfRankService.getRankByCondition(pageBean, hqclcfRank);
		return grid;
	}

	/**
	 * 左边点击职级管理右边跳转页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(ConstantCtl.INIT)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName(ConstantCtl.HQCLC_RANK_INITVIEW_NAME);
		return model;
	}

	/**
	 * 新增职级
	 * 
	 * @param hqclcfRank
	 * @return
	 */
	@RequestMapping(ConstantCtl.ADD)
	@ResponseBody
	public ResultVo insert(HqclcfRank hqclcfRank) throws Exception{
		return hqclcfRankService.insert(hqclcfRank);
	}

	/**
	 * 删除职级
	 * 
	 * @param hqclcfRank
	 * @return
	 */
	@RequestMapping(ConstantCtl.DEL)
	@ResponseBody
	public ResultVo deleteByPrimaryKey(HqclcfRank hqclcfRank) throws Exception{
		return hqclcfRankService.deleteByPrimaryKey(hqclcfRank);
	}
	/**
	 * 修改职级信息
	 * 
	 * @param hqclcfRank
	 * @return
	 */
	@RequestMapping(ConstantCtl.EDIT)
	@ResponseBody
	public ResultVo updateByPrimaryKeySelective(HqclcfRank hqclcfRank) throws Exception{
		return hqclcfRankService.updateByPrimaryKeySelective(hqclcfRank);
	}

	/**
	 * ajax获取最新职级信息 优化
	 * @return
	 */
	@RequestMapping(value = "/latestRank")
	@ResponseBody
	public List<HqclcfRank> getLatestRank() throws Exception{
		return hqclcfRankService.getLatestRank();
	}

	/**
	 * check rankName is used !
	 * @param rankName
	 * @return
	 */
	@RequestMapping("/checkRankName")
	@ResponseBody
	public ResultVo checkRankName(@Param("rankName") String rankName,@Param("no") String no) throws Exception{
		return hqclcfRankService.checkRankName(rankName,no);
	}

}
