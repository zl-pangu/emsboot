package com.zhph.scheduled.cf;

import com.zhph.mapper.cf.CfEmpStatusMapper;
import com.zhph.util.SpringContextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
/**
 * Create By lishuangjiang
 */

public class autoEmpStatusJob implements Job {

    public static final Logger logger = LogManager.getLogger(autoEmpStatusJob.class);

    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    private CfEmpStatusMapper getApplicationContext(){
        ApplicationContext application = SpringContextUtils.getApplicationContext();
        return (CfEmpStatusMapper) application.getBean("cfEmpStatusMapper");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Thread.sleep(5000);

            logger.info("同步员工状态开始... ");
            getApplicationContext().autoEmpStatus();
            logger.info("同步员工状态结束！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.info("同步员工状态出错 ");
            e.printStackTrace();
        }

    }

}
