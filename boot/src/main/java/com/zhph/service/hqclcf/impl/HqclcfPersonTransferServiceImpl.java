package com.zhph.service.hqclcf.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.config.UrlConfig;
import com.zhph.exception.AppException;
import com.zhph.mapper.common.ZhphObjectMapper;
import com.zhph.mapper.hqclcf.*;
import com.zhph.mapper.sys.SysConfigTypeMapper;
import com.zhph.model.common.OperateType;
import com.zhph.model.hqclcf.*;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysUser;
import com.zhph.model.vo.ResultVo;
import com.zhph.service.cf.TimeAutomatedService;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfPersonTransferService;
import com.zhph.service.sys.SysConfigTypeService;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service("HqclcfPersonTransferService")
@Transactional(rollbackFor = Exception.class)
@EnableConfigurationProperties(UrlConfig.class)
public class HqclcfPersonTransferServiceImpl implements HqclcfPersonTransferService {

    @Autowired
    private HqclcfPersonTransferMapper hqclcfPersonTransferMapper;

    @Autowired
    private HqclcfDeptMapper hqclcfDeptMapper;

    @Autowired
    private HqclcfPostMapper hqclcfPostMapper;

    @Autowired
    private HqclcfBusinessMapper hqclcfBusinessMapper;

    @Autowired
    private HqclcfRankMapper hqclcfRankMapper;

    @Autowired
    private ZhphObjectMapper zhphObjectMapper;

    @Autowired
    private HqclcfEmpMapper hqclcfEmpMapper;

    @Resource
    private SysConfigTypeMapper sysConfigTypeMapper;

    @Resource
    private HqclcfEmpFileMapper hqclcfEmpFileMapper;

    @Autowired
    private UrlConfig urlConfig;

    @Autowired
    private BaseService baseService;

    @Autowired
    private TimeAutomatedService  timeAutomatedService;

    @Autowired
    private HqclcfEmpOragnizationDtoMapper hqclcfEmpOragnizationDtoMapper;

    @Resource
    private SysConfigTypeService sysConfigTypeService;

    public static final Logger logger = LogManager.getLogger(HqclcfPersonTransferServiceImpl.class);

    @Override
    public Grid<HqclcfPersonTransfer> getPersonTransferByCondition(PageBean pageBean, HqclcfPersonTransfer hqclcfPersonTransfer) throws Exception {
        PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
        if (hqclcfPersonTransfer != null) {
            if (hqclcfPersonTransfer.getPriDeptNo() != null && !hqclcfPersonTransfer.getPriDeptNo().equals("")) {
                if (hqclcfPersonTransfer.getPriDeptNo().equals("E6C412A515AC")) {
                    hqclcfPersonTransfer.setPriDeptNo("");
                }
            }
            if (hqclcfPersonTransfer.getNewDept() != null && !hqclcfPersonTransfer.getNewDept().equals("")) {
                if (hqclcfPersonTransfer.getNewDept().equals("E6C412A515AC")) {
                    hqclcfPersonTransfer.setNewDept("");
                }
            }

        }
        hqclcfPersonTransfer.setPriHqRank("排空");
        hqclcfPersonTransfer.setLoginUserId(baseService.getOnlineUserBl());

        List<HqclcfPersonTransfer> dataList = hqclcfPersonTransferMapper.getPersonTransferByCondition(hqclcfPersonTransfer);
        List<HqclcfPersonTransfer> queryByName = new ArrayList<>();
        boolean flag = false;
        for (int i = 0; i < dataList.size(); i++) {
            HqclcfEmp emps = hqclcfEmpMapper.queryEmpNameByNo(dataList.get(i).getEmpNo());
            if (emps != null) {
                dataList.get(i).setEmpName(emps.getEmpName());
            }
            if (hqclcfPersonTransfer.getEmpName() != null && hqclcfPersonTransfer.getEmpName() != "") {
                flag = true;
                int s = dataList.get(i).getEmpName().lastIndexOf(hqclcfPersonTransfer.getEmpName());
                if (s != -1) {
                    queryByName.add(dataList.get(i));
                    continue;
                }
            }
        }
        if (flag) {
            PageInfo<HqclcfPersonTransfer> pageInfo = new PageInfo<>(queryByName);
            Grid<HqclcfPersonTransfer> grid = new Grid<>();
            grid.setData(pageInfo.getList());
            grid.setCount(pageInfo.getTotal());
            return grid;
        }
        PageInfo<HqclcfPersonTransfer> pageInfo = new PageInfo<>(dataList);
        Grid<HqclcfPersonTransfer> grid = new Grid<>();
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
        return grid;
    }

    @Override
    public ResultVo deleteByPrimaryKey(HqclcfPersonTransfer hqclcfPersonTransfer) throws Exception {
        ResultVo resultVo = new ResultVo();
        try {
            if (StringUtil.isEmpty(hqclcfPersonTransfer.getPriNumber().toString())) {
                resultVo.setInfo("传入id不能为空");
                resultVo.setStatus(0);
            } else {
                if (StringUtil.isEmpty(hqclcfPersonTransfer.getTransferTime())) {
                    resultVo.setInfo("生效时间不能为空");
                    resultVo.setStatus(0);
                } else {
                    int flag = dateUtilMethod(hqclcfPersonTransfer);
                    if (flag == -1 || flag == 0) {
                        resultVo.setInfo("该人员异动已生效，不允许删除");
                        resultVo.setStatus(0);
                    } else {
                        hqclcfPersonTransferMapper.deleteByPrimaryKey(hqclcfPersonTransfer.getPriNumber());
                        resultVo.setStatus(1);
                        resultVo.setInfo("删除成功");
                        baseService.saveLog(hqclcfPersonTransfer, null, HqclcfPersonTransferServiceImpl.class, OperateType.DELETE, logger);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除异常！" + e.getMessage());
            resultVo.setStatus(0);
        }
        return resultVo;
    }

    @Override
    public HqclcfPersonTransfer geTransferById(Long id) throws Exception {
        return hqclcfPersonTransferMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(HqclcfPersonTransfer hqclcfPersonTransfer, MultipartHttpServletRequest request, HttpServletRequest req) throws Exception {
        ResultVo resultVo = new ResultVo();
        HqclcfPersonTransfer trans = hqclcfPersonTransferMapper.selectByPrimaryKey(hqclcfPersonTransfer.getPriNumber());
        if (trans.getStatus().equals(Constant.ENABLE)) {
            resultVo.setStatus(0);
            resultVo.setInfo(" 人员异动已生效，无法修改！");
            return 2;
        }

        String newPostCode = hqclcfPersonTransfer.getNewPost();
        String newDeptCode = hqclcfPersonTransfer.getNewDept();

        HqclcfDept deptCheck = hqclcfDeptMapper.queryDeptByCode(newDeptCode);
        HqclcfPost postCheck = hqclcfPostMapper.queryByPostNo(newPostCode);

        if (deptCheck != null) {
            if (deptCheck.getStatus().equals(Constant.DISABLE)) {
                return 7;
            }
        }

        if (postCheck != null) {
            if (postCheck.getStatus().equals(Constant.DISABLE)) {
                return 6;
            }
        }

        /*检测是否跨业务条线*/
        if (!hqclcfPersonTransfer.getPriBusinessLine().equals(hqclcfPersonTransfer.getNewBusinessLine())) {
            String transferInfoFile = "transferInfoFile";
            String applyInfoFile = "applyInfoFile";
            MultipartFile apply = request.getFile(applyInfoFile);
            MultipartFile transfer = request.getFile(transferInfoFile);
            HqclcfEmpFile empFile = new HqclcfEmpFile();
            empFile.setEmpNo(hqclcfPersonTransfer.getEmpNo());
            empFile.setBusinessLine(Integer.valueOf(hqclcfPersonTransfer.getNewBusinessLine()));
            if (apply.getOriginalFilename() == null || apply.getOriginalFilename().equals("")) {
                empFile.setFileType(96527);
                List<HqclcfEmpFile> file = hqclcfEmpFileMapper.queryEmpFile(empFile);
                if (file.size() == 0) {
                    return 4;
                }
            }

            if (transfer.getOriginalFilename() == null || transfer.getOriginalFilename().equals("")) {
                empFile.setFileType(96528);
                List<HqclcfEmpFile> file = hqclcfEmpFileMapper.queryEmpFile(empFile);
                if (file.size() == 0) {
                    return 4;
                }
            }

        }

        int flag = dateUtilMethod(hqclcfPersonTransfer);
        try {
            // 修改选中的职级信息
            int result = hqclcfPersonTransferMapper.updateByPrimaryKeySelective(hqclcfPersonTransfer);
            uploadFile(request, hqclcfPersonTransfer);
            if (result > 0) {
                resultVo.setStatus(1);
                resultVo.setInfo(" 修改成功！");
                if (flag == 1) return 1;
                if (updateEmpTrans(hqclcfPersonTransfer) < 0) return 3;
                baseService.saveLog(hqclcfPersonTransfer, null, HqclcfPersonTransferServiceImpl.class, OperateType.UPDATE, logger);
                return 1;
            }
            resultVo.setStatus(0);
        } catch (Exception e) {
            resultVo.setStatus(0);
            resultVo.setInfo(e.getMessage() + "修改数据失败！");
            return 0;
        }
        return 0;
    }

    @Override
    public void exportExl(HqclcfPersonTransfer hqclcfPersonTransfer, HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            Map<String, String> businessLineMap = new HashMap<>();
            List<HqclcfPersonTransfer> list = hqclcfPersonTransferMapper.queryAllHqclcfPersonTransfer(hqclcfPersonTransfer);

            HqclcfEmp emp = new HqclcfEmp();
            HqclcfDept dept = new HqclcfDept();
            HqclcfPost post = new HqclcfPost();
            HqclcfRank rank = null;
            HqclcfBusiness business = new HqclcfBusiness();
            String[] colTitleAry = {"序号", "员工编码", "员工姓名", "原部门", "原业务条线", "原岗位", "原职务", "原职级", "异动类型", "生效日期", "新部门", "新业务条线", "新岗位", "新职务", "新职级", "创建人", "创建日期"};
            String[][] array = new String[list.size()][colTitleAry.length];
            short[] colWidthAry = {80, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 200};
            for (int i = 0; i < list.size(); i++) {
                HqclcfPersonTransfer hqclcfPersonTransfer1 = list.get(i);
                array[i][0] = i + 1 + ""; //序号
                array[i][1] = hqclcfPersonTransfer1.getEmpNo() + "";//员工编码

                if (hqclcfPersonTransfer1.getEmpNo() != null && !(hqclcfPersonTransfer1.getEmpNo().equals(""))) {
                    array[i][2] = hqclcfEmpMapper.queryEmpNameByNo(hqclcfPersonTransfer1.getEmpNo()).getEmpName(); //员工姓名
                }

                if (hqclcfPersonTransfer1.getPriDeptNo() != null && !(hqclcfPersonTransfer1.getPriDeptNo().equals(""))) {
                    dept.setDeptCode(hqclcfPersonTransfer1.getPriDeptNo());
                    if (hqclcfDeptMapper.queryAll(dept).size() > 0) {
                        array[i][3] = hqclcfDeptMapper.queryAll(dept).get(0).getDeptName(); //原部门
                    }
                }

                if (hqclcfPersonTransfer1.getPriPostNo() != null && !(hqclcfPersonTransfer1.getPriPostNo().equals(""))) {
                    post.setPostNo(hqclcfPersonTransfer1.getPriPostNo());
                    if (hqclcfPostMapper.queryByPostNo(hqclcfPersonTransfer1.getNewPost()) != null) {
                        array[i][5] = hqclcfPostMapper.queryByPostNo(hqclcfPersonTransfer1.getNewPost()).getPostName(); //原岗位
                    }
                }
                if (hqclcfPersonTransfer1.getPriBusinessLine() != null && !hqclcfPersonTransfer1.getPriBusinessLine().equals("")) {
                    array[i][4] = queryBusinessLine(hqclcfPersonTransfer1.getPriBusinessLine(), Constant.BUSINESS_LINE);//原业务条线
                }

                if (hqclcfPersonTransfer1.getPriHqPosition() != null && !(hqclcfPersonTransfer1.getPriHqPosition().equals(""))) {
                    business.setPosCode(hqclcfPersonTransfer1.getPriHqPosition());
                    if (hqclcfBusinessMapper.getBusinessByCondition(business).size() > 0) {
                        array[i][6] = hqclcfBusinessMapper.getBusinessByCondition(business).get(0).getPosName();//原职务
                    }
                }

                //array[i][7] = hqclcfPersonTransfer1.getPriHqRank();//原职级
                if (hqclcfPersonTransfer1.getPriHqRank() != null && !(hqclcfPersonTransfer1.getPriHqRank().equals(""))) {
                    rank = new HqclcfRank();
                    rank.setNo(hqclcfPersonTransfer1.getPriHqRank());
                    if (hqclcfRankMapper.getRankByCondition(rank).size() > 0) {
                        array[i][7] = hqclcfRankMapper.getRankByCondition(rank).get(0).getName(); //原职级
                    } else if (hqclcfRankMapper.getRankByCondition(rank).size() == 0) {
                        rank.setNo("");
                        rank.setName(hqclcfPersonTransfer1.getPriHqRank());
                        if (hqclcfRankMapper.getRankByCondition(rank).size() > 0) {
                            array[i][7] = hqclcfRankMapper.getRankByCondition(rank).get(0).getName(); //原职级
                        } else {
                            array[i][7] = "暂无";
                        }
                    }
                }


                if (hqclcfPersonTransfer1.getNewDept() != null && !(hqclcfPersonTransfer1.getNewDept().equals(""))) {
                    dept.setDeptCode(hqclcfPersonTransfer1.getNewDept());
                    if (hqclcfDeptMapper.queryAll(dept).size() > 0) {
                        array[i][10] = hqclcfDeptMapper.queryAll(dept).get(0).getDeptName(); //新部门
                    }
                }

                array[i][8] = queryBusinessLine(hqclcfPersonTransfer1.getTransferType(), Constant.TRANSFER_TYPE);//异动类型
                array[i][9] = hqclcfPersonTransfer1.getTransferTime();//生效日期

                array[i][11] = queryBusinessLine(hqclcfPersonTransfer1.getNewBusinessLine(), Constant.BUSINESS_LINE);//新业务条线

                if (hqclcfPersonTransfer1.getNewPost() != null && !(hqclcfPersonTransfer1.getNewPost().equals(""))) {
                    post.setPostNo(hqclcfPersonTransfer1.getNewPost());
                    if (hqclcfPostMapper.queryByPostNo(hqclcfPersonTransfer1.getNewPost()) != null) {
                        array[i][12] = hqclcfPostMapper.queryByPostNo(hqclcfPersonTransfer1.getNewPost()).getPostName(); //新岗位
                    }
                }

                if (hqclcfPersonTransfer1.getNewHqPosition() != null && !(hqclcfPersonTransfer1.getNewHqPosition().equals(""))) {
                    business.setPosCode(hqclcfPersonTransfer1.getNewHqPosition());
                    if (hqclcfBusinessMapper.getBusinessByCondition(business).size() > 0) {
                        array[i][13] = hqclcfBusinessMapper.getBusinessByCondition(business).get(0).getPosName();//新职务
                    }
                }
                //array[i][14] = hqclcfPersonTransfer1.getNewHqRank();//新职级
                if (hqclcfPersonTransfer1.getNewHqRank() != null && !(hqclcfPersonTransfer1.getNewHqRank().equals(""))) {
                    rank = new HqclcfRank();
                    rank.setNo(hqclcfPersonTransfer1.getNewHqRank());
                    if (hqclcfRankMapper.getRankByCondition(rank).size() > 0) {
                        array[i][14] = hqclcfRankMapper.getRankByCondition(rank).get(0).getName(); //原职级
                    } else if (hqclcfRankMapper.getRankByCondition(rank).size() == 0) {
                        rank.setNo("");
                        rank.setName(hqclcfPersonTransfer1.getNewHqRank());
                        if (hqclcfRankMapper.getRankByCondition(rank).size() > 0) {
                            array[i][14] = hqclcfRankMapper.getRankByCondition(rank).get(0).getName(); //原职级
                        } else {
                            array[i][14] = "暂无";
                        }
                    }
                }
                array[i][15] = hqclcfPersonTransfer1.getCreateName();//创建人
                array[i][16] = hqclcfPersonTransfer1.getCreateDate();//创建日期
            }
            //导出Util
            ExcelUtil excelUtil = new ExcelUtil();
            excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "人员异动信息", 1);
            baseService.saveLog(hqclcfPersonTransfer, null, HqclcfPersonTransferServiceImpl.class, OperateType.EXPORT, logger);
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String queryBusinessLine(String key, String constant) throws Exception {
        List<SysConfigType> sysConfigTypeList = sysConfigTypeService.getConfigTypesByPSysCode(constant);
        for (SysConfigType keyValue : sysConfigTypeList) {
            if (key.equals(keyValue.getSysValue().toString())) {
                return keyValue.getSysName();
            }
        }
        return "None";
    }

    @Override
    public int dateUtilMethod(HqclcfPersonTransfer hqclcfPersonTransfer) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date transferTime = sdf.parse(hqclcfPersonTransfer.getTransferTime());
        Date now = sdf.parse(sdf.format(new Date()));
        return DateUtil.compare_date(transferTime, now);

    }

    @Override
    public int insertSelective(HqclcfPersonTransfer hqclcfPersonTransfer, MultipartHttpServletRequest request, HttpServletRequest req) throws Exception {
        ResultVo json = new ResultVo();

        String newPostCode = hqclcfPersonTransfer.getNewPost();
        String newDeptCode = hqclcfPersonTransfer.getNewDept();

        HqclcfDept deptCheck = hqclcfDeptMapper.queryDeptByCode(newDeptCode);
        HqclcfPost postCheck = hqclcfPostMapper.queryByPostNo(newPostCode);

        if (deptCheck != null) {
            if (deptCheck.getStatus().equals(Constant.DISABLE)) {
                return 7;
            }
        }

        if (postCheck != null) {
            if (postCheck.getStatus().equals(Constant.DISABLE)) {
                return 6;
            }
        }

        /*检测此员工是否存在未生效异动记录*/
        HqclcfPersonTransfer person = new HqclcfPersonTransfer();
        person.setStatus(Constant.DISABLE);
        person.setEmpNo(hqclcfPersonTransfer.getEmpNo());
        List<HqclcfPersonTransfer> persons = hqclcfPersonTransferMapper.getPersonTransferByCondition(person);
        if (persons.size() > 0) {
            for (HqclcfPersonTransfer p : persons) {
                if (p.getEmpNo() != null) {
                    if (p.getEmpNo().equals(hqclcfPersonTransfer.getEmpNo())) {
                        return 5;
                    }
                }
            }
        }

        /*检测是否跨业务条线*/
        if (!hqclcfPersonTransfer.getPriBusinessLine().equals(hqclcfPersonTransfer.getNewBusinessLine())) {
            String applyInfoFile = "applyInfoFile";
            String transferInfoFile = "transferInfoFile";
            MultipartFile apply = request.getFile(applyInfoFile);
            MultipartFile trans = request.getFile(transferInfoFile);
            if (apply.getOriginalFilename() == null || apply.getOriginalFilename().equals("")) {
                return 4;
            }

            if (trans.getOriginalFilename() == null || trans.getOriginalFilename().equals("")) {
                return 4;
            }

        }

        hqclcfPersonTransfer.setStatus(Constant.DISABLE);//默认状态
        hqclcfPersonTransfer.setCreateDate(DateUtil.parseDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间格式化
        hqclcfPersonTransfer.setCreateName(CommonUtil.getOnlineFullName());//创建人
        int flag = dateUtilMethod(hqclcfPersonTransfer);//判断是否即时生效
        int result = hqclcfPersonTransferMapper.insert(hqclcfPersonTransfer);

        try {

            /*上传*/
            uploadFile(request, hqclcfPersonTransfer);
        } catch (Exception e) {
            logger.info("上传接口异常" + e.getMessage());
            return 2;
        }


        if (result > 0) {
            if (flag == 1) {
                baseService.saveLog(hqclcfPersonTransfer, null, HqclcfPersonTransferServiceImpl.class, OperateType.SAVE, logger);
                return 1;
            } else {
                if (updateEmpTrans(hqclcfPersonTransfer) < 0) json.setInfo("生效异常，请等待下次生效！");
            }
            baseService.saveLog(hqclcfPersonTransfer, null, HqclcfPersonTransferServiceImpl.class, OperateType.SAVE, logger);
            return 1;
        }
        return 0;
    }

    @Override
    public Boolean setEditPersonTransfer(ModelAndView model, Long id) throws Exception {
        HqclcfPersonTransfer hqclcfPersonTransfer = geTransferById(id);
        hqclcfPersonTransfer.setEmpName(hqclcfEmpMapper.queryEmpNameByNo(hqclcfPersonTransfer.getEmpNo()).getEmpName());
        model.addObject("personTransfer", hqclcfPersonTransfer);
        if (hqclcfPersonTransfer.getStatus().equals(Constant.ENABLE)) {
            return false;
        }
        return true;
    }

    @Override
    public void quartzMission() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); //改为共用线程不安全
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        HqclcfPersonTransfer hqclcfPersonTransfer = null;
        List<HqclcfPersonTransfer> hqclcfPersonTransfers = hqclcfPersonTransferMapper.queryAllHqclcfPersonTransfer(hqclcfPersonTransfer);
        for (HqclcfPersonTransfer person : hqclcfPersonTransfers) {
            int result = DateUtil.compare_date(sdf.parse(sdf.format(new Date())), sdf.parse(person.getTransferTime()));
            if (result == -1) continue;  //不满足异动条件
            if (person.getStatus().equals(Constant.ENABLE)) continue; //已生效
            if (updateEmpTrans(person) < 1) logger.info("异动记录失败，编制已满或者超编!");
        }
    }

    @Override
    public int updateEmpTrans(HqclcfPersonTransfer transferPerson) throws Exception {
        try {
            HqclcfEmp emp = hqclcfEmpMapper.queryEmpNameByNo(transferPerson.getEmpNo());

            Long deptId = hqclcfDeptMapper.queryDeptByCode(transferPerson.getNewDept()).getId();

            JSONObject json = queryEmpOrganizat(deptId.toString(), transferPerson.getNewPost()); //编制判断

            if (json.get("deptCode").equals("501") || json.get("deptCode").equals("502")) {
                return 0;
            }

            if (json.get("postCode").equals("501") || json.get("postCode").equals("502")) {
                return 0;
            }

            emp.setDeptNo(transferPerson.getNewDept());//dept
            emp.setPost(transferPerson.getNewPost());//post
            emp.setRank(transferPerson.getNewHqRank());//rank CN
            emp.setPosition(transferPerson.getNewHqPosition());//position
            emp.setBusinessLine(Integer.valueOf(transferPerson.getNewBusinessLine()));//businessLine
            int result = hqclcfEmpMapper.updateById(emp);
            if (result > 0) {
                transferPerson.setStatus(Constant.ENABLE);  // 员工信息管理生效成功
                int rs = hqclcfPersonTransferMapper.updateByPrimaryKeySelective(transferPerson);
                if (rs > 0) { //异动信息生效成功
                    Json jsons = timeAutomatedService.updateForEmployerTransfor(transferPerson,CommonUtil.getOnlineUser());//调用排班接口
                    if(!jsons.isSuccess()){
                        throw new AppException("人员异动后排班生成异常");
                    }
                    HqclcfEmpFile empFile = new HqclcfEmpFile();
                    empFile.setEmpNo(transferPerson.getEmpNo());
                    empFile.setFileType(96528);
                    int dResult = hqclcfEmpFileMapper.deleteFileByBusiness(empFile);
                    empFile.setFileType(96527);
                    int aResult = hqclcfEmpFileMapper.deleteFileByBusiness(empFile);
                    baseService.saveLog(empFile, empFile, HqclcfPersonTransferServiceImpl.class, OperateType.DELETE, logger);
                    return 1;
                }
            }
        } catch (NumberFormatException e) {
            logger.info("人员异动生效异常" + e.getMessage());
            throw new AppException(e.getMessage());
        }
        return 0;
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
        if (id != null && !("").equals(id) && id != 5) {
            HqclcfPersonTransfer hqclcfPersonTransfer = hqclcfPersonTransferMapper.selectByPrimaryKey(id);
            HqclcfEmpFile empFiles = new HqclcfEmpFile();
            empFiles.setFileType(96528);
            empFiles.setBusinessLine(Integer.valueOf(hqclcfPersonTransfer.getNewBusinessLine()));
            empFiles.setEmpNo(hqclcfPersonTransfer.getEmpNo());
            if (hqclcfEmpFileMapper.queryEmpFile(empFiles).size() > 0) {
                HqclcfEmpFile empFileTrans = hqclcfEmpFileMapper.queryEmpFile(empFiles).get(0);
                model.addObject("trans", empFileTrans);//96528
            }
            empFiles.setFileType(96527);
            if (hqclcfEmpFileMapper.queryEmpFile(empFiles).size() > 0) {
                HqclcfEmpFile empFileApply = hqclcfEmpFileMapper.queryEmpFile(empFiles).get(0);
                model.addObject("apply", empFileApply);//96527
            }
        }
        return model;
    }

    @Override
    public JSONObject queryDeptAndPostListByDeptNo(Long id) throws Exception {
        JSONObject json = new JSONObject();
        HqclcfDept dept = new HqclcfDept();
        dept.setId(id);
        List<HqclcfDept> depts = hqclcfDeptMapper.queryAll(dept);
        try {
            if (depts.size() > 0) {
                json.put("code", "200");
                json.put("dept", depts.get(0));
                List<HqclcfPost> postByDept = hqclcfPostMapper.queryAll(new HqclcfPost());
                Long deptCode = depts.get(0).getId();
                List<HqclcfPost> newPost = new ArrayList<>();
                for (int i = 0; i < postByDept.size(); i++) {
                    if (postByDept.get(0).getPriNumber() == 1) {
                        postByDept.remove(i);
                        continue;
                    }
                    if (deptCode.equals(Long.valueOf(postByDept.get(i).getDeptNo()))) {
                        newPost.add(postByDept.get(i));
                    }
                }
                json.put("post", newPost);
                return json;
            } else {
                json.put("code", "500");
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        json.put("code", "500");
        return json;
    }

    @Override
    public JSONObject queryAllPersonTransfer(String data, String q, PageBean pageBean, int rows) throws Exception {
        JSONObject json = new JSONObject();
        String isBl = "排空";
        Grid<HqclcfEmp> grid = new Grid<>();
        List<Integer> loginUserId = baseService.getOnlineUserBl();
        /*if (!("".equals(q)) && q != null) {
            List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq(q, isBl, loginUserId);
            return json;
        }*/
        pageBean.setLimit(rows);
        PageHelper.startPage(pageBean.getPage(),pageBean.getLimit());
        List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq(q, isBl, loginUserId);
        PageInfo<HqclcfEmp> pageInfo = new PageInfo<>(emps);
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
        json.put("rows",grid.getData());
        json.put("total",grid.getCount());
        return json;
    }

    @Override
    public List<HqclcfPost> queryAllOpenPost() throws Exception {
        return hqclcfPostMapper.queryAll(new HqclcfPost());
    }

    @Override
    public List<HqclcfRank> queryAllOpenRank() throws Exception {
        return hqclcfRankMapper.getRankByCondition(new HqclcfRank());
    }

    @Override
    public JSONObject queryAllOpenBusiness(String posCode) throws Exception {
        JSONObject json = new JSONObject();
        HqclcfBusiness business = new HqclcfBusiness();
        List<String> rankLevel = new ArrayList<>();
        List<String> rankCode = new ArrayList<>();
        business.setPosCode(posCode);
        try {
            if (posCode != null && !("".equals(posCode))) {
                List<HqclcfBusiness> businesses = hqclcfBusinessMapper.getBusinessByCondition(business);
                if (businesses.size() > 0) {
                    if(null!=businesses.get(0)){
                        if (null!=businesses.get(0).getRankName() && !"".equals(businesses.get(0).getRankName())) {
                            String[] array = businesses.get(0).getRankName().split(" ");
                            String[] arrayCode = businesses.get(0).getRankCode().split(",");
                            for (int i = 0; i < array.length; i++) {
                                if (array[i] != null && !("".equals(array[i]))) {
                                    rankLevel.add(array[i]);
                                }
                            }
                            for (int i = 0; i < arrayCode.length; i++) {
                                if (arrayCode[i] != null && !("".equals(arrayCode[i]))) {
                                    rankCode.add(arrayCode[i]);
                                }
                            }
                        }
                    }
                }
                json.put("rank", rankLevel);
                json.put("rankCode", rankCode);
                json.put("code", "200");
                json.put("business", businesses.get(0));
                return json;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        json.put("code", 500);
        return json;
    }

    /**
     * 上传文件
     *
     * @param request
     */
    private void uploadFile(MultipartHttpServletRequest request, HqclcfPersonTransfer transfer) throws Exception {
        Integer businessLines = Integer.valueOf(transfer.getNewBusinessLine());
        switch (businessLines) {
            case 3://信贷
                uploadFile1Hdfs(transfer, request, Constant.ZHXD);
                break;
            case 1://总部
                uploadFile1Hdfs(transfer, request, Constant.ZHPHHQ);
                break;
            case 2://消分
                uploadFile1Hdfs(transfer, request, Constant.ZHPHXJ);
                break;
        }
    }

    private void uploadFile1Hdfs(HqclcfPersonTransfer hqclcfPersonTransfer, MultipartHttpServletRequest request, String zhPath) throws Exception {
        List<HqclcfEmpFile> files = new ArrayList<>();
        SysUser onlineUser = CommonUtil.getOnlineUser();
        String applyInfoFile = "applyInfoFile";
        String transferInfoFile = "transferInfoFile";
        HqclcfEmpFile empFileApply = new HqclcfEmpFile();
        HqclcfEmpFile empFileTransfer = new HqclcfEmpFile();
        MultipartFile apply = request.getFile(applyInfoFile);
        if (apply.getOriginalFilename() != null && !apply.getOriginalFilename().equals("")) {
            empFileApply.setBusinessLine(Integer.valueOf(hqclcfPersonTransfer.getNewBusinessLine()));
            empFileApply.setEmpNo(hqclcfPersonTransfer.getEmpNo());
            empFileApply.setCreator(onlineUser.getFullName());
            empFileApply.setFileName(apply.getOriginalFilename());
            empFileApply.setFileType(96527);
            empFileApply.setCreateTime(new Date());
            empFileApply.setFileExtend(apply.getOriginalFilename().substring(apply.getOriginalFilename().lastIndexOf(".")));
            boolean flag = FileUpload.upload(apply, hqclcfPersonTransfer.getEmpNo(), zhPath, empFileApply.getFileType().toString(), empFileApply.getFileName(), logger, urlConfig.getFileUpload());
            if (flag) {
                files.add(empFileApply);
            }
        }
        MultipartFile trans = request.getFile(transferInfoFile);
        if (trans.getOriginalFilename() != null && !trans.getOriginalFilename().equals("")) {
            empFileTransfer.setBusinessLine(Integer.valueOf(hqclcfPersonTransfer.getNewBusinessLine()));
            empFileTransfer.setEmpNo(hqclcfPersonTransfer.getEmpNo());
            empFileTransfer.setCreator(onlineUser.getFullName());
            empFileTransfer.setFileName(trans.getOriginalFilename());
            empFileTransfer.setFileType(96528);
            empFileTransfer.setCreateTime(new Date());
            empFileTransfer.setFileExtend(apply.getOriginalFilename().substring(apply.getOriginalFilename().lastIndexOf(".")));
            boolean flag = FileUpload.upload(trans, hqclcfPersonTransfer.getEmpNo(), zhPath, empFileTransfer.getFileType().toString(), empFileTransfer.getFileName(), logger, urlConfig.getFileUpload());
            if (flag) {
                files.add(empFileTransfer);
            }
        }
        if (files.size() != 0) {
            hqclcfEmpFileMapper.batchInsertFile(files);
        }
    }


    /**
     * 文件下载
     *
     * @param file
     * @param req
     * @param res
     * @throws Exception
     */
    @Override
    public void downloadHdsfFile(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HqclcfEmpFile hqclcfEmpFile = hqclcfEmpFileMapper.queryEmpFileByEmpNoAndFileType(file.getEmpNo(), file.getFileType());
        if (hqclcfEmpFile != null) {
            StringBuffer path = bulidPathByempFile(hqclcfEmpFile);
            path.append(hqclcfEmpFile.getFileName());
            //从hdfs上下载文件
            boolean flag = FileUtil.downFileForHdfs(res, path.toString(), hqclcfEmpFile.getFileName(), urlConfig);
            logger.info("path:" + path + "; flag :" + flag);
        }
    }

    /**
     * 构建文件路径
     *
     * @param file
     * @return
     */
    private StringBuffer bulidPathByempFile(HqclcfEmpFile file) {
        StringBuffer path = new StringBuffer();
        path.append(Constant.HDFS_OPT_PATH);
        path.append(File.separator);
        switch (file.getBusinessLine()) {
            case 1://总部
                path.append(Constant.ZHPHHQ);
                break;
            case 3://信贷
                path.append(Constant.ZHXD);
                break;
            case 2://消分
                path.append(Constant.ZHPHXJ);
                break;
        }
        path.append(File.separator);
        path.append(file.getEmpNo());
        path.append(File.separator);
        path.append(file.getFileType().toString());
        path.append(File.separator);
        return path;
    }

    /**
     * 文件预览
     *
     * @param file
     * @param req
     * @param res
     */
    @Override
    public void previewHdsfFile(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) {
        try {
            HqclcfEmpFile hqclcfEmpFile = hqclcfEmpFileMapper.queryEmpFileByEmpNoAndFileType(file.getEmpNo(), file.getFileType());
            if (hqclcfEmpFile != null) {

                StringBuffer path = bulidPathByempFile(hqclcfEmpFile);
                path.append(hqclcfEmpFile.getFileName());
                logger.info("path: " + path);
                //从hdfs上拉取文件预览
                FileUtil.showFile(res, path.toString(), hqclcfEmpFile.getFileName(), urlConfig);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public JSONObject queryEmpOrganizat(String deptNo, String postNo) throws Exception {
        JSONObject json = new JSONObject();

        HqclcfPost post = null;
        HqclcfDept deptP = null;

        //对应部门
        if (deptNo != null && !deptNo.equals("")) {

            deptP = new HqclcfDept();
            deptP.setId(Long.valueOf(deptNo));

            HqclcfDept dept = hqclcfDeptMapper.queryAll(deptP).get(0);

            int organizatDept = hqclcfEmpMapper.queryEmpOrganizatDept(dept.getDeptCode());

            if (organizatDept == dept.getOrganizat()) {
                json.put("deptMsg", "部门已满编");
                json.put("deptCode", "501");

            } else if (organizatDept < dept.getOrganizat()) {
                json.put("deptCode", "200");
            } else {
                json.put("deptMsg", "部门超编");
                json.put("deptCode", "502");
            }

        }

        //对应岗位
        if (postNo != null && !postNo.equals("")) {

            post = hqclcfPostMapper.queryByPostNo(postNo);
            int organizatPost = hqclcfEmpMapper.queryEmpOrganizatPost(postNo);

            if (organizatPost == post.getOrganizat()) {


                json.put("postMsg", "岗位已满编");
                json.put("postCode", "501");


            } else if (organizatPost < post.getOrganizat()) {
                json.put("postCode", "200");
            } else {
                json.put("postMsg", "岗位超编");
                json.put("postCode", "502");
            }

        }

        return json;
    }


}
