package com.status.springboot.process;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.status.springboot.core.type.MBeanObjectType;
import com.status.springboot.model.MemoryPool;
import com.status.springboot.model.OperatingSystem;
import com.status.springboot.pool.MonitoringServerPool;
import com.status.springboot.pool.model.Connector;
import com.status.springboot.repository.ServerInfoRepository;
import com.status.springboot.service.InfluxDBService;
import com.status.springboot.support.CalcUtil;
import com.status.springboot.support.HttpRestTemplate;
import com.status.springboot.support.MonitorUtils;


@Service
public class MonitorClient implements Monitor{
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ServerInfoRepository repository;
	
	@Autowired
	private MBeanProcess mBeanProcess;
	
	@Autowired
	private HttpRestTemplate httpRestTemplate; 
	
	@Autowired
	private InfluxDBService influxDBService;
	
	
	private final String DB_NAME = "monitor";
	
	@Override
	public void osMonitor() {
		doProcess((connector)->{
			OperatingSystem os = mBeanProcess.getOperatingSystem(connector);
			logger.info("OperatingSystem - {}",os);
		});
	}
	
	
	@Override
	public void openFileDescriptorCountMonitor() {
		doProcess((connector)->{
			OperatingSystem os = mBeanProcess.getOperatingSystem(connector);
			logger.info("OperatingSystem - {}",os);
			StringBuilder bodyString = new StringBuilder("os_fd_count").append(",");
			bodyString.append("host=").append(os.getHostname());
	    	bodyString.append(",ip=").append(os.getIp());
	    	bodyString.append(",object_name=").append(MonitorUtils.getObjectNameTag(os.getObjectName()));
	    	bodyString.append(" fd_count=").append(os.getOpenFileDescriptorCount());
	    	
	    	/** 데이터 저장 **/
			String response = influxDBService.writingData(DB_NAME, bodyString.toString());
			
			logger.info("writingData # response : {}", response);
		});
	}
	
	@Override
	public void oldGenMonitor() {
		doProcess((connector)->{
			MemoryPool mem = mBeanProcess.getMemoryPool(connector, MBeanObjectType.MEMORY_OLD_GEN);
			logger.info("Old generation usage : {}%",CalcUtil.usagePercent(mem));
			mem.setUsagePercent(CalcUtil.usagePercent(mem));
			
			StringBuilder bodyString = new StringBuilder("mem_heap_usage").append(",");
	    	bodyString.append("host=").append(mem.getHostname());
	    	bodyString.append(",ip=").append(mem.getIp());
	    	bodyString.append(",object_name=").append(MonitorUtils.getObjectNameTag(mem.getObjectName()));
	    	bodyString.append(" max=").append(mem.getUsage().getMax());
	    	bodyString.append(",used=").append(mem.getUsage().getUsed());
			bodyString.append(",percent=").append(CalcUtil.usagePercent(mem));
			
			/** 데이터 저장 **/
			String response = influxDBService.writingData(DB_NAME, bodyString.toString());
			
			logger.info("writingData # response : {}", response);
		});
	}
	
	@Override
	public void permMonitor() {
		// TODO Auto-generated method stub
		doProcess((connector)->{
			MemoryPool mem = mBeanProcess.getMemoryPool(connector, MBeanObjectType.MEMORY_PERM_GEN);
			logger.info("Perm generation usage - {}",mem);
			logger.info("Perm generation usage : {}%",CalcUtil.usagePercent(mem));
		});
	} 
	
	@Override
	public void edenMonitor() {
		// TODO Auto-generated method stub
		doProcess((connector)->{
			MemoryPool mem = mBeanProcess.getMemoryPool(connector, MBeanObjectType.MEMORY_EDEN_SPACE);
			logger.info("eden generation usage - {}",mem);
			logger.info("eden generation usage : {}%",CalcUtil.usagePercent(mem));
		});
	}
	
	@Override
	public void survivordenMonitor() {
		// TODO Auto-generated method stub
		doProcess((connector)->{
			MemoryPool mem = mBeanProcess.getMemoryPool(connector, MBeanObjectType.MEMORY_SURVIVOR_SPACE);
			logger.info("survivor generation usage - {}",mem);
			logger.info("survivor generation usage : {}%",CalcUtil.usagePercent(mem));
		});
	}
	
	private void doProcess(Process process){
		MonitoringServerPool pool = MonitoringServerPool.getInstance();
		Map<String, Connector> map = pool.getPoolMap();
		map.forEach((k,v)->{
				try {
					process.doProcess(v);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		});
	}
}
