package com.status.springboot.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeMBeanException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.status.springboot.core.support.JMXUtil;
import com.status.springboot.core.type.MBeanObjectType;
import com.status.springboot.model.ServerInfo;
import com.status.springboot.pool.model.Connector;

public class MBeanMonitoring {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MBeanObjectType objecType;
	private ObjectName name ;
	private MBeanServerConnection connection;
	private MBeanInfo mBeanInfo;
	private boolean init = false;
	private ServerInfo info;
	
	/**
	 * 생성자 init 생략 가능하다.
	 * @param connector
	 * @param type
	 * @throws Exception 
	 */
	public MBeanMonitoring(Connector connector, MBeanObjectType type) throws Exception{
		this.objecType = type;
		this.info = connector.getInfo();
		init(connector.getConnector());
	}

	public MBeanMonitoring(ServerInfo info, MBeanObjectType objecType) {
		this.objecType = objecType;
		this.info = info;
	}
	
	public void init(MBeanServerConnection connection) throws Exception{
		if(init) throw new Exception("already initialized");
		try{
			this.connection = connection;
			this.name = JMXUtil.getMBeanObjectName(objecType);
			this.mBeanInfo = this.connection.getMBeanInfo(name);
			this.init = true;
		}catch(ConnectException e){
			logger.error("Connection refused {} ", this.info.getIp());
			/**
			 * re connection 
			 */
			throw e;
		}
	}

	public <T> T monitoring() throws Exception{
		if(!init) throw new Exception("not initialized yet"); 
	    return this.invoke(MonitoringReflection.getInstance(objecType));
	}
	
	private <T> T  invoke(MonitoringReflection reflection) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException, NoSuchMethodException, AttributeNotFoundException, InstanceNotFoundException, IllegalArgumentException, InvocationTargetException, MBeanException, ReflectionException, IOException, MalformedObjectNameException{
	       MBeanAttributeInfo[] attrInfos = mBeanInfo.getAttributes();
	       reflection.serverInfoInvoke(info);
	       for (MBeanAttributeInfo attrInfo : attrInfos) {
	    	   reflection.initMethod(attrInfo.getName());
	    	   try{
	    		   reflection.invoke(this.connection.getAttribute(this.name, attrInfo.getName()));
	    	   }catch(RuntimeMBeanException e){
	    		    //Value가 타입과 무관하게 미사용(Value =[N/A])인 케이스가 존재한다.
					if(e.getCause() instanceof UnsupportedOperationException){
						logger.info("[{} - {}] this attribute not applicable.(Value : [N/A])",this.name,attrInfo.getName());
					}else{
						throw e;
					}
				}
	       }
	       return reflection.getModel();
	}
}
