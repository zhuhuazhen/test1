package com.hzyw.iot.config;

 
	import java.util.List;
	import java.util.Map;
	 
	import org.springframework.boot.context.properties.ConfigurationProperties;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.context.annotation.PropertySource;
	 
	import lombok.Data;
	/**
	 * 读取自定义配置文件
	 */
	@Data
	@Configuration 
	@ConfigurationProperties(prefix = "etl",ignoreUnknownFields = false)
	@PropertySource(value ={"classpath:map.properties","classpath:list.properties"}, ignoreResourceNotFound = true)
	public class MapListPropertiesConfig {
		
		private  Map<String,String> map;
		
		private List<String> list;
		
		private Map<String,String> prison;//监狱

		public Map<String, String> getMap() {
			return map;
		}

		public void setMap(Map<String, String> map) {
			this.map = map;
		}

		public List<String> getList() {
			return list;
		}

		public void setList(List<String> list) {
			this.list = list;
		}

		public Map<String, String> getPrison() {
			return prison;
		}

		public void setPrison(Map<String, String> prison) {
			this.prison = prison;
		}
		
	} 