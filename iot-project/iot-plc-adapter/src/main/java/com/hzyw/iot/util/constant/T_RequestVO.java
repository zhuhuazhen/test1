package com.hzyw.iot.util.constant;

import com.alibaba.fastjson.JSONObject;
import com.hzyw.iot.vo.dataaccess.DataType;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dataaccess.RequestDataVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class T_RequestVO {


    public static JSONObject getRequestVO(String uuid, String code, String cmd){
        return getRequestVO(uuid,code,cmd,null,null);
    }

    public static JSONObject getRequestVO(String uuid, String code, String cmd,String msgId,Map<String,Object> pdtMap){
        msgId=msgId==null?"31a8c447-5079-4e91-a364-1769ac06fd5c":msgId;
        MessageVO<RequestDataVO> mesVO=new MessageVO<RequestDataVO>();
        mesVO.setType(DataType.Request.getMessageType());
        mesVO.setTimestamp(System.nanoTime());
        mesVO.setMsgId(msgId);
        mesVO.setGwId(uuid);
        mesVO.setData(getDataVO(code,cmd,pdtMap));
        JSONObject jsonObj= (JSONObject) JSONObject.toJSON(mesVO);
        return jsonObj;
    }

    private static RequestDataVO getDataVO(String code,String cmd,Map<String,Object> pdtMap) {
        RequestDataVO dataVO=new RequestDataVO();
        List<Map> methods=new ArrayList<Map>();
        List<Map>ins=new ArrayList<Map>();
        Map<String,Object>dataMap=new HashMap<String, Object>();
        Map<String,Object>inMap=new HashMap<String, Object>();

        inMap.put("code", code); //00H
        inMap.put("pdt",T_ParamTest.getPdtParams(cmd,pdtMap));
        ins.add(inMap);
        dataMap.put("method", cmd); //82H
        dataMap.put("in", ins);
        methods.add(dataMap);

        dataVO.setId("");
        dataVO.setMethods(methods);
        return dataVO;
    }
}
