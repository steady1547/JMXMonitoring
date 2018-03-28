package com.status.springboot.model;

import java.lang.management.MemoryUsage;

import javax.management.ObjectName;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.status.springboot.support.CalcUtil;

/**
 * Field 네이밍 규칙은 
 * MBean Attribute Name과 동일하게 맞추기위하여 첫대문자로
 * @author line play
 */
@Data	
public class MemoryPool extends ServerInfo{

	private MemoryUsage Usage;
	private MemoryUsage PeakUsage;
	private String[] MemoryManagerNames;
	private Long UsageThreshold;
	private Boolean UsageThresholdExceeded;
	private Long UsageThresholdCount;
	private Boolean UsageThresholdSupported;
	private Long CollectionUsageThreshold;
	private Boolean CollectionUsageThresholdExceeded;
	private Long CollectionUsageThresholdCount;
	private MemoryUsage CollectionUsage;
	private Boolean CollectionUsageThresholdSupported;
	private Boolean Valid;
	private String Name;
	private String Type;
	private ObjectName ObjectName;
	private Double UsagePercent;
	
	public MemoryUsage getUsage() {
		return Usage;
	}


	public void setUsage(MemoryUsage usage) {
		Usage = usage;
	}


	public MemoryUsage getPeakUsage() {
		return PeakUsage;
	}


	public void setPeakUsage(MemoryUsage peakUsage) {
		PeakUsage = peakUsage;
	}


	public String[] getMemoryManagerNames() {
		return MemoryManagerNames;
	}


	public void setMemoryManagerNames(String [] memoryManagerNames) {
		MemoryManagerNames = memoryManagerNames;
	}


	public Long getUsageThreshold() {
		return UsageThreshold;
	}


	public void setUsageThreshold(Long usageThreshold) {
		UsageThreshold = usageThreshold;
	}


	public Boolean getUsageThresholdExceeded() {
		return UsageThresholdExceeded;
	}


	public void setUsageThresholdExceeded(Boolean usageThresholdExceeded) {
		UsageThresholdExceeded = usageThresholdExceeded;
	}


	public Long getUsageThresholdCount() {
		return UsageThresholdCount;
	}


	public void setUsageThresholdCount(Long usageThresholdCount) {
		UsageThresholdCount = usageThresholdCount;
	}


	public Boolean getUsageThresholdSupported() {
		return UsageThresholdSupported;
	}


	public void setUsageThresholdSupported(Boolean usageThresholdSupported) {
		UsageThresholdSupported = usageThresholdSupported;
	}


	public Long getCollectionUsageThreshold() {
		return CollectionUsageThreshold;
	}


	public void setCollectionUsageThreshold(Long collectionUsageThreshold) {
		CollectionUsageThreshold = collectionUsageThreshold;
	}


	public Boolean getCollectionUsageThresholdExceeded() {
		return CollectionUsageThresholdExceeded;
	}


	public void setCollectionUsageThresholdExceeded(
			Boolean collectionUsageThresholdExceeded) {
		CollectionUsageThresholdExceeded = collectionUsageThresholdExceeded;
	}


	public Long getCollectionUsageThresholdCount() {
		return CollectionUsageThresholdCount;
	}


	public void setCollectionUsageThresholdCount(Long collectionUsageThresholdCount) {
		CollectionUsageThresholdCount = collectionUsageThresholdCount;
	}


	public MemoryUsage getCollectionUsage() {
		return CollectionUsage;
	}


	public void setCollectionUsage(MemoryUsage collectionUsage) {
		CollectionUsage = collectionUsage;
	}


	public Boolean getCollectionUsageThresholdSupported() {
		return CollectionUsageThresholdSupported;
	}


	public void setCollectionUsageThresholdSupported(
			Boolean collectionUsageThresholdSupported) {
		CollectionUsageThresholdSupported = collectionUsageThresholdSupported;
	}


	public Boolean getValid() {
		return Valid;
	}


	public void setValid(Boolean valid) {
		Valid = valid;
	}


	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public String getType() {
		return Type;
	}


	public void setType(String type) {
		Type = type;
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


	public Double getUsagePercent() {
		return UsagePercent;
	}


	public void setUsagePercent(Double usagePercent) {
		UsagePercent = usagePercent;
	};
	

	
}
