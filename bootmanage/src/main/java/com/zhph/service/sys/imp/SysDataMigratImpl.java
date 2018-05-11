package com.zhph.service.sys.imp;

import com.alibaba.fastjson.JSONObject;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpApvMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpFileMapper;
import com.zhph.mapper.hqclcf.HqclcfPostMapper;
import com.zhph.mapper.sys.SysDataMigratMapper;
import com.zhph.mapper.sys.SysWorkplacesetMapper;
import com.zhph.mapper.sys.SysZhphBankMapper;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.hqclcf.HqclcfPost;
import com.zhph.model.sys.SysZhphBank;
import com.zhph.service.hqclcf.impl.HqclcfPersonTransferServiceImpl;
import com.zhph.service.sys.SysDataMigratService;
import com.zhph.util.CommonUtil;
import com.zhph.util.DateUtil;
import com.zhph.util.RandomUtil;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/23.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDataMigratImpl implements SysDataMigratService {
    @Resource
    private HqclcfDeptMapper hqclcfDeptMapper;
    @Resource
    private SysDataMigratMapper sysDataMigratMapper;
    @Resource
    private HqclcfPostMapper hqclcfPostMapper;
    @Resource
    private SysWorkplacesetMapper sysWorkplacesetMapper;
    @Resource
    private SysZhphBankMapper sysZhphBankMapper;

    @Resource
    private HqclcfEmpApvMapper hqclcfEmpApvMapper;
    @Resource
    private HqclcfEmpFileMapper hqclcfEmpFileMapper;

    public static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(SysDataMigratImpl.class);

    @Override
    public void generateDept() throws Exception {
  /*      generateOldDeptData2NewDept();*/
      /*  generatePidCode2Pid();*/
       /* generatDeptCodeByUUId();*/

     /*  shengchengxjdept();*/

     /*  shengchengxjpid();*/

    /* quertXdDqand2db();*/

    /*updatePidandsataus();*/

  /*  genjuquyushengchengxiajifengongsi();*/

    /*    generatingCreditDepartmentAccordingToBranch();*/

        shengchengxddetuandui();

    }

    @Override
    public void generatePost() throws Exception {
//            shengchengOldPost2Db();
       /* updatePostPid();*/

      /* generXfPost();*/

     /* updateXfPostPid();*/

    }
/*    private String areaCode;//地区编码
    private String area;//地区
    private String province;//所属省份
    private String businessLine;//业务条线
    private String status;//状态
    private String remark;//备注
    private Integer cityLevel;//城市级别
    private String createName ;  //创建人 CREATE_NAME
    private Date createDate ;  //创建时间 CREATE_DATE*/

    @Override
    public void generateWork() throws Exception {
        List<Map<String, String>> maps = sysDataMigratMapper.queryBank();
        for (Map<String, String> map : maps) {
            SysZhphBank bank = new SysZhphBank();
            String code = map.get("BANK_CODE");
            String bank_name = map.get("BANK_NAME");
            String bank_full_name = null != map.get("BANK_FULL_NAME") ? map.get("BANK_FULL_NAME") : "";
            bank.setStatus("1");
            bank.setCreateName(CommonUtil.getOnlineFullName());
            bank.setBankFullName(bank_full_name);
            bank.setBankCode(code);
            bank.setBankName(bank_name);
            bank.setCreateTime(DateUtil.getCurrentDate());
            sysZhphBankMapper.addByObj(bank);
        }


      /*  List<Map<String, String>> maps = sysDataMigratMapper.queryHqWork();
        for (Map<String,String> map:maps) {
            SysWorkplaceset workplaceset=new SysWorkplaceset();
            String node_no = map.get("NODE_NO");
            String node_name = map.get("NODE_NAME");
            workplaceset.setStatus("1");
            workplaceset.setBusinessLine("1");
            workplaceset.setCreateDate(new Date());
            workplaceset.setCreateName(CommonUtil.getOnlineFullName());
            workplaceset.setArea(node_name);
            workplaceset.setAreaCode(node_no);
            workplaceset.setCityLevel(null);
            workplaceset.setProvince(null);
            sysWorkplacesetMapper.addByObj(workplaceset);
        }*/

/*        List<Map<String, String>> maps = sysDataMigratMapper.queryXdWork();
        for (Map<String,String> map:maps) {
            SysWorkplaceset place=new SysWorkplaceset();
            String no = map.get("NO");
            String name = map.get("NAME");
            String status = map.get("STATUS");
            String province = map.get("PROVINCE");
            String categorynum = null!=map.get("CATEGORYNUM")?map.get("CATEGORYNUM"):"";
            place.setAreaCode(no);
            place.setArea(name);
            place.setProvince(province);
            place.setStatus("1");
            switch (status){
                case "2":
                    place.setStatus("0");
                    break;
                case "1":
                    place.setStatus("1");
                    break;
            }
            switch (categorynum){
                case "A":
                    place.setCityLevel(1);
                    break;
            }
            place.setCreateName(CommonUtil.getOnlineFullName());
            place.setCreateDate(new Date());
            place.setBusinessLine("3");
            sysWorkplacesetMapper.addByObj(place);
        }*/
    }

    /**
     * 生成消分的员工
     *
     * @throws Exception
     */
    @Override
    public void generateXfEmp() throws Exception {
        List<Map<String, String>> maps = sysDataMigratMapper.queryRankByOldAndNew2Hr();
        Map<String, String> needRankMap = new HashMap<>();
        for (Map<String, String> rankMap : maps) {
            String oldRank = null != rankMap.get("OLD_RANK_NO") ? rankMap.get("OLD_RANK_NO") : "";
            String realRank = null != rankMap.get("REAL_RANK_NO") ? rankMap.get("REAL_RANK_NO") : "";
            needRankMap.put(oldRank, realRank);
        }
        List<Map<String, Object>> list = sysDataMigratMapper.queryXfAllEmp();
        for (Map<String, Object> map : list) {
            HqclcfEmp emp = new HqclcfEmp();
            String empNo = null != map.get("EMP_NO") ? (String) map.get("EMP_NO") : "";
            emp.setEmpNo(empNo);
            String empName = null != map.get("EMP_NAME") ? (String) map.get("EMP_NAME") : "";
            emp.setEmpName(empName);
            String idcardName = null != map.get("REAL_NAME") ? (String) map.get("REAL_NAME") : "";
            emp.setIdcardName(idcardName);
            Object sexObj = null != map.get("SEX") ? map.get("SEX") : "";
            Integer sex = Integer.valueOf(sexObj.toString());
            emp.setSex(sex);
            Object marriageObj = null != map.get("SEX") ? map.get("SEX") : "";
            Integer marriage = Integer.valueOf(marriageObj.toString());
            switch (marriage) {
                case 1:
                    emp.setMarriage(2);
                    break;
                case 2:
                    emp.setMarriage(1);
                    break;
            }
            String nativePlace = null != map.get("NATIVE_PLACE") ? (String) map.get("NATIVE_PLACE") : "";
            emp.setNativeType(null);
            emp.setNativePlace(nativePlace);
            emp.setHjxjdz(nativePlace);
            String addr = null != map.get("ADDR") ? (String) map.get("ADDR") : "";
            emp.setAddr(addr);
            emp.setXjxxdz(addr);
            String idType = null != map.get("ID_TYPE") ? (String) map.get("ID_TYPE") : "";
            emp.setIdType(Integer.valueOf(idType));
            String idCard = null != map.get("ID_CARD") ? (String) map.get("ID_CARD") : "";
            emp.setIdCard(idCard);
            String mobilePhone = null != map.get("MOBILE_PHONE") ? (String) map.get("MOBILE_PHONE") : "";
            emp.setMobilePhone(mobilePhone);
            String urgencyPhone = null != map.get("FAMILY_PHONE") ? (String) map.get("FAMILY_PHONE") : "";
            emp.setUrgencyPhone(urgencyPhone);
            emp.setUrgency(null);
            emp.setUrgencyRelation(null);
            String email = null != map.get("EAMIL") ? (String) map.get("EAMIL") : "";
            emp.setEmail(email);
            emp.setCompanyMailbox(null);
            emp.setNation(null);
            String empSubject = null != map.get("EMP_SUBJECT") ? (String) map.get("EMP_SUBJECT") : "";
            switch (empSubject) {
                case "HT":
                    emp.setEmpSubject(2);
                    break;
                case "ZH":
                    emp.setEmpSubject(1);
                    break;
            }
            String eduObj = null != map.get("EDU") ? (String) map.get("EDU") : "";
            switch (eduObj) {
                case "1":
                    emp.setEdu(1);
                    break;
                case "2":
                    emp.setEdu(2);
                    break;
                case "3":
                    emp.setEdu(4);
                    break;
                case "4":
                    emp.setEdu(5);
                    break;
                case "5":
                    emp.setEdu(6);
                    break;
                case "6":
                    emp.setEdu(7);
                    break;
                case "10":
                    emp.setEdu(8);
                    break;
            }
            String schoolGraduation = null != map.get("SCHOOL_GRADUATION") ? (String) map.get("SCHOOL_GRADUATION") : "";
            emp.setSchoolGraduation(schoolGraduation);
            String specialty = null != map.get("SPECIALTY") ? (String) map.get("SPECIALTY") : "";
            emp.setSpecialty(specialty);
            String eduType = null != map.get("EDUCATION_WAY") ? (String) map.get("EDUCATION_WAY") : "";
            emp.setEduType(Integer.valueOf(eduType));
            emp.setLxEnterDate(null);
            Object enterDateObj = null != map.get("ENTER_DATE") ? map.get("ENTER_DATE") : null;
            if (enterDateObj != null) {
                Date enterDate = DateUtil.parseStringToDate(enterDateObj.toString(), "yyyy-MM-dd");
                emp.setEnterDate(enterDate);
            }
            Object tfDateObj = null != map.get("TF_DATE") ? map.get("TF_DATE") : null;
            if (tfDateObj != null) {
                emp.setTfDate(DateUtil.parseStringToDate(tfDateObj.toString(), "yyyy-MM-dd"));
            }
            Object fulltimeBeginDateObj = null != map.get("FULLTIME_BEGIN_DATE") ? map.get("FULLTIME_BEGIN_DATE") : null;
            if (fulltimeBeginDateObj != null) {
                emp.setFulltimeBeginDate(DateUtil.parseStringToDate(fulltimeBeginDateObj.toString(), "yyyy-MM-dd"));
            }
            Object fulltimeEndDateObj = null != map.get("FULLTIME_END_DATE") ? map.get("FULLTIME_END_DATE") : null;
            if (fulltimeEndDateObj != null) {
                emp.setFulltimeEndDate(DateUtil.parseStringToDate(fulltimeEndDateObj.toString(), "yyyy-MM-dd"));
            }
            String workOrgNo = null != map.get("WORK_ORG_NO") ? (String) map.get("WORK_ORG_NO") : "";
            emp.setWorkOrgNo(workOrgNo);
            emp.setSocialSecurityOrgNo(workOrgNo);
            String taxCode = null != map.get("TAX_NO") ? (String) map.get("TAX_NO") : "";
            emp.setTaxCode(taxCode);
            String wagesNo = null != map.get("IS_AGREEMENT_SALARY") ? (String) map.get("IS_AGREEMENT_SALARY") : "";
            switch (wagesNo) {
                case "1":
                    emp.setWagesNo("0");
                    break;
                case "2":
                    emp.setWagesNo("1");
                    break;
            }
            Object wagesNum = null != map.get("AGREEMENT_SALARY") ? map.get("AGREEMENT_SALARY") : "";
            emp.setWagesNum(wagesNum.toString());
            String referees = null != map.get("RECOMMENDED_NAME") ? (String) map.get("RECOMMENDED_NAME") : "";
            emp.setReferees(referees);
            String refereesNo = null != map.get("RECOMMENDED_NO") ? (String) map.get("RECOMMENDED_NO") : "";
            emp.setRefereesNo(refereesNo);
            String deptNo = null != map.get("DEPT_CODE") ? (String) map.get("DEPT_CODE") : "";
            emp.setDeptNo(deptNo);
            String gwCode = null != map.get("POSITION") ? (String) map.get("POSITION") : "";
            emp.setPost(gwCode);
            String zwCode = null != map.get("ZW_CODE") ? (String) map.get("ZW_CODE") : "";
            emp.setPosition(zwCode);
            String rankNo = null != map.get("RANK_NO") ? (String) map.get("RANK_NO") : "";
            String realRankNo = null != needRankMap.get(rankNo) ? needRankMap.get(rankNo) : "";
            emp.setRank(realRankNo);
            String pfBankCode = null != map.get("PF_BANK_CODE") ? (String) map.get("PF_BANK_CODE") : "";
            emp.setPfBankCode(pfBankCode);
            String pfBankNo = null != map.get("PF_BANK_NO") ? (String) map.get("PF_BANK_NO") : "";
            emp.setPfBankNo(pfBankNo);
            String openBankOrg = null != map.get("PF_BANK_SUBBRANCH") ? (String) map.get("PF_BANK_SUBBRANCH") : "";
            emp.setOpenBankOrg(openBankOrg);
            emp.setEduType(1);
            emp.setEmpType(1);//消分里面没字段我自己定义的值
            String comments = null != map.get("COMMENTS") ? (String) map.get("COMMENTS") : "";
            emp.setComments(comments);
            String status = null != map.get("STATUS") ? (String) map.get("STATUS") : "";
            if (!"".equals(status)) {
                emp.setStatus(Integer.valueOf(status));
            }
            String statusAppObj = null != map.get("APPROVAL_STATUS") ? (String) map.get("APPROVAL_STATUS") : "";
            /*审批状态[1:未审批,2:通过,3拒绝,4撤销  0,未提交]*/
            switch (statusAppObj) {
                case "1":
                    emp.setStatusApp(4);
                    break;
                case "2":
                    emp.setResultsApp("1");
                    emp.setStatusApp(1);
                    break;
                case "3":
                    emp.setResultsApp("0");
                    emp.setStatusApp(2);
                    break;
                case "4":
                    emp.setStatusApp(3);
                    break;
            }
            String reasonsApp = null != map.get("APPROVAL_REASON") ? (String) map.get("APPROVAL_REASON") : "";
            emp.setReasonsApp(reasonsApp);
            emp.setBusinessLine(2);
            String leaveDate = null != map.get("LEAVE_DATE") ? (String) map.get("LEAVE_DATE") : "";
            if ("".equals(leaveDate) && leaveDate != null) {
                emp.setLeaveDate(DateUtil.parseStringToDate(leaveDate, "yyyy-MM-dd"));
            }
            String badCredit = null != map.get("BADCREDIT") ? (String) map.get("BADCREDIT") : "";
            emp.setBadCredit(badCredit);
            emp.setCreatetime(new Date());
            emp.setCreator(CommonUtil.getOnlineFullName());
            hqclcfEmpApvMapper.insert(emp);
        }

    }

    /**
     * 生成消分的文件信息
     *
     * @throws Exception
     */
    @Override
    public void syncAttachmentnInfo() throws Exception {
        List<Map<String, String>> maps = sysDataMigratMapper.queryXfFileInfo();
        for (Map<String, String> map : maps) {
            HqclcfEmpFile file = new HqclcfEmpFile();
            file.setFileTypeFlag("oldSalary");
            file.setCreator(CommonUtil.getOnlineFullName());
            file.setCreateTime(new Date());
            String emp_no = map.get("EMP_NO");
            file.setEmpNo(emp_no);
            String file_name = map.get("FILE_NAME");
            file.setFileName(file_name);
            String file_extend = map.get("FILE_EXTEND");
            file.setFileExtend(file_extend);
            String file_type = map.get("FILE_TYPE");
            file.setBusinessLine(2);
            switch (file_type) {
                case "1"://员工照片
                    file.setFileType(704);
                    break;
                case "2"://面试申请表
                    file.setFileType(1);
                    break;
                case "3"://身份证正反面
                    file.setFileType(2);
                    break;
                case "4"://zh离职证明
                    file.setFileType(3);
                    break;
                case "5"://ZH全日制合同信息
                    file.setFileType(4);
                    break;
                case "6"://ZH非全日制合同信息
                    file.setFileType(4);
                    break;
                case "7"://体检报告
                    file.setFileType(5);
                    break;
                case "8"://学历证复印件
                    file.setFileType(6);
                    break;
                case "9"://保密协议
                    file.setFileType(7);
                    break;
                case "10"://征信报告
                    file.setFileType(8);
                    break;
                case "11"://公司制度确认书
                    file.setFileType(9);
                    break;
                case "12"://入职确认表
                    file.setFileType(10);
                    break;
                case "13":
                    file.setFileType(15);
                    break;
                case "14"://十大禁令
                    file.setFileType(11);
                    break;
                case "15"://销售人员行为规范
                    file.setFileType(12);
                    break;
                case "16"://录用条件及岗位职责描述
                    file.setFileType(13);
                    break;
                case "17":
                    file.setFileType(14);
                    break;
                case "EMP_HT_4"://ht离职证明
                    file.setFileType(3);
                    break;
                case "EMP_HT_5"://全日制合同信息
                    file.setFileType(4);
                    break;
                case "EMP_HT_6"://非全日制合同信息
                    file.setFileType(4);
                    break;
                case "EMP_HT_7"://非全日制合同信息
                    file.setFileType(5);
                    break;
                case "EMP_HT_8":
                    file.setFileType(6);
                    break;
                case "EMP_HT_9"://保密协议保密协议
                    file.setFileType(7);
                    break;
                case "EMP_HT_10"://保密协议保密协议
                    file.setFileType(8);
                    break;
                case "EMP_HT_11"://保密协议保密协议
                    file.setFileType(9);
                    break;
                case "EMP_HT_12"://入职确认表
                    file.setFileType(10);
                    break;
                case "EMP_HT_13"://其他附件
                    file.setFileType(15);
                    break;
                case "EMP_HT_14"://十大禁令
                    file.setFileType(11);
                    break;
                case "EMP_HT_15"://销售人员行为规范
                    file.setFileType(12);
                    break;
                case "EMP_HT_16":
                    file.setFileType(13);
                    break;
                case "EMP_HT_17"://目标责任书
                    file.setFileType(14);
                    break;
                case "xjleave01":
                    file.setFileType(706);
                    break;
                case "xjleave02":
                    file.setFileType(707);
                    break;
                case "xjleave03":
                    file.setFileType(708);
                    break;
                case "xjleave04":
                    file.setFileType(709);
                    break;
                case "HT_4":
                    file.setFileType(3);
                    break;
                case "HT_5":
                    file.setFileType(4);
                    break;
                case "HT_6":
                    file.setFileType(4);
                    break;
                case "HT_7":
                    file.setFileType(5);
                    break;
                case "HT_8":
                    file.setFileType(6);
                    break;
                case "HT_9":
                    file.setFileType(7);
                    break;
                case "HT_10":
                    file.setFileType(8);
                    break;
                case "HT_11":
                    file.setFileType(9);
                    break;
                case "HT_12":
                    file.setFileType(10);
                    break;
                case "HT_13":
                    file.setFileType(15);
                    break;
                case "HT_14":
                    file.setFileType(11);
                    break;
                case "HT_15":
                    file.setFileType(12);
                    break;
                case "HT_16":
                    file.setFileType(13);
                    break;
                case "HT_17":
                    file.setFileType(14);
                    break;
            }
            hqclcfEmpFileMapper.insertempFile(file);
        }
    }

    /**
     * 生成总部的员工信息
     *
     * @throws Exception
     */
    @Override
    public void generateHqEmp() throws Exception {
        List<Map<String, Object>> list = sysDataMigratMapper.queryHqEmp();
        for (Map<String, Object> map : list) {
            String empNo = null != map.get("EMP_NO") ? (String) map.get("EMP_NO") : "";
            String empName = null != map.get("EMP_NAME") ? (String) map.get("EMP_NAME") : "";
            String sex = null != map.get("SEX") ? (String) map.get("SEX") : "";
            String marriage = null != map.get("MARRIAGE") ? (String) map.get("MARRIAGE") : "";
            String nativeType = null != map.get("NATIVE_TYPE") ? (String) map.get("NATIVE_TYPE") : "";
            String idType = null != map.get("ID_TYPE") ? (String) map.get("ID_TYPE") : "";
            String idCard = null != map.get("ID_CARD") ? (String) map.get("ID_CARD") : "";
            String mobilePhone = null != map.get("MOBILE_PHONE") ? (String) map.get("MOBILE_PHONE") : "";
            String nation = null != map.get("NATION") ? (String) map.get("NATION") : "";
            String edu = null != map.get("EDU") ? (String) map.get("EDU") : "";
            String schoolGraduation = null != map.get("SCHOOL_GRADUATION") ? (String) map.get("SCHOOL_GRADUATION") : "";
            String specialty = null != map.get("SPECIALTY") ? (String) map.get("SPECIALTY") : "";
            String eduType = null != map.get("EDU_TYPE") ? (String) map.get("EDU_TYPE") : "";
            String lxEnterDate = null != map.get("LX_ENTER_DATE") ? (String) map.get("LX_ENTER_DATE") : "";
            String enterDate = null != map.get("ENTER_DATE") ? (String) map.get("ENTER_DATE") : "";
            String tfDate = null != map.get("TF_DATE") ? (String) map.get("TF_DATE") : "";
            String workOrgNo = null != map.get("WORK_ORG_NO") ? (String) map.get("WORK_ORG_NO") : "";
            String cityLevel = null != map.get("CITY_LEVEL") ? (String) map.get("CITY_LEVEL") : "";
            String deptCode = null != map.get("DEPT_CODE") ? (String) map.get("DEPT_CODE") : "";
            String pfBankCode = null != map.get("PF_BANK_CODE") ? (String) map.get("PF_BANK_CODE") : "";
            String pfBankNo = null != map.get("PF_BANK_NO") ? (String) map.get("PF_BANK_NO") : "";
            String openBankOrg = null != map.get("OPEN_BANK_ORG") ? (String) map.get("OPEN_BANK_ORG") : "";
            String pfBankSubbranch = null != map.get("PF_BANK_SUBBRANCH") ? (String) map.get("PF_BANK_SUBBRANCH") : "";
            String empType = null != map.get("EMP_TYPE") ? (String) map.get("EMP_TYPE") : "";
            String comments = null != map.get("COMMENTS") ? (String) map.get("COMMENTS") : "";
            Object statusObj = null != map.get("STATUS") ? map.get("STATUS") : "";
            String leaveDate = null != map.get("LEAVE_DATE") ? (String) map.get("LEAVE_DATE") : "";
            String postNo = null != map.get("POST_NO") ? (String) map.get("POST_NO") : "";
            Object approvalStatusObj = null != map.get("APPROVAL_STATUS") ? map.get("APPROVAL_STATUS") : "";
            String approvalMark = null != map.get("APPROVAL_MARK") ? (String) map.get("APPROVAL_MARK") : "";
            String rankNo = null != map.get("RANK_NO") ? (String) map.get("RANK_NO") : "";
            String badcredit = null != map.get("BADCREDIT") ? (String) map.get("BADCREDIT") : "";
            String hqposition = null != map.get("HQ_POSITION") ? (String) map.get("HQ_POSITION") : "";
            HqclcfEmp emp = new HqclcfEmp();
            switch (rankNo) {
                case "ZH01"://初级
                    emp.setRank("ZH3C04");
                    break;
                case "ZH02"://中级
                    emp.setRank("ZH73B6");
                    break;
                case "ZH03"://高级
                    emp.setRank("ZHE33A");
                    break;
                case "ZH04"://资深
                    emp.setRank("ZH617B");
                    break;
                case "ZH05"://见习
                    emp.setRank("ZHEE67");
                    break;
            }
            emp.setEmpNo(empNo);
            emp.setEmpName(empName);
            emp.setIdcardName(null);
            emp.setSex(Integer.valueOf(sex));
            emp.setMarriage(!"".equals(marriage) ? Integer.valueOf(marriage) : null);
            switch (nativeType) {
                case "1":
                    emp.setNativeType(2);
                    break;
                case "2":
                    emp.setNativeType(1);
                    break;
            }
            emp.setIdType(1);
            emp.setIdCard(idCard);
            emp.setMobilePhone(mobilePhone);
            emp.setNation(Integer.valueOf(nation));
            emp.setEdu(!"".equals(edu) ? Integer.valueOf(edu) : null);
            emp.setSchoolGraduation(schoolGraduation);
            emp.setSpecialty(specialty);
            switch (eduType) {
                case "1":
                    emp.setEduType(1);
                    break;
                case "2":
                    emp.setEduType(3);
                    break;
                case "3":
                    emp.setEduType(4);
                    break;
                case "4":
                    emp.setEduType(5);
                    break;
                case "5":
                    emp.setEduType(6);
                    break;
                case "6":
                    emp.setEduType(7);
                    break;
                case "10":
                    emp.setEduType(8);
                    break;
            }
            emp.setLxEnterDate(DateUtil.parseStringToDate(lxEnterDate, "yyyy-MM-dd"));
            emp.setEnterDate(DateUtil.parseStringToDate(enterDate, "yyyy-MM-dd"));
            emp.setTfDate(DateUtil.parseStringToDate(tfDate, "yyyy-MM-dd"));
            emp.setFulltimeBeginDate(null);
            emp.setFulltimeEndDate(null);
            emp.setWorkOrgNo(workOrgNo);
            emp.setSocialSecurityOrgNo(schoolGraduation);
            emp.setTaxCode(null);
            emp.setWagesNo("0");
            emp.setWagesNum(null);
            emp.setReferees(null);
            emp.setRefereesNo(null);
            emp.setCityLevel(Integer.valueOf(cityLevel));
            emp.setDeptNo(deptCode);
            emp.setPost(postNo);
            emp.setPosition(hqposition);
            emp.setPfBankCode(pfBankCode);
            emp.setPfBankNo(pfBankNo);
            emp.setOpenBankOrg(openBankOrg);
            emp.setPfBankSubbranch(pfBankSubbranch);
            switch (eduType) {
                case "1":
                    emp.setEmpType(1);
                    break;
                case "2":
                    emp.setEmpType(2);
                    break;
                case "3":
                    emp.setEmpType(2);
                    break;
                case "4":
                    emp.setEduType(null);
                    break;
            }
            emp.setComments(comments);
            String status = statusObj.toString();
            switch (status) {
                case "701":
                    emp.setStatus(1);
                    break;
                case "702":
                    emp.setStatus(2);
                    break;
            }
            String approvalStatus = approvalStatusObj.toString();
            switch (approvalStatus) {
                case "601"://未审批
                    emp.setStatusApp(4);
                    break;
                case "602"://通过
                    emp.setStatusApp(1);
                    break;
                case "603"://拒绝
                    emp.setStatusApp(2);
                    break;
                case "604"://撤销
                    emp.setStatusApp(3);
                    break;
            }
            emp.setResultsApp("1");
            emp.setBusinessLine(1);
            emp.setLeaveDate(DateUtil.parseStringToDate(leaveDate, "yyyy-MM-dd"));
            emp.setBadCredit(badcredit);
            emp.setCreator(CommonUtil.getOnlineFullName());
            emp.setCreatetime(new Date());
            hqclcfEmpApvMapper.insert(emp);
        }
    }


    private void updateXfPostPid() throws Exception {
        List<Map<String, String>> maps = sysDataMigratMapper.queryOldPostNo2newId();
        Map<String, Long> codeMap = new HashMap<>();
        for (Map<String, String> map : maps) {
            Object id = map.get("ID");
            String code = map.get("CODE");
            codeMap.put(code, Long.valueOf(id.toString()));
        }
        List<Map<String, String>> list = sysDataMigratMapper.queryOldPostNo2newId();
        for (Map<String, String> map : list) {
            HqclcfPost psot = new HqclcfPost();
            Object id = map.get("ID");
            String superior_jobno = map.get("P_CODE");
            Long aLong = Long.valueOf(id.toString());
            Long pid = null != codeMap.get(superior_jobno) ? codeMap.get(superior_jobno) : null;
            psot.setPriNumber(aLong);
            psot.setPostPid(pid);
            sysDataMigratMapper.updatePid(psot);
        }
    }

    /**
     * 生成消分的岗位
     *
     * @throws Exception
     */
    @Override
    public void generatexfPost() throws Exception {
       /* buildXfPostNo();*//*从老系统把岗位信息构建过来*/
        updateXfPostPid();
    }

    /**
     * 处理导入的数据
     *
     * @param file
     * @throws Exception
     */
    @Override
    public void importExl(MultipartFile file) throws Exception {
     /*   处理信贷初始的原始岗位数据(file);*/
    }

    @Override
    public void generateXdPost() throws Exception {
        生成信贷的岗位();
    }

    /**
     * 考勤排班生成
     */
    @Override
    public void generateXjAutoMeted() throws Exception {
        logger.info("生成考勤排班中..");
        sysDataMigratMapper.generateXjAutoMeted();
        logger.info("生成考勤结束..");
    }

    /**
     * 同步考勤排班生成
     */
    @Override
    public JSONObject generateXjAutoMetedWithRef(String gzym) throws Exception {
        JSONObject json = new JSONObject();
        logger.info("同步考勤排班附表中..");

        char[] gz = gzym.trim().toCharArray();

        if (gz.length != 7) {
            json.put("msg", "格式错误");
            json.put("code", "500");
            return json;
        } else if (!"-".equals(String.valueOf(gz[4]))) {
            json.put("msg", "格式错误");
            json.put("code", "500");
            return json;
        }

        try {

            String pre = gzym.substring(0, 4);
            Integer pres = Integer.parseInt(pre);

            String suff = gzym.substring(5, 7);
            Integer suffs = Integer.parseInt(suff);

        } catch (Exception e) {
            json.put("msg", "日期错误");
            json.put("code", "501");
            return json;
        }

        int res = sysDataMigratMapper.generateXjAutoMetedWithRef(gzym);
        if(res <= 0){
            json.put("msg", "同步失败！");
            json.put("code", "501");
            return json;
        }
        logger.info("同步考勤附表结束..");
        json.put("code","200");

        return json;
    }


    /**
     * 取消同步考勤排班附表
     */
    @Override
    public JSONObject QxGenerateXjWithRef(String gzym) throws Exception {
        JSONObject json = new JSONObject();
        logger.info("取消同步考勤排班附表中..");

        char[] gz = gzym.trim().toCharArray();

        if (gz.length != 7) {
            json.put("msg", "格式错误");
            json.put("code", "501");
            return json;
        } else if (!"-".equals(String.valueOf(gz[4]))) {
            json.put("msg", "格式错误");
            json.put("code", "501");
            return json;
        }

        try {

            String pre = gzym.substring(0, 4);
            Integer pres = Integer.parseInt(pre);

            String suff = gzym.substring(5, 7);
            Integer suffs = Integer.parseInt(suff);

        } catch (Exception e) {
            json.put("msg", "日期错误");
            json.put("code", "502");
            return json;
        }

        int res = sysDataMigratMapper.deleteHqclcfEmpBakByGzym(gzym);
        if(res <= 0){
            json.put("msg", "取消同步失败，请核对工资年月是否正确或者存在！");
            json.put("code", "501");
            return json;
        }
        logger.info("取消同步考勤附表结束..");
        json.put("code","200");

        return json;
    }

    /**
     * 调休天数生成
     */
    @Override
    public void generateXjPaidLeave() throws Exception {
        logger.info("生成考调休天数中..");
        sysDataMigratMapper.generateXjPaidLeave();
        logger.info("生成调休天数结束..");
    }

    /**
     * 调休天数生成
     */
    @Override
    public void generateXjNodeDays() throws Exception {
        logger.info("生成请休假天数附表中..");
        sysDataMigratMapper.generateXjNodeDays();
        logger.info("生成请休假附表结束..");
    }

    /**
     * 调休天数正表生成
     */
    @Override
    public void generateXjVacatemanage() throws Exception {
        logger.info("生成请休假天数管理中..");
        sysDataMigratMapper.generateXjVacatemanage();
        logger.info("生成请休假管理结束..");
    }

    /**
     * 调休天数正表生成
     */
    @Override
    public void generateXjAbnormity() throws Exception {
        logger.info("生成打卡异常中..");
        sysDataMigratMapper.generateXjAbnormity();
        logger.info("生成打卡异常结束..");
    }

    /**
     * 生成考勤统计
     */
    @Override
    public void generateXjTimestatistical() throws Exception {
        logger.info("生成考勤统计中..");
        sysDataMigratMapper.generateXjTimestatistical();
        logger.info("生成考勤统计结束..");
    }


    /**
     * 生成消分员工状态
     */
    @Override
    public void generateXjEmpStatus() throws Exception {
        logger.info("生成消分员工状态中..");
        sysDataMigratMapper.generateXjEmpStatus();
        logger.info("生成消分员工状态结束..");
    }


    private void 生成信贷的岗位() throws Exception {

    }

    /**
     * 处理信贷的原始数据
     *
     * @param file
     * @throws Exception
     */
/*    private void 处理信贷初始的原始岗位数据(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        int trLength = sheet.getLastRowNum();
        HSSFRow row = sheet.getRow(0);
        short tdLength = row.getLastCellNum();

        for (int i = 1; i < trLength + 1; i++) {
            SysXdOldPost post=new SysXdOldPost();
            HSSFRow tr = sheet.getRow(i);
            for (int j = 0; j < tdLength; j++) {
                HSSFCell cell = tr.getCell(j);
                String val="";
                if (cell != null) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    val=cell.getStringCellValue().trim();
                }
              switch (j){
                  case 0:
                      post.setWorkPlace(val);
                      break;
                  case 1:
                      post.setTeam(val);
                      break;
                  case 2:
                      post.setRankName(val);
                      break;
                  case 3:
                      post.setPostNo(val);
                      break;
                  case 4:
                      post.setPostName(val);
                      break;
                  case 5:
                      post.setpPostNo(val);
                      break;
                  case 6:
                      post.setpPostName(val);
                      break;
              }
            }
            sysDataMigratMapper.addXdPostList(post);
        }

    }*/
    private void buildXfPostNo() throws Exception {
        List<Map<String, String>> list = sysDataMigratMapper.queryXfPost();
        for (Map<String, String> map : list) {
            HqclcfPost post = new HqclcfPost();
            Object dept_id = map.get("DEPT_ID");
            String rank_type = map.get("RANK_TYPE");
            String job_no = map.get("JOB_NO");
            String job = map.get("JOB");
            String status = map.get("STATUS");
            String organization = map.get("ORGANIZATION");

            post.setDeptNo(dept_id.toString());
            post.setBusinessLine(2);
            post.setPostNo(job_no);
            post.setPostName(job);
            post.setCreateBy(CommonUtil.getOnlineFullName());
            post.setCreateTime(DateUtil.getCurrentDate());
            post.setOrganizat(Integer.valueOf(organization));
            switch (status) {
                case "1":
                    post.setStatus("1");
                    break;
                case "2":
                    post.setStatus("0");
                    break;
            }
            switch (rank_type) {
                case "1":
                    post.setRankNo("ZW9C7A9");
                    break;
                case "2":
                    post.setRankNo("ZW906E2");
                    break;
                case "3":
                    post.setRankNo("ZW13FED");
                    break;
                case "4":
                    post.setRankNo("ZW7A25B");
                    break;
                case "5":
                    post.setRankNo("ZW72510");
                    break;
                case "6":
                    post.setRankNo("ZW616F5");
                    break;
            }
            hqclcfPostMapper.addPost(post);
        }
    }

    private void updatePostPid() throws Exception {
        List<Map<BigDecimal, BigDecimal>> list = sysDataMigratMapper.queryPostIdWithOldId();
        Map<Long, Long> maps = new HashMap<>();
        for (Map<BigDecimal, BigDecimal> map : list) {
            BigDecimal id = map.get("ID");
            BigDecimal old_id = map.get("OLD_ID");
            maps.put(old_id.longValue(), id.longValue());
        }

        List<HqclcfPost> hqclcfPosts = sysDataMigratMapper.queryAllPost();
        for (HqclcfPost post : hqclcfPosts) {
            Long pid = post.getPostPid();
            Long newPid = null != maps.get(pid) ? maps.get(pid) : null;
            post.setPostPid(newPid);
            sysDataMigratMapper.updatePid(post);
        }

    }

    private void shengchengOldPost2Db() throws Exception {
        List<Map<String, Object>> list = sysDataMigratMapper.queryOldPost();
        for (Map<String, Object> map : list) {
            HqclcfPost post = new HqclcfPost();
            String post_no = (String) map.get("POST_NO");
            BigDecimal deptId = (BigDecimal) map.get("ID");
            String supHqpost = (String) map.get("SUP_HQPOST");
            String post_name = (String) map.get("POST_NAME");
            String hq_position = (String) map.get("HQ_POSITION");
            post.setBusinessLine(1);
            post.setStatus("1");
            post.setOrganizat(null);
            post.setCreateBy(CommonUtil.getOnlineFullName());
            post.setDeptNo(deptId.toString());
            post.setCreateTime(DateUtil.getCurrentDate());
            post.setPostName(post_name);
            post.setRankNo(hq_position);
            post.setPostNo(post_no);
            post.setPostPid(Long.valueOf(supHqpost));
            hqclcfPostMapper.addPost(post);
        }
    }

    private void shengchengxddetuandui() throws Exception {
        List<Map<String, Object>> maps = sysDataMigratMapper.generatedXdTeam();
        for (Map<String, Object> map : maps) {
            HqclcfDept dept = new HqclcfDept();
            String teamNo = (String) map.get("TEAM_NO");
            String organization = (String) map.get("ORGANIZATION");
            BigDecimal yybId = (BigDecimal) map.get("YYB_ID");
            String teamName = (String) map.get("TEAM_NAME");
            dept.setDeptName(teamName);
            dept.setDeptCode(teamNo);
            dept.setBusinessLine(3);
            dept.setOrganizat(Integer.valueOf(organization));
            dept.setPid(yybId.longValue());
            dept.setCreator(CommonUtil.getOnlineFullName());
            dept.setStatus("1");
            dept.setDeptType("4");
            hqclcfDeptMapper.addDept(dept);
        }
    }

    /**
     * 生成信贷的部门
     *
     * @throws Exception
     */
    private void generatingCreditDepartmentAccordingToBranch() throws Exception {
        Map<String, Long> newdeptMap = new HashMap<>();
        List<HqclcfDept> depts = sysDataMigratMapper.queryXddeptbyparms();
        for (HqclcfDept dept : depts) {
            newdeptMap.put(dept.getDeptCode(), dept.getId());
        }

        List<Map<String, String>> maps = sysDataMigratMapper.queryXdYyb2Org();
        for (Map<String, String> map : maps) {
            HqclcfDept dept = new HqclcfDept();
            String orgNo = map.get("ORG_NO");
            String no = map.get("NO");
            String name = map.get("NAME");
            String organization = null != map.get("ORGANIZATION") ? map.get("ORGANIZATION") : "20";
            Long pid = null != newdeptMap.get(orgNo) ? newdeptMap.get(orgNo) : null;
            dept.setDeptCode(no);
            dept.setPid(pid);
            dept.setStatus("1");
            dept.setBusinessLine(3);
            dept.setDeptType("3");
            dept.setCreateTime(new Date());
            dept.setCreator(CommonUtil.getOnlineFullName());
            dept.setOrganizat(Integer.valueOf(organization));
            dept.setDeptName(name);
            hqclcfDeptMapper.addDept(dept);
        }

    }


    /**
     * 根据信贷区域生成下级分公司的数据
     *
     * @throws Exception
     */
    private void genjuquyushengchengxiajifengongsi() throws Exception {

        Map<String, Long> newdeptMap = new HashMap<>();
        List<HqclcfDept> depts = sysDataMigratMapper.queryXddeptbyparms();
        for (HqclcfDept dept : depts) {
            newdeptMap.put(dept.getDeptCode(), dept.getId());
        }

        List<Map<String, String>> maps = sysDataMigratMapper.queryXdDqAndRegion();
        for (Map<String, String> map : maps) {
            HqclcfDept dept = new HqclcfDept();
            String no = map.get("NO");
            String name = map.get("NAME");
            String region = map.get("REGION");
            Long aLong = newdeptMap.get(region);
            dept.setDeptCode(no);
            dept.setPid(aLong);
            dept.setStatus("1");
            dept.setBusinessLine(3);
            dept.setDeptType("2");
            dept.setCreateTime(new Date());
            dept.setCreator(CommonUtil.getOnlineFullName());
            dept.setOrganizat(20);
            dept.setDeptName(name);
            hqclcfDeptMapper.addDept(dept);
        }
    }

    /**
     * 修改信贷大区的状态和pid
     *
     * @throws Exception
     */
    private void updatePidandsataus() throws Exception {
        List<HqclcfDept> depts = sysDataMigratMapper.queryXddeptbyparms();
        for (HqclcfDept dept : depts) {
            dept.setStatus("1");
            dept.setPid(Long.valueOf(4));
            sysDataMigratMapper.updateXjdeptPid(dept);
        }

    }

    /**
     * 生成信贷的大区
     *
     * @throws Exception
     */
    private void quertXdDqand2db() throws Exception {
        List<Map<String, String>> maps = sysDataMigratMapper.queryXdArea();
        for (Map<String, String> map : maps) {
            HqclcfDept dept = new HqclcfDept();
            String area_no = map.get("AREA_NO");
            String area = map.get("AREA");
            String compile = null != map.get("COMPILE") ? map.get("COMPILE") : "1000";
            dept.setDeptCode(area_no);
            dept.setOrganizat(Integer.valueOf(compile));
            dept.setDeptName(area);
            dept.setCreator(CommonUtil.getOnlineFullName());
            dept.setCreateTime(new Date());
            dept.setBusinessLine(3);
            dept.setDeptType("1");
            hqclcfDeptMapper.addDept(dept);
        }
    }



 /*   private void shengchengxjpid() throws Exception{
        List<HqclcfDept> xjList = sysDataMigratMapper.queryNewXjDept();
        List<Map<String, Object>> list = sysDataMigratMapper.queryxjpidMap();

        Map<Long,Long> oldMap=new HashMap<>();
        for (Map<String, Object> map:list) {
            BigDecimal id = null!=map.get("ID")?(BigDecimal)map.get("ID"):null;
            BigDecimal pid = null!=map.get("PID")?(BigDecimal)map.get("PID"):null;
            oldMap.put(id.longValue(),pid.longValue());
        }

        for (HqclcfDept dept:xjList) {
            SysMidlleDept midlleDept=new SysMidlleDept();
            midlleDept.setNewId(dept.getId());
            midlleDept.setOldCode(dept.getDeptCode());
            sysDataMigratMapper.addMiddleDept(midlleDept);
            Long pid = oldMap.get(dept.getId());
            dept.setPid(pid);
            sysDataMigratMapper.updateXjdeptPid(dept);
        }
    }*/

    /*   private void shengchengxjdept() throws Exception{
           List<SysOldXjDept> lists = sysDataMigratMapper.queryAllXjdept();
           for (SysOldXjDept xjdept: lists) {
               HqclcfDept dept=new HqclcfDept();
               dept.setDeptCode(xjdept.getNo());
               dept.setDeptName(xjdept.getName());
               dept.setBusinessLine(2);
               dept.setCreateTime(new Date());
               if ("1".equals(xjdept.getStatus())){
                   dept.setStatus("1");
               }else{
                   dept.setStatus("0");
               }
               dept.setCreator(CommonUtil.getOnlineFullName());
               dept.setOrganizat(20);
               switch (xjdept.getDeptType()){
                   case "大区":
                       dept.setDeptType("1");
                       break;
                   case "分公司":
                       dept.setDeptType("2");
                       break;
                   case "营业部":
                       dept.setDeptType("3");
                       break;
                   case "团队":
                       dept.setDeptType("4");
                       break;
               }
               hqclcfDeptMapper.addDept(dept);
           }
       }
   */
    private void generatDeptCodeByUUId() throws Exception {
        HqclcfDept dept = new HqclcfDept();
        List<HqclcfDept> depts = hqclcfDeptMapper.queryAll(dept);
        for (HqclcfDept d : depts) {
            if (d.getId() != 1 && d.getId() != 2 && d.getId() != 3 && d.getId() != 4) {
                d.setDeptCode(RandomUtil.getOrderIdByUUId());
                hqclcfDeptMapper.updataById(d);
            }
        }
    }

    /**
     * 老数据生成到newsalary
     */
/*    private void generateOldDeptData2NewDept() throws Exception{
        List<SysDataMigratDept> oldLists = sysDataMigratMapper.queryAll();
        for (SysDataMigratDept old:oldLists) {
            HqclcfDept dept=new HqclcfDept();
            dept.setDeptName(old.getDeptName());
            dept.setStatus("1");
            dept.setBusinessLine(1);
            dept.setDeptCode(old.getDeptCode());
            dept.setCreateTime(new Date());
            dept.setCreator(CommonUtil.getOnlineFullName());
            dept.setOrganizat(20);
            hqclcfDeptMapper.addDept(dept);
        }
    }*/

    /**
     * 生成pid
     * @throws Exception
     */
/*    private void  generatePidCode2Pid()throws Exception{
        List<SysDataMigratDept> oldLists = sysDataMigratMapper.queryAll();
        Map<String,String> map=new HashMap<>();
        for (SysDataMigratDept old:oldLists) {
            if (old.getId()!=1){
                SysDataMigratDept oldDept = sysDataMigratMapper.queryById(old.getPid());
                //map放入本身的code和父id的code
                map.put(old.getDeptCode(),oldDept.getDeptCode());
            }
        }

        HqclcfDept depts=new HqclcfDept();
        List<HqclcfDept> newdept = hqclcfDeptMapper.queryAll(depts);
        Map<String,Long> newmap=new HashMap<>();
        for (HqclcfDept de:newdept) {
            //newsalary的map放入生成进来的code和生成的id
            newmap.put(de.getDeptCode(),de.getId());
        }

        for (HqclcfDept dept:newdept) {
            SysMidlleDept midlleDept=new SysMidlleDept();
            Long id = dept.getId();
            if (id!=1&&id!=2&&id!=3&&id!=4){
                String pcode= null!=map.get(dept.getDeptCode())?map.get(dept.getDeptCode()):null;
                Long pid =null!= newmap.get(pcode)?newmap.get(pcode):null;
                dept.setPid(pid);
                if (pid==null){
                    dept.setId(Long.valueOf(2));
                }
                midlleDept.setNewId(id);
                midlleDept.setOldCode(dept.getDeptCode());
            }
            sysDataMigratMapper.addMiddleDept(midlleDept);
            hqclcfDeptMapper.updataById(dept);
        }
    }*/


}
