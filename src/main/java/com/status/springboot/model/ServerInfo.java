package com.status.springboot.model;

import org.springframework.data.annotation.Id;

import lombok.Data;
@Data
public class ServerInfo {
	
	@Id
	private String id;
	private String ip;
	private String hostname;
	/** 모니터링 할 타입 추가 필요.**/
	private Integer port = 18001;
	
	public ServerInfo(){}
	public ServerInfo(String ip, String hostname, int port) {
		// TODO Auto-generated constructor stub
		this.ip = ip;
		this.hostname = hostname;
		this.port = port;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getTags(){
		StringBuilder sb = new StringBuilder();
		sb.append("host=").append(this.hostname);
		sb.append(",ip=").append(this.ip);
		return sb.toString();
	}
	
}
