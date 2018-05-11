package com.zhph.mapper.common;

import com.zhph.model.common.SysLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouliang on 2018/1/16.
 */
@Repository
public interface BaseMapper {
    /**
     * 保存操作日志
     * @param sysLog
     */
    void saveLog(SysLog sysLog);


    List<SysLog> queryLogByUserName(Map<String,Object> map);
}
