package com.zhph.scheduled.cl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhph.config.datasource.DataSourceNameList;
import com.zhph.config.datasource.DataSourceSelector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.zhph.mapper.cl.ClFirstBeOverdueMapper;
import com.zhph.mapper.cl.ClGzymMapper;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.model.cl.ClFirstBeOverdue;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.service.cl.ClFirstBeOverdueService;
import com.zhph.service.hqclcf.HqclcfPersonTransferService;
import com.zhph.util.SpringContextUtils;

public class ClFirstBeOverJob implements Job{

	public static final Logger logger = LogManager.getLogger(ClFirstBeOverJob.class);
	
//	private ClFirstBeOverdueMapper getApplicationContext(){
//        ApplicationContext application = SpringContextUtils.getApplicationContext();
//        return (ClFirstBeOverdueMapper) application.getBean(ClFirstBeOverdueMapper.class);
//          
//    }
	
//	private ClGzymMapper getApplicationContextGzym(){
//        ApplicationContext applicationGzym = SpringContextUtils.getApplicationContext();
//        return (ClGzymMapper) applicationGzym.getBean(ClGzymMapper.class);
//          
//    }
	
//	private HqclcfDeptMapper getApplicationContextDept(){
//        ApplicationContext applicationDept = SpringContextUtils.getApplicationContext();
//        return (HqclcfDeptMapper) applicationDept.getBean(HqclcfDeptMapper.class);
//          
//    }
	
	 private ClFirstBeOverdueService getApplicationContextFirst(){
	        ApplicationContext getApplicationContextFirst = SpringContextUtils.getApplicationContext();
	        return (ClFirstBeOverdueService) getApplicationContextFirst.getBean("ClFirstBeOverdueService");
	    }
	
	@Override
	@DataSourceSelector(DataSourceNameList.BUSI_DATASOURCE)
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("孙伟宇");
		try {
			Thread.sleep(5000);
            logger.info("查询首逾开始... ");
            getApplicationContextFirst().ClFirstBeOverduTime();
			logger.info("查询首逾结束... ");
		} catch (Exception e) {
			logger.info("查询首逾出错... ");
			e.printStackTrace();
		}
		
	}
	

}
