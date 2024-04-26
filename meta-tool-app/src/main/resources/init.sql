-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: meta_tool
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

create
database if not exists `meta-tool`;
use
`meta-tool`;

SET
@MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET
@@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET
@@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'c356e02c-5051-11ee-87d0-e070eacf9427:1-131664';

--
-- Table structure for table `t_data_field`
--

DROP TABLE IF EXISTS `t_data_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_data_field`
(
    `id`   bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(100) NOT NULL COMMENT '领域名称',
    `desc` varchar(255) DEFAULT NULL COMMENT '说明',
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
VALUES (1, 'bdf', '基础数据域'),
       (2, 'psf', '产销信息域'),
       (3, 'vuf', '车辆使用域'),
       (4, 'asf', '车辆售后域');
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
    `id`    bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `range` varchar(100) NOT NULL COMMENT '范围/周期',
    `desc`  varchar(255) DEFAULT NULL COMMENT '说明',
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
VALUES (1, 'i', '增量'),
       (2, 'f', '分区全量'),
       (3, 'a', '非分区全量'),
       (4, 'z', '拉链表'),
       (5, 'd', '日快照'),
       (6, 'w', '周'),
       (7, 'm', '月'),
       (8, 'y', '年');
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
    `id`     bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `storey` varchar(100) NOT NULL COMMENT '层名',
    `desc`   varchar(255) DEFAULT NULL COMMENT '说明',
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
VALUES (1, 'ods', '原始数据层'),
       (2, 'dwd', '数据明细层'),
       (3, 'dwm', '数据中间层'),
       (4, 'dws', '数据服务层'),
       (5, 'ads', '数据应用层'),
       (6, 'dim', '维度层'),
       (7, 'mid', '中间表-可以删除'),
       (8, 'tmp', '临时表-可以删除');
/*!40000 ALTER TABLE `t_data_storey` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `t_root_word`
--

DROP TABLE IF EXISTS `t_root_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_root_word`
(
    `id`   bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `word` varchar(100) NOT NULL COMMENT '单词',
    `desc` varchar(255) DEFAULT NULL COMMENT '说明',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_word` (`word`)
) ENGINE=InnoDB AUTO_INCREMENT=851 DEFAULT CHARSET=utf8mb3 COMMENT='词根表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_root_word`
--

LOCK
TABLES `t_root_word` WRITE;
/*!40000 ALTER TABLE `t_root_word` DISABLE KEYS */;
INSERT INTO `t_root_word`
VALUES (1, 'be', '( am   is   are   was   were   been   being ) 是'),
       (2, 'go', '( went   gone   going   goes ) 去'),
       (3, 'come', '( came   coming ) 来'),
       (4, 'get', '( got ) 获得'),
       (5, 'give', '( gave   given ) 给予'),
       (6, 'take', '( took   taken ) 拿'),
       (7, 'put', '放'),
       (8, 'make', '( made ) 制作'),
       (9, 'do', '( did   done   doing   does ) 做'),
       (10, 'have', '( had   having   has ) 有'),
       (11, 'keep', '( kept ) 持有'),
       (12, 'let', '让'),
       (13, 'seem', '看来好像'),
       (14, 'see', '( saw   seen   seeing ) 看'),
       (15, 'say', '( said   saying   says ) 说'),
       (16, 'send', '( sent ) 进'),
       (17, 'will', '( would ) 将；愿'),
       (18, 'may', '( might ) 可能；可以'),
       (19, 'on', '在...上'),
       (20, 'off', '离开'),
       (21, 'of', '...的'),
       (22, 'at', '在...地点，在...时刻'),
       (23, 'in', '在...里'),
       (24, 'out', '在外'),
       (25, 'from', '从'),
       (26, 'to', '到'),
       (27, 'before', '在...以前'),
       (28, 'after', '在...以后'),
       (29, 'up', '向...上'),
       (30, 'down', '往...下方'),
       (31, 'over', '在...之上'),
       (32, 'under', '在...下面'),
       (33, 'across', '横越'),
       (34, 'through', '穿过'),
       (35, 'for', '为'),
       (36, 'against', '反对'),
       (37, 'between', '在...（两者）之间'),
       (38, 'among', '在...之中'),
       (39, 'about', '关于；在...的附近'),
       (40, 'by', '被；由；在...的旁边'),
       (41, 'with', '用'),
       (42, 'as', '作为；像...一样；当...时'),
       (43, 'till', '直到...为止'),
       (44, 'than', '比'),
       (45, 'a', '一'),
       (46, 'the', '这；那'),
       (47, 'all', '全体'),
       (48, 'any', '任何一个'),
       (49, 'every', '每一个'),
       (50, 'little', '少的；小的'),
       (51, 'much', '许多；非常'),
       (52, 'no', '不；没有'),
       (53, 'other', '另一个；其余的'),
       (54, 'some', '有些；某些'),
       (55, 'such', '这样的；如此的'),
       (56, 'that', '那'),
       (57, 'this', '这'),
       (58, 'I', '我'),
       (59, 'he', '他'),
       (60, 'you', '你'),
       (61, 'who', '谁'),
       (62, 'and', '和'),
       (63, 'because', '因为'),
       (64, 'but', '但是'),
       (65, 'or', '或者'),
       (66, 'if', '如果；是否'),
       (67, 'though', '虽然'),
       (68, 'while', '当...的时候'),
       (69, 'how', '怎样'),
       (70, 'when', '什么时候'),
       (71, 'where', '哪里'),
       (72, 'why', '为什么'),
       (73, 'again', '再'),
       (74, 'ever', '从来；永远'),
       (75, 'far', '远'),
       (76, 'forward', '向前'),
       (77, 'here', '这里'),
       (78, 'near', '近；接近'),
       (79, 'now', '现在'),
       (80, 'still', '还；还是'),
       (81, 'then', '那时'),
       (82, 'there', '在那里'),
       (83, 'together', '一起'),
       (84, 'well', '很好'),
       (85, 'almost', '几乎'),
       (86, 'enough', '足够的'),
       (87, 'even', '甚至'),
       (88, 'not', '不'),
       (89, 'only', '只'),
       (90, 'quite', '相当'),
       (91, 'so', '这么，那么'),
       (92, 'very', '非常，很'),
       (93, 'tomorrow', '明天'),
       (94, 'yesterday', '昨天'),
       (95, 'north', '北'),
       (96, 'south', '南'),
       (97, 'east', '东'),
       (98, 'west', '西'),
       (99, 'please', '请；使高兴'),
       (100, 'yes', '是'),
       (101, 'angle', '角；角度'),
       (102, 'ant', '蚂蚁'),
       (103, 'apple', '苹果'),
       (104, 'arch', '拱形；弧形'),
       (105, 'arm', '臂'),
       (106, 'army', '军队'),
       (107, 'baby', '婴儿'),
       (108, 'bag', '袋'),
       (109, 'ball', '球'),
       (110, 'band', '带子；乐队'),
       (111, 'basin', '盆'),
       (112, 'basket', '篮'),
       (113, 'bath', '浴缸'),
       (114, 'bed', '床'),
       (115, 'bee', '蜜蜂'),
       (116, 'bell', '钟；铃'),
       (117, 'berry', '莓果'),
       (118, 'bird', '鸟'),
       (119, 'blade', '刀身'),
       (120, 'board', '木板'),
       (121, 'boat', '小船'),
       (122, 'bone', '骨'),
       (123, 'book', '书'),
       (124, 'boot', '靴'),
       (125, 'bottle', '瓶子'),
       (126, 'box', '箱；盒'),
       (127, 'boy', '男孩'),
       (128, 'brain', '脑'),
       (129, 'brake', '煞车'),
       (130, 'branch', '树枝'),
       (131, 'brick', '砖'),
       (132, 'bridge', '桥'),
       (133, 'brush', '刷子'),
       (134, 'bucket', '水桶'),
       (135, 'bulb', '球茎；灯泡'),
       (136, 'button', '纽扣'),
       (137, 'cake', '蛋糕'),
       (138, 'camera', '照相机'),
       (139, 'card', '卡片；纸牌'),
       (140, 'cart', '小车'),
       (141, 'carriage', '车厢'),
       (142, 'cat', '猫'),
       (143, 'chain', '链'),
       (144, 'cheese', '乳酪'),
       (145, 'chest', '胸'),
       (146, 'chicken', '鸡'),
       (147, 'chin', '下巴'),
       (148, 'church', '教堂'),
       (149, 'circle', '圆圈'),
       (150, 'clock', '时钟'),
       (151, 'cloud', '云'),
       (152, 'coat', '外套，大衣'),
       (153, 'collar', '衣领'),
       (154, 'comb', '梳子'),
       (155, 'cord', '绳子'),
       (156, 'cow', '母牛'),
       (157, 'cup', '杯子'),
       (158, 'curtain', '帘；幕'),
       (159, 'cushion', '垫子'),
       (160, 'dog', '狗'),
       (161, 'door', '门'),
       (162, 'drain', '排水管'),
       (163, 'drawer', '抽屉'),
       (164, 'dress', '女装'),
       (165, 'drop', '滴；落下'),
       (166, 'ear', '耳'),
       (167, 'egg', '蛋；鸡蛋'),
       (168, 'engine', '发动机；机车'),
       (169, 'eye', '眼睛'),
       (170, 'face', '脸'),
       (171, 'farm', '农场'),
       (172, 'feather', '羽毛'),
       (173, 'finger', '手指'),
       (174, 'fish', '鱼'),
       (175, 'flag', '旗'),
       (176, 'floor', '地板'),
       (177, 'fly', '苍蝇'),
       (178, 'foot', '脚'),
       (179, 'fork', '叉'),
       (180, 'frame', '结构；框架'),
       (181, 'garden', '花园'),
       (182, 'girl', '姑娘'),
       (183, 'glove', '手套'),
       (184, 'goat', '山羊'),
       (185, 'gun', '枪；炮'),
       (186, 'hair', '毛发'),
       (187, 'hammer', '铁锤'),
       (188, 'hand', '手'),
       (189, 'hat', '帽子'),
       (190, 'head', '头'),
       (191, 'heart', '心脏'),
       (192, 'hook', '钩'),
       (193, 'horn', '角；号角'),
       (194, 'horse', '马'),
       (195, 'hospital', '医院'),
       (196, 'house', '房子'),
       (197, 'island', '岛'),
       (198, 'jewel', '宝石'),
       (199, 'kettle', '水壶'),
       (200, 'key', '钥匙'),
       (201, 'knee', '膝'),
       (202, 'knife', '刀'),
       (203, 'knot', '（绳）结'),
       (204, 'leaf', '叶子'),
       (205, 'leg', '腿'),
       (206, 'library', '图书馆'),
       (207, 'line', '线，绳'),
       (208, 'lip', '嘴唇'),
       (209, 'lock', '锁'),
       (210, 'map', '地图'),
       (211, 'match', '火柴；比赛'),
       (212, 'monkey', '猴子'),
       (213, 'moon', '月亮'),
       (214, 'mouth', '嘴'),
       (215, 'muscle', '肌肉'),
       (216, 'nail', '指甲'),
       (217, 'neck', '脖子'),
       (218, 'needle', '针'),
       (219, 'nerve', '神经'),
       (220, 'net', '网'),
       (221, 'nose', '鼻'),
       (222, 'nut', '坚果'),
       (223, 'office', '办公室'),
       (224, 'orange', '橙'),
       (225, 'oven', '炉；烤箱'),
       (226, 'parcel', '包裹'),
       (227, 'pen', '钢笔'),
       (228, 'pencil', '铅笔'),
       (229, 'picture', '画'),
       (230, 'pig', '猪'),
       (231, 'pin', '大头针；别针'),
       (232, 'pipe', '管'),
       (233, 'plane', '平面；飞机'),
       (234, 'plate', '盘子'),
       (235, 'plow', '犁'),
       (236, 'pocket', '口袋'),
       (237, 'pot', '罐'),
       (238, 'potato', '马铃薯'),
       (239, 'prison', '监狱'),
       (240, 'pump', '抽水机；打气筒'),
       (241, 'rail', '栏杆；铁轨'),
       (242, 'rat', '老鼠'),
       (243, 'receipt', '收据'),
       (244, 'ring', '环；戒指'),
       (245, 'rod', '棒，杆'),
       (246, 'roof', '屋顶'),
       (247, 'root', '根'),
       (248, 'sail', '帆'),
       (249, 'school', '学校'),
       (250, 'scissors', '剪刀'),
       (251, 'screw', '螺丝钉'),
       (252, 'seed', '种子'),
       (253, 'sheep', '绵羊'),
       (254, 'shelf', '架子'),
       (255, 'ship', '船'),
       (256, 'shirt', '衬衫'),
       (257, 'shoe', '鞋'),
       (258, 'skin', '皮肤'),
       (259, 'skirt', '裙子'),
       (260, 'snake', '蛇'),
       (261, 'sock', '短袜'),
       (262, 'spade', '铲'),
       (263, 'sponge', '海绵'),
       (264, 'spoon', '匙'),
       (265, 'spring', '春天；弹簧；泉'),
       (266, 'square', '正方形；广场'),
       (267, 'stamp', '邮票'),
       (268, 'star', '星'),
       (269, 'station', '车站'),
       (270, 'stem', '茎'),
       (271, 'stick', '枝条；棒；杖'),
       (272, 'stocking', '长袜'),
       (273, 'stomach', '胃；腹部'),
       (274, 'store', '店'),
       (275, 'street', '街'),
       (276, 'sun', '太阳'),
       (277, 'table', '桌子'),
       (278, 'tail', '尾巴'),
       (279, 'thread', '线'),
       (280, 'throat', '咽喉'),
       (281, 'thumb', '拇指'),
       (282, 'ticket', '票'),
       (283, 'toe', '脚趾'),
       (284, 'tongue', '舌'),
       (285, 'tooth', '牙'),
       (286, 'town', '市镇'),
       (287, 'train', '火车'),
       (288, 'tray', '托盘'),
       (289, 'tree', '树'),
       (290, 'trousers', '裤子'),
       (291, 'umbrella', '雨伞'),
       (292, 'wall', '墙'),
       (293, 'watch', '手表'),
       (294, 'wheel', '轮子'),
       (295, 'whip', '鞭子'),
       (296, 'whistle', '口哨'),
       (297, 'window', '窗'),
       (298, 'wing', '翅膀'),
       (299, 'wire', '金属线；电线'),
       (300, 'worm', '虫'),
       (301, 'account', '帐'),
       (302, 'act', '行动'),
       (303, 'addition', '加'),
       (304, 'adjustment', '调节'),
       (305, 'advertisement', '广告'),
       (306, 'agreement', '同意'),
       (307, 'air', '空气'),
       (308, 'amount', '数量'),
       (309, 'amusement', '乐趣'),
       (310, 'animal', '动物'),
       (311, 'answer', '回答'),
       (312, 'apparatus', '器悈'),
       (313, 'approval', '批准'),
       (314, 'argument', '争执'),
       (315, 'art', '艺术'),
       (316, 'attack', '进攻'),
       (317, 'attempt', '试图'),
       (318, 'attention', '注意'),
       (319, 'attraction', '吸引'),
       (320, 'authority', '权；官方'),
       (321, 'back', '背部；后背'),
       (322, 'balance', '平衡'),
       (323, 'base', '基'),
       (324, 'behavior', '行为'),
       (325, 'belief', '相信'),
       (326, 'birth', '出生'),
       (327, 'bit', '小片；少量'),
       (328, 'bite', '咬'),
       (329, 'blood', '血'),
       (330, 'blow', '吹动'),
       (331, 'body', '身体'),
       (332, 'brass', '黄铜'),
       (333, 'bread', '面包'),
       (334, 'breath', '呼吸'),
       (335, 'brother', '兄弟'),
       (336, 'building', '建筑物'),
       (337, 'burn', '烧伤'),
       (338, 'burst', '爆发；破裂'),
       (339, 'business', '生意'),
       (340, 'butter', '奶油'),
       (341, 'canvas', '帆布'),
       (342, 'care', '照料；小心；忧虑'),
       (343, 'cause', '原因；目标'),
       (344, 'chalk', '粉笔'),
       (345, 'chance', '机会'),
       (346, 'change', '变化'),
       (347, 'cloth', '布'),
       (348, 'coal', '煤'),
       (349, 'color', '颜色'),
       (350, 'comfort', '舒适'),
       (351, 'committee', '委员会'),
       (352, 'company', '公司；陪伴'),
       (353, 'comparison', '比较'),
       (354, 'competition', '竞争；比赛'),
       (355, 'condition', '情况'),
       (356, 'connection', '连接；关系'),
       (357, 'control', '控制'),
       (358, 'cook', '厨师'),
       (359, 'copper', '铜'),
       (360, 'copy', '抄本；复印'),
       (361, 'cork', '软木'),
       (362, 'cotton', '棉'),
       (363, 'cough', '咳嗽'),
       (364, 'country', '国家；乡下'),
       (365, 'cover', '遮盖物'),
       (366, 'crack', '裂缝'),
       (367, 'credit', '赊账；信用'),
       (368, 'crime', '罪'),
       (369, 'crush', '压碎'),
       (370, 'cry', '哭；喊叫'),
       (371, 'current', '流动'),
       (372, 'curve', '曲线'),
       (373, 'damage', '损害'),
       (374, 'danger', '危险'),
       (375, 'daughter', '女儿'),
       (376, 'day', '日'),
       (377, 'death', '死'),
       (378, 'debt', '债'),
       (379, 'decision', '决定'),
       (380, 'degree', '度；程度；学位'),
       (381, 'design', '设计'),
       (382, 'desire', '渴望'),
       (383, 'destruction', '毁灭'),
       (384, 'detail', '细节'),
       (385, 'development', '发展'),
       (386, 'digestion', '消化'),
       (387, 'direction', '方向'),
       (388, 'discovery', '发现'),
       (389, 'discussion', '讨论'),
       (390, 'disease', '病'),
       (391, 'disgust', '厌恶'),
       (392, 'distance', '距离'),
       (393, 'distribution', '分配；分布；销售'),
       (394, 'division', '分开'),
       (395, 'doubt', '怀疑'),
       (396, 'drink', '饮料'),
       (397, 'driving', '操纵；驾驶'),
       (398, 'dust', '灰尘'),
       (399, 'earth', '地球；地面；土'),
       (400, 'edge', '边缘'),
       (401, 'education', '教育'),
       (402, 'effect', '结果；效果'),
       (403, 'end', '末端；结束'),
       (404, 'error', '错误'),
       (405, 'event', '事件'),
       (406, 'example', '例子'),
       (407, 'exchange', '交换'),
       (408, 'existence', '存在；生存'),
       (409, 'expansion', '扩展'),
       (410, 'experience', '经验'),
       (411, 'expert', '专家'),
       (412, 'fact', '事实'),
       (413, 'fall', '落下；秋天'),
       (414, 'family', '家'),
       (415, 'father', '父亲'),
       (416, 'fear', '害怕'),
       (417, 'feeling', '感觉'),
       (418, 'fiction', '小说'),
       (419, 'field', '田野；场'),
       (420, 'fight', '战斗；争吵'),
       (421, 'fire', '火'),
       (422, 'flame', '火焰'),
       (423, 'flight', '飞行'),
       (424, 'flower', '花'),
       (425, 'fold', '折叠'),
       (426, 'food', '食物'),
       (427, 'force', '力量'),
       (428, 'form', '形状'),
       (429, 'friend', '朋友'),
       (430, 'front', '前面'),
       (431, 'fruit', '水果'),
       (432, 'glass', '玻璃'),
       (433, 'gold', '金'),
       (434, 'government', '政府'),
       (435, 'grain', '谷粒'),
       (436, 'grass', '草'),
       (437, 'grip', '紧握'),
       (438, 'group', '群；团体'),
       (439, 'growth', '生长'),
       (440, 'guide', '向导；指南'),
       (441, 'harbor', '港湾'),
       (442, 'harmony', '和睦；和谐'),
       (443, 'hate', '仇恨'),
       (444, 'hearing', '听'),
       (445, 'heat', '热'),
       (446, 'help', '帮助'),
       (447, 'history', '历史'),
       (448, 'hole', '洞'),
       (449, 'hope', '希望'),
       (450, 'hour', '小时'),
       (451, 'humor', '幽默'),
       (452, 'ice', '冰'),
       (453, 'idea', '注意'),
       (454, 'impulse', '冲动'),
       (455, 'increase', '增加'),
       (456, 'industry', '工业'),
       (457, 'ink', '墨水'),
       (458, 'insect', '昆虫'),
       (459, 'instrument', '仪器'),
       (460, 'insurance', '保险'),
       (461, 'interest', '兴趣'),
       (462, 'invention', '发明'),
       (463, 'iron', '铁'),
       (464, 'jelly', '胶冻'),
       (465, 'join', '连接处'),
       (466, 'journey', '旅行'),
       (467, 'judge', '法官'),
       (468, 'jump', '跳'),
       (469, 'kick', '踢'),
       (470, 'kiss', '吻'),
       (471, 'knowledge', '知识'),
       (472, 'land', '陆地'),
       (473, 'language', '语言'),
       (474, 'laugh', '笑'),
       (475, 'law', '法律'),
       (476, 'lead', '铅'),
       (477, 'learn', '学习'),
       (478, 'leather', '皮革'),
       (479, 'letter', '字母；信；文字'),
       (480, 'level', '水平；高度'),
       (481, 'lift', '提'),
       (482, 'light', '光；灯'),
       (483, 'limit', '限度'),
       (484, 'linen', '亚麻'),
       (485, 'liquid', '液体'),
       (486, 'list', '表目录；名单'),
       (487, 'look', '看'),
       (488, 'loss', '丧失；损失'),
       (489, 'love', '爱'),
       (490, 'machine', '机器'),
       (491, 'man', '男人'),
       (492, 'manager', '经理'),
       (493, 'mark', '痕迹；记号'),
       (494, 'market', '市场'),
       (495, 'mass', '团块；堆'),
       (496, 'meal', '进餐'),
       (497, 'measure', '度量；措施'),
       (498, 'meat', '肉'),
       (499, 'meeting', '会议'),
       (500, 'memory', '记忆'),
       (501, 'metal', '金属'),
       (502, 'middle', '中部'),
       (503, 'milk', '牛奶'),
       (504, 'mind', '头脑'),
       (505, 'mine', '矿'),
       (506, 'minute', '分钟'),
       (507, 'mist', '薄雾'),
       (508, 'money', '钱'),
       (509, 'month', '月份'),
       (510, 'morning', '早晨'),
       (511, 'mother', '母亲'),
       (512, 'motion', '运动；动作'),
       (513, 'mountain', '山'),
       (514, 'move', '移动'),
       (515, 'music', '音乐'),
       (516, 'name', '名字'),
       (517, 'nation', '国民；国家'),
       (518, 'need', '需要'),
       (519, 'news', '新闻'),
       (520, 'night', '夜'),
       (521, 'noise', '响声；噪音'),
       (522, 'note', '笔记；便条；纸巾'),
       (523, 'number', '数字'),
       (524, 'observation', '观察'),
       (525, 'offer', '提供；出价'),
       (526, 'oil', '油'),
       (527, 'operation', '运转；操作'),
       (528, 'opinion', '意见'),
       (529, 'order', '顺序；命令；订货'),
       (530, 'organization', '组织'),
       (531, 'ornament', '装饰'),
       (532, 'owner', '物主'),
       (533, 'page', '页'),
       (534, 'pain', '痛'),
       (535, 'paint', '油漆'),
       (536, 'paper', '纸'),
       (537, 'part', '一部分'),
       (538, 'paste', '浆糊'),
       (539, 'payment', '支付'),
       (540, 'peace', '和平'),
       (541, 'person', '人'),
       (542, 'place', '地方'),
       (543, 'plant', '植物'),
       (544, 'play', '玩；戏剧'),
       (545, 'pleasure', '愉快'),
       (546, 'point', '尖端；一点'),
       (547, 'poison', '毒'),
       (548, 'polish', '磨光；擦亮'),
       (549, 'porter', '搬运工'),
       (550, 'position', '位置'),
       (551, 'powder', '粉'),
       (552, 'power', '权力；力量'),
       (553, 'price', '价格'),
       (554, 'print', '印刷'),
       (555, 'process', '过程；步骤'),
       (556, 'produce', '产品'),
       (557, 'profit', '利润'),
       (558, 'property', '财产；房地产'),
       (559, 'prose', '散文'),
       (560, 'protest', '抗议'),
       (561, 'pull', '拉'),
       (562, 'punishment', '惩罚'),
       (563, 'purpose', '目的'),
       (564, 'push', '推'),
       (565, 'quality', '质量；特性'),
       (566, 'question', '问题'),
       (567, 'rain', '雨'),
       (568, 'range', '排；行；范围'),
       (569, 'rate', '比率；速度'),
       (570, 'ray', '光线'),
       (571, 'reaction', '反应'),
       (572, 'reading', '阅读'),
       (573, 'reason', '理由'),
       (574, 'record', '记录；唱片'),
       (575, 'regret', '懊悔；遗憾'),
       (576, 'relation', '关系'),
       (577, 'religion', '宗教'),
       (578, 'representative', '代表'),
       (579, 'request', '要求；请求'),
       (580, 'respect', '尊敬'),
       (581, 'rest', '休息'),
       (582, 'reward', '报答；奖赏'),
       (583, 'rhythm', '节奏'),
       (584, 'rice', '米'),
       (585, 'river', '江；河'),
       (586, 'road', '路'),
       (587, 'roll', '滚动；（一）卷'),
       (588, 'room', '房间'),
       (589, 'rub', '摩擦'),
       (590, 'rule', '规则；统治'),
       (591, 'run', '跑'),
       (592, 'salt', '盐'),
       (593, 'sand', '沙'),
       (594, 'scale', '刻度；比例'),
       (595, 'science', '科学'),
       (596, 'sea', '海'),
       (597, 'seat', '座位'),
       (598, 'secretary', '秘书'),
       (599, 'selection', '选择'),
       (600, 'self', '自己'),
       (601, 'sense', '感觉'),
       (602, 'servant', '仆人'),
       (603, 'sex', '性'),
       (604, 'shade', '荫；阴暗'),
       (605, 'shake', '摇动'),
       (606, 'shame', '羞耻'),
       (607, 'shock', '冲击；震惊'),
       (608, 'side', '边；旁边'),
       (609, 'sign', '记号；标志；征兆'),
       (610, 'silk', '丝；丝绸'),
       (611, 'silver', '银'),
       (612, 'sister', '姐妹'),
       (613, 'size', '尺寸；大小'),
       (614, 'sky', '天空'),
       (615, 'sleep', '睡觉'),
       (616, 'slip', '滑动；下降；溜走；疏忽'),
       (617, 'slope', '倾斜'),
       (618, 'smash', '打碎；猛撞'),
       (619, 'smell', '气味'),
       (620, 'smile', '微笑'),
       (621, 'smoke', '烟'),
       (622, 'sneeze', '喷嚏'),
       (623, 'snow', '雪'),
       (624, 'soap', '肥皂'),
       (625, 'society', '社会'),
       (626, 'son', '儿子'),
       (627, 'song', '歌曲'),
       (628, 'sort', '种类'),
       (629, 'sound', '声音'),
       (630, 'soup', '汤'),
       (631, 'space', '空间'),
       (632, 'stage', '舞台'),
       (633, 'start', '开始'),
       (634, 'statement', '陈述；生明'),
       (635, 'steam', '蒸汽'),
       (636, 'steel', '钢'),
       (637, 'step', '脚步'),
       (638, 'stitch', '一针'),
       (639, 'stone', '石头'),
       (640, 'stop', '停止'),
       (641, 'story', '故事'),
       (642, 'stretch', '伸展'),
       (643, 'structure', '结构'),
       (644, 'substance', '物质'),
       (645, 'sugar', '糖'),
       (646, 'suggestion', '建议'),
       (647, 'summer', '夏天'),
       (648, 'support', '支撑；支持'),
       (649, 'surprise', '惊奇'),
       (650, 'swim', '游泳'),
       (651, 'system', '系统'),
       (652, 'talk', '谈话'),
       (653, 'taste', '味觉'),
       (654, 'tax', '税'),
       (655, 'teach', '教'),
       (656, 'tendency', '倾向；接触'),
       (657, 'test', '实验；测试'),
       (658, 'theory', '理论'),
       (659, 'thing', '物；东西'),
       (660, 'thought', '思想'),
       (661, 'thunder', '雷；雷声'),
       (662, 'time', '时间'),
       (663, 'tin', '锡'),
       (664, 'top', '顶部'),
       (665, 'touch', '触觉；接触'),
       (666, 'trade', '贸易'),
       (667, 'transport', '运输'),
       (668, 'trick', '诡计'),
       (669, 'trouble', '烦恼；麻烦'),
       (670, 'turn', '转动'),
       (671, 'twist', '扭转'),
       (672, 'unit', '单位'),
       (673, 'use', '使用'),
       (674, 'value', '价值'),
       (675, 'verse', '诗'),
       (676, 'vessel', '容器；船'),
       (677, 'view', '观看；景色；观点'),
       (678, 'voice', '（人的）声音'),
       (679, 'walk', '走；散步'),
       (680, 'war', '战争'),
       (681, 'wash', '洗'),
       (682, 'waste', '浪费'),
       (683, 'water', '水'),
       (684, 'wave', '波；波浪'),
       (685, 'wax', '蜡'),
       (686, 'way', '路；方向；方法'),
       (687, 'weather', '天气'),
       (688, 'week', '星期'),
       (689, 'weight', '质量'),
       (690, 'wind', '风'),
       (691, 'wine', '酒'),
       (692, 'winter', '冬天'),
       (693, 'woman', '女人'),
       (694, 'wood', '木头'),
       (695, 'wool', '羊毛'),
       (696, 'word', '词；一句话'),
       (697, 'work', '工作'),
       (698, 'wound', '伤'),
       (699, 'writing', '写'),
       (700, 'year', '年。'),
       (701, 'able', '有能力的'),
       (702, 'acid', '酸的'),
       (703, 'angry', '发怒的'),
       (704, 'automatic', '自动的'),
       (705, 'beautiful', '美丽的'),
       (706, 'black', '黑色'),
       (707, 'boiling', '沸腾的'),
       (708, 'bright', '明亮的'),
       (709, 'broken', '损坏的'),
       (710, 'brown', '褐色的'),
       (711, 'cheap', '便宜的'),
       (712, 'chemical', '化学的'),
       (713, 'chief', '首要的'),
       (714, 'clean', '干净的'),
       (715, 'clear', '清澈的'),
       (716, 'common', '普通的'),
       (717, 'complex', '复杂的'),
       (718, 'conscious', '有意识的'),
       (719, 'cut', '切过的'),
       (720, 'deep', '深的'),
       (721, 'dependent', '依靠的'),
       (722, 'early', '早的'),
       (723, 'elastic', '有弹性的'),
       (724, 'electric', '电的'),
       (725, 'equal', '相等的'),
       (726, 'fat', '肥的'),
       (727, 'fertile', '多产的'),
       (728, 'first', '第一的'),
       (729, 'fixed', '固定的'),
       (730, 'flat', '平的'),
       (731, 'free', '自由的'),
       (732, 'frequent', '经常的'),
       (733, 'full', '满的'),
       (734, 'general', '一般的'),
       (735, 'good', '好的'),
       (736, 'great', '大的'),
       (737, 'grey', '灰色的'),
       (738, 'hanging', '悬挂的'),
       (739, 'happy', '高兴的'),
       (740, 'hard', '硬的；困难的'),
       (741, 'healthy', '健康的'),
       (742, 'high', '高的'),
       (743, 'hollow', '空的'),
       (744, 'important', '重要的'),
       (745, 'kind', '亲切的'),
       (746, 'like', '相似的'),
       (747, 'living', '活的'),
       (748, 'long', '长的'),
       (749, 'male', '男性的'),
       (750, 'married', '已婚的'),
       (751, 'material', '物质的'),
       (752, 'medical', '医学的'),
       (753, 'military', '军事的'),
       (754, 'natural', '自然的'),
       (755, 'necessary', '必要的'),
       (756, 'new', '新的'),
       (757, 'normal', '正常的'),
       (758, 'open', '打开的'),
       (759, 'parallel', '平行的'),
       (760, 'past', '过去的'),
       (761, 'physical', '身体的；物质的'),
       (762, 'political', '政治的'),
       (763, 'poor', '贫穷的'),
       (764, 'possible', '可能的'),
       (765, 'present', '在场的；现在的'),
       (766, 'private', '个人的'),
       (767, 'probable', '很可能的'),
       (768, 'quick', '快的'),
       (769, 'quiet', '安静的'),
       (770, 'ready', '准备好的'),
       (771, 'red', '红色的'),
       (772, 'regular', '规则的'),
       (773, 'responsible', '负责任的'),
       (774, 'right', '右的；对的'),
       (775, 'round', '圆的'),
       (776, 'same', '同样的'),
       (777, 'second', '第二的'),
       (778, 'separate', '分开的'),
       (779, 'serious', '严重的；严肃的'),
       (780, 'sharp', '锋利的'),
       (781, 'smooth', '平滑的'),
       (782, 'sticky', '粘的'),
       (783, 'stiff', '硬的'),
       (784, 'straight', '直的'),
       (785, 'strong', '强的'),
       (786, 'sudden', '突然的'),
       (787, 'sweet', '甜的'),
       (788, 'tall', '身材高的'),
       (789, 'thick', '厚的'),
       (790, 'tight', '紧的'),
       (791, 'tired', '疲倦的'),
       (792, 'TRUE', '真实的'),
       (793, 'violent', '激烈的；暴力的'),
       (794, 'waiting', '等待的'),
       (795, 'warm', '温暖的'),
       (796, 'wet', '湿的'),
       (797, 'wide', '宽阔的'),
       (798, 'wise', '聪明的'),
       (799, 'yellow', '黄色的'),
       (800, 'young', '年青的'),
       (801, 'awake', '清醒的'),
       (802, 'bad', '坏的'),
       (803, 'bent', '弯曲的'),
       (804, 'bitter', '苦的'),
       (805, 'blue', '蓝色的'),
       (806, 'certain', '肯定的'),
       (807, 'cold', '冷的'),
       (808, 'complete', '完整的'),
       (809, 'cruel', '残酷的'),
       (810, 'dark', '黑暗的'),
       (811, 'dead', '死的'),
       (812, 'expensive', '昂贵的'),
       (813, 'dear', '亲爱的'),
       (814, 'delicate', '娇贵的；微妙的'),
       (815, 'different', '不同的'),
       (816, 'dirty', '脏的'),
       (817, 'dry', '干的'),
       (818, 'FALSE', '假的'),
       (819, 'female', '女性的'),
       (820, 'foolish', '愚蠢的'),
       (821, 'future', '未来的'),
       (822, 'green', '绿的'),
       (823, 'ill', '生病的'),
       (824, 'last', '最后的'),
       (825, 'late', '晚的'),
       (826, 'left', '左的'),
       (827, 'loose', '松的'),
       (828, 'loud', '大声的'),
       (829, 'low', '低的'),
       (830, 'mixed', '混合的'),
       (831, 'narrow', '窄的'),
       (832, 'old', '老的'),
       (833, 'opposite', '相反的'),
       (834, 'public', '公众的'),
       (835, 'rough', '粗糙的；狂暴的'),
       (836, 'sad', '悲哀的'),
       (837, 'safe', '安全的'),
       (838, 'secret', '秘密的'),
       (839, 'short', '短的'),
       (840, 'shut', '关闭的'),
       (841, 'simple', '简单的'),
       (842, 'slow', '慢的'),
       (843, 'small', '小的'),
       (844, 'soft', '柔软的'),
       (845, 'solid', '固定的；结实的；纯的'),
       (846, 'special', '特殊的'),
       (847, 'strange', '奇怪的'),
       (848, 'thin', '薄的'),
       (849, 'white', '白色的'),
       (850, 'wrong', '错误的');
/*!40000 ALTER TABLE `t_root_word` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Dumping routines for database 'meta_tool'
--
SET
@@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-26 15:04:04
