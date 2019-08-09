package com.hzyw.iot.kafka;

import java.util.Properties;

public abstract class AbstractKafkaCommon {

	private Properties config = new Properties();

	public void setConfig(Properties config) {
		this.config = config;
	}

	public Properties getConfig() {
		return config;
	}
}
