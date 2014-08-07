package org.jenkinsci.plugins.jboss_deployment;

import hudson.Launcher;

import java.io.PrintStream;
import java.text.MessageFormat;

public class DomainJBossDeployment extends JBossDeployment {

	private String serverGroupName;

	public DomainJBossDeployment(Launcher launcher, ServerBean server, PrintStream log, String serverGroupName) {
		super(launcher, server, log);
		this.serverGroupName = serverGroupName;
	}

	@Override
	protected String getCheckDeployCommand() {
		return MessageFormat.format("ls /server-group={0}/deployment", serverGroupName);
	}

	@Override
	protected String getUndeployCommand(String artifactName) {
		return MessageFormat.format("undeploy {0} --all-relevant-server-groups", artifactName);
	}

	@Override
	protected String getDeployCommand(String artifactName) {
		return MessageFormat.format("deploy {0} --server-groups={1}", artifactName, serverGroupName);
	}

}
