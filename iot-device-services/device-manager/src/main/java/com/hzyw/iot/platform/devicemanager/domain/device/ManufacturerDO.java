package com.hzyw.iot.platform.devicemanager.domain.device;

import lombok.Data;

/**
 * @POJO 生产商
 * @blame Android Team
 */

@Data
public class ManufacturerDO {
    private Integer manufacturerCode; //'生产商编号'
    private String manufacturerName; // '生产商名称(企业名称)'
    private String address; //  '企业地址'
    private String contactInfo; // '企业联系方式'
    private String attr1; // '预留扩展'
    private String attr2; // '预留扩展'
    private String attr3; // '预留扩展'

}
