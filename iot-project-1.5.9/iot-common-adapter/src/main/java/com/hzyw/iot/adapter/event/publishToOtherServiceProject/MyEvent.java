package com.hzyw.iot.adapter.event.publishToOtherServiceProject;


import org.springframework.context.ApplicationEvent;

 
/**
 * 自定义自己的事件
 *
 */
public class MyEvent extends ApplicationEvent {
	private String user;

	public MyEvent(Object source,String uu) {
		super(source);
		this.user = uu;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
 
}
