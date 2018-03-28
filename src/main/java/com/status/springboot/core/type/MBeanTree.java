package com.status.springboot.core.type;

public enum MBeanTree implements Value{
	/**
	 * 모니터링 할  하위 노드 정의 
	 */
	JAVA_LANG("java.lang");
	
	private String value;
	
	MBeanTree(String value){
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
}
