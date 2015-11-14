CREATE DATABASE `picpoll` /*!40100 DEFAULT CHARACTER SET utf8 */;
CREATE TABLE `poll` (
  `idpoll` int(11) NOT NULL AUTO_INCREMENT,
  `poll_title` varchar(1000) DEFAULT NULL,
  `poll_desc` longtext,
  PRIMARY KEY (`idpoll`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `pic` (
  `idpic` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `pic_url` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  `pic_count` int(11) DEFAULT '0',
  `picpoll` int(11) DEFAULT NULL,
  `pictitle` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `tmp1` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `tmp2` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `tmp3` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `tmp4` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `tmp5` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`idpic`),
  KEY `fk_poll_idx` (`picpoll`),
  CONSTRAINT `fk_poll` FOREIGN KEY (`picpoll`) REFERENCES `poll` (`idpoll`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `ip_pool` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(100) DEFAULT NULL,
  `retired` int(11) DEFAULT NULL,
  `vote4picid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ipaddress_UNIQUE` (`ipaddress`),
  KEY `fk_v4p_idx` (`vote4picid`),
  CONSTRAINT `fk_v4p` FOREIGN KEY (`vote4picid`) REFERENCES `pic` (`idpic`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



