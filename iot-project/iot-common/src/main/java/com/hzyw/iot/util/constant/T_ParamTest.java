package com.hzyw.iot.util.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试类
 * 指令测试参数封装
 */
public class T_ParamTest {
    public static List getPdtParams(String cmd){
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
                pdtList.add(new String[]{"03"});
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
                pdtList.add(new String[]{"03"});
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
                pdtList.add(new String[]{"03"});
                break;
            case "42H": //节点调光
                /*C = 01H：ID = 节点PHYID
                C = 02H:  6个Byte的最低Byte表示组号。(例如，控制第2组节点，ID = 00 00 00 00 00 02)
                C = 03H:  ID为全0。（即 ID = 00 00 00 00 00 00）*/
                pdtList.add(new String[]{"","03H","8CH"});
                break;
            case "45H": //查询节点详细数据
                pdtList.add(new String[]{"03"});
                break;
            case "F7H": //主动上报节点数据
                pdtList.add(new String[]{"03"});
                break;
            case "FBH": //查询和上传历史数据
                pdtList.add(new String[]{"03"});
                break;
            case "FCH": //设置集中器远程更新IP和端口
                pdtList.add(new String[]{"03"});
                break;
            case "FDH": //查询集中器远程更新IP和端口
                pdtList.add(new String[]{"03"});
                break;
            case "9AH": //查询集中器组网情况
                pdtList.add(new String[]{"03"});
                break;
            case "9BH": //查询集中器版本信息
                pdtList.add(new String[]{"03"});
                break;
            case "9CH": //PLC软件复位
                pdtList.add(new String[]{"03"});
                break;
            case "60H": //设置集中器继电器必须开启时间
                pdtList.add(new String[]{"03"});
                break;
            case "61H": //查询集中器继电器必须开启时间
                pdtList.add(new String[]{"03"});
                break;
            case "46H": //查询节点传感器信息
                pdtList.add(new String[]{"03"});
                break;
            case "FEH": //节点传感器主动上报信息
                pdtList.add(new String[]{"03"});
                break;
            case "62H": //2480开始组网
                pdtList.add(new String[]{"03"});
                break;
            case "63H": //2480停止组网
                pdtList.add(new String[]{"03"});
                break;
            case "66H": //2480存储节点列表
                pdtList.add(new String[]{"03"});
                break;
            case "67H": //读取2480FLAH节点列表
                pdtList.add(new String[]{"03"});
                break;
            case "9EH": //增加单个节点
                pdtList.add(new String[]{"03"});
                break;
            case "9DH": //删除单个节点
                pdtList.add(new String[]{"03"});
                break;
            case "69H": //2480删除节点FLSH存储列表
                pdtList.add(new String[]{"03"});
                break;
            case "4AH": //查询集中器硬件信息
                pdtList.add(new String[]{"03"});
                break;
            case "F8H": //设置集中器服务器IP和端口
                pdtList.add(new String[]{"03"});
                break;
            case "F9H": //查询集中器服务器IP和端口
                pdtList.add(new String[]{"03"});
                break;
            case "6AH": //设定电源最大功率
                pdtList.add(new String[]{"03"});
                break;
            case "6BH": //查询电源最大功率
                pdtList.add(new String[]{"03"});
                break;
            case "6CH": //设定电源报警阀值
                pdtList.add(new String[]{"03"});
                break;
            case "6DH": //查询电源报警阀值
                pdtList.add(new String[]{"03"});
                break;
            case "6FH": //查询电源任务编号
                pdtList.add(new String[]{"03"});
                break;
            case "47H": //删除电源任务编号
                pdtList.add(new String[]{"03"});
                break;
            case "48H": //查询电源一条定时任务
                pdtList.add(new String[]{"03"});
                break;
            case "49H": //设定电源时间
                pdtList.add(new String[]{"03"});
                break;
            case "4BH": //查询电源时间
                pdtList.add(new String[]{"03"});
                break;
            case "4CH": //设定电源初始化值
                pdtList.add(new String[]{"03"});
                break;
        }
        return pdtList;
    }
}
