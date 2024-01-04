CREATE TABLE IF NOT EXISTS `users` (
    `id` INT AUTO_INCREMENT,
    `user_name` VARCHAR(255) DEFAULT NULL,
    `full_name` VARCHAR(255) DEFAULT NULL,
    `password` VARCHAR(255) DEFAULT NULL,
    `account_non_expired` BIT(1) DEFAULT NULL,
    `account_non_locked` BIT(1) DEFAULT NULL,
    `credentials_non_expired` BIT(1) DEFAULT NULL,
    `enable` BIT(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_name` (`user_name`)
) ENGINE=InnoDB;
