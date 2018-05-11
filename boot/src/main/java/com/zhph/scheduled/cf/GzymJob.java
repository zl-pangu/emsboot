package com.zhph.scheduled.cf;

/**
 * Create By lishuangjiang
 */

import com.zhph.mapper.cf.HqclcfGzymMapper;
import com.zhph.scheduled.demo.TransferJob;
import com.zhph.util.SpringContextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;

public class GzymJob implements Job {

    public static final Logger logger = LogManager.getLogger(TransferJob.class);

    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    private HqclcfGzymMapper getApplicationContext(){
        ApplicationContext application = SpringContextUtils.getApplicationContext();
        return (HqclcfGzymMapper) application.getBean("HqclcfGzymMapper");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Thread.sleep(5000);

            logger.info("同步工资年月开始... ");
            getApplicationContext().updateGzym();
            logger.info("同步工资年月结束！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.info("同步工资年月出错 ");
            e.printStackTrace();
        }

    }

}
