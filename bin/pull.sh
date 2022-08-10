#!/bin/bash

REPO_DIR=/home/ubuntu/bibim-web-server
TOMCAT_WEBAPPS_DIR=/var/lib/tomcat9/webapps

sudo systemctl stop tomcat9

echo "==========================================="
echo "1. Git Pull"
echo "==========================================="
cd $REPO_DIR
git fetch --all
git reset --hard origin/develop
git pull origin develop

echo "==========================================="
echo "2. Build"
echo "==========================================="
cd $REPO_DIR
./gradlew build -x check

echo "==========================================="
echo "3. Place war file to ROOT"
echo "==========================================="
cd $TOMCAT_WEBAPPS_DIR
sudo rm -rf ROOT
sudo rm -rf ROOT.war
cd $REPO_DIR/build/libs
sudo mv bibimWeb.war $TOMCAT_WEBAPPS_DIR/ROOT.war
cd $TOMCAT_WEBAPPS_DIR
echo "==== Directory : tomcat9/webapps ===="
ls

sudo systemctl start tomcat9