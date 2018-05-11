package com.zhph.service.hqclcf.impl;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.log.SysoCounter;
import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.mapper.hqclcf.*;
import com.zhph.mapper.sys.SysUserMapper;
import com.zhph.mapper.sys.SysWorkplacesetMapper;
import com.zhph.model.hqclcf.*;
import com.zhph.model.hqclcf.dto.HqclcfEmpApv2SelectJsonResult;
import com.zhph.model.hqclcf.dto.HqclcfEmpApvIdArray;
import com.zhph.model.sys.SysUser;
import com.zhph.model.sys.SysWorkplaceset;
import com.zhph.model.vo.ZtreeVo;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.service.hqclcf.HqclcfJobTransferService;
import com.zhph.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;



import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class HqclcfJobTransferServiceImpl implements HqclcfJobTransferService {

    public static final Logger logger = LogManager.getLogger(HqclcfJobTransferServiceImpl.class);

    @Autowired
    private HqclcfJobTransferMapper hqclcfJobTransferMapper;
    @Autowired
    private HqclcfEmpMapper hqclcfEmpMapper;
    @Autowired
    private HqclcfDeptMapper hqclcfDeptMapper;
    @Autowired
    private HqclcfBusinessMapper hqclcfBusinessMapper;
    @Autowired
    private HqclcfRankMapper hqclcfRankMapper;
    @Autowired
    private HqclcfDeptService deptService;
    @Autowired
    private HqclcfPostMapper postMapper;
    @Autowired
    private HqclcfDeptService hqclcfDeptService;
    @Autowired
    private SysWorkplacesetMapper sysWorkplacesetMapper;
    @Autowired
    private HqclcfEmpApvMapper empApvMapper;
    @Autowired
    private HqclcfPostMapper hqclcfPostMapper;
    @Autowired
    private BaseService baseService;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Grid<HqclcfJobTransfer> queryPageInfo(PageBean pageBean, HqclcfJobTransfer params) throws Exception {
    	PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
    	
    	Grid<HqclcfJobTransfer> grid = new Grid<>();
        String deptId = params.getDeptNo();
        String oldId = params.getOldDeptNo();
        HqclcfDept dept;
        List<HqclcfJobTransfer> list = new ArrayList<>();
        List<HqclcfJobTransfer> OldDeptNamelist = new ArrayList<>();
        
        //检查现部门
        if (StringUtil.isNotEmpty(deptId) || StringUtil.isNotEmpty(oldId)) {
            if (StringUtil.isNotEmpty(deptId)) {
            	if (params.getDeptNo() != null && params.getDeptNo() != "null" && params.getDeptNo() != "") {
					    dept = hqclcfDeptService.queryDept(Long.valueOf(deptId));
						params.setDeptNo(dept.getDeptCode());
        			    params.setDeptHid(Long.valueOf(deptId));
        		}
            }
            //检查是否是查询原部门
            if (StringUtil.isNotEmpty(oldId)) {
            	if (params.getOldDeptNo() != null && params.getOldDeptNo() != "null" && params.getOldDeptNo() != "") {
				    dept = hqclcfDeptService.queryDept(Long.valueOf(oldId));
					params.setOldDeptNo(dept.getDeptCode());
    			    params.setOlddeptHid(Long.valueOf(oldId));
    		   }
            }
        }
        //权限登录显示问题
        params.setLoginUserId(CommonUtil.getOnlineUser().getUserId());
        PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
        list = hqclcfJobTransferMapper.queryAllJobTransfer(params);
        OldDeptNamelist = hqclcfJobTransferMapper.queryOldDeptJobTransfer(params);
        for (int i = 0; i < OldDeptNamelist.size(); i++) {
        	for (int j = 0; j < list.size(); j++) {
        		if (OldDeptNamelist.get(i).getPriNumber().equals(list.get(j).getPriNumber())) {
    				 list.get(j).setOldDeptPname(OldDeptNamelist.get(i).getOldDeptPname());
    			}
			}
		}
        PageInfo<HqclcfJobTransfer> pageInfo = new PageInfo<>(list);
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
        return grid;
    }

    @Override
    public Json add(HqclcfJobTransfer readValue, HttpServletRequest res) {
        Json json = new Json();
        try {
            //加入离职转在职数据
            //原离职时间
            HqclcfEmp emp = hqclcfEmpMapper.queryEmpNameByNo(readValue.getEmpNo());
            Json isFull = checkIsFull(readValue);//检查是满编，并转为部门与岗位编码
            if (isFull.isSuccess()) {
                isFull.setSuccess(false);
                return isFull;
            }
            
            //工资年月
            String gzymTime = DateUtil.getCurrentDate("yyyy-MM");
            readValue.setGzYm(gzymTime);
            readValue.setLeaveDate(emp.getLeaveDate());//这里获取该人员原离职时间
            //创建人员及创建时间
            readValue.setCreateDate(new Date());//当前申请离转在时间
            readValue.setCreateName(CommonUtil.getOnlineFullName(res));//创建人
            hqclcfJobTransferMapper.addById(readValue);
            
            //是否需要审批要验证
            json = updateEmp(readValue);
            if (json.isSuccess()) {
                json.setMsg("新增成功！");
                return json;
            }
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg("新增失败！");
            logger.error("新增失败：" + e.getMessage());
            return json;
        }
        return json;
    }

    @Override
    public Json editById(HttpServletRequest req,HqclcfJobTransfer readValue) {
        Json json = new Json();
        try {
        	
            Json isFull = checkIsFull(readValue);//检查是满编，并转为部门与岗位编码
            if (isFull.isSuccess()) {
                isFull.setSuccess(false);
                return isFull;
            }
            //更新时间以及人员
            readValue.setUpdatetime(new Date());
            readValue.setUpdator(CommonUtil.getOnlineFullName());
            json = updateEmp(readValue);
            if (json.isSuccess()) {
                json.setMsg("修改成功");
                return json;
            }
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg("修改失败");
            logger.error("修改失败：" + e.getMessage());
            return json;
        }
        return json;
    }

    @Override
    public HashMap queryjobTransferEmpByEmpNameOrNo(String empName, String empNo) {
        //后面加入检查当前新增人员是否已存在表中
        HashMap emp = null;
        try {
            emp = hqclcfEmpMapper.jobTransferEmpByEmpNameOrNo(empName, empNo);
        } catch (SQLException e) {
            logger.error(" Err  :[queryjobTransferEmpByEmpNameOrNo]" + "[STATE]" + e.getSQLState() + "[ERR_CODE]" + e.getErrorCode() + "[MES]" + e.getMessage());
            return emp;
        }
        return emp;
    }

    @Override
    public JSONArray getParentDeptTree(String userId, Long deptId, String deptCode) {
        JSONArray jsonArray = new JSONArray();
        List<HqclcfDept> depts = null;
        try {
            depts = deptService.queryDeptUpOrDown("up", deptId);
        } catch (Exception e) {
            logger.error("获取上级部门时:" + e.getMessage());
        }
        for (HqclcfDept item : depts) {
            ZtreeVo tree = new ZtreeVo();
            tree.setId(item.getId());
            tree.setPid(item.getPid());
            tree.setName(item.getDeptName());
            jsonArray.add(tree);
        }
        return jsonArray;
    }

    @Override
    public Object getBusLine(Long id) {
        return hqclcfDeptMapper.queryDeptById(id);
    }

    @Override
    public Json checkIsFull(HqclcfJobTransfer transfer) throws Exception {
        Json json = new Json();
        json.setSuccess(false);
        //检查部门是否满编
        //判断新增或者修改
        if (transfer.getOperationType().equals("add")) {
        	//根据部门id获取部门编号
        	HqclcfDept queryDept = hqclcfDeptMapper.queryDeptById(transfer.getDeptHid());
        	//获取该部门编制数
            int organizatDept = hqclcfEmpMapper.queryEmpOrganizatDept(queryDept.getDeptCode());
            HqclcfDept deptP = new HqclcfDept();
            deptP.setId(Long.valueOf(queryDept.getId()));//新增根据id
            HqclcfDept dept = hqclcfDeptMapper.queryAll(deptP).get(0);

            if (organizatDept + 1 > dept.getOrganizat()) {
                json.setSuccess(true);
                json.setMsg("该部门已满编");
                return json;
            } else {
                transfer.setDeptNo(dept.getDeptCode());//设置部门编号
            }
            //检查岗位是否满编
            HqclcfPost post = postMapper.queryByPostId(Long.valueOf(transfer.getPostHid()), "1");//编制
            Integer count = hqclcfEmpMapper.queryEmpOrganizatPost(post.getPostNo());//在编人员
            if (count + 1 > post.getOrganizat()) {
                json.setSuccess(true);
                json.setMsg("该岗位已满编");
                return json;
            } else {
                transfer.setPost(post.getPostNo());//设置岗位编号
            }

            //设置职级职务
            try {
                HqclcfBusiness postion = hqclcfBusinessMapper.queryBusinessByCode(transfer.getPosition());
                transfer.setPositionName(postion.getPosName());
                if (transfer.getRank() != null) {
                    HqclcfRank rank = hqclcfRankMapper.getRankByNo(transfer.getRank());
                    transfer.setRankName(rank.getName());
                }
            } catch (Exception e) {
                json.setSuccess(true);
                json.setMsg("查询职级或职务错误" + e.getMessage());
                return json;
            }
            return json;
            
        } else if (transfer.getOperationType().equals("edit")) {
        	//根据部门id获取部门编号
        	HqclcfDept queryDept = hqclcfDeptMapper.queryDeptById(transfer.getDeptHid());
        	//获取该部门编制数
            int organizatDept = hqclcfEmpMapper.queryEmpOrganizatDept(queryDept.getDeptCode());
            HqclcfDept deptP = new HqclcfDept();
            deptP.setId(Long.valueOf(queryDept.getId()));//新增根据id
            HqclcfDept dept = hqclcfDeptMapper.queryAll(deptP).get(0);

            if (organizatDept + 1 > dept.getOrganizat()) {
                json.setSuccess(true);
                json.setMsg("该部门已满编");
                return json;
            } else {
                transfer.setDeptNo(dept.getDeptCode());//设置部门编号
            }
            //HqclcfPost post = postMapper.queryByPostNo(transfer.getPost());//编制
            //检查岗位是否满编
            HqclcfPost post = postMapper.queryByPostId(Long.valueOf(transfer.getPostHid()), "1");//编制
            Integer count = hqclcfEmpMapper.queryEmpOrganizatPost(post.getPostNo());//在编人员
            if (count + 1 > post.getOrganizat()) {
                json.setSuccess(true);
                json.setMsg("该岗位已满编");
                return json;
            } else {
                transfer.setPost(post.getPostNo());//设置岗位编号
            }

            //设置职级职务
            try {
                HqclcfBusiness postion = hqclcfBusinessMapper.queryBusinessByCode(transfer.getPosition());
                transfer.setPositionName(postion.getPosName());
                if (transfer.getRank() != null) {
                    HqclcfRank rank = hqclcfRankMapper.getRankByNo(transfer.getRank());
                    transfer.setRankName(rank.getName());
                }
            } catch (Exception e) {
                json.setSuccess(true);
                json.setMsg("查询职级或职务错误" + e.getMessage());
                return json;
            }
            return json;
        }
        return json;
    }

    @Override
    public void showBtnList(HttpServletRequest request, ModelAndView modelAndView) {
        //按钮权限 根据当前session中存的资源权限
        List<String> resourcesUrl = (List<String>) request.getSession().getAttribute("resourcesUrl");
        if (resourcesUrl.contains(ConstantCtl.HQCLCF_JOBTRANS + ConstantCtl.ADD)) {
            //是否展示新增按钮
            modelAndView.addObject("addBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("addBtn", Constant.DISABLE);
        }
        if (resourcesUrl.contains(ConstantCtl.HQCLCF_JOBTRANS + ConstantCtl.EDIT)) {
            //是否展示编辑按钮
            modelAndView.addObject("editBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("editBtn", Constant.DISABLE);
        }
        if (resourcesUrl.contains(ConstantCtl.HQCLCF_JOBTRANS + ConstantCtl.QUERY)) {
            //是否展示查询按钮
            modelAndView.addObject("queryBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("queryBtn", Constant.DISABLE);
        }
        if (resourcesUrl.contains(ConstantCtl.HQCLCF_JOBTRANS + ConstantCtl.DETAIL)) {
            //是否展示详情按钮
            modelAndView.addObject("detailBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("detailBtn", Constant.DISABLE);
        }
    }

    /**
     * 对emp操作
     *
     * @param readValue
     * @return
     * @throws Exception
     */
    private Json updateEmp(HqclcfJobTransfer readValue) {
        Json json = new Json();
        json.setSuccess(true);
        try {
            //操作时间
            readValue.setUpdatetime(new Date());
            readValue.setUpdator(CommonUtil.getOnlineFullName());
            //更新职务职级操作
            HqclcfBusiness postiAndRank = null;
            postiAndRank = hqclcfBusinessMapper.queryBusinessByCode(readValue.getPosition());
            readValue.setPositionName(postiAndRank.getPosName());//职务
            HqclcfRank hqclcfRank = null;
            hqclcfRank = hqclcfRankMapper.getRankByNo(readValue.getRank());
            readValue.setRankName(hqclcfRank.getName());//职级
            //检查入职时间是否为工作日
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String enterDay = format.format(readValue.getEnterDate());
            String week = DateUtil.getWeekOfDate(enterDay);
            if (week.equals("六") || week.equals("日")) {
                json.setSuccess(false);
                json.setMsg("请选择工作日");
                return json;
            }
            //检查入职时间是否为当前时间
//            String currDay = DateUtil.getCurrentDate("yyyy-MM-dd");
//            if (currDay.equals(enterDay)) {
                //最终更新员工状态
                 readValue.setStatus(Constant.JOB_ON);
                 readValue.setLeaveDate(null);//修改离职时间为null
                 readValue.setUpdateDate(readValue.getUpdatetime());
                 //获取登录用户账号
                 String updator = readValue.getUpdator();
                 SysUser user = sysUserMapper.queryUserByUserName(updator);
                 
                 readValue.setUpdateName(user.getFullName());
                 
//                HqclcfPost post = postMapper.queryByPostId(Long.valueOf(readValue.getPost()), "1"); //通过岗位编制id查询编制岗位编制信息
//                readValue.setPost(post.getPostNo());//更新POST_NO
                 /*更新职务以及职级*/
                 hqclcfJobTransferMapper.updateById(readValue);
                 System.out.println("=====");
                 hqclcfEmpMapper.updateJobById(readValue);
                 System.out.println("++++");
//            }
        } catch (Exception e) {
        	e.printStackTrace();
            json.setSuccess(false);
            return json;
        }
        return json;
    }

	@Override
	public void buildEditFormReult(Long id, HttpServletRequest req) throws Exception {
		HqclcfJobTransfer hqclcfJobTransfer = new HqclcfJobTransfer();
		hqclcfJobTransfer.setPriNumber(id);
		List<HqclcfJobTransfer> queryAllJobTransfer = hqclcfJobTransferMapper.queryAllJobTransfer(hqclcfJobTransfer);
		HqclcfJobTransfer hqclcfJobTransfer1 = queryAllJobTransfer.get(0);
		req.setAttribute("hqclcfJobTransfer1",hqclcfJobTransfer1);
		HqclcfDept oldDept = hqclcfDeptService.queryDeptByDeptCode(hqclcfJobTransfer1.getOldDeptNo());
        req.setAttribute("oldDept",oldDept);
		HqclcfDept dept = hqclcfDeptService.queryDeptByDeptCode(hqclcfJobTransfer1.getDeptNo());
        dept.setDeptNo(hqclcfJobTransfer1.getDeptNo());
		req.setAttribute("dept",dept);
        HqclcfPost oldPost = postMapper.queryByPostNo(hqclcfJobTransfer1.getOldPost());
        req.setAttribute("oldPost",oldPost);
        HqclcfPost post = postMapper.queryByPostNo(hqclcfJobTransfer1.getPost());
        post.setPostNo(hqclcfJobTransfer1.getPost());
        req.setAttribute("post",post);
        //根据岗位部门id构建职务
        List<HqclcfBusiness> oldBusinesses = hqclcfBusinessMapper.queryBusinessByDeptIdAndPost(dept.getId(), oldPost.getPriNumber());
        req.setAttribute("oldBusinesses",oldBusinesses);
        List<HqclcfBusiness> businesses = hqclcfBusinessMapper.queryBusinessByDeptIdAndPost(dept.getId(), post.getPriNumber());
        req.setAttribute("businessesList",businesses);
        //根据已选择的职务构建职级
        HqclcfBusiness hqclcfBusiness = hqclcfBusinessMapper.queryBusinessByCode(hqclcfJobTransfer1.getPosition());
        String rankCodeStr = hqclcfBusiness.getRankCode();
        List<String> rankList=new ArrayList<>();
        if (!"".equals(rankCodeStr)){
            String[] rankCode = rankCodeStr.split(",");
            for (int i = 0; i <rankCode.length ; i++) {
                rankList.add(rankCode[i]);
            }
        }
        List<HqclcfRank> rankByRankCodes = hqclcfRankMapper.getRankByRankCodes(rankList);
        req.setAttribute("rankList",rankByRankCodes);
        //HqclcfEmpFile file=new HqclcfEmpFile();
	}

	
	
	@Override
	public HqclcfEmpApv2SelectJsonResult bulidSelectDataByDeptId(String data) throws Exception {
		HqclcfEmpApv2SelectJsonResult result= new HqclcfEmpApv2SelectJsonResult();
		ObjectMapper mapper=new ObjectMapper();
        HqclcfEmpApvIdArray apvIdArray = mapper.readValue(data.getBytes(), HqclcfEmpApvIdArray.class);
        switch (null!=apvIdArray.getType()?apvIdArray.getType():""){
            case "deptSelect":
                //查询业务条线
                HqclcfDept dept = hqclcfDeptService.queryDept(apvIdArray.getDeptId());
                //根据业务条线查询工作地
                List<SysWorkplaceset> workist=new ArrayList<>();
                if (dept.getBusinessLine()!=null&&!"".equals(dept.getBusinessLine())){
                    SysWorkplaceset workplaceset=new SysWorkplaceset();
                    workplaceset.setBusinessLine(dept.getBusinessLine().toString());
                    workplaceset.setStatus("1");
                    workist = sysWorkplacesetMapper.queryAllWorkplaceset(workplaceset);
                }
                result.setWorkspaceData(workist);
                result.setBusinessLine(dept.getBusinessLine());
                //这个部门下的启用的岗位
                JSONArray jsonArray = hqclcfDeptService.buildPostTreeByDeptId(apvIdArray.getDeptId());
                result.setPostTree(jsonArray);
                break;
            case"gwSelect":
                Long deptId = apvIdArray.getDeptId();
                Long postId = apvIdArray.getPostId();
                Map map=new HashMap();
                map.put("deptId",deptId);
                map.put("postId",postId);
                map.put("status",1);
                Integer empSize = empApvMapper.queryEmpSizeByDeptIdAndPostId(map);
                HqclcfPost post = hqclcfPostMapper.queryByPostId(postId, "1");
                Integer organizat = post.getOrganizat();
                if(empSize+1>organizat){
                    result.setSuccess(false);
                    result.setMsg("此部门岗位下的人数："+empSize+",岗位编制："+organizat+"岗位超编!");
                }else{
                    List<HqclcfBusiness> hqclcfBusinesses = hqclcfBusinessMapper.queryBusinessByDeptIdAndPost(deptId, postId);
                    result.setBusinessData(hqclcfBusinesses);
                }
                break;
            case"zwSelect":
                HqclcfBusiness hqclcfBusiness = hqclcfBusinessMapper.queryBusinessByCode(apvIdArray.getBusinessCode());
                String rankCodeStr = hqclcfBusiness.getRankCode();
                List<String> rankList=new ArrayList<>();
                if (!"".equals(rankCodeStr)){
                    String[] rankCode = rankCodeStr.split(",");
                    for (int i = 0; i <rankCode.length ; i++) {
                        rankList.add(rankCode[i]);
                    }
                }
                List<HqclcfRank> rankByRankCodes = hqclcfRankMapper.getRankByRankCodes(rankList);
                result.setRankData(rankByRankCodes);
                break;
            case "workSelect":
                String workCode = apvIdArray.getWorkCode();
                Integer businessLine = apvIdArray.getBusinessLine();
                //根据业务条线生成编码
                switch (businessLine){
                    case 1:
                        String zbseqNo= empApvMapper.querySeqBySeqName(Constant.SEQ_HQCLCF_ZBEMPNO);
                        String zbempNo="ZHZB"+(!"".equals(zbseqNo) ? zbseqNo : RandomUtil.generateGUIDByNum(4));
                        result.setEmpNo(zbempNo);
                        break;
                    case 2:
                        String xfseqNo= empApvMapper.querySeqBySeqName(Constant.SEQ_HQCLCF_XFEMPNO);
                        String xfempNo="ZHXF"+workCode+(!"".equals(xfseqNo) ? xfseqNo : RandomUtil.generateGUIDByNum(4));
                        result.setEmpNo(xfempNo);
                        break;
                    case 3:
                        String xdseqNo= empApvMapper.querySeqBySeqName(Constant.SEQ_HQCLCF_XDEMPNO);
                        String xdempNo="ZHXD"+workCode+(!"".equals(xdseqNo) ? xdseqNo : RandomUtil.generateGUIDByNum(4));
                        result.setEmpNo(xdempNo);
                        break;
                }
        }
		return result;
	}

	@Override
	public List<Map<String, Object>> queryAllHqclcfJob(String data, String q) throws Exception {
		String isBl = "排空";
        List<Integer> loginUserId = baseService.getOnlineUserBl();
        List<Map<String, Object>> emps=new ArrayList<>();
        if (!("".equals(q)) && q != null) {
        	 emps = hqclcfEmpMapper.queryEmpByq2(q, isBl, loginUserId);
            return emps;
        }
        emps = hqclcfEmpMapper.queryEmpByq2(q, isBl, loginUserId);
        return emps;
	}

	@Override
	public void buildListTpl(HttpServletRequest req, ModelAndView modelAndView) throws Exception {
		    Json json=new Json();
	        ObjectMapper mapper=new ObjectMapper();
	        //部门
	        HqclcfDept dept=new HqclcfDept();
	        req.setAttribute("deptListTpl",mapper.writeValueAsString(json.getObj(hqclcfDeptMapper.queryAll(dept))));
	        //岗位
	        HqclcfPost post=new HqclcfPost();
	        req.setAttribute("postListTpl",mapper.writeValueAsString(json.getObj(hqclcfPostMapper.queryAll(post))));
	        //职务
	        HqclcfBusiness business=new HqclcfBusiness();
	        req.setAttribute("bussinListTpl",mapper.writeValueAsString(json.getObj(hqclcfBusinessMapper.getBusinessByCondition(business))));
	        //职级
	        HqclcfRank rank=new HqclcfRank();
	        req.setAttribute("rankListTpl",mapper.writeValueAsString(json.getObj(hqclcfRankMapper.getRankByCondition(rank))));		
	}
}
