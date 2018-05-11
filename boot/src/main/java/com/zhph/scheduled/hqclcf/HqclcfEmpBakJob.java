package com.zhph.scheduled.hqclcf;

import com.zhph.service.hqclcf.HqclcfEmpService;

/**
 * Create By lishuangjiang
 */

import com.zhph.service.hqclcf.HqclcfPersonTransferService;
import com.zhph.util.DateUtil;
import com.zhph.util.SpringContextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HqclcfEmpBakJob implements Job {

    public static final Logger logger = LogManager.getLogger(HqclcfEmpBakJob.class);

    private HqclcfEmpService getApplicationContext(){
        ApplicationContext application = SpringContextUtils.getApplicationContext();
        return (HqclcfEmpService) application.getBean(HqclcfEmpService.class);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.info("员工备份中... ");
            String gzym = DateUtil.parseDateFormat(new Date(), "yyyy-MM");
			getApplicationContext().bakupHqclcfEmpByGzym(gzym );
            logger.info("员工备份中结束！");
        } catch (Exception e) {
            logger.info("员工备份中出错 ",e);
        }

    }

}
