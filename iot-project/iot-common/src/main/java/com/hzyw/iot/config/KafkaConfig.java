package com.hzyw.iot.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hzyw.iot.kafka.KafkaCommon;
import com.hzyw.iot.util.JedisPoolUtils;

@Configuration   
public class KafkaConfig {
	private static Logger logger = Logger.getLogger(KafkaConfig.class);  
	   
    //@Value("${spring.redis.port}")  
    static Integer port;
    static String host;
    static String password;
     
    
    static Properties props = new Properties();;
    static {
		// 手动加载加载配置文件
		InputStream in = JedisPoolUtils.class.getClassLoader().getResourceAsStream("kafka.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*// 获得池子对象
		poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(Integer.parseInt(pro.get("redis.maxIdle").toString()));// 最大闲置个数
		poolConfig.setMinIdle(Integer.parseInt(pro.get("redis.minIdle").toString()));// 最小闲置个数
		poolConfig.setMaxTotal(Integer.parseInt(pro.get("redis.maxTotal").toString()));// 最大连接数
		poolConfig.setTestOnBorrow(true);// 在borrow一个jedis实例的时候，是否需要验正作，如果是赋值true
		poolConfig.setTestOnReturn(true);// 在return。。。。
		poolConfig.setBlockWhenExhausted(true);// 连接耗尽时是否阻塞
		port = new Integer(pro.get("redis.port").toString());
		host = pro.get("redis.url").toString();
		password = pro.get("redis.passWord").toString();*/
		
		props.put("bootstrap.servers", pro.get("spring.kafka.bootstrap.servers"));    
        props.put("group.id", "group_zhu");   
        props.put("enable.auto.commit", "true"); 
        props.put("auto.commit.interval.ms", "1000"); 
        props.put("session.timeout.ms", "30000");  
        props.put("auto.offset.reset", "earliest");  
        //props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
	}
    
    
     
    /* 自动加载配置
    @Bean  
    @ConfigurationProperties(prefix = "spring.kafka")  
    public Properties getProperties() {  
        Properties config = new Properties();  
        return config;  
    } */ 
    
    @Bean  
    public Properties getProperties() {   
        return props;  //扩展并手动从指定文件加载
    } 
  
      
    //@ConfigurationProperties(prefix = "spring.kafka")  未來可以考慮直接走springboot默認配置
    @Bean
    public KafkaCommon getKafkaCommon() {  
    	KafkaCommon factory = new KafkaCommon();  
        factory.setConfig(getProperties());  
        logger.info("KafkaCommon bean init success.");    
        return factory;
    }  
}