package com.hzyw.iot.util.constant;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 测试类
 * 指令测试参数封装
 */
public class T_ParamTest {
    public static List getPdtParams(String cmd, Map<String,Object> pdtMap){
        List pdtList=new ArrayList<>();
        switch(cmd){
            case "70H": //集中器继电器开
                pdtList.add(new String[]{"03"});
                break;
            case "71H": //集中器继电器关
                pdtList.add(new String[]{"03"});
                break;
            case "73H": //查询集中器状态
                //pdtList.add(new String[]{"03"}); //不用传入参
                break;
            case "82H": //下发定时任务
                pdtList.add(new String[]{"200","040919","051119","02H","0","00H","5922","80","02H","12","42H","C8H","01H"});
                break;
            case "83H": //查询定时任务
                //pdtList.add(new String[]{"03"}); //不用传入参
                break;
            case "84H": //清除定时任务
                pdtList.add(new String[]{"03"});
                break;
            case "8CH": //设置集中器时间
                pdtList.add(new String[]{"03"});
                break;
            case "8EH": //设置集中器参数
                pdtList.add(new String[]{"03"});
                break;
            case "8FH": //查询集中器参数
                pdtList.add(new String[]{"03"});
                break;
            case "96H": //下发节点
                pdtList.add(new String[]{"03"});
                break;
            case "97H": //读取节点
                pdtList.add(new String[]{"03"});
                break;
            case "98H": //配置节点
                pdtList.add(new String[]{"03"});
                break;
            case "99H": //删除节点
                pdtList.add(new String[]{"03"});
                break;
            case "F0H": //集中器登录
               // pdtList.add(new String[]{"03"});
                break;
            case "F1H": //集中器与主机保持连接心跳
                pdtList.add(new String[]{"03"});
                break;
            case "F2H": //系统控制
                pdtList.add(new String[]{"03"});
                break;
            case "F3H": //集中器报警
                pdtList.add(new String[]{"03"});
                break;
            case "F4H": //执行失败返回
                pdtList.add(new String[]{"03"});
                break;
            case "F5H": //报警能使设置
                pdtList.add(new String[]{"03"});
                break;
            case "F6H": //报警能使查询
                break;
            case "42H": {//节点调光
                String onoff= StringUtils.trimToNull(pdtMap.get("onoff")+"");  //0:关灯; 1: 开灯
                String dim=StringUtils.trimToNull(pdtMap.get("level")+"");
                dim="0".equals(onoff)? "0":("".equals(dim) || dim==null)? "100":dim;

                int dimNum=Integer.parseInt(dim)*2;
                dim=DecimalTransforUtil.toHexStr(String.valueOf(dimNum),1);
                pdtMap.put("level",dim+"H");

                String[] paramTemp=new String[]{"ID","ab","level"};
                paramTemp=getPdtParamVal(paramTemp,pdtMap);
                pdtList.add(paramTemp);
                //pdtList.add(new String[]{"","03H","8CH"});
                break;
            }
            case "45H": //查询节点详细数据
                String[] paramTemp=new String[]{"ID"};
                paramTemp=getPdtParamVal(paramTemp,pdtMap);
                pdtList.add(paramTemp);
                //pdtList.add(new String[]{"000001000156"});
                break;
            case "F7H": //主动上报节点数据
                //pdtList.add(new String[]{"03"});
                break;
            case "FBH": //查询和上传历史数据
                pdtList.add(new String[]{"01"});
                break;
            case "FCH": //设置集中器远程更新IP和端口
                pdtList.add(new String[]{"03"});
                break;
            case "FDH": //查询集中器远程更新IP和端口
                break;
            case "9AH": //查询集中器组网情况
                break;
            case "9BH": //查询集中器版本信息
                break;
            case "9CH": //PLC软件复位
                break;
            case "60H": //设置集中器继电器必须开启时间
                pdtList.add(new String[]{"01","2359","1023","2359","1023","01","2359","1023","2359","1023","01","2359","1023","2359","1023"});
                break;
            case "61H": //查询集中器继电器必须开启时间
                break;
            case "46H": //查询节点传感器信息
                pdtList.add(new String[]{"16777558"});
                break;
            case "FEH": //节点传感器主动上报信息
                pdtList.add(new String[]{"000001000156","82H","0101","14811793","0201","356","0301","08"});
                break;
            case "62H": //2480开始组网
                pdtList.add(new String[]{"03"});
                break;
            case "63H": //2480停止组网
                break;
            case "66H": //2480存储节点列表
                break;
            case "67H": //读取2480FLAH节点列表
                break;
            case "9EH": //增加单个节点
                pdtList.add(new String[]{"000001000156","245","82H","000001000157"});
                break;
            case "9DH": //删除单个节点
                pdtList.add(new String[]{"000001000156"});
                break;
            case "69H": //2480删除节点FLSH存储列表
                break;
            case "4AH": //查询集中器硬件信息
                break;
            case "F8H": //设置集中器服务器IP和端口
                pdtList.add(new String[]{"03"});
                break;
            case "F9H": //查询集中器服务器IP和端口
                break;
            case "6AH": //设定电源最大功率
                pdtList.add(new String[]{"000000000002","13"});
                break;
            case "6BH": //查询电源最大功率
                pdtList.add(new String[]{"000000000002"});
                break;
            case "6CH": //设定电源报警阀值
                pdtList.add(new String[]{"000000000112","220","210","215"});
                break;
            case "6DH": //查询电源报警阀值
                pdtList.add(new String[]{"000000000112"});
                break;
            case "6FH": //查询电源任务编号
                pdtList.add(new String[]{"000000000112"});
                break;
            case "47H": //删除电源任务编号
                pdtList.add(new String[]{"0000000001121003"});
                break;
            case "48H": //查询电源一条定时任务
                pdtList.add(new String[]{"000000000112","03"});
                break;
            case "49H": //设定电源时间
                pdtList.add(new String[]{"000000000112","19091731500"});
                break;
            case "4BH": //查询电源时间
                pdtList.add(new String[]{"000000000112"});
                break;
            case "4CH": //设定电源初始化值
                pdtList.add(new String[]{"000000000112","10","1000","200","100"});
                break;
        }
        return pdtList;
    }

    /**
     * 组装 下发请求的 指令入参
     * @param paramTemp
     * @param pdtMap
     * @return
     */
    private static String[] getPdtParamVal(String[] paramTemp,Map<String,Object> pdtMap){
        for(int i=0;i<paramTemp.length; i++){
            if(pdtMap.containsKey(paramTemp[i])){
                Arrays.fill(paramTemp,i,i+1,pdtMap.get(paramTemp[i]));
            }else{
                Arrays.fill(paramTemp,i,i+1,"");
            }
        }
        System.out.println("==========getPdtParamVal==PLC 下发请求 组装后的 指令入参:"+ JSONObject.toJSONString(paramTemp));
        return paramTemp;
    }
}
