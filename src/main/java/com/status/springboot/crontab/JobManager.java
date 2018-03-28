package com.status.springboot.crontab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.status.springboot.process.Monitor;

@Component
public class JobManager {
	static final Logger LOG = LoggerFactory.getLogger(JobManager.class);
	@Autowired
	Monitor client;
	
	// 매일 5시 30분 0초에 실행한다. 
	@Scheduled(cron = "0 30 5 * * *") public void aJob() { 
		// 실행될 로직 
	}
	
	/**
	 * 애플리케이션 시작 후 initialDelay(ms) 후에 첫 실행, 그 후 매 fixedDelay(ms)마다 주기적으로 실행한다. 
	 * 1시간
	 */
	//@Scheduled(initialDelay = 6000000, fixedDelay = 6000000)
	@Scheduled(initialDelay = 6000, fixedDelay = 60000)
	public void oldGenMonitor() {  
		client.oldGenMonitor();
	}
	
	@Scheduled(initialDelay = 6000, fixedDelay = 60000)
	public void openFileDescriptorCountMonitor() {  
		client.openFileDescriptorCountMonitor();
	}
	
	/*@Scheduled(initialDelay = 6000, fixedDelay = 6000)
	public void permJob() {  
		client.permMonitor();
	}
	
	@Scheduled(initialDelay = 6000, fixedDelay = 6000)
	public void edenJob() {  
		client.edenMonitor();
	}
	
	@Scheduled(initialDelay = 6000, fixedDelay = 6000)
	public void survivorJob() {  
		client.survivordenMonitor();
	}*/
	
	/*@Scheduled(initialDelay = 6000, fixedDelay = 6000)
	public void osJob() {  
		client.osMonitor();
	}*/
	
	/*
	 * push test 
	 * 1분
	 */
	@Scheduled(initialDelay = 6000000, fixedDelay = 6000000)
	public void cJob() {  
	}
}
