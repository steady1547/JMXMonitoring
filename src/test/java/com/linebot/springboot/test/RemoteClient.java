package com.linebot.springboot.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.MemoryUsage;
import java.net.URLEncoder;
import java.util.Set;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class InfluxDBProcess{
	RestTemplate rt ;
	
	private class DBInfo{
		final static String DB_URL = "http://localhost:8086";
		final static String WRITE_PATH = "/write";
		final static String WRITE_URL = DB_URL + WRITE_PATH;
		final static String DATABASE_NAME = "mydb";  
				
	}
	
	InfluxDBProcess(){
		rt = new RestTemplate();
	}
	
	/**
			1) heap_status
			- tags
				- host : hostname
				- addr : ip address
				- obj_type  : MBean ObjectType(java.lang:type={$objectType},name={$objectName})
			- values
				- max : 최대값 
				- used : 사용량
				- percent : 사용률
			@ sample 
				> heap_status,host=api001,addr=10.10.1.1,obj=Memory max=100,usage=10,percent=10
		  
			2) jvm_status
				- tags
					- host : hostname
					- addr : ip addr
					- obj_type  : MBean ObjectType(java.lang:type={$objectType},name={$objectName})
					- obj_name	: Mbean ObjectName(java.lang:type={$objectType},name={$objectName})
					- type : obj_name 타입 명
						- 영역별 타입명 지정(OLD, PERM, EDEN,SURV)
				- value
					- max : 최대값 
					- used : 사용량
					- percent : 사용률
			@ sample
				> jvm_status,host=api001,addr=10.10.1.1,obj=MemoryPool,name=CMS_Old_Gen,type=OLD usage=30,max=100,percent=30 
		  
			3) buffer_status
				- tags
					- host : hostname
					- addr : ip address
				- values
					- count : And estimate of the number of direct buffers in the pool
					- total_capacity : An etimate of the total capacity of the buffers in this pool
					- memory_used : An etimate of the memory that the JVM is using for this buffer pool
			@ sample
				> buffer_status,host=api001,addr=10.10.1.1,obj=BufferPool,type=DIRECT count=412,mem_used=10.6,total_capacity=10.6
				
			4) fd_status
				- tags
					- host : hostname
					- addr : ip address
					- obj_type : 고정 값 OperatingSystem 
						> MBean ObjectType(java.lang:type={$objectType},name={$objectName})
				- values
					- count : OpenFileDescriptorCount
					
			@ sample
				> fd_status,host=api001,addr=10.10.10.1,obj_type=OperatingSystem count=3000
	 */
	
	
	/**
	 * heap_status,host=api001,addr=10.10.1.1,obj=Memory max=100,usage=10,percent=10
	 */
	public void writeHeapStatus(){
		//WRITE_URL
		
		//String url = WRITE_URL;
		
	}
	
	/**
	 * jvm_status,host=api001,addr=10.10.1.1,obj=MemoryPool,name=CMS_Old_Gen,type=OLD usage=30,max=100,percent=30
	 */
	public void wirteJvmStatus(){
		
	}
	
	/**
	 * fd_status,host=api001,addr=10.10.10.1,obj_type=OperatingSystem count=3000
	 */
	public void wirteFdStatus(){
		
	}
	
	public void write(){//heap_status,host=api001,addr='10.10.10.1',obj_name='java.lang:type' max='100',usage='30',percent='30'
		try{
		StringBuffer url = new StringBuffer(DBInfo.WRITE_URL)
		.append("?db=").append(DBInfo.DATABASE_NAME);
		//String response = this.post(url.toString(), "cpu_load_short,host=server9999,region=us-west value=0.9999");
		String response = this.post(url.toString(), "heap_status,host=api001,addr=10.10.10.1,obj_name=java.lang:type max=100,usage=30,percent=30%");
		System.out.println(response);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String encode(String paramString) throws UnsupportedEncodingException{
		return URLEncoder.encode(paramString, "UTF8");
	}
	
	private HttpEntity<String> getEntity(String body){
		return new HttpEntity<String>(body);
	}
	
	private String get(String url){
		return exchange(url, null, HttpMethod.GET);
	}
	
	private String post(String url, String body){
		return exchange(url, body, HttpMethod.POST);
	}
	
	private String exchange(String url, String body, HttpMethod method){
		ResponseEntity<String> response = rt.exchange(url, method, getEntity(body), String.class);
		System.out.println(response.getStatusCodeValue());
		System.out.println(response.getBody());
		return response.getBody();
	}
}

public class RemoteClient {
	Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@FunctionalInterface
	interface Process{
		public void doProdess() throws Exception;
	}
	
	
	
	private MBeanServerConnection mbsc = null;
	
	private String hostname;
	private String port;
	
	private final String OBJ_NAME_PRE_KEY = "java.lang:type=";
	@SuppressWarnings("unused")
	private RemoteClient(){};
	
	public RemoteClient(String hostname, String port){
		this.hostname = hostname;
		this.port = port;
	}
	
	public void init() throws Exception{
		/*
		 * init
		 * .........
		 */
		this.connect();
	}
	
	/**
	 * remote conn
	 * @throws Exception
	 */
	private void connect() throws Exception{
		if(StringUtils.isEmpty(hostname)|| StringUtils.isEmpty(port)){
			throw new Exception("empty remote info .. ");
		}

		JMXServiceURL url = new JMXServiceURL(getUrl(hostname, port));
	    JMXConnector jmxc = JMXConnectorFactory.connect(url);
	    
	    mbsc = jmxc.getMBeanServerConnection();
	}
	
	/**
	 * Memmory Pool
	 * @throws Exception
	 */
	public void getMemoryPool() throws Exception {
		doProcess(() -> {
			String [] attrArr = {"PeakUsage", "Usage", "CollectionUsage"};
			printMemoryInfo(getPools(getObjectName("MemoryPool")), true, attrArr);
		});
	}

	public void getHeapMemoryUsage() throws Exception{
		doProcess(() -> {
			String [] attrArr = {"HeapMemoryUsage","NonHeapMemoryUsage"};
			printMemoryInfo(getPools(getObjectName("Memory")), true, attrArr);
		});
	}
	
	public void getThreading()throws Exception{
		doProcess(() -> {
			String [] attrArr = {"ThreadCount"};
			printInfo(getPools(getObjectName("Threading")), true, attrArr);
		});
	}
	
	public void getGCTime() throws Exception{
		doProcess(() -> {
			String [] attrArr = {"CollectionTime", "CollectionCount"};
			printInfo(getPools(getObjectName("GarbageCollector")), true, attrArr);
		});
	}
	
	private void doProcess(Process process) throws Exception{
		/*
		 * .......
		 */
		process.doProdess();
	}
	
	private Set<ObjectName> getPools(String Mbeans) throws MalformedObjectNameException, IOException{
		return mbsc.queryNames(new ObjectName(Mbeans), null);
	}
	
	private void printInfo(Set<ObjectName> pools, boolean isMBeanInfo, String... attrName) throws Exception{
		for (ObjectName on : pools) {
			printInfo(on, true, attrName);
		}
	}
	
	private void printInfo(ObjectName objectName, boolean isMBeanInfo, String... attrName) throws Exception{
		if(isMBeanInfo) printMBeanInfo(mbsc.getMBeanInfo(objectName));
		for(String name : attrName){
			System.out.format("[CollectionTime]%d\n", mbsc.getAttribute(objectName, name));
		}
	}
	
	private void printMBeanInfo(MBeanInfo bean){
		System.out.format("[ClassName]=%s\n",bean.getClassName());
		System.out.format("[Descriptor]=%s\n",bean.getDescriptor());
		System.out.format("[Description]=%s\n",bean.getDescription());
	}
	
	private void printMemoryInfo(Set<ObjectName> pools, boolean isMBeanInfo, String... attrName) throws Exception{
		for (ObjectName on : pools) {
			printMemoryInfo(on, true, attrName);
		}
	}
	
	private void printMemoryInfo(ObjectName objectName, boolean isMBeanInfo, String... attrName) throws Exception{
		if(isMBeanInfo) printMBeanInfo(mbsc.getMBeanInfo(objectName));
		for(String name : attrName){
			System.out.format("%s\n", MemoryUsage.from((CompositeData)mbsc.getAttribute(objectName, name)));
		}
	}
	//public void getGabageCollector
	public static void main(String[] args) throws Exception {
		/*RemoteClient client = new RemoteClient("10.113.132.154","18001");
		client.init();
		client.getHeapMemoryUsage();
		client.getThreading();
		client.getGCTime();
		client.getMemoryPool();*/	
		
		InfluxDBProcess p = new InfluxDBProcess();
		p.write();
	}
	
	private String getObjectName(String value){
		return new StringBuilder(OBJ_NAME_PRE_KEY).append(value).append(",*").toString();
	}
	
	private String getUrl(String hostname, String port){
		return new StringBuilder("service:jmx:rmi:///jndi/rmi://")
		.append(hostname).append(":")
		.append(port).append("/jmxrmi").toString();
	}
	
	
	
	
	

}
