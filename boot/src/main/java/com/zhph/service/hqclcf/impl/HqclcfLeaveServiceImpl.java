package com.zhph.service.hqclcf.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.annotation.SameUrlData;
import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.config.UrlConfig;
import com.zhph.exception.AppException;
import com.zhph.mapper.cf.CfEmpStatusMapper;
import com.zhph.mapper.cl.ClGzymMapper;
import com.zhph.mapper.common.ZhphObjectMapper;
import com.zhph.mapper.hqclcf.HqclcfBusinessMapper;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpFileMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.mapper.hqclcf.HqclcfLeaveMapper;
import com.zhph.mapper.hqclcf.HqclcfPostMapper;
import com.zhph.mapper.hqclcf.HqclcfRankMapper;
import com.zhph.mapper.sys.SysConfigTypeMapper;
import com.zhph.model.cf.CfEmpStatus;
import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.hqclcf.HqclcfLeave;
import com.zhph.model.hqclcf.HqclcfPersonTransfer;
import com.zhph.model.hqclcf.HqclcfPost;
import com.zhph.model.hqclcf.HqclcfRank;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysUser;
import com.zhph.model.sys.SysWorkplaceset;
import com.zhph.service.cf.TimeAutomatedService;
import com.zhph.service.common.BaseService;
import com.zhph.service.cf.TimeAutomatedService;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.service.hqclcf.HqclcfLeaveService;
import com.zhph.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class HqclcfLeaveServiceImpl implements HqclcfLeaveService {
    @Autowired
    private HqclcfLeaveMapper hqclcfLeaveMapper;
    @Autowired
    private HqclcfEmpMapper hqclcfEmpMapper;
    @Autowired
    private HqclcfEmpFileMapper hqclcfEmpFileMapper;
    @Autowired
    private HqclcfDeptMapper hqclcfDeptMapper;
    @Autowired
    private SysConfigTypeMapper sysConfigTypeMapper;
    @Autowired
    private UrlConfig urlConfig;
    @Autowired
    private HqclcfDeptService deptService;
    @Autowired
    private HqclcfEmpFileMapper fileMapper;
    @Autowired
    private CfEmpStatusMapper cfEmpStatusMapper;
    @Autowired
    private ClGzymMapper clGzymMapper;
    @Autowired
    private ZhphObjectMapper zhphObjectMapper;
    @Autowired
    private HqclcfRankMapper hqclcfRankMapper;
    @Autowired
    private HqclcfBusinessMapper hqclcfBusinessMapper;
    @Autowired
    private HqclcfPostMapper hqclcfPostMapper;
    @Autowired
    private BaseService baseService;
    @Resource
	private HqclcfDeptService hqclcfDeptService;
    @Resource
    private TimeAutomatedService timeAutomatedService;


    public static final Logger logger = LogManager.getLogger(HqclcfLeaveServiceImpl.class);

    @Override
    public Grid<HqclcfLeave> queryPageInfo(PageBean pageBean, HqclcfLeave params) throws Exception {
    	PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
/*
		if (params.getDeptNo() != null && params.getDeptNo() != "null" && params.getDeptNo() != "") {
			Long deptId = Long.valueOf(params.getDeptNo());
			HqclcfDept dept = hqclcfDeptService.queryDept(deptId);
			params.setDeptNo(dept.getDeptCode());
			params.setDeptHid(deptId);
		}
*/
		// 添加数据权限
		params.setLoginUserId(CommonUtil.getOnlineUser().getUserId());
		List<HqclcfLeave> list = hqclcfLeaveMapper.queryAllLeave(params);
		PageInfo<HqclcfLeave> pageInfo = new PageInfo<>(list);
		Grid<HqclcfLeave> grid = new Grid<>();
		grid.setCount(pageInfo.getTotal());
		grid.setData(pageInfo.getList());
		return grid;
        
    }

    @Override
    public Json del(HqclcfLeave readValue) {
        Json json = new Json();
        try {
            if (readValue.getPriNumber() == null) {
                throw new AppException("ID不能为空");
            }
            if ("0".equals(readValue.getStatus())) {
                json.setSuccess(true);
                json.setMsg("该员工信息未被审核，不能删除");
            } else {
                hqclcfLeaveMapper.delById(readValue.getPriNumber());
            }
        } catch (Exception e) {
            json.setSuccess(true);
            json.setMsg(e.getMessage());
            logger.error("删除失败：" + e.getMessage());
            return json;
        }
        return json;
    }

    @Override
    public void exportExl(HqclcfLeave data, HttpServletRequest req, HttpServletResponse res) {
        try {
        	HqclcfDept dept = new HqclcfDept();
            HqclcfPost post = new HqclcfPost();
            HqclcfRank rank = null;
            HqclcfBusiness business = new HqclcfBusiness();
        	
            List<HqclcfLeave> list = hqclcfLeaveMapper.queryAllLeave(data);
            String[] colTitleAry = {"序号", "部门", "岗位", "职务", "职级", "员工工号", "姓名", "入职时间", "离职时间", "离职原因", "备注"};
            String[][] array = new String[list.size()][colTitleAry.length];
            short[] colWidthAry = {80, 100, 100, 100, 100, 100, 100, 100, 100, 100, 200};
            for (int i = 0; i < list.size(); i++) {
                HqclcfLeave hqclcfLeave = list.get(i);
                array[i][0] = i + 1 + ""; //序号
	                dept.setDeptCode(hqclcfLeave.getDeptNo());
	                if (hqclcfDeptMapper.queryAll(dept).size() > 0) {
	                	array[i][1] = hqclcfDeptMapper.queryAll(dept).get(0).getDeptName();//部门
	                }
                
                if (hqclcfLeave.getPost() != null && !(hqclcfLeave.getPost().equals(""))) {
                    post.setPostNo(hqclcfLeave.getPost());
                    if (hqclcfPostMapper.queryByPostNo(hqclcfLeave.getPost()) != null) {
                        array[i][2] = hqclcfPostMapper.queryByPostNo(hqclcfLeave.getPost()).getPostName(); //原岗位
                    }
                }
                
                if (hqclcfLeave.getPosition() != null && !(hqclcfLeave.getPosition().equals(""))) {
                    business.setPosCode(hqclcfLeave.getPosition());
                    if (hqclcfBusinessMapper.getBusinessByCondition(business).size() > 0) {
                        array[i][3] = hqclcfBusinessMapper.getBusinessByCondition(business).get(0).getPosName();//原职务
                    }
                }
                
                if (hqclcfLeave.getRank() != null && !(hqclcfLeave.getRank().equals(""))) {
                    rank = new HqclcfRank();
                    rank.setNo(hqclcfLeave.getRank());
                    if (hqclcfRankMapper.getRankByCondition(rank).size() > 0) {
                        array[i][4] = hqclcfRankMapper.getRankByCondition(rank).get(0).getName(); //职级
                    } else if (hqclcfRankMapper.getRankByCondition(rank).size() == 0) {
                        rank.setNo("");
                        rank.setName(hqclcfLeave.getRank());
                        if (hqclcfRankMapper.getRankByCondition(rank).size() > 0) {
                            array[i][4] = hqclcfRankMapper.getRankByCondition(rank).get(0).getName(); //职级
                        } else {
                            array[i][4] = "暂无";
                        }
                    }
                }
                array[i][5] = hqclcfLeave.getEmpNo();//员工工号
                array[i][6] = hqclcfLeave.getEmpName();//员工姓名
                array[i][7] = DateUtil.parseDateFormat(hqclcfLeave.getEntryTime(), "yyyy-MM-dd");//入职时间
                array[i][8] = DateUtil.parseDateFormat(hqclcfLeave.getExitTime(), "yyyy-MM-dd");//离职时间
                SysConfigType sysConfigType = sysConfigTypeMapper.querySingleBySysVal(hqclcfLeave.getLeavingReason(), Constant.DIMISSION_TYPE);
                array[i][9] = sysConfigType.getSysName();//离职原因
                array[i][10] = hqclcfLeave.getRemark();//意见
            }
            ExcelUtil excelUtil = new ExcelUtil();
            excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "离职淘汰", 1);
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void editById(HqclcfLeave leave, MultipartHttpServletRequest multipartHttpServletRequest,HttpServletRequest res) throws Exception {
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String exitTimee = format.format(leave.getExitTime());
        int queryEmpByworkNo = hqclcfLeaveMapper.queryEmpByworkNo(exitTimee);
        
        if (queryEmpByworkNo != 1)
            throw new AppException("不能选择非工作日");
    	
    	
    	leave.setUpdateName(CommonUtil.getOnlineFullName(res));
    	leave.setUpdateDate(new Date());
    	
    	uploadFile(multipartHttpServletRequest,leave.getEmpNo());
    	
    	hqclcfLeaveMapper.updateById(leave);
    }

    @Override
	public JSONObject appById(Long id,HqclcfLeave leave,SysUser sysUser,HttpServletRequest res) throws Exception{
    	JSONObject obj=new JSONObject();
        obj.put("code","200");
        try {
        	HqclcfLeave hqclcfLeave = new HqclcfLeave();
        	hqclcfLeave.setPriNumber(leave.getPriNumber());
        	HqclcfLeave leave1 =  hqclcfLeaveMapper.queryAllLeave(hqclcfLeave).get(0);
            leave1.setApproverName(CommonUtil.getOnlineFullName(res));
            leave1.setApproverTime(new Date());
            leave1.setAppOpinions(leave.getAppOpinions());
            if (Constant.LEAVE_APP_REFUSE.equals(leave.getAppStatus())) {//同意留职
                    hqclcfLeaveMapper.delById(leave1.getPriNumber());
            }
          //更新用户的状态
            if (Constant.LEAVE_APP_PASS.equals(leave.getAppStatus())) {//同意离职
            	leave1.setAppStatus(leave.getAppStatus());
            	leave1.setStatus(Constant.JOB_OFF);
            	leave1.setUpdateDate(new Date());//更新当前时间
                HqclcfEmp emp = hqclcfEmpMapper.queryEmpByEmpNo(leave1.getEmpNo());
                emp.setLeaveDate(leave1.getExitTime());
                timeAutomatedService.addOneEmpForLeave(emp, sysUser);
                hqclcfEmpMapper.updateEmpById(leave1);
                //同步离职日志到消分员工状态管理
                CfEmpStatus empStatus = cfEmpStatusMapper.queryEmpByEmpNo(leave1.getEmpNo());
                if(null!=empStatus){
                        if(null==empStatus.getEndDate()){
                            empStatus.setEndDate(DateUtil.parseDateFormat(leave1.getExitTime(),"yyyy-MM-dd"));
                            cfEmpStatusMapper.updateEmpStatuses(empStatus);
                        }
                }
                //插入离职信息
                hqclcfLeaveMapper.appById(leave1);
            }
        } catch (Exception e) {
            obj.put("code","500");
            obj.put("msg","审批失败:"+e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    @SameUrlData
    @Override
    public void add(HqclcfLeave readValue,HttpServletRequest res, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
        Json json = new Json();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String exitTimee = format.format(readValue.getExitTime());
//        String week = DateUtil.getWeekOfDate(exitTimee);
        HqclcfLeave selectByEmpNo = hqclcfLeaveMapper.selectByEmpNo(readValue.getEmpNo());
        int queryEmpByworkNo = hqclcfLeaveMapper.queryEmpByworkNo(exitTimee);
        if (selectByEmpNo != null)
           throw new AppException("不能新增同一个员工");
        
        if (queryEmpByworkNo != 1)
            throw new AppException("不能选择非工作日");
        
//        if (week.equals("六") || week.equals("日")) {
//        	throw new AppException("请选择工作日");
//        }
            readValue.setStatus(Constant.JOB_ON);//员工状态
            readValue.setAppStatus(Constant.LEAVE_APP_WAITE);//等待审批
            readValue.setCreateName(CommonUtil.getOnlineFullName(res));
            readValue.setCreateDate(new Date());
            String currentGzym = clGzymMapper.queryCurrGzym().getCurrentGzym();
            readValue.setGzYm(currentGzym);
            uploadFile(multipartHttpServletRequest,readValue.getEmpNo());
            hqclcfLeaveMapper.addByObj(readValue);
    }


    @Override
    public HqclcfLeave queryLeave(Long id) throws Exception {
        HqclcfLeave hqclcfLeave = new HqclcfLeave();
        hqclcfLeave.setPriNumber(id);
        List<HqclcfLeave> hqclcfLeaves = hqclcfLeaveMapper.queryAllLeave(hqclcfLeave);
        HqclcfLeave leave = hqclcfLeaves.size() == 1 ? hqclcfLeaves.get(0) : hqclcfLeave;
        return leave;
    }

    /**
     * 通过员工姓名带出原信息
     *
     * @param empName
     * @return
     */
    @Override
    public Map<String, Object> queryleaveEmpByempName(String empName, String empNo) throws SQLException {
        return  hqclcfEmpMapper.leaveEmpByEmpNameOrNo(empName, empNo);
    }

    /**
     * 上传附件
     *
     * @param request
     */
    @Override
    public HqclcfEmpFile uploadFile(MultipartHttpServletRequest request, String empNo) throws Exception{
        Json msg = new Json();
        if (StringUtil.isEmpty(empNo)) {
            return null;
        }
        HqclcfEmp empLeave = hqclcfEmpMapper.queryEmpNameByNo(empNo);
        HqclcfEmpFile file = null;
        if (empLeave != null) {
        	 Iterator<Map.Entry<String, Integer>> iter = buildFileMapName(Constant.LEAVE_FILE,msg).entrySet().iterator();
        	 file = uploadFile2Hdfs(iter, empLeave, request);
 /*           switch (empLeave.getBusinessLine()) {
                case 1:// 总部
                    Iterator<Map.Entry<String, Integer>> hqIter = buildFileMapName(Constant.ZB_FILE, msg).entrySet().iterator();
                    if (!msg.isSuccess()) {
                        break;
                    }
                    file = uploadFile2Hdfs(hqIter, empLeave, request, Constant.ZHPHHQ);
                    break;
                case 2:// 消分
                    Iterator<Map.Entry<String, Integer>> xfIter = buildFileMapName(Constant.XF_FILE, msg).entrySet().iterator();
                    if (!msg.isSuccess()) {
                        break;
                    }
                    file = uploadFile2Hdfs(xfIter, empLeave, request, Constant.ZHPHXJ);
                    break;
                case 3:// 信贷
                    Iterator<Map.Entry<String, Integer>> xdIter = buildFileMapName(Constant.XD_FILE, msg).entrySet().iterator();
                    if (!msg.isSuccess()) {
                        break;
                    }
                    file = uploadFile2Hdfs(xdIter, empLeave, request, Constant.ZHXD);
                    break;
            }*/
        }
        return file;
    }

    /**
     * 预览文件
     *
     * @param file
     * @param res
     */
    @Override
    public void preViewFile(HqclcfEmpFile file, HttpServletResponse res) {
        HqclcfEmpFile hqclcfEmpFile = hqclcfEmpFileMapper.queryEmpFileByEmpNoAndFileType(file.getEmpNo(), file.getFileType());
        if (hqclcfEmpFile != null) {
            if (hqclcfEmpFile != null) {
                StringBuffer path = bulidPathByempFile(hqclcfEmpFile);
                path.append(hqclcfEmpFile.getFileName());
                //从hdfs上拉取文件预览
                try {
                    FileUtil.showFile(res, path.toString(), hqclcfEmpFile.getFileName(), urlConfig);
                } catch (Exception e) {
                    logger.error("预览文件时出现:" + e.getMessage() + "cause:" + e.getCause().toString());
                }
            }
        }
    }

    @Override
    public Json delFile(HqclcfEmpFile file) {
        Json msg = new Json();
        HqclcfEmpFile hqclcfEmpFile = hqclcfEmpFileMapper.queryEmpFileByEmpNoAndFileType(file.getEmpNo(), file.getFileType());
        if (hqclcfEmpFile != null) {
            if (hqclcfEmpFile != null) {
                StringBuffer path = bulidPathByempFile(hqclcfEmpFile);
                path.append(hqclcfEmpFile.getFileName());
                try {
//                    FileUtil.deleteFile()
//                    FileUtil.deletePath();
//                    FileUtil.deleteFile(res, path.toString(), hqclcfEmpFile.getFileName(), urlConfig);
                } catch (Exception e) {
                    msg.setSuccess(false);
                    logger.error("删除文件时出现:" + e.getMessage());
                }
            }
        }
        return msg;
    }

    @Override
    public List<Map<String, Object>> queryfileName(String empNo, String busLine) {
        List<Map<String, Object>> fileMap = null;
        try {
            fileMap = hqclcfLeaveMapper.queryFileMap(empNo, busLine);
        } catch (Exception e) {
            logger.error("获取文件名称时：" + e.getMessage());
            return null;
        }
        return fileMap;
    }

    @Override
    public void showBtnList(HttpServletRequest request, ModelAndView modelAndView) {
        //按钮权限 根据当前session中存的资源权限
        List<String> resourcesUrl = (List<String>) request.getSession().getAttribute("resourcesUrl");
        if (resourcesUrl.contains(ConstantCtl.HQCLCF_LEAVE + ConstantCtl.ADD)) {
            //是否展示新增按钮
            modelAndView.addObject("addBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("addBtn", Constant.DISABLE);
        }
        if (resourcesUrl.contains(ConstantCtl.HQCLCF_LEAVE + ConstantCtl.EDIT)) {
            //是否展示编辑按钮
            modelAndView.addObject("editBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("editBtn", Constant.DISABLE);
        }
        //是否展示删除按钮
        if (resourcesUrl.contains(ConstantCtl.HQCLCF_LEAVE + ConstantCtl.DEL)) {
            modelAndView.addObject("delBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("delBtn", Constant.DISABLE);
        }
        //是否展示查询按钮
        if (resourcesUrl.contains(ConstantCtl.HQCLCF_LEAVE + ConstantCtl.QUERY)) {
            modelAndView.addObject("queryBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("queryBtn", Constant.DISABLE);
        }
        //是否展示导出按钮
        if (resourcesUrl.contains(ConstantCtl.HQCLCF_LEAVE + ConstantCtl.EXPORTEXL)) {
            modelAndView.addObject("exportExlBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("exportExlBtn", Constant.DISABLE);
        }
        //是否展示审批按钮
        if (resourcesUrl.contains(ConstantCtl.HQCLCF_LEAVE + ConstantCtl.APP)) {
            modelAndView.addObject("appBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("appBtn", Constant.DISABLE);
        }
        //是否展示详情按钮
        if (resourcesUrl.contains(ConstantCtl.HQCLCF_LEAVE + ConstantCtl.DETAIL)) {
            modelAndView.addObject("detailBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("detailBtn", Constant.DISABLE);
        }
        modelAndView.setViewName(ConstantCtl.INITVIEW_HQCLCF);
    }

    /**
     * @param file
     * @returnHQCLCF_EMP_FILE
     */
    private StringBuffer bulidPathByempFile(HqclcfEmpFile file) {
        StringBuffer path = new StringBuffer();
        path.append(Constant.HDFS_OPT_PATH);
        path.append(File.separator);
        switch (file.getBusinessLine()) {
            case 1://总部
                path.append(Constant.ZHPHHQ);
                break;
            case 2://消分
                path.append(Constant.ZHPHXJ);
                break;
            case 3://信贷
                path.append(Constant.ZHXD);
                break;
        }
        path.append(File.separator);
        path.append(file.getEmpNo());
        path.append(File.separator);
        path.append(file.getFileType().toString());
        path.append(File.separator);
        return path;
    }

    private Map<String, Integer> buildFileMapName(String sysCode, Json msg) {
        Map<String, Integer> fileMap = new HashMap<>();
        try {
            List<SysConfigType> listFiles = sysConfigTypeMapper.getConfigByPSysCode(sysCode);
            for (SysConfigType configType : listFiles) {
                fileMap.put(configType.getSysCode(), configType.getSysValue());
            }
            msg.setSuccess(true);
        } catch (Exception e) {
            msg.setSuccess(false);
            logger.error("生成文件文件映射名称时：" + e.getMessage());
        }
        return fileMap;
    }

    @SuppressWarnings("unused")
	private HqclcfEmpFile uploadFile2Hdfs(Iterator<Map.Entry<String, Integer>> hqIter, HqclcfEmp empLeave, MultipartHttpServletRequest request) throws Exception {
        SysUser onlineUser = CommonUtil.getOnlineUser();
        boolean flag = true;
        HqclcfEmpFile empFile = null;
       String  zhPath=null;
        while (hqIter.hasNext()) {
            Map.Entry<String, Integer> hqNext = hqIter.next();
            Integer fileType = hqNext.getValue();
            String fileKey = hqNext.getKey();
            MultipartFile file = request.getFile(fileKey);
            String originalFilename = file.getOriginalFilename();
            if (file != null) {
                if ("".equals(file.getOriginalFilename())){
                    continue;
                }
            	Integer businessLine = empLeave.getBusinessLine();
                empFile = new HqclcfEmpFile();
                empFile.setBusinessLine(businessLine);
                empFile.setCreateTime(new Date());
                empFile.setCreator(onlineUser.getFullName());
                empFile.setEmpNo(empLeave.getEmpNo());
                empFile.setFileName(originalFilename);
                empFile.setFileType(fileType);
                empFile.setFileExtend(originalFilename.substring(originalFilename.lastIndexOf(".")));
                switch (businessLine) {
    			case 1:
    				zhPath=Constant.ZHPHHQ;
    				break;
    			case 2:
    				zhPath=Constant.ZHPHXJ;
    				break;
    			case 3:
    				zhPath=Constant.ZHXD;
    				break;
    			}
                //后期提供删除
                boolean result = FileUpload.upload(file, empLeave.getEmpNo(), zhPath, empFile.getFileType().toString(), empFile.getFileName(), logger, urlConfig.getFileUpload());
                flag = result;
                if (flag) {
                    hqclcfEmpFileMapper.deleteFile(empFile.getEmpNo(), empFile.getFileType());
                    hqclcfEmpFileMapper.insertempFile(empFile);
                }            	
            }
        }
        return empFile;
    }

	@Override
	public ModelAndView queryAllOpenTypes(ModelAndView model, Long id) throws Exception {
		Json json = new Json();
        model.addObject("depts", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfDeptMapper.queryAll(new HqclcfDept()))));
        model.addObject("dept", hqclcfDeptMapper.queryAll(new HqclcfDept()));
        model.addObject("ranks", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfRankMapper.getRankByCondition(new HqclcfRank()))));
        model.addObject("rank", hqclcfRankMapper.getRankByCondition(new HqclcfRank()));
        model.addObject("businesses", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfBusinessMapper.getBusinessByCondition(new HqclcfBusiness()))));
        model.addObject("business", hqclcfBusinessMapper.getBusinessByCondition(new HqclcfBusiness()));
        model.addObject("posts", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfPostMapper.queryAll(new HqclcfPost()))));
        model.addObject("post", hqclcfPostMapper.queryAll(new HqclcfPost()));
        List<Integer> list = baseService.getOnlineUserBl();
        model.addObject("blList", list);
        return model;
	}

	@Override
	public List<Map<String, String>> queryAllLeave(String q) throws Exception {
		
		 HashMap<String, String> queryMap = new HashMap<>();
	        queryMap.put("q", q);
	        queryMap.put("userId", CommonUtil.getOnlineUser().getUserId());
		
	        List<Map<String, String>> lists = hqclcfEmpMapper.queryEmpByq1(queryMap);
       
        
	        return lists;
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

	@Override
	public void buildEditFormReult(Long id, HttpServletRequest req) throws Exception {
		HqclcfLeave hqclcfLeave = new HqclcfLeave();
		hqclcfLeave.setPriNumber(id);
		List<HqclcfLeave> Leave = hqclcfLeaveMapper.queryAllLeave(hqclcfLeave);
		HqclcfLeave hqclcfLeave1 = Leave.get(0);
		req.setAttribute("hqclcfLeave1",hqclcfLeave1);
		HqclcfDept dept = hqclcfDeptService.queryDeptByDeptCode(hqclcfLeave1.getDeptNo());
        req.setAttribute("dept",dept);
        HqclcfPost post = hqclcfPostMapper.queryByPostNo(hqclcfLeave1.getPost());
        req.setAttribute("post",post);
        //根据岗位部门id构建职务
        List<HqclcfBusiness> businesses = hqclcfBusinessMapper.queryBusinessByDeptIdAndPost(dept.getId(), post.getPriNumber());
        req.setAttribute("businessesList",businesses);
      //根据已选择的职务构建职级
        HqclcfBusiness hqclcfBusiness = hqclcfBusinessMapper.queryBusinessByCode(hqclcfLeave1.getPosition());

        String rankCodeStr = hqclcfBusiness.getRankCode();
        List<String> rankList=new ArrayList<>();
        List<HqclcfRank> rankByRankCodes=new ArrayList<>();
        if (!"".equals(rankCodeStr)&&rankCodeStr!=null){
            String[] rankCode = rankCodeStr.split(",");
            for (int i = 0; i <rankCode.length ; i++) {
                rankList.add(rankCode[i]);
            }
            rankByRankCodes = hqclcfRankMapper.getRankByRankCodes(rankList);

        }
        req.setAttribute("rankList",rankByRankCodes);
        HqclcfEmpFile file=new HqclcfEmpFile();
        file.setEmpNo(hqclcfLeave1.getEmpNo());
        List<HqclcfEmpFile> hqclcfEmpFiles = hqclcfEmpFileMapper.queryEmpFile(file);
        req.setAttribute("files",hqclcfEmpFiles);
        
	}

}
