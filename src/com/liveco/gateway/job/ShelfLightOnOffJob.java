package com.liveco.gateway.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.system.ShelfLightingSystem;
import com.liveco.gateway.system.SystemRepository;

public class ShelfLightOnOffJob implements Job {
	private static int count;

	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		System.out.println("--------------------------------------------------------------------");
		System.out.println("MyJob start: " + jobContext.getFireTime());
		JobDetail jobDetail = jobContext.getJobDetail();
		System.out.println("Example name is: "+ "  "+jobDetail.getJobDataMap().getString("event"));		
		System.out.println("MyJob end: " + jobContext.getJobRunTime() + ", key: " + jobDetail.getKey());
		System.out.println("MyJob next scheduled time: " + jobContext.getNextFireTime());
		System.out.println("--------------------------------------------------------------------");
		
		
		JobDataMap data = jobDetail.getJobDataMap();
		int system_index =  data.getInt("system-index");
		String system_type =  data.getString("system-type");
		String event_type =  data.getString("event");
		
		System.out.println(system_index+"  "+system_type+"   "+event_type);
		
		SystemRepository repository = (SystemRepository) data.get("repository");
		if(repository!=null){
			//System.out.println(repository.getSystemStatus());
			
			ShelfLightingSystem system;
	    	try{
	    		system = (ShelfLightingSystem) repository.findSystem(SystemStructure.SHELF_LIGHTING_SYSTEM,   system_index);
	    		if(event_type == "open"){
	    			system.controlAllOn();
	    		}else if(event_type == "close"){
	    			system.controlAllOff();
	    		}
	    	}catch(java.lang.IndexOutOfBoundsException e){
	    		e.printStackTrace();
	    	} catch (AdsException e) {
				e.printStackTrace();
			}			
			
		}
		
	}

}
