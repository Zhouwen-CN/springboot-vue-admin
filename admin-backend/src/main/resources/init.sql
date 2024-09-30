-- MySQL dump 10.13  Distrib 8.1.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: springboot_vue_admin
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;
SET
@MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET
@@SESSION.SQL_LOG_BIN = 0;

--
-- GTID state at the beginning of the backup 
--

SET
@@GLOBAL.GTID_PURGED = /*!80000 '+'*/ 'c356e02c-5051-11ee-87d0-e070eacf9427:1-136100';

--
-- Table structure for table `t_data_field`
--

DROP TABLE IF EXISTS `t_data_field`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_data_field`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(100) NOT NULL COMMENT '领域名称',
    `desc`        varchar(255) DEFAULT NULL COMMENT '说明',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COMMENT='数据域表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_data_field`
--

LOCK
TABLES `t_data_field` WRITE;
/*!40000 ALTER TABLE `t_data_field` DISABLE KEYS */;
INSERT INTO `t_data_field`
VALUES (1, 'bdf', '基础数据域', '2024-05-06 13:57:21', '2024-05-06 13:57:21'),
       (2, 'psf', '产销信息域', '2024-05-06 13:57:21', '2024-05-06 13:57:21'),
       (3, 'vuf', '车辆使用域', '2024-05-06 13:57:21', '2024-05-06 13:57:21'),
       (4, 'asf', '车辆售后域', '2024-05-06 13:57:21', '2024-05-06 13:57:21');
/*!40000 ALTER TABLE `t_data_field` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `t_data_range`
--

DROP TABLE IF EXISTS `t_data_range`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_data_range`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `range`       varchar(100) NOT NULL COMMENT '范围/周期',
    `desc`        varchar(255) DEFAULT NULL COMMENT '说明',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_range` (`range`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 COMMENT='数据范围表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_data_range`
--

LOCK
TABLES `t_data_range` WRITE;
/*!40000 ALTER TABLE `t_data_range` DISABLE KEYS */;
INSERT INTO `t_data_range`
VALUES (1, 'i', '增量', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (2, 'f', '分区全量', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (3, 'a', '非分区全量', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (4, 'z', '拉链表', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (5, 'd', '日快照', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (6, 'w', '周', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (7, 'm', '月', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (8, 'y', '年', '2024-05-06 13:57:53', '2024-05-06 13:57:53');
/*!40000 ALTER TABLE `t_data_range` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `t_data_storey`
--

DROP TABLE IF EXISTS `t_data_storey`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_data_storey`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `storey`      varchar(100) NOT NULL COMMENT '层名',
    `desc`        varchar(255) DEFAULT NULL COMMENT '说明',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_storey` (`storey`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 COMMENT='数仓层级表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_data_storey`
--

LOCK
TABLES `t_data_storey` WRITE;
/*!40000 ALTER TABLE `t_data_storey` DISABLE KEYS */;
INSERT INTO `t_data_storey`
VALUES (1, 'ods', '原始数据层', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (2, 'dwd', '数据明细层', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (3, 'dwm', '数据中间层', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (4, 'dws', '数据服务层', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (5, 'ads', '数据应用层', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (6, 'dim', '维度层', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (7, 'mid', '中间表-可以删除', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (8, 'tmp', '临时表-可以删除', '2024-05-06 13:57:53', '2024-05-06 13:57:53');
/*!40000 ALTER TABLE `t_data_storey` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `t_menu`
--

DROP TABLE IF EXISTS `t_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_menu`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title`       varchar(100) NOT NULL COMMENT '标题',
    `access_path` varchar(100) NOT NULL COMMENT '访问路径',
    `file_path`   varchar(255) DEFAULT NULL COMMENT '文件路径',
    `icon`        varchar(100) NOT NULL COMMENT '图标',
    `pid`         bigint       NOT NULL COMMENT '一级菜单id',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8mb3 COMMENT ='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_menu`
--

LOCK
TABLES `t_menu` WRITE;
/*!40000 ALTER TABLE `t_menu`
    DISABLE KEYS */;
INSERT INTO `t_menu`
VALUES (1, '权限管理', '/auth', NULL, 'Lock', 0, '2024-05-06 13:46:48', '2024-05-16 04:04:36'),
       (2, '用户管理', '/auth/user', '/layout/auth/user/index.vue', 'User', 1, '2024-05-06 13:46:48',
        '2024-05-06 13:46:48'),
       (3, '角色管理', '/auth/role', '/layout/auth/role/index.vue', 'Avatar', 1, '2024-05-06 13:46:48',
        '2024-05-06 13:46:48'),
       (4, '菜单管理', '/auth/menu', '/layout/auth/menu/index.vue', 'List', 1, '2024-05-06 13:46:48',
        '2024-05-06 13:46:48'),
       (5, '数据域管理', '/field', '/layout/field/index.vue', 'Location', 0, '2024-05-06 13:46:48',
        '2024-05-06 13:46:48'),
       (6, '数据范围管理', '/range', '/layout/range/index.vue', 'Location', 0, '2024-05-06 13:46:48',
        '2024-05-06 13:46:48'),
       (7, '数仓层级管理', '/storey', '/layout/storey/index.vue', 'Location', 0, '2024-05-06 13:46:48',
        '2024-05-06 13:46:48'),
       (8, '词根管理', '/word', '/layout/word/index.vue', 'Location', 0, '2024-05-06 13:46:48', '2024-05-06 13:46:48'),
       (9, '测试子1', '/word/sub-1', NULL, 'Location', 8, '2024-05-13 00:50:29', '2024-05-13 00:50:29'),
       (10, '测试孙1', '/word/sub-1/sub-2', '/layout/word/subtest1.vue', 'Location', 9, '2024-05-13 00:50:29',
        '2024-05-13 00:50:29'),
       (11, '测试孙2', '/word/sub-1/sub-3', '/layout/word/subtest2.vue', 'HomeFilled', 9, '2024-05-13 00:50:29',
        '2024-05-13 00:50:29');
/*!40000 ALTER TABLE `t_menu`
    ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `t_role`
--

DROP TABLE IF EXISTS `t_role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_role`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_name`   varchar(100) NOT NULL COMMENT '角色名称',
    `desc`        varchar(255) NOT NULL COMMENT '角色说明',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_role_name` (`role_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8mb3 COMMENT ='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role`
--

LOCK
TABLES `t_role` WRITE;
/*!40000 ALTER TABLE `t_role`
    DISABLE KEYS */;
INSERT INTO `t_role`
VALUES (1, 'admin', '超级管理员', '2024-05-06 13:45:21', '2024-05-16 03:21:03'),
       (2, 'test', '测试', '2024-05-06 13:45:21', '2024-05-16 01:31:23'),
       (3, 'dev', '开发', '2024-05-15 06:21:25', '2024-05-15 06:21:25'),
       (4, 'frontend', '前端', '2024-05-15 06:21:25', '2024-05-15 06:21:25'),
       (5, 'backend', '后端', '2024-05-15 06:21:25', '2024-05-15 06:21:25'),
       (6, 'bigdata', '大数据', '2024-05-15 06:21:25', '2024-05-15 06:21:25');
/*!40000 ALTER TABLE `t_role`
    ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `t_role_menu`
--

DROP TABLE IF EXISTS `t_role_menu`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_role_menu`
(
    `id`      bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id` bigint NOT NULL COMMENT '角色id',
    `menu_id` bigint NOT NULL COMMENT '菜单id',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8mb3 COMMENT ='用户权限关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role_menu`
--

LOCK
TABLES `t_role_menu` WRITE;
/*!40000 ALTER TABLE `t_role_menu`
    DISABLE KEYS */;
INSERT INTO `t_role_menu`
VALUES (1, 1, 1, '2024-05-06 13:47:16', '2024-05-06 13:47:16'),
       (2, 1, 2, '2024-05-06 13:47:16', '2024-05-06 13:47:16'),
       (3, 1, 3, '2024-05-16 03:30:19', '2024-05-16 03:30:19'),
       (4, 1, 4, '2024-05-06 13:47:16', '2024-05-06 13:47:16'),
       (5, 1, 5, '2024-05-16 03:30:19', '2024-05-16 03:30:19'),
       (6, 1, 6, '2024-05-16 01:30:24', '2024-05-16 01:30:24'),
       (7, 1, 7, '2024-05-16 01:30:24', '2024-05-16 01:30:24'),
       (8, 1, 8, '2024-05-16 01:31:12', '2024-05-16 01:31:12'),
       (9, 1, 9, '2024-05-16 01:31:12', '2024-05-16 01:31:12'),
       (10, 1, 10, '2024-05-16 03:30:19', '2024-05-16 03:30:19'),
       (11, 1, 11, '2024-05-16 01:31:12', '2024-05-16 01:31:12');
/*!40000 ALTER TABLE `t_role_menu`
    ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `t_root_word`
--

DROP TABLE IF EXISTS `t_root_word`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_root_word`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `word`        varchar(100) NOT NULL COMMENT '单词',
    `desc`        varchar(255) DEFAULT NULL COMMENT '说明',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_word` (`word`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb3 COMMENT='词根表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_root_word`
--

LOCK
TABLES `t_root_word` WRITE;
/*!40000 ALTER TABLE `t_root_word` DISABLE KEYS */;
INSERT INTO `t_root_word`
VALUES (1, 'material', '物质的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (2, 'medical', '医学的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (3, 'military', '军事的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (4, 'natural', '自然的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (5, 'necessary', '必要的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (6, 'new', '新的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (7, 'normal', '正常的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (8, 'open', '打开的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (9, 'parallel', '平行的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (10, 'past', '过去的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (11, 'physical', '身体的；物质的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (12, 'political', '政治的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (13, 'poor', '贫穷的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (14, 'possible', '可能的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (15, 'present', '在场的；现在的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (16, 'private', '个人的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (17, 'probable', '很可能的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (18, 'quick', '快的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (19, 'quiet', '安静的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (20, 'ready', '准备好的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (21, 'red', '红色的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (22, 'regular', '规则的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (23, 'responsible', '负责任的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (24, 'right', '右的；对的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (25, 'round', '圆的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (26, 'same', '同样的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (27, 'second', '第二的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (28, 'separate', '分开的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (29, 'serious', '严重的；严肃的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (30, 'sharp', '锋利的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (31, 'smooth', '平滑的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (32, 'sticky', '粘的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (33, 'stiff', '硬的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (34, 'straight', '直的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (35, 'strong', '强的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (36, 'sudden', '突然的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (37, 'sweet', '甜的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (38, 'tall', '身材高的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (39, 'thick', '厚的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (40, 'tight', '紧的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (41, 'tired', '疲倦的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (42, 'TRUE', '真实的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (43, 'violent', '激烈的；暴力的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (44, 'waiting', '等待的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (45, 'warm', '温暖的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (46, 'wet', '湿的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (47, 'wide', '宽阔的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (48, 'wise', '聪明的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (49, 'yellow', '黄色的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (50, 'young', '年青的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (51, 'awake', '清醒的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (52, 'bad', '坏的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (53, 'bent', '弯曲的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (54, 'bitter', '苦的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (55, 'blue', '蓝色的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (56, 'certain', '肯定的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (57, 'cold', '冷的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (58, 'complete', '完整的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (59, 'cruel', '残酷的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (60, 'dark', '黑暗的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (61, 'dead', '死的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (62, 'expensive', '昂贵的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (63, 'dear', '亲爱的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (64, 'delicate', '娇贵的；微妙的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (65, 'different', '不同的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (66, 'dirty', '脏的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (67, 'dry', '干的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (68, 'FALSE', '假的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (69, 'female', '女性的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (70, 'foolish', '愚蠢的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (71, 'future', '未来的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (72, 'green', '绿的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (73, 'ill', '生病的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (74, 'last', '最后的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (75, 'late', '晚的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (76, 'left', '左的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (77, 'loose', '松的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (78, 'loud', '大声的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (79, 'low', '低的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (80, 'mixed', '混合的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (81, 'narrow', '窄的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (82, 'old', '老的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (83, 'opposite', '相反的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (84, 'public', '公众的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (85, 'rough', '粗糙的；狂暴的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (86, 'sad', '悲哀的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (87, 'safe', '安全的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (88, 'secret', '秘密的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (89, 'short', '短的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (90, 'shut', '关闭的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (91, 'simple', '简单的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (92, 'slow', '慢的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (93, 'small', '小的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (94, 'soft', '柔软的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (95, 'solid', '固定的；结实的；纯的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (96, 'special', '特殊的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (97, 'strange', '奇怪的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (98, 'thin', '薄的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (99, 'white', '白色的', '2024-05-06 13:57:53', '2024-05-06 13:57:53'),
       (100, 'wrong', '错误的', '2024-05-06 13:57:53', '2024-05-06 13:57:53');
/*!40000 ALTER TABLE `t_root_word` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user`
(
    `id`            bigint           NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`      varchar(100)     NOT NULL COMMENT '用户名',
    `password`      varchar(255)     NOT NULL COMMENT '密码',
    `token_version` bigint default 0 not null comment 'access和refresh共用一个version',
    `create_time`   timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_username` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb3 COMMENT ='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK
TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user`
    DISABLE KEYS */;
INSERT INTO `t_user`
VALUES (1, 'admin', '$2a$10$wTV12Fs5hQfhG9RzZuKd6.c135tv2y8PTJAtKRhlXnU.9xS5Qqcxu', 0, '2024-05-06 13:45:21',
        '2024-05-16 02:13:17');
/*!40000 ALTER TABLE `t_user`
    ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `t_user_role`
--

DROP TABLE IF EXISTS `t_user_role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user_role`
(
    `id`      bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` bigint NOT NULL COMMENT '用户id',
    `role_id` bigint NOT NULL COMMENT '角色id',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb3 COMMENT ='用户角色关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_role`
--

LOCK
TABLES `t_user_role` WRITE;
/*!40000 ALTER TABLE `t_user_role`
    DISABLE KEYS */;
INSERT INTO `t_user_role`
VALUES (1, 1, 1, '2024-05-06 13:45:21', '2024-05-06 13:45:21');
/*!40000 ALTER TABLE `t_user_role`
    ENABLE KEYS */;
UNLOCK
TABLES;
SET
@@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-16 13:07:29
