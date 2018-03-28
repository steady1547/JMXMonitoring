package com.status.springboot.core.support;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.springframework.util.ObjectUtils;

import com.status.springboot.core.type.MBeanObjectType;
import com.status.springboot.core.type.MBeanTree;

public class JMXUtil {
	
	public static ObjectName getMBeanObjectName(MBeanObjectType objType) throws MalformedObjectNameException{
		StringBuilder sb = new StringBuilder(MBeanTree.JAVA_LANG.getValue())
		.append(":type=").append(objType.getObjectTypeValue());
		
		if(!ObjectUtils.isEmpty(objType.getObjectSubNameValue()))
			sb.append(",name=").append(objType.getObjectSubNameValue());
		
		return new ObjectName(sb.toString());
	}
}
