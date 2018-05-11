package com.zhph.service.hqclcf.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.mapper.hqclcf.HqclcfBusinessMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.mapper.hqclcf.HqclcfPostMapper;
import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfPost;
import com.zhph.model.hqclcf.HqclcfRank;
import com.zhph.model.vo.ResultVo;
import com.zhph.service.hqclcf.HqclcfBusinessService;
import com.zhph.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class HqclcfBusinessServiceImpl implements HqclcfBusinessService{

	@Autowired
	private HqclcfBusinessMapper hqclcfBusinessMapper;
	@Autowired
	private HqclcfEmpMapper hqclcfEmpMapper;
	@Autowired
	private HqclcfPostMapper heHqclcfPostMapper;
	public static final Logger logger = LogManager.getLogger(HqclcfBusinessServiceImpl.class);
	@Override
	public Grid<HqclcfBusiness> getBusinessByCondition(PageBean pageBean, HqclcfBusiness hqclcfBusiness) throws Exception{
		 PageHelper.startPage(pageBean.getPage(),pageBean.getLimit());
		 List<HqclcfBusiness> dataList = hqclcfBusinessMapper.getBusinessByCondition(hqclcfBusiness);
	        PageInfo<HqclcfBusiness> pageInfo=new PageInfo<>(dataList);
	        Grid<HqclcfBusiness> grid=new Grid<>();
	        grid.setData(pageInfo.getList());
	        grid.setCount(pageInfo.getTotal());
	        return grid;
	}
	@Override
	public ResultVo insert(HqclcfBusiness hqclcfBusiness) throws Exception{
		ResultVo resultVo = new ResultVo();
		ResultVo vo = checkPosName(hqclcfBusiness.getPosName(),null);
		if(vo.getStatus().toString().equals(Constant.DISABLE)){
			vo.setStatus(0);
			vo.setInfo("该职务名已被使用！");
			return vo;
		}
		try {
			// 判断传入对象是否为空
			if (null != hqclcfBusiness) {
				// 获取当前操作用户
				if (null != CommonUtil.getOnlineFullName()) {
					// 创建者
					hqclcfBusiness.setCreator(CommonUtil.getOnlineFullName());
				}
				// 创建时间
				hqclcfBusiness.setCreateTime(DateUtil.getCurrentDate("yyyy-MM-dd"));
				hqclcfBusiness.setPosCode(getRankNoIdByUUId());

				hqclcfBusinessMapper.insert(hqclcfBusiness);
				resultVo.setStatus(1);
				resultVo.setInfo("保存成功！");
			} else {
				resultVo.setStatus(0);
				resultVo.setInfo("传入数据为空！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultVo.setStatus(0);
			resultVo.setInfo("保存异常！");
			logger.error("职务数据保存异常" + e.getMessage());
		}
		return resultVo;
	}

	@Override
	public ResultVo judgeStatus(String posCode,String value) throws Exception{
		ResultVo rs = new ResultVo();
		List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq("","",new ArrayList<Integer>());
		if(value.equals("0")){
			for(HqclcfEmp emp : emps){
				if(emp.getPosition()!=null){
					if(emp.getPosition().equals(posCode)){
						rs.setStatus(0);
						rs.setInfo("该职务正在被使用中！");
						return rs;
					}
				}
			}
		}
		rs.setStatus(1);
		return rs;
	}

	@Override
	public ResultVo checkPosName(String posName,String posNo) throws Exception {
		ResultVo vo = new ResultVo();
		HqclcfBusiness hqclcfBusiness = new HqclcfBusiness();
		if(null!=posName && !"".equals(posName)){

			if(null!=posNo && !"".equals(posNo)){
				String noName = hqclcfBusinessMapper.queryBusinessByCode(posNo).getPosName();  //检测当前posNo 的原职务名
				vo.setStatus(noName.equals(posName)?Constant.ENABLE:Constant.DISABLE);   // 和原职务名相同为1，false为0
				if(vo.getStatus().toString().equals(Constant.ENABLE)){   // 为0 提示
					return vo;
				}
			}

			hqclcfBusiness.setPosName(posName.trim());
			List<HqclcfBusiness> dataList = hqclcfBusinessMapper.checkName(hqclcfBusiness);
			if(dataList.size()>0){
				vo.setInfo("该职务已被使用!");
				vo.setStatus('0');
				return vo;
			}
		}
		vo.setStatus('1');
		return vo;
	}

	@Override
	public ResultVo deleteByPrimaryKey(HqclcfBusiness hqclcfBusiness) throws Exception{
		ResultVo resultVo = new ResultVo();
		HqclcfBusiness business = hqclcfBusinessMapper.queryBusinessByPrimaryKey(hqclcfBusiness.getPrinumber());
		List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq("","",new ArrayList<Integer>());
		for(HqclcfEmp emp : emps){
			if(emp.getPosition()!=null){
				if(emp.getPosition().equals(business.getPosCode())){
					resultVo.setInfo("有员工正在使用！");
					resultVo.setStatus(0);
					return resultVo;
				}
			}
		}
		List<HqclcfPost> post = heHqclcfPostMapper.queryAll(new HqclcfPost());
		for(HqclcfPost p : post){
			if(p.getRankNo()!=null && !p.getRankNo().equals("")){
				if(p.getRankNo().equals(business.getPosCode())){
					resultVo.setInfo("有岗位正在使用！");
					resultVo.setStatus(0);
					return resultVo;
				}
			}

		}


		try {
			// 判断传入的状态是否为启用
			if (!StringUtil.isEmpty(hqclcfBusiness.getStatus())) {
				// 删除该职务
				if(!StringUtil.isEmpty(hqclcfBusiness.getPrinumber())){
					hqclcfBusinessMapper.deleteByPrimaryKey(hqclcfBusiness.getPrinumber());
					resultVo.setStatus(1);
					resultVo.setInfo(" 删除成功!");
				}else{
					resultVo.setStatus(0);
					resultVo.setInfo("传入id不能为空！");
				}
			} else {
				resultVo.setStatus(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("职务删除异常！" + e.getMessage());
			resultVo.setStatus(0);
		}
		
		return resultVo;
	}
	@Override
	public ResultVo updateByPrimaryKeySelective(HqclcfBusiness hqclcfBusiness) throws Exception{
		ResultVo resultVo = new ResultVo();

		ResultVo vo = checkPosName(hqclcfBusiness.getPosName(),hqclcfBusiness.getPosCode());
		if(vo.getStatus().toString().equals(Constant.DISABLE)){
			vo.setStatus(0);
			vo.setInfo("该职务名已被使用！");
			return vo;
		}

		if(hqclcfBusiness.getStatus().equals(Constant.DISABLE)){
			HqclcfPost post = new HqclcfPost();
			HqclcfBusiness business = hqclcfBusinessMapper.queryBusinessByPrimaryKey(hqclcfBusiness.getPrinumber());
			post.setPostNo(hqclcfBusiness.getPosCode());
			List<HqclcfPost> posts = heHqclcfPostMapper.queryAll(post);
			List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq("","",new ArrayList<Integer>());
			for(HqclcfEmp emp : emps){
				if(emp.getPosition()!=null && !emp.getPosition().equals("")){
					if(emp.getPosition().equals(business.getPosCode())){
						resultVo.setInfo("无法禁用,有员工正在使用");
						resultVo.setStatus(0);
						return resultVo;
					}
				}
			}
			for(HqclcfPost p : posts){
				if(p.getRankNo()!=null && !p.getRankNo().equals("")){
					if(p.getRankNo().equals(business.getPosCode())){
						resultVo.setInfo("无法禁用,有岗位正在使用");
						resultVo.setStatus(0);
						return resultVo;
					}
				}
			}


		}

		try {
			// 修改选中的职务信息
			hqclcfBusinessMapper.updateByPrimaryKeySelective(hqclcfBusiness);
			resultVo.setStatus(1);
			resultVo.setInfo("修改成功！");
		} catch (Exception e) {
			resultVo.setStatus(0);
			resultVo.setInfo("修改失败！");
		}
		return resultVo;
	}

	/**
	 * 生成职级编号
	 * @return
	 */
	public String getRankNoIdByUUId() {
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		if(hashCodeV < 0) {
			//有可能是负数
			hashCodeV = - hashCodeV;
		}
		// 0 代表前面补充0
		// 12 代表长度为12
		// d 代表参数为正数型
		return "ZW"+Md5Util.encode(String.format("%012d", hashCodeV)).substring(0, 5);
	}

	@Override
	public List<HqclcfRank> getRankList() throws Exception {
		return hqclcfBusinessMapper.selectRankList();
	}

	@Override
	public HqclcfBusiness queryBusinessByNo(String no) throws Exception {
		return hqclcfBusinessMapper.queryBusinessByCode(no);
	}

}
