package com.cube.logic.proc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import com.cube.core.CubeRun;
import com.cube.core.conn.Connection;
import com.cube.core.conn.ConnectionManager;
import com.cube.event.CubeMsg;
import com.cube.event.ReplyEvent;
import com.cube.exception.IllegalDataException;
import com.cube.logic.Process;
import com.cube.server.CubeBootstrap;
import com.cube.utils.CommUtils;
import com.cube.utils.HexByteToolUtil;
import com.cube.utils.newStringUtils.Constant;
import com.cube.utils.newStringUtils.Location;
import com.cube.utils.newStringUtils.Utils;
import com.webService.QueryMapEntity;
import com.webService.QueryMapServerImplService;

/**
 * 接收手表消息
 * @description HandShake
 * @author tengyz
 * @version 0.1
 * @date 2014年8月6日
 */
public class ReceiveWatchMessages implements Process {
	private static final Logger LOG = Logger.getLogger(ReceiveWatchMessages.class);

    public void excute(CubeMsg msg) throws IllegalDataException {
    	String mac =new String(msg.getData());
        Connection conn =null;;
        conn = CommUtils.getConn(msg.getCtx());
        conn.setMac(mac);
        ConnectionManager.getInstance().addToConns(conn.getMac(), conn);
        
        //发送给gps地图数据
        QueryMapServerImplService QueryMapServerImplService=new QueryMapServerImplService();
        QueryMapEntity QueryMapEntity=new QueryMapEntity();
        try {
        	Location location=(Location)Utils.wristwatchToPlatform(msg.getDataString()).get("location");
        	if(null!=location&&!"".equals(location)){
        		if(Constant.COMMAND_AL.equals(location.getOpType())){
        			//告警
        			String[] SNs=mac.split("\\*");
        			QueryMapServerImplService.getQueryMapServerImplPort().sendAlert(SNs[1], location.getStatus());
        		}else if(Constant.COMMAND_UD.equals(location.getOpType())){
        			//位置上报
        			PropertyUtils.copyProperties(QueryMapEntity, location);
        			QueryMapEntity.setEquipmentId(mac);
        			QueryMapEntity.setDataStr(msg.getDataString());
        			LOG.info((new StringBuilder()).append("全部位置上报=:").append(QueryMapEntity.getDataStr()).toString());
        			try{

            			QueryMapServerImplService.getQueryMapServerImplPort().sendTest(QueryMapEntity);
            			LOG.info("全部位置上报=:");
        			}catch(Exception e){
            			LOG.info("全部位置上报=:");
        				LOG.error(e.getMessage());
        				e.printStackTrace();
        			}
        		}
        		
            }
			
		} catch (IllegalAccessException e){
			LOG.error("发送给GPS地图服务异常1");
			e.printStackTrace();
		} catch (InvocationTargetException e){
			LOG.error("发送给GPS地图服务异常2");
			e.printStackTrace();
		} catch (NoSuchMethodException e){
			LOG.error("发送给GPS地图服务异常3");
			e.printStackTrace();
		}
        
        //发给手表回复信息
        String backString =Utils.wristwatchToPlatform(msg.getDataString()).get("responseMessage")+"";
        LOG.info((new StringBuilder()).append("返回给手表数据=:").append(backString).toString());
        if(null!=backString&&!"".equals(backString)){
    		ByteBuf  buff=(ByteBuf) Unpooled.wrappedBuffer(HexByteToolUtil.hexStringToBytes(HexByteToolUtil.encode(backString)));
        	conn.getCtx().writeAndFlush(buff);
        }else{
        	msg.getCtx().pipeline().close();
        }
        
        //放入手表异步应答消息
    	ReplyEvent replyEvent = CubeBootstrap.processRunnable.getReply(mac);
    	if (replyEvent == null) {
            return;
        }
    	replyEvent.setObj(msg.getDataString());//手表返回串
        synchronized (replyEvent) {
            replyEvent.notifyAll();
        }
        
        
    }
}
