package com.status.springboot.pool.model;

import javax.management.MBeanServerConnection;

import com.status.springboot.model.ServerInfo;

public class Connector {
	private MBeanServerConnection connection;
	private ServerInfo info;
	public Connector(ServerInfo info, MBeanServerConnection connection){
		this.connection = connection;
		this.info = info;
	}

	public MBeanServerConnection getConnector() {
		return connection;
	}

	public void setConnector(MBeanServerConnection connection) {
		this.connection = connection;
	}

	public ServerInfo getInfo() {
		return info;
	}

	public void setInfo(ServerInfo info) {
		this.info = info;
	}
	
	

}
