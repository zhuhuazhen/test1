/*
 Navicat Premium Data Transfer

 Source Server         : 10.100.254.147
 Source Server Type    : MySQL
 Source Server Version : 50644
 Source Host           : localhost:3306
 Source Schema         : febs_shiro_jwt

 Target Server Type    : MySQL
 Target Server Version : 50644
 File Encoding         : 65001

 Date: 02/09/2019 22:47:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for au_app_t
-- ----------------------------
DROP TABLE IF EXISTS `au_app_t`;
CREATE TABLE `au_app_t`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `sort` int(11) NOT NULL COMMENT '排序',
  `createTime` datetime(0) NOT NULL COMMENT '创建时间',
  `isEnable` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否启用',
  `code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for au_dept_t
-- ----------------------------
DROP TABLE IF EXISTS `au_dept_t`;
CREATE TABLE `au_dept_t`  (
  `DEPT_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `PARENT_ID` int(11) NOT NULL COMMENT '上级部门ID',
  `DEPT_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `ORDER_NUM` int(11) NULL DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`DEPT_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for au_log_t
-- ----------------------------
DROP TABLE IF EXISTS `au_log_t`;
CREATE TABLE `au_log_t`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `USERNAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户',
  `OPERATION` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作内容',
  `TIME` decimal(11, 0) NULL DEFAULT NULL COMMENT '耗时',
  `METHOD` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作方法',
  `PARAMS` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '方法参数',
  `IP` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作者IP',
  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `location` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作地点',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1839 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for au_login_log_t
-- ----------------------------
DROP TABLE IF EXISTS `au_login_log_t`;
CREATE TABLE `au_login_log_t`  (
  `USERNAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `LOGIN_TIME` datetime(0) NOT NULL COMMENT '登录时间',
  `LOCATION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录地点',
  `IP` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP地址'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for au_permission_t
-- ----------------------------
DROP TABLE IF EXISTS `au_permission_t`;
CREATE TABLE `au_permission_t`  (
  `PERMISSION_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `PARENT_ID` int(11) NOT NULL COMMENT '上级菜单ID',
  `PERMISSION_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单/按钮名称',
  `PATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
  `PERMS` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `ICON` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标名称',
  `APPID` int(11) NULL DEFAULT NULL COMMENT '应用ID',
  `TYPE` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型 0菜单 1按钮',
  `ORDER_NUM` int(11) NULL DEFAULT NULL COMMENT '排序',
  `ISEnable` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否启用',
  `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`PERMISSION_ID`) USING BTREE,
  INDEX `FK_AU_PERMISSION_APPID`(`APPID`) USING BTREE,
  CONSTRAINT `FK_AU_PERMISSION_APPID` FOREIGN KEY (`APPID`) REFERENCES `au_app_t` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 139 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for au_role_permission_t
-- ----------------------------
DROP TABLE IF EXISTS `au_role_permission_t`;
CREATE TABLE `au_role_permission_t`  (
  `id` int(11) NOT NULL,
  `ROLE_ID` int(11) NOT NULL,
  `PERMISSION_ID` int(11) NOT NULL,
  `appId` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_AU_ROLE_PERMISSION_APPID`(`appId`) USING BTREE,
  CONSTRAINT `FK_AU_ROLE_PERMISSION_APPID` FOREIGN KEY (`appId`) REFERENCES `au_app_t` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for au_role_t
-- ----------------------------
DROP TABLE IF EXISTS `au_role_t`;
CREATE TABLE `au_role_t`  (
  `ROLE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `ROLE_NAME` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `APP_ID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用ID',
  `REMARK` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `isEnable` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否启用',
  PRIMARY KEY (`ROLE_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 74 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for au_user_role_t
-- ----------------------------
DROP TABLE IF EXISTS `au_user_role_t`;
CREATE TABLE `au_user_role_t`  (
  `ID` int(11) NOT NULL COMMENT '用户角色关系ID',
  `USER_ID` int(11) NOT NULL COMMENT '用户ID',
  `ROLE_ID` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for au_user_t
-- ----------------------------
DROP TABLE IF EXISTS `au_user_t`;
CREATE TABLE `au_user_t`  (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USERNAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `DEPT_ID` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `EMAIL` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `MOBILE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `STATUS` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态 0锁定 1有效2无效',
  `LAST_LOGIN_TIME` datetime(0) NULL DEFAULT NULL COMMENT '最近访问时间',
  `SSEX` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `DESCRIPTION` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `AVATAR` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`USER_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
