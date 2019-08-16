package com.cube.logic;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.cube.core.CubeRun;
import com.cube.event.EventEnum;
import com.cube.logic.proc.ReceiveWatchMessages;
import com.cube.logic.proc.SendWatchMessage;
import com.cube.logic.proc.SendWatchMessageLong;

/**
 * tcp接口初始化类
 * @ClassName: ProcessManager 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author A18ccms a18ccms_gmail_com 
 * @date Jul 9, 2015 12:44:57 PM
 */
@Component
public class ProcessManager {

	private static final Logger LOG = Logger.getLogger(CubeRun.class);
	private static ProcessManager INSTANCE;

    public Map<EventEnum, Process> processMap = new HashMap<EventEnum, Process>();

    public static ProcessManager getInstance() {
        return INSTANCE;
    }

    public Process getProcess(EventEnum eventEnum) {
        return processMap.get(eventEnum);
    }

    /**
     * 初始化处理过程 在此处添加处理接口
     */
    @PostConstruct
    private void init() {
        LOG.info("init the INSTANCE OF ProcessManager");
        INSTANCE = new ProcessManager();
        // 手表发送消息到服务器
        INSTANCE.processMap.put(EventEnum.ONE, new ReceiveWatchMessages());
        // 服务器发送消息到手表
        INSTANCE.processMap.put(EventEnum.TWO, new SendWatchMessage());
        // 超长指令，服务器发送消息到手表
        INSTANCE.processMap.put(EventEnum.THREE, new SendWatchMessageLong());
    }
}
