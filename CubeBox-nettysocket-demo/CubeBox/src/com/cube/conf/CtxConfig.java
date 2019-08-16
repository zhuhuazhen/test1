package com.cube.conf;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;



@Configuration
public class CtxConfig {
	private static final Logger LOG = Logger.getLogger(CtxConfig.class);


    @Bean(name = {"dataSource"})
    public DataSource dataSource() throws PropertyVetoException {
//    ComboPooledDataSource cpds = new ComboPooledDataSource(); 
//    cpds.setDriverClass( "com.mysql.jdbc.Driver" );  
//    	cpds.setJdbcUrl("jdbc:mysql://192.168.0.200:3306/message_db" ); 
//    	cpds.setUser("root");   
//    	cpds.setPassword("root");   
//    	// 下面的设置是可选的，c3p0可以在默认条件下工作，也可以设置其他条件  
//    	cpds.setMinPoolSize(5);   
//    	cpds.setAcquireIncrement(5);   
//    	cpds.setMaxPoolSize(20);   
//    	
//    	LOG.debug("=============CtxConfig==============dataSource========================");
//        return cpds;
        
    	 return null;
    	}
}
