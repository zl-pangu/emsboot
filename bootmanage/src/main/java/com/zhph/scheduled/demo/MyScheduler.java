package com.zhph.scheduled.demo;

import com.zhph.scheduled.cf.GzymJob;
import com.zhph.scheduled.cf.autoEmpStatusJob;
import com.zhph.scheduled.cl.ClGzymJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.zhph.scheduled.cl.ClFirstBeOverJob;

/**
 * @author Jianglinghao
 * @ClassName MyScheduler
 * @Description TODO 这是一个关于定时任务的总类
 * @date 2017年8月15日
 */
@Component
public class MyScheduler {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * @return void    返回类型
     * @throws SchedulerException
     * @Title scheduleJobs
     * @Description 所有的定时任务 运行开始
     */
    public void scheduleJobs() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //transferEffect(scheduler);
        GzymJob(scheduler);
        //ClGzymJob(scheduler);
        autoEmpStatusJob(scheduler);
        FirstBeOverJob(scheduler);
        //没有具体的业务需求 暂时关闭 定时任务
//	        startJob1(scheduler);
//	        startJob2(scheduler);
    }

    /**
     * @param @param  scheduler
     * @param @throws SchedulerException    参数
     * @return void    返回类型
     * @Title startJob1
     * @Description TODO 为自定义的定时任务设置 trigger 和jobDeail
     */
    private void startJob1(Scheduler scheduler) throws SchedulerException {

        //防止启动定时任务报错，报错原因是因为数据库中已经存在相同的 job名称和group ，所以每次重启服务器时删除已经存在 数据库中的定时任务
        JobKey jobKey = JobKey.jobKey("job1", "group1");
        scheduler.deleteJob(jobKey);

        JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class)
                .withIdentity("job1", "group1").build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }


    private void startJob2(Scheduler scheduler) throws SchedulerException {

        JobKey jobKey = JobKey.jobKey("job2", "group1");
        scheduler.deleteJob(jobKey);


        JobDetail jobDetail = JobBuilder.newJob(ScheduledJob2.class)
                .withIdentity("job2", "group1").build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger2", "group1")
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    /**
     * 人员异动定时任务（每月1号凌晨开始执行）
     *
     * @param scheduler
     * @throws SchedulerException
     */
    private void transferEffect(Scheduler scheduler) throws SchedulerException {

        JobKey jobKey = JobKey.jobKey("transCheck", "group1");
        scheduler.deleteJob(jobKey);

        JobDetail jobDetail = JobBuilder.newJob(TransferJob.class)
                .withIdentity("transCheck", "group1")
                .build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0 1 * ?");

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("transferTrigger", "group1")
                .withSchedule(scheduleBuilder)
                .build();
        scheduler.scheduleJob(jobDetail, cronTrigger);

    }


    /**
     * 工资年月定时任务（每月15号中午12点开始执行）
     *
     * @param scheduler
     * @throws SchedulerException
     */
    private void GzymJob(Scheduler scheduler) throws SchedulerException {

        JobKey jobKey = JobKey.jobKey("gzymJob", "group1");
        scheduler.deleteJob(jobKey);

        JobDetail jobDetail = JobBuilder.newJob(GzymJob.class)
                .withIdentity("gzymJob", "group1")
                .build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 12 15 * ?");

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("gzymJobTrigger", "group1")
                .withSchedule(scheduleBuilder)
                .build();

        scheduler.scheduleJob(jobDetail, cronTrigger);

    }

    /**
     * 员工状态变更定时任务（每天凌晨1点开始执行）autoEmpStatus
     *
     * @param scheduler
     * @throws SchedulerException
     */
    private void autoEmpStatusJob(Scheduler scheduler) throws SchedulerException {

        JobKey jobKey = JobKey.jobKey("autoEmpStatusJob", "group1");
        scheduler.deleteJob(jobKey);

        JobDetail jobDetail = JobBuilder.newJob(autoEmpStatusJob.class)
                .withIdentity("autoEmpStatusJob", "group1")
                .build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 1 * * ?");

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("autoEmpStatusJobTrigger", "group1")
                .withSchedule(scheduleBuilder)
                .build();

        scheduler.scheduleJob(jobDetail, cronTrigger);

    }
    
    /**
     * 信贷工资年月
     * @param scheduler
     * @throws SchedulerException
     */
    private void ClGzymJob(Scheduler scheduler) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("clGzymJob", "group2");
        scheduler.deleteJob(jobKey);

        JobDetail jobDetail = JobBuilder.newJob(ClGzymJob.class)
                .withIdentity("clGzymJob", "group2")
                .build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 12 15 * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("clGzymJobTrigger", "group2")
                .withSchedule(scheduleBuilder)
                .build();

        scheduler.scheduleJob(jobDetail, cronTrigger);
    }
    
    
    private void FirstBeOverJob(Scheduler scheduler) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("firstBeOverJob", "group2");
        scheduler.deleteJob(jobKey);

        JobDetail jobDetail = JobBuilder.newJob(ClFirstBeOverJob.class)
                .withIdentity("firstBeOverJob", "group2")
                .build();
        
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0 5 * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("firstBeOverJobTrigger", "group2")
                .withSchedule(scheduleBuilder)
                .build();

        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

}

