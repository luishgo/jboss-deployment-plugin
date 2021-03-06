package org.jenkinsci.plugins.jboss_deployment;

import hudson.CopyOnWrite;
import hudson.Extension;
import hudson.FilePath.FileCallable;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.remoting.VirtualChannel;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.util.DirScanner;
import hudson.util.FileVisitor;
import hudson.util.ListBoxModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

public class JBossDeploymentPublisher extends Publisher {

	private String filter;
	private final String serverName;
	private String serverGroupName;

	@DataBoundConstructor
	public JBossDeploymentPublisher(String filter, String serverName, String serverGroupName) {
		this.filter = filter;
		this.serverName = serverName;
		this.serverGroupName = serverGroupName;
	}

	public String getServerName() {
		return serverName;
	}

	public String getServerGroupName() {
		return serverGroupName;
	}

	public String getFilter() {
		return filter;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, final BuildListener listener)
			throws IOException, InterruptedException {
		ServerBean server = getDescriptor().findServer(serverName);

		final JBossDeployment deployment = server.isDomain() ? 
				new DomainJBossDeployment(launcher, server, listener.getLogger(), serverGroupName) : 
				new StandaloneJBossDeployment(launcher, server, listener.getLogger());

		build.getWorkspace().act(new FileCallable<Void>() {

			private static final long serialVersionUID = 1L;

			public Void invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
				new DirScanner.Glob(filter, null).scan(f, new FileVisitor() {

					@Override
					public void visit(File f, String relativePath) throws IOException {
						if (f.isFile()) {
							listener.getLogger().println("Starting deployment of " + f.getName() + " in " + serverName);
							deployment.deploy(f.getCanonicalPath());
						}
					}
				});

				return null;
			}

		});

		return true;
	}

	@Override
	public JBossDeploymentDescriptor getDescriptor() {
		return (JBossDeploymentDescriptor) super.getDescriptor();
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.BUILD;
	}

	@Extension
	public static final class JBossDeploymentDescriptor extends BuildStepDescriptor<Publisher> {

		@CopyOnWrite
		private List<ServerBean> servers = new ArrayList<ServerBean>();

		public JBossDeploymentDescriptor() {
			load();
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
			servers.clear();

			List<Object> servers = normalizeJSONReturn(formData, "servers");
			for (Object o : servers) {
				addToServers((JSONObject) o);
			}

			save();
			return super.configure(req, formData);
		}
		
		private void addToServers(JSONObject serverObject) {
			List<String> serverGroups = new ArrayList<String>();
			
			List<Object> serverGroupsJSON = normalizeJSONReturn(serverObject, "serverGroups");
			for (Object o : serverGroupsJSON) {
				serverGroups.add(((JSONObject) o).getString("name"));
			}

			servers.add(new ServerBean(serverObject.getString("name"), 
					serverObject.getString("jbossHome"),
					serverObject.getString("controllerName"), 
					serverObject.getInt("controllerPort"), 
					serverObject.getString("username"), 
					serverObject.getString("password"), 
					serverObject.getBoolean("domain"), 
					serverGroups));
		}
		
		private List<Object> normalizeJSONReturn(JSONObject data, String objectName) {
			List<Object> objects = new ArrayList<Object>();
			
			JSONObject optObject = data.optJSONObject(objectName);
			if (optObject != null) {
				objects.add(optObject);
			} else {
				JSONArray optArray = data.optJSONArray(objectName);
				if (optArray != null) objects.addAll(optArray);
			}

			return objects;
		}

		protected ServerBean findServer(String serverProfileName) {
			for (ServerBean server : servers) {
				if (serverProfileName.equals(server.getName())) {
					return server;
				}
			}
			return null;
		}

		public List<ServerBean> getServers() {
			return servers;
		}

		public ListBoxModel doFillServerNameItems() {
			ListBoxModel m = new ListBoxModel();
			for (ServerBean s : servers) {
				m.add(s.getName());
			}
			return m;
		}
		
		public ListBoxModel doFillServerGroupNameItems(@QueryParameter String serverName) {
			ListBoxModel m = new ListBoxModel();
			if (!"".equals(serverName) && serverName != null) {
				ServerBean serverBean = findServer(serverName);
				if (serverBean.isDomain()) {
					for (String s : serverBean.getServerGroups()) {
						m.add(s);
					}
				} else {
					m.add("-- No need --");
				}
			}
			return m;
		}		

		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		public String getDisplayName() {
			return "JBoss Deployment Servers";
		}

	}

}
