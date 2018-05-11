package com.zhph.scheduled.demo;

/**
 * Create By lishuangjiang
 */

import com.zhph.service.hqclcf.HqclcfPersonTransferService;
import com.zhph.util.SpringContextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;

public class TransferJob implements Job {

    public static final Logger logger = LogManager.getLogger(TransferJob.class);

    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    private HqclcfPersonTransferService getApplicationContext(){
        ApplicationContext application = SpringContextUtils.getApplicationContext();
        return (HqclcfPersonTransferService) application.getBean("HqclcfPersonTransferService");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.info("人员异动检查中... ");
            getApplicationContext().quartzMission();
            logger.info("人员异动检查结束！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.info("人员异动检查出错 ");
            e.printStackTrace();
        }

    }

}
