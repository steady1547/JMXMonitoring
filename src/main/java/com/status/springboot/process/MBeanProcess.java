package com.status.springboot.process;

import org.springframework.stereotype.Component;

import com.status.springboot.core.MBeanMonitoring;
import com.status.springboot.core.type.MBeanObjectType;
import com.status.springboot.model.MemoryPool;
import com.status.springboot.model.OperatingSystem;
import com.status.springboot.pool.model.Connector;


@Component
public class MBeanProcess {
	
	private <T> T process(Connector connector, MBeanObjectType type) throws Exception{
		return new MBeanMonitoring(connector, type).monitoring();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public OperatingSystem getOperatingSystem(Connector connector) throws Exception{
		/**
		 * findAll 
		 * OperatingSystem 
		 */
		return process(connector, MBeanObjectType.OPERTATING_SYSTEM);
	}
	
	public MemoryPool getMemoryPool(Connector connector, MBeanObjectType type) throws Exception{
		return process(connector, type);
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public MemoryPool oldGenMonitor(Connector connector) throws Exception{
		/**
		 * findAll 
		 * JVM Old 영역을 모니터링
		 */
		return process(connector, MBeanObjectType.MEMORY_OLD_GEN);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public MemoryPool permGenMonitor(Connector connector) throws Exception{
		/**
		 * findAll 
		 * JVM Perm 영역을 모니터링 
		 */
		return process(connector, MBeanObjectType.MEMORY_PERM_GEN);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public MemoryPool edenGenMonitor(Connector connector) throws Exception{
		/**
		 * findAll 
		 * JVM eden 영역을 모니터링 .
		 */
		return process(connector, MBeanObjectType.MEMORY_EDEN_SPACE);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public MemoryPool survivorGenMonitor(Connector connector) throws Exception{
		/**
		 * findAll 
		 * JVM sruvivor 영역을 모니터링
		 */
		return process(connector, MBeanObjectType.MEMORY_SURVIVOR_SPACE);
	}
	
	
	
}



