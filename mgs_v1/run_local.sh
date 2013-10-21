#!/bin/bash
java $JAVA_OPTS -DMGS_DB_URL=$MGS_DB_URL -DMGS_DB_USER=$MGS_DB_USER -DMGS_DB_PW=$MGS_DB_PW -jar target/dependency/jetty-runner.jar target/*.war
