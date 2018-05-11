package com.zhph.service.cf.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.exception.AppException;
import com.zhph.mapper.cf.CfCardAbnormityMapper;
import com.zhph.mapper.cf.CfTimeLockMapper;
import com.zhph.mapper.cf.HqclcfGzymMapper;
import com.zhph.mapper.hqclcf.HqclcfBusinessMapper;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpApvMapper;
import com.zhph.model.cf.CfCardAbnormity;
import com.zhph.model.cf.CfTimeLock;
import com.zhph.model.cf.dto.CfCardAbResult;
import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfGzym;
import com.zhph.service.cf.CfCardAbnormityService;
import com.zhph.util.CommonUtil;
import com.zhph.util.ExcelUtil;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/2.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CfCardAbnormityServiceImpl implements CfCardAbnormityService{

    @Resource
    private HqclcfBusinessMapper hqclcfBusinessMapper;
    @Resource
    private CfCardAbnormityMapper cfCardAbnormityMapper;
    @Resource
    private HqclcfEmpApvMapper hqclcfEmpApvMapper;
    @Resource
    private HqclcfDeptMapper hqclcfDeptMapper;
    @Resource
    private HqclcfGzymMapper hqclcfGzymMapper;
    @Resource
    private CfTimeLockMapper cfTimeLockMapper;


    @Override
    public void bulidInitAtr(HttpServletRequest req,Long id) throws Exception {
        CfCardAbnormity cf = new CfCardAbnormity();
        cf.setId(id);
        List<Map<String, String>> list = buildQueryList(cf);
        Map<String, String> map = list.size() == 1 ? list.get(0) :null;
        req.setAttribute("card",map);
    }

    @Override
    public  List<Map<String, String>> queryEmpByQAndGetData(String q) throws Exception {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("q", q);
        queryMap.put("userId", CommonUtil.getOnlineUser().getUserId());
        queryMap.put("posName", "营业部经理");
        List<Map<String, String>> lists = cfCardAbnormityMapper.queryEmpByQAndGetData(queryMap);

        String currentGzym = hqclcfGzymMapper.queryCurrGzym().getCurrentGzym();
        for (Map<String, String> map : lists) {
            map.put("GZ_YM",currentGzym);
            String deptinfo = null != map.get("DEPTINFO") ? map.get("DEPTINFO") : "";
            if (!"".equals(deptinfo)) {
                String[] deptTemp;
                String[] depts = deptinfo.split(",");
                for (String dept : depts) {
                    deptTemp = dept.split("=");
                    switch (deptTemp[0]){
                        case "1":
                            map.put("REGION_NAME",deptTemp[1]);
                            break;
                        case "2":
                            map.put("ORG_NAME",deptTemp[1]);
                            break;
                        case "3":
                            map.put("BUSINESS_NAME",deptTemp[1]);
                            break;
                        case "4":
                            map.put("TEAM_NAME",deptTemp[1]);
                            break;
                    }
                }
            }
        }
        return lists;
    }


   public CfCardAbnormity queryCardAbnormity(String empNo) throws Exception{
       CfCardAbnormity cardAbnormity=new CfCardAbnormity();
       cardAbnormity.setEmpNo(empNo);
       List<CfCardAbnormity> lists = cfCardAbnormityMapper.queryAll(cardAbnormity);
       return lists.size() ==1? lists.get(0) : null;
   }

    @Override
    public void addCardAbnormity(String data) throws Exception {
        HqclcfEmp queryParm=new HqclcfEmp();
        queryParm.setBusinessLine(2);
        List<HqclcfEmp> empList = hqclcfEmpApvMapper.queryAll(queryParm);
        Map<String,HqclcfEmp> empMap=new HashMap<>();
        for (HqclcfEmp emp:empList) {
            Integer status = emp.getStatus();
            if (status!=null){
                if (status==2||status==1||status==3||status==4){
                    empMap.put(emp.getEmpNo(),emp);
                }
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        CfCardAbnormity cardAbnormity = mapper.readValue(data.getBytes(), CfCardAbnormity.class);
        String empNo = cardAbnormity.getEmpNo();
        HqclcfEmp emp = empMap.get(empNo);
        if (emp==null)
            throw new AppException("这个员工编码不存在");
        if (!isLock(cardAbnormity.getGzYm()))
            throw new AppException("当前工资年月考勤统计锁定无法新增");
        if (cfCardAbnormityMapper.queryAbnByEmpNo(cardAbnormity.getEmpNo(), cardAbnormity.getGzYm())!=null)
            throw new AppException("当前工资年月已经录入了"+cardAbnormity.getEmpName()+"的打卡异常记录");
        cardAbnormity.setCreatetime(new Date());
        cardAbnormity.setCreator(CommonUtil.getOnlineFullName());
        cfCardAbnormityMapper.addCfCardAb(cardAbnormity);
    }

    @Override
    public void editCardAbnormity(String data) throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        CfCardAbnormity card = mapper.readValue(data.getBytes(), CfCardAbnormity.class);
        String currentGzym = hqclcfGzymMapper.queryCurrGzym().getCurrentGzym();
        if (!currentGzym.equals(card.getGzYm()))
            throw new AppException("不能修改不是当前工资年月的打卡异常信息");
        if (!isLock(card.getGzYm()))
            throw new AppException("当前工资年月考勤统计锁定无法修改");

        String upWorkAbnormityNo = card.getUpWorkAbnormityNo();
        String offWorkAbnormityNo = card.getOffWorkAbnormityNo();
        String checkAbnormityNo = card.getCheckAbnormityNo();
        int total = Integer.valueOf(upWorkAbnormityNo) + Integer.valueOf(offWorkAbnormityNo) + Integer.valueOf(checkAbnormityNo);
        card.setAbnormityTotalNo(String.valueOf(total));
        //修改
        cfCardAbnormityMapper.updateByCardId(card);
    }

    @Override
    public Grid<Map<String,String>> queryPageInfo(PageBean pageBean, CfCardAbnormity card) throws Exception {
        PageHelper.startPage(pageBean.getPage(),pageBean.getLimit());
        List<Map<String, String>> lists = buildQueryList(card);
        PageInfo<Map<String, String>> pageInfo=new PageInfo<>(lists);
        Grid<Map<String,String>> grid=new Grid<>();
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        return grid;
    }

    private  List<Map<String, String>> buildQueryList(CfCardAbnormity card) {
        buildQueryParams(card);
        List<Map<String, String>> lists = cfCardAbnormityMapper.queryList(card);
        for (Map<String, String> map:lists) {
            String deptInfo = null != map.get("deptInfo") ? map.get("deptInfo") : "";
            if (!"".equals(deptInfo)) {
                String[] deptTemp;
                String[] depts = deptInfo.split(",");
                for (String dept : depts) {
                    deptTemp = dept.split("=");
                    switch (deptTemp[0]){
                        case "4":
                            map.put("team",deptTemp[1]);
                            break;
                        case "3":
                            map.put("dept",deptTemp[1]);
                            break;
                        case "2":
                            map.put("filiale",deptTemp[1]);
                            break;
                        case "1":
                            map.put("region",deptTemp[1]);
                            break;
                    }
                }
            }
        }
        return lists;
    }


    private void buildQueryParams(CfCardAbnormity card) {
        String region = card.getRegion();
        String filiale = card.getFiliale();
        String dept = card.getDept();
        if ("".equals(region)){
            if ("".equals(filiale)){
                if (!"".equals(dept)){
                    card.setQueryDeptParm(dept);
                }
            }else{
                card.setQueryDeptParm(filiale);
            }
        }else{
            card.setQueryDeptParm(region);
        }
    }

    @Override
    public void importExl(MultipartFile file, HttpServletRequest req, HttpServletResponse res) throws Exception {
        InputStream inputStream = file.getInputStream();
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        int trLength = sheet.getLastRowNum();
        HSSFRow row = sheet.getRow(0);
        short tdLength = row.getLastCellNum();

        if (trLength == 0)
            throw new AppException("未导入任何数据！");

        //查询消金系统全部的员工信息
        HqclcfEmp queryParm=new HqclcfEmp();
        queryParm.setBusinessLine(2);
        List<HqclcfEmp> empList = hqclcfEmpApvMapper.queryAll(queryParm);
        Map<String,HqclcfEmp> empMap=new HashMap<>();
        for (HqclcfEmp emp:empList) {
            Integer status = emp.getStatus();
            if (status!=null){
                if (status==1||status==2||status==3||status==4){
                    empMap.put(emp.getEmpNo(),emp);
                }
            }
        }

        //查询工资年月的打卡异常信息
        HqclcfGzym hqclcfGzym = hqclcfGzymMapper.queryCurrGzym();
        String currentGzym = hqclcfGzym.getCurrentGzym();
        CfCardAbnormity cardAbQuery=new CfCardAbnormity();
        cardAbQuery.setGzYm(currentGzym);
        List<CfCardAbnormity> cardAbList = cfCardAbnormityMapper.queryAll(cardAbQuery);
        Map<String,CfCardAbnormity> cardMap=new HashMap<>();
        for (CfCardAbnormity card:cardAbList) {
            cardMap.put(card.getEmpNo(),card);
        }

        List<CfCardAbnormity> list=new ArrayList<>();

        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        Pattern pattern2 = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,1})?$");

            List<String> empNostrList=new ArrayList<>();
        for (int i = 1; i < trLength + 1; i++) {
            CfCardAbnormity cardAbnormity=new CfCardAbnormity();
            HSSFRow tr = sheet.getRow(i);
            for (int j = 0; j < tdLength; j++) {
                HSSFCell cell = tr.getCell(j);
                if (cell != null) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String cellValue = cell.getStringCellValue().trim();
                switch (j) {
                    case 0:
                        if ("".equals(cellValue))
                            throw new AppException("员工编码不能为空！");
                        if (empMap.get(cellValue)==null)
                            throw new AppException("员工编码不存在，或员工编码未曾入职公司");
                        HqclcfEmp emp = empMap.get(cellValue);
                        cardAbnormity.setGzYm(currentGzym);
                        cardAbnormity.setJobNo(null!=emp.getPosition()?emp.getPosition():"");
                        cardAbnormity.setDeptCode(!"".equals(emp.getDeptNo())?emp.getDeptNo():"");

                        if (cardMap.get(cellValue)!=null){
                            throw new AppException("该工资年月员工编码已经录入！");
                        }


                        for (String empAd: empNostrList) {
                            if (empAd.equals(cellValue)){
                                throw new AppException("同一个员工编码，请上传的时候写到一行里面");
                            }
                        }
                        empNostrList.add(cellValue);

                        cardAbnormity.setEmpNo(cellValue);
                        break;
                    case 1:
                        if ("".equals(cellValue))
                            throw new AppException("员工姓名不能为空！");
                        cardAbnormity.setEmpName(cellValue);
                        break;
                    case 2:
                        if (!pattern.matcher(cellValue).matches())
                            throw new Exception("上班打卡异常次数必须为正整数");
                        cardAbnormity.setUpWorkAbnormityNo(!"".equals(cellValue) ? cellValue : null);
                        break;
                    case 3:
                        if (!pattern.matcher(cellValue).matches())
                            throw new Exception("下班打卡异常次数必须为正整数");
                        cardAbnormity.setOffWorkAbnormityNo(!"".equals(cellValue) ? cellValue : null);
                        break;
                    case 4:
                        if (!pattern.matcher(cellValue).matches())
                            throw new Exception("抽查打卡异常次数必须为正整数");
                        cardAbnormity.setCheckAbnormityNo(!"".equals(cellValue) ? cellValue : null);
                        break;
                    case 5:
                        if (!pattern2.matcher(cellValue).matches())
                            throw new Exception("旷工天数必须为正数或则一位小数");
                        cardAbnormity.setAbsenteeismNo(!"".equals(cellValue) ? cellValue : null);
                        break;
                }
            }
            cardAbnormity.setCreatetime(new Date());
            cardAbnormity.setCreator(CommonUtil.getOnlineFullName());
            list.add(cardAbnormity);
        }

        //计算打卡异常总次数
        for (CfCardAbnormity cf : list) {
            int count = Integer.valueOf(cf.getUpWorkAbnormityNo()) +
                    Integer.valueOf(cf.getOffWorkAbnormityNo()) +
                    Integer.valueOf(cf.getCheckAbnormityNo());
            cf.setAbnormityTotalNo(String.valueOf(count));
        }
        //导入进去
        cfCardAbnormityMapper.batchInsertList(list);
    }

    @Override
    public CfCardAbResult buildDeptSelect(Long id) throws Exception {
        CfCardAbResult result=new CfCardAbResult();
        if (id!=null){
            List<HqclcfDept> depts = hqclcfDeptMapper.queryCfDeptStartWithId(id);
            for (HqclcfDept dept: depts) {
                switch (dept.getDeptType()){
                    case "1":
                        result.setAreaId(dept.getId());
                        break;
                    case "2":
                        result.setOrgId(dept.getId());
                        break;
                    case "3":
                        result.setSaleId(dept.getId());
                        break;
                }
            }
        }
        return result;
    }

    /**
     * 查询打出打卡异常信息
     * @param cardAbnormity
     * @param res
     * @throws Exception
     */
    @Override
    public void exportExl(CfCardAbnormity cardAbnormity, HttpServletResponse res) throws Exception {
        List<Map<String, String>> list = buildQueryList(cardAbnormity);
        String[] colTitleAry = {"序号", "年月", "员工编码", "员工姓名","大区","分公司","营业部","团队","职务","上班打卡异常", "下班打卡异常","抽查打卡异常次数","异常打卡总次数","旷工次数"};
        String[][] array = new String[list.size()][colTitleAry.length];
        short[] colWidthAry = {80,100,100,100,100,100,100,100,100,100,100,100,100,100,100};
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            array[i][0] =  i+1+""; //序号
            array[i][1] =null!=map.get("gzYm")?map.get("gzYm"):"";
            array[i][2] =null!=map.get("empNo")?map.get("empNo"):"";
            array[i][3] =null!=map.get("empName")?map.get("empName"):"";
            array[i][4] =null!=map.get("region")?map.get("region"):"";
            array[i][5] =null!=map.get("filiale")?map.get("filiale"):"";
            array[i][6] =null!=map.get("dept")?map.get("dept"):"";
            array[i][7] =null!=map.get("team")?map.get("team"):"";
            array[i][8] =null!=map.get("jobNo")?map.get("jobNo"):"";
            array[i][9] =null!=map.get("upWorkAbnormityNo")?map.get("upWorkAbnormityNo"):"";
            array[i][10] =null!=map.get("offWorkAbnormityNo")?map.get("offWorkAbnormityNo"):"";
            array[i][11] =null!=map.get("checkAbnormityNo")?map.get("checkAbnormityNo"):"";
            array[i][12] =null!=map.get("abnormityTotalNo")?map.get("abnormityTotalNo"):"";
            array[i][13] =null!=map.get("absenteeismNo")?map.get("absenteeismNo"):"";
        }
        //导出Util
        ExcelUtil excelUtil=new ExcelUtil();
        excelUtil.writeExecl(colTitleAry,colWidthAry,array,null,res,"异常打卡信息",1);
    }

    /**
     * 根据id查询打卡异常信息
     * @param id
     * @return
     * @throws Exception
     */
    private CfCardAbnormity queryCard(Long id) throws Exception{
        CfCardAbnormity cf=new CfCardAbnormity();
        cf.setId(id);
        List<CfCardAbnormity> list = cfCardAbnormityMapper.queryAll(cf);
        return list.size() == 1 ? list.get(0) : null;
    }

    /**
     * 根据id删除异常打卡信息
     * @param id
     * @throws Exception
     */
    @Override
    public void delele(Long id) throws Exception {
        CfCardAbnormity card = queryCard(id);
        String gzYm = card.getGzYm();
        HqclcfGzym hqclcfGzym = hqclcfGzymMapper.queryCurrGzym();
        if (hqclcfGzym==null)
            throw new AppException("查询当前工资年月失败");
        String currentGzym = hqclcfGzym.getCurrentGzym();
        if (!gzYm.equals(currentGzym))
            throw new AppException("不能删除不是当前工资年月的信息");
        if(!isLock(currentGzym))
            throw new AppException("当前工资年月的数据在考勤统统计中被锁定");
        cfCardAbnormityMapper.delById(id);
    }



    /**
     * 查询当前工资年月是否在考勤统计里面被锁定
     * @param currentGzym
     * @return
     * @throws Exception
     */
    private  boolean isLock(String currentGzym) throws Exception{
        boolean flag;
        String[] gzSp = currentGzym.split("-");
        CfTimeLock cftime =new CfTimeLock();
        cftime.setYear(Integer.valueOf(gzSp[0]));
        cftime.setMonth(Integer.valueOf(gzSp[1]));
        CfTimeLock cfTimeLock = cfTimeLockMapper.queryIsLock(cftime);
        if (cfTimeLock!=null&&cfTimeLock.getIsLock()==1){
            flag=false;
        }else if (cfTimeLock!=null&&cfTimeLock.getIsLock()==0){
            flag=true;
        }else{
            throw new AppException("当前工资年月未能查询到在考勤统计是否锁定，无法录入");
        }
        return flag;
    }

}
