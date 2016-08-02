/**
 * 
 */
package com.phule.javavnnblog.model;

/**
 * @author BH4Ljfe
 *
 */
public class Config {
	String id;
	int timeout;
	int threads;
	int timoutPort;
	
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public int getThreads() {
		return threads;
	}
	public void setThreads(int threads) {
		this.threads = threads;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getTimoutPort() {
		return timoutPort;
	}
	public void setTimoutPort(int timoutPort) {
		this.timoutPort = timoutPort;
	}
	
}
