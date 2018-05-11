package com.zhph.scheduled.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zhph.service.demo.SexService;
import com.zhph.util.SpringContextUtils;

/**
 * 
 * @ClassName ScheduledJob
 * @Description TODO 定时任务job 用于处理自己模块的业务逻辑
 * @author Jianglinghao
 * @date 2017年8月15日
 *
 */
public class ScheduledJob implements Job{
	
	private SexService sexService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	
    	//获取 spring 容器bean对象
		if (sexService == null)
		{

			sexService = (SexService) SpringContextUtils
					.getBeanByClass(SexService.class);
		}
    	System.out.println(sexService.findById(1));

    	System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA: The time is now " + dateFormat().format(new Date()));
        
    }
    
    
    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

}
