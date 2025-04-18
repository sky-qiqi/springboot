
CREATE TABLE IF NOT EXISTS t_order (
                                       order_id VARCHAR(50) PRIMARY KEY,
                                       sender_user_id VARCHAR(50), -- 可选：关联用户表中的寄件人
                                       sender_name VARCHAR(100) NOT NULL,
                                       sender_phone VARCHAR(20) NOT NULL,
                                       sender_address VARCHAR(255) NOT NULL,
                                       receiver_user_id VARCHAR(50), -- 可选：关联用户表中的收件人
                                       receiver_name VARCHAR(100) NOT NULL,
                                       receiver_phone VARCHAR(20) NOT NULL,
                                       receiver_address VARCHAR(255) NOT NULL,
                                       weight DECIMAL(10, 2) NOT NULL,
                                       distance DECIMAL(10, 2),
                                       freight DECIMAL(10, 2) NOT NULL,
                                       order_status VARCHAR(50) NOT NULL,
                                       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       delivery_man_id VARCHAR(50),
                                       expected_pickup_time TIMESTAMP,
                                       expected_delivery_time TIMESTAMP,
                                       actual_pickup_time TIMESTAMP,
                                       actual_delivery_time TIMESTAMP,
                                       logistics_type VARCHAR(50) DEFAULT 'express' COMMENT '物流类型，例如：express, standard, large_item', -- 预留字段
                                       payment_status VARCHAR(50) DEFAULT 'unpaid' COMMENT '支付状态，例如：unpaid, paid', -- 预留字段，未来可能加入支付功能
                                       payment_method VARCHAR(50) COMMENT '支付方式', -- 预留字段
                                       remark VARCHAR(255) COMMENT '订单备注', -- 允许用户添加备注
                                       INDEX idx_order_status (order_status),
                                       INDEX idx_delivery_man_id (delivery_man_id),
                                       FOREIGN KEY (delivery_man_id) REFERENCES t_delivery_man(delivery_man_id),
                                       FOREIGN KEY (sender_user_id) REFERENCES t_user(user_id), -- 如果需要关联用户
                                       FOREIGN KEY (receiver_user_id) REFERENCES t_user(user_id) -- 如果需要关联用户
);

-- 创建配送员表 (t_delivery_man)
CREATE TABLE IF NOT EXISTS t_delivery_man (
                                              delivery_man_id VARCHAR(50) PRIMARY KEY,
                                              name VARCHAR(100) NOT NULL,
                                              phone VARCHAR(20) NOT NULL,
                                              status VARCHAR(50) NOT NULL,
                                              current_location_latitude DECIMAL(10, 6),
                                              current_location_longitude DECIMAL(10, 6),
                                              last_location_update_time TIMESTAMP,
                                              vehicle_type VARCHAR(50) COMMENT '配送工具类型，例如：motorcycle, bicycle, car', -- 预留字段
                                              INDEX idx_delivery_man_status (status)
);

-- 创建用户表 (t_user)
CREATE TABLE IF NOT EXISTS t_user (
                                      user_id VARCHAR(50) PRIMARY KEY,
                                      username VARCHAR(50) NOT NULL UNIQUE,
                                      password VARCHAR(255) NOT NULL,
                                      phone VARCHAR(20) UNIQUE,
                                      role VARCHAR(50) NOT NULL,
                                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      INDEX idx_username (username),
                                      INDEX idx_phone (phone)
);

-- 可以创建地址信息表，用于缓存常用地址，减少重复输入
CREATE TABLE IF NOT EXISTS t_address (
                                         address_id INT AUTO_INCREMENT PRIMARY KEY,
                                         user_id VARCHAR(50) NOT NULL,
                                         address VARCHAR(255) NOT NULL,
                                         latitude DECIMAL(10, 6),
                                         longitude DECIMAL(10, 6),
                                         is_default BOOLEAN DEFAULT FALSE,
                                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         INDEX idx_user_id (user_id),
                                         FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);