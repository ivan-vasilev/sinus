/*
SQLyog Community v8.4 
MySQL - 5.5.15 : Database - sinus
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sinus` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `sinus`;

/*Table structure for table `default_annotate_values_path` */

DROP TABLE IF EXISTS `default_annotate_values_path`;

CREATE TABLE `default_annotate_values_path` (
  `default_annotate_values_paths_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `default_filter_values_id` int(10) unsigned NOT NULL,
  `path` mediumtext NOT NULL,
  PRIMARY KEY (`default_annotate_values_paths_id`),
  KEY `FK_default_annotate_values_path_default_filter_values` (`default_filter_values_id`),
  CONSTRAINT `FK_default_annotate_values_path_default_filter_values` FOREIGN KEY (`default_filter_values_id`) REFERENCES `default_filter_values` (`default_filter_values_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Table structure for table `default_display_values_path` */

DROP TABLE IF EXISTS `default_display_values_path`;

CREATE TABLE `default_display_values_path` (
  `default_display_values_paths_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `default_filter_values_id` int(10) unsigned NOT NULL,
  `path` mediumtext NOT NULL,
  PRIMARY KEY (`default_display_values_paths_id`),
  KEY `FK_default_display_values_path_default_filter_values` (`default_filter_values_id`),
  CONSTRAINT `FK_default_display_values_path_default_filter_values` FOREIGN KEY (`default_filter_values_id`) REFERENCES `default_filter_values` (`default_filter_values_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Table structure for table `default_filter_values` */

DROP TABLE IF EXISTS `default_filter_values`;

CREATE TABLE `default_filter_values` (
  `default_filter_values_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ontologies_id` int(10) unsigned NOT NULL,
  `iri` varchar(500) NOT NULL,
  PRIMARY KEY (`default_filter_values_id`),
  KEY `FK_default_filter_values_ontologies` (`ontologies_id`),
  CONSTRAINT `FK_default_filter_values_ontologies` FOREIGN KEY (`ontologies_id`) REFERENCES `ontologies` (`ontologies_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `ontologies` */

DROP TABLE IF EXISTS `ontologies`;

CREATE TABLE `ontologies` (
  `ontologies_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uri` varchar(500) NOT NULL,
  `id` varchar(500) NOT NULL,
  `is_configured` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`ontologies_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `saved_searches` */

DROP TABLE IF EXISTS `saved_searches`;

CREATE TABLE `saved_searches` (
  `saved_searches_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parent` int(10) unsigned DEFAULT NULL,
  `users_id` int(10) unsigned NOT NULL,
  `human_readable` varchar(5000) NOT NULL,
  `date_created` date NOT NULL,
  `object_uri` varchar(5000) NOT NULL,
  `sparql` varchar(5000) NOT NULL,
  `sparql_no_context` varchar(5000) NOT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `is_selected` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `all_selected` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`saved_searches_id`),
  KEY `FK_saved_searches_users` (`users_id`),
  KEY `FK_saved_searches_parent` (`parent`),
  CONSTRAINT `FK_saved_searches_parent` FOREIGN KEY (`parent`) REFERENCES `saved_searches` (`saved_searches_id`),
  CONSTRAINT `FK_saved_searches_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`users_id`)
) ENGINE=InnoDB AUTO_INCREMENT=169 DEFAULT CHARSET=utf8;

/*Table structure for table `search_display_values` */

DROP TABLE IF EXISTS `search_display_values`;

CREATE TABLE `search_display_values` (
  `search_display_values_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `saved_searches_id` int(10) unsigned NOT NULL,
  `uri_path` varchar(10000) NOT NULL,
  PRIMARY KEY (`search_display_values_id`),
  KEY `FK_search_display_values_saved_searches` (`saved_searches_id`),
  CONSTRAINT `FK_search_display_values_saved_searches` FOREIGN KEY (`saved_searches_id`) REFERENCES `saved_searches` (`saved_searches_id`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8;

/*Table structure for table `search_results` */

DROP TABLE IF EXISTS `search_results`;

CREATE TABLE `search_results` (
  `search_results_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `saved_searches_id` int(10) unsigned NOT NULL,
  `result` varchar(1000) NOT NULL,
  PRIMARY KEY (`search_results_id`),
  KEY `FK_search_results_saved_searches` (`saved_searches_id`),
  CONSTRAINT `FK_search_results_saved_searches` FOREIGN KEY (`saved_searches_id`) REFERENCES `saved_searches` (`saved_searches_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6626 DEFAULT CHARSET=utf8;

/*Table structure for table `selected_results` */

DROP TABLE IF EXISTS `selected_results`;

CREATE TABLE `selected_results` (
  `selected_results_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `users_id` int(10) unsigned NOT NULL,
  `class_iri` varchar(500) NOT NULL,
  `object_iri` varchar(255) NOT NULL,
  `saved_searches_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`selected_results_id`),
  KEY `FK_selected_results_users` (`users_id`),
  KEY `NewIndex1` (`class_iri`(255)),
  KEY `NewIndex2` (`object_iri`),
  KEY `FK_selected_results_saved_searches` (`saved_searches_id`),
  CONSTRAINT `FK_selected_results_saved_searches` FOREIGN KEY (`saved_searches_id`) REFERENCES `saved_searches` (`saved_searches_id`),
  CONSTRAINT `FK_selected_results_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`users_id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8;

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `users_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password_hash` varbinary(160) NOT NULL,
  `date_created` date NOT NULL,
  `unlimited_ads` tinyint(4) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `auth_role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`users_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `users_confirmation` */

DROP TABLE IF EXISTS `users_confirmation`;

CREATE TABLE `users_confirmation` (
  `users_id` int(10) unsigned NOT NULL,
  `confirm_key` varchar(255) NOT NULL,
  PRIMARY KEY (`users_id`),
  CONSTRAINT `FK_users_confirmation_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`users_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
