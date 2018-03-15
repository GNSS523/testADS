package com.liveco.gateway.system;

import com.liveco.gateway.plc.AdsException;

public abstract class SystemListener {

	public abstract void onStartUP() throws InterruptedException;
	public abstract void onShutDown() throws InterruptedException;
	public abstract void onChangeToRunningMode() throws AdsException;
	public abstract void onChangeToMaintainceMode()throws AdsException; 
	
}
