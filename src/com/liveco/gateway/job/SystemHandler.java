package com.liveco.gateway.job;

import static org.quartz.JobBuilder.newJob;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.liveco.gateway.common.ThreadPoolManager;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.system.SystemRepository;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class SystemHandler {
	
    private static final Logger LOG = LogManager.getLogger(SystemHandler.class);
	private SystemRepository repository;
	private Scheduler quartzScheduler;
	private ScheduledFuture<?> schedulerFuture;
	private Object schedulerLock = new Object();
	
	
	private static final String SYSTEM_HANDLER_THREADPOOL_NAME = "systemHandler";
	protected final ScheduledExecutorService scheduler = ThreadPoolManager.getScheduledPool(SYSTEM_HANDLER_THREADPOOL_NAME);
	
	private static final String JOB_DAILY = "job-daily-";
	private static final String JOB_DAILY_TRIGGER = "job-daily-trigger-";
	
	String thingUid = "abc";
	
	public SystemHandler(SystemRepository repository){
		this.repository = repository;
		initalize();
		
	}
	
	public void initalize(){
		restartJobs();
		
	}
	
	public void dispose(){
		if(schedulerFuture !=null && !schedulerFuture.isCancelled()){
			schedulerFuture.cancel(true);
			schedulerFuture = null;
		}
		stopJobs();
		quartzScheduler = null;
		LOG.debug(" disposed ");
	}
	
	private void restartJobs(){
		schedulerFuture = scheduler.schedule(new Runnable(){
			@Override
			public void run() {
							
				try{
					synchronized(schedulerLock){
						if(quartzScheduler == null){
							quartzScheduler = StdSchedulerFactory.getDefaultScheduler();
							System.out.println(" StdSchedulerFactory.getDefaultScheduler()");
							//quartzScheduler.clear();
						}
						
					}	
					}catch( SchedulerException ex){
						LOG.error(""+ex.getMessage(),ex);
					}
				
				stopJobs();
				startTimerExecution();
				
				//testSchedule();
			}
		}, 2000, TimeUnit.MICROSECONDS);
	}
	
	private void stopJobs(){
		synchronized(schedulerLock){
			
			if(quartzScheduler !=null){
				try{
					Set<JobKey> jobKeys =  quartzScheduler.getJobKeys(GroupMatcher.jobGroupEquals(JOB_DAILY));
					for(JobKey jobKey: jobKeys){ 
						System.out.println("111--------------  jobkey   "+jobKey.getGroup()+"   "+jobKey.getName());
					}						
					
					for( JobKey jobKey :quartzScheduler.getJobKeys(GroupMatcher.jobGroupEquals(thingUid))){
						quartzScheduler.deleteJob(jobKey);
					}
					
				}catch(SchedulerException ex){
					LOG.error(""+ex.getMessage(),ex);
				}
			}
			
		}
	}
	
	public Scheduler getScheduler(){
		return quartzScheduler;
	}
	
	public void startTimerExecution(){
		try{
			quartzScheduler.start();
			quartzScheduler.getListenerManager().addSchedulerListener(new MySchedulerListener(quartzScheduler));
			//quartzScheduler.standby();
		}catch(SchedulerException ex){
			LOG.error(""+ex.getMessage(),ex);
		}
	}
	
	public void scheduleRange(SystemStructure type, int system_index,  Class clazz, Date eventOpen, Date eventClose) throws SchedulerException{
		scheduleON(type, system_index, clazz, eventOpen);
		scheduleOFF(type, system_index, clazz, eventClose);
	}
	
	// MyJob.class
	public void scheduleON(SystemStructure type, int system_index,  Class clazz, Date eventAt) throws SchedulerException{
       	
		JobDataMap data = new JobDataMap();
		data.put("repository", repository);// event
		data.put("system-type", type.name() );// thing
		data.put("system-index", system_index);
		data.put("event", "open");     
		
		JobDetail jobDetail = newJob(clazz)
				.usingJobData(data)
				.withIdentity( "system.open."+system_index,JOB_DAILY+type.name())
				.build();

		Trigger trigger = TriggerBuilder.newTrigger()
		.withIdentity("openTrigger"+system_index, JOB_DAILY_TRIGGER+type.name())
		.startNow()  	//.startAt(eventAt.getTime())	
		.withSchedule(CronScheduleBuilder.cronSchedule("0 " + eventAt.getMinutes() + " " + eventAt.getHours() + " * * ? *"))		
		.build();  	
		
		quartzScheduler.scheduleJob(jobDetail, trigger);
	}
	
	public void scheduleOFF(SystemStructure type, int system_index,Class clazz, Date eventAt) throws SchedulerException{
	
		JobDataMap data = new JobDataMap();
		data.put("repository", repository);// event
		data.put("system-type", type.name() );// thing
		data.put("system-index", system_index);
		data.put("event", "close");     		
		JobDetail jobDetail = newJob(clazz)
				.usingJobData(data)
				.withIdentity( "system.close."+system_index,JOB_DAILY+type.name())
				.build();
		Trigger trigger = TriggerBuilder.newTrigger()
		.withIdentity("offTrigger"+system_index, JOB_DAILY_TRIGGER+type.name())
		.startNow()		//.startAt(eventAt.getTime())	
		.withSchedule(CronScheduleBuilder.cronSchedule("0 " + eventAt.getMinutes() + " " + eventAt.getHours() + " * * ? *"))		
		.build(); 
		
		quartzScheduler.scheduleJob(jobDetail, trigger);
	}
	
	public Set<JobKey> getDailySchedules() throws SchedulerException{

		Set<JobKey> jobKeys =  quartzScheduler.getJobKeys(GroupMatcher.jobGroupEquals(JOB_DAILY));
		for(JobKey jobKey: jobKeys){
			System.out.println("getDailySchedules--------------  jobkey   "+jobKey.getGroup()+"   "+jobKey.getName());
		}
		return jobKeys;
	
	}
	
	public Set<JobKey> getSystemSchedules(SystemStructure type) throws SchedulerException{
		Set<JobKey> jobKeys =  quartzScheduler.getJobKeys(GroupMatcher.jobGroupContains(type.name()));
		for(JobKey jobKey: jobKeys){
			System.out.println("getSystemSchedules--------------  jobkey   "+jobKey.getGroup()+"   "+jobKey.getName());
		}
		return jobKeys;
	}
	
    public void testSchedule(){
    			
		// create the job
       	JobBuilder jobBuilder = JobBuilder.newJob(ShelfLightOnOffJob.class);
       	jobBuilder.withDescription("this is a job description");
       	
		JobDataMap data = new JobDataMap();
		data.put("system-index", 3);
		data.put("system-type", SystemStructure.SHELF_LIGHTING_SYSTEM.name() );// thing
		data.put("event", "open");     
		data.put("repository", repository);// event
		
		JobDetail jobDetail1 = newJob(ShelfLightOnOffJob.class)
				.usingJobData(data)
				.withIdentity( "shelf4","job-daily-"+"lighting-on")
				.build();   	


       	JobBuilder jobBuilder2 = JobBuilder.newJob(ShelfLightOnOffJob.class);
       	jobBuilder2.withDescription("this is a job description");
       	
		JobDataMap data2 = new JobDataMap();
		data2.put("system-index", 3);
		data2.put("system-type", SystemStructure.SHELF_LIGHTING_SYSTEM.name() );// thing
		data2.put("event", "close");     
		data2.put("repository", repository);// event		
		JobDetail jobDetail2 = jobBuilder2
				.usingJobData(data2)
				.withIdentity( "shelf3","job-daily-"+"lighting-off")
				.build();		
		
    	// create the trigger
		/*
    	SimpleTrigger trigger = TriggerBuilder.newTrigger()
		.withIdentity("myTrigger", "trigger-job-daily")
		.startNow()
		.withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withRepeatCount(1)
                .withIntervalInSeconds(2))		
		.build();
    	*/	
    	
		Calendar rightNow = Calendar.getInstance();
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
		int min = rightNow.get(Calendar.MINUTE);
		
		System.out.println("Current time: " + new Date());
		
		// Fire at curent time + 1 min every day
		Trigger trigger1 = TriggerBuilder.newTrigger()
		.withIdentity("myTrigger1111", "trigger-job-daily")
		.startAt(DateBuilder.todayAt(3, 20, 20))	
		.withSchedule(CronScheduleBuilder.cronSchedule("0 " + (min + 1) + " " + hour + " * * ? *"))		
		.build();    	

		Trigger trigger2 = TriggerBuilder.newTrigger()
		.withIdentity("myTrigger1112", "trigger-job-daily")
		.startAt(DateBuilder.todayAt(3, 20, 20))	
		.withSchedule(CronScheduleBuilder.cronSchedule("30 " + (min + 1) + " " + hour + " * * ? *"))		
		.build(); 		
		
		// schedule the jobs
    	try {
    		
    		quartzScheduler.scheduleJob(jobDetail1, trigger1);
    		quartzScheduler.scheduleJob(jobDetail2, trigger2);
    					
			Set<JobKey> jobKeys =  quartzScheduler.getJobKeys(GroupMatcher.jobGroupEquals("job-daily-"+"lighting-on"));
			for(JobKey jobKey: jobKeys){
				System.out.println("222--------------  jobkey   "+jobKey.getGroup()+"   "+jobKey.getName());
			}
			
			jobKeys =  quartzScheduler.getJobKeys(GroupMatcher.jobGroupStartsWith("job-daily-"));
			for(JobKey jobKey: jobKeys){
				System.out.println("333--------------  jobkey   "+jobKey.getGroup()+"   "+jobKey.getName());
			}			
					
			
		} catch (SchedulerException e) {
			System.out.println("fuck you  fuck"+e.getMessage());
			e.printStackTrace();
			
		}     	
    }	
}
