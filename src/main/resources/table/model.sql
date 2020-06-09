CREATE TABLE `model` ( 
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `model_name` varchar(255)  NOT NULL, 
  `file_type` varchar(255)  NULL, 
  `translate_status` varchar(255)  NULL, 
  `sync` int(11)  NULL, 
  `model_file_path` varchar(255)  NULL, 
  `file_id` int(11) unique NOT NULL, 
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8