package com.hzyw.iot.mqtt.callback;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hzyw.iot.util.GatewayMqttUtil;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Component
public class Handler {
	private static final Logger log = LoggerFactory.getLogger(Handler.class);
	/*@Autowired
	private CommPubHandler commPubHandler;*/
	public void handlerMessages(String topic, String message) {
		JSONObject jsonObject =null;
		try{
			 jsonObject = JSONUtil.parseObj(message);//messageVO
		    //yes, it is
		}catch(Exception e){
			log.error("json数据解析错误",e);
		}
		JSONObject data = JSONUtil.parseObj(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_data));
		// 判断type类型
		String type = (String)jsonObject.get(GatewayMqttUtil.dataModel_messageVO_type);
		if(type.equals("serviceOffline")) {
			//下线消息
			/*Map<String, String> map = new HashMap<String,String>();
			map.put("key", "value");
			String msgId = GatewayMqttUtil.getUUID();
			ServiceDataVO serviceOfflineVO = new ServiceDataVO();
			serviceOfflineVO.setId("0001-f82d132f9bb018ca-2001-ffff-acbc");
			serviceOfflineVO.setStatus("offline");
			serviceOfflineVO.setTags(map);
			MessageVO offlineMessageVO = new MessageVO(serviceOfflineVO);
			offlineMessageVO.setType(DataType.ServiceOffline.getMessageType());
			offlineMessageVO.setMsgId(msgId);
			offlineMessageVO.setTimestamp(System.currentTimeMillis());
			offlineMessageVO.setGwId("");
			commPubHandler.Publish(JSON.toJSONString(offlineMessageVO));*/
			System.out.println("=========mqtt服务异常关闭");
		}
			
		
	}

}
