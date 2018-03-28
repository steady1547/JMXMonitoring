package com.linebot.springboot.test;

import java.lang.management.MemoryUsage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.RuntimeMBeanException;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;



import com.status.springboot.core.MBeanMonitoring;
import com.status.springboot.core.type.MBeanObjectType;
import com.status.springboot.model.MemoryPool;
import com.status.springboot.model.OperatingSystem;
import com.status.springboot.model.ServerInfo;
import com.status.springboot.support.CalcUtil;


public class Test2 {
	public void OldGenUsage() {
		try {
			Class<?> clazz = Class.forName("com.status.springboot.model.MemoryPool");
			Object instance = clazz.newInstance();
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://10.113.132.154:18001/jmxrmi");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			
			MBeanServerConnection mbs = jmxc.getMBeanServerConnection();
			ObjectName name = new ObjectName("java.lang:type=MemoryPool,name=PS Old Gen");
			MBeanInfo mBeanInfo = mbs.getMBeanInfo(name);
			MBeanAttributeInfo[] attrInfos = mBeanInfo.getAttributes();
   
			for (MBeanAttributeInfo attrInfo : attrInfos) {
				Field f =  instance.getClass().getDeclaredField(attrInfo.getName());
				Class<?> fieldClass = Class.forName(f.getType().getCanonicalName());
				Method m = instance.getClass().getDeclaredMethod("set"+attrInfo.getName(), fieldClass);
				Object attr = mbs.getAttribute(name, attrInfo.getName());
				
				if(attr instanceof CompositeData){
					m.invoke(instance, MemoryUsage.from((CompositeData)attr));
		   	 	}else if(attr instanceof String) {
		   	 		m.invoke(instance, String.valueOf(attr));
		   	 	}else if (attr instanceof Boolean){
		   	 		m.invoke(instance, Boolean.getBoolean(String.valueOf(attr)));
		   	 	}else if(attr instanceof Long){
		   	 		m.invoke(instance, Long.parseLong(String.valueOf(attr)));
		   	 	}else if(attr instanceof ObjectName){
		   	 		System.out.println(String.valueOf(attr));
		   	 		m.invoke(instance, new ObjectName(String.valueOf(attr)));
		   	 	}else if(attr instanceof Integer){
		   	 		m.invoke(instance, Integer.getInteger(String.valueOf(attr)));
		   	 	}else{
			   		 System.out.format("[invoke]  unsupport class type : %s, Type : %s\n", m.getName(), instance.getClass());
		   	 	}
			}
			MemoryPool pool = (MemoryPool)instance;
			System.out.println(pool);

			Double usage = CalcUtil.usagePercent(pool);
			System.out.format("Old Generation Usage Percent %4.3f\n",usage);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * OperatingSystem 정보를 조회
	 * OpenFileDescrptorCount를 조회하낟.
	 * @throws ClassNotFoundException 
	 */
	public void printAttributeFieldInfo() throws ClassNotFoundException{
		MBeanAttributeInfo[] attrInfos = null;
		MBeanServerConnection mbs = null ;
		ObjectName name  = null ;
		try {
			Class<?> clazz = Class.forName("com.status.springboot.model.MemoryPool");
			Object instance = clazz.newInstance();
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://10.113.132.154:18001/jmxrmi");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			
			mbs = jmxc.getMBeanServerConnection();
			name = new ObjectName("java.lang:type=MemoryPool,name=PS Eden Space");
			MBeanInfo mBeanInfo = mbs.getMBeanInfo(name);
			attrInfos = mBeanInfo.getAttributes();
			
			for (MBeanAttributeInfo attrInfo : attrInfos) {
				Field f =  instance.getClass().getDeclaredField(attrInfo.getName());
				System.out.println(f.getType().getName());
				//Class<?> fieldClass = Class.forName(f.getType().getCanonicalName());
				Class<?> fieldClass = Class.forName(f.getType().getName());
				Method m = instance.getClass().getDeclaredMethod("set"+attrInfo.getName(), fieldClass);
				
				try{
					Object attr = mbs.getAttribute(name, attrInfo.getName());
					System.out.println(attr.getClass()+"\t"+attrInfo.getName()+"\t"+attr.toString());
					System.out.println();

					if(attr instanceof CompositeData){
						m.invoke(instance, MemoryUsage.from((CompositeData)attr));
			   	 	}else if(attr instanceof String) {
			   	 		m.invoke(instance, String.valueOf(attr));
			   	 	}else if (attr instanceof Boolean){
			   	 		m.invoke(instance, Boolean.getBoolean(String.valueOf(attr)));
			   	 	}else if(attr instanceof Long){
			   	 		m.invoke(instance, Long.parseLong(String.valueOf(attr)));
			   	 	}else if(attr instanceof Double){
			   	 		m.invoke(instance, Double.parseDouble(String.valueOf(attr)));
			   	 	}else if(attr instanceof ObjectName){
			   	 		m.invoke(instance, new ObjectName(String.valueOf(attr)));
			   	 	}else if(attr instanceof Integer){
			   	 		m.invoke(instance, Integer.getInteger(String.valueOf(attr)));
			   	 	}else if(attr instanceof String[]){
				   	 	Object[] param = {attr};
			   	 		m.invoke(instance, param);
			   	 	}
			   	 	else{
						System.out.println(attr.getClass()+"\t"+attrInfo.getName()+"\t"+attr.toString());
						System.out.format("[invoke]  unsupport class type : %s, Type : %s\n", m.getName(), instance.getClass());
			   	 	}
				}catch(RuntimeMBeanException e){
					if((e.getCause() instanceof UnsupportedOperationException)){
						System.out.println(name+"this attribute not applicable.(Value : [N/A])");
					}
				}
			}
			MemoryPool pool = (MemoryPool)instance;
			System.out.println(pool);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	
	}
	
	public void os() {
		try {
			String type = "OperatingSystem";
			Class<?> clazz = Class.forName("com.status.springboot.model."+type);
			Object instance = clazz.newInstance();
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://10.113.132.154:18001/jmxrmi");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			
			MBeanServerConnection mbs = jmxc.getMBeanServerConnection();
			ObjectName name = new ObjectName("java.lang:type="+type);
			MBeanInfo mBeanInfo = mbs.getMBeanInfo(name);
			MBeanAttributeInfo[] attrInfos = mBeanInfo.getAttributes();
   
			for (MBeanAttributeInfo attrInfo : attrInfos) {
				Field f =  instance.getClass().getDeclaredField(attrInfo.getName());
				Class<?> fieldClass = Class.forName(f.getType().getCanonicalName());
				Method m = instance.getClass().getDeclaredMethod("set"+attrInfo.getName(), fieldClass);
				
				try{
					Object attr = mbs.getAttribute(name, attrInfo.getName());
					if(attr instanceof CompositeData){
						m.invoke(instance, MemoryUsage.from((CompositeData)attr));
			   	 	}else if(attr instanceof String) {
			   	 		m.invoke(instance, String.valueOf(attr));
			   	 	}else if (attr instanceof Boolean){
			   	 		m.invoke(instance, Boolean.getBoolean(String.valueOf(attr)));
			   	 	}else if(attr instanceof Long){
			   	 		m.invoke(instance, Long.parseLong(String.valueOf(attr)));
			   	 	}else if(attr instanceof Double){
			   	 		m.invoke(instance, Double.parseDouble(String.valueOf(attr)));
			   	 	}else if(attr instanceof ObjectName){
			   	 		m.invoke(instance, new ObjectName(String.valueOf(attr)));
			   	 	}else if(attr instanceof Integer){
			   	 		m.invoke(instance, Integer.getInteger(String.valueOf(attr)));
			   	 	}else{
						System.out.println(attr.getClass()+"\t"+attrInfo.getName()+"\t"+attr.toString());
						System.out.format("[invoke]  unsupport class type : %s, Type : %s\n", m.getName(), instance.getClass());
			   	 	}
				}catch(RuntimeMBeanException e){
					if((e.getCause() instanceof UnsupportedOperationException)){
						System.out.println(name+"this attribute not applicable.(Value : [N/A])");
					}
				}
			}
			OperatingSystem pool = (OperatingSystem)instance;
			System.out.println(pool);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void getOs()throws Exception{
		JMXServiceURL url = new JMXServiceURL(
             "service:jmx:rmi:///jndi/rmi://10.113.132.154:18001/jmxrmi");
		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		MBeanServerConnection mbs = jmxc.getMBeanServerConnection();
		ServerInfo info = new  ServerInfo();
		info.setHostname("test host name");
		info.setIp("10.113.132.154");
		info.setPort(18001);
		MBeanMonitoring mm = new MBeanMonitoring(info, MBeanObjectType.OPERTATING_SYSTEM);
		mm.init(mbs);
		OperatingSystem pool = mm.monitoring();
		       
		System.out.println(pool);
	}
	
	
	public void getMemoryPool() throws Exception{
		JMXServiceURL url = new JMXServiceURL (
	             "service:jmx:rmi:///jndi/rmi://10.113.132.154:18001/jmxrmi");
		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		MBeanServerConnection mbs = jmxc.getMBeanServerConnection();
		ServerInfo info = new  ServerInfo();
		info.setHostname("test host name");
		info.setIp("10.113.132.154");
		info.setPort(18001);
		MBeanMonitoring mm = new MBeanMonitoring(info, MBeanObjectType.MEMORY_OLD_GEN);
		mm.init(mbs);
		MemoryPool pool = mm.monitoring();
		       
		System.out.println(pool);
	}
	@Test
	public void getObjectName()throws Exception{
		System.out.println("====================================================================");
		ObjectName on = new ObjectName("java.lang:type=MemoryPool,name=PS Eden Space");
		System.out.println(on.getCanonicalKeyPropertyListString());
		System.out.println(on.getCanonicalName());
		System.out.println(on.getDomain());
		System.out.println(on.getKeyPropertyListString());
		System.out.println(on.getKeyProperty("type"));
		System.out.println(on.getKeyProperty("name"));
		System.out.println("====================================================================");
		ObjectName d =  new ObjectName("java.lang:type=OperatingSystem");
		System.out.println(d.getCanonicalKeyPropertyListString());
		System.out.println(d.getCanonicalName());
		System.out.println(d.getDomain());
		System.out.println(d.getKeyPropertyListString());
		System.out.println(d.getKeyProperty("type"));
		System.out.println(d.getKeyProperty("name"));
		System.out.println("====================================================================");
		StringBuilder sb = new StringBuilder(d.getKeyProperty("type"));
		
		if(!StringUtils.isEmpty(d.getKeyProperty("name"))){
			sb.append(":");
			sb.append(d.getKeyProperty("name"));
		}
		System.out.println(sb.toString());
	}
	
	private String getObjectNameString(ObjectName objectname){
		StringBuilder sb = new StringBuilder(objectname.getKeyProperty("type"));
		
		if(!StringUtils.isEmpty(objectname.getKeyProperty("name"))){
			sb.append(":").append(objectname.getKeyProperty("name"));
		}
		return getReplaceWhiteSpace(sb.toString());
	}
	
	private String getReplaceWhiteSpace(String text){
		return text.replaceAll(" ","_");
	}
	
	public void getPrtm(){
		try {
		JMXServiceURL url = new JMXServiceURL(
	             "service:jmx:rmi:///jndi/rmi://10.113.132.154:18001/jmxrmi");
	       JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
	       MBeanServerConnection mbs = jmxc.getMBeanServerConnection();
	       ServerInfo info = new  ServerInfo();
	       info.setHostname("test host name");
	       info.setIp("10.113.132.154");
	       info.setPort(18001);
	       MBeanMonitoring mm = new MBeanMonitoring(info, MBeanObjectType.MEMORY_EDEN_SPACE);
	       mm.init(mbs);
	       MemoryPool pool = mm.monitoring();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void modelparserTest(){
		MemoryPool mem = new MemoryPool();
		mem.setIp("10.10.10.10");
		mem.setHostname("test001");
		this.generateTags(mem);
		
	}
	
	private <T> void generateTags(T model){
		MemoryPool info = (MemoryPool)model;
		System.out.println(">>> "+info.getIp());
		System.out.println(">>> "+info.getHostname());
	}
	
	private <T> void generateValues(T model){
		MemoryPool info = (MemoryPool)model;
		MemoryUsage usage = MemoryUsage.from((CompositeData)info.getUsage());
		usage.getUsed();
		usage.getMax();
	}
}

