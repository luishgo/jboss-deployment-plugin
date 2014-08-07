package org.jenkinsci.plugins.jboss_deployment;

import hudson.Launcher;

import java.io.PrintStream;
import java.text.MessageFormat;

public class StandaloneJBossDeployment extends JBossDeployment {

	public StandaloneJBossDeployment(Launcher launcher, ServerBean server, PrintStream log) {
		super(launcher, server, log);
	}

	@Override
	protected String getCheckDeployCommand() {
		return "ls /deployment";
	}

	@Override
	protected String getUndeployCommand(String artifactName) {
		return MessageFormat.format("undeploy {0}", artifactName);
	}

	@Override
	protected String getDeployCommand(String artifactName) {
		return MessageFormat.format("deploy {0}", artifactName);
	}

}
