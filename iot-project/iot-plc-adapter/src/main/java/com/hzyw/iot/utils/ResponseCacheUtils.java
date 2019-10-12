package com.hzyw.iot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.hzyw.iot.vo.ResponseCache;
import com.hzyw.iot.vo.dataaccess.ResponseDataVO;
import com.hzyw.iot.vo.dataaccess.ResultMessageVO;
import com.hzyw.iot.vo.dc.ModbusInfo;

import cn.hutool.core.date.DateUtil;

public class ResponseCacheUtils {
	private static final Logger logger = LoggerFactory.getLogger(PlcProtocolsBusiness.class);
	
	/**
	 * 缓存hash结构：<sn+cmcCOde+cCode , msgId-gwid-nodeid-timestamp>
	 */
	public static final Map<String,ResponseCache> plc_ResponseCache = new HashMap<String,ResponseCache>();
	
	/**
	 * 消息是否超时
	 * @param cacheMsgId
	 * @return
	 */
	public static boolean timeout(String cacheMsgId){
		ResponseCache temp = plc_ResponseCache.get(cacheMsgId);
		if(System.currentTimeMillis()-temp.getTimestamp() > 1000*20){ //超过20秒 则提示超时
			return true;
		}else{
			return false; //未超时请求会直接返回
		}
	}
	
	/**
	 * 
	 * 增加一条待返回的response消息(request下发场景)
	 * @param modbusInfo
	 * @param logmsg
	 * @param msgid
	 * @param plcid
	 * @param nodeid
	 */
	public static void addResponseCache(ModbusInfo modbusInfo,String method,String msgid,String plcid,String nodeid){
		ResponseCache rc = new ResponseCache(method,modbusInfo.getAddress_str(),modbusInfo.getCmdCode_str(),modbusInfo.getcCode_str());
		if(plc_ResponseCache.get(modbusInfo.getCacheMsgId())!= null
				&& timeout(modbusInfo.getCacheMsgId())){
			//上一个请求处理已超时，上报上个超时的消息
			logger.info("---msgid -覆盖上一个消息并返回上一个的通知-----" +msgid);
			ResponseCache ls = plc_ResponseCache.get(modbusInfo.getCacheMsgId());
			sendkafka_by_ResponseCache(ls.getMethod(), ls.getMsgId(), ls.getPlcId(), ls.getNodeId(), ResponseCode.HZYW_PLC_RESPONSE_TIMEOU);//上报超时
		}
		plc_ResponseCache.put(modbusInfo.getCacheMsgId(), rc);
	}
	 
	
	/**
	 * @param method  操作方法
	 * @param msgid   消息ID
	 * @param plcid   
	 * @param nodeid  
	 * @param messageCode  消息编号
	 */
	public static void sendkafka_by_ResponseCache(String method,String msgid,String plcid,String nodeid,int messageCode){
		logger.info("---msgid ------" +msgid);
		//上报成功或失败
		ResponseDataVO responseDataVo = new ResponseDataVO();
		responseDataVo.setId(nodeid);
		List<Map> ls = new ArrayList<Map>();
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("method", method);
		List<Map> out = new ArrayList<Map>();
		out.add(new HashMap());
		m.put("out",out);
		ls.add(m);
 		responseDataVo.setMethods(ls);
		Map<String,String> tags = new HashMap<String,String>();
		tags.put(IotInfoConstant.dev_plc_dataaccess_key, IotInfoConstant.dev_plc_dataaccess_value);
		responseDataVo.setTags(tags);
		//消息结构
		ResultMessageVO messageVo = new ResultMessageVO();
		//MessageVO messageVo = new MessageVO<>();
		messageVo.setType("metricInfoResponse");
		messageVo.setTimestamp(DateUtil.currentSeconds());
		messageVo.setMsgId(msgid);
		messageVo.setData(responseDataVo);
		messageVo.setGwId(plcid);
		messageVo.setMessageCode(messageCode);
		logger.info("---上报response ------" +JSON.toJSONString(messageVo));
		//kafka处理
		//sendKafka("topic",JSON.toJSONString(messageVo));
	}
 
	/**
	 * 获取cacheId消息，上报response请求到前端页面
	 * @param cacheID
	 * @param messageCode
	 */
	public static void sendkafka_by_ResponseCache(String cacheID,int messageCode){
		ResponseCache ls = ResponseCacheUtils.plc_ResponseCache.get(cacheID);
		if(ls == null){
			logger.info("---准备上报response,,但消息没有找到 ,忽略------" );			
			return;
		}
		sendkafka_by_ResponseCache(ls.getMethod(), ls.getMsgId(), ls.getPlcId(), ls.getNodeId(), messageCode);
	}
}
