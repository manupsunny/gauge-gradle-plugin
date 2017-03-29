GAUGE_LATEST=`curl -w "%{url_effective}\n" -L -s -S https://github.com/getgauge/gauge/releases/latest -o /dev/null`
GAUGE_LATEST_VERSION=`echo $GAUGE_LATEST | sed 's/.*\/v//'`
echo "Downloading Gauge with version $GAUGE_LATEST_VERSION"
GAUGE_DOWNLOAD_URL="https://github.com/getgauge/gauge/releases/download/v$GAUGE_LATEST_VERSION/gauge-$GAUGE_LATEST_VERSION-linux.x86_64.zip"
echo "Downloading $GAUGE_DOWNLOAD_URL"
wget -qO- -O tmp.zip $GAUGE_DOWNLOAD_URL && unzip tmp.zip && rm tmp.zip
sudo sh install.sh
gauge --install java
echo "bintray.user=user\nbintray.apikey=key" > local.properties
