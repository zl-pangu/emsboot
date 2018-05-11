package com.zhph.service.sys.imp;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.exception.AppException;
import com.zhph.model.sys.dto.SysConfigAddParams;
import com.zhph.model.sys.dto.SysUserAddParams;
import com.zhph.util.CommonUtil;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import com.zhph.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhph.mapper.sys.SysConfigTypeMapper;
import com.zhph.model.sys.SysConfigType;
import com.zhph.service.sys.SysConfigTypeService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysConfigTypeServiceImpl implements SysConfigTypeService {

	@Autowired
	private SysConfigTypeMapper sysConfigTypeMapper;

	/**
	 * 
	 * @Title getConfigTypesByPSysCode
	 * @Description TODO 根据父级编码 查询旗下所有子级
	 * @param pSysCode
	 * @param
	 * @return List<SysConfigType> 返回类型
	 *
	 */
	public List<SysConfigType> getConfigTypesByPSysCode(String pSysCode) {

		return sysConfigTypeMapper.getConfigByPSysCode(pSysCode);
	}

	/**
	 * 
	 * @Title getConfigValueByCodeAndPSysCode
	 * @Description TODO 根据父级编码 和其子级名称 查询 子级所对应的值
	 * @param pSysCode
	 * @param sysName
	 * @param
	 * @return Integer 返回类型
	 *
	 */
	public Integer getConfigValueByCodeAndPSysCode(String pSysCode, String sysName) {

		Integer value = null;
		List<SysConfigType> list = sysConfigTypeMapper.getConfigByPSysCode(pSysCode);

		for (SysConfigType sysConfigType : list) {
			if (sysConfigType.getSysName().equals(sysName)) {
				value = sysConfigType.getSysValue();
			}
		}
		return value;
	}

	/**
	 * 
	 * @Title getCofigNameByCodeAndPSysCode
	 * @Description TODO 根据父级编码 和其子级值 查询 子级所对应的名字
	 * @param @param
	 *            pSysCode
	 * @param @param
	 *            sysValue
	 * @param @return
	 *            参数
	 * @return String 返回类型
	 *
	 */
	public String getCofigNameByCodeAndPSysCode(String pSysCode, int sysValue) {

		String sysName = "";
		List<SysConfigType> list = sysConfigTypeMapper.getConfigByPSysCode(pSysCode);
		if (list.size() > 0) {
			for (SysConfigType sysConfigType : list) {
				if (sysConfigType.getSysValue() == sysValue) {
					sysName = sysConfigType.getSysName();
				}
			}
		}
		return sysName;
	}
	@Override
	public Grid<SysConfigType> queryPageInfo(PageBean pageBean, SysConfigType configType) throws Exception {
		PageHelper.startPage(pageBean.getPage(),pageBean.getLimit());
		List<SysConfigType> list = sysConfigTypeMapper.queryList(configType);
		PageInfo<SysConfigType> pageInfo=new PageInfo<>(list);
		Grid<SysConfigType> grid=new Grid<>();
		grid.setData(pageInfo.getList());
		grid.setCount(pageInfo.getTotal());
		return grid;
	}

	@Override
	public List<SysConfigType> queryPConfigType(String q) throws Exception {
		List<SysConfigType> list = sysConfigTypeMapper.queryPConfigType(q);
		return list;
	}

	@Override
	public int queryOrder(String sysCode) throws Exception {
		SysConfigType configType=new SysConfigType();
		configType.setpSysCode(sysCode);
		Integer maxSort = sysConfigTypeMapper.querySortBypCode(configType);
		return  null != maxSort ? (maxSort + 1) : 0;
	}

	@Override
	public String dictionaryCoding(String sysName, String pCode) throws Exception {
		StringBuilder sb=new StringBuilder("");
		if (!"".equals(pCode)&&pCode!=null){
			if (!"".equals(sysName)&&sysName!=null){
				SysConfigType checkSysName= new SysConfigType();
				checkSysName.setpSysCode(pCode);
				checkSysName.setSysName(sysName);
				if (sysConfigTypeMapper.queryDuplication(checkSysName)>0)
					throw new AppException(pCode+":父节点下已经有为：（"+sysName+"）子节点的名字了");
				String newSysCode = pCode + "_" + CommonUtil.cn2FirstSpell(sysName);
				SysConfigType checkSysCode= new SysConfigType();
				checkSysCode.setpSysCode(pCode);
				checkSysCode.setSysCode(newSysCode);
				if (sysConfigTypeMapper.queryDuplication(checkSysCode)>0){
					sb.append(newSysCode + "_" + RandomUtil.generateGUIDByNum(2));
				}else{
					sb.append(newSysCode);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 新增
	 * @param data
	 * @throws Exception
	 */
	@Override
	public void add(String data) throws Exception {
		ObjectMapper mapper=new ObjectMapper();
		SysConfigAddParams params = mapper.readValue(data.getBytes(), SysConfigAddParams.class);
		Integer addType = params.getAddType();
		switch (addType){
			case 1://新增子节点
				SysConfigType addChildType=new SysConfigType();
				addChildType.setSysName(params.getSysName1());
				addChildType.setSysCode(params.getSysCode1());
				addChildType.setpSysCode(params.getpCode());
				addChildType.setSort(params.getChildOrder());
				addChildType.setSysValue(params.getChildOrder());//子节点的值默认为等于排序最大值
				addChildType.setpId(params.getpId());
				addChildType.setpSysCode(params.getpCode());
				addChildType.setDescription(params.getDisplay1());
				sysConfigTypeMapper.addSysType(addChildType);
				break;
			case 0://新增父节点
				SysConfigType addPtype=new SysConfigType();
				addPtype.setSysCode(params.getSysCode());
				addPtype.setSysName(params.getSysName());
				addPtype.setDescription(params.getDisplay0());
				addPtype.setSort(params.getOrder0());
				sysConfigTypeMapper.addSysType(addPtype);
				break;
		}
	}

	@Override
	public void del(Long id) throws Exception {
		SysConfigType configType = sysConfigTypeMapper.queryConfigTypeById(id);
		String sysCode = configType.getSysCode();
		SysConfigType params=new SysConfigType();
		params.setpSysCode(sysCode);
		int i = sysConfigTypeMapper.queryDuplication(params);
		if (i>0){
			throw new AppException("无法删除，有在使用的子节点！");
		}else{
			sysConfigTypeMapper.del(id);
		}
	}

	@Override
	public SysConfigType queryObjById(Long id) throws Exception {
		SysConfigType configType = sysConfigTypeMapper.queryConfigTypeById(id);
		return configType;
	}

	@Override
	public void update(String data) throws Exception {
		ObjectMapper mapper=new ObjectMapper();
		SysConfigType configType = mapper.readValue(data.getBytes(), SysConfigType.class);
		sysConfigTypeMapper.update(configType);
	}

}
