package org.jenkinsci.plugins.jboss_deployment;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class JBossDeploymentBuilder extends Builder {

    private final String serverName;
	private String serverGroupName;

    @DataBoundConstructor
    public JBossDeploymentBuilder(String serverName, String serverGroupName) {
        this.serverName = serverName;
		this.serverGroupName = serverGroupName;
    }

    public String getServerName() {
        return serverName;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {
        listener.getLogger().println("Server: "+serverName);
        listener.getLogger().println("Group: "+serverGroupName);
        return true;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private List<ServerBean> servers = new ArrayList<ServerBean>();

        public DescriptorImpl() {
            load();
        }
        
        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
        	servers.clear();
        	
            JSONObject optServersObject = formData.optJSONObject("servers");
            if (optServersObject != null) {
            	JSONObject serverObject = (JSONObject) (optServersObject.optJSONObject("CurrentServer"));
            	servers.add(new ServerBean(
            			serverObject.getString("name"),
            			serverObject.getString("jbossHome"),
            			serverObject.getString("controllerName"),
            			serverObject.getInt("controllerPort"),
            			serverObject.getString("username"),
            			serverObject.getString("password")
            			));
            } else {
            	JSONArray optServersArray = formData.optJSONArray("servers");
            	if (optServersArray != null) {
            		for (int i=0; i < optServersArray.size(); i++) {
            			JSONObject serverObject = (JSONObject) ((JSONObject)optServersArray.get(i)).optJSONObject("CurrentServer");
                    	servers.add(new ServerBean(
                    			serverObject.getString("name"),
                    			serverObject.getString("jbossHome"),
                    			serverObject.getString("controllerName"),
                    			serverObject.getInt("controllerPort"),
                    			serverObject.getString("username"),
                    			serverObject.getString("password")
                    			));            			
            		}
            	}            	
            }
        	
            save();
            return super.configure(req,formData);
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
        
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            return "JBoss Deployment Servers";
        }

    }
    
    public static class ServerBean {
    	
    	private String name;

    	private String jbossHome;
    	
    	private String controllerName;
    	
    	private Integer controllerPort;
    	
    	private String username;
    	
    	private String password;
    	
		public ServerBean(String name, String jbossHome, String controllerName, Integer controllerPort, String username, String password) {
			this.name = name;
			this.jbossHome = jbossHome;
			this.controllerName = controllerName;
			this.controllerPort = controllerPort;
			this.username = username;
			this.password = password;
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

    }
}

