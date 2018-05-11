package com.zhph.scheduled.cl;

import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.zhph.mapper.cl.ClGzymMapper;
import com.zhph.util.SpringContextUtils;


public class ClGzymJob implements Job{
	

	public static final Logger logger = LogManager.getLogger(ClGzymJob.class);
	
	private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }
	
	private ClGzymMapper getApplicationContext(){
        ApplicationContext application = SpringContextUtils.getApplicationContext();
        return (ClGzymMapper) application.getBean(ClGzymMapper.class);
          
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