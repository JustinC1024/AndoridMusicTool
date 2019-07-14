/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : music_app

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2019-06-28 10:40:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for singer
-- ----------------------------
DROP TABLE IF EXISTS `singer`;
CREATE TABLE `singer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of singer
-- ----------------------------
INSERT INTO `singer` VALUES ('1', '古天乐、谢安琪', '957');
INSERT INTO `singer` VALUES ('2', '洪卓立', '1139');
INSERT INTO `singer` VALUES ('3', 'Ashes Remain', '173402');
INSERT INTO `singer` VALUES ('4', '讲者、Dewen', '83882');
INSERT INTO `singer` VALUES ('5', '杨千嬅', '6535');
INSERT INTO `singer` VALUES ('6', '暴走音频组', '580031');
INSERT INTO `singer` VALUES ('7', '陈奕迅', '420');
INSERT INTO `singer` VALUES ('8', 'rubberband', '8541');
INSERT INTO `singer` VALUES ('9', '家入レオ', '84246');
INSERT INTO `singer` VALUES ('10', 'Che\'Nelle', '84543');
INSERT INTO `singer` VALUES ('11', 'aiko', '11143');
INSERT INTO `singer` VALUES ('12', 'LSD、Sia、Diplo、Labrinth', '61886');
INSERT INTO `singer` VALUES ('13', '宇多田ヒカル', '12901');
INSERT INTO `singer` VALUES ('14', '麦浚龙', '2147');
INSERT INTO `singer` VALUES ('15', 'Paula DeAnda', '98521');
INSERT INTO `singer` VALUES ('16', '古巨基', '951');
INSERT INTO `singer` VALUES ('17', '梁静茹', '5085');
INSERT INTO `singer` VALUES ('18', '关楚耀', '971');
INSERT INTO `singer` VALUES ('19', '侧田', '427');
INSERT INTO `singer` VALUES ('20', '小肥', '3072');
INSERT INTO `singer` VALUES ('21', '林峯、钟嘉欣', '1593');
INSERT INTO `singer` VALUES ('22', '黄旭', '209245');
INSERT INTO `singer` VALUES ('23', 'VaVa', '177441');
INSERT INTO `singer` VALUES ('24', 'GAI', '718960');
INSERT INTO `singer` VALUES ('25', 'Tizzy T', '201804');

-- ----------------------------
-- Table structure for song
-- ----------------------------
DROP TABLE IF EXISTS `song`;
CREATE TABLE `song` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sname` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of song
-- ----------------------------
INSERT INTO `song` VALUES ('1', '201906280138/71d66f2db14939bf84739b149b845581/G151/M07/05/04/1w0DAF0LaBWAXrylADU-DpuL79o252', '释然', '20190620/20190620185203921422');
INSERT INTO `song` VALUES ('2', '201906280135/a4119dddc653ac0241db0adf527354c0/G151/M01/01/15/d5QEAF0LPmmAWbXSAD2ng4uQaps451', '枕边书', '20190620/20190620155105690971');
INSERT INTO `song` VALUES ('3', '201906280139/2a23e7d3caa3f530e78e9e74570e8579/G163/M02/14/1B/Q4cBAF0QmbOALCCfAD12O8qRj-k464', '傻得可以', '20190624/20190624155503165029');
INSERT INTO `song` VALUES ('4', '201906280141/738c609df8c95fe702846756b3d10807/G162/M06/08/09/gpQEAF0QlK2AK0Q5ADUYM8Z_Ynk565', '爱恨两难', '20190624/20190624171103733229');
INSERT INTO `song` VALUES ('5', '201906280140/bc5fffcfdb6c0b951b2840d9f5857113/G168/M05/05/01/iJQEAF0QhCKABz5XAC3gMzI8AFc243', '悲伤童话', '20190624/20190624164134881830');
INSERT INTO `song` VALUES ('6', '201906280137/c76323827a972f7748f37f4fcd7900b3/G166/M02/11/07/5g0DAF0QtjOADuhWAESuZTmmuDc504', '判若两人', '20190624/20190624190605500310');
INSERT INTO `song` VALUES ('13', '201906280909/68337320158428afa2c205cc7ff5e8a5/G154/M0B/0A/01/2g0DAF0KCL-ARWq7ADtDRCDwZxc049', '几分之几', '');
