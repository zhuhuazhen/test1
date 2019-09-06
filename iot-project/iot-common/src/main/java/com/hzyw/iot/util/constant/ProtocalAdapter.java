package com.hzyw.iot.util.constant;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dataaccess.RequestDataVO;
/**
 * PLC 协议适配置器
 * 上行通信规约
 */
public class ProtocalAdapter{
	//public static ByteBuffer buffer=null;
    /**
     * 按指令生成对应的协议码报文
     * @param uuid 设备ID
     * @param code  指定控制码
     * @param cmd 命令码
     * @param paramBody 命令参数
     * @return
     */
    public static String  generaMessage(String uuid,String code, String cmd, String paramBody) throws Exception {
        //有控制码指定控制码，没有默认模板的控制码(应对"xxH"多控制码入参选择)
    	if(code!=null && !"".equals(code)) {
    		C_CODE_VAL.CMethod(code);
    	}else {
    		C_CODE_VAL.TxxH.setValue(null);
    	}
        //设备ID (校验16进制的长度6个字节)
        HEAD_TEMPLATE.setUID(checkDeviceUID(uuid));

        //生成"指令参数"的十六进制值
        System.out.println("===============111==生成[指令参数]的十六进制值:"+paramBody);
        paramBody=O_CODE_VAL.PDTTemplate(code,cmd,paramBody);
        System.out.println("=================生成[指令参数]的十六进制值:"+paramBody);
        HEAD_TEMPLATE.setPDT(paramBody);

        String L_SIZE=CLAC_L_SIZE(O_CODE_VAL.CmdValueMethod(cmd),paramBody);
        //计算“数据域”中所有数据的字节数；L=0 表示无数据域
        HEAD_TEMPLATE.setL(L_SIZE);

        //计算校验码(CS): 从“帧起始符”到校验码之前的所有字节的模256的和，即各字节二进制算术和，不计超过256 的溢出值
        HEAD_TEMPLATE.setCS(CLAC_CS_SUM(C_CODE_VAL.CValueMethod(code),L_SIZE,O_CODE_VAL.CmdValueMethod(cmd),paramBody));

        String reqMessage=O_CODE_VAL.OrderMethod(cmd);
        System.out.println("====reqMessage:"+reqMessage);
        System.out.println("====uid:"+HEAD_TEMPLATE.getUID());
        
        //校验生成的协议报文是否有错误
        byte[] resp=ConverUtil.hexStrToByteArr(reqMessage);
        boolean validateRes=ValidateProtocalMessage(resp);
        
        if (!validateRes) throw new Exception("协议报文的格式或校验有错误! 无法生成报文!");
        return reqMessage;
    }

    /**
     * 按指令生成对应请求码报文 (上游调用：KAFKA 或其它)
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public static String  messageRequest(JSONObject jsonObject) throws Exception {
    	try {
			String jsonStr=((JSONObject) jsonObject.get("data")).toJSONString();
			RequestDataVO requestVO=JSONObject.parseObject(jsonStr,RequestDataVO.class);
			Map<String,Object> methodMap=(Map<String, Object>)requestVO.getMethods().get(0);  //自定义扩展属性集合
			List<Map<String,Object>> inList=(List<Map<String,Object>>) methodMap.get("in"); //上报参数属性集合
			String uuid=jsonObject.getString("gwId"); //集中器ID
			String nodeID=requestVO.getId(); //节点ID  或 组ID
			String code=inList.get(0).get("code").toString(); //控制码
			String cmd=methodMap.get("method").toString(); //指令码
			List<String[]> pdtParams=(List<String[]>) inList.get(0).get("pdt"); //指令参数
			List<String[]> nodeArr=new ArrayList<String[]>();
			nodeArr.add(new String[]{nodeID});
			
			pdtParams=(nodeID!=null && !"".equals(nodeID))? nodeArr:pdtParams;
			String pdtParamsStr = JSONObject.toJSONString(pdtParams);
			
			System.out.println("=========按指令生成对应请求码报文KAFKA 集中器ID:"+uuid);
			System.out.println("=========按指令生成对应请求码报文KAFKA 节点ID:"+nodeID);
			System.out.println("=========按指令生成对应请求码报文KAFKA 控制码:"+code);
			System.out.println("=========按指令生成对应请求码报文KAFKA 指令码:"+cmd);
			System.out.println("=========按指令生成对应请求码报文KAFKA 指令参数:"+pdtParams);

			return generaMessage(uuid,code,cmd,pdtParamsStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "";
    }

    /**
     * 按指令生成对应响应码报文
     * 集中器->主机
     * @param resp
     * @return
     * @throws Exception
     */
    public static String  messageRespose(byte[] resp) throws Exception {
//        System.out.println("=====入参码resp:"+ConverUtil.convertByteToHexString(resp));
//        System.out.println("=====入参字节码数组长度:"+resp.length);
//        for(int j=0;j<resp.length;j++){
//            System.out.println("=====入参字节码数组长值:"+ConverUtil.convertByteToHexStr(resp[j]));
//        }
        
        if(resp.length<13) throw new Exception("协议报文长度有错误！总字节长度范围：13~267!");
        
        C_CODE_VAL.CMethod("80H");
        //System.out.println("=====提取的入参设备UUID go in...");
        String uuid="";//设备ID
        for(int i=1;i<7; i++) {
            uuid=uuid+ConverUtil.convertByteToHexStr(resp[i]);
        }
        System.out.println("=====提取的入参设备UUID:"+uuid);
        HEAD_TEMPLATE.setUID(uuid); //设备ID

        //校验集中器- 返回的协议报文 (01H: 成功 02H: 失败, 03H：主机忙  或 协议报文有错误)
        boolean validateRes=ValidateProtocalMessage(resp);
        if(!validateRes){
            throw new Exception("协议报文的格式或校验有错误! 无法生成报文!");
        }

        //从 集中器 返回的报文 中 提取入参
        String code=ConverUtil.convertByteToHexStr(resp[8])+"H"; //控制码
        String cmd=ConverUtil.convertByteToHexStr(resp[10])+"H";  //指令码
        //提取指令入参
        ByteBuffer buffer = ByteBuffer.allocate(resp.length-13);
        for(int i=11;i<resp.length-2; i++) {
    	   buffer.put(resp[i]);
        }
        String paramBody=ConverUtil.convertByteToHexString(buffer.array());
        System.out.println("=====提取的入参控制码:"+code);
        System.out.println("=====提取的入参指令码:"+cmd);
        System.out.println("=====提取的入参指令参数:"+paramBody);
       // return "";
        return generaMessage(uuid,"80H",cmd,paramBody);
    }

    /**
     * 校验响应协议报文的准确性
     * @param message 报文
     * @tip 异常提示信息
     * @return
     */
    public static boolean ValidateProtocalMessage(byte[] message) throws Exception {
        String byteToHexStr =ConverUtil.convertByteToHexString(message); //字节数组转16进制字符串
        message=ConverUtil.hexStrToByteArr(byteToHexStr); //16进制字符串转字节数组
        System.out.println("======协议报文总字节长度:"+message.length);
        if(!rangeInDefined(message.length,13,267)) {
            System.out.println("协议报文长度有错误！总字节长度范围：13~267!");
            return false;
        }
        if(!checkDataArea(message)) {
            System.out.println("协议报文模板的L(数据长度),CS(校验码) 与实际数据域长度不匹配!");
            return false;
        }
        if(!checkFixedValue(message)){
            System.out.println("协议报文模板的H,C,T 固定值有错误!");
            return false;
        }
        return true;
    }

    /**
     * 计算 数据域 字节数长度
     * @param params
     * @return
     */
    private static String CLAC_L_SIZE(String... params){
        byte[] byteArry=null;
        String sizeVal="00";
        int calcSum=0;
        try {
            if(params.length>0){
                for(int i=0;i<params.length;i++){
                    System.out.println("第"+(i+1)+"个数据域参数是"+params[i]+";");
                    byteArry= ConverUtil.hexStrToByteArr(params[i]);
                    System.out.println("第"+(i+1)+"个数据域字节长度是"+byteArry.length);
                    calcSum=byteArry.length+calcSum;
                }
                System.out.println("数据域 字节数计算和："+calcSum);
                sizeVal= ConverUtil.toHex(calcSum);  //10进制转16进制值
                sizeVal=sizeVal.length()%2!=0?"0".concat(sizeVal):"0".equals(sizeVal)? "00":sizeVal;
                //sizeVal=new DecimalFormat("00").format(sizeVal);
                System.out.println("计算和 10进制转16进制值结果："+sizeVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sizeVal;
    }

    /**
     * 计算 校验码和
     * @param params
     * @return
     */
    private static String CLAC_CS_SUM(String... params){
        String csStr=HEAD_TEMPLATE.H.getValue().concat(HEAD_TEMPLATE.UID.getValue().concat(HEAD_TEMPLATE.H.getValue().concat("%s%s%s%s")));
        System.out.println("计算 校验码和 模板："+csStr);
        if(params.length==4){
            csStr=String.format(csStr,params[0],params[1],params[2],params[3]);
            System.out.println("计算 校验码和 原值："+csStr);
            System.out.println("计算 校验码和 CS："+ConverUtil.makeChecksum(csStr));
            return ConverUtil.makeChecksum(csStr);
        }
        return "00";
    }

    /**
     * 数值区间比较
     * @param current
     * @param min
     * @param max
     * @return
     */
    public static boolean rangeInDefined(int current, int min, int max)
    {
        return Math.max(min, current) == Math.min(current, max);
    }

   /**
        * 检查协议报文的数据长度，校验码
     * @param message
     * @return
     */
    public static boolean checkDataArea(byte[] message){
        String L=ConverUtil.convertByteToHexStr(message[9]); //L
        System.out.println("====检查协议报文的数据长度，校验码=L:"+L);
        String CS=ConverUtil.convertByteToHexStr(message[message.length-2]);  //CS
        System.out.println("====检查协议报文的数据长度，校验码=CS:"+CS);

        String code=ConverUtil.convertByteToHexStr(message[8])+"H"; //控制码
        System.out.println("====检查协议报文的数据长度，校验码=控制码:"+code);
        String cmd=ConverUtil.convertByteToHexStr(message[10])+"H"; //指令码

        System.out.println("====检查协议报文的数据长度，校验码=指令码:"+ cmd);
        String paramBody=""; //指令参数
        for(int i=11;i<message.length-2; i++) {
            paramBody=paramBody+ConverUtil.convertByteToHexStr(message[i]);
        }
        System.out.println("====检查协议报文的数据长度，校验码=paramBody:"+paramBody);
        //计算“数据域”中所有数据的字节数；L=0 表示无数据域
        String L_SIZE=CLAC_L_SIZE(O_CODE_VAL.CmdValueMethod(cmd),paramBody);
        System.out.println("====检查协议报文的数据长度，校验码=L_SIZE:"+L_SIZE);
        //计算校验码(CS): 从“帧起始符”到校验码之前的所有字节的模256的和，即各字节二进制算术和，不计超过256 的溢出值
        String CS_SIZE=CLAC_CS_SUM(C_CODE_VAL.CValueMethod(code),L_SIZE,O_CODE_VAL.CmdValueMethod(cmd),paramBody);
        return L.equals(L_SIZE) && Integer.parseInt(L,16)<=255 && CS.equals(CS_SIZE);
    }

    /**
     * 检查协议模板固定值 H,C,T
     * @param message
     * @return
     */
    public static boolean checkFixedValue(byte[] message){
        System.out.println(String.format("====检查协议模板固定值 GO IN... "));
        String H= ConverUtil.convertByteToHexStr(message[0]);
        String H1= ConverUtil.convertByteToHexStr(message[7]);
        String C= ConverUtil.convertByteToHexStr(message[8])+"H";
        String CMD= ConverUtil.convertByteToHexStr(message[10])+"H";
        String T= ConverUtil.convertByteToHexStr(message[message.length-1]);
        System.out.println(String.format("====检查协议模板固定值 H:%s, H1:%s, C:%s, CMD:%s ",H,H1,C,CMD));
        C=C_CODE_VAL.CValueMethod(C);
        CMD=O_CODE_VAL.CmdValueMethod(CMD);
        return "68".equals(H) && "68".equals(H1) && "16".equals(T) && !"".equals(C) && !"".equals(CMD);
    }

    /**
     * 检查设备ID的16进制长度是否6个字节，不满足高位补0,超过报异常
     * @param uid
     * @return
     * @throws Exception
     */
    public static String checkDeviceUID(String uid) throws Exception{
        byte[]uidByte=ConverUtil.hexStrToByteArr(uid);
        if(uidByte.length>6) throw new Exception("PLC 设备UUID的16进制长度超过了6个字节!");
        String UUID=ConverUtil.convertUUIDByteToHexString(uidByte);
        System.out.println("===========222222:"+UUID);
        return UUID;
    }

    public static void main(String[] args){
        try {
            //*****************主机->集中器    请求**************************
            //System.out.println(generaMessage("000000000100",null,"70H","03")); //集中器继电器开  T70H
            
            //68 00 00 00 00 00 01 68 00 01 73 45 16
           // System.out.println(generaMessage("000000000001",null,"73H",null)); //查询集中器状态(73H)
            
           //System.out.println(generaMessage("0122","01H","46H","000001000156")); //查询节点传感器信息
            //System.out.println(generaMessage("000000000100","00H","66H",null));//设置PLC-2480存储节点列表指令(66H)
        	
            //System.out.println(generaMessage("000000000100","80H","f0H","01"));
            
        	//System.out.println(generaMessage("012200","00H","70H","12")); 
        	
            //下发定时任务(82H) 模拟 复杂PDT入参解析 请求模板
            // 68 00 00 00 00 00 01 68 00 01 73 45 16
              JSONObject jsonObj=getRequestVO("000000000001","00H","82H");
              //System.out.println("====11111====messageRequest RESULT:"+jsonObj.toJSONString());
			  System.out.println("========messageRequest RESULT:"+messageRequest(jsonObj));



            //*************集中器->主机  响应  **************************
            byte[] byteArrs=null;
            
            //查询集中器状态(73H)   模拟 复杂PDT出参解析 响应模板
            /* String ss="6800000000000168802273089808B608FC27102648251C086C085D0873193C62626362003DB3010EA622BA02E316"; //查询集中器状态(73H)
              byteArrs=ConverUtil.hexStrToByteArr(ss);
              System.out.println(messageRespose(byteArrs));*/

        	 /*
              //String ss="68000000000001680401f0c616"; ////集中器登录(F0H)
              
              String ss="680000000000016880027001c416";  //集中器继电器开（70H） 响应的
              byteArrs=ConverUtil.hexStrToByteArr(ss);
              System.out.println(messageRespose(byteArrs)); //
             
			 * byte[] byteArrs=null; String ss="680000000000016880027001C416";
			 * byteArrs=ConverUtil.hexStrToByteArr(ss);
			 * System.out.println(messageRespose(byteArrs));
			 */
              

            //System.out.println("=======:"+checkDeviceUID("25611011"));
            //System.out.println(messageRequest("80H","F0H","01")); //集中器登录(F0H)
        } catch (Exception e) {
            System.out.println("=====PLC Exception: "+e.getMessage());
        }
    }


    public static JSONObject getRequestVO(String uuid,String code,String cmd){
        String msgId="31a8c447-5079-4e91-a364-1769ac06fd5c";
        MessageVO<RequestDataVO> mesVO=new MessageVO<RequestDataVO>();
        mesVO.setType("request");
        mesVO.setTimestamp(1566205651);
        mesVO.setMsgId(msgId);
        mesVO.setGwId(uuid);
        mesVO.setData(getDataVO(code,cmd));  //000000000001
        JSONObject jsonObj= (JSONObject) JSONObject.toJSON(mesVO);
        return jsonObj;
    }
    
    private static RequestDataVO getDataVO(String code,String cmd) {
    	RequestDataVO dataVO=new RequestDataVO();
    	List<Map>methods=new ArrayList<Map>();
    	List<Map>ins=new ArrayList<Map>();
    	Map<String,Object>dataMap=new HashMap<String, Object>();
    	Map<String,Object>inMap=new HashMap<String, Object>();
    	
    	inMap.put("code", code); //00H
    	inMap.put("pdt",getPdtParams());
    	ins.add(inMap);
    	
    	dataMap.put("method", cmd); //82H
    	dataMap.put("in", ins);
    	
    	methods.add(dataMap);
    	
    	dataVO.setId("");
    	dataVO.setMethods(methods);
    	return dataVO;
    }
    
    private static List<String[]> getPdtParams(){
    	List<String[]> pdtList=new ArrayList<String[]>();
		//pdtList.add(new String[]{"200","040919","051119","02H","0","00H","5922","80","02H","12","42H","C8H","01H"});
		pdtList.add(new String[]{"200","040919","051119","02H","0","00H","5922","80","02H","12","42H","C8H","01H"});
		return pdtList;
    }
}