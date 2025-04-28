package com.example.t5springboot.model;

import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.io.Serializable; // <-- 添加这一行导入 Serializable 接口

@Data
public class Product implements Serializable { // <-- 添加 implements Serializable

    // 建议添加一个 serialVersionUID，用于版本控制，让IDE帮你生成或手动指定一个
    @Serial
    private static final long serialVersionUID = 1L; // 例如指定为 1L

    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Timestamp createTime;
    private Timestamp updateTime;

    // Lombok 会自动生成构造函数、getter、setter 等，不需要手动添加这些方法的实现Serializable接口
}