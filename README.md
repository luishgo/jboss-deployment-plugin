JBoss Deployment Plugin
=====================

[![Build Status](https://travis-ci.org/luishgo/jboss-deployment-plugin.svg)](https://travis-ci.org/luishgo/jboss-deployment-plugin)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/69dea7947f8944db8b7ad264e244fa19)](https://www.codacy.com/app/luishgo/jboss-deployment-plugin?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=luishgo/jboss-deployment-plugin&amp;utm_campaign=Badge_Grade)

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
