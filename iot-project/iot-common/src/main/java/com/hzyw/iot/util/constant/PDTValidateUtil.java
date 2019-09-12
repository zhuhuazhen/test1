package com.hzyw.iot.util.constant;

import com.alibaba.fastjson.JSONArray;
import java.util.Arrays;
import java.util.List;

/**
 * pdt 指令参数校验
 */
public class PDTValidateUtil {


    /**
     * 主动上报节点数据(F7H)请求报文校验-----（文档上 没有请求需求）
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateReqF7HPdt(String cmdParam) throws Exception {
        return true;
    }

    /**
     * 主动上报节点数据(F7H)响应报文校验
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateResF7HPdt(String cmdParam) throws Exception {
        boolean resBoolean=true;
        if ("02".equals(cmdParam)) return false; //02 (02H) 表示报文校验有错误，返回错误码:02
        byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(cmdParam);
        if(bytePdtArrs.length>250) {
            resBoolean = false;
            System.out.println("======解析 '主动上报节点数据'(F7H)响应的PDT报文错误！字节长度超过250个字节!");
        }
        return resBoolean;
    }


    /**
     * 查询节点详细数据(45H) 请求报文校验
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateReqT45HPdt(String cmdParam) throws Exception {

        return true;
    }

    /**
     * 查询节点详细数据(45H) 响应报文校验
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateResT45HPdt(String cmdParam) throws Exception {

        return true;
    }

    /**
     * 查询集中器状态 (73H)响应报文校验
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateResT73HPdt(String cmdParam) throws Exception {
        boolean resBoolean=true;
        if ("02".equals(cmdParam)) return false; //02(02H) 表示报文校验有错误，返回错误码:02
        byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(cmdParam);
        if(bytePdtArrs.length>33) {
            resBoolean = false;
            System.out.println("======解析 '查询集中器状态'(73H)响应的PDT报文错误！字节长度超过33个字节!");
            //throw new Exception(" 解析 '查询集中器状态'(73H)响应的PDT报文错误！字节长度超过33个字节!");
        }
        return resBoolean;
    }

    /**
     * 下发定时任务 (82H)请求报文校验
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateReqT82HPdt(String cmdParam) throws Exception{
        return true;
    }

    /**
     * 下发定时任务(82H)响应报文校验
     * @return
     * @param cmdParam
     */
    public static boolean validateResT82HPdt(String cmdParam)throws Exception{
        boolean resBoolean=true;
        if ("02".equals(cmdParam)) return false; //02 (02H) 表示报文校验有错误，返回错误码:02
        byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(cmdParam);
        if(bytePdtArrs.length>250) {
            resBoolean = false;
            System.out.println("======解析 '下发定时任务'(82H)响应的PDT报文错误！字节长度超过250个字节!");
        }
        return resBoolean;
    }

    /**
     * 节点调光(42H)请求报文校验
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateReqT42HPdt(String c,StringBuffer cmdParam)throws Exception{
        if("".equals(c)) return false;
        List<Object> paramList= JSONArray.parseArray(cmdParam.toString());
        String pdtParam="";
        if(paramList.size()>0){
            JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
            for (int h = 0; h < paramValArray.size(); h++) {
                System.out.print("======解析'节点调光(42H)===Array:" + paramValArray.getString(h));
                pdtParam+=ConverUtil.MappCODEVal(paramValArray.getString(h),true);
                System.out.println("=====解析'节点调光(42H)==11111====cmdParam:" +pdtParam);
            }
            byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(pdtParam);
            int pdtLen=bytePdtArrs.length;
            if(!PLCValidateUtil.rangeInDefined(pdtLen,2,8)) {
                System.out.println("======解析'节点调光(42H)'请求的PDT报文长度有错误！字节长度范围：2~8字节!");
                return false;
            }
            paramValArray.set(0,validateNodeID(c,bytePdtArrs));
            paramList=new JSONArray();
            paramList.add(paramValArray);
            cmdParam.replace(0,cmdParam.length(),JSONArray.toJSONString(paramList));
            return true;
        }
        return false;
    }

    private static String validateNodeID(String c,byte[] bytePdtArrs)throws Exception{
        String phyID="";
        byte[]byteAB,byteDIM,itemByteBuf;
        int itemStart=0,itemEnd=0;
        int pdtLen=bytePdtArrs.length;
        //C=01H: ID=节点PHYID
        //C=02H: 6个Byte的最低Byte表示组号(例如:控制第2组节点,ID=000000000002)
        if("01H".equals(c) || "02H".equals(c)){
            itemEnd=pdtLen-2;
            if(itemEnd<=0) throw new Exception("节点调光(42H)请求报文校验: 当C=01H或02H时,ID节点入参不能为空！");
            System.out.println("======节点调光(42H)请求报文校验 ==itemEnd:"+itemEnd);
            itemByteBuf= Arrays.copyOfRange(bytePdtArrs,itemStart,itemEnd);
            String ID=ConverUtil.convertByteToHexString(itemByteBuf);
            System.out.println("======节点调光(42H)请求报文校验 ===控制码(C):"+c+", ===获取入参节点或组ID:"+ID);
            phyID=PLCValidateUtil.checkDeviceUID(ID);
            System.out.println("======节点调光(42H)请求报文校验 ====控制码(C):"+c+",===拼6个字节后的入参节点或组ID:"+phyID);
        }else if("03H".equals(c)){ //C=03H: ID为全0（即 ID = 000000000000）
            itemByteBuf=ConverUtil.hexStrToByteArr("000000000000");
            phyID=ConverUtil.convertByteToHexString(itemByteBuf);
            System.out.println("======节点调光(42H)请求报文校验 ==C=03H: ID为全0====控制码(C):"+c+",===:"+phyID);
        }
        System.out.println("======节点调光(42H)请求报文校验 ==phyID:"+phyID);
        return phyID;
    }
}
