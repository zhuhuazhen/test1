package com.cube.core.db;

import java.sql.Connection;
import java.sql.SQLException;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * jdbc管理类
 * @description DbManager
 * @author tengyz
 * @version 0.1
 * @date 2014年8月31日
 */
@Component
public class DbManager {

    @Autowired
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }
    

    /**
     * 获取连接
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public  void close(Connection conn) throws SQLException{
        conn.close();
    }


	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
    

}
