package com.hzyw.iot.service;


public interface FFmpegCommandService {
	
	/**
	 * 直播开始
	 */
	public void liveStart();
	
	/**
	 * 直播停止
	 */
	public void liveStop();
	
	 
	/**
	 * 查询点播列表
	 */
	public void queryVODList();
	

	
	
}
