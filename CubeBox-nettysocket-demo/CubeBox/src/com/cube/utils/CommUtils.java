package com.cube.utils;

import java.util.Iterator;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
 

import com.cube.core.common.SysConst;
import com.cube.core.conn.Connection;
import com.cube.exception.IllegalDataException;
import com.cube.handler.CubeInboundHandler;

public class CommUtils {

	private static final Logger LOG = Logger.getLogger(CommUtils.class);

    /**
     * 从channel的attr中获取connection
     */
    public static Connection getConn(ChannelHandlerContext ctx) throws IllegalDataException {
        Attribute<Connection> secAttr = ctx.channel().attr(SysConst.CONN_KEY);
        Connection conn = secAttr.get();
        if (conn == null) {
            throw new IllegalDataException("conn为空");
        }
        return conn;
    }

    /**
     * 从channel的attr中获取key
     * @throws IllegalDataException
     */
    public static String getKeyFromAttr(ChannelHandlerContext ctx) throws IllegalDataException {
        Attribute<String> secAttr = ctx.channel().attr(SysConst.SECURE_KEY);
        String key = secAttr.get();
        if (StringUtils.isBlank(key)) {
            throw new IllegalDataException("key为空");
        }
        LOG.info("=========key======{}"+ key);
        return key;
    }

    /**
     * 获取客户端的checksum
     */
    public static String getChecksumFromByte(byte[] content) throws IllegalDataException {
        if (content == null || content.length != 49) {
            throw new IllegalDataException("获取checksum,数据长度错误");
        }
        return new String(content, 0, 32);
    }

    /**
     * 获取mac
     */
    public static String getMacFromByte(byte[] content) throws IllegalDataException {
//        if (content == null || content.length != 49) {
//            throw new IllegalDataException("获取mac,数据长度错误");
//        }
        return new String(content);
    }

    /**
     * 将mac设置到channel的attr中
     */
    public static void setMacAttr(ChannelHandlerContext ctx, String mac) {
        Attribute<String> macAttr = ctx.channel().attr(SysConst.MAC_KEY);
        macAttr.set(mac);
    }

    /**
     * 从channel的attr中获取mac
     */
    public static String getMacFromAttr(ChannelHandlerContext ctx) {
        Attribute<String> macAttr = ctx.channel().attr(SysConst.MAC_KEY);
        return macAttr.get();
    }

    public static String getVerFromByte(byte[] bits) throws IllegalDataException {
        if (bits == null || (bits.length != 2 && bits.length !=3 )) {
            throw new IllegalDataException("获取ver,数据长度错误");
        }
//        Integer ver = ((bits[0] << 8) & 0xff00) | (bits[1] & 0x00ff);
//        return ver.shortValue();
        return MD5FileUtil.bufferToHex(bits);
    }

    public static Integer getReplyIdFromByte(byte[] bits) throws IllegalDataException {
        if (bits == null || bits.length < 4) {
            throw new IllegalDataException("获取replyId,数据长度错误");
        }
        Integer id =
                ((bits[0] << 24) & 0xff000000) | ((bits[1] << 16) & 0x00ff0000) | ((bits[2] << 8) & 0x0000ff00)
                        | (bits[3] & 0x000000ff);
        return id;
    }
    
    public static byte[] getReplyObjDataFromByte(byte[] bits) throws IllegalDataException{
        if (bits == null || bits.length < 4) {
            throw new IllegalDataException("获取reply的obj数据,数据长度错误");
        }
        if(bits.length == 4){
            return new byte[0];
        }
        byte[] obj = new byte[bits.length - 4];
        System.arraycopy(bits, 4, obj, 0, obj.length);
        return obj;
    }
    
    /** 
     * 方法名称:transMapToString 
     * 传入参数:map 
     * 返回值:String 形如 username'chenziwen^password'1234 
    */  
    public static String transMapToString(Map map){  
      java.util.Map.Entry entry;  
      StringBuffer sb = new StringBuffer();  
      for(Iterator iterator = map.entrySet().iterator(); iterator.hasNext();)  
      {  
        entry = (java.util.Map.Entry)iterator.next();  
          sb.append(entry.getKey().toString()).append( "'" ).append(null==entry.getValue()?"":  
          entry.getValue().toString()).append (iterator.hasNext() ? "^" : "");  
      }  
      return sb.toString();  
    }  
}
