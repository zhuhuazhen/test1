package com.hzyw.iot.platform.devicemanager.service.device;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceInfoDO;
import com.hzyw.iot.platform.devicemanager.domain.vo.DeviceListVO;
import com.hzyw.iot.platform.models.transfer.DefaultDeviceException;
import com.hzyw.iot.platform.models.transfer.IllegalParameterException;

import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/29.
 */
public interface DeviceInfoService {

    /**
     * 通过ID查询设备静态信息
     *
     * @param deviceId
     * @return
     */
    DeviceInfoDO searchEquipmentById(String deviceId);

    /**
     * 通过设备序列号（SN）查询设备静态信息,附带设备大类.
     * 大多数同领域SN不会重复，只会得到一个设备信息，但是不否认会出现某些厂商SN撞车的情况
     *
     * @param serialNumber
     * @return
     */
    List<DeviceInfoDO> searchEquipmentBySN(String serialNumber, Integer deviceDomain);

    /**
     * 通过SN和设备型号（编号-大类-厂商）查询设备静态信息，默认单个厂商自己序列号不会重复，
     * 如有多调记录，视为数据录入有问题，只返回第一条数据.
     *
     * @param serialNumber
     * @return
     */
    DeviceInfoDO searchEquipmentBySN(String serialNumber, String deviceType);

    /**
     * 查询该型号下的所有设备列表
     *
     * @param deviceType （deviceType格式：设备型号Code::大类::厂商Code）
     * @return
     */
    List<DeviceInfoDO> searchEquipmentByType(String deviceType);

    /**
     * 新增单条设备信息
     *
     * @param deviceInfoDO
     */
    void createDevice(DeviceInfoDO deviceInfoDO);

    /**
     * 删除单条设备
     *
     * @param deviceId
     */
    void deleteById(String deviceId);

    /**
     * 对单条设备信息做增/补操作，
     * 如果deviceId有值，则更新数据（不更新SN）,没值则插入新数据。
     * deviceInfoDO中需保证deviceId与serialNumber有值（若为不计SN的可替换设备，SN为统一为NULL大写字符串）。
     * deviceId为根据算法计算所得，具有可验证性，验证通过后才进行后续操作，但是对于不计SN设备，只验证格式，不验证绑定关系。
     * 注意：本接口不做ID和SN变更，若真要更改，请使用专门SN变更接口。
     *
     * @param deviceInfoDO deviceInfoDO中需保证serialNumber有值，这是硬件关键属性
     *
     * @throws DefaultDeviceException
     */
    void saveDevice(DeviceInfoDO deviceInfoDO) throws IllegalParameterException;

    /**
     * 变更设备SN序列号
     * @param deviceId
     * @param oldSN
     * @param newSN
     * @return
     */
    boolean changeSerialNumber(String deviceId, String oldSN, String newSN);

//    /**
//     * 查询要导出的数据
//     * @param device
//     * @return
//     */
//    List<Device> getAllDevices(Device device)throws Exception;

//    /**
//     * 查询多条设备(分页)
//     * @param device
//     * @param curPage
//     * @param pageSize
//     * @return
//     * @throws Exception
//     */
//    ResultVO<List<Device>> getAllDevicesPage(Device device, Integer curPage, Integer pageSize)throws Exception;

    /***
     * 页面获取设备数据列表
     * @param deviceId
     * @param deviceType
     * @param gatewayId
     * @return
     */
    List<DeviceListVO> getAllDeviceInfo(String deviceId,String deviceType,String gatewayId);
}
