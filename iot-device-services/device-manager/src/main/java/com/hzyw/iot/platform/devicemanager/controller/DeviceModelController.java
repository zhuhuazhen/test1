//package com.hzyw.iot.platform.devicemanager.controller;
//
//import com.hzyw.iot.platform.devicemanager.domain.comm.ResultVO;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 设备模型维护接口 (新增、修改、删除)
// */
//@RestController
//@RequestMapping("/device/model")
//public class DeviceModelController {
//
//    DeviceModelService modelService;
//
//    /**
//     * 设备模型新增
//     * @param model
//     * @return
//     */
//    @PostMapping("/add")
//    public Object addModel(@RequestBody DeviceModel model) {
//        try {
//            modelService.addOneDeviceModel(model);
//            return ResultVO.getSuccess("添加成功!");
//        } catch (Exception e) {
//            return ResultVO.getFailed("添加失败! Exception: "+e.getMessage());
//        }
//    }
//
//    /**
//     * 设备模型修改
//     * @param model
//     * @return
//     */
//    @PostMapping("/update")
//    public Object updateModel(@RequestBody DeviceModel model) {
//        try {
//            modelService.updateDeviceModelByID(model);
//            return ResultVO.getSuccess("更新成功!");
//        } catch (Exception e) {
//            return ResultVO.getFailed("更新失败! Exception: "+e.getMessage());
//        }
//    }
//
//    /**设备模型删除
//     * @param model
//     * @return
//     */
//    @PostMapping("/delete")
//    public Object deleteModel(@RequestBody DeviceModel model){
//        try {
//            modelService.deleteByID(model);
//            return ResultVO.getSuccess("删除成功!");
//        } catch (Exception e) {
//            return ResultVO.getFailed("删除失败! Exception: "+e.getMessage());
//        }
//    }
//}
