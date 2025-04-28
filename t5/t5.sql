-- 创建数据库 (如果需要)
CREATE DATABASE IF NOT EXISTS springboottest DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 切换到数据库
USE springboottest;

-- 创建商品表
CREATE TABLE `product` (
                           `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
                           `name` VARCHAR(255) NOT NULL COMMENT '商品名称',
                           `description` TEXT COMMENT '商品描述',
                           `price` DECIMAL(10, 2) NOT NULL COMMENT '商品价格',
                           `stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
                           `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='商品信息表';

-- 插入一些示例数据
INSERT INTO `product` (`name`, `description`, `price`, `stock`) VALUES
                                                                    ('Apple iPhone 13', 'A great smartphone.', 6999.00, 100),
                                                                    ('Samsung Galaxy S22', 'Another flagship phone.', 6599.00, 120),
                                                                    ('Sony WH-1000XM4', 'Noise cancelling headphones.', 1999.00, 50),
                                                                    ('Logitech MX Master 3', 'Advanced wireless mouse.', 699.00, 80);