package com.example.work4.repository; // 包名已更新

import com.example.work4.entity.IdCard; // 导入路径已更新
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdCardRepository extends JpaRepository<IdCard, Integer> {
    // Spring Data JPA will provide basic CRUD methods
    IdCard findByCardNumber(String cardNumber);
}