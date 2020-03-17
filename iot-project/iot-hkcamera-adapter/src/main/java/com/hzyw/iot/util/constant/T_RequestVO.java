package com.hzyw.iot.util.constant;

import com.alibaba.fastjson.JSONObject;
import com.hzyw.iot.vo.dataaccess.DataType;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dataaccess.RequestDataVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class T_RequestVO {


    public static JSONObject getRequestVO(String uuid, String code, String cmd){
        return getRequestVO(uuid,code,cmd,null,null);
    }

    public static JSONObject getRequestVO(String uuid, String code, String cmd,String msgId,String dim){
        msgId=msgId==null?"31a8c447-5079-4e91-a364-1769ac06fd5c":msgId;
        MessageVO<RequestDataVO> mesVO=new MessageVO<RequestDataVO>();
        mesVO.setType(DataType.Request.getMessageType());
        mesVO.setTimestamp(1566205651);
        mesVO.setMsgId(msgId);
        mesVO.setGwId(uuid);
        mesVO.setData(getDataVO(code,cmd,dim));  //000000000001
        JSONObject jsonObj= (JSONObject) JSONObject.toJSON(mesVO);
        return jsonObj;
    }

    private static RequestDataVO getDataVO(String code,String cmd,String dim) {
        RequestDataVO dataVO=new RequestDataVO();
        List<Map> methods=new ArrayList<Map>();
        List<Map>ins=new ArrayList<Map>();
        Map<String,Object>dataMap=new HashMap<String, Object>();
        Map<String,Object>inMap=new HashMap<String, Object>();

        inMap.put("code", code); //00H
        if(dim!=null){
            List pdtList=new ArrayList<>();
            int dimNum=Integer.parseInt(dim)*2;
            dim=DecimalTransforUtil.toHexStr(String.valueOf(dimNum),1);
            pdtList.add(new String[]{"","03H",dim+"H"});
            inMap.put("pdt",pdtList);
        }else{
            inMap.put("pdt",T_ParamTest.getPdtParams(cmd));
        }
        ins.add(inMap);
        dataMap.put("method", cmd); //82H
        dataMap.put("in", ins);
        methods.add(dataMap);

        dataVO.setId("");
        dataVO.setMethods(methods);
        return dataVO;
    }
}
