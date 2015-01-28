#!/bin/sh
cp /home/jalal/projects/samen/internetbanking/seapay/web/target/seapay-war.war $JBOSS_HOME/standalone/deployments
touch $JBOSS_HOME/standalone/deployments/seapay-war.war.dodeploy

