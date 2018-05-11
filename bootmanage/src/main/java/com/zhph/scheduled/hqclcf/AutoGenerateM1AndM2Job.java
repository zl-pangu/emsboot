package com.zhph.scheduled.hqclcf;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.zhph.service.cl.ClSixMonthM2Service;
import com.zhph.util.SpringContextUtils;

/**
 * @author lxp
 * @date 2018年3月1日 上午10:22:47
 * @parameter
 * @return
 */
public class AutoGenerateM1AndM2Job implements Job {
	public static final Logger logger = LogManager.getLogger(AutoGenerateM1AndM2Job.class);

	private ClSixMonthM2Service getApplicationContext() {
		ApplicationContext application = SpringContextUtils.getApplicationContext();
		return (ClSixMonthM2Service) application.getBean(ClSixMonthM2Service.class);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {

			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;

			String gzym = year + "-" + month;

			logger.info("自动生成M1与M2+开始...");
			getApplicationContext().AutoGenerateM1AndM2(gzym);
			logger.info("自动生成M1与M2+结束...");
		} catch (Exception e) {
			logger.info("自动生成M1与M2+出错... ", e);
		}

	}

}
