package com.status.springboot.core.type;

import com.status.springboot.model.MemoryPool;
import com.status.springboot.model.OperatingSystem;

public enum MBeanObjectType {
	MEMORY_OLD_GEN(ObjectType.MEMORY_POOL, ObjecSubName.PS_OLD_GEN),
	MEMORY_PERM_GEN(ObjectType.MEMORY_POOL, ObjecSubName.PS_PERM_GEN),
	MEMORY_EDEN_SPACE(ObjectType.MEMORY_POOL, ObjecSubName.PS_EDEM_SPACE),
	MEMORY_SURVIVOR_SPACE(ObjectType.MEMORY_POOL, ObjecSubName.PS_SURVIVOR_SPACE),
	OPERTATING_SYSTEM(ObjectType.OPERATING_SYSTEM, null);
	
	private ObjectType type;
	private ObjecSubName name;
	
	MBeanObjectType(ObjectType type, ObjecSubName name){
		this.type = type;
		this.name = name;
	}
	
	public String getObjectTypeValue(){
		return type.getValue();
	}
	
	public String getObjectSubNameValue(){
		return (name == null) ? null : name.getValue();
	}
	
	private enum ObjectType implements Value {
		MEMORY_POOL(MemoryPool.class.getSimpleName()),
		OPERATING_SYSTEM(OperatingSystem.class.getSimpleName());

		private String value;
		
		ObjectType(String value){
			this.value = value;
		}
		
		@Override
		public String getValue() {
			return value;
		}
	}

	private  enum ObjecSubName implements Value {
		PS_OLD_GEN("PS Old Gen"),
		PS_PERM_GEN("PS Perm Gen"),
		PS_EDEM_SPACE("PS Eden Space"),
		PS_SURVIVOR_SPACE("PS Survivor Space")
		;
		
		private String value;
		
		ObjecSubName(String value){
			this.value = value;
		}
		
		@Override
		public String getValue() {
			return value;
		}
	};
}

