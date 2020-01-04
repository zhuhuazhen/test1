package com.hzyw.iot.platform.devicemanager.controller;

import com.hzyw.iot.platform.devicemanager.domain.comm.ResultVO;
import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAttributeDO;
import com.hzyw.iot.platform.devicemanager.domain.vo.DeviceAttrVO;
import com.hzyw.iot.platform.devicemanager.service.device.DeviceAttrService;
import com.hzyw.iot.platform.util.poi.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备属性维护接口（增，删，改，查）
 *
 * @blame Android Team
 */
@Slf4j
@RestController
@RequestMapping("/device/attr")
public class DeviceAttrController {

    @Autowired
    DeviceAttrService deviceService;

    /**
     * @param attributeKey
     * @return
     */
    @GetMapping("/info/{attributeKey}")
    public ResultVO<DeviceAttrVO> getDeviceAttrByKey(@PathVariable String attributeKey) {
        if (StringUtils.isEmpty(attributeKey)) {
            DeviceAttributeDO attributeDO = deviceService.selectDeviceAttr(attributeKey);
            DeviceAttrVO vo = buildAttrVO(attributeDO);
            return ResultVO.success(vo);
        } else {
            return ResultVO.success(null);
        }
    }

    /**
     * 设备分页查询
     *
     * @return
     */
    @GetMapping(value = "/page/{currentPage}/{pageSize}")
    public ResultVO<List<DeviceAttrVO>> getAllDevice(@PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        List<DeviceAttributeDO> list = deviceService.ListDeviceAttr(currentPage, pageSize);
        List<DeviceAttrVO> listVo = new ArrayList<>();
        list.stream().forEach(e -> {
            DeviceAttrVO vo = buildAttrVO(e);
            listVo.add(vo);
        });
        return ResultVO.success(listVo);
    }

    /**
     * 设备属性变更
     *
     * @param vo DeviceAttrVO
     * @return
     */
    @PostMapping("/save")
    public ResultVO updateDevice(@RequestBody DeviceAttrVO vo) {
        if (vo != null && vo.getAttrKey() != null) {
            DeviceAttributeDO attr = buildAttrDO(vo);
            deviceService.saveDeviceAttr(attr);
        }
        return ResultVO.success("更新成功！");
    }

    /**
     * 设备批量删除
     *
     * @param attributeKey
     * @return
     */
    @PostMapping("/del/{attributeKey}")
    public ResultVO deleteDevice(@PathVariable String attributeKey) {
        deviceService.deleteDeviceAttr(attributeKey);
        return ResultVO.success("删除成功!");
    }

//    /**
//     * 设备导出
//     *
//     * @param response
//     * @throws Exception
//     */
//    @RequestMapping("export")
//    public void export(HttpServletResponse response) throws Exception {
//        //response.setHeader("content-Type", "application/vnd.ms-excel");
//        //response.setHeader("Content-Disposition", "attachment;filename=orders.xls");
//        //查出需要导出的数据
//        List<Device> deviceList = deviceService.getAllDevices(null);
//        String fileNameStr = "device_".concat(System.currentTimeMillis() + ".xls");
//
//        //导出操作
//        FileUtil.exportExcel(deviceList, "devicetitle", "device", Device.class, fileNameStr, response);
//    }

//    /**
//     * 设备导入
//     *
//     * @param file
//     * @param request
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("importExcel")
//    @ResponseBody
//    public ResultVO<Object> importExcel(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception {
//        String message = "";
//        if (file == null) {
//            return ResultVO.getFailed("导入失败！ 上传文件不能为空!");
//        }
//        String fileName = file.getOriginalFilename();
//        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
//            return ResultVO.getFailed("导入失败！ 上传文件格式错误，请上传后缀为.xls或.xlsx的文件!");
//        }
//        List<Device> deviceList = FileUtil.importExcel(file, 1, 1, Device.class);
//        System.out.println("导入数据一共【" + deviceList.size() + "】行");
//        //TODO 保存数据库
//        deviceService.addMoreDevice(deviceList);
//        message = "导入成功！ 导入数据一共【" + deviceList.size() + "】行";
//        return ResultVO.getSuccess(message);
//    }

    private DeviceAttrVO buildAttrVO(DeviceAttributeDO attributeDO) {
        DeviceAttrVO vo = new DeviceAttrVO();
        vo.setAttrKey(attributeDO.getAttrKey());
        vo.setAttrName(attributeDO.getAttrName());
        vo.setRatio(attributeDO.getRatio());
        vo.setStdUnit(attributeDO.getStdUnit());
        vo.setUnit(attributeDO.getUnit());
        vo.setValueType(attributeDO.getValueType());
        return vo;
    }

    private DeviceAttributeDO buildAttrDO(DeviceAttrVO vo) {
        DeviceAttributeDO ddo = new DeviceAttributeDO();
        ddo.setAttrKey(vo.getAttrKey());
        ddo.setAttrName(vo.getAttrName());
        ddo.setRatio(vo.getRatio());
        ddo.setStdUnit(vo.getStdUnit());
        ddo.setUnit(vo.getUnit());
        ddo.setValueType(vo.getValueType());
        return ddo;
    }

}
