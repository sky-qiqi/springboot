-- 插入用户表 (t_user)
INSERT INTO t_user (user_id, username, password, phone, role) VALUES
                                                                  ('USR001', 'admin', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxx', '13800138000', 'admin'), -- 密码需要替换为实际加密后的密码
                                                                  ('USR002', 'zhangsan', '$2a$10$yyyyyyyyyyyyyyyyyyyyyyyyyyyyy', '13912345678', 'customer'), -- 密码需要替换为实际加密后的密码
                                                                  ('USR003', 'lisi', '$2a$10$zzzzzzzzzzzzzzzzzzzzzzzzzzzzz', '13588889999', 'customer'); -- 密码需要替换为实际加密后的密码

-- 插入配送员表 (t_delivery_man)
INSERT INTO t_delivery_man (delivery_man_id, name, phone, status, current_location_latitude, current_location_longitude, last_location_update_time, vehicle_type) VALUES
                                                                                                                                                                      ('DEL001', '王强', '13711112222', '空闲', 39.9042, 116.4074, NOW(), 'motorcycle'), -- 北京
                                                                                                                                                                      ('DEL002', '李梅', '13633334444', '工作中', 31.2304, 121.4737, NOW(), 'bicycle'), -- 上海
                                                                                                                                                                      ('DEL003', '张伟', '13555556666', '空闲', 22.5431, 114.0579, NOW(), 'car'); -- 深圳

-- 插入订单表 (t_order)
INSERT INTO t_order (order_id, sender_user_id, sender_name, sender_phone, sender_address, receiver_user_id, receiver_name, receiver_phone, receiver_address, weight, distance, freight, order_status, create_time, delivery_man_id, expected_pickup_time, expected_delivery_time) VALUES
                                                                                                                                                                                                                                                                                      ('ORD20250415001', 'USR002', '张三', '13912345678', '北京市朝阳区建国门外大街1号', 'USR003', '李四', '13588889999', '上海市浦东新区陆家嘴东路161号', 2.5, 1200.5, 25.00, '运输中', NOW(), 'DEL001', '2025-04-16 09:00:00', '2025-04-17 14:00:00'),
                                                                                                                                                                                                                                                                                      ('ORD20250415002', NULL, '王小红', '13698765432', '广东省深圳市南山区科技园', NULL, '赵明', '13701234567', '浙江省杭州市西湖区文三路398号', 1.0, 1500.0, 18.50, '待揽件', NOW(), 'DEL003', '2025-04-16 10:30:00', '2025-04-17 16:30:00'),
                                                                                                                                                                                                                                                                                      ('ORD20250415003', 'USR003', '李四', '13588889999', '上海市浦东新区世纪大道88号', 'USR002', '张三', '13912345678', '北京市海淀区中关村大街27号', 5.0, 1150.2, 32.00, '已揽件', NOW(), 'DEL002', '2025-04-15 21:00:00', '2025-04-16 20:00:00');

-- 插入地址信息表 (t_address)
INSERT INTO t_address (user_id, address, latitude, longitude, is_default) VALUES
                                                                              ('USR002', '北京市朝阳区建国门外大街1号', 39.9075, 116.4039, TRUE),
                                                                              ('USR002', '北京市海淀区中关村大街27号', 39.9879, 116.3206, FALSE),
                                                                              ('USR003', '上海市浦东新区陆家嘴东路161号', 31.2447, 121.5017, TRUE),
                                                                              ('USR003', '上海市浦东新区世纪大道88号', 31.2333, 121.5333, FALSE);