package com.hzyw.iot.service;

import com.hzyw.iot.vo.dataaccess.MessageVO;

public interface GateWayService {

	//kafka消费PLC指令
	void dataSendDown();
	
	
}
