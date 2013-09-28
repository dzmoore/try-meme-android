#!/bin/bash
mvn clean package
./run_local.sh | tee local.out
