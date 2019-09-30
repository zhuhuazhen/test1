package com.hzyw.iot.util.constant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hzyw.iot.utils.PlcProtocolsUtils;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dataaccess.ResponseDataVO;
import io.netty.channel.ChannelHandlerContext;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 指令码值 枚举集合
 */
public enum O_CODE_VAL{
    /* 集中器继电器开 */
    T70H("70",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),70)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception {
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                // 控制对象	Bit0- Bit2 每一位对应一路继电器。为1有效	1B
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0){
                    JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
                    String hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(0),1);
                    System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                    byte[] codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                    if(codeByte.length>1)throw new Exception("pdt param length is 1B!");
                    pdtMesg=hexStr;
                }
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========集中器继电器开(70H) 响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            System.out.println("*************集中器继电器开(70H) 响应的PDT解析 结果:" +pdtMesg);
            return pdtMesg;
        }
    },
    /* 集中器继电器关 */
    T71H("71",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),71)+"",C_CODE_VAL.T00H.getValue())
            {
                @Override
                public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx) throws Exception{
                    if("".equals(cmdParam) || cmdParam==null) return "";
                    String pdtMesg="";
                    //区分 请求:true、响应:false 类型(80:响应;非80:请求)
                    boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
                    System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
                    if(isRequest){ //pdt 请求报文解析处理
                        // 控制对象	Bit0- Bit2 每一位对应一路继电器。为1有效	1B
                        List<Object> paramList= JSONArray.parseArray(cmdParam);
                        if(paramList.size()>0){
                            JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
                            String hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(0),1);
                            System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                            byte[] codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                            if(codeByte.length>1)throw new Exception("pdt param length is 1B!");
                            pdtMesg=hexStr;
                        }
                    }else { //pdt 响应报文解析处理
                        if (PDTValidateUtil.validateResComPdt(cmdParam)){
                            //对响应状态码，发kafka
                            System.out.println("==========集中器继电器关(71H) 响应结果 发KAFKA 操作......");
                            pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                        }else{
                            pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                        }
                    }
                    return pdtMesg;
                }
            },
    /* 查询集中器状态 */
    T73H("73",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),73)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx) throws Exception{
            /*
             * 参数 字节长度的二维数组模板（响应类型）
             * 一维：第个参数值的固定字节长度；
             * 二维： 解析参数值方法【1:十进制转,2:二进制转 3:码映射，4：直接赋值】）
             * 三维：属性值单位【十进制转时:1、相电压,2、相电流,3、相功率,4、相功率因数,5、电能,6、AD输入电压】; 二进制转时【转二进制保留位数】
             * 注：请求模板 不考虑二进制位的转换
             */
            int[][] byteLenResTemp=new int[][]{{2,1,1,0},{2,1,1,0},{2,1,1,0},{2,1,2,0},{2,1,2,0},{2,1,2,0},{2,1,3,0},{2,1,3,0},
                    {2,1,3,0},{2,1,3,0},{1,1,4,0},{1,1,4,0},{1,1,4,0},{1,1,4,0},{3,1,5,0},
                    {1,2,3,0},{2,1,6,0},{2,1,6,0},{1,2,2,0}};

            //参数属性名模板
            String[] paramNameTemp=new String[] {"A相电压","B相电压","C相电压","A相电流","B相电流","C相电流","A相功率","B相功率","C相功率","总功率","PFa","PFb","PFc",
                    "PFs","电能","继电器状态","AD1路输入电压","AD2路输入电压","光耦输入电平"};

            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception("查询集中器状态(73H),不用传 请求指令参数入参!");
            }else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                if (PDTValidateUtil.validateResT73HPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam,ctx);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"73H", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======解析 查询集中器状态(73H) 响应PDT协议报文的 结果:" +pdtMesg);
                    //String mesageID=ProtocalAdapter.consumeRequestID(HEAD_TEMPLATE.getUID().concat("_73H")); //响应 消费对应 请求消息ID
                    //PlcProtocolsUtils.plcACKResponseSend(null,null,null,20324,null);
                    //封装成指定JSON结构返回 或 调KAFKA发送报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 下发定时任务 */
    T82H("82",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),82)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx) throws Exception{
            /*
             * 参数 字节长度的二维数组模板(请求类型)
             * 一维：第个参数值的固定字节长度；
             * 二维： 解析参数值方法【1:进制转, 2:码映射，3：直接赋值】）
             * 请求模板 不考虑二进制位的转换
             */
            int[][] byteLenReqTemp=new int[][]{{1,1},{3,1},{3,1},{1,2},{1,1},{1,2},{2,1},{1,1},{1,2},{1,1},{1,2},{1,2},{1,2}};
            /*
             * 参数 字节长度的二维数组模板（响应类型）
             * 一维：第个参数值的固定字节长度；
             * 二维： 解析参数值方法【1:十进制转,2:二进制转 3:码映射，4：直接赋值】）
             * 请求模板 不考虑二进制位的转换
             */
            int[][] byteLenResTemp=new int[][]{{1,3,0,0},{250,1,0,0}};
            //参数属性名模板
            String[] paramNameTemp=new String[] {"ACK","TN"};

            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //校验pdt参数（规则依据文档）
                if (PDTValidateUtil.validateReqT82HPdt(cmdParam)) {
                    return PDTAdapter.pdtRequstParser(byteLenReqTemp, cmdParam);
                }
            }else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                if (PDTValidateUtil.validateResT82HPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam,ctx);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"82H", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======解析 下发定时任务(82H) 响应PDT协议报文的 结果:" +pdtMesg);
                    //封装成指定JSON结构返回 或 调KAFKA发送报文
                }else{
                    pdtMesg=ConverUtil.MappCODE(cmdParam);
                }
            }
            return pdtMesg;
        }
    },
    /* 查询定时任务 */
    T83H("83",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),83)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception("查询定时任务(83H),不用传 请求指令参数入参!");
            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 清除定时任务 */
    T84H("84",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),84)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 设置集中器时间 */
    T8CH("8c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"8c")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 设置集中器参数 */
    T8EH("8e",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"8e")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 查询集中器参数 */
    T8FH("8f",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"8f")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 下发节点 */
    T96H("96",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),96)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 读取节点 */
    T97H("97",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T03H.getValue(),97)+"",C_CODE_VAL.T03H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T03H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 配置节点 */
    T98H("98",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T03H.getValue(),98)+"",C_CODE_VAL.T03H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T03H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 删除节点 */
    T99H("99",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),99)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(!"80H".equals(c)){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 集中器登录 集中器->主机 */
    F0H("f0",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T04H.getValue(),"f0")+"",C_CODE_VAL.T04H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            boolean isRequest=!"80".equals(C_CODE_VAL.T04H.getValue());  //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception("集中器登录(F0H),不用传 请求指令参数入参!");
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResF0HPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========集中器登录(F0H) 响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            System.out.println("========pdt模析解析【集中器登录】 返回出参："+cmdParam);
            //
            return pdtMesg;
        }
    },
    /* 集中器与主机保持连接心跳 集中器->主机 */
    F1H("f1",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"f1")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());  //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 系统控制 */
    F2H("f2",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f2")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());  //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 集中器报警 集中器->主机 */
    F3H("f3",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"f3")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());  //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 执行失败返回 */
    F4H("f4",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T04H.getValue(),"f4")+"",C_CODE_VAL.T04H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            boolean isRequest=!"80".equals(C_CODE_VAL.T04H.getValue());  //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 报警能使设置 */
    F5H("f5",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f5")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());  //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 报警能使查询 */
    F6H("f6",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f6")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());  //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 节点调光 */
    T42H("42",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),42)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            /*
             * 参数 字节长度的二维数组模板(请求类型)
             * 一维：第个参数值的固定字节长度；
             * 二维： 解析参数值方法【1:进制转, 2:码映射，3：直接赋值】）
             * 请求模板 不考虑二进制位的转换
             */
            int[][] byteLenReqTemp=new int[][]{{6,1},{1,2},{1,2}};

            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //校验pdt参数（规则依据文档）
                StringBuffer paramBuffer=new StringBuffer(cmdParam);
                if (PDTValidateUtil.validateReqT42HPdt(c,paramBuffer)) {
                    cmdParam=paramBuffer.toString();
                    pdtMesg=PDTAdapter.pdtRequstParser(byteLenReqTemp,cmdParam);
                }
            }else { //pdt 响应报文解析处理
                pdtMesg=ConverUtil.MappCODE(cmdParam);
                System.out.println("*************节点调光(42H) 响应的PDT解析 结果:" +pdtMesg);
                Integer mesgCode=0;
                String mesageID="";
                /*01H：集中器成功受理.
                  02H：命令或数据格式无效.
                  03H：集中器忙*/
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                    //mesageID=ProtocalAdapter.consumeRequestID(HEAD_TEMPLATE.getUID().concat("_42H")); //响应 消费对应 请求消息ID
                    System.out.println("==========节点调光(42H) 响应结果 发KAFKA 操作......");
                    //PlcProtocolsUtils.plcACKResponseSend(HEAD_TEMPLATE.getUID(),"42H",null,mesgCode,mesageID,null);
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1);
                    //02：失败 (命令或数据格式无效); 03：忙 (集中器忙)
                    //mesgCode="2".equals(pdtMesg)?10005:20324;
                   // mesageID=ProtocalAdapter.consumeRequestID(HEAD_TEMPLATE.getUID().concat("_42H")); //响应 消费对应 请求消息ID
                   // PlcProtocolsUtils.plcACKResponseSend(HEAD_TEMPLATE.getUID(),"42H",null,mesgCode,mesageID,null);
                }
            }
            return pdtMesg;
        }
    },
    /* 主动上报节点数据 集中器->主机 */
    F7H("f7",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T04H.getValue(),"f7")+"",C_CODE_VAL.T04H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            /*
             * 参数 字节长度的多维数组模板（响应类型）
             * 一维：第个参数值的固定字节长度；
             * 二维：解析参数值方法【1:十进制转(11:有符号整型进制转; 12:浮点型进制转),2:二进制转 3:码映射，4：直接赋值】）
             * 三维：属性值单位【十进制转时:1、相电压(V),2、相电流(A),3、相功率(W),4、相功率因数(%),5、电能(kWh),6、AD输入电压(0.00V),7、毫安电流(mA),8、温度单位(.C),9、小时(h)】;
             *                 二进制转时【转二进制保留位数,为0时 默认字节8位倍数 保留位数】
             * 四维：统计属性字段【如： 节点总数、当前帧数、总帧数】0：默认不是，1：是
             * 请求模板 不考虑二进制位的转换
             */
            //非路灯控制器设备【路灯电源】 老程序
            int[][] byteLenResTemp_OLD=new int[][]{{1,1,0,1},{6,1,0,0},{1,3,0,0},{1,3,0,0},{2,1,6,0},{2,1,2,0},{2,1,3,0},{1,1,4,0},
                    {2,2,16,0},{1,1,0,0}};
            //非路灯控制器设备【路灯电源】 新程序
            int[][] byteLenResTemp_NEW=new int[][]{{1,1,0,1},{6,1,0,0},{1,3,0,0},{1,3,0,0},{2,1,6,0},{2,1,2,0},{2,1,3,0},{1,1,4,0},
                    {2,2,16,0},{1,1,0,0},{1,11,8,0},{2,1,3,0},{2,1,9,0},{2,1,5,0},{2,1,9,0}};

            //状态: 位(bit)属性名模板
            Object[][] stateBitTemp=PLC_CONFIG.StateBitTemp();
            //参数属性名模板
            Object[][] paramNameTemp=PLC_CONFIG.paramNameTF7HTemp(byteLenResTemp_OLD,byteLenResTemp_NEW);

            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            boolean isRequest="80".equals(C_CODE_VAL.T04H.getValue())?false:true;  //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理 注:[该指令 文档上没有请求的需求]
            }else { //pdt 响应报文解析处理
                AtomicInteger indexNum=new AtomicInteger(0);
                if (PDTValidateUtil.validateResF7HPdt(cmdParam,indexNum)) {
                    String[] paramNamesTemp= (String[]) paramNameTemp[indexNum.intValue()][0];
                    int[][] byteLenResTemp= (int[][]) paramNameTemp[indexNum.intValue()][1];
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNamesTemp,cmdParam,ctx);

                    //处理 二进制位 属性名映射 解析
                    List<Map<String,Object>>pdtList= (List<Map<String, Object>>) pdtResposeVO.get("pdtList");
                    pdtResposeVO.put("pdtList",pdtList);
                    BitResposeParser(pdtList,stateBitTemp,ctx);

                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"F7H", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======解析 主动上报节点数据(f7H) 响应PDT协议报文的 结果:" +pdtMesg);
                    //调KAFKA发送 json报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 查询节点详细数据 【注： 这个指令 属性名映射需求 复杂 代码有点问题， 后面待细化处理】*/
    T45H("45",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),45)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            /*
             * 参数 字节长度的二维数组模板(请求类型)
             * 一维：第个参数值的固定字节长度；
             * 二维： 解析参数值方法【1:进制转, 2:码映射，3：直接赋值】）
             * 请求模板 不考虑二进制位的转换
             */
            int[][] byteLenReqTemp=new int[][]{{6,1}};

            /*
             * 参数 字节长度的多维数组模板（响应类型）
             * 一维：第个参数值的固定字节长度；
             * 二维：解析参数值方法【1:十进制转(11:有符号整型进制转; 12:浮点型进制转),2:二进制转 3:码映射，4：直接赋值】）
             * 三维：属性值单位【十进制转时:1、相电压(V),2、相电流(A),3、相功率(W),4、相功率因数(%)【41:A\B路亮度(0~200对应0~100%)】,5、电能(kWh),6、AD输入电压(0.00V),7、毫安电流(mA),8、温度单位(.C),9、小时(h)】;
             *                 二进制转时【转二进制保留位数,为0时 默认字节8位倍数 保留位数】
             * 四维：统计属性字段【如： 节点总数、当前帧数、总帧数】0：默认不是，1：是
             * 请求模板 不考虑二进制位的转换
             */
            /*int[][] byteLenResTemp_OLD=new int[][]{{6,1,0,0},{1,3,0,0},{1,3,0,0},{1,1,8,0},{2,1,6,0},{2,1,6,0},{2,1,7,0},{2,1,7,0},
                                                   {2,1,3,0},{1,1,4,0},{1,1,41,0},{2,2,16,0},{1,1,0,0}}; */
            int[][] byteLenResTemp_OLD=new int[][]{{6,1,0,0},{1,3,0,0},{1,3,0,0},{1,1,8,0},{2,1,6,0},{2,1,6,0},{2,1,7,0},{2,1,7,0},
                    {1,1,4,0},{1,1,41,0},{2,2,16,0},{1,1,0,0}}; //{2,1,3,0},
            int[][] byteLenResTemp_NEW=new int[][]{{6,1,0,0},{1,3,0,0},{1,3,0,0},{1,1,8,0},{2,1,6,0},{2,1,6,0},{2,1,7,0},{2,1,7,0},
                    {2,1,3,0},{1,1,4,0},{1,1,41,0},{2,2,16,0},{1,1,0,0},
                    {1,11,8,0},{2,1,3,0},{2,1,9,0},{2,1,5,0},{2,1,9,0}};
            int[][] byteLenResTemp_SINGLE=new int[][]{{6,1,0,0},{1,3,0,0},{1,3,0,0},{1,1,8,0},{2,1,6,0},{2,1,7,0},{2,1,3,0},{1,1,4,0},
                    {1,1,41,0},{2,2,16,0},{1,1,0,0}};
            int[][] byteLenResTemp_DOUBLE=new int[][]{{6,1,0,0},{1,3,0,0},{1,3,0,0},{1,1,8,0},{2,1,6,0},{2,1,7,0},{2,1,7,0},
                    {2,1,4,0},{2,1,4,0},{1,1,41,0},{1,1,41,0},{2,2,16,0},{1,1,0,0}};
            //状态: 位(bit)属性名模板
            Object[][] stateBitTemp=PLC_CONFIG.StateBitTemp();
            //参数属性名模板
            Object[][] paramNameTemp=PLC_CONFIG.paramNameT45HTemp(byteLenResTemp_OLD,byteLenResTemp_NEW,byteLenResTemp_SINGLE,byteLenResTemp_DOUBLE);

            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //校验pdt参数（规则依据文档）
                StringBuffer paramBuffer=new StringBuffer(cmdParam);
                if (PDTValidateUtil.validateReqT45HPdt(c,paramBuffer)) {
                    cmdParam=paramBuffer.toString();
                    pdtMesg=PDTAdapter.pdtRequstParser(byteLenReqTemp,cmdParam);
                }
            }else { //pdt 响应报文解析处理
                AtomicInteger indexNum=new AtomicInteger(0);
                if (PDTValidateUtil.validateResT45HPdt(cmdParam,indexNum)) {
                    String[] paramNamesTemp= (String[]) paramNameTemp[indexNum.intValue()][0];
                    int[][] byteLenResTemp= (int[][]) paramNameTemp[indexNum.intValue()][1];
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNamesTemp,cmdParam,ctx);

                    //处理 二进制位 属性名映射 解析
                    List<Map<String,Object>>pdtList= (List<Map<String, Object>>) pdtResposeVO.get("pdtList");
                    pdtResposeVO.put("pdtList",pdtList);
                    BitResposeParser(pdtList,stateBitTemp,ctx);

                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"F7H", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======解析 查询节点详细数据(45H) 响应PDT协议报文的 结果:" +pdtMesg);
                    //调KAFKA发送 json报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 查询和上传历史数据 */
    FBH("fb",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"fb")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0){
                    JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
                    String hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(0),1);
                    System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                    byte[] codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                    if(codeByte.length != 1)throw new Exception("pdt param length is 1B!");
                    pdtMesg=hexStr;
                }
            }
            return pdtMesg;
        }
    },
    /* 设置集中器远程更新IP和端口 */
    FCH("fc",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"fc")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========设置集中器远程更新IP和端口（FCH）响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 查询集中器远程更新IP和端口 */
    FDH("fd",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"fd")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception("查询集中器远程更新IP和端口（FDH）,不用传 请求指令参数入参!");

            }else { //pdt 响应报文解析处理
            }
            return pdtMesg;
        }
    },
    /* 查询集中器组网情况 */
    T9AH("9a",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"9a")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception("查询集中器组网情况（9AH）,不用传 请求指令参数入参!");
            }else { //pdt 响应报文解析处理
            }
            return pdtMesg;
        }
    },
    /* 查询集中器版本信息 */
    T9BH("9b",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"9b")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception("查询集中器版本信息（9BH）,不用传 请求指令参数入参!");
            }else {
                //校验pdt参数（规则依据文档）
                /*
                 * 参数 字节长度的二维数组模板
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:十进制转,2:二进制转 3:码映射，4：直接赋值】）
                 * 三维：属性值单位【十进制转时:1、相电压,2、相电流,3、相功率,4、相功率因数,5、电能,6、AD输入电压】; 二进制转时:转二进制保留位数
                 * 请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenResTemp=new int[][]{{4,1,0,0},{2,1,0,0}};

                //参数属性名模板
                String[] paramNameTemp=new String[] {"PLC_ID","PLC版本"};
                if (PDTValidateUtil.validateResT9BHPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam,ctx);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"9BH", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======查询集中器版本信息  (9BH) 响应PDT协议报文的 结果:" +pdtMesg);
                    //封装成指定JSON结构返回 或 调KAFKA发送报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* PLC软件复位 */
    T9CH("9c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"9c")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception("PLC软件复位（9CH）,不用传 请求指令参数入参!");
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========PLC软件复位（9CH）响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 设置集中器继电器必须开启时间 */
    T60H("60",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),60)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //校验pdt参数（规则依据文档）
                /*
                 * 参数 字节长度的二维数组模板
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:进制转, 2:码映射，3：直接赋值】）
                 * 请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenTemp=new int[][]{{1,1},{2,1},{2,1},{2,1},{2,1},{1,1},{2,1},{2,1},{2,1},{2,1},{1,1},{2,1},{2,1},{2,1},{2,1}};
                if (PDTValidateUtil.validateReqT82HPdt(cmdParam)) {
                    return PDTAdapter.pdtRequstParser(byteLenTemp,cmdParam);
                }
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========设置集中器继电器必须开启时间（60H）响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 查询集中器继电器必须开启时间 */
    T61H("61",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),61)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception("查询集中器继电器必须开启时间（61H）,不用传 请求指令参数入参!");
            }else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                /*
                 * 参数 字节长度的二维数组模板
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:十进制转,2:二进制转 3:码映射，4：直接赋值】）
                 * 三维：属性值单位【十进制转时:1、相电压,2、相电流,3、相功率,4、相功率因数,5、电能,6、AD输入电压】; 二进制转时:转二进制保留位数
                 * 请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenResTemp=new int[][]{{1,1,0,0},{2,1,9,0},{2,1,9,0},{2,1,9,0},{2,1,9,0},
                        {1,1,0,0},{2,1,9,0},{2,1,9,0},{2,1,9,0},{2,1,9,0},{1,1,0,0},{2,1,9,0},{2,1,9,0},{2,1,9,0},{2,1,9,0}};

                //参数属性名模板
                String[] paramNameTemp=new String[] {"继电器1使能位","继电器1必须打开开始时间","继电器1必须打开结束时间","继电器1必须关闭开始时间","继电器1必须关闭结束时间",
                        "继电器2使能位","继电器2必须打开开始时间","继电器2必须打开结束时间","继电器2必须关闭开始时间","继电器2必须关闭结束时间","继电器3使能位","继电器3必须打开开始时间",
                        "继电器3必须打开结束时间","继电器3必须关闭开始时间","继电器3必须关闭结束时间"};
                if (PDTValidateUtil.validateResTFEHPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam,ctx);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"61H", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======查询集中器继电器必须开启时间 (61H) 响应PDT协议报文的 结果:" +pdtMesg);
                    //封装成指定JSON结构返回 或 调KAFKA发送报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 查询节点传感器信息 */
    T46H("46",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),46)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                // 控制对象	Bit0- Bit2 每一位对应一路继电器。为1有效	1B
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0){
                    JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
                    String hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(0),6);
                    System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                    byte[] codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                    if(codeByte.length != 6)throw new Exception("pdt param length is 6B!");
                    pdtMesg=hexStr;
                }
            }else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                /*
                 * 参数 字节长度的二维数组模板
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:十进制转,2:二进制转 3:码映射，4：直接赋值】）
                 * 三维：属性值单位【十进制转时:1、相电压,2、相电流,3、相功率,4、相功率因数,5、电能,6、AD输入电压】; 二进制转时:转二进制保留位数
                 * 请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenResTemp=new int[][]{{6,1,0,0},{1,3,0,0},{2,1,0,0},{4,1,0,0},{2,1,0,0},{4,1,0,0},{2,1,0,0},{4,1,0,0}};

                //参数属性名模板
                String[] paramNameTemp=new String[] {"节点ID","设备码","节点传感器A编码","传感器A数据","节点传感器B编码","传感器B数据","节点传感器C编码","传感器C数据"};
                if (PDTValidateUtil.validateResTFEHPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam,ctx);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"46H", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======查询节点传感器信息(46H) 响应PDT协议报文的 结果:" +pdtMesg);
                    //封装成指定JSON结构返回 或 调KAFKA发送报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 节点传感器主动上报信息 集中器->主机 */
    FEH("FE",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"FE")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);


            if(isRequest){ //pdt 请求报文解析处理
                //校验pdt参数（规则依据文档）
            }else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                /*
                 * 参数 字节长度的二维数组模板
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:十进制转,2:二进制转 3:码映射，4：直接赋值】）
                 * 三维：属性值单位【十进制转时:1、相电压,2、相电流,3、相功率,4、相功率因数,5、电能,6、AD输入电压】; 二进制转时:转二进制保留位数
                 * 请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenResTemp=new int[][]{{6,1,0,0},{1,3,0,0},{2,1,0,0},{4,1,0,0},{2,1,0,0},{4,1,0,0},{2,1,0,0},{4,1,0,0}};
                //参数属性名模板
                String[] paramNameTemp=new String[] {"节点ID","设备码","节点传感器A编码","传感器A数据","节点传感器B编码","传感器B数据","节点传感器C编码","传感器C数据"};

                if (PDTValidateUtil.validateResTFEHPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam,ctx);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"FEH", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======节点传感器主动上报信息 (FEH) 响应PDT协议报文的 结果:" +pdtMesg);
                    //封装成指定JSON结构返回 或 调KAFKA发送报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 2480开始组网 */
    T62H("62",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),62)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);

            if(isRequest){ //pdt 请求报文解析处理
                // 控制对象	Bit0- Bit2 每一位对应一路继电器。为1有效	1B
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0){
                    JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
                    String hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(0),1);
                    System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                    byte[] codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                    if(codeByte.length != 1)throw new Exception("pdt param length is 1B!");
                    pdtMesg=hexStr;
                }
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========2480开始组网（62H）响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 2480停止组网 */
    T63H("63",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),63)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception(" 2480停止组网（63H）,不用传 请求指令参数入参!");
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========2480停止组网（63H）响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 2480存储节点列表 */
    T66H("66",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),66)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception(" 2480存储节点列表 （66H）,不用传 请求指令参数入参!");
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========2480存储节点列表 （66H）响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 读取2480FLAH节点列表 */
    T67H("67",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),67)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception(" 读取2480FLAH节点列表（67H）,不用传 请求指令参数入参!");
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========读取2480FLAH节点列表（67H）响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 增加单个节点 */
    T9EH("9e",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"9e")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T01H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
                /*
                 * 参数 字节长度的二维数组模板
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:进制转, 2:码映射，3：直接赋值】）
                 * 请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenTemp=new int[][]{{6,1},{1,2},{1,2},{6,1}};
                //校验pdt参数（规则依据文档）
                if (PDTValidateUtil.validateReqT9EHPdt(cmdParam)) {
                    return PDTAdapter.pdtRequstParser(byteLenTemp, cmdParam);
                }
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========增加单个节点(9EH）响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 删除单个节点 */
    T9DH("9d",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"9d")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T01H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0){
                    JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
                    String hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(0),6);
                    System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                    byte[] codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                    if(codeByte.length != 6)throw new Exception("pdt param length is 6B!");
                    pdtMesg=hexStr;
                }
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========删除单个节点(9DH）响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 2480删除节点FLSH存储列表 */
    T69H("69",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),69)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception(" 设置PLC-2480删除存储节点列表指令（69H）,不用传 请求指令参数入参!");

            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========设置PLC-2480删除存储节点列表指令（69H）响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 查询集中器硬件信息 */
    T4AH("4a",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"4a")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception(" 查询集中器硬件信息(4AH),不用传 请求指令参数入参!");
            }else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                /*
                 * 参数 字节长度的二维数组模板（响应类型）
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:十进制转,2:二进制转 3:码映射，4：直接赋值】）
                 * 三维：属性值单位【十进制转时:1、相电压,2、相电流,3、相功率,4、相功率因数,5、电能,6、AD输入电压,9、时分】; 二进制转时【转二进制保留位数】
                 * 注：请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenResTemp=new int[][]{{1,1,0,0},{1,1,0,0},{8,1,0,0},{1,1,0,0}};

                //参数属性名模板
                String[] paramNameTemp=new String[] {"通信方式","联网方式","软件版本号","电表型号与接线方式"};
                if (PDTValidateUtil.validateResT4AHPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam,ctx);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"4AH", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======查询集中器硬件信息 (4AH) 响应PDT协议报文的 结果:" +pdtMesg);
                    //封装成指定JSON结构返回 或 调KAFKA发送报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 设置集中器服务器IP和端口 */
    F8H("f8",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f8")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理

            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========设置集中器服务器IP和端口(F8H) 响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 查询集中器服务器IP和端口 */
    F9H("f9",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f9")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T00H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){
                //该指令 文档没有定义指令参数 ,此处无代码逻辑处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception("查询集中器服务器IP和端口(F9H),不用传 请求指令参数入参!");

            }else { //pdt 响应报文解析处理


            }
            return pdtMesg;
        }
    },
    /* 设定电源最大功率 */
    T6AH("6a",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"6a")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                /*
                 * 参数 字节长度的二维数组模板(请求类型)
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:进制转, 2:码映射，3：直接赋值】）
                 * 请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenReqTemp=new int[][]{{6,1},{2,1}};
                //校验pdt参数（规则依据文档）
                if (PDTValidateUtil.validateReqT6CHPdt(cmdParam)) {
                    return PDTAdapter.pdtRequstParser(byteLenReqTemp, cmdParam);
                }
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========设定电源最大功率(6AH) 响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 查询电源最大功率 */
    T6BH("6b",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"6b")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T01H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                // 控制对象	Bit0- Bit2 每一位对应一路继电器。为1有效	1B
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0){
                    JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
                    String hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(0),6);
                    System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                    byte[] codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                    if(codeByte.length != 6)throw new Exception("pdt param length is 6B!");
                    pdtMesg=hexStr;
                }
            }else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                /*
                 * 参数 字节长度的二维数组模板（响应类型）
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:十进制转,2:二进制转 3:码映射，4：直接赋值】）
                 * 三维：属性值单位【十进制转时:1、相电压,2、相电流,3、相功率,4、相功率因数,5、电能,6、AD输入电压,9、时分】; 二进制转时【转二进制保留位数】
                 * 注：请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenResTemp=new int[][]{{6,1,0,0},{2,1,3,0}};

                //参数属性名模板
                String[] paramNameTemp=new String[] {"ID","电源最大功率"};
                if (PDTValidateUtil.validateResT6DHPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam,ctx);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"6BH", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======查询电源报警阀值(6DH) 响应PDT协议报文的 结果:" +pdtMesg);
                    //封装成指定JSON结构返回 或 调KAFKA发送报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 设定电源报警阀值 */
    T6CH("6c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"6c")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                /*
                 * 参数 字节长度的二维数组模板(请求类型)
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:进制转, 2:码映射，3：直接赋值】）
                 * 请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenReqTemp=new int[][]{{6,1},{2,1},{2,1},{2,1}};
                //校验pdt参数（规则依据文档）
                if (PDTValidateUtil.validateReqT6CHPdt(cmdParam)) {
                    return PDTAdapter.pdtRequstParser(byteLenReqTemp, cmdParam);
                }
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========设定电源报警阀值(6CH) 响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            System.out.println("************设定电源报警阀值(6CH) 响应的PDT解析 结果:" +pdtMesg);
            return pdtMesg;
        }
    },
    /* 查询电源报警阀值 */
    T6DH("6d",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"6d")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T01H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                // 控制对象	Bit0- Bit2 每一位对应一路继电器。为1有效	1B
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0){
                    JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
                    String hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(0),6);
                    System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                    byte[] codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                    if(codeByte.length != 6)throw new Exception("pdt param length is 6B!");
                    pdtMesg=hexStr;
                }
            }else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                /*
                 * 参数 字节长度的二维数组模板（响应类型）
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:十进制转,2:二进制转 3:码映射，4：直接赋值】）
                 * 三维：属性值单位【十进制转时:1、相电压,2、相电流,3、相功率,4、相功率因数,5、电能,6、AD输入电压,9、时分】; 二进制转时【转二进制保留位数】
                 * 注：请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenResTemp=new int[][]{{6,1,0,0},{2,1,1,0},{2,1,1,0},{2,1,1,0}};

                //参数属性名模板
                String[] paramNameTemp=new String[] {"ID","输入电压最小值","输入电压最大值","输出电压最大值"};
                if (PDTValidateUtil.validateResT6DHPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam,ctx);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"6DH", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======查询电源任务编号(6FH) 响应PDT协议报文的 结果:" +pdtMesg);
                    //封装成指定JSON结构返回 或 调KAFKA发送报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 查询电源任务编号 */
    T6FH("6f",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"6f")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T01H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                // 控制对象	Bit0- Bit2 每一位对应一路继电器。为1有效	1B
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0){
                    JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
                    String hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(0),6);
                    System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                    byte[] codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                    if(codeByte.length != 6)throw new Exception("pdt param length is 6B!");
                    pdtMesg=hexStr;
                }
            }else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                /*
                 * 参数 字节长度的二维数组模板（响应类型）
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:十进制转,2:二进制转 3:码映射，4：直接赋值】）
                 * 三维：属性值单位【十进制转时:1、相电压,2、相电流,3、相功率,4、相功率因数,5、电能,6、AD输入电压,9、时分】; 二进制转时【转二进制保留位数】
                 * 注：请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenResTemp=new int[][]{{6,1,0,0},{1,1,0,0},{1,1,0,0},{1,1,0,0}};

                //参数属性名模板
                String[] paramNameTemp=new String[] {"ID","任务总数","任务编号1","任务编号2"};
                if (PDTValidateUtil.validateResT73HPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam,ctx);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"6FH", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======查询电源任务编号(6FH) 响应PDT协议报文的 结果:" +pdtMesg);
                    //封装成指定JSON结构返回 或 调KAFKA发送报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 删除电源任务编号 */
    T47H("47",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),47)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                // 控制对象	Bit0- Bit2 每一位对应一路继电器。为1有效	1B
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0){
                    JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
                    String hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(0),8);
                    System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                    byte[] codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                    if(codeByte.length != 8)throw new Exception("pdt param length is 8B!");
                    pdtMesg=hexStr;
                }
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========删除电源任务编号(47H) 响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            System.out.println("*************删除电源任务编号(47H) 响应的PDT解析 结果:" +pdtMesg);
            return pdtMesg;
        }
    },
    /* 查询电源一条定时任务 */
    T48H("48",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),48)+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T01H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                /*
                 * 参数 字节长度的二维数组模板(请求类型)
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:进制转, 2:码映射，3：直接赋值】）
                 * 请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenReqTemp=new int[][]{{6,1},{1,1}};
                //校验pdt参数（规则依据文档）
                if (PDTValidateUtil.validateReqT48HPdt(cmdParam)) {
                    return PDTAdapter.pdtRequstParser(byteLenReqTemp, cmdParam);
                }
            }else { //pdt 响应报文解析处理

            }
            return pdtMesg;
        }
    },
    /* 设定电源时间 */
    T49H("49",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),49)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                //校验pdt参数（规则依据文档）
                /*
                 * 参数 字节长度的二维数组模板(请求类型)
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:进制转, 2:码映射，3：直接赋值】）
                 * 请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenReqTemp=new int[][]{{6,1},{7,1}};
                //校验pdt参数（规则依据文档）
                if (PDTValidateUtil.validateReqT49HPdt(cmdParam)) {
                    return PDTAdapter.pdtRequstParser(byteLenReqTemp, cmdParam);
                }
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========设定电源时间(49H) 响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            System.out.println("*************设定电源时间(49H) 响应的PDT解析 结果:" +pdtMesg);
            return pdtMesg;
        }
    },
    /* 查询电源时间 */
    T4BH("4b",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"4b")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) throw new Exception("入参不能为空！");
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.T01H.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                // 控制对象	Bit0- Bit2 每一位对应一路继电器。为1有效	1B
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0){
                    JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(0)));
                    String hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(0),6);
                    System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                    byte[] codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                    if(codeByte.length != 6)throw new Exception("pdt param length is 6B!");
                    pdtMesg=hexStr;
                }
            }else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                /*
                 * 参数 字节长度的二维数组模板（响应类型）
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:十进制转,2:二进制转 3:码映射，4：直接赋值】）
                 * 三维：属性值单位【十进制转时:1、相电压,2、相电流,3、相功率,4、相功率因数,5、电能,6、AD输入电压,9、时分】; 二进制转时【转二进制保留位数】
                 * 注：请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenResTemp=new int[][]{{6,1,0,0},{7,1,0,0},{2,1,9,0},{2,1,9,0}};

                //参数属性名模板
                String[] paramNameTemp=new String[] {"ID","年月日周时分秒","日出时分","日落时分"};
                if (PDTValidateUtil.validateResT73HPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam,ctx);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"4BH", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
                    System.out.println("======查询电源时间(4BH) 响应PDT协议报文的 结果:" +pdtMesg);
                    //封装成指定JSON结构返回 或 调KAFKA发送报文
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr("02",1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    },
    /* 设定电源初始化值 */
    T4CH("4c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"4c")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam,ChannelHandlerContext ctx)throws Exception{
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            //区分 请求:true、响应:false 类型(80:响应;非80:请求)
            boolean isRequest=!"80".equals(C_CODE_VAL.TxxH.getValue());
            System.out.println("=========goin ==pdt pdtData 方法 是否请求类型(请求:true、响应:false):"+isRequest);
            if(isRequest){ //pdt 请求报文解析处理
                /*
                 * 参数 字节长度的二维数组模板(请求类型)
                 * 一维：第个参数值的固定字节长度；
                 * 二维： 解析参数值方法【1:进制转, 2:码映射，3：直接赋值】）
                 * 请求模板 不考虑二进制位的转换
                 */
                int[][] byteLenReqTemp=new int[][]{{6,1},{1,1},{2,1},{2,1},{2,1}};
                //校验pdt参数（规则依据文档）
                if (PDTValidateUtil.validateReqT4CHPdt(cmdParam)) {
                    return PDTAdapter.pdtRequstParser(byteLenReqTemp, cmdParam);
                }
            }else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResComPdt(cmdParam)){
                    //对响应状态码，发kafka
                    System.out.println("==========设定电源初始化值(4CH) 响应结果 发KAFKA 操作......");
                    pdtMesg=ConverUtil.MappCODEVal("01H"); //01H：登陆成功;
                }else{
                    pdtMesg=DecimalTransforUtil.toHexStr(cmdParam,1); //02H：登陆失败; 03H：主机忙
                }
            }
            return pdtMesg;
        }
    };

    private String value;
    private String message;
    private String cmd;
    O_CODE_VAL(String value,String message,String cmd) {
        this.value = value;
        this.message=message;
        this.cmd=cmd;
    }

    public String getValue() {
        return value;
    }


    public String getMessage() {
        return message;
    }
//public abstract String eval(double a, double b);

    /**
     * 根据指令获取相应请求报文
     * @param cmd
     * @return
     */
    public static String OrderMethod(String cmd){
        try {
            cmd=cmd.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String resMessage="";
        for (O_CODE_VAL cf : O_CODE_VAL.values()) {
            if (cf.name().endsWith(cmd)) {
                System.out.println("查询到 "+cf.name()+" 的对应指令码值： " + cf.value);
                System.out.println("查询到 *******HEAD_TEMPLATE L： " + HEAD_TEMPLATE.getL());
                System.out.println("查询到 HEAD_TEMPLATE PDT： " + HEAD_TEMPLATE.getPDT());
                System.out.println("查询到 *******HEAD_TEMPLATE CS： " + HEAD_TEMPLATE.getCS());
                System.out.println("查询到 C_CODE_VAL C： " + C_CODE_VAL.TxxH.getValue());
                System.out.println("查询到 HEAD_TEMPLATE UID： " + HEAD_TEMPLATE.getUID());
                //resMessage=cf.message.replaceAll("null","%s");

                resMessage=HEAD_TEMPLATE.H.getValue()+HEAD_TEMPLATE.UID.getValue()+HEAD_TEMPLATE.H.getValue()+"%s"+HEAD_TEMPLATE.L.getValue()+"%s"+HEAD_TEMPLATE.PDT.getValue()+HEAD_TEMPLATE.CS.getValue()+HEAD_TEMPLATE.T.getValue();
                resMessage=String.format(resMessage,C_CODE_VAL.TxxH.getValue(),cf.value);

                System.out.println("查询到 resMessage： " + resMessage);
                //resMessage=String.format(resMessage,HEAD_TEMPLATE.getL(),HEAD_TEMPLATE.getPDT(),HEAD_TEMPLATE.getCS());
                break;
            }
        }
        System.out.println("查询指令码 end");
        return resMessage;
    }

    /**
     * 查询指令码值
     * @param cmd
     * @return
     */
    public static String CmdValueMethod(String cmd){
        try {
            cmd=cmd.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String resMessage="";
        for (O_CODE_VAL cf : O_CODE_VAL.values()) {
            if (cf.name().endsWith(cmd)) {
                resMessage=cf.value;
                System.out.println("查询到 "+cf.name()+" 的对应指令码值： " + cf.value);
                break;
            }
        }
        return resMessage;
    }

    /**
     * 根据指令码查询对应控制码
     * @param cmd
     * @return
     * @throws Exception
     */
    public static String CodeNameMethod(String cmd)throws Exception{
        try {
            cmd=cmd.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String resMessage="";
        for (O_CODE_VAL cf : O_CODE_VAL.values()) {
            if (cf.name().endsWith(cmd)) {
                resMessage=ConverUtil.MappCODE(cf.cmd);
                System.out.println("查询到指令码 "+cf.name()+" 的对应控制码:" + resMessage);
                break;
            }
        }
        return resMessage;
    }

    /**
     * 生成相应指令参数的16进制值
     * @param ccode
     * @param cmd
     * @param cmdParam
     * @return
     */
    public static String PDTTemplate(String ccode, String cmd, String cmdParam, ChannelHandlerContext ctx)throws Exception{
        String resMessage="";
        try {
            cmd=cmd.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (O_CODE_VAL cf : O_CODE_VAL.values()) {
            if (cf.name().endsWith(cmd)) {
                resMessage=cf.pdtData(ccode,cmdParam,ctx);
                break;
            }
        }
        return resMessage;
    }

    /**
     * 二进制 (位->属性名)模板 映射解析
     * @param pdtList
     * @param bitTemp
     * @return
     * @throws Exception
     */
    private static void BitResposeParser(List<Map<String,Object>> pdtList,Object[][] bitTemp,ChannelHandlerContext ctx)throws Exception{
        List<Map>devSignalList=null; //设备信号上报的数据LIST
        Map<String,Object>devSignalMap=null;  //设备信号上报的数据MAP
        Integer signalCode=0;
        for(int i=0; i<pdtList.size();i++){
            devSignalList=new ArrayList<Map>();  //设备信号上报的数据
            LinkedHashMap<String,Object>pdtMap= (LinkedHashMap<String, Object>) pdtList.get(i);
            String devCode= (String) pdtMap.get(PLC_CONFIG.设备码.getKey());
            String bitCode= (String) pdtMap.get(PLC_CONFIG.状态.getKey());
            String nodeID= (String) pdtMap.get(PLC_CONFIG.节点ID.getKey());
            char[] bitChar=bitCode.toCharArray(); //倒序排列,从低至高位正确映射到属性名模板
            ConverUtil.reverseString(bitChar);
            System.out.println("=========BitResposeParser 方法==设备码:"+devCode);
            Long codeValue=DecimalTransforUtil.hexToLong(D_CODE_VAL.DValueMethod(devCode),true);
            System.out.println("=========BitResposeParser 方法==设备码("+devCode+") 转 10进制值:"+codeValue);

            if(bitTemp.length!=bitChar.length) throw new Exception("PDT '二进制位' 与 '位属性名模板'的长度不匹配!");
            if(PLCValidateUtil.rangeInDefined(codeValue.intValue(),0,111)){ //路灯电源（设备码：00H~6FH）
                for(int j=0;j<bitTemp.length;j++){
                    signalCode=(Integer) bitTemp[j][0];
                    if(bitChar[j]=='0' &&  signalCode>0){  //获取有报警信息的属性 0:无报警、1:有报警
                        devSignalMap=new HashMap<String,Object>();  //设备信号上报的数据
                        devSignalMap.put("signal_code",signalCode);
                        devSignalList.add(devSignalMap);
                    }
                }
            }else if(PLCValidateUtil.rangeInDefined(codeValue.intValue(),112,127)){//路灯控制器（设备码：70H~7FH）
                for(int j=0;j<bitTemp.length;j++){
                    signalCode=(Integer) bitTemp[j][1];
                    if(bitChar[j]=='0'){  //获取有报警信息的属性 0:无报警、1:有报警
                        devSignalMap=new HashMap<String,Object>();  //设备信号上报的数据
                        devSignalMap.put("signal_code",signalCode);
                        devSignalList.add(devSignalMap);
                    }
                }
            }
            //kafka  发送 设备上报的信号数据
            if(devSignalList.size()>0){
                System.out.println("===============kafka 发送设备信号上报数据 操作...");
                Object resultBitJSON=JSONArray.toJSON(devSignalList);
                System.out.println("===pdtResposeParser==发送设备信号上报数据 的json结构:"+ JSONObject.toJSONString(resultBitJSON));
                PlcProtocolsUtils.plcSignlResponseSend(HEAD_TEMPLATE.getUID(),nodeID,devSignalList,ctx);
            }
        }
    }

    /**
     * 生成相应指令参数格式的16进制值（PDT）
     * @param ccode
     * @param cmdParam
     * @return
     */
    public abstract String pdtData(String ccode,String cmdParam,ChannelHandlerContext ctx)throws Exception;

  /*  public static void main(String[] args) {
        //String aa=OrderMethod(null,"T70H","10000");
        //String aa= HEAD_TEMPLATE.T.toString();
       //String aa= String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),O_CODE_VAL.T70H.getValue());
       //System.out.println("====:"+aa);
    }*/
}