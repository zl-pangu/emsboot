package com.zhph.service.sys;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2018/1/23.
 */
public interface SysDataMigratService {
    void generateDept() throws Exception;

    void generatePost() throws Exception;

    void generateWork() throws Exception;

    void generateXfEmp() throws Exception;

    void syncAttachmentnInfo() throws Exception;

    void generateHqEmp() throws Exception;

    void generatexfPost() throws Exception;

    void importExl(MultipartFile file) throws Exception;

    void generateXdPost() throws Exception;

    void generateXjAutoMeted() throws Exception;

    void generateXjPaidLeave() throws Exception;

    void generateXjNodeDays() throws Exception;

    void generateXjVacatemanage() throws Exception;

    void generateXjAbnormity() throws Exception;

    void generateXjTimestatistical() throws Exception;

    void generateXjEmpStatus() throws Exception;

    JSONObject generateXjAutoMetedWithRef(String gzym) throws Exception;

    JSONObject QxGenerateXjWithRef(String gzym) throws Exception;



}
