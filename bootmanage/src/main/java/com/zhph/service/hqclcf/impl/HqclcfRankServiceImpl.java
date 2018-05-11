package com.zhph.service.hqclcf.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.mapper.hqclcf.HqclcfBusinessMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.mapper.hqclcf.HqclcfRankMapper;
import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfRank;
import com.zhph.model.vo.ResultVo;
import com.zhph.service.hqclcf.HqclcfRankService;
import com.zhph.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class HqclcfRankServiceImpl implements HqclcfRankService {
	@Autowired
	private HqclcfRankMapper hqclcfRankMapper;
	@Autowired
	private HqclcfEmpMapper hqclcfEmpMapper;
	@Autowired
	private HqclcfBusinessMapper hqclcfBusinessMapper;
	public static final Logger logger = LogManager.getLogger(HqclcfRankServiceImpl.class);

	@Override
	public Grid<HqclcfRank> getRankByCondition(PageBean pageBean, HqclcfRank hqclcfRank) throws Exception {
		PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
		List<HqclcfRank> dataList = hqclcfRankMapper.getRankByCondition(hqclcfRank);
		PageInfo<HqclcfRank> pageInfo = new PageInfo<>(dataList);
		Grid<HqclcfRank> grid = new Grid<>();
		grid.setData(pageInfo.getList());
		grid.setCount(pageInfo.getTotal());
		return grid;
	}

	@Override
	public ResultVo checkRankName(String rankName,String no) throws Exception {
		ResultVo vo = new ResultVo();
		HqclcfRank rank = new HqclcfRank();
		if(null!=rankName && !"".equals(rankName)){
			rank.setName(rankName.trim());
			if(null!=no && !"".equals(no)){
				String noName = hqclcfRankMapper.getRankByNo(no).getName();
				vo.setStatus(noName.equals(rankName)?Constant.ENABLE:Constant.DISABLE);
				if(vo.getStatus().toString().equals(Constant.ENABLE)){
					return vo;
				}
			}
			List<HqclcfRank> dataList = hqclcfRankMapper.checkRankName(rank);
			if(dataList.size()>0){
				vo.setInfo("该职级名已被使用!");
				vo.setStatus('0');
				return vo;
			}
		}
		vo.setStatus('1');
		return vo;
	}

	@Override
	public ResultVo insert(HqclcfRank hqclcfRank) throws Exception {
		ResultVo resultVo = new ResultVo();
		try {
			// 判断传入对象是否为空
			if (null != hqclcfRank) {

				ResultVo vo = checkRankName(hqclcfRank.getName(),null);
				if(vo.getStatus().toString().equals(Constant.DISABLE)){
					vo.setStatus(0);
					vo.setInfo("职级名已被使用！");
					return vo;
				}

				// 获取当前操作用户
				if (null != CommonUtil.getOnlineFullName()) {
					// 创建者
					hqclcfRank.setCreateName(CommonUtil.getOnlineFullName());
				}
				hqclcfRank.setNo(getRankNoIdByUUId());
				// 创建时间
				hqclcfRank.setCreateDate(DateUtil.getCurrentDate("yyyy-MM-dd"));
				hqclcfRankMapper.insert(hqclcfRank);
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
			logger.error("职级数据保存异常" + e.getMessage());
		}

		return resultVo;
	}

	@Override
	public ResultVo deleteByPrimaryKey(HqclcfRank hqclcfRank) throws Exception {
		ResultVo resultVo = new ResultVo();
		Set<String> set = new HashSet();
		List<String> rankPer = new ArrayList<>();
		List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq("","",new ArrayList<Integer>());
		HqclcfRank rank = hqclcfRankMapper.selectByPrimaryKey(hqclcfRank.getPrinumber());
		for (HqclcfEmp emp : emps) {
			if (emp.getRank() != null) {
				if (emp.getRank().equals(rank.getNo())) {
					resultVo.setStatus(0);
					resultVo.setInfo("有员工正在使用该职级！");
					return resultVo;
				}
			}
		}
		List<HqclcfBusiness> business = hqclcfBusinessMapper.getBusinessByCondition(new HqclcfBusiness());
		List<String> listCode = new ArrayList<>();
		List<String> list = new ArrayList<>();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < business.size(); i++) {
			if (business.get(i).getRankCode() != null && business.get(i).getRankName() != null) {

				List<String> CodeList = Arrays.asList(business.get(i).getRankCode().split(","));
				if (CodeList.size() > 0) {
					for (String n : CodeList) {
						if (n != null && !n.equals("")) {
							listCode.add(n);
						}
					}
					List<String> NameList = Arrays.asList(business.get(i).getRankName().split(" "));
					for (String x : NameList) {
						if (x != null && !x.equals("")) {
							list.add(x);
						}
					}
				}

			}
		}

		if (listCode.size() > 0) {

			for (String s : listCode) {
				if (s != null && !s.equals("")) {
					set.add(s);
				}
			}
		}
		if (list.size() > 0) {
			for (String l : list) {
				if (l != null && !l.equals("")) {
					set.add(l);
				}
			}
		}

		Iterator<String> it = set.iterator();
		boolean res = true;
		while (it.hasNext()) {
			String value = it.next();
			if (value != null && !value.equals("")) {
				if (value.equals(rank.getNo())) {
					res = false;
				}
			}
		}

		if (res) {
			try {
				// 判断传入的状态是否为启用
				if (!StringUtil.isEmpty(hqclcfRank
						.getStatus()) /*
										 * && hqclcfRank.getStatus().equals("0")
										 */) {
					if (!StringUtil.isEmpty(hqclcfRank.getPrinumber())) {
						hqclcfRankMapper.deleteByPrimaryKey(hqclcfRank.getPrinumber());
						resultVo.setStatus(1);
						resultVo.setInfo("删除成功！");
					} else {
						resultVo.setStatus(0);
						resultVo.setInfo("传入id不能为空！");
					}
				} else {
					/* resultVo.setInfo("启用状态的职级不能被删除！"); */
					resultVo.setStatus(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("职级删除异常！" + e.getMessage());
				resultVo.setStatus(0);
			}
		} else {
			resultVo.setStatus(0);
			resultVo.setInfo("有职务正在使用中！");
		}

		return resultVo;
	}

	@Override
	public ResultVo updateByPrimaryKeySelective(HqclcfRank hqclcfRank) throws Exception {
		ResultVo resultVo = new ResultVo();
		ResultVo vo = checkRankName(hqclcfRank.getName().trim(),hqclcfRank.getNo());
		if(vo.getStatus().toString().equals(Constant.DISABLE)){
			vo.setStatus(0);
			vo.setInfo("职级名已被使用！");
			return vo;
		}
		Set<String> set = new HashSet<>();
		HqclcfRank rank = hqclcfRankMapper.selectByPrimaryKey(hqclcfRank.getPrinumber());
		try {

			if (hqclcfRank.getStatus().equals(com.zhph.commons.Constant.DISABLE)) {
				/* 职务 */
				List<HqclcfBusiness> business = hqclcfBusinessMapper.getBusinessByCondition(new HqclcfBusiness());
				for (HqclcfBusiness b : business) {
					if (b.getRankName() != null && !b.getRankName().equals("")) {
						List<String> NameList = Arrays.asList(b.getRankName().split(" "));
						List<String> CodeList = Arrays.asList(b.getRankCode().split(","));
						for (int i = 0; i < CodeList.size(); i++) {
							if (NameList.get(i) != null && !NameList.get(i).equals("")) {
								set.add(NameList.get(i));
							}
							if (CodeList.get(i) != null && !CodeList.get(i).equals("")) {
								set.add(CodeList.get(i));
							}

						}
					}
				}
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					String value = it.next();
					if (value != null && !value.equals("")) {
						if (value.equals(rank.getNo())) {
							resultVo.setStatus(0);
							resultVo.setInfo("有职务正在使用该职级！");
							return resultVo;
						}
					}
				}
				/* 人员 */
				List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq("","",new ArrayList<Integer>());
				for (HqclcfEmp emp : emps) {
					if (emp.getRank() != null && !emp.getRank().equals("")) {
						if (emp.getRank().equals(rank.getNo())) {
							resultVo.setStatus(0);
							resultVo.setInfo("有员工正在使用该职级！");
							return resultVo;
						}
					}
				}
			}

			// 修改选中的职级信息
			hqclcfRankMapper.updateByPrimaryKeySelective(hqclcfRank);
			resultVo.setStatus(1);
			resultVo.setInfo("修改成功！");
		} catch (Exception e) {
			resultVo.setStatus(0);
			resultVo.setInfo("查询数据失败！"+e.getMessage());
		}
		return resultVo;
	}

	/**
	 * 生成职级编号
	 *
	 * @return
	 */
	public String getRankNoIdByUUId() {
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		if (hashCodeV < 0) {
			// 有可能是负数
			hashCodeV = -hashCodeV;
		}
		// 0 代表前面补充0
		// 12 代表长度为12
		// d 代表参数为正数型
		return "ZH" + Md5Util.encode(String.format("%012d", hashCodeV)).substring(0, 4);
	}

	/**
	 * 重新获取最新职级消息
	 *
	 * @return
	 */
	public List<HqclcfRank> getLatestRank() throws Exception {
		List<HqclcfRank> ranks = hqclcfRankMapper.getLatestRank();
		for (int i = 0; i < ranks.size(); i++) {
			if (ranks.get(i) == null || ranks.get(i).getStatus().equals(com.zhph.commons.Constant.DISABLE)) {
				ranks.remove(i);
			}
		}
		return ranks;
	}

	@Override
	public HqclcfRank getRankByNo(String no) throws Exception {
		return hqclcfRankMapper.getRankByNo(no);
	}

}
