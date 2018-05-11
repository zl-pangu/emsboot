package com.zhph.scheduled.hqclcf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.zhph.service.hqclcf.HqclcfEmpService;
import com.zhph.util.SpringContextUtils;

public class HqclcfEmpAutoTfDateType implements Job {

	public static final Logger logger = LogManager.getLogger(HqclcfEmpAutoTfDateType.class);

	private HqclcfEmpService getApplicationContext() {
		ApplicationContext application = SpringContextUtils.getApplicationContext();
		return (HqclcfEmpService) application.getBean(HqclcfEmpService.class);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.info("员工自动转正中... ");
			getApplicationContext().AutoTfEmpType();
			logger.info("员工自动转正结束！");
		} catch (Exception e) {
			logger.info("员工自动转正出错 ", e);
		}

	}

}
