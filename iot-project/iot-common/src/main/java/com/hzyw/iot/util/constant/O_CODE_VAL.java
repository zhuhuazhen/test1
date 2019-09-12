package com.hzyw.iot.util.constant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dataaccess.ResponseDataVO;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 指令码值 枚举集合
 */
public enum O_CODE_VAL{
    /* 集中器继电器开 */
    T70H("70",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),70)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception {
            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
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
                pdtMesg=ConverUtil.MappCODE(cmdParam);
                System.out.println("*************集中器继电器开(70H) 响应的PDT解析 结果:" +pdtMesg);
            	//调KAFKA，发送应用平台
            }
            return pdtMesg;
        }
    },
    /* 集中器继电器关 */
    T71H("71",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),71)+"",C_CODE_VAL.T00H.getValue())
            {
                @Override
                public String pdtData(String c,String cmdParam) throws Exception{
                    if("".equals(cmdParam) || cmdParam==null) return "";
                    String pdtMesg="";
                    if(!"80H".equals(c)){ //pdt 请求报文解析处理
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
                        pdtMesg=ConverUtil.MappCODE(cmdParam);
                        System.out.println("*************集中器继电器开(70H) 响应的PDT解析 结果:" +pdtMesg);
                        //调KAFKA，发送应用平台
                    }
                    return pdtMesg;
                }
            },
    /* 查询集中器状态 */
    T73H("73",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),73)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam) throws Exception{
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
        	if(!"80H".equals(c)){ //pdt 请求报文解析处理
                List<Object> paramList= JSONArray.parseArray(cmdParam);
                if(paramList.size()>0) throw new Exception("查询集中器状态(73H),不用传 请求指令参数入参!");
                //pdtMesg=(paramList.size()>0)?JSONArray.toJSONString(paramList.get(0)):"";
        	}else { //pdt 响应报文解析处理
        		//校验pdt参数（规则依据文档）
            	if (PDTValidateUtil.validateResT73HPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam);
                    MessageVO<ResponseDataVO> ResultMesssage=T_MessageResult.getResponseVO(HEAD_TEMPLATE.getUID(),c,"73H", pdtResposeVO);
                    pdtMesg=JSONObject.toJSONString(ResultMesssage);
            		//System.out.println("======解析 查询集中器状态(73H) 响应PDT协议报文的 结果:" +pdtMesg);
            		//封装成指定JSON结构返回 或 调KAFKA发送报文
            	}else{
                    pdtMesg=ConverUtil.MappCODE(cmdParam);
                }
            }
            return pdtMesg;
        }
    },
    /* 下发定时任务 */
    T82H("82",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),82)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam) throws Exception{
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
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
            	//校验pdt参数（规则依据文档）
            	if (PDTValidateUtil.validateReqT82HPdt(cmdParam)) {
            		return PDTAdapter.pdtRequstParser(byteLenReqTemp, cmdParam);
            	}
        	}else { //pdt 响应报文解析处理
                //校验pdt参数（规则依据文档）
                if (PDTValidateUtil.validateResT82HPdt(cmdParam)) {
                    LinkedHashMap<String,Object> pdtResposeVO=PDTAdapter.pdtResposeParser(byteLenResTemp,paramNameTemp,cmdParam);
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
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 清除定时任务 */
    T84H("84",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),84)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 设置集中器时间 */
    T8CH("8c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"8c")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 设置集中器参数 */
    T8EH("8e",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"8e")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询集中器参数 */
    T8FH("8f",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"8f")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 下发节点 */
    T96H("96",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),96)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 读取节点 */
    T97H("97",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T03H.getValue(),97)+"",C_CODE_VAL.T03H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 配置节点 */
    T98H("98",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T03H.getValue(),98)+"",C_CODE_VAL.T03H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 删除节点 */
    T99H("99",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),99)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 集中器登录 集中器->主机 */
    F0H("f0",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"f0")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
            	cmdParam="01";
        	}else { //pdt 响应报文解析处理
        		cmdParam="01";
            }
            return cmdParam;
        }
    },
    /* 集中器与主机保持连接心跳 集中器->主机 */
    F1H("f1",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"f1")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 系统控制 */
    F2H("f2",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f2")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 集中器报警 集中器->主机 */
    F3H("f3",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"f3")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 执行失败返回 */
    F4H("f4",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T04H.getValue(),"f4")+"",C_CODE_VAL.T04H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 报警能使设置 */
    F5H("f5",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f5")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 报警能使查询 */
    F6H("f6",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f6")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 节点调光 */
    T42H("42",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),42)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            /*
             * 参数 字节长度的二维数组模板(请求类型)
             * 一维：第个参数值的固定字节长度；
             * 二维： 解析参数值方法【1:进制转, 2:码映射，3：直接赋值】）
             * 请求模板 不考虑二进制位的转换
             */
            int[][] byteLenReqTemp=new int[][]{{6,1},{1,2},{1,2}};

            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
                //校验pdt参数（规则依据文档）
                StringBuffer paramBuffer=new StringBuffer(cmdParam);
                if (PDTValidateUtil.validateReqT42HPdt(c,paramBuffer)) {
                    cmdParam=paramBuffer.toString();
                    pdtMesg=PDTAdapter.pdtRequstParser(byteLenReqTemp,cmdParam);
                }
        	}else { //pdt 响应报文解析处理
                pdtMesg=ConverUtil.MappCODE(cmdParam);
                System.out.println("*************节点调光(42H) 响应的PDT解析 结果:" +pdtMesg);
            }
            return pdtMesg;
        }
    },
    /* 主动上报节点数据 集中器->主机 */
    F7H("f7",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"f7")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            /*
             * 参数 字节长度的多维数组模板（响应类型）
             * 一维：第个参数值的固定字节长度；
             * 二维：解析参数值方法【1:十进制转(11:有符号整型进制转; 12:浮点型进制转),2:二进制转 3:码映射，4：直接赋值】）
             * 三维：属性值单位【十进制转时:1、相电压(V),2、相电流(A),3、相功率(W),4、相功率因数(%),5、电能(kWh),6、AD输入电压(0.00V),7、毫安电流(mA),8、温度单位(.C),9、小时(h)】; 二进制转时【转二进制保留位数】
             * 四维：统计属性字段【如： 节点总数、当前帧数、总帧数】0：默认不是，1：是
             * 请求模板 不考虑二进制位的转换
             */
            int[][] byteLenResTemp=new int[][]{{1,1,0,1},{6,1,0,1},{1,3,0,0},{1,3,0,0},{2,1,6,0},{2,1,2,0},{2,1,3,0},{1,1,4,0},
                                               {2,2,16,0},{1,2,3,0},{1,11,8,0},{2,1,3,0},{2,1,9,0},{2,1,5,0},{2,1,9,0}};
            //参数属性名模板

            String[] paramNameTemp=new String[] { "节点总数","节点ID","设备码","在线状态","输入电压V",
                                                 "输入电流A","输入功率P","功率因素F","状态","异常状态",
                                                 "灯具温度","输出功率","灯具运行时长","电能","故障时长"};

            if("".equals(cmdParam) || cmdParam==null) return "";
            String pdtMesg="";
            if(!"80H".equals(c)){ //pdt 请求报文解析处理 注:[该指令 文档上没有请求的需求]
        	}else { //pdt 响应报文解析处理
                if (PDTValidateUtil.validateResF7HPdt(cmdParam)) {

                }
            }
            return pdtMesg;
        }
    },
    /* 查询节点详细数据 */
    T45H("45",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),45)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询和上传历史数据 */
    FBH("fb",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"fb")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 设置集中器远程更新IP和端口 */
    FCH("fc",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"fc")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询集中器远程更新IP和端口 */
    FDH("fd",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"fd")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询集中器组网情况 */
    T9AH("9a",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"9a")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询集中器版本信息 */
    T9BH("9b",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"9b")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* PLC软件复位 */
    T9CH("9c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"9c")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 设置集中器继电器必须开启时间 */
    T60H("60",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),60)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询集中器继电器必须开启时间 */
    T61H("61",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),61)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询节点传感器信息 */
    T46H("46",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),46)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 节点传感器主动上报信息 集中器->主机 */
    FEH("fe",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"fe")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 2480开始组网 */
    T62H("62",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),62)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 2480停止组网 */
    T63H("63",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),63)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 2480存储节点列表 */
    T66H("66",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),66)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 读取2480FLAH节点列表 */
    T67H("67",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),67)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 增加单个节点 */
    T9EH("9e",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"9e")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 删除单个节点 */
    T9DH("9d",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"9d")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 2480删除节点FLSH存储列表 */
    T69H("69",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),69)+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询集中器硬件信息 */
    T4AH("4a",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"4a")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 设置集中器服务器IP和端口 */
    F8H("f8",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f8")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询集中器服务器IP和端口 */
    F9H("f9",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f9")+"",C_CODE_VAL.T00H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 设定电源最大功率 */
    T6AH("6a",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"6a")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询电源最大功率 */
    T6BH("6b",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"6b")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 设定电源报警阀值 */
    T6CH("6c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"6c")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询电源报警阀值 */
    T6DH("6d",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"6d")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询电源任务编号 */
    T6FH("6f",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"6f")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 删除电源任务编号 */
    T47H("47",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),47)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询电源一条定时任务 */
    T48H("48",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),48)+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 设定电源时间 */
    T49H("49",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),49)+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 查询电源时间 */
    T4BH("4b",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"4b")+"",C_CODE_VAL.T01H.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
        }
    },
    /* 设定电源初始化值 */
    T4CH("4c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"4c")+"",C_CODE_VAL.TxxH.getValue()){
        @Override
        public String pdtData(String c,String cmdParam)throws Exception{
            if(!"80H".equals(c)){ //pdt 请求报文解析处理
        		
        	}else { //pdt 响应报文解析处理
            	
            }
            return "";
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
                System.out.println("查询到 HEAD_TEMPLATE L： " + HEAD_TEMPLATE.getL());
                System.out.println("查询到 HEAD_TEMPLATE PDT： " + HEAD_TEMPLATE.getPDT());
                System.out.println("查询到 HEAD_TEMPLATE CS： " + HEAD_TEMPLATE.getCS());
                System.out.println("查询到 C_CODE_VAL C： " + C_CODE_VAL.TxxH.getValue());
                System.out.println("查询到 HEAD_TEMPLATE UID： " + HEAD_TEMPLATE.getUID());
                resMessage=cf.message.replaceAll("null","%s");

                System.out.println("查询到 resMessage： " + resMessage);
                resMessage=String.format(resMessage,HEAD_TEMPLATE.getL(),HEAD_TEMPLATE.getPDT(),HEAD_TEMPLATE.getCS());
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
    public static String PDTTemplate(String ccode,String cmd,String cmdParam)throws Exception{
        String resMessage="";
        try {
            cmd=cmd.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (O_CODE_VAL cf : O_CODE_VAL.values()) {
            if (cf.name().endsWith(cmd)) {
                resMessage=cf.pdtData(ccode,cmdParam);
                break;
            }
        }
        return resMessage;
    }

    /**
     * 生成相应指令参数格式的16进制值（PDT）
     * @param ccode
     * @param cmdParam
     * @return
     */
    public abstract String pdtData(String ccode,String cmdParam)throws Exception;

  /*  public static void main(String[] args) {
        //String aa=OrderMethod(null,"T70H","10000");
        //String aa= HEAD_TEMPLATE.T.toString();
       //String aa= String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),O_CODE_VAL.T70H.getValue());
       //System.out.println("====:"+aa);
    }*/
}