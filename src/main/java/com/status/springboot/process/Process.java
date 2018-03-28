package com.status.springboot.process;

import com.status.springboot.pool.model.Connector;

@FunctionalInterface
public interface Process {
	public void doProcess(Connector connector) throws Exception;
}
