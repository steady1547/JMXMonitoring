package com.status.springboot.core;

import java.lang.management.MemoryUsage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.status.springboot.core.type.MBeanObjectType;
import com.status.springboot.model.ServerInfo;

/**
 * Model Reflection
 * MBean attribute의 Class Type을 사용자 정의한 MBeanModel 클래스로 invoke를 수행한다.
 * @author ys
 *
 */
public class MonitoringReflection {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Object instance;
	private String fieldName;
	private String className;
	private Class<?> modelClass;
	private Class<?> fieldClass;
	private Method method;
	private String MODEL_PACKAGE_HOME = "com.status.springboot.model.";
	
	/*
	 * TODO Custom Exception정의
	 */

	/**
	 * Model Class Instance를 생성한다.
	 * @param modelClassName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static MonitoringReflection getInstance(String modelClassName) throws NoSuchFieldException, SecurityException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException{
		return new MonitoringReflection(modelClassName);
	}
	
	public static MonitoringReflection getInstance(MBeanObjectType objecType) throws NoSuchFieldException, SecurityException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException{
		return getInstance(objecType.getObjectTypeValue());
	}
	
	/**
	 * Model Class의 Field의 Class Type과 Setter Method를 초기화 한다. 
	 * @param fieldName
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public void initMethod(String fieldName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException, NoSuchMethodException{
		this.fieldName = fieldName;
		this.fieldClass  = getFieldClass();
		this.method    = getSetterMethod();
	}
	
	/** private method area **/
	/**
	 * private construction
	 * @param className
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private MonitoringReflection(String className) throws NoSuchFieldException, SecurityException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException{
		init(className);
	};
	
	/**
	 * Model Class Instance를 생성한다.
	 * @param className
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	private void init(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException, NoSuchMethodException{
		this.instance  = getModelClass(className).newInstance();
	}
	
	/**
	 *	 
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	private  Class<?> getModelClass(String className) throws ClassNotFoundException{
		return Class.forName(MODEL_PACKAGE_HOME+className);
	}
	
	/**
	 * Filed 클래스 타입을 반환
	 * @param instance
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 */
	private Class<?> getFieldClass() throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		return  Class.forName(getFieldClassName());
	}
	
	/**
	 * Filed 클래스 타입을 반환
	 * @param instance
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 */
	private Class<?> getSuperFieldClass(String superFieldName) throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		return  Class.forName(getSuperFieldClassName(superFieldName));
	}
	
	/**
	 * Field의 Class Type Name을 반환한다.
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private String getFieldClassName() throws NoSuchFieldException, SecurityException{
		return this.instance.getClass().getDeclaredField(this.fieldName).getType().getName();
	}
	
	/**
	 * Field의 Class Type Name을 반환한다.
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private String getSuperFieldClassName(String superFieldName) throws NoSuchFieldException, SecurityException{
		return this.instance.getClass().getField(superFieldName.toLowerCase()).getType().getCanonicalName();
	}
	
	/**
	 * model의 Setter method를 반환
	 * @param instance
	 * @param filedName
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws ClassNotFoundException
	 */
	private Method getSetterMethod() throws NoSuchMethodException, SecurityException, NoSuchFieldException, ClassNotFoundException{
		 return instance.getClass().getDeclaredMethod("set"+this.fieldName, getFieldClass());
	}
	
	private Method getMethod(String name, String superFieldName) throws NoSuchMethodException, SecurityException, NoSuchFieldException, ClassNotFoundException{
		 return instance.getClass().getMethod(name, getSuperFieldClass(superFieldName));
	}
	
	/**
	 * MBean Attribute Class Type에 맞게 형변환하여 invoke를 수행한다.
	 * 공통으로 사용할 수 있도록 처리 해놓았지만, Mbean의 MemoryPool을 기반으로 처리 하였음 
	 * 
	 * 추가 형변환이 필요한 타입은 아래 분기 추가
	 *  
	 * @param method
	 * @param instance
	 * @param attr
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws MalformedObjectNameException 
	 */
	public void invoke(Object attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedObjectNameException{
  		 invoke(this.method, attribute);
	}
	
	public void invoke(Method m, Object o) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedObjectNameException{
		if(o instanceof CompositeData){
			m.invoke(instance, MemoryUsage.from((CompositeData)o));
   	 	}else if(o instanceof String) {
   	 		m.invoke(instance, String.valueOf(o));
   	 	}else if (o instanceof Boolean){
   	 		m.invoke(instance, Boolean.getBoolean(String.valueOf(o)));
   	 	}else if(o instanceof Long){
   	 		m.invoke(instance, Long.parseLong(String.valueOf(o)));
   	 	}else if(o instanceof Double){
   	 		m.invoke(instance, Double.parseDouble(String.valueOf(o)));
   	 	}else if(o instanceof ObjectName){
   	 		m.invoke(instance, new ObjectName(String.valueOf(o)));
   	 	}else if(o instanceof Integer){
   	 		m.invoke(instance, Integer.parseInt(String.valueOf(o)));
   	 	}else if(o instanceof String[]){
	   	 	Object[] param = {o};
	 		m.invoke(instance, param);
	 	}else{
   	 		/**
   	 		 * 형변환하기 까다로운 타입들인데, 메이저 옵션이 아닐 경우 이정도로만 
   	 		 */
   	 		logger.warn("[invoke]  unsupport class type : {} , Type : {}", method.getName(), o.getClass());
   	 	}
	}
	
	/**
	 * ServerInfo 상속받은 MemoryPool 
	 * 부모 Method invoke
	 * @param info
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws MalformedObjectNameException 
	 */
	public void serverInfoInvoke(ServerInfo info) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, InstantiationException, ClassNotFoundException, MalformedObjectNameException{
		String [] setterFieldList = {"Ip", "Hostname", "Port"};
		for(String fieldName  : setterFieldList){
			Object o = info.getClass().getMethod("get"+fieldName).invoke(info);
			this.invoke(instance.getClass().getMethod("set"+fieldName,  o.getClass()), o);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getModel(){
		return (T) instance;
	}
}
