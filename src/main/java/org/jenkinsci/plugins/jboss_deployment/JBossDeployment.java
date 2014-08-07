package org.jenkinsci.plugins.jboss_deployment;

import hudson.Launcher;
import hudson.Launcher.ProcStarter;
import hudson.util.ArgumentListBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

public abstract class JBossDeployment {
	
	private Launcher launcher;
	private ServerBean server;
	private PrintStream log;

	public JBossDeployment(Launcher launcher, ServerBean server, PrintStream log) {
		this.launcher = launcher;
		this.server = server;
		this.log = log;
	}

	public void deploy(String artifactPath) {
		String artifactName = artifactPath.substring(artifactPath.lastIndexOf(File.separatorChar) + 1);
		
		try {
			if (isAlreadyDeployed(artifactName)) {
				undeploy(artifactName);
			}
			doDeploy(artifactPath);
		} catch (IOException e) {
			log.println(e.getMessage());
		} catch (InterruptedException e) {
			log.println(e.getMessage());
		}
	}
	
	private boolean isAlreadyDeployed(String artifactName) throws IOException, InterruptedException {
		log.println("Checking if " + artifactName + " already deployed");
		String result = execute(getCheckDeployCommand());

		boolean alreadyDeployed = result.contains(artifactName);
		
		log.println(alreadyDeployed ? artifactName + " is already deployed" : "Not deployed");
		
		return alreadyDeployed;
	}
	
	private void undeploy(String artifactName) throws IOException, InterruptedException {
		log.println("Undeploying " + artifactName);
		execute(getUndeployCommand(artifactName));
		log.println("Undeploy done");
	}
	
	private void doDeploy(String artifactPath) throws IOException, InterruptedException {
		log.println("Deploying " + artifactPath);
		execute(getDeployCommand(artifactPath));
		log.println("Deploy done");
	}
	

	protected abstract String getCheckDeployCommand();

	protected abstract String getUndeployCommand(String artifactName);
	
	protected abstract String getDeployCommand(String artifactName);
	
	private String execute(String... commands) throws IOException, InterruptedException {
		String path = createCommandsFile(commands);
		ArgumentListBuilder args = createJBossCLIExecutor(path);
		
		return callJBossCLI(args);
	}
	
	private String callJBossCLI(ArgumentListBuilder args) throws IOException, InterruptedException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
        	ProcStarter starter = launcher.launch()
	    		.stderr(log)
	    		.stdout(stream)
	    		.cmds(args);
        	
        	boolean[] masks = starter.masks();
        	Arrays.fill(masks, true);
        	starter.masks(masks);
        	
        	starter.join();
        	
        	return stream.toString();
		} finally {
			stream.close();
		}
	}
	
	private String createCommandsFile(String...commands) throws IOException {
		File file = File.createTempFile("jboss", ".cli");
		file.deleteOnExit();
		FileWriter writer = new FileWriter(file);
		for (String cmd : commands) {
			writer.write(cmd);
			writer.write("\r\n");
		}
		writer.write("quit");
		writer.close();
		
		return file.getCanonicalPath();
	}
	
	private ArgumentListBuilder createJBossCLIExecutor(String path) {
		String cli = server.getJbossHome() + "/bin/jboss-cli" + (launcher.isUnix() ? ".sh" : ".bat");
    	
        ArgumentListBuilder args = new ArgumentListBuilder();
        args.add(cli);
    	args.add("--connect");
    	args.add("controller=" + server.getControllerName() + ":" + server.getControllerPort());
    	args.add("--user=" + server.getUsername());
    	args.add("--password=" + server.getPassword(), true);
    	args.add("--file=" + path);
    	
        if(!launcher.isUnix()) {
            args = args.toWindowsCommand();
        }
		return args;
	}

}
