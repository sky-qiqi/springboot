package com.example.work4.entity; // 包名已更新，与您的项目结构一致

import jakarta.persistence.*;
import java.time.LocalDateTime; // 导入 LocalDateTime 类

@Entity // 标记这个类是一个JPA实体
@Table(name = "users") // 映射到数据库的 users 表
public class User {

    @Id // 标记为主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 配置主键自增长策略
    private Integer userId; // 对应数据库的 user_id 字段

    @Column(nullable = false, unique = true) // 映射到 username 字段，非空且唯一
    private String username; // 对应 username

    @Column(nullable = false, unique = true) // 映射到 email 字段，非空且唯一
    private String email; // 对应 email

    @Column(name = "created_at") // 映射到 created_at 字段
    private LocalDateTime createdAt; // 对应 created_at

    // 一对一关系：一个User对应一个IdCard
    // mappedBy = "user" 指示由 IdCard 类中的 "user" 字段来维护关系的外键
    // cascade = CascadeType.ALL 表示对 User 的操作（如保存、删除）会级联到关联的 IdCard
    // orphanRemoval = true 表示如果一个 IdCard 从 User 的关联中移除，它会被删除
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private IdCard idCard;

    // --- 构造函数 (可选，JPA 通常需要一个无参构造函数) ---
    public User() {
    }

    // --- Getter 和 Setter 方法 ---

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public IdCard getIdCard() {
        return idCard;
    }

    // 设置 IdCard 时，同时在 IdCard 端建立对 User 的关联 (维护双向关系)
    public void setIdCard(IdCard idCard) {
        if (this.idCard != idCard) { // 避免不必要的循环或操作
            if (this.idCard != null) {
                this.idCard.setUser(null); // 断开旧的关联
            }
            this.idCard = idCard;
            if (idCard != null) {
                idCard.setUser(this); // 建立新的关联
            }
        }
    }

    // --- 可选：重写 toString(), equals(), hashCode() ---
    // 对于实体类，toString() 可能会引起懒加载问题，谨慎使用
    // equals() 和 hashCode() 对于集合操作比较重要，但对于简单的CRUD可能不是必需的，特别是使用代理时要小心
}