package com.zhph.service.hqclcf.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.config.UrlConfig;
import com.zhph.exception.AppException;
import com.zhph.mapper.hqclcf.*;
import com.zhph.mapper.sys.SysConfigTypeMapper;
import com.zhph.mapper.sys.SysWorkplacesetMapper;
import com.zhph.model.common.OperateType;
import com.zhph.model.hqclcf.*;
import com.zhph.model.hqclcf.dto.*;
import com.zhph.model.sys.*;
import com.zhph.service.cf.TimeAutomatedService;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.service.hqclcf.HqclcfEmpApvService;
import com.zhph.service.hqclcf.HqclcfEmpService;
import com.zhph.service.sys.SysCalendarPoolService;
import com.zhph.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
@EnableConfigurationProperties(UrlConfig.class)
public class HqclcfEmpApvServiceImpl implements HqclcfEmpApvService{

    public static final Logger logger = LogManager.getLogger(HqclcfEmpApvServiceImpl.class);

    @Autowired
    private HqclcfEmpApvMapper empApvMapper;
    @Resource
    private SysConfigTypeMapper sysConfigTypeMapper;
    @Resource
    private HqclcfDeptService hqclcfDeptService;
    @Resource
    private HqclcfBusinessMapper hqclcfBusinessMapper;
    @Autowired
    private SysWorkplacesetMapper sysWorkplacesetMapper;
    @Autowired
    private HqclcfPostMapper hqclcfPostMapper;
    @Resource
    private HqclcfRankMapper hqclcfRankMapper;
    @Autowired
    private  UrlConfig urlConfig;
    @Resource
    private HqclcfEmpFileMapper hqclcfEmpFileMapper;
    @Resource
    private SysCalendarPoolService calendarPoolService;
    @Resource
    private BaseService baseService;
    @Resource
    private  HqclcfDeptMapper hqclcfDeptMapper;
    @Resource
    private TimeAutomatedService timeAutomatedService;
    @Resource
	private  HqclcfEmpService  hqclcfEmpService;



    @Override
    public Grid<HqclcfEmp> queryPageInfo(PageBean pageBean, HqclcfEmp emp) throws Exception {
        Grid<HqclcfEmp> grid=new Grid<>();
        try {
            buildQueryParms(emp);
            PageHelper.startPage(pageBean.getPage(),pageBean.getLimit());
            List<HqclcfEmp> list = empApvMapper.queryAll(emp);
            PageInfo<HqclcfEmp> pageInfo=new PageInfo<>(list);
            grid.setData(pageInfo.getList());
            grid.setCount(pageInfo.getTotal());
        } catch (Exception e) {
            logger.error("分页查询失败:"+e.getMessage());
            e.printStackTrace();
        }
        return grid;
    }

    /**
     * 构建查询参数
     * @param emp
     * @throws Exception
     */
    private void buildQueryParms(HqclcfEmp emp) throws Exception{
        if(emp.getDeptHid()!=null&&!"".equals(emp.getDeptHid())&&emp.getQx()!=null){
            emp.setDeptNo(null);
        }else if (emp.getDeptHid()!=null&&!"".equals(emp.getDeptHid())&&emp.getQx()==null){
            HqclcfDept dept = hqclcfDeptService.queryDept(emp.getDeptHid());
            if (dept!=null){
                emp.setDeptNo(dept.getDeptCode());
            }
        }
        if(emp.getEmpNo()!=null&&!"".equals(emp.getEmpNo())){
            emp.setOperType("empNoFuzzyQuery");
        }
        if(emp.getEmpName()!=null&&!"".equals(emp.getEmpName())){
            emp.setOperType("empNameFuzzyQuery");
        }
        //数据权限：当前登录用户可以查看的部门的数据权限
        SysUser onlineUser = CommonUtil.getOnlineUser();
        String userId = onlineUser.getUserId();
        emp.setUserId(userId);
    }


    @Override
    public HqclcfEmpApv2SelectJsonResult bulidSelectDataByDeptId(String data) throws Exception{
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
                    if (!"".equals(rankCodeStr)&&rankCodeStr!=null){
                        String[] rankCode = rankCodeStr.split(",");
                        for (int i = 0; i <rankCode.length ; i++) {
                            rankList.add(rankCode[i]);
                        }
                        List<HqclcfRank> rankByRankCodes = hqclcfRankMapper.getRankByRankCodes(rankList);
                        result.setRankData(rankByRankCodes);
                    }else{
                        List<HqclcfRank> rankByRankCodes=new ArrayList<>();
                        result.setRankData(rankByRankCodes);
                    }
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
                    break;
                case"enterDateCheck":
                    Date enterDate = apvIdArray.getEnterDate();
                    Map<String,Object> calMap=new HashMap();
                    calMap.put("cldDate",Integer.parseInt(DateUtil.parseDateFormat(enterDate, "yyyy-MM-dd").replace("-", "")));
                    List<SysCalendarPool> byCondtion = calendarPoolService.findByCondtion(calMap, false);
                    if (byCondtion.size()>0){
                        SysCalendarPool sysCalendarPool =null!= byCondtion.get(0)?byCondtion.get(0):null;
                        //1是最后一个工作日
                        boolean islastwork = calendarPoolService.ifLastWorkDay(DateUtil.parseDateFormat(enterDate, "yyyy-MM-dd"));
                        //0是工作日
                        boolean cldFlg = "0".equals(sysCalendarPool.getCldFlg()) ? true : false;
                        if (!islastwork&&cldFlg){
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(enterDate);
                            Map<String, Date> dateMap = DateUtil.getEndDate(calendar);
                            result.setFulltimeEndDate(dateMap.get("endDate"));
                        }else{
                            result.setMsg("入职日期为最后一个工作日");
                            result.setSuccess(false);
                        }
                    }else{
                        result.setMsg("入职日期请选择工作日");
                        result.setSuccess(false);
                    }
                    break;
                case "fulltimeBeginDateClick":
                    Date fulltimeBeginDate = apvIdArray.getFulltimeBeginDate();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fulltimeBeginDate);
                    Map<String, Date> fulltimeBeginDateMap = DateUtil.getEndDate(cal);
                    result.setFulltimeEndDate(fulltimeBeginDateMap.get("endDate"));
                    break;
            }
        return result;
    }

    public void addEmp(HqclcfEmp emp, MultipartHttpServletRequest request) throws Exception {
            HqclcfEmp addEmp = buildFormEmp2Emp(emp);

            uploadFile(request,addEmp);

            //新增员工处理状态创建人等
            addEmp.setStatusApp(4);
            addEmp.setCreator(CommonUtil.getOnlineFullName());
            addEmp.setCreatetime(new Date());

            empApvMapper.insert(addEmp);

            //记录到日志
            baseService.saveLog(null,addEmp,addEmp.getClass(), OperateType.SAVE,logger);
    }



    @Override
    public void editEmp(HqclcfEmp emp, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
            HqclcfEmp editEmp = buildFormEmp2Emp(emp);

            uploadFile(multipartHttpServletRequest,editEmp);

            //添加修改信息
            editEmp.setUpdatetime(new Date());
            editEmp.setUpdator(CommonUtil.getOnlineFullName());
            editEmp.setStatusApp(4);
            //修改员工
            empApvMapper.updateByEmp(editEmp);

            //记录到日志
            baseService.saveLog(null,editEmp,editEmp.getClass(), OperateType.UPDATE,logger);
    }

    /**
     * 审批员工信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject apvEmp(Long id,HqclcfEmp emp,SysUser sysUser) throws Exception {
        JSONObject obj=new JSONObject();
        obj.put("code","200");
        try {
            HqclcfEmp apvEmp = queryEmp(id);
            String resultsApp = emp.getResultsApp();
            switch (resultsApp){
                case "1"://通过
                    apvEmp.setStatusApp(1);
                    apvEmp.setStatus(1);//在职
                    apvEmp.setReasonsApp(emp.getReasonsApp());
                    apvEmp.setResultsApp(resultsApp);
                    timeAutomatedService.addOneEmpForEntry(apvEmp, sysUser);
                    //獲取時間信息，添加排班備份表
                    Date enterDate = emp.getEnterDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    String str = sdf.format(enterDate);
                    hqclcfEmpService.bakupHqclcfEmpByGzym(str);
                    break;
                case "0"://拒绝
                    apvEmp.setStatusApp(2);
                    apvEmp.setReasonsApp(emp.getReasonsApp());
                    apvEmp.setResultsApp(resultsApp);
                    break;
            }

            apvEmp.setApprover(CommonUtil.getOnlineFullName());
            apvEmp.setApproverTime(new Date());
            //修改员工状态
            empApvMapper.updateByEmp(apvEmp);

        } catch (Exception e) {
            obj.put("code","500");
            obj.put("msg","审批失败:"+e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 撤销
     * @param id
     * @throws Exception
     */
    @Override
    public JSONObject delEmp(Long id) throws Exception {
        JSONObject obj=new JSONObject();
        obj.put("code",200);

        try {
            HqclcfEmp emp = queryEmp(id);
            if( CommonUtil.getOnlineFullName().equals(emp.getCreator())){
                emp.setStatusApp(3);
                empApvMapper.updateByEmp(emp);
            }else{
                obj.put("code",500);
                obj.put("msg","当前数据的创建人为："+emp.getCreator()+",必须是创建人才能撤销当前数据。");
            }
        } catch (Exception e) {
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void buildListTpl(HttpServletRequest req, ModelAndView model) throws Exception {
        Json json=new Json();
        ObjectMapper mapper=new ObjectMapper();
        //业务线
        baseService.addObject(model, Constant.CODE_LIST_TPL,Constant.BUSINESS_LINE);
        //部门
        HqclcfDept dept=new HqclcfDept();
        req.setAttribute("deptListTpl",mapper.writeValueAsString(json.getObj(hqclcfDeptMapper.queryAll(dept))));
        //工作地
        SysWorkplaceset workplaceset=new SysWorkplaceset();
        req.setAttribute("workplacesetListTpl",mapper.writeValueAsString(json.getObj(sysWorkplacesetMapper.queryAllWorkplaceset(workplaceset))));
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
    public JSONObject checkIspreviewHdsfFile(HqclcfEmpFile file) {
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        List<HqclcfEmpFile> list = hqclcfEmpFileMapper.queryFileByParam(file.getEmpNo(), file.getFileType());
        HqclcfEmpFile hqclcfEmpFile = list.size() > 0 ? list.get(0) : null;
        String fileName = hqclcfEmpFile.getFileName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        if (suffix.equals("txt")){
            obj.put("code",500);
            obj.put("msg","txt格式的文件不支持预览！");
        }
        if (suffix.equals("rar")){
            obj.put("code",500);
            obj.put("msg","rar格式的文件不支持预览！");
        }
        if (suffix.equals("zip")){
            obj.put("code",500);
            obj.put("msg","zip格式的文件不支持预览！");
        }
        return obj;
    }

    @Override
    public HqclcfEmp queryEmp(Long id) throws Exception {
        HqclcfEmp param=new HqclcfEmp();
        param.setId(id);
        List<HqclcfEmp> hqclcfEmps = empApvMapper.queryAll(param);
        if(hqclcfEmps.size()>1)
            throw new Exception("一个ID只能有一个员工！");
        HqclcfEmp emp = hqclcfEmps.size() == 1 ? hqclcfEmps.get(0) : null;
        return emp;
    }

    @Override
    public void buildEditFormReult(Long id, HttpServletRequest req) throws Exception {
        HqclcfEmp emp = queryEmp(id);
        req.setAttribute("emp",emp);
        HqclcfDept dept = hqclcfDeptService.queryDeptByDeptCode(emp.getDeptNo());
        req.setAttribute("dept",dept);
        HqclcfPost post = hqclcfPostMapper.queryByPostNo(emp.getPost());
        req.setAttribute("post",post);
        //根据岗位部门id构建职务
        List<HqclcfBusiness> businesses = hqclcfBusinessMapper.queryBusinessByDeptIdAndPost(dept.getId(), post.getPriNumber());
        req.setAttribute("businessesList",businesses);
        //根据已选择的职务构建职级
        HqclcfBusiness hqclcfBusiness = hqclcfBusinessMapper.queryBusinessByCode(emp.getPosition());

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
        file.setEmpNo(emp.getEmpNo());
        List<HqclcfEmpFile> hqclcfEmpFiles = hqclcfEmpFileMapper.queryEmpFile(file);
        req.setAttribute("files",hqclcfEmpFiles);
        //构建工作地和社保购买地
        Map<String,String> workPalceMap=new HashMap<>();
        SysWorkplaceset workplaceset=new SysWorkplaceset();
        List<SysWorkplaceset> workplacesetList = sysWorkplacesetMapper.queryAllWorkplaceset(workplaceset);
        for (SysWorkplaceset work: workplacesetList) {
            workPalceMap.put(work.getAreaCode(),work.getArea());
        }
        req.setAttribute("workName",null != workPalceMap.get(emp.getWorkOrgNo()) ? workPalceMap.get(emp.getWorkOrgNo()) : "");
        req.setAttribute("socialName",null != workPalceMap.get(emp.getSocialSecurityOrgNo()) ? workPalceMap.get(emp.getSocialSecurityOrgNo()) : "");
    }

    @Override
    public void showEmpPoto(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) throws Exception {
        List<HqclcfEmpFile> hqclcfEmpFiles = hqclcfEmpFileMapper.queryFileByParam(file.getEmpNo(), file.getFileType());
        HqclcfEmpFile empFile = hqclcfEmpFiles.size() > 0 ? hqclcfEmpFiles.get(0) :null;
        if (empFile!=null){
            String fileTypeFlag = empFile.getFileTypeFlag();
            //查询文件表记录了这这个员工图片没有
            List<HqclcfEmpFile> files = hqclcfEmpFileMapper.queryEmpFile(file);
            switch (fileTypeFlag){
                case "oldSalary":
                    List<String> list = bulidPathByempFiles(empFile);
                    if (files!=null&&files.size()>0){
                        FileUtil.showImg(res,list,urlConfig);
                    }
                    break;
                case "newSalary":
                    StringBuffer stringBuffer = bulidPathByempFile(empFile);
                    if (files!=null&&files.size()>0){
                        FileUtil.showImg(res,stringBuffer.toString(),urlConfig);
                    }
                    break;
            }

        }
    }

    /**
     * 新系统新增的文件路径构建
     * @param empFile
     * @return
     * @throws Exception
     */
    private StringBuffer bulidPathByempFile(HqclcfEmpFile empFile) throws Exception{
        StringBuffer path = new StringBuffer();
        path.append(Constant.HDFS_OPT_PATH);
        path.append(File.separator);
        buildBlbyFileAndPath(empFile,path);
        path.append(File.separator);
        path.append(empFile.getEmpNo());
        path.append(File.separator);
        path.append(empFile.getFileType());
        path.append(File.separator);
        path.append(empFile.getFileName());
        return path;
    }

    /**
     * 根据业务条线构建路径
     * @param file
     * @param path
     */
    private void buildBlbyFileAndPath(HqclcfEmpFile file,StringBuffer path){
        switch (file.getBusinessLine()){
            case 2://消分
                path.append(Constant.ZHPHXJ);
                break;
            case 1://总部
                path.append(Constant.ZHPHHQ);
                break;
            case 3://信贷
                path.append(Constant.ZHXD);
                break;
        }
    }
    /**
     * 通过老系统的文件类型构建文件路径
     * @param file
     * @return
     * @throws Exception
     */
    private List<String> bulidPathByempFiles(HqclcfEmpFile file) throws Exception{
        SysMidFileType midFileType=new SysMidFileType();
        midFileType.setBusinessline(file.getBusinessLine());
        midFileType.setNewFileType(file.getFileType());
        List<String> paths=new ArrayList<>();
        StringBuffer path = new StringBuffer();
        path.append(Constant.HDFS_OPT_PATH);
        path.append(File.separator);
        buildBlbyFileAndPath(file,path);
        path.append(File.separator);
        path.append(file.getEmpNo());
        path.append(File.separator);
        List<Map<String, Object>> list = empApvMapper.queryMidFileType(midFileType);
        for (Map<String, Object> map:list) {
            Object oldFileType = map.get("OLD_FILE_TYPE");
            if (oldFileType!=null){
                path.append(oldFileType.toString());
                path.append(File.separator);
                path.append(file.getFileName());
                paths.add(path.toString());
            }
        }
        return  paths;
    }



    /**
     * 文件预览
     * @param file
     * @param req
     * @param res
     */
    @Override
    public void previewHdsfFile(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) {
        try {
            List<HqclcfEmpFile> hqclcfEmpFiles = hqclcfEmpFileMapper.queryFileByParam(file.getEmpNo(), file.getFileType());
            HqclcfEmpFile hqclcfEmpFile = hqclcfEmpFiles.size() > 0 ? hqclcfEmpFiles.get(0) :null;
            if (hqclcfEmpFile!=null){
                String fileTypeFlag = hqclcfEmpFile.getFileTypeFlag();
                if ("oldSalary".equals(fileTypeFlag)){
                    List<String> list = bulidPathByempFiles(hqclcfEmpFile);
                    //从hdfs上拉取文件预览
                    FileUtil.showFile(res,list,hqclcfEmpFile.getFileName(),urlConfig);
                }else{
                    StringBuffer stringBuffer = bulidPathByempFile(hqclcfEmpFile);
                    FileUtil.showFile(res,stringBuffer.toString(),hqclcfEmpFile.getFileName(),urlConfig);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 文件下载
     * @param file
     * @param req
     * @param res
     * @throws Exception
     */
    @Override
    public void downloadHdsfFile(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) throws Exception {
        List<HqclcfEmpFile> hqclcfEmpFiles = hqclcfEmpFileMapper.queryFileByParam(file.getEmpNo(), file.getFileType());
        HqclcfEmpFile hqclcfEmpFile = hqclcfEmpFiles.size() > 0 ? hqclcfEmpFiles.get(0) :null;
        if (hqclcfEmpFile!=null){
            String fileTypeFlag = hqclcfEmpFile.getFileTypeFlag();
            if ("oldSalary".equals(fileTypeFlag)){
                List<String> list = bulidPathByempFiles(hqclcfEmpFile);
                FileUtil.downFileForHdfs(res,list,hqclcfEmpFile.getFileName(),urlConfig);
            }else{
                StringBuffer sb = bulidPathByempFile(hqclcfEmpFile);
                FileUtil.downFileForHdfs(res,sb.toString(),hqclcfEmpFile.getFileName(),urlConfig);
            }
        }
    }


    /**
     *   构建form参数成我们需要保存的emp参数
     * @param emp
     * @throws Exception
     */
    private HqclcfEmp buildFormEmp2Emp(HqclcfEmp emp) throws Exception{
        //户籍地址
        StringBuffer nativePlace=new StringBuffer();
        nativePlace.append(null!=emp.getHjProvince()?emp.getHjProvince():"");
        nativePlace.append(null!=emp.getHjCity()?emp.getHjCity():"");
        nativePlace.append(null!=emp.getHjArea()?emp.getHjArea():"");
        nativePlace.append(null!=emp.getHjxjdz()?emp.getHjxjdz():"");
        emp.setNativePlace(nativePlace.toString());
        //现居地址
        StringBuffer addr=new StringBuffer();
        addr.append(null!=emp.getXjProvince()?emp.getXjProvince():"");
        addr.append(null!=emp.getXjCity()?emp.getXjCity():"");
        addr.append(null!=emp.getXjArea()?emp.getHjArea():"");
        addr.append(null!=emp.getXjxxdz()?emp.getXjxxdz():"");
        emp.setAddr(addr.toString());
        //银行卡开户行地址
        StringBuffer openBankOrg=new StringBuffer();
        openBankOrg.append(emp.getBankProvince());
        openBankOrg.append(emp.getBankCity());
        openBankOrg.append(emp.getBankxxdj());
        emp.setOpenBankOrg(openBankOrg.toString());
        //部门code
        Long deptId= emp.getDeptHid();
        HqclcfDept dept = hqclcfDeptService.queryDept(deptId);
        emp.setDeptNo(dept.getDeptCode());
        //岗位Code
        Long postId = emp.getPostHid();
        HqclcfPost post = hqclcfPostMapper.queryByPostId(postId, null);
        emp.setPost(post.getPostNo());
        //隐藏的bl
        emp.setBusinessLine(emp.getBusinessLineHid());
        //备注
        if (emp.getBusinessLine()==null)
            throw new AppException("新增员工业务条线为空，请检出是否数据错误！");
        switch (emp.getBusinessLine()){
            case 3:
                emp.setComments(emp.getXdbz());
                break;
            case 2:
                emp.setComments(emp.getXjbz());
                break;
            case 1:
                emp.setComments(emp.getZbbz());
                break;
        }
        //百融不良信息接口
        Map<String,Object> brMap=new HashMap<>();
        brMap.put("login_code",urlConfig.getLoginCode());
        brMap.put("login_pw", urlConfig.getLoginPw());
        brMap.put("meal", urlConfig.getMeal());
        brMap.put("id", emp.getIdCard());
        brMap.put("cell", emp.getMobilePhone());
        brMap.put("name", emp.getEmpName());
        String str = HttpClientHelper.doPost(urlConfig.getBadInformationUrl(),brMap);
        if (str!=null&&!"".equals(str)){
            ObjectMapper mapper=new ObjectMapper();
            HqclcfEmpApvBrResult result = mapper.readValue(str.getBytes(), HqclcfEmpApvBrResult.class);
            int flagBadinfo = result.getFlag().getFlagBadinfo();
            switch (flagBadinfo){
                case 0:
                    emp.setBadCredit(null);
                    break;
                case 1:
                    String checkcount2 = result.getProduct().getCheckcount2();
                    Integer caseCount = Integer.valueOf(checkcount2);
                    //如果有不良信息
                    StringBuffer sb=new StringBuffer("");
                    StringBuffer sbtpye =new StringBuffer();
                    if (caseCount>0){
                        HqclcfEmpApvBrProduct product = result.getProduct();
                        sbtpye.append("案件类型：");
                        sbtpye.append("1".equals(product.getSdcheckresult()) ? "涉毒 " : " ");
                        sbtpye.append("1".equals(product.getWfxwcheckresult()) ? "违反行为 " : " ");
                        sbtpye.append("1".equals(product.getZtcheckresult()) ? "在逃 " : " ");
                        sbtpye.append("1".equals(product.getXdcheckresult()) ? "吸毒 " : " ");
                        String casetime =product.getItem().getCasetime();
                        String timeDescription = HqclcfEmpApvEnumCaseTime.getTimeDescription(casetime);
                        String casetypecode = product.getItem().getCasetypecode();
                        sb = sbtpye.append("案件时间：" + (!"".equals(timeDescription) ? timeDescription : "") + ";案件说明：" + (!"".equals(casetypecode) ? casetypecode : ""));
                    }
                    emp.setBadCredit(sb.toString());
                    break;
            }
        }
        return  emp;
    }
    /**
     * 上传附件
     * @param request
     */
    private void uploadFile(MultipartHttpServletRequest request,HqclcfEmp emp) throws Exception{
        Integer businessLine = emp.getBusinessLine();
        switch (businessLine){
            case 3://信贷
                Iterator<Map.Entry<String, Integer>> xdIter = buildFileMapName(Constant.XD_FILE).entrySet().iterator();
                uploadFile2Hdfs(xdIter,emp,request,Constant.ZHXD);
                break;
            case 2://消分
                Iterator<Map.Entry<String, Integer>> xfIter = buildFileMapName(Constant.XF_FILE).entrySet().iterator();
                uploadFile2Hdfs(xfIter,emp,request,Constant.ZHPHXJ);
                break;
            case 1://总部
                Iterator<Map.Entry<String, Integer>> hqIter = buildFileMapName(Constant.ZB_FILE).entrySet().iterator();
                uploadFile2Hdfs(hqIter,emp,request,Constant.ZHPHHQ);
                break;
        }
    }

    private Map<String,Integer> buildFileMapName(String sysCode) throws Exception{
        Map<String,Integer> fileMap=new HashMap<>();
        List<SysConfigType> listFiles = sysConfigTypeMapper.getConfigByPSysCode(sysCode);
        List<SysConfigType> empimgpotos = sysConfigTypeMapper.getConfigByPSysCode(Constant.EMPIMGPOTO);
        SysConfigType config = empimgpotos.size() == 1 ? empimgpotos.get(0) : null;
        if (config!=null){
            fileMap.put(config.getSysCode(),config.getSysValue());
        }
        for (SysConfigType configType :listFiles) {
            fileMap.put(configType.getSysCode(),configType.getSysValue());
        }
        return fileMap;
    }


    private void uploadFile2Hdfs(Iterator<Map.Entry<String, Integer>> hqIter, HqclcfEmp emp, MultipartHttpServletRequest request,String zhPath) throws Exception{
        SysUser onlineUser = CommonUtil.getOnlineUser();
        while (hqIter.hasNext()) {
            Map.Entry<String, Integer> hqNext = hqIter.next();
            Integer fileType = hqNext.getValue();
            String fileKey= hqNext.getKey();
            MultipartFile file = request.getFile(fileKey);
            if(file!=null){
                if ("".equals(file.getOriginalFilename())){
                    continue;
                }
                HqclcfEmpFile empFile=new HqclcfEmpFile();
                empFile.setBusinessLine(emp.getBusinessLine());
                empFile.setCreateTime(new Date());
                empFile.setCreator(onlineUser.getFullName());
                empFile.setEmpNo(emp.getEmpNo());
                empFile.setFileName(file.getOriginalFilename());
                empFile.setFileType(fileType);
                empFile.setFileExtend(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
                FileUpload.upload(file, emp.getEmpNo(), zhPath, empFile.getFileType().toString(), empFile.getFileName(), logger, urlConfig.getFileUpload());
                //上传正常运行，就代表没立刻抛出异常便会记录到文件表里面去
                hqclcfEmpFileMapper.deleteFile(empFile.getEmpNo(),empFile.getFileType());
                hqclcfEmpFileMapper.insertempFile(empFile);
            }
        }
    }


    @Override
    public JSONObject empCheck(String data) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("success", true);

        ObjectMapper mapper = new ObjectMapper();
        HqclcfEmp hqclcfEmp = mapper.readValue(data.getBytes(), HqclcfEmp.class);

        /**
         * 身份证验证
         */
        String idCard = hqclcfEmp.getIdCard();
        boolean idCardFlag = true;
        if (idCard != null && !"".equals(idCard)) {
            String msg = IDCardUtil.IDCardValidate(idCard);
            if (!"YES".equals(msg) && idCardFlag) {
                obj.put("msg", msg);
                obj.put("success", false);
                idCardFlag = false;
            }
            if (idCardFlag) {
                HqclcfEmp idcardEmp = new HqclcfEmp();
                idcardEmp.setIdCard(hqclcfEmp.getIdCard());
                int a = empApvMapper.queryAll(idcardEmp).size();
                switch (hqclcfEmp.getOperType()) {
                    case "":
                        if (a > 0 ? true : false) {
                            obj.put("msg", "这个身份证号已经被录入");
                            obj.put("success", false);
                        }
                        break;
                    case "edit":
                        if (a > 1 ? true : false) {
                            obj.put("msg", "这个身份证号已经被录入");
                            obj.put("success", false);
                        }
                        break;
                }
            }
        }

        /**
         * 员工姓名验证
         */
        String empName = hqclcfEmp.getEmpName();
        if (empName != null && !"".equals(empName)) {
            HqclcfEmp empNameEmp = new HqclcfEmp();
            empNameEmp.setEmpName(empName);
            int b = empApvMapper.queryAll(empNameEmp).size();
            switch (hqclcfEmp.getOperType()) {
                case "":
                    if (b > 0 ? true : false) {
                        obj.put("msg", "'" + empName + "'此员工姓名已经被录入");
                        obj.put("success", false);
                    }
                    break;
                case "edit":
                    if (b > 1 ? true : false) {
                        obj.put("msg", "'" + empName + "'此员工姓名已经被录入");
                        obj.put("success", false);
                    }
                    break;
            }
        }
        /**
         * 身份证员工姓名
         */
        String idcardName = hqclcfEmp.getIdcardName();
        if (idcardName != null && !"".equals(idcardName)) {
            HqclcfEmp idcardNameEmp = new HqclcfEmp();
            idcardNameEmp.setIdcardName(idcardName);
            int c = empApvMapper.queryAll(idcardNameEmp).size();
            switch (hqclcfEmp.getOperType()) {
                case "":
                    if (c > 0 ? true : false) {
                        obj.put("msg", "'" + idcardName + "'此身份证员工姓名已经被录入");
                        obj.put("success", false);
                    }
                    break;
                case "edit":
                    if (c > 1 ? true : false) {
                        obj.put("msg", "'" + idcardName + "'此身份证员工姓名已经被录入");
                        obj.put("success", false);
                    }
                    break;
            }
        }

        /**
         * 卡号验证
         */
        String pfBankNo = hqclcfEmp.getPfBankNo();
        if (pfBankNo != null && !"".equals(pfBankNo)) {
            HqclcfEmp pfBankNoEmp = new HqclcfEmp();
            pfBankNoEmp.setPfBankNo(pfBankNo);
            int d = empApvMapper.queryAll(pfBankNoEmp).size();
            switch (hqclcfEmp.getOperType()) {
                case "":
                    if (d > 0 ? true : false) {
                        obj.put("msg", "此银行卡卡号已经被录入");
                        obj.put("success", false);
                    }
                    break;
                case "edit":
                    if (d > 1 ? true : false) {
                        obj.put("msg", "此银行卡卡号已经被录入");
                        obj.put("success", false);
                    }
                    break;
            }
        }
        return obj;
    }
}
