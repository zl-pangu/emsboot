package com.zhph.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.zhph.service.sys.SysDataMigratService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/1/22.
 */
@RequestMapping("/sys/dataMigrat")
@Controller
public class SysDataMigratController {

    @Resource
    private SysDataMigratService sysDataMigratService;

    @RequestMapping("init")
    public String init(){
            return "/pages/sys/dataMigrat/sys_datamigrat";
    }

    @RequestMapping("generate")
    @ResponseBody
    public Object generate(){
        JSONObject obj = new JSONObject();
        obj.put("code", 200);
        try {
            sysDataMigratService.generateDept();
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    @RequestMapping("generatePost")
    @ResponseBody
    public Object generatePost(){
        JSONObject obj = new JSONObject();
        obj.put("code", 200);
        try {
            sysDataMigratService.generatePost();
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }
    @RequestMapping("generateWork")
    @ResponseBody
    public Object generateWork(){
        JSONObject obj = new JSONObject();
        obj.put("code", 200);
        try {
            sysDataMigratService.generateWork();
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    @RequestMapping("generateXfEmp")
    @ResponseBody
    public Object generateXfEmp(){
        JSONObject obj = new JSONObject();
        obj.put("code", 200);
        try {
            sysDataMigratService.generateXfEmp();
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    @RequestMapping("syncAttachmentnInfo")
    @ResponseBody
    public Object syncAttachmentnInfo(){
        JSONObject obj = new JSONObject();
        obj.put("code", 200);
        try {
            sysDataMigratService.syncAttachmentnInfo();
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    @RequestMapping("generateHqEmp")
    @ResponseBody
    public Object generateHqEmp(){
        JSONObject obj = new JSONObject();
        obj.put("code", 200);
        try {
            sysDataMigratService.generateHqEmp();
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 生成消分的岗位信息
     * @return
     */
    @RequestMapping("generatexfPost")
    @ResponseBody
    public Object generatexfPost(){
        JSONObject obj = new JSONObject();
        obj.put("code", 200);
        try {
            sysDataMigratService.generatexfPost();
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }


    @RequestMapping("importExl")
    @ResponseBody
    public JSONObject importExl(MultipartFile file) {
        JSONObject obj =new JSONObject();
        obj.put("code",200);
        try {
            sysDataMigratService.importExl(file);
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    @RequestMapping("generateXdPost")
    @ResponseBody
    public JSONObject generateXdPost() {
        JSONObject obj =new JSONObject();
        obj.put("code",200);
        try {
            sysDataMigratService.generateXdPost();
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }


    /**
     *  分割线
     *  考勤排班
     */
    @RequestMapping("generateXjAutoMeted")
    @ResponseBody
    public JSONObject generateXjAutoMeted() {
        JSONObject obj =new JSONObject();
        obj.put("code",200);
        try {
            Thread.sleep(3000);
            sysDataMigratService.generateXjAutoMeted();
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 生成调休天数
     * @return
     */
    @RequestMapping("generateXjPaidLeave")
    @ResponseBody
    public JSONObject generateXjPaidLeave() {
        JSONObject obj =new JSONObject();
        obj.put("code",200);
        try {
            sysDataMigratService.generateXjPaidLeave();
        } catch (Exception e) {
            obj.put("code", 503);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 请休假附表生成
     * @return
     */
    @RequestMapping("generateXjNodeDays")
    @ResponseBody
    public JSONObject generateXjNodeDays() {
        JSONObject obj =new JSONObject();
        obj.put("code",200);
        try {
            Thread.sleep(2000);
            sysDataMigratService.generateXjNodeDays();
        } catch (Exception e) {
            obj.put("code", 503);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 请休假附正表生成
     * @return
     */
    @RequestMapping("generateXjVacatemanage")
    @ResponseBody
    public JSONObject generateXjVacatemanage() {
        JSONObject obj =new JSONObject();
        obj.put("code",200);
        try {
            Thread.sleep(2000);
            sysDataMigratService.generateXjVacatemanage();
        } catch (Exception e) {
            obj.put("code", 503);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 请休假附正表生成
     * @return
     */
    @RequestMapping("generateXjAbnormity")
    @ResponseBody
    public JSONObject generateXjAbnormity() {
        JSONObject obj =new JSONObject();
        obj.put("code",200);
        try {
            Thread.sleep(2000);
            sysDataMigratService.generateXjAbnormity();
        } catch (Exception e) {
            obj.put("code", 503);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 生成考勤统计
     * @return
     */
    @RequestMapping("generateXjTimestatistical")
    @ResponseBody
    public JSONObject generateXjTimestatistical() {
        JSONObject obj =new JSONObject();
        obj.put("code",200);
        try {
            Thread.sleep(2000);
            sysDataMigratService.generateXjTimestatistical();
        } catch (Exception e) {
            obj.put("code", 503);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 生成消分员工状态
     * @return
     */
    @RequestMapping("generateXjEmpStatus")
    @ResponseBody
    public JSONObject generateXjEmpStatus() {
        JSONObject obj =new JSONObject();
        obj.put("code",200);
        try {
            Thread.sleep(2000);
            sysDataMigratService.generateXjEmpStatus();
        } catch (Exception e) {
            obj.put("code", 503);
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }


    /**
     * 同步考勤排班
     * @return
     */
    @RequestMapping("generateXjAutoMetedWithRef")
    @ResponseBody
    public Object generateXjAutoMetedWithRef(@Param("gzym") String gzym) throws Exception {
        return sysDataMigratService.generateXjAutoMetedWithRef(gzym);
    }
    /**
     * 取消同步考勤排班
     * @return
     */
    @RequestMapping("QxGenerateXjWithRef")
    @ResponseBody
    public Object QxGenerateXjWithRef(@Param("gzym") String gzym) throws Exception {
        return sysDataMigratService.QxGenerateXjWithRef(gzym);
    }


}
