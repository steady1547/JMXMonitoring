package com.status.springboot.process;


public interface Monitor {
	public void oldGenMonitor();
	public void permMonitor();
	public void edenMonitor();
	public void survivordenMonitor();
	public void osMonitor();
	public void openFileDescriptorCountMonitor();
}
