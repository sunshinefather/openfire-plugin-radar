/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50616
Source Host           : localhost:3306
Source Database       : openfire

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2017-03-17 16:58:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ofdevicetoken
-- ----------------------------
DROP TABLE IF EXISTS `ofdevicetoken`;
CREATE TABLE `ofdevicetoken` (
  `userName` varchar(64) NOT NULL,
  `iosToken` varchar(64) DEFAULT NULL,
  `appName` varchar(64) DEFAULT NULL,
  `userType` varchar(64) DEFAULT NULL,
  `version` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`userName`),
  KEY `index_ios_token` (`iosToken`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of ofdevicetoken
-- ----------------------------

-- ----------------------------
-- Table structure for ofextcomponentconf
-- ----------------------------
DROP TABLE IF EXISTS `ofextcomponentconf`;
CREATE TABLE `ofextcomponentconf` (
  `subdomain` varchar(255) NOT NULL,
  `wildcard` tinyint(4) NOT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `permission` varchar(10) NOT NULL,
  PRIMARY KEY (`subdomain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofextcomponentconf
-- ----------------------------

-- ----------------------------
-- Table structure for ofgroup
-- ----------------------------
DROP TABLE IF EXISTS `ofgroup`;
CREATE TABLE `ofgroup` (
  `groupName` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`groupName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofgroup
-- ----------------------------

-- ----------------------------
-- Table structure for ofgroupprop
-- ----------------------------
DROP TABLE IF EXISTS `ofgroupprop`;
CREATE TABLE `ofgroupprop` (
  `groupName` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `propValue` text NOT NULL,
  PRIMARY KEY (`groupName`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofgroupprop
-- ----------------------------

-- ----------------------------
-- Table structure for ofgroupuser
-- ----------------------------
DROP TABLE IF EXISTS `ofgroupuser`;
CREATE TABLE `ofgroupuser` (
  `groupName` varchar(50) NOT NULL,
  `username` varchar(100) NOT NULL,
  `administrator` tinyint(4) NOT NULL,
  PRIMARY KEY (`groupName`,`username`,`administrator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofgroupuser
-- ----------------------------

-- ----------------------------
-- Table structure for ofid
-- ----------------------------
DROP TABLE IF EXISTS `ofid`;
CREATE TABLE `ofid` (
  `idType` int(11) NOT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`idType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofid
-- ----------------------------
INSERT INTO `ofid` VALUES ('18', '1');
INSERT INTO `ofid` VALUES ('19', '5837161');
INSERT INTO `ofid` VALUES ('23', '1');
INSERT INTO `ofid` VALUES ('25', '192');
INSERT INTO `ofid` VALUES ('26', '2');

-- ----------------------------
-- Table structure for ofmucaffiliation
-- ----------------------------
DROP TABLE IF EXISTS `ofmucaffiliation`;
CREATE TABLE `ofmucaffiliation` (
  `roomID` bigint(20) NOT NULL,
  `jid` text NOT NULL,
  `affiliation` tinyint(4) NOT NULL,
  PRIMARY KEY (`roomID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofmucaffiliation
-- ----------------------------

-- ----------------------------
-- Table structure for ofmucconversationlog
-- ----------------------------
DROP TABLE IF EXISTS `ofmucconversationlog`;
CREATE TABLE `ofmucconversationlog` (
  `roomID` bigint(20) NOT NULL,
  `sender` text NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `logTime` char(15) NOT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `body` text,
  KEY `ofMucConversationLog_time_idx` (`logTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofmucconversationlog
-- ----------------------------

-- ----------------------------
-- Table structure for ofmucmember
-- ----------------------------
DROP TABLE IF EXISTS `ofmucmember`;
CREATE TABLE `ofmucmember` (
  `roomID` bigint(20) NOT NULL,
  `jid` text NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `firstName` varchar(100) DEFAULT NULL,
  `lastName` varchar(100) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `faqentry` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`roomID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofmucmember
-- ----------------------------

-- ----------------------------
-- Table structure for ofmucroom
-- ----------------------------
DROP TABLE IF EXISTS `ofmucroom`;
CREATE TABLE `ofmucroom` (
  `serviceID` bigint(20) NOT NULL,
  `roomID` bigint(20) NOT NULL,
  `creationDate` char(15) NOT NULL,
  `modificationDate` char(15) NOT NULL,
  `name` varchar(50) NOT NULL,
  `naturalName` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `lockedDate` char(15) NOT NULL,
  `emptyDate` char(15) DEFAULT NULL,
  `canChangeSubject` tinyint(4) NOT NULL,
  `maxUsers` int(11) NOT NULL,
  `publicRoom` tinyint(4) NOT NULL,
  `moderated` tinyint(4) NOT NULL,
  `membersOnly` tinyint(4) NOT NULL,
  `canInvite` tinyint(4) NOT NULL,
  `roomPassword` varchar(50) DEFAULT NULL,
  `canDiscoverJID` tinyint(4) NOT NULL,
  `logEnabled` tinyint(4) NOT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `rolesToBroadcast` tinyint(4) NOT NULL,
  `useReservedNick` tinyint(4) NOT NULL,
  `canChangeNick` tinyint(4) NOT NULL,
  `canRegister` tinyint(4) NOT NULL,
  PRIMARY KEY (`serviceID`,`name`),
  KEY `ofMucRoom_roomid_idx` (`roomID`) USING BTREE,
  KEY `ofMucRoom_serviceid_idx` (`serviceID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofmucroom
-- ----------------------------

-- ----------------------------
-- Table structure for ofmucroomprop
-- ----------------------------
DROP TABLE IF EXISTS `ofmucroomprop`;
CREATE TABLE `ofmucroomprop` (
  `roomID` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `propValue` text NOT NULL,
  PRIMARY KEY (`roomID`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofmucroomprop
-- ----------------------------

-- ----------------------------
-- Table structure for ofmucservice
-- ----------------------------
DROP TABLE IF EXISTS `ofmucservice`;
CREATE TABLE `ofmucservice` (
  `serviceID` bigint(20) NOT NULL,
  `subdomain` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `isHidden` tinyint(4) NOT NULL,
  PRIMARY KEY (`subdomain`),
  KEY `ofMucService_serviceid_idx` (`serviceID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofmucservice
-- ----------------------------
INSERT INTO `ofmucservice` VALUES ('1', 'conference', null, '0');

-- ----------------------------
-- Table structure for ofmucserviceprop
-- ----------------------------
DROP TABLE IF EXISTS `ofmucserviceprop`;
CREATE TABLE `ofmucserviceprop` (
  `serviceID` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `propValue` text NOT NULL,
  PRIMARY KEY (`serviceID`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofmucserviceprop
-- ----------------------------

-- ----------------------------
-- Table structure for ofoffline
-- ----------------------------
DROP TABLE IF EXISTS `ofoffline`;
CREATE TABLE `ofoffline` (
  `username` varchar(64) CHARACTER SET utf8 NOT NULL,
  `messageID` bigint(20) NOT NULL,
  `creationDate` char(15) CHARACTER SET utf8 NOT NULL,
  `messageSize` int(11) NOT NULL,
  `stanza` text NOT NULL,
  PRIMARY KEY (`username`,`messageID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of ofoffline
-- ----------------------------

-- ----------------------------
-- Table structure for ofpresence
-- ----------------------------
DROP TABLE IF EXISTS `ofpresence`;
CREATE TABLE `ofpresence` (
  `username` varchar(64) NOT NULL,
  `offlinePresence` text,
  `offlineDate` char(15) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofpresence
-- ----------------------------
INSERT INTO `ofpresence` VALUES ('034', null, '001486630035515');
INSERT INTO `ofpresence` VALUES ('10000000003', null, '001486631107424');
INSERT INTO `ofpresence` VALUES ('10000000056', null, '001489558086031');
INSERT INTO `ofpresence` VALUES ('10000000058', null, '001484630383054');
INSERT INTO `ofpresence` VALUES ('10000000059', null, '001484563831981');
INSERT INTO `ofpresence` VALUES ('10000000060', null, '001489654588820');
INSERT INTO `ofpresence` VALUES ('10000000061', null, '001489655815114');
INSERT INTO `ofpresence` VALUES ('10000000062', null, '001488441042209');
INSERT INTO `ofpresence` VALUES ('10000000063', null, '001488444566330');
INSERT INTO `ofpresence` VALUES ('10000000064', null, '001489644330132');
INSERT INTO `ofpresence` VALUES ('10000000065', null, '001489634171913');
INSERT INTO `ofpresence` VALUES ('10000000066', null, '001489737131784');
INSERT INTO `ofpresence` VALUES ('10000000067', null, '001489717661733');
INSERT INTO `ofpresence` VALUES ('10000000068', null, '001489463927936');
INSERT INTO `ofpresence` VALUES ('10000000069', null, '001487842375650');
INSERT INTO `ofpresence` VALUES ('10000000070', null, '001489459905872');
INSERT INTO `ofpresence` VALUES ('10000000071', null, '001488506813637');
INSERT INTO `ofpresence` VALUES ('10000000072', null, '001488351818170');
INSERT INTO `ofpresence` VALUES ('10000000073', null, '001488432124161');
INSERT INTO `ofpresence` VALUES ('10000000074', null, '001488507107818');
INSERT INTO `ofpresence` VALUES ('10000000075', null, '001488507473866');
INSERT INTO `ofpresence` VALUES ('10000000078', null, '001488513602626');
INSERT INTO `ofpresence` VALUES ('10000000079', null, '001488513602626');
INSERT INTO `ofpresence` VALUES ('10000000080', null, '001488182856289');
INSERT INTO `ofpresence` VALUES ('10000000081', null, '001489716962885');
INSERT INTO `ofpresence` VALUES ('10000000082', null, '001489737465970');
INSERT INTO `ofpresence` VALUES ('10000000084', null, '001489730458327');
INSERT INTO `ofpresence` VALUES ('10000000086', null, '001489722964347');
INSERT INTO `ofpresence` VALUES ('10000000087', null, '001486348561440');
INSERT INTO `ofpresence` VALUES ('10000000090', null, '001489556361892');
INSERT INTO `ofpresence` VALUES ('10000000091', null, '001489736659579');
INSERT INTO `ofpresence` VALUES ('10000000092', null, '001489460950272');
INSERT INTO `ofpresence` VALUES ('10000000093', null, '001489454927189');
INSERT INTO `ofpresence` VALUES ('10000000094', null, '001489455350021');
INSERT INTO `ofpresence` VALUES ('10000000095', null, '001487928778120');
INSERT INTO `ofpresence` VALUES ('10000000096', null, '001489737243487');
INSERT INTO `ofpresence` VALUES ('10000000097', null, '001489736468000');
INSERT INTO `ofpresence` VALUES ('10000000099', null, '001489641022274');
INSERT INTO `ofpresence` VALUES ('10000000101', null, '001481687007556');
INSERT INTO `ofpresence` VALUES ('11000000001', null, '001487302138012');
INSERT INTO `ofpresence` VALUES ('11000000002', null, '001481188595718');
INSERT INTO `ofpresence` VALUES ('13000000001', null, '001489550133638');
INSERT INTO `ofpresence` VALUES ('13000000002', null, '001485056415099');
INSERT INTO `ofpresence` VALUES ('13000000003', null, '001485075810186');
INSERT INTO `ofpresence` VALUES ('13000000004', null, '001489457618168');
INSERT INTO `ofpresence` VALUES ('13000000005', null, '001485057661583');
INSERT INTO `ofpresence` VALUES ('13000000006', null, '001489656069141');
INSERT INTO `ofpresence` VALUES ('13000000007', null, '001484978854322');
INSERT INTO `ofpresence` VALUES ('13000000008', null, '001489718598706');
INSERT INTO `ofpresence` VALUES ('13000000009', null, '001489456229007');
INSERT INTO `ofpresence` VALUES ('13000000010', null, '001485072892964');
INSERT INTO `ofpresence` VALUES ('13000000011', null, '001485069229266');
INSERT INTO `ofpresence` VALUES ('13000000012', null, '001485069146402');
INSERT INTO `ofpresence` VALUES ('13000000013', null, '001487912854301');
INSERT INTO `ofpresence` VALUES ('13000000014', null, '001485062787623');
INSERT INTO `ofpresence` VALUES ('13000000015', null, '001485069008773');
INSERT INTO `ofpresence` VALUES ('13000000016', null, '001485068808692');
INSERT INTO `ofpresence` VALUES ('13000000017', null, '001485067841517');
INSERT INTO `ofpresence` VALUES ('13000000018', null, '001485071243051');
INSERT INTO `ofpresence` VALUES ('13000000019', null, '001485067757498');
INSERT INTO `ofpresence` VALUES ('13000000020', null, '001485067736366');
INSERT INTO `ofpresence` VALUES ('13060091980', null, '001489458001167');
INSERT INTO `ofpresence` VALUES ('13683450668', null, '001480666686935');
INSERT INTO `ofpresence` VALUES ('13699013722', null, '001489735776126');
INSERT INTO `ofpresence` VALUES ('13699479091', null, '001480656810804');
INSERT INTO `ofpresence` VALUES ('13880132785', null, '001486346260247');
INSERT INTO `ofpresence` VALUES ('13908029872', null, '001481539319021');
INSERT INTO `ofpresence` VALUES ('13908029873', null, '001486364885692');
INSERT INTO `ofpresence` VALUES ('15000000001', null, '001481782174560');
INSERT INTO `ofpresence` VALUES ('15000000002', null, '001483673139806');
INSERT INTO `ofpresence` VALUES ('15000000003', null, '001483671039317');
INSERT INTO `ofpresence` VALUES ('15000000004', null, '001481253328057');
INSERT INTO `ofpresence` VALUES ('15000000005', null, '001481253301179');
INSERT INTO `ofpresence` VALUES ('15000000006', null, '001481253215619');
INSERT INTO `ofpresence` VALUES ('15000000007', null, '001481256107371');
INSERT INTO `ofpresence` VALUES ('15002823575', null, '001488332665123');
INSERT INTO `ofpresence` VALUES ('15609083370', null, '001480564014665');
INSERT INTO `ofpresence` VALUES ('15828107384', null, '001488167090841');
INSERT INTO `ofpresence` VALUES ('17713592594', null, '001489558086031');
INSERT INTO `ofpresence` VALUES ('18180609216', null, '001484123503157');
INSERT INTO `ofpresence` VALUES ('18200241089', null, '001489736244674');
INSERT INTO `ofpresence` VALUES ('18200385195', null, '001486710867404');
INSERT INTO `ofpresence` VALUES ('18200390603', null, '001486605499686');
INSERT INTO `ofpresence` VALUES ('18583971188', null, '001481614312309');
INSERT INTO `ofpresence` VALUES ('18613223863', null, '001489640791415');
INSERT INTO `ofpresence` VALUES ('61bc576e13f5e4182a113512958cf2d7', null, '001486350956162');
INSERT INTO `ofpresence` VALUES ('767782dd66e34d84163390d02ed5607b', null, '001487923093762');
INSERT INTO `ofpresence` VALUES ('8b78b15def731b366c25fd1fc56b2962', null, '001489737738480');
INSERT INTO `ofpresence` VALUES ('a5e23b6fecffc3986ec8552f0a10024f', null, '001488784902310');
INSERT INTO `ofpresence` VALUES ('appcon', null, '001484115849732');
INSERT INTO `ofpresence` VALUES ('b9be2cfef74ad0308afb747eec86525d', null, '001487748029418');
INSERT INTO `ofpresence` VALUES ('c2eec55a027272a13ee94c0fc9438d51', null, '001487756491444');
INSERT INTO `ofpresence` VALUES ('gszx', null, '001482902387089');
INSERT INTO `ofpresence` VALUES ('lqyqfybjy', null, '001487560496580');
INSERT INTO `ofpresence` VALUES ('oeg3pw6neurrembw4m352z4c4cja', null, '001486351191810');
INSERT INTO `ofpresence` VALUES ('oeg3pw7x66dgznzw8w10_pjlsyda', null, '001487923136173');
INSERT INTO `ofpresence` VALUES ('otyvlwaky10cntxyxymny53q4xj0', null, '001483077447356');
INSERT INTO `ofpresence` VALUES ('otyvlwspelljlfkqgxooe9jnm8xm', null, '001483069257314');
INSERT INTO `ofpresence` VALUES ('scsfybjy', null, '001487567061550');
INSERT INTO `ofpresence` VALUES ('zhangsan', null, '001482459635744');
INSERT INTO `ofpresence` VALUES ('zhangsan1', null, '001482206154427');

-- ----------------------------
-- Table structure for ofprivacylist
-- ----------------------------
DROP TABLE IF EXISTS `ofprivacylist`;
CREATE TABLE `ofprivacylist` (
  `username` varchar(64) NOT NULL,
  `name` varchar(100) NOT NULL,
  `isDefault` tinyint(4) NOT NULL,
  `list` text NOT NULL,
  PRIMARY KEY (`username`,`name`),
  KEY `ofPrivacyList_default_idx` (`username`,`isDefault`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofprivacylist
-- ----------------------------

-- ----------------------------
-- Table structure for ofprivate
-- ----------------------------
DROP TABLE IF EXISTS `ofprivate`;
CREATE TABLE `ofprivate` (
  `username` varchar(64) NOT NULL,
  `name` varchar(100) NOT NULL,
  `namespace` varchar(200) NOT NULL,
  `privateData` text NOT NULL,
  PRIMARY KEY (`username`,`name`,`namespace`(100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofprivate
-- ----------------------------

-- ----------------------------
-- Table structure for ofproperty
-- ----------------------------
DROP TABLE IF EXISTS `ofproperty`;
CREATE TABLE `ofproperty` (
  `name` varchar(100) NOT NULL,
  `propValue` text NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofproperty
-- ----------------------------
INSERT INTO `ofproperty` VALUES ('adminConsole.port', '9090');
INSERT INTO `ofproperty` VALUES ('adminConsole.securePort', '9091');
INSERT INTO `ofproperty` VALUES ('connectionProvider.className', 'org.jivesoftware.database.DefaultConnectionProvider');
INSERT INTO `ofproperty` VALUES ('database.defaultProvider.connectionTimeout', '1.0');
INSERT INTO `ofproperty` VALUES ('database.defaultProvider.driver', 'com.mysql.jdbc.Driver');
INSERT INTO `ofproperty` VALUES ('database.defaultProvider.maxConnections', '600');
INSERT INTO `ofproperty` VALUES ('database.defaultProvider.minConnections', '10');
INSERT INTO `ofproperty` VALUES ('database.defaultProvider.password', '6196321b8ba11632eb0139d1253303fde86aef3befbd29ad625abd1cf5f026c4');
INSERT INTO `ofproperty` VALUES ('database.defaultProvider.serverURL', 'jdbc:mysql://127.0.0.1:3306/openfire?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf8');
INSERT INTO `ofproperty` VALUES ('database.defaultProvider.testAfterUse', 'false');
INSERT INTO `ofproperty` VALUES ('database.defaultProvider.testBeforeUse', 'false');
INSERT INTO `ofproperty` VALUES ('database.defaultProvider.testSQL', 'select 1');
INSERT INTO `ofproperty` VALUES ('database.defaultProvider.username', '479878fa3336f6ed3dccc96c15707acb4b9e1c2b870549f6');
INSERT INTO `ofproperty` VALUES ('httpbind.CORS.domains', '*');
INSERT INTO `ofproperty` VALUES ('httpbind.CORS.enabled', 'true');
INSERT INTO `ofproperty` VALUES ('httpbind.enabled', 'true');
INSERT INTO `ofproperty` VALUES ('httpbind.forwarded.enabled', 'false');
INSERT INTO `ofproperty` VALUES ('jdbcAuthProvider.passwordSQL', 'SELECT password FROM sys_user WHERE user_name=?');
INSERT INTO `ofproperty` VALUES ('jdbcAuthProvider.passwordType', 'PLAIN');
INSERT INTO `ofproperty` VALUES ('jdbcProvider.connectionString', 'jdbc:mysql://192.168.1.240:3306/test_mom_user_platform?user=root&password=1qa2ws3ed&rewriteBatchedStatements=true&userunicode=true&characterencoding=utf8');
INSERT INTO `ofproperty` VALUES ('jdbcProvider.driver', 'com.mysql.jdbc.Driver');
INSERT INTO `ofproperty` VALUES ('jdbcUserProvider.allUsersSQL', 'select user_name from sys_user');
INSERT INTO `ofproperty` VALUES ('jdbcUserProvider.emailField', 'email');
INSERT INTO `ofproperty` VALUES ('jdbcUserProvider.loadUserSQL', 'SELECT user_name,type FROM sys_user WHERE user_name=?');
INSERT INTO `ofproperty` VALUES ('jdbcUserProvider.nameField', 'user_name');
INSERT INTO `ofproperty` VALUES ('jdbcUserProvider.searchSQL', 'SELECT user_name FROM sys_user a left join sys_ext_user b on a.id=b.id WHERE ');
INSERT INTO `ofproperty` VALUES ('jdbcUserProvider.userCountSQL', 'select count(0) from sys_user');
INSERT INTO `ofproperty` VALUES ('jdbcUserProvider.usernameField', 'user_name');
INSERT INTO `ofproperty` VALUES ('locale', 'zh_CN');
INSERT INTO `ofproperty` VALUES ('log.debug.enabled', 'true');
INSERT INTO `ofproperty` VALUES ('passwordKey', 'Yk18klG38l8IxPl');
INSERT INTO `ofproperty` VALUES ('provider.admin.className', 'org.jivesoftware.openfire.admin.DefaultAdminProvider');
INSERT INTO `ofproperty` VALUES ('provider.auth.className', 'org.jivesoftware.openfire.auth.JDBCAuthProvider');
INSERT INTO `ofproperty` VALUES ('provider.group.className', 'org.jivesoftware.openfire.group.DefaultGroupProvider');
INSERT INTO `ofproperty` VALUES ('provider.lockout.className', 'org.jivesoftware.openfire.lockout.DefaultLockOutProvider');
INSERT INTO `ofproperty` VALUES ('provider.securityAudit.className', 'org.jivesoftware.openfire.security.DefaultSecurityAuditProvider');
INSERT INTO `ofproperty` VALUES ('provider.user.className', 'org.jivesoftware.openfire.user.JDBCUserProvider');
INSERT INTO `ofproperty` VALUES ('provider.vcard.className', 'org.jivesoftware.openfire.vcard.DefaultVCardProvider');
INSERT INTO `ofproperty` VALUES ('rayo.publicip', '127.0.0.1');
INSERT INTO `ofproperty` VALUES ('sasl.mechs', 'ANONYMOUS,PLAIN,DIGEST-MD5,CRAM-MD5,JIVE-SHAREDSECRET');
INSERT INTO `ofproperty` VALUES ('sasl.scram-sha-1.iteration-count', '4096');
INSERT INTO `ofproperty` VALUES ('setup', 'true');
INSERT INTO `ofproperty` VALUES ('stream.management.active', 'true');
INSERT INTO `ofproperty` VALUES ('stream.management.requestFrequency', '5');
INSERT INTO `ofproperty` VALUES ('update.lastCheck', '1489627084308');
INSERT INTO `ofproperty` VALUES ('xmpp.auth.anonymous', 'true');
INSERT INTO `ofproperty` VALUES ('xmpp.domain', '127.0.0.1');
INSERT INTO `ofproperty` VALUES ('xmpp.httpbind.client.requests.polling', '0');
INSERT INTO `ofproperty` VALUES ('xmpp.httpbind.client.requests.wait', '10');
INSERT INTO `ofproperty` VALUES ('xmpp.httpbind.scriptSyntax.enabled', 'true');
INSERT INTO `ofproperty` VALUES ('xmpp.session.conflict-limit', '0');
INSERT INTO `ofproperty` VALUES ('xmpp.socket.ssl.active', 'true');

-- ----------------------------
-- Table structure for ofproperty_3.10.3
-- ----------------------------
DROP TABLE IF EXISTS `ofproperty_3.10.3`;
CREATE TABLE `ofproperty_3.10.3` (
  `name` varchar(100) NOT NULL,
  `propValue` text NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofproperty_3.10.3
-- ----------------------------

-- ----------------------------
-- Table structure for ofpubsubaffiliation
-- ----------------------------
DROP TABLE IF EXISTS `ofpubsubaffiliation`;
CREATE TABLE `ofpubsubaffiliation` (
  `serviceID` varchar(100) NOT NULL,
  `nodeID` varchar(100) NOT NULL,
  `jid` varchar(255) NOT NULL,
  `affiliation` varchar(10) NOT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofpubsubaffiliation
-- ----------------------------
INSERT INTO `ofpubsubaffiliation` VALUES ('pubsub', '', '127.0.0.1', 'owner');

-- ----------------------------
-- Table structure for ofpubsubdefaultconf
-- ----------------------------
DROP TABLE IF EXISTS `ofpubsubdefaultconf`;
CREATE TABLE `ofpubsubdefaultconf` (
  `serviceID` varchar(100) NOT NULL,
  `leaf` tinyint(4) NOT NULL,
  `deliverPayloads` tinyint(4) NOT NULL,
  `maxPayloadSize` int(11) NOT NULL,
  `persistItems` tinyint(4) NOT NULL,
  `maxItems` int(11) NOT NULL,
  `notifyConfigChanges` tinyint(4) NOT NULL,
  `notifyDelete` tinyint(4) NOT NULL,
  `notifyRetract` tinyint(4) NOT NULL,
  `presenceBased` tinyint(4) NOT NULL,
  `sendItemSubscribe` tinyint(4) NOT NULL,
  `publisherModel` varchar(15) NOT NULL,
  `subscriptionEnabled` tinyint(4) NOT NULL,
  `accessModel` varchar(10) NOT NULL,
  `language` varchar(255) DEFAULT NULL,
  `replyPolicy` varchar(15) DEFAULT NULL,
  `associationPolicy` varchar(15) NOT NULL,
  `maxLeafNodes` int(11) NOT NULL,
  PRIMARY KEY (`serviceID`,`leaf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofpubsubdefaultconf
-- ----------------------------
INSERT INTO `ofpubsubdefaultconf` VALUES ('pubsub', '0', '0', '0', '0', '0', '1', '1', '1', '0', '0', 'publishers', '1', 'open', 'English', null, 'all', '-1');
INSERT INTO `ofpubsubdefaultconf` VALUES ('pubsub', '1', '1', '5120', '0', '-1', '1', '1', '1', '0', '1', 'publishers', '1', 'open', 'English', null, 'all', '-1');

-- ----------------------------
-- Table structure for ofpubsubitem
-- ----------------------------
DROP TABLE IF EXISTS `ofpubsubitem`;
CREATE TABLE `ofpubsubitem` (
  `serviceID` varchar(100) NOT NULL,
  `nodeID` varchar(100) NOT NULL,
  `id` varchar(100) NOT NULL,
  `jid` varchar(255) NOT NULL,
  `creationDate` char(15) NOT NULL,
  `payload` mediumtext,
  PRIMARY KEY (`serviceID`,`nodeID`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofpubsubitem
-- ----------------------------

-- ----------------------------
-- Table structure for ofpubsubnode
-- ----------------------------
DROP TABLE IF EXISTS `ofpubsubnode`;
CREATE TABLE `ofpubsubnode` (
  `serviceID` varchar(100) NOT NULL,
  `nodeID` varchar(100) NOT NULL,
  `leaf` tinyint(4) NOT NULL,
  `creationDate` char(15) NOT NULL,
  `modificationDate` char(15) NOT NULL,
  `parent` varchar(100) DEFAULT NULL,
  `deliverPayloads` tinyint(4) NOT NULL,
  `maxPayloadSize` int(11) DEFAULT NULL,
  `persistItems` tinyint(4) DEFAULT NULL,
  `maxItems` int(11) DEFAULT NULL,
  `notifyConfigChanges` tinyint(4) NOT NULL,
  `notifyDelete` tinyint(4) NOT NULL,
  `notifyRetract` tinyint(4) NOT NULL,
  `presenceBased` tinyint(4) NOT NULL,
  `sendItemSubscribe` tinyint(4) NOT NULL,
  `publisherModel` varchar(15) NOT NULL,
  `subscriptionEnabled` tinyint(4) NOT NULL,
  `configSubscription` tinyint(4) NOT NULL,
  `accessModel` varchar(10) NOT NULL,
  `payloadType` varchar(100) DEFAULT NULL,
  `bodyXSLT` varchar(100) DEFAULT NULL,
  `dataformXSLT` varchar(100) DEFAULT NULL,
  `creator` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `replyPolicy` varchar(15) DEFAULT NULL,
  `associationPolicy` varchar(15) DEFAULT NULL,
  `maxLeafNodes` int(11) DEFAULT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofpubsubnode
-- ----------------------------
INSERT INTO `ofpubsubnode` VALUES ('pubsub', '', '0', '001480487835478', '001480487835478', null, '0', '0', '0', '0', '1', '1', '1', '0', '0', 'publishers', '1', '0', 'open', '', '', '', '127.0.0.1', '', 'English', '', null, 'all', '-1');

-- ----------------------------
-- Table structure for ofpubsubnodegroups
-- ----------------------------
DROP TABLE IF EXISTS `ofpubsubnodegroups`;
CREATE TABLE `ofpubsubnodegroups` (
  `serviceID` varchar(100) NOT NULL,
  `nodeID` varchar(100) NOT NULL,
  `rosterGroup` varchar(100) NOT NULL,
  KEY `ofPubsubNodeGroups_idx` (`serviceID`,`nodeID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofpubsubnodegroups
-- ----------------------------

-- ----------------------------
-- Table structure for ofpubsubnodejids
-- ----------------------------
DROP TABLE IF EXISTS `ofpubsubnodejids`;
CREATE TABLE `ofpubsubnodejids` (
  `serviceID` varchar(100) NOT NULL,
  `nodeID` varchar(100) NOT NULL,
  `jid` varchar(255) NOT NULL,
  `associationType` varchar(20) NOT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofpubsubnodejids
-- ----------------------------

-- ----------------------------
-- Table structure for ofpubsubsubscription
-- ----------------------------
DROP TABLE IF EXISTS `ofpubsubsubscription`;
CREATE TABLE `ofpubsubsubscription` (
  `serviceID` varchar(100) NOT NULL,
  `nodeID` varchar(100) NOT NULL,
  `id` varchar(100) NOT NULL,
  `jid` varchar(255) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `state` varchar(15) NOT NULL,
  `deliver` tinyint(4) NOT NULL,
  `digest` tinyint(4) NOT NULL,
  `digest_frequency` int(11) NOT NULL,
  `expire` char(15) DEFAULT NULL,
  `includeBody` tinyint(4) NOT NULL,
  `showValues` varchar(30) DEFAULT NULL,
  `subscriptionType` varchar(10) NOT NULL,
  `subscriptionDepth` tinyint(4) NOT NULL,
  `keyword` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofpubsubsubscription
-- ----------------------------

-- ----------------------------
-- Table structure for ofremoteserverconf
-- ----------------------------
DROP TABLE IF EXISTS `ofremoteserverconf`;
CREATE TABLE `ofremoteserverconf` (
  `xmppDomain` varchar(255) NOT NULL,
  `remotePort` int(11) DEFAULT NULL,
  `permission` varchar(10) NOT NULL,
  PRIMARY KEY (`xmppDomain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofremoteserverconf
-- ----------------------------

-- ----------------------------
-- Table structure for ofroster
-- ----------------------------
DROP TABLE IF EXISTS `ofroster`;
CREATE TABLE `ofroster` (
  `rosterID` bigint(20) NOT NULL,
  `username` varchar(64) NOT NULL,
  `jid` varchar(1024) NOT NULL,
  `sub` tinyint(4) NOT NULL,
  `ask` tinyint(4) NOT NULL,
  `recv` tinyint(4) NOT NULL,
  `nick` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rosterID`),
  KEY `ofRoster_unameid_idx` (`username`) USING BTREE,
  KEY `ofRoster_jid_idx` (`jid`(255)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofroster
-- ----------------------------

-- ----------------------------
-- Table structure for ofrostergroups
-- ----------------------------
DROP TABLE IF EXISTS `ofrostergroups`;
CREATE TABLE `ofrostergroups` (
  `rosterID` bigint(20) NOT NULL,
  `rank` tinyint(4) NOT NULL,
  `groupName` varchar(255) NOT NULL,
  PRIMARY KEY (`rosterID`,`rank`),
  KEY `ofRosterGroup_rosterid_idx` (`rosterID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofrostergroups
-- ----------------------------

-- ----------------------------
-- Table structure for ofsaslauthorized
-- ----------------------------
DROP TABLE IF EXISTS `ofsaslauthorized`;
CREATE TABLE `ofsaslauthorized` (
  `username` varchar(64) NOT NULL,
  `principal` text NOT NULL,
  PRIMARY KEY (`username`,`principal`(200))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofsaslauthorized
-- ----------------------------

-- ----------------------------
-- Table structure for ofsecurityauditlog
-- ----------------------------
DROP TABLE IF EXISTS `ofsecurityauditlog`;
CREATE TABLE `ofsecurityauditlog` (
  `msgID` bigint(20) NOT NULL,
  `username` varchar(64) NOT NULL,
  `entryStamp` bigint(20) NOT NULL,
  `summary` varchar(255) NOT NULL,
  `node` varchar(255) NOT NULL,
  `details` text,
  PRIMARY KEY (`msgID`),
  KEY `ofSecurityAuditLog_tstamp_idx` (`entryStamp`) USING BTREE,
  KEY `ofSecurityAuditLog_uname_idx` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofsecurityauditlog
-- ----------------------------
INSERT INTO `ofsecurityauditlog` VALUES ('171', 'admin', '1480488520625', 'set server property xmpp.domain', '127.0.0.1', 'xmpp.domain = 192.168.1.245');
INSERT INTO `ofsecurityauditlog` VALUES ('172', 'admin', '1480498237485', 'deleted SSL cert from SOCKET_S2S with alias im.yunbaober.com_dsa', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('173', 'admin', '1480498239438', 'deleted SSL cert from SOCKET_S2S with alias im.yunbaober.com_rsa', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('174', 'admin', '1480498253862', 'generated SSL self-signed certs', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('175', 'admin', '1480563195923', 'closed session for address 10000000086@192.168.1.245/Smack', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('176', 'admin', '1480563215929', 'closed session for address admin@192.168.1.245/4d29jvwsv2', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('177', 'admin', '1480563219282', 'closed session for address admin@192.168.1.245/4e4a9qiuxf', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('178', 'admin', '1480563224191', 'closed session for address admin@192.168.1.245/jcxkjdyxs', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('179', 'admin', '1480563231298', 'closed session for address admin@192.168.1.245/30ol1t91m2', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('180', 'admin', '1480563239631', 'closed session for address admin@192.168.1.245/8kqjdvfr5x', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('181', 'admin', '1480563249729', 'closed session for address admin@192.168.1.245/49yrg9aa23', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('182', 'admin', '1486530621041', 'closed session for address appts@192.168.1.245/DESKTOP-NR3VK44', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('183', 'admin', '1486530627370', 'closed session for address admin@192.168.1.245/8ruk66rcow', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('184', 'admin', '1486530633193', 'closed session for address admin@192.168.1.245/272wxfsycj', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('185', 'admin', '1489456424261', 'closed session for address 18030785510@192.168.1.245/Smack', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('186', 'admin', '1489456506139', 'closed session for address 18030785510@192.168.1.245/Smack', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('187', 'admin', '1489722825381', 'reloaded plugin radar', '127.0.0.1', null);
INSERT INTO `ofsecurityauditlog` VALUES ('188', 'admin', '1489739195659', 'enabled debug logging', 'C8R8VIYB41VIW7K', null);
INSERT INTO `ofsecurityauditlog` VALUES ('189', 'admin', '1489739199998', 'enabled debug logging', 'C8R8VIYB41VIW7K', null);
INSERT INTO `ofsecurityauditlog` VALUES ('190', 'admin', '1489739203170', 'enabled debug logging', 'C8R8VIYB41VIW7K', null);
INSERT INTO `ofsecurityauditlog` VALUES ('191', 'admin', '1489739972381', 'closed session for address appts@127.0.0.1/C8R8VIYB41VIW7K', 'C8R8VIYB41VIW7K', null);

-- ----------------------------
-- Table structure for ofuser
-- ----------------------------
DROP TABLE IF EXISTS `ofuser`;
CREATE TABLE `ofuser` (
  `username` varchar(64) NOT NULL,
  `plainPassword` varchar(32) DEFAULT NULL,
  `encryptedPassword` varchar(255) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `creationDate` char(15) NOT NULL,
  `modificationDate` char(15) NOT NULL,
  `storedKey` varchar(32) DEFAULT NULL,
  `serverKey` varchar(32) DEFAULT NULL,
  `salt` varchar(32) DEFAULT NULL,
  `iterations` int(11) DEFAULT NULL,
  PRIMARY KEY (`username`),
  KEY `ofUser_cDate_idx` (`creationDate`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofuser
-- ----------------------------
INSERT INTO `ofuser` VALUES ('admin', 'admin', null, 'Administrator', 'admin@example.com', '0', '0', null, null, null, null);

-- ----------------------------
-- Table structure for ofuserflag
-- ----------------------------
DROP TABLE IF EXISTS `ofuserflag`;
CREATE TABLE `ofuserflag` (
  `username` varchar(64) NOT NULL,
  `name` varchar(100) NOT NULL,
  `startTime` char(15) DEFAULT NULL,
  `endTime` char(15) DEFAULT NULL,
  PRIMARY KEY (`username`,`name`),
  KEY `ofUserFlag_sTime_idx` (`startTime`) USING BTREE,
  KEY `ofUserFlag_eTime_idx` (`endTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofuserflag
-- ----------------------------

-- ----------------------------
-- Table structure for ofuserprop
-- ----------------------------
DROP TABLE IF EXISTS `ofuserprop`;
CREATE TABLE `ofuserprop` (
  `username` varchar(64) NOT NULL,
  `name` varchar(100) NOT NULL,
  `propValue` text NOT NULL,
  PRIMARY KEY (`username`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofuserprop
-- ----------------------------
INSERT INTO `ofuserprop` VALUES ('admin', 'console.refresh', 'session-summary=10');
INSERT INTO `ofuserprop` VALUES ('admin', 'console.rows_per_page', 'session-summary=15');

-- ----------------------------
-- Table structure for ofvcard
-- ----------------------------
DROP TABLE IF EXISTS `ofvcard`;
CREATE TABLE `ofvcard` (
  `username` varchar(64) NOT NULL,
  `vcard` mediumtext NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofvcard
-- ----------------------------

-- ----------------------------
-- Table structure for ofversion
-- ----------------------------
DROP TABLE IF EXISTS `ofversion`;
CREATE TABLE `ofversion` (
  `name` varchar(50) NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ofversion
-- ----------------------------
INSERT INTO `ofversion` VALUES ('openfire', '22');
