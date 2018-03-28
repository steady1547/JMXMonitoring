package com.status.springboot.support;

import java.lang.management.MemoryUsage;

import com.status.springboot.model.MemoryPool;

public class CalcUtil {
	
	
	public static Double usagePercent(MemoryPool memoryPool){
		MemoryUsage usage = memoryPool.getUsage();
		return usagePercent((double)usage.getUsed()
				,(double)memoryPool.getUsage().getMax());
	}
	
	public static Double usagePercent(Double used, Double max){
		return (used/max)*100;
	}

}
