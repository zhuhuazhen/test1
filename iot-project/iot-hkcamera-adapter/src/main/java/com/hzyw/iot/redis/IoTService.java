package com.hzyw.iot.redis;

import com.alibaba.fastjson.JSON;
import com.hzyw.iot.utils.JsonUtils;
import com.hzyw.iot.utils.KeyUtils;
import com.hzyw.iot.vo.dataaccess.DevInfoDataVO;
import com.hzyw.iot.vo.dc.GlobalInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.hzyw.iot.vo.dc.GlobalInfo.Global_Iot_Redis_Key;

/**
 * 物联网 service
 *
 * @author TheEmbers Guo
 * @version 1.0
 * createTime 2018-10-26 16:36
 */
@Service
public class IoTService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IoTService.class);

    //@Autowired
    //private RedisService redisService;

    /**
     * 加载设备信息到 全局
     *
     * @param host
     * @param port
     * @return
     */
    public boolean loadIotMapper2Global(String host, Integer port) throws Exception {
        /*if (GlobalInfo.iotMapper == null) {
            Global_Iot_Redis_Key = KeyUtils.buildKey(host, port);
            GlobalInfo.iotMapper = this.getIoTSnIdMapper(Global_Iot_Redis_Key);
        }
        if (CollectionUtils.isEmpty(GlobalInfo.iotMapper)) {
            return false;
        }*/
        return true;
    }

    /**
     * 刷新 全局 IoT 映射信息
     *
     * @return
     * @throws Exception
     */
    public boolean refreshIotMapper2Global() throws Exception {
        /*GlobalInfo.iotMapper = this.getIoTSnIdMapper(Global_Iot_Redis_Key);
        if (CollectionUtils.isEmpty(GlobalInfo.iotMapper)) {
            return false;
        }*/
        return true;
    }
    
    

    /**
     * 获取 设备信息    映射信息
     * <sn ,<objid,objData>>
     *  -- plc : <port ,<UUID ,《key,value》>>    节点ID之类的作为设备的某个KEY VALUE
     */
   /* private Map<String, IotInfo> getIoTSnIdMapper(String key) throws Exception {
        LOGGER.info("get IoT SnId Mapper... key is [{}]", key);
        //Map<Object, Object> mapperMap = redisService.hGetAll(key);
        Map<Object, Object> mapperMap = new HashMap<Object, Object>();
        IotInfo dev = new IotInfo();
        dev.setId("dev_light_01"); //数据ID或设备在物联网平台的ID
        Map<String, String> dd =new HashMap<String, String>();
        dd.put("devType", "LED-400W-01"); //设备类型
        dd.put("SN", "123456"); //SN码
        dd.put("galvanic", "50"); //电流
        dd.put("galvanic_unit", "w");//电流单位
        dev.setData(dd);
        mapperMap.put("000000000001", dev);
        if (CollectionUtils.isEmpty(mapperMap)) {
            LOGGER.warn("the IoTSnIdMapper is empty!");
            return new HashMap<>(0);
        }

        Map<String, IotInfo> mapperStrMap = new HashMap<>(mapperMap.size());
        Set<Object> keySet = mapperMap.keySet();
        for (Object sn : keySet) {
            Object data = mapperMap.get(sn);
            //IotInfo dataMap = JsonUtils.jsonStr2Obj(String.valueOf(data), IotInfo.class); 
            IotInfo dataMap = JSON.parseObject(JSON.toJSONString(data), IotInfo.class);
            mapperStrMap.put(String.valueOf(sn), dataMap);
        } 
        return mapperStrMap;
        
        
        return null;
    }*/
    
  
    
    
    /**
     * 根据设备端建立连接后的端口来获取当前接入设备的硬件信息
     * 
     * @param port
     * @return
     * @throws Exception
     */
    /*private Map<String, IotInfo> getDevInfo (int port) throws Exception {
        LOGGER.info("get getDevInfo... key is [{}]", port);
        
        Map<String, IotInfo> mapperStrMap = new HashMap<String, IotInfo>();
        
        //Map<Object, Object> mapperMap = redisService.hGetAll(key);
        Map<Object, Object> mapperMap = new HashMap<Object, Object>();
        IotInfo dev = new IotInfo();
        dev.setId("dev_light_01"); //数据ID或设备在物联网平台的ID
        Map<String, String> dd =new HashMap<String, String>();
        dd.put("devType", "LED-400W-01"); //设备类型
        dd.put("SN", "123456"); //SN码
        dd.put("galvanic", "50"); //电流
        dd.put("galvanic_unit", "w");//电流单位
        dev.setData(dd);
        mapperMap.put("000000000001", dev);
        if (CollectionUtils.isEmpty(mapperMap)) {
            LOGGER.warn("the IoTSnIdMapper is empty!");
            return new HashMap<>(0);
        }

        Map<String, IotInfo> ss = new HashMap<>(mapperMap.size());
        Set<Object> keySet = mapperMap.keySet();
        for (Object sn : keySet) {
            Object data = mapperMap.get(sn);
            IotInfo dataMap = JSON.parseObject(JSON.toJSONString(data), IotInfo.class);
            ss.put(String.valueOf(sn), dataMap);
        }
        
        return ss;
    }*/
}
