package com.status.springboot.model;

import javax.management.ObjectName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class OperatingSystem extends ServerInfo{
	private Long	OpenFileDescriptorCount;
	private Long	MaxFileDescriptorCount;
	private Long	CommittedVirtualMemorySize;
	private Long	TotalSwapSpaceSize;
	private Long	FreeSwapSpaceSize;
	private Long	ProcessCpuTime;
	private Long	FreePhysicalMemorySize;
	private Long	TotalPhysicalMemorySize;
	private Double	SystemCpuLoad;
	private Double	ProcessCpuLoad;
	private String	Arch;
	private String	Version;
	private Double	SystemLoadAverage;
	private Integer	AvailableProcessors;
	private String	Name;
	private ObjectName	ObjectName;
	
	public Long getOpenFileDescriptorCount() {
		return OpenFileDescriptorCount;
	}
	public void setOpenFileDescriptorCount(Long openFileDescriptorCount) {
		OpenFileDescriptorCount = openFileDescriptorCount;
	}
	public Long getMaxFileDescriptorCount() {
		return MaxFileDescriptorCount;
	}
	public void setMaxFileDescriptorCount(Long maxFileDescriptorCount) {
		MaxFileDescriptorCount = maxFileDescriptorCount;
	}
	public Long getCommittedVirtualMemorySize() {
		return CommittedVirtualMemorySize;
	}
	public void setCommittedVirtualMemorySize(
			Long committedVirtualMemorySize) {
		CommittedVirtualMemorySize = committedVirtualMemorySize;
	}
	public Long getTotalSwapSpaceSize() {
		return TotalSwapSpaceSize;
	}
	public void setTotalSwapSpaceSize(Long totalSwapSpaceSize) {
		TotalSwapSpaceSize = totalSwapSpaceSize;
	}
	public Long getFreeSwapSpaceSize() {
		return FreeSwapSpaceSize;
	}
	public void setFreeSwapSpaceSize(Long freeSwapSpaceSize) {
		FreeSwapSpaceSize = freeSwapSpaceSize;
	}
	public Long getProcessCpuTime() {
		return ProcessCpuTime;
	}
	public void setProcessCpuTime(Long processCpuTime) {
		ProcessCpuTime = processCpuTime;
	}
	public Long getFreePhysicalMemorySize() {
		return FreePhysicalMemorySize;
	}
	public void setFreePhysicalMemorySize(Long freePhysicalMemorySize) {
		FreePhysicalMemorySize = freePhysicalMemorySize;
	}
	public Long getTotalPhysicalMemorySize() {
		return TotalPhysicalMemorySize;
	}
	public void setTotalPhysicalMemorySize(Long totalPhysicalMemorySize) {
		TotalPhysicalMemorySize = totalPhysicalMemorySize;
	}
	public Double getSystemCpuLoad() {
		return SystemCpuLoad;
	}
	public void setSystemCpuLoad(Double systemCpuLoad) {
		SystemCpuLoad = systemCpuLoad;
	}
	public Double getProcessCpuLoad() {
		return ProcessCpuLoad;
	}
	public void setProcessCpuLoad(Double processCpuLoad) {
		ProcessCpuLoad = processCpuLoad;
	}
	public String getArch() {
		return Arch;
	}
	public void setArch(String arch) {
		Arch = arch;
	}
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public Double getSystemLoadAverage() {
		return SystemLoadAverage;
	}
	public void setSystemLoadAverage(Double systemLoadAverage) {
		SystemLoadAverage = systemLoadAverage;
	}
	public Integer getAvailableProcessors() {
		return AvailableProcessors;
	}
	public void setAvailableProcessors(Integer availableProcessors) {
		AvailableProcessors = availableProcessors;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public ObjectName getObjectName() {
		return ObjectName;
	}
	public void setObjectName(ObjectName objectName) {
		ObjectName = objectName;
	}
	
	@Override
	public String toString() {
		 return ToStringBuilder.reflectionToString(this);
	}
}
