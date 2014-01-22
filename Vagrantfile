# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant::configure("2") do |config|
  config.vm.box = "wheezy64"
  config.vm.hostname = "esbmon"

  config.vm.provider "virtualbox" do |provider|
    provider.customize ["modifyvm", :id, "--memory", 2048]
  end

  config.vm.network "private_network", ip: "192.168.1.10"
  config.vm.provision :shell, inline: $install
end

$install = <<END
  sudo apt-get update
  sudo apt-get -y --force-yes install oracle-java7-jdk

  export DEBIAN_FRONTEND=noninteractive
  apt-get -q -y install mysql-server mysql-client

  wget http://dist.groovy.codehaus.org/distributions/groovy-binary-2.2.1.zip
  wget http://dist.springframework.org.s3.amazonaws.com/release/GRAILS/grails-2.3.5.zip

  unzip groovy-binary-2.2.1.zip
  unzip grails-2.3.5.zip

  echo "export PATH=$PATH:/home/vagrant/groovy-2.2.1/bin/:/home/vagrant/grails-2.3.5/bin" > /home/vagrant/.bash_profile
  echo "export JAVA_HOME=/usr/lib/jvm/java-7-oracle" >> /home/vagrant/.bash_profile

  echo "create database esbmon_development;" | mysql -u root
END
