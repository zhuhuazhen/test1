package com.hzyw.iot.vo.dc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.hzyw.iot.util.constant.ConverUtil;

/**
 * modbus PLC -数据模型
 *
 * @author TheEmbers Guo
 * @version 1.0
 * createTime 2018-10-23 14:16
 */
public class ModbusInfo {
	private byte[] headStart;  //1
    private byte[] address;  //6
    private byte[] headEnd;  //1
    private byte[] cCode;   //1
    private byte[] length;  //1
    private byte[] cmdCode;
    private byte[] pdt;
    private byte[] data;  //1字节命令码 + XX
    private byte[] crc;  //1
    private byte[] end;  //1
    private byte[] fullData;
    
    public  byte[] crcData(){
    	int len = headStart.length + address.length + headEnd.length + cCode.length + length.length 
    			+ cmdCode.length + pdt.length;   	
    	 //byte[] bt3 = new byte[len];  
         //System.arraycopy(headStart, 0, bt3, 0, headStart.length);  
         //System.arraycopy(address, 0, bt3, headStart.length, address.length);  
         
         ByteBuf byteBuf = Unpooled.buffer();
         byteBuf.writeBytes(headStart).writeBytes(address).writeBytes(headEnd).writeBytes(cCode).writeBytes(length)
         			.writeBytes(cmdCode).writeBytes(pdt);
         
         return byteBuf.array();  
          
    }

    private ByteBuf source; //源数据

    {
    	headStart = new byte[ModBusModel.HEADSTART_LEN.len];
        address = new byte[ModBusModel.ADDRESS_LEN.len];
        headEnd = new byte[ModBusModel.HEADEND_LEN.len];
        cCode = new byte[ModBusModel.CCODE_LEN.len];
        length = new byte[ModBusModel.LENGTH_LEN.len];
        crc = new byte[ModBusModel.CRC_LEN.len];
        end = new byte[ModBusModel.END_LEN.len];
    }

    public ModbusInfo(){//下发场景
    	try {
			this.headStart=ConverUtil.hexStrToByteArr("68") ; //1
			this.headEnd=ConverUtil.hexStrToByteArr("68");  //1
	        this.end=ConverUtil.hexStrToByteArr("16");  //1
		} catch (Exception e) {
			e.printStackTrace();
		}  
    }
    public ModbusInfo(ByteBuf source) { //上报场景
        this.source = source;
        //这种数据 貌似报文  地址 | 命令 | 长度 |数据| CRC检验码
        this.data = new byte[source.readableBytes()- headStart.length - address.length - headEnd.length  
                             - cCode.length  - length.length - crc.length - end.length];
        this.fullData = new byte[source.readableBytes() - crc.length - end.length];

        this.source.readBytes(headStart)
                .readBytes(address)
                .readBytes(headEnd)
                .readBytes(cCode)
                .readBytes(length)
                .readBytes(data) 
                .readBytes(crc)
                .readBytes(end);
        // fullData
        this.source.resetReaderIndex(); //从新回到起始位置
        this.source.readBytes(fullData); //全部数据包
        
        ByteBuf byteBuf = Unpooled.wrappedBuffer(data);
        byte[] cmd = new byte[1];
        cmd[0] = data[0];
        this.setCmdCode(cmd);
        if(data.length > 1){
        	byte[] pdt = new byte[data.length -1];
        	int i=0;
        	for(int p=1;p<data.length;p++){
        		pdt[i] = data[p];
        		i++;
        	}
        }
         
        String bw ="ModbusInfo{" +
					        "headStart=" + com.hzyw.iot.util.constant.ConverUtil.convertByteToHexString(headStart) +
					        "address=" + com.hzyw.iot.util.constant.ConverUtil.convertByteToHexString(address) +
					        "headEnd=" + com.hzyw.iot.util.constant.ConverUtil.convertByteToHexString(headEnd) +
					        "cCode=" + com.hzyw.iot.util.constant.ConverUtil.convertByteToHexString(cCode) +
					        "length=" + com.hzyw.iot.util.constant.ConverUtil.convertByteToHexString(length) +
					        "cmdCode=" + com.hzyw.iot.util.constant.ConverUtil.convertByteToHexString(cmdCode) +
					        "pdt=" + com.hzyw.iot.util.constant.ConverUtil.convertByteToHexString(pdt) +
					        "crc=" + com.hzyw.iot.util.constant.ConverUtil.convertByteToHexString(crc) +
					        "end=" + com.hzyw.iot.util.constant.ConverUtil.convertByteToHexString(end) +
					        '}';
        System.out.println("---------ModbusInfo--------");
        System.out.println(bw);
        System.out.println();
    }

    private enum ModBusModel {
    	HEADSTART_LEN(1),
        ADDRESS_LEN(6),
        HEADEND_LEN(1),
        CCODE_LEN(1),
        LENGTH_LEN(1),
        CRC_LEN(1),
    	END_LEN(1);

        int len;

        ModBusModel(int len) {
            this.len = len;
        }
    }

 

    public byte[] getPdt() {
		return pdt;
	}



	public void setPdt(byte[] pdt) {
		this.pdt = pdt;
	}



	public byte[] getHeadStart() {
		return headStart;
	}



	public void setHeadStart(byte[] headStart) {
		this.headStart = headStart;
	}



	public byte[] getAddress() {
		return address;
	}
	public String getAddress_str() {
		return ConverUtil.convertUUIDByteToHexString(address);
	}
	 


	public void setAddress(byte[] address) {
		this.address = address;
	}



	public byte[] getHeadEnd() {
		return headEnd;
	}



	public void setHeadEnd(byte[] headEnd) {
		this.headEnd = headEnd;
	}



	public byte[] getcCode() {
		return cCode;
	}



	public void setcCode(byte[] cCode) {
		this.cCode = cCode;
	}



	public byte[] getLength() {
		return length;
	}



	public void setLength(byte[] length) {
		this.length = length;
	}



	public byte[] getCmdCode() {
		return cmdCode;
	}
	
	public String getCmdCode_str() {
		return ConverUtil.convertUUIDByteToHexString(cmdCode);
	}



	public void setCmdCode(byte[] cmdCode) {
		this.cmdCode = cmdCode;
	}



	public byte[] getData() {
		return data;
	}



	public void setData(byte[] data) {
		this.data = data;
	}



	public byte[] getCrc() {
		return crc;
	}



	public void setCrc(byte[] crc) {
		this.crc = crc;
	}



	public byte[] getEnd() {
		return end;
	}



	public void setEnd(byte[] end) {
		this.end = end;
	}



	public byte[] getFullData() {
		return fullData;
	}



	public void setFullData(byte[] fullData) {
		this.fullData = fullData;
	}



	public ByteBuf getSource() {
		return source;
	}



	public void setSource(ByteBuf source) {
		this.source = source;
	}



	@Override
    public String toString() {
        return "ModbusInfo{" +
                "address=" + Arrays.toString(address) +
              //  ", command=" + Arrays.toString("") +
                ", length=" + Arrays.toString(length) +
                ", data=" + Arrays.toString(data) +
                ", crc=" + Arrays.toString(crc) +
                '}';
    }
}
