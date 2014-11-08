JBoss Deployment Plugin
=====================

[![Build Status](https://travis-ci.org/luishgo/jboss-deployment-plugin.svg)](https://travis-ci.org/luishgo/jboss-deployment-plugin)

This plugin is designed with JBoss AS 7.x in mind. It allows deployment using JBoss CLI for servers running in domain and standalone modes.

##Configuration

>Server name

Server name for identification.

>JBoss Home

Location of a JBoss installation. Only jboss-cli is used, no need to be running neither be a full installment.

>Controller Name

Name of the controller server.

>Controller Port

Port of the controller server.

>Username
>Password

Management user on the controller server.

>Domain Mode

If selected, opens the possibility to register the server group name.

##Usage

>Artifacts to deploy

Relative paths to artifact(s) to deploy. Can use wildcards like module/dist/**/*.jar, and use comma (followed by optional whitespace) to separate multiple entries. See the @includes of Ant fileset for the exact format. May also contain references to build parameters like $PARAM.

>Server

Select the server previously registered.

>Server Group

Select the server group to deploy, if the selected server is in domain mode.