/*
SQLyog Community v12.3.3 (64 bit)
MySQL - 5.5.54 : Database - devicedb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`devicedb` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `devicedb`;

/*Table structure for table `device_access_t` */

DROP TABLE IF EXISTS `device_access_t`;

CREATE TABLE `device_access_t` (
  `serial_number` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '设备序列号',
  `pole_id` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '灯杆编号(挂载的灯杆)',
  `registration` int(1) DEFAULT NULL COMMENT '在网状态(1.在网/0.注销)',
  `north_port` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '北向接口(实际接入的)',
  `north_protocol` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '南向接口(实际使用协议)',
  `south_port` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '北向接口(实际接入的端口)',
  `south_protocol` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '南向接口(实际使用的协议)',
  `protocol_version` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '协议版本(格式化文本(K:V;))',
  `north_agent` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '北向代理(比如盒子，关联SN)',
  `south_agent` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '南向代理(有则使用代理的协议)',
  `access_time` datetime DEFAULT NULL COMMENT '入网时间',
  `leave_time` datetime DEFAULT NULL COMMENT '注销时间(离网注销时间)',
  PRIMARY KEY (`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `device_access_t` */

/*Table structure for table `device_attribute_t` */

DROP TABLE IF EXISTS `device_attribute_t`;

CREATE TABLE `device_attribute_t` (
  `attribute_key` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '属性键(产品注册用的标准商品名)',
  `attribute_action` int(2) DEFAULT NULL COMMENT '属性行为(比如：上行属性，下行控制属性，双工属性，规则属性，告警属性等等，用1-99表示)',
  `device_type` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '设备类型编号(不同型号的特定属性)',
  `device_domain` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '设备域（大类）--区分灯还是屏的模型共有属性',
  `attribute_metadata` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '属性元数据(描述性元数据，比如计量单位)',
  PRIMARY KEY (`attribute_key`,`device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `device_attribute_t` */

/*Table structure for table `device_method_t` */

DROP TABLE IF EXISTS `device_method_t`;

CREATE TABLE `device_method_t` (
  `method_id` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '方法名',
  `method_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '方法说明',
  `method_in` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '方法输入属性',
  `method_out` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '方法输出属性',
  `method_description` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '方法描述',
  `device_type` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '设备类型编号',
  PRIMARY KEY (`method_id`,`device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `device_method_t` */

/*Table structure for table `device_model_t` */

DROP TABLE IF EXISTS `device_model_t`;

CREATE TABLE `device_model_t` (
  `attribute_id` bigint(200) unsigned NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `attribute_key` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '属性键(产品注册用的标准商品名)',
  `attribute_action` int(2) DEFAULT NULL COMMENT '属性行为(比如：上行属性，下行控制属性，双工属性，规则属性，告警属性等等，用1-99表示)',
  `device_type` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '设备类型编号(不同型号的特定属性)',
  `device_domain` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '设备域（大类）--区分灯还是屏的模型共有属性',
  `attribute_metadata` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '属性元数据(描述性元数据，比如计量单位)',
  PRIMARY KEY (`attribute_id`,`attribute_key`,`device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `device_model_t` */

/*Table structure for table `device_t` */

DROP TABLE IF EXISTS `device_t`;

CREATE TABLE `device_t` (
  `serial_number` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '设备序列号',
  `device_alias` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '设备逻辑名(别名，或通俗名，注意不是设备名称)',
  `device_type` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '设备类型编号(逻辑外键)',
  `ex_factory_date` datetime DEFAULT NULL COMMENT '出厂日期',
  `batch_number` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '生产批号',
  `is_edge` tinyint(1) DEFAULT NULL COMMENT '是否边缘网关(盒子选是)',
  PRIMARY KEY (`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `device_t` */

/*Table structure for table `device_type_attribute_t` */

DROP TABLE IF EXISTS `device_type_attribute_t`;

CREATE TABLE `device_type_attribute_t` (
  `device_type` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '设备类型编号',
  `attribute_key` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '属性键',
  PRIMARY KEY (`device_type`,`attribute_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `device_type_attribute_t` */

/*Table structure for table `device_type_mapped_t` */

DROP TABLE IF EXISTS `device_type_mapped_t`;

CREATE TABLE `device_type_mapped_t` (
  `id` bigint(200) unsigned NOT NULL AUTO_INCREMENT COMMENT '条目ID',
  `attribute_id` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '属性键(产品注册用的标准商品名)',
  `key_alias` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '属性别名(与标准模型中含义相同但是名称不同的定义，用于设备灵活接入)',
  `information` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '必要描述(描述与备注)',
  `device_domain` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '设备域（大类）--区分灯还是屏的模型共有属性',
  `device_type` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '设备类型编号(不同型号的特定属性)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `device_type_mapped_t` */

/*Table structure for table `device_type_t` */

DROP TABLE IF EXISTS `device_type_t`;

CREATE TABLE `device_type_t` (
  `device_type` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '设备类型编号(主键，对应的是一个具体的型号)',
  `device_name` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '设备名称(产品注册用的标准商品名)',
  `device_domain` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '设备域(大类)--每一种大类有不同的领域模型',
  `device_code` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '设备商品条码(国标规范的商品条码)',
  `manufacturer_code` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '生产商编号(企业代码，注意不是企业名称)',
  `specifications` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '设备规格',
  `communication_ports` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '支持的通信接口',
  `support_protocols` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '支持的协议集合',
  PRIMARY KEY (`device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `device_type_t` */

/*Table structure for table `lamp_pole_t` */

DROP TABLE IF EXISTS `lamp_pole_t`;

CREATE TABLE `lamp_pole_t` (
  `id` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '灯杆序列号',
  `pole_alias` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '灯杆逻辑名(比如龙岗区清林路11076杆)',
  `location` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '灯杆经纬度',
  `location_type` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '经纬度格式',
  `pole_type` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '灯杆类别(0-物理，1-虚拟)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `lamp_pole_t` */

/*Table structure for table `manufacturer_t` */

DROP TABLE IF EXISTS `manufacturer_t`;

CREATE TABLE `manufacturer_t` (
  `manufacturer_code` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '生产商编号',
  `manufacturer_name` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '生产商名称(企业名称)',
  `address` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '企业地址',
  `contact_info` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '企业联系方式',
  `attr1` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '预留扩展',
  `attr2` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '预留扩展',
  `attr3` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '预留扩展',
  PRIMARY KEY (`manufacturer_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `manufacturer_t` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
