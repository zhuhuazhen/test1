package com.hzyw.iot.util.constant;

import com.alibaba.fastjson.JSONArray;
import java.util.Arrays;
import java.util.List;

/**
 * pdt 指令参数校验
 */
public class PDTValidateUtil {
    /**
     * 通用校验 公共  响应类型
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateResComPdt(String cmdParam) throws Exception {
        boolean resBoolean=true;
        if ("02".equals(cmdParam) || "03".equals(cmdParam)) return false; //02 (02H) 表示报文校验有错误，返回错误码:02; 03: 系统忙
        return resBoolean;
    }

    /**
     * 集中器登录(F0H)响应报文校验 (部分指令的验证 通用)
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateResF0HPdt(String cmdParam) throws Exception {
        boolean resBoolean=true;
        if ("02".equals(cmdParam) || "03".equals(cmdParam)) return false; //02 (02H) 表示报文校验有错误，返回错误码:02; 03: 系统忙
        return resBoolean;
    }

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
     * 主动上报节点数据(F7H)/查询节点详细数据 响应报文校验
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateResF7HPdt(String cmdParam) throws Exception {
        boolean resBoolean=true;
        if ("02".equals(cmdParam) || "03".equals(cmdParam)) return false; //02 (02H) 表示报文校验有错误，返回错误码:02; 03: 系统忙
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
        boolean resBoolean=true;
        if ("02".equals(cmdParam) || "03".equals(cmdParam)) return false; //02 (02H) 表示报文校验有错误，返回错误码:02; 03: 系统忙
        return resBoolean;
    }

    /**
     * 查询集中器状态 (73H)响应报文校验
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateResT73HPdt(String cmdParam) throws Exception {
        boolean resBoolean=true;
        if ("02".equals(cmdParam) || "03".equals(cmdParam)) return false; //02 (02H) 表示报文校验有错误，返回错误码:02; 03: 系统忙
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
        if ("02".equals(cmdParam) || "03".equals(cmdParam)) return false; //02 (02H) 表示报文校验有错误，返回错误码:02; 03: 系统忙
        byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(cmdParam);
        if(bytePdtArrs.length>250) {
            resBoolean = false;
            System.out.println("======解析 '下发定时任务'(82H)响应的PDT报文错误！字节长度超过250个字节!");
        }
        return resBoolean;
    }

    public static boolean validateResTFEHPdt(String cmdParam) throws Exception {
        boolean resBoolean=true;
        if ("02".equals(cmdParam) || "03".equals(cmdParam)) return false; //02 (02H) 表示报文校验有错误，返回错误码:02; 03: 系统忙
        byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(cmdParam);
        if(bytePdtArrs.length>=250) throw new Exception(" 解析 '查询集中器状态'(73H)响应的PDT报文错误！字节长度必须小于250个字节!");
        return resBoolean;
    }

    public static boolean validateResT9BHPdt(String cmdParam) throws Exception {
        boolean resBoolean=true;
        if ("02".equals(cmdParam) || "03".equals(cmdParam)) return false; //02 (02H) 表示报文校验有错误，返回错误码:02; 03: 系统忙
        byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(cmdParam);
        if(bytePdtArrs.length>242) throw new Exception(" 解析 '查询集中器下PLC版本信息（9BH）响应的PDT报文错误！字节长度超过242个字节!");
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

    /**
     * 查询节点详细数据(45H) 请求报文校验
     * @param c
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateReqT45HPdt(String c,StringBuffer cmdParam)throws Exception{
        if("".equals(c)) return false;
        List<Object> paramList= JSONArray.parseArray(cmdParam.toString());
        String pdtParam="";
        if(paramList.size()>0) {
            JSONArray paramValArray = JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
            for (int h = 0; h < paramValArray.size(); h++) {
                System.out.print("======解析'查询节点详细数据(45H)===Array:" + paramValArray.getString(h));
                pdtParam+=ConverUtil.MappCODEVal(paramValArray.getString(h),true);
                System.out.println("=====解析'查询节点详细数据(45H)==11111====cmdParam:" +pdtParam);
            }
            byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(pdtParam);
            int pdtLen=bytePdtArrs.length;
            if(!PLCValidateUtil.rangeInDefined(pdtLen,0,6)) {
                System.out.println("======解析'查询节点详细数据(45H)'请求的PDT 指令参数(ID)长度有错误！字节长度范围：0~6字节!");
                return false;
            }
            paramValArray.set(0,validateNodeID(c,bytePdtArrs));
            paramList=new JSONArray();
            paramList.add(paramValArray);
            cmdParam.replace(0,cmdParam.length(),JSONArray.toJSONString(paramList));
            return true;
        }
        return true;
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
    
    /**
     * 设定电源时间(49H)
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateReqT49HPdt(String cmdParam) throws Exception {
        return true;
    }
    
    /**
     * 校验设定电源初始化值(4CH)
     * @param cmdParam
     * @return
     * @throws Exception
     */
    public static boolean validateReqT4CHPdt(String cmdParam) throws Exception {
        return true;
    }

	public static boolean validateReqT48HPdt(String cmdParam) {
		// TODO Auto-generated method stub
		return true;
	}

	public static boolean validateResT6DHPdt(String cmdParam) throws Exception {
		byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(cmdParam);
        if(bytePdtArrs.length>242) throw new Exception(" 解析 '查询电源报警阙值(6DH)响应的PDT报文错误！字节长度超过242个字节!");
        return true;
	}

	public static boolean validateReqT6CHPdt(String cmdParam) {
		// TODO Auto-generated method stub
		return true;
	}

	public static boolean validateResT4AHPdt(String cmdParam) throws Exception {
		byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(cmdParam);
        if(bytePdtArrs.length>242) throw new Exception(" 解析 '查询电源报警阙值(6DH)响应的PDT报文错误！字节长度超过242个字节!");
        return true;
	}

	public static boolean validateReqT9EHPdt(String cmdParam) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public static boolean validateResTF6HPdt(String cmdParam) throws Exception {
		byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(cmdParam);
        if(bytePdtArrs.length>29) throw new Exception(" 解析 '报警使能查询(F6H)响应的PDT报文错误！字节长度超过29个字节!");
        return true;
	}
}
