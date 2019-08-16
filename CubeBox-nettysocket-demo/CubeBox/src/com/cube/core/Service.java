package com.cube.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

 
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.Lifecycle;
import org.springframework.stereotype.Component;

import com.cube.core.db.DbManager;
import com.cube.logic.HttpProcess;
import com.cube.logic.HttpProcessRunnable;
import com.cube.server.CubeBootstrap;
import com.cube.server.CubeBootstrapLong;

import com.cube.server.HttpBootstrap;

@Component
public class Service implements Lifecycle {

	private static final Logger LOG = Logger.getLogger(Service.class);

	private final static ExecutorService threadPool = Executors.newCachedThreadPool();
	private boolean started = false;
	private Future future;

	@Autowired
	private CubeBootstrap cubeBoostrap;
	@Autowired
	private CubeBootstrapLong cubeBootstrapLong;
	
	@Autowired
	private HttpBootstrap httpBootstrap;
//	@Autowired
//	private DbManager dbManager;

	public void start() {
		LOG.info("Service serverRun start");
		started = true;
		future = threadPool.submit(cubeBoostrap);
		future = threadPool.submit(cubeBootstrapLong);

		int sleepCount = 5;
		while (!cubeBoostrap.isRun()) {
			if (sleepCount < 0) {
				future.cancel(true);
				return;
			}
			try {
				sleepCount--;
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOG.info("InterruptedException:", e);
				e.printStackTrace();
			}
		}
		//tcp 超长指令发送给手表
		int sleepCount2 = 5;
		while (!cubeBootstrapLong.isRun()) {
			if (sleepCount2 < 0) {
				future.cancel(true);
				return;
			}
			try {
				sleepCount2--;
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOG.info("InterruptedException:", e);
				e.printStackTrace();
			}
		}
		
		 
		// 初始化路由
		LOG.info("初始化路由httpBootstrap");
		initRoute();
		threadPool.submit(httpBootstrap);
		
		
//		LOG.info("dbManager:{}", dbManager.toString());
		//数据库连接测试
//		Connection connection = null;
//		PreparedStatement st = null;
//		try {
//			connection = dbManager.getConnection();
//			String sql = "select * from led_romupdate_log";
//			st = connection.prepareStatement(sql);
//			ResultSet rst = st.executeQuery();
//			if(rst.next()){
//				LOG.info("id:{}", rst.getString(1));
//				LOG.info("mac:{}", rst.getString(2));
//				LOG.info("type:{}", rst.getString(3));
//				LOG.info("ins_tm:{}", rst.getString(4));
//				LOG.info("upd_tm:{}", rst.getString(5));
//				LOG.info("pre_version:{}", rst.getString(6));
//				LOG.info("now_version:{}", rst.getString(7));
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (connection != null) {
//					dbManager.close(connection);
//				}
//				if(st != null){
//					st.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}

	}

	
	
	// 初始化路由
	private void initRoute() {
		try {
			String pkg = "com.cube.logic.http";
			URL root = this.getClass().getResource("/route");
			InputStream in = this.getClass().getResourceAsStream("/route");
			LOG.info("root=:"+ root);
			// FileReader fr = new FileReader(root.getPath());
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(inReader);
			String line = null;
			String[] args = null;
			while ((line = br.readLine()) != null) {
				args = line.split("\\s{1,}");
				if (args.length != 2) {
					continue;
				}

				Class<?> clazz = this.getClass().forName(pkg + "." + args[1]);
				Class<?>[] ifaces = clazz.getInterfaces();
				if (!isInterface(clazz, HttpProcess.class)) {
					LOG.info("{} is not the HttpProcess"+clazz.getCanonicalName());
					continue;
				}

				HttpProcess process = (HttpProcess) clazz.newInstance();
				HttpProcessRunnable.ROUTE.put(args[0], process);
				if (args[0].endsWith("/") == false) {
					//LOG.info("args[0]=:"+ args[0] + "/");
					HttpProcessRunnable.ROUTE.put(args[0] + "/", process);
				} else {
					//LOG.info("args[0222]=:"+ args[0].substring(0, args[0].length() - 2));
					HttpProcessRunnable.ROUTE.put(args[0].substring(0, args[0].length() - 2), process);
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("初始化路由文件没找到", e);
		} catch (IOException e) {
			LOG.error("初始化路由出错", e);
		} catch (ClassNotFoundException e) {
			LOG.error("初始化路由出错", e);
		} catch (InstantiationException e) {
			LOG.error("初始化路由出错", e);
		} catch (IllegalAccessException e) {
			LOG.error("初始化路由出错", e);
		}

	}

	public void stop() {
		LOG.info("Service serverRun stop");
		started = false;
		threadPool.shutdown();
		if (future != null) {
			future.cancel(true);
		}
	}

	public boolean isRunning() {
		LOG.info("Service serverRun isRunning");
		return started;
	}

	public boolean isInterface(Class clazz, Class infClazz) {
		if (clazz == Object.class) {
			return false;
		}
		Class[] ifaces = clazz.getInterfaces();
		boolean trueType = false;
		for (Class<?> iface : ifaces) {
			if (iface.equals(infClazz)) {
				trueType = true;
				break;
			}
		}
		if (trueType) {
			return true;
		}
		clazz = clazz.getSuperclass();
		return isInterface(clazz, infClazz);

	}

}
