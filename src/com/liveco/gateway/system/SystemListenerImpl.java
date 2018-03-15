package com.liveco.gateway.system;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.DateBuilder;
import org.quartz.SchedulerException;

import com.liveco.gateway.constant.HydroponicsConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.job.SystemHandler;
import com.liveco.gateway.plc.AdsException;

public class SystemListenerImpl extends SystemListener{

    private SystemRepository repository;
    private SystemHandler systemScheduleHandler;
    
	public SystemListenerImpl(SystemRepository repository ,  SystemHandler systemScheduleHandler){
		this.repository = repository;
		this.systemScheduleHandler = systemScheduleHandler;
	}

	@Override
	public void onStartUP() throws InterruptedException {
		
		Thread startup = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					
					Calendar rightNow = Calendar.getInstance();
					int hour = rightNow.get(Calendar.HOUR_OF_DAY);
					int min = rightNow.get(Calendar.MINUTE);					
					Date startTime = DateBuilder.todayAt(hour, min+1, 0);
					Date closeTime =  DateBuilder.todayAt(hour, min+5, 0);
					
					System.out.println("on startup thread");
					systemScheduleHandler.getDailySchedules();
					systemScheduleHandler.getSystemSchedules(SystemStructure.SHELF_LIGHTING_SYSTEM);
					
					//systemScheduleHandler.scheduleRange(SystemStructure.SHELF_LIGHTING_SYSTEM,3,MyJob.class, startTime,closeTime);
					//systemScheduleHandler.scheduleRange(SystemStructure.SHELF_LIGHTING_SYSTEM,4,MyJob.class, startTime,closeTime);
					
										
					systemScheduleHandler.getDailySchedules();
					systemScheduleHandler.getSystemSchedules(SystemStructure.SHELF_LIGHTING_SYSTEM);
					
				} catch (SchedulerException e) {
					e.printStackTrace();
				}			
			}			
		});
		startup.sleep(200);
		startup.start();
		
	}
	
	@Override
	public void onShutDown() throws InterruptedException {
		if(repository !=null){
			try {
				this.repository.getADSConnection().closePort();
				System.exit(0);
			} catch (AdsException e) {
				e.printStackTrace();
			}			
		}

	}
	
	@Override
	public void onChangeToRunningMode() throws AdsException {
		if(repository !=null){
			List list = repository.getHydroponicsSystems();
			HydroponicsSystem hydro = null;
			for(int i = 0;i< list.size();i++)
			{
				hydro = (HydroponicsSystem)list.get(i);
				hydro.setMode(HydroponicsConstant.ModeCommand.RUNNING);
			}
			 	
			
			list = repository.getFogponicsSystems();
			FogponicsSystem fogpo = null;
			for(int i = 0;i< list.size();i++){
				fogpo = (FogponicsSystem)list.get(i);
				fogpo.setMode(HydroponicsConstant.ModeCommand.RUNNING);        		
			}
		}
	}
	
	@Override
	public void onChangeToMaintainceMode() throws AdsException {
		
		if(repository == null) return;
		
		List list = repository.getHydroponicsSystems();
		HydroponicsSystem hydro = null;
		for(int i = 0;i< list.size();i++)
		{
			hydro = (HydroponicsSystem)list.get(i);
			hydro.setMode(HydroponicsConstant.ModeCommand.MANUAL);
		}
		 	
		list = repository.getFogponicsSystems();
		FogponicsSystem fogpo = null;
		for(int i = 0;i< list.size();i++){
			fogpo = (FogponicsSystem)list.get(i);
			fogpo.setMode(HydroponicsConstant.ModeCommand.MANUAL);        		
		} 
	}
	
	
}