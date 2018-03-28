package com.status.springboot.pool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.status.springboot.model.ServerInfo;
import com.status.springboot.pool.model.Connector;

/**
 * 모니터링 객체 커넥션 풀
 * @author line play
 *
 */
public class MonitoringServerPool {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private volatile Map<String, Connector> pool = 
			Collections.synchronizedMap(new HashMap<String, Connector>());

	private volatile static MonitoringServerPool instance;
	private volatile boolean isInit = false;
	
	private MonitoringServerPool(){};
	
	public static MonitoringServerPool getInstance(){
		if(instance == null) {
			synchronized (MonitoringServerPool.class) {
				if(instance ==  null){
					instance = new MonitoringServerPool();
				}
			}
		}
		return instance;
	}
	
	/**
	 * pool initialize
	 */
	public void init(Set<ServerInfo> set) {
		synchronized (MonitoringServerPool.class) {
			try{
				if(isInit) throw new Exception("already initialized");
				if(CollectionUtils.isEmpty(set)) throw new Exception("empty server list") ;

				for(ServerInfo info : set) 
					pool.put(info.getIp(), new Connector(info, this.connect(info)));
				
				isInit = true;
			    logger.debug("[MonitoringServerPool - Initialize] Done");
			}catch(Exception e){
				e.printStackTrace();
			}
		};
	}
	
	/**
	 * append pool list
	 * @param info
	 */
	public void appendPool(ServerInfo info){
		synchronized (MonitoringServerPool.class) {
			try {
				if(isInit) throw new Exception("pool instance not initialized yet");
				if(ObjectUtils.isEmpty(info)) throw new Exception("empty server info") ;
				
				pool.put(info.getIp(), new Connector(info, this.connect(info)));
				logger.debug("[MonitoringServerPool - Append Pool] Done");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public MBeanServerConnection getConnection(String ip){
		return pool.get(ip).getConnector();
	}
	
	public Connector getConnector(String ip){
		return pool.get(ip);
	}
	
	private MBeanServerConnection connect(ServerInfo info) throws Exception{
		logger.debug("[MonitoringServerPool - trying to connect..] {}:{}", info.getIp(),info.getPort());
	    JMXConnector jmxc = JMXConnectorFactory.connect(new JMXServiceURL(getUrl(info)));
	    
	    return jmxc.getMBeanServerConnection();
	}
	
	public Map<String, Connector> getPoolMap(){
		return pool;
	}
	
	private String getUrl(ServerInfo info){
		return new StringBuilder("service:jmx:rmi:///jndi/rmi://")
		.append(info.getIp()).append(":")
		.append(info.getPort()).append("/jmxrmi").toString();
	}
}
