package com.hzyw.iot.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.hzyw.iot.service.FFmpegCommandService;

@Component
@Order(value = 1)
public class StartApplication implements ApplicationRunner {
 
	@Autowired
	private FFmpegCommandService ffcommandService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		 //ffcommandService.recordingVOD();

	}

}
