package com.hzyw.iot.util.constant;

/**
 * 指令码值 枚举集合
 */
public enum O_CODE_VAL{
    /* 集中器继电器开 */
    T70H("70",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),70)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            if("".equals(cmdParam) || cmdParam==null) return "";
            // 控制对象	Bit0- Bit2 每一位对应一路继电器。为1有效	1B
            byte[] byteArry=null;  //默认 ISO8859-1编码格式设置
            String pdtMesg="";
            try {
                byteArry= ConverUtil.hexStrToByteArr(cmdParam);  //16进制转成字节数组
                if(byteArry.length==1){//校验 PDT 字节长度
                    pdtMesg= ConverUtil.convertByteToHexString(byteArry);
                }else{
                    throw new Exception("pdt param length is 1B!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return pdtMesg;
        }
    },
    /* 集中器继电器关 */
    T71H("71",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),71)+"")
            {
                @Override
                public String pdtData(String c,String cmdParam) {
                    return "";
                }
            },
    /* 查询集中器状态 */
    T73H("73",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),73)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 下发定时任务 */
    T82H("82",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),82)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询定时任务 */
    T83H("83",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),83)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 清除定时任务 */
    T84H("84",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),84)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 设置集中器时间 */
    T8CH("8c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"8c")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 设置集中器参数 */
    T8EH("8e",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"8e")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询集中器参数 */
    T8FH("8f",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"8f")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 下发节点 */
    T96H("96",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),96)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 读取节点 */
    T97H("97",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T03H.getValue(),97)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 配置节点 */
    T98H("98",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T03H.getValue(),98)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 删除节点 */
    T99H("99",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),99)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 集中器登录 集中器->主机 */
    F0H("f0",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"f0")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return cmdParam;
        }
    },
    /* 集中器与主机保持连接心跳 集中器->主机 */
    F1H("f1",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"f1")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 系统控制 */
    F2H("f2",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f2")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 集中器报警 集中器->主机 */
    F3H("f3",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"f3")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 执行失败返回 */
    F4H("f4",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T04H.getValue(),"f4")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 报警能使设置 */
    F5H("f5",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f5")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 报警能使查询 */
    F6H("f6",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f6")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 节点调光 */
    T42H("42",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),42)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 主动上报节点数据 集中器->主机 */
    F7H("f7",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"f7")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询节点详细数据 */
    T45H("45",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),45)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询和上传历史数据 */
    FBH("fb",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"fb")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 设置集中器远程更新IP和端口 */
    FCH("fc",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"fc")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询集中器远程更新IP和端口 */
    FDH("fd",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"fd")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询集中器组网情况 */
    T9AH("9a",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"9a")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询集中器版本信息 */
    T9BH("9b",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"9b")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* PLC软件复位 */
    T9CH("9c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"9c")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 设置集中器继电器必须开启时间 */
    T60H("60",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),60)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询集中器继电器必须开启时间 */
    T61H("61",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),61)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询节点传感器信息 */
    T46H("46",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),46)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 节点传感器主动上报信息 集中器->主机 */
    FEH("fe",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"fe")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 2480开始组网 */
    T62H("62",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),62)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 2480停止组网 */
    T63H("63",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),63)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 2480存储节点列表 */
    T66H("66",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),66)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 读取2480FLAH节点列表 */
    T67H("67",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),67)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 增加单个节点 */
    T9EH("9e",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"9e")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 删除单个节点 */
    T9DH("9d",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"9d")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 2480删除节点FLSH存储列表 */
    T69H("69",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),69)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询集中器硬件信息 */
    T4AH("4a",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"4a")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 设置集中器服务器IP和端口 */
    F8H("f8",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f8")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询集中器服务器IP和端口 */
    F9H("f9",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T00H.getValue(),"f9")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 设定电源最大功率 */
    T6AH("6a",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"6a")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询电源最大功率 */
    T6BH("6b",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"6b")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 设定电源报警阀值 */
    T6CH("6c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"6c")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询电源报警阀值 */
    T6DH("6d",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"6d")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询电源任务编号 */
    T6FH("6f",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"6f")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 删除电源任务编号 */
    T47H("47",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),47)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询电源一条定时任务 */
    T48H("48",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),48)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 设定电源时间 */
    T49H("49",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),49)+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 查询电源时间 */
    T4BH("4b",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.T01H.getValue(),"4b")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    },
    /* 设定电源初始化值 */
    T4CH("4c",String.format(HEAD_TEMPLATE.T.toString(),C_CODE_VAL.TxxH.getValue(),"4c")+""){
        @Override
        public String pdtData(String c,String cmdParam) {
            return "";
        }
    };

    private String value;
    private String message;
    O_CODE_VAL(String value,String message) {
        this.value = value;
        this.message=message;
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
                System.out.println("查询到 HEAD_TEMPLATE PDT： " + HEAD_TEMPLATE.PDT.getValue());
                System.out.println("查询到 HEAD_TEMPLATE CS： " + HEAD_TEMPLATE.getCS());
                System.out.println("查询到 C_CODE_VAL C： " + C_CODE_VAL.TxxH.getValue());
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