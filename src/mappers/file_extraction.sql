/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : file_extraction

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 06/11/2023 17:00:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for analysis_content_matches
-- ----------------------------
DROP TABLE IF EXISTS `analysis_content_matches`;
CREATE TABLE `analysis_content_matches`  (
  `index` int NOT NULL AUTO_INCREMENT,
  `id` int NOT NULL COMMENT '关联的分析点id',
  `type` int NULL DEFAULT NULL COMMENT '数据类型，0表示table，1表示image，2表示text',
  `content` longtext CHARACTER SET utf8mb4 NULL COMMENT '数据内容',
  `fid` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  PRIMARY KEY (`index`) USING BTREE,
  INDEX `id`(`id` ASC) USING BTREE,
  CONSTRAINT `id` FOREIGN KEY (`id`) REFERENCES `analysis_points` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3811 CHARACTER SET = utf8mb4 ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for analysis_hierarchy
-- ----------------------------
DROP TABLE IF EXISTS `analysis_hierarchy`;
CREATE TABLE `analysis_hierarchy`  (
  `cid` int NOT NULL,
  `pid` int NOT NULL,
  `layer` int NOT NULL,
  `fid` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  PRIMARY KEY (`cid`) USING BTREE,
  INDEX `pid`(`pid` ASC) USING BTREE,
  INDEX `fid`(`fid` ASC) USING BTREE,
  CONSTRAINT `cid` FOREIGN KEY (`cid`) REFERENCES `analysis_points` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `pid` FOREIGN KEY (`pid`) REFERENCES `analysis_points` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for analysis_points
-- ----------------------------
DROP TABLE IF EXISTS `analysis_points`;
CREATE TABLE `analysis_points`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `fid` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '所属文件id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fid`(`fid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 761 CHARACTER SET = utf8mb4 ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
