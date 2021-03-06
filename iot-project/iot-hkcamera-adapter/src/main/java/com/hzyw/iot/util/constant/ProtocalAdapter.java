package com.hzyw.iot.util.constant;

import com.alibaba.fastjson.JSONObject;
import com.hzyw.iot.vo.dataaccess.DataType;
import com.hzyw.iot.vo.dataaccess.RequestDataVO;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PLC 协议适配置器
 * 上行通信规约
 */
public class ProtocalAdapter{
    //public static ByteBuffer buffer=null;
    /**
     * 按指令生成对应的协议码报文
     * @param uuid 设备ID(必填)
     * @param code  指定控制码  (格式 如：00H) 动态值时(必填)
     * @param cmd 命令码(必填) (格式 如：F0H)
     * @param paramBody 命令参数
     * @return
     */
    public static String  generaMessage(String uuid,String code, String cmd, String paramBody,ChannelHandlerContext ctx) throws Exception {
        //有控制码指定控制码，没有默认模板的控制码(应对"xxH"多控制码入参选择)
        if(code!=null && !"".equals(code)) {
            //如果是响应方法(messageRespose)调过来的，这里设值自动无效，以 响应方法里设置为准
            if(!"80H".equals(code)) C_CODE_VAL.CMethod(code);
        }else {
            C_CODE_VAL.TxxH.setValue(null);
        }
        //如果控制码传参为空，从指令模板中取默认值
        if("".equals(code) || code==null) code=O_CODE_VAL.CodeNameMethod(cmd);

        //响应类型的 后面的协议报文不用校验，直接返回PDT的JSON转换结果
        /*if("80H".equals(code)){
            //根据指令调PDT模板，生成相应指令参数
            paramBody=O_CODE_VAL.PDTTemplate(code,cmd,paramBody);
            //System.out.println("========PDT(响应) 解析后JSON结果:"+paramBody);
            return paramBody;
        }*/

        //设备ID (校验16进制的长度6个字节)
        HEAD_TEMPLATE.setUID(checkDeviceUID(uuid));

        //生成"指令参数"的十六进制值
        System.out.println("================待生成[指令参数]十六进制值的原值入参:"+paramBody);
        //根据指令调PDT模板，生成相应指令参数
        paramBody=O_CODE_VAL.PDTTemplate(code,cmd,paramBody,ctx);
        System.out.println("===========根据指令调PDT模板 生成[指令参数]的十六进制值:"+paramBody);
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
        if (!validateRes) throw new Exception("主机->设备 的 协议报文的格式或校验有错误! 无法生成报文!");
        return reqMessage;
    }

    /**
     * 按指令生成对应请求码报文 (上游调用：KAFKA 或其它)
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public static String  messageRequest(JSONObject jsonObject) throws Exception {
        //与上游对接 入参格式的适配
       //jsonObject=RequestFormatAdapter(jsonObject);

        try {
            String requestType= jsonObject.get("type").toString();
            if(!requestType.equals(DataType.Request.getMessageType()))
                throw new Exception("PLC 消息类型 错误! 请检查'type'入参 ");

            String jsonStr=((JSONObject) jsonObject.get("data")).toJSONString();
            RequestDataVO requestVO=JSONObject.parseObject(jsonStr,RequestDataVO.class);
            Map<String,Object> methodMap=(Map<String, Object>)requestVO.getMethods().get(0);  //自定义扩展属性集合
            List<Map<String,Object>> inList=(List<Map<String,Object>>) methodMap.get("in"); //上报参数属性集合
            String uuid=jsonObject.getString("gwId"); //集中器ID
            String nodeID=requestVO.getId(); //节点ID  或 组ID
            String code= StringUtils.trimToNull(inList.get(0).get("code")+""); //控制码
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

            return generaMessage(uuid,code,cmd,pdtParamsStr,null);
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
    public static String  messageRespose(byte[] resp, ChannelHandlerContext ctx) throws Exception {
        String paramBody="";
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
        boolean validateRes= false;
        try {
            validateRes = ValidateProtocalMessage(resp);
            //协议报文 校验是否成功? 01H: 成功 02H: 失败
            paramBody=validateRes? "01":"02";
        } catch (Exception e) {
            System.out.println("=====集中器->主机 的 协议报文的格式或校验有错误! 无法生成报文!");
            paramBody="03"; //主机忙 或异常错误
            //throw new Exception("协议报文的格式或校验有错误! 无法生成报文!");
            e.printStackTrace();
        }
        //if(!validateRes) paramBody="02";//响应失败;
        //从 集中器 返回的报文 中 提取入参
        String code=ConverUtil.convertByteToHexStr(resp[8])+"H"; //控制码
        String cmd=ConverUtil.convertByteToHexStr(resp[10])+"H";  //指令码
        //提取指令入参
        ByteBuffer buffer = ByteBuffer.allocate(resp.length-13);
        for(int i=11;i<resp.length-2; i++) {
            buffer.put(resp[i]);
        }
        String pdtContent=ConverUtil.convertByteToHexString(buffer.array()); //指令入参
        paramBody="".equals(pdtContent)?paramBody:pdtContent; //指令参数为空时,设返回状态码
        System.out.println("======messageRespose方法==提取的入参控制码:"+code);
        System.out.println("======messageRespose方法==提取的入参指令码:"+cmd);
        System.out.println("======messageRespose方法==提取的入参指令参数:"+paramBody);
        return generaMessage(uuid,"80H",cmd,paramBody,ctx);
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
        Integer calcSum=0;
        try {
            if(params.length>0){
                for(int i=0;i<params.length;i++){
                    System.out.println("第"+(i+1)+"个数据域参数是"+params[i]+";");
                    byteArry= ConverUtil.hexStrToByteArr(params[i]);
                    System.out.println("第"+(i+1)+"个数据域字节长度是"+byteArry.length);
                    calcSum=byteArry.length+calcSum;
                }
                System.out.println("数据域 字节数计算和："+calcSum);
                //sizeVal= ConverUtil.toHex(calcSum);  //10进制转16进制值
                sizeVal=DecimalTransforUtil.toHexStr(String.valueOf(calcSum),1); //10进制转16进制值
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
        String L=StringUtils.lowerCase(ConverUtil.convertByteToHexStr(message[9])); //L
        System.out.println("====检查协议报文的数据长度，校验码=L:"+L);
        String CS=StringUtils.lowerCase(ConverUtil.convertByteToHexStr(message[message.length-2]));  //CS
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
        String L_SIZE=StringUtils.lowerCase(CLAC_L_SIZE(O_CODE_VAL.CmdValueMethod(cmd),paramBody));
        System.out.println("====检查协议报文的数据长度，校验码=L_SIZE:"+L_SIZE);
        //计算校验码(CS): 从“帧起始符”到校验码之前的所有字节的模256的和，即各字节二进制算术和，不计超过256 的溢出值
        String CS_SIZE=StringUtils.lowerCase(CLAC_CS_SUM(C_CODE_VAL.CValueMethod(code),L_SIZE,O_CODE_VAL.CmdValueMethod(cmd),paramBody));

        System.out.println("====检查协议报文的数据长度，校验码 比较结果:"+L_SIZE);
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

    /**
     * 请求 测试入口
     * @param code
     * @param cmd
     * @return
     * @throws Exception
     */
    public static String testRequestCode(String code, String cmd)throws Exception{
        JSONObject jsonObj=T_RequestVO.getRequestVO("000000000001",code,cmd);
        return messageRequest(jsonObj);
    }

    /**
     * 响应 测试入口
     * @param resp
     * @return
     * @throws Exception
     */
    public static String testResponseCode(String resp) throws Exception {
        byte[] byteArrs=ConverUtil.hexStrToByteArr(resp);
        return messageRespose(byteArrs,null);
    }

    /**
     * 与上游对接入参格式的适配
     * @param jsonObj
     * @return
     */
    private static JSONObject RequestFormatAdapter(JSONObject jsonObj){
        String requestType= jsonObj.get("type").toString(); //消息类型
        String uuid=jsonObj.get("gwId").toString(); //PLC设备ID
        String msgID=jsonObj.get("msgId").toString(); //消息ID

        String jsonData=((JSONObject) jsonObj.get("data")).toJSONString();
        RequestDataVO requestVO=JSONObject.parseObject(jsonData,RequestDataVO.class);
        String nodeID=requestVO.getId(); //节点ID  或 组ID

        Map<String,Object> methodMap=(Map<String, Object>)requestVO.getMethods().get(0);  //自定义扩展属性集合
        List<Map<String,Object>> inList=(List<Map<String,Object>>) methodMap.get("in"); //上报参数属性集合
        String cmd=methodMap.get("method").toString(); //指令码
        Map<String,Object>pdtMap=inList.get(0);
        if("set_onoff".equals(cmd)){
            cmd="42H";
        }
        String onoff=StringUtils.trimToNull(pdtMap.get("onoff")+"");  //0:关灯; 1: 开灯
        String dim=StringUtils.trimToNull(pdtMap.get("level")+"");
        dim="0".equals(onoff)? "0":("".equals(dim) || dim==null)? "100":dim;

        if("0".equals(onoff)){
            System.out.println("========节点调光(42H)=======执行关灯操作, 调光值："+dim);
        }else if("1".equals(onoff)){
            System.out.println("========节点调光(42H)=======执行开灯操作, 调光值："+dim);
        }else{
            System.out.println("========节点调光(42H)=======执行空操作, 调光值："+dim);
        }

        String code="03H";//控制码  上游没有 控制码 入参，暂定值:03H 广播类型

        if(!"000000000100".equals(uuid))uuid="000000000100";
        JSONObject tragetObj=T_RequestVO.getRequestVO(uuid,code,cmd,msgID,dim);
        return tragetObj;
    }
}