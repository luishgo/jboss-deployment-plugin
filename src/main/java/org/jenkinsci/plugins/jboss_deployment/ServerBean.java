package org.jenkinsci.plugins.jboss_deployment;

import java.util.List;

public class ServerBean {
	
	private String name;

	private String jbossHome;
	
	private String controllerName;
	
	private Integer controllerPort;
	
	private String username;
	
	private String password;

	private boolean domain;

	private List<String> serverGroups;
	
	public ServerBean(String name, String jbossHome, String controllerName, Integer controllerPort, String username, String password, boolean domain, List<String> serverGroups) {
		this.name = name;
		this.jbossHome = jbossHome;
		this.controllerName = controllerName;
		this.controllerPort = controllerPort;
		this.username = username;
		this.password = password;
		this.domain = domain;
		this.serverGroups = serverGroups;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getJbossHome() {
		return jbossHome;
	}

	public void setJbossHome(String jbossHome) {
		this.jbossHome = jbossHome;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public Integer getControllerPort() {
		return controllerPort;
	}

	public void setControllerPort(Integer controllerPort) {
		this.controllerPort = controllerPort;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isDomain() {
		return domain;
	}
	
	public boolean getDomain() {
		return domain;
	}

	public void setDomain(boolean domain) {
		this.domain = domain;
	}

	public List<String> getServerGroups() {
		return serverGroups;
	}

	public void setServerGroups(List<String> serverGroups) {
		this.serverGroups = serverGroups;
	}

}
