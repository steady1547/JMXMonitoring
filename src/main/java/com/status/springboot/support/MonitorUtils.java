package com.status.springboot.support;

import javax.management.ObjectName;

import org.apache.commons.lang3.StringUtils;

public class MonitorUtils {
	public static String getObjectNameTag(ObjectName objectName){
		StringBuilder sb = new StringBuilder(objectName.getKeyProperty("type"));
		if(!StringUtils.isEmpty(objectName.getKeyProperty("name"))){
			sb.append(":").append(objectName.getKeyProperty("name"));
		}
		return getReplaceWhiteSpace(sb.toString());
	}
	
	private static String getReplaceWhiteSpace(String text){
		return text.replaceAll(" ","_");
	}
}
