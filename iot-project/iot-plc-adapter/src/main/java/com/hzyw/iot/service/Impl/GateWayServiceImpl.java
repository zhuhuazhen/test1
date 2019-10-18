package com.hzyw.iot.service.Impl;


import com.hzyw.iot.service.RedisService;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzyw.iot.kafka.DataSendDownConsumer;
import com.hzyw.iot.kafka.KafkaCommon;
import com.hzyw.iot.kafka.config.ApplicationConfig;
import com.hzyw.iot.listener.ListenerService;
import com.hzyw.iot.service.GateWayService;
import com.hzyw.iot.utils.IotInfoConstant;
import com.hzyw.iot.utils.PlcProtocolsBusiness;
import com.hzyw.iot.utils.PlcProtocolsUtils;
 

/**
 * 网关服务
 *
 */
@Service
public class GateWayServiceImpl implements GateWayService {
	private static final Logger logger = LoggerFactory.getLogger(GateWayServiceImpl.class);
	@Autowired
	private KafkaCommon kafkaCommon; //KAFKA工具类
	
	@Autowired
	private ApplicationConfig applicationConfig;//全局配置

	@Autowired
	private RedisService redisService;
	/* 
	 * 数据下发-消费KAFKA获取下发数据
	 */
	@Override
	public void dataSendDown() {
		//从KAFKA获取下发消息 DataSendDownConsumer
		new Thread(new DataSendDownConsumer(kafkaCommon,applicationConfig,this),"数据下发::消费KAFKA").start();
		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see com.hzyw.iot.service.GateWayService#getPLCMetricInfo()
	 */
	@Override
	public void getPLCMetricInfo() {
		//PLC类型设备所有节点
		//List<Map<String, String>> nodelist = IotInfoConstant.plc_relation_plcsnToNodelist.get("12345").get("xxx");
		for(String port :IotInfoConstant.plc_relation_plcsnToNodelist.keySet()){
			for(String plc_sn : IotInfoConstant.plc_relation_plcsnToNodelist.get(port).keySet()){
				List<Map<String, String>> nodelist = IotInfoConstant.plc_relation_plcsnToNodelist.get(port).get(plc_sn);
				for (int i = 0; i < nodelist.size(); i++) {
					try{
					if(IotInfoConstant.plc_relation_plcsnToNodelist.get(port) == null) return ;
					String flag = PlcProtocolsUtils.gloable_dev_status.get(plc_sn + "_login"); // 1表示已登陆
					// 0
					// 表示未登陆
					// （通过心跳判断是否掉线来设置）
					if (!"1".equals(flag)) {
						return ;
					} 
					flag = (String)redisService.get("plc_isconfig_"+plc_sn);
					if (!"1".equals(flag)) {
						return ;
					}
						
					Map<String, String> item = nodelist.get(i);
					String node_sn = item.get(IotInfoConstant.dev_plc_node_sn); // 节点SN
					String node_id = (String)IotInfoConstant.allDevInfo.get(port).get(node_sn + "_defAttribute").get(IotInfoConstant.dev_plc_node_id); 
					String plc_id = (String)IotInfoConstant.allDevInfo.get(port).get(plc_sn + "_defAttribute").get(IotInfoConstant.dev_plc_plc_id);
					logger.info("->>>---Scheduled-----/查询节点详情[调度]发起请求../plc_sn/node_sn --," + plc_sn + "/" + node_sn );
					//构造请求的消息体
					String reqesutJson = "{ \"data\": { \"methods\": [ { \"method\": \"_method\", \"in\": [ {\"plc_node_cCode\":\"_plc_node_cCode\",\"plc_node_id\":\"_plc_node_id\"} ] } ],"
							+ " \"id\": \"_devicid\", \"tags\": { \"agreement\": \"plc\" } }, \"msgId\": \"_msgId\", \"gwId\": \"_gwId\", \"type\": \"request\", \"timestamp\": _timestamp }";
					String method = "sel_detail_node";
					String plc_node_cCode = "01";//按节点查询
					String plc_node_id = node_id;
					String id = plc_id;//=gwid
					String msgId = "cmdcode45_"+UUID.randomUUID().toString();
					String gwId = plc_id;
					String timestamp = System.currentTimeMillis()+"";
			 
			        reqesutJson = reqesutJson.replace("_method", method);
			        reqesutJson = reqesutJson.replaceAll("_plc_node_cCode", plc_node_cCode);
			        reqesutJson = reqesutJson.replaceAll("_plc_node_id", plc_node_id);
			        reqesutJson = reqesutJson.replaceAll("_devicid", id);
			        reqesutJson = reqesutJson.replaceAll("_msgId", msgId);
			        reqesutJson = reqesutJson.replaceAll("_gwId", gwId);
			        reqesutJson = reqesutJson.replaceAll("_timestamp", timestamp);
					logger.info("->>>---Scheduled-----json="+ reqesutJson);//如果没有上报，拷贝此JSON手动发送看看

					PlcProtocolsBusiness.protocals_process(reqesutJson);
					}catch(Exception e){
						logger.error("ListenerService::GateWayServiceImpl::getPLCMetricInfo",e);
					}
				}
			}
		}
	}
	 

}
