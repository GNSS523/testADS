package com.liveco.gateway.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.PanelLightingSystem;
import com.liveco.gateway.system.SystemRepository;

public class PanelLightControlJob implements Job {
	
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
			PanelLightingSystem temp_system = null;
			List list = repository.getPanelLightingSystems();
        	for(int i=0;i < list.size();i++){
           		
       		    temp_system = (PanelLightingSystem) list.get(i);
                try {               	
	    	    		if(event_type == "open"){
	    	    			temp_system.setLightIntensity(100,100,100,100,100);
	    	    		}else if(event_type == "close"){
	    	    			temp_system.setLightIntensity(0,0,0,0,0);
	    	    		}
               	 		
    				} catch (AdsException | DeviceTypeException e1) {
    					e1.printStackTrace();
    				}            		
        	}   			
			
		
			
		}
		
	}
}
