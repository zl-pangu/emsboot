package com.zhph.service.cf.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.exception.AppException;
import com.zhph.mapper.cf.DaysNodeMapper;
import com.zhph.mapper.cf.HqclcfGzymMapper;
import com.zhph.mapper.cf.CfVacateManageMapper;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.model.cf.DaysNode;
import com.zhph.model.common.RankType;
import com.zhph.model.cf.CfVacateManage;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfGzym;
import com.zhph.model.hqclcf.HqclcfRank;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysHoliday;
import com.zhph.service.cf.CfVacateManageService;
import com.zhph.service.sys.SysConfigTypeService;
import com.zhph.service.sys.SysHolidayService;
import com.zhph.util.CalculateHoursUtil;
import com.zhph.util.DateUtil;
import com.zhph.util.ExcelUtil;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import com.zhph.util.SplitDateUtil;
import com.zhph.util.StringUtil;


@Service
@Transactional(rollbackFor = Exception.class)
public class CfVacateManageServiceImpl implements CfVacateManageService {

	protected Log log = LogFactory.getLog(CfVacateManageServiceImpl.class);
	@Resource
    private CfVacateManageMapper cfVacateManageMapper;
    @Resource
    private SysHolidayService sysHolidayService;
    @Autowired
    HqclcfDeptMapper hqclcfDeptMapper;
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	@Resource
	private DaysNodeMapper daysNodeMapper;
	@Resource
	private HqclcfGzymMapper hqclcfGzymMapper;
	@Resource
	private SysConfigTypeService sysConfigTypeService;
    @Override
    public Grid<CfVacateManage> queryByPage(CfVacateManage queryParams, PageBean pageBean)
        throws AppException {
    	if(pageBean != null){
    		PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
    	}
        List<CfVacateManage> list = cfVacateManageMapper.queryByPage(queryParams);
        PageInfo<CfVacateManage> pageInfo=new PageInfo<>(list);
        Grid<CfVacateManage> grid=new Grid<>();
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
        return grid;
    }
    
    @Override
    public Long queryVacateManageCount(CfVacateManage queryParams)
        throws Exception {
        return cfVacateManageMapper.queryVacateManageCount(queryParams);
    }

    @Override
    public Json calculationbydatas(String empNo, Date startDate, Date endDate,String priNumber)
        throws Exception {
    	Json json = new Json();
		Long id = StringUtil.isEmpty(priNumber) ? 0 : Long.parseLong(priNumber);
        CfVacateManage queryParams = new CfVacateManage();
        queryParams.setStartTime(DateUtil.parseDateFormat(startDate, "yyyy-MM-dd HH"));
        queryParams.setEndTime(DateUtil.parseDateFormat(endDate, "yyyy-MM-dd HH"));
        queryParams.setEmpNo(empNo);
		List<CfVacateManage> list = cfVacateManageMapper.queryByPage(queryParams);
		StringBuffer sb = new StringBuffer("你选择的请假时间中，部分时间和已有的请假申请时间段");
		if(list != null && list.size() > 0){
			for (CfVacateManage cfVacateManage : list) {
				if(cfVacateManage.getPriNumber() - id != 0.0){
					sb.append("[").append(cfVacateManage.getStartTime()).append("-").append(cfVacateManage.getEndTime()).append("]");
				}
			}
			sb.append("重复，请重新选择！");
			json.setMsg(sb.toString());
			json.setSuccess(false);
			return json;
		}
    	
        BigDecimal totalday = new BigDecimal(0);
        /*TODO <NEW_CHANGE_FOR_RANK_TYPE>由于新的职级管理中没有职级类型，此外无法做任何判断，只能采用统一的判断方式*/
        List<Map<String, Object>> queryByempNo = cfVacateManageMapper.queryByempNo(empNo);
        if(queryByempNo == null || queryByempNo.size() <=0){
        	json.setMsg("计算请假天数失败，未查询到员工相关信息！");
        	json.setSuccess(false);
        	return json;
        }
        String type = queryByempNo.get(0).get("POSITION").toString();//新的职位对应原来的职级
        // 若员工的职级类别为客户代表（老系统代码是5）、团队经理（老系统代码是4），则请假天数为请假时间之间的差
        if (RankType.TEAM_MANAGER.getNum().equals(type) || RankType.CUSTOMER_STAND.getNum().equals(type)) {
            float leaveHours = CalculateHoursUtil.calculateHours(startDate, endDate);
            BigDecimal remainder = new BigDecimal(leaveHours % 8);
            Double merchant = new Double(leaveHours / 8);
            BigDecimal t1 = new BigDecimal(Math.floor(merchant));
            BigDecimal t2 = new BigDecimal(0.5);
            int checkT2 = remainder.intValue();
            if (checkT2 == 3 || checkT2 == 5) {
                totalday = t1.add(t2);
            }else {
                totalday = t1;
            }
        }
        // 若员工的职级类别为行政专员（老系统代码是6）、营业部经理（老系统代码是3）、城市经理（老系统代码是2）
        else if (RankType.CITY_MANAGER.getNum().equals(type) || RankType.SALES_MANAGER.getNum().equals(type) || RankType.ADMIN_STAFF.getNum().equals(type)) {
        	//查询(startDate上个月1号)到(endDate下下个月1号)
            SysHoliday holiday = new SysHoliday();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);//查询
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            holiday.setHolidayStartDate(cal.getTime());
            cal.setTime(endDate);//查询
            cal.add(Calendar.MONTH, 2);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            holiday.setHolidayEndDate(cal.getTime());
            
            List<SysHoliday> holidays = sysHolidayService.queryAll(holiday);
            float leaveHours = CalculateHoursUtil.calculateHoursOthersStaff(startDate, endDate, holidays);
            BigDecimal remainder = new BigDecimal(leaveHours % 8);
            Double merchant = new Double(leaveHours / 8);
            BigDecimal t1 = new BigDecimal(Math.floor(merchant));
            BigDecimal t2 = new BigDecimal(0.5);
            int checkT2 = remainder.intValue();
            if (checkT2 == 3 || checkT2 == 5) {
                totalday = t1.add(t2);
            } else {
                totalday = t1;
            }
       }else{
    	   json.setMsg("未知的消分职务，请联系管理员！");
    	   json.setSuccess(false);
    	   return json;
       }
        json.setSuccess(true);
        json.setObj(totalday.toString());
        return json;
    }
    
    @Override
    public List<Map<String, Object>> queryByempNo(String empNo) {
        return cfVacateManageMapper.queryByempNo(empNo);
    }
    
    /**
     * 提交数据：未提交和已拒绝的数据
     */
    @Override
    public Json submitThisDatas(List<Long> list)
        throws Exception {
    	Json json = new Json();
        try {
        	if(list == null || list.size() <= 0){
        		json.setMsg("需求提交的数据为空！");
        		json.setSuccess(false);
        		return json;
        	}else{
        		Map<String,Object> map = new HashMap<String,Object>();
        		map.put("status", Constant.NOMAL_APPROVE_STAT_WAITING_APPROVE);// 设置为未审批状态
        		map.put("ids", list);
        		map.put("condStatus", new String[]{Constant.NOMAL_APPROVE_STAT_NOTCOMMIT,Constant.NOMAL_APPROVE_STAT_REFUSE});//未提交和已拒绝
        		int i  = cfVacateManageMapper.updateStatusByIds(map);
        		if(i < list.size()){
					json.setMsg("成功提交" + i + "条!");
            		json.setSuccess(false);
            		return json;
        		}
        		json.setSuccess(true);
        		return json;
        	}
        } catch (Exception e) {
            log.error("提交数据失败！",e);
            throw new Exception("提交数据失败！");
        }
    }
    
    /**
     * 撤销，待审核的数据
     */
    @Override
    public Json revokeThisDatas(List<Long> list)
        throws Exception {
    	Json json = new Json();
        try {
        	if(list == null || list.size() <= 0){
        		json.setMsg("需求撤销的数据为空！");
        		json.setSuccess(false);
        		return json;
        	}else{
        		Map<String,Object> map = new HashMap<String,Object>();
        		map.put("status", Constant.NOMAL_APPROVE_STAT_NOTCOMMIT);// 设置未提交状态
        		map.put("ids", list);
        		map.put("condStatus", new String[]{Constant.NOMAL_APPROVE_STAT_WAITING_APPROVE});//未提交和已拒绝
        		int i  = cfVacateManageMapper.updateStatusByIds(map);
        		if(i < list.size()){
					json.setMsg("成功撤销" + i + "条!");
            		json.setSuccess(false);
            		return json;
        		}
        		json.setSuccess(true);
        		return json;
        	}
        } catch (Exception e) {
            log.error("撤销数据失败！",e);
            throw new Exception("撤销数据失败！");
        }
    }
    
    /**
     * 审核
     */
    @Override
    public Json reviewThisDatas(List<Long> list, String status, String auditOpinion)
        throws Exception {
    	Json json = new Json();
        try {
        	if(list == null || list.size() <= 0){
        		json.setMsg("需求审核的数据为空！");
        		json.setSuccess(false);
        		return json;
        	}else{
        		Map<String,Object> map = new HashMap<String,Object>();
        		map.put("status", status);// 设置为提交状态
        		map.put("auditOpinion", auditOpinion);
        		map.put("ids", list);
        		map.put("condStatus", new String[]{Constant.NOMAL_APPROVE_STAT_WAITING_APPROVE});//待审核
        		int i  = cfVacateManageMapper.updateStatusByIds(map);
        		if(i < list.size()){
					json.setMsg("成功审核" + i + "条!");
            		json.setSuccess(false);
            		return json;
        		}
        		json.setSuccess(true);
        		return json;
        	}
        } catch (Exception e) {
            log.error("审核数据失败！",e);
            throw new Exception("审核数据失败！");
        }
    }
    
    /**
     * 反审核：已通过的数据
     */
    @Override
    public Json unreviewThisdatas(List<Long> list)
        throws Exception {
    	Json json = new Json();
        try {
        	if(list == null || list.size() <= 0){
        		json.setMsg("需求反审核的数据为空！");
        		json.setSuccess(false);
        		return json;
        	}else{
        		Map<String,Object> map = new HashMap<String,Object>();
        		map.put("status", "1");// 设置为提交状态
        		map.put("ids", list);
        		map.put("condStatus", new String[]{Constant.NOMAL_APPROVE_STAT_PASS});//未提交和已拒绝
        		int i  = cfVacateManageMapper.updateStatusByIds(map);
        		if(i < list.size()){
					json.setMsg("成功反审核" + i + "条!");
            		json.setSuccess(false);
            		return json;
        		}
        		json.setSuccess(true);
        		return json;
        	}
        } catch (Exception e) {
            log.error("审核数据失败！",e);
            throw new Exception("审核数据失败！");
        }
    }
    
    @Override
    public void exportExlByquery(List<CfVacateManage> list, List<HqclcfDept> hqclcfDepts, List<HqclcfRank> hqclcfRanks,
        HttpServletRequest request, HttpServletResponse response) {
        String[] colTitleAry = {"序号","员工编码", "员工姓名", "大区", "分公司", "营业部", "团队", "请假类型", "请假时间", "请假天数", "请假原因", "状态"};
        String[][] convStr = new String[list.size()][colTitleAry.length];
        short[] colWidthAry = {100, 140, 140, 140, 140, 140, 140, 140, 250, 140, 140, 140};
        List<SysConfigType> sysConfigTypeList = sysConfigTypeService.getConfigTypesByPSysCode(Constant.LEAVE_TYPE);//请假类型
        Map<String,String> leaveTypeMap = new HashMap<String,String>();
        Map<String,String> hqclcfDeptsMap = new HashMap<String,String>();
        if(sysConfigTypeList != null && sysConfigTypeList.size() > 0){
        	for (SysConfigType sysConfigType : sysConfigTypeList) {
				leaveTypeMap.put(sysConfigType.getSysValue()+"", sysConfigType.getSysName());
			}
        }
        if (hqclcfDepts != null && hqclcfDepts.size() > 0) {
			for (HqclcfDept hqclcfDept : hqclcfDepts) {
				hqclcfDeptsMap.put(hqclcfDept.getId()+"", hqclcfDept.getDeptName());
			} 
		}
		try {
        	int i = 0;
            for (CfVacateManage vacateManage:list) {
				convStr[i][0] = i +1+ "";// 序号
                convStr[i][1] = vacateManage.getEmpNo();//员工编号
                convStr[i][2] = vacateManage.getEmpName();//员工名称
                convStr[i][3] = hqclcfDeptsMap.get(vacateManage.getRegionId());//大区
                convStr[i][4] = hqclcfDeptsMap.get(vacateManage.getFilialeId());//分公司
                convStr[i][5] = hqclcfDeptsMap.get(vacateManage.getBusinessDeptId());//营业部
                convStr[i][6] = hqclcfDeptsMap.get(vacateManage.getTeamId());//团队
                convStr[i][7] = leaveTypeMap.get(vacateManage.getLeaveType());//请假类型
                convStr[i][8] = vacateManage.getLeaveInterval();//请假时间
                convStr[i][9] = vacateManage.getTotalTime();//请假天数
                convStr[i][10] = vacateManage.getLeaveReason();//请假原因
                convStr[i][11] = Constant.getNomalApproveStatusDesc(vacateManage.getStatus());//状态
                i ++;
            }
            ExcelUtil execlUtil = new ExcelUtil();
			execlUtil.writeExecl(colTitleAry, colWidthAry, convStr, null, response,
					"请休假管理-" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()),1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public Json saveOrUpdate(CfVacateManage vacat, boolean flag, CfVacateManage manage, List<SysHoliday> holidays) throws Exception  {
		Json ret  = new Json();
		if(vacat == null){
			ret.setSuccess(false);
			ret.setMsg("传入的参数为空！");
			return ret;
		}
		int i;
		if(vacat.getPriNumber() == null){
			i = cfVacateManageMapper.save(vacat);
		}else{
			CfVacateManage old = cfVacateManageMapper.getById(vacat.getPriNumber());
			if(old == null){
				ret.setSuccess(false);
				ret.setMsg("数据不存在，更新失败！");
				return ret; 
			}
			if(!Constant.NOMAL_APPROVE_STAT_NOTCOMMIT.equals(old.getStatus()) && !Constant.NOMAL_APPROVE_STAT_REFUSE.equals(old.getStatus())){
				ret.setSuccess(false);
				ret.setMsg("只有未提交或者已拒绝的请休假才能修改！");
				return ret;
			}
			i = cfVacateManageMapper.update(vacat);
		}
		if(i <= 0){
			ret.setSuccess(false);
			ret.setMsg("保存失败！");
			return ret;
		}
		ret.setSuccess(true);
		saveDayNode(flag,manage,vacat,vacat.getPost(),holidays);
		return ret;
	}
	   /**
     * 保存请休假维度表
     */
    public void saveDayNode(boolean flag, CfVacateManage manage, CfVacateManage vacat, String rankType, List<SysHoliday> list) throws AppException {
    	if(manage.getPriNumber() != null){
    		daysNodeMapper.deleteByKeyId(manage.getPriNumber());
    	}
        if (flag) {
            //从里面查询到的数据存到维度表里面
            Map<String, String> segmenteDays = SplitDateUtil.segmenteDays(manage.getStartTime()+":00", manage.getEndTime()+":00", rankType, list);
            List<DaysNode> node=new ArrayList<DaysNode>();
            Set<Entry<String, String>> entrySet = segmenteDays.entrySet();
            Iterator<Entry<String, String>> iterator = entrySet.iterator();
    			SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
    			int i = 0;
    			try {
    				 while (iterator.hasNext()) {
    		                DaysNode nodeday=new DaysNode();
    		                Entry<String, String> entry = iterator.next();
    		                String key = entry.getKey();
    		                String value = entry.getValue();
    		                nodeday.setTreeId(vacat.getEmpNo());
    		                nodeday.setNodeNo(key);
    		                nodeday.setNodeValue(value);
    		                nodeday.setKeyId(vacat.getPriNumber());
    		                node.add(nodeday);
    		                i ++;
    		               daysNodeMapper.save(nodeday);
    		               if ((i > 0 && i % 100 == 0) || i == segmenteDays.size()) {
       						session.commit();
       						session.clearCache();
       						}
    		            }
    			} catch (Exception e) {
    				session.rollback();
    				e.printStackTrace();
    				throw e;
    			} finally {
    				session.close();
    			}
        }else{
            DaysNode nodeday=new DaysNode();
            nodeday.setTreeId(vacat.getEmpNo());
            nodeday.setNodeNo(vacat.getDateofLeave());
            nodeday.setNodeValue(manage.getTotalTime());
            nodeday.setKeyId(vacat.getPriNumber());
            daysNodeMapper.save(nodeday);
        }
    }

	@Override
	public Grid<Map<String, Object>> querySalaryEmp(Map<String, Object> queryParam, PageBean pageBean) throws Exception {
		List<Map<String, Object>> list = cfVacateManageMapper.querySalaryEmp(queryParam);
		if(list !=null && list.size() > 0){
			for (Map<String, Object> map : list) {
				String deptInfoStr = (String) map.get("DEPTINFO");
				String[] temp;
				if(!StringUtil.isEmpty(deptInfoStr)){
					String[] depts = deptInfoStr.split(",");
					for (String dept : depts) {
						temp = dept.split("=");
						if("1".equals(temp[0])){
							map.put("REGION_NAME", temp[1]);
						}else if("2".equals(temp[0])){
							map.put("ORG_NAME",  temp[1]);
						}else if("3".equals(temp[0])){
							map.put("BUSINESS_NAME", temp[1]);
						}else if("4".equals(temp[0])){
							map.put("TEAM_NAME",  temp[1]);
						}
					}
				}
			}
		}
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(list);
        Grid<Map<String, Object>> grid=new Grid<>();
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
		return grid;
	}

	@Override
	public HqclcfGzym queryCurrGzym() throws Exception {
		return hqclcfGzymMapper.queryCurrGzym();
	}

	/**
	 * 删除请休假
	 * @param priNumber
	 * @return
	 * @throws Exception
	 */
	@Override
	public Json delete(String idsStr) throws Exception {
		String[] ids = idsStr.split(",");
		int i = 0;
		if(ids != null && ids.length > 0){
			for (String id:ids) {
				daysNodeMapper.deleteByKeyId(Long.parseLong(id));
				cfVacateManageMapper.delete(id);
				i++;
			}
		}
		Json json = new Json();
		json.setSuccess(i > 0);
		json.setMsg(i > 0 ? "删除成功！" : "删除失败！");
		return json;
	}
}
