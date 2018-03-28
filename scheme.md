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
		
			
		
		
			 
			

  
  
  