#!/usr/bin/env bash
echo deb https://dl.bintray.com/gauge/gauge-deb stable main | sudo tee -a /etc/apt/sources.list
sudo apt-get update
sudo apt-get install gauge
gauge_setup
gauge --install java
echo "bintray.user=user\nbintray.apikey=key" > local.properties
