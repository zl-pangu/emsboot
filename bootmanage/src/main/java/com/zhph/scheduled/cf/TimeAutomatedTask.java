package com.zhph.scheduled.cf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.zhph.model.sys.SysUser;
import com.zhph.service.cf.TimeAutomatedService;
import com.zhph.util.SpringContextUtils;

/**
 * 消金考勤排班
 */
public class TimeAutomatedTask extends QuartzJobBean {
	private Log log = LogFactory.getLog(TimeAutomatedTask.class);

    private TimeAutomatedService getApplicationContext(){
        ApplicationContext application = SpringContextUtils.getApplicationContext();
        return application.getBean(TimeAutomatedService.class);
    }
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			log.info("消金考勤排班开始……");
			SysUser user = new SysUser();
			user.setFullName("admin");
			getApplicationContext().addAllEmp(user);
			log.info("消金考勤排班结束……");
		} catch (Exception e) {
			log.error("消金考勤排班错误……", e);
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
}
