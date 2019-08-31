package com.theembers.iot.netty.processor;

import com.theembers.iot.enums.ERTUChannelFlag;
import io.netty.buffer.ByteBuf;

/**
 * 数据解码器 抽象类
 *
 * @author TheEmbers Guo
 * @version 1.0
 * createTime 2018-10-19 15:52
 */
public abstract class ProcessorAbstract implements IDataProcessor {
    private IDataProcessor nextProcessor;
    private ERTUChannelFlag flag;

    @Override
    public IDataProcessor getNextProcessor() {
        return nextProcessor;
    }

    @Override
    public void setNextProcessor(IDataProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    ProcessorAbstract(ERTUChannelFlag flag) {
        this.flag = flag;
    }

    /**
     * 通道检测：前两个字节比较
     *
     * @param source
     * @return
     */
    boolean checkAndGetAvailable(ByteBuf source) {
    	//读取消息长度 ，长度小于某个值 ，一般是其实位置已确定的一些值，比如头部，头部长度是定的，可能由几个部分组成，也可能如网上一些简单例子
    	// 描述的头部放一个int类型的数据长度（4个字节 ）
        int length = source.readableBytes(); 
        
        if (length <= 2 && flag == null) {
            return false;
        }
        byte[] sourceFlag = new byte[2];//定义两个字节
        source.readBytes(sourceFlag);//取两个字节的内容
        int i = 0;
        int n = sourceFlag.length;
        byte[] checkFlag = flag.getFlag();
        while (n-- != 0) {  //一个字节一个字节的比较 如果存在一个不相等的 ，读取指针直接跳回到原来初始的位置（前面不是跳过4个字节了嘛）
            if (sourceFlag[i] != checkFlag[i]) {
                // 未监测到 重置读标记位
                source.resetReaderIndex();
                return false;
            }
            i++;
        }
        // mark 读取标记位
        source.markReaderIndex();
        return true;
    }
    
    boolean checkAndGetAvailableNew(ByteBuf source) {
    	//读取消息长度 ，长度小于某个值 ，一般是其实位置已确定的一些值，比如头部，头部长度是定的，可能由几个部分组成，也可能如网上一些简单例子
    	// 描述的头部放一个int类型的数据长度（4个字节 ）
        int length = source.readableBytes(); 
        
        if (length <= 2 && flag == null) {
            return false;
        }
        byte[] sourceFlag = new byte[2];//定义两个字节
        source.readBytes(sourceFlag);//取两个字节的内容
        int i = 0;
        int n = sourceFlag.length;
        byte[] checkFlag = flag.getFlag();
        while (n-- != 0) {  //一个字节一个字节的比较 如果存在一个不相等的 ，读取指针直接跳回到原来初始的位置（前面不是跳过4个字节了嘛）
            if (sourceFlag[i] != checkFlag[i]) {
                // 未监测到 重置读标记位
                source.resetReaderIndex();
                return false;
            }
            i++;
        }
        // mark 读取标记位
        source.markReaderIndex();
        return true;
    }

    String buildDataKey(String littleKey) {
        return this.flag + "-" + littleKey;
    }
}
