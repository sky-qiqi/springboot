package com.example.work4.controller; // 包名已更新

import com.example.work4.entity.IdCard; // 导入 IdCard 实体
import com.example.work4.entity.User; // 导入 User 实体
import com.example.work4.repository.IdCardRepository; // 导入 IdCardRepository
import com.example.work4.repository.UserRepository; // 导入 UserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate; // 导入 LocalDate
import java.time.LocalDateTime; // 导入 LocalDateTime
import java.util.List;
import java.util.Optional;

@RestController // 标记这是一个 REST 控制器
@RequestMapping("/api/users") // 设置基础请求路径
public class UserController {

    @Autowired // 注入 UserRepository
    private UserRepository userRepository;

    @Autowired // 注入 IdCardRepository (用于更灵活地操作 IdCard)
    private IdCardRepository idCardRepository;

    // --- User 的 CRUD 操作 ---

    // 创建新用户 (可选带身份证信息)
    // POST http://localhost:8080/api/users
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // 如果请求体中包含了 IdCard 信息
        if (user.getIdCard() != null) {
            // 确保 IdCard 的 user 字段指向当前 User 对象，维护双向关联
            user.getIdCard().setUser(user);
            // 确保 IdCard 在创建时没有指定 ID，让 JPA 自动生成
            user.getIdCard().setCardId(null);
        }
        // 如果 created_at 没有在请求体中提供，则设置当前时间
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        // 保存 User，由于 User 实体中 IdCard 关联设置了 cascade = CascadeType.ALL，
        // 保存 User 会级联保存关联的 IdCard
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser); // 返回创建成功的用户对象和 201 状态码
    }

    // 获取所有用户
    // GET http://localhost:8080/api/users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll(); // 返回所有用户列表
    }

    // 根据用户 ID 获取用户 (会包含关联的 IdCard，取决于 FetchType)
    // GET http://localhost:8080/api/users/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(ResponseEntity::ok) // 如果找到用户，返回用户对象和 200 状态码
                .orElseGet(() -> ResponseEntity.notFound().build()); // 否则返回 404 状态码
    }

    // 更新用户基本信息 (不更新身份证信息)
    // PUT http://localhost:8080/api/users/{userId}
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Integer userId, @RequestBody User userDetails) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 更新用户基本信息
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            // 不在这里处理 IdCard 的更新，通过专门的 IdCard 接口处理

            User updatedUser = userRepository.save(user); // 保存更新后的用户
            return ResponseEntity.ok(updatedUser); // 返回更新后的用户对象和 200 状态码
        } else {
            return ResponseEntity.notFound().build(); // 否则返回 404 状态码
        }
    }

    // 根据用户 ID 删除用户 (会级联删除关联的 IdCard，因为在 User 实体中设置了 orphanRemoval = true)
    // DELETE http://localhost:8080/api/users/{userId}
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId); // 删除用户
            return ResponseEntity.noContent().build(); // 返回 204 No Content 状态码表示成功但无返回体
        } else {
            return ResponseEntity.notFound().build(); // 否则返回 404 状态码
        }
    }

    // --- 与 User 关联的 IdCard 操作 ---

    // 为指定用户创建或更新身份证信息
    // POST http://localhost:8080/api/users/{userId}/idcard
    // 如果用户已有身份证，则更新；如果用户没有身份证，则创建新的并关联
    @PostMapping("/{userId}/idcard")
    public ResponseEntity<IdCard> createOrUpdateIdCard(@PathVariable Integer userId, @RequestBody IdCard idCardDetails) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            IdCard existingIdCard = user.getIdCard(); // 获取用户当前关联的 IdCard

            if (existingIdCard == null) {
                // 用户没有关联的 IdCard，创建新的
                idCardDetails.setUser(user); // 设置新 IdCard 的用户关联
                user.setIdCard(idCardDetails); // 设置 User 的 IdCard 关联
                idCardDetails.setCardId(null); // 确保作为新实体创建
                IdCard savedIdCard = idCardRepository.save(idCardDetails); // 保存新的 IdCard
                // 也可以只调用 userRepository.save(user); 因为 User 实体设置了 cascade=ALL
                return ResponseEntity.status(HttpStatus.CREATED).body(savedIdCard); // 返回创建成功的 IdCard 和 201 状态码
            } else {
                // 用户已有关联的 IdCard，更新现有 IdCard
                existingIdCard.setCardNumber(idCardDetails.getCardNumber());
                existingIdCard.setIssueDate(idCardDetails.getIssueDate());
                IdCard updatedIdCard = idCardRepository.save(existingIdCard); // 保存更新后的 IdCard
                return ResponseEntity.ok(updatedIdCard); // 返回更新后的 IdCard 和 200 状态码
            }
        } else {
            return ResponseEntity.notFound().build(); // 用户不存在，返回 404 状态码
        }
    }

    // 获取指定用户的身份证信息
    // GET http://localhost:8080/api/users/{userId}/idcard
    @GetMapping("/{userId}/idcard")
    public ResponseEntity<IdCard> getIdCardByUserId(@PathVariable Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            IdCard idCard = user.getIdCard(); // 通过 User 实体获取关联的 IdCard

            if (idCard != null) {
                return ResponseEntity.ok(idCard); // 返回 IdCard 对象和 200 状态码
            } else {
                // 用户存在，但没有关联的 IdCard
                return ResponseEntity.notFound().build(); // 或者返回 204 No Content 表示没有内容
            }
        } else {
            // 用户不存在
            return ResponseEntity.notFound().build(); // 返回 404 状态码
        }
    }

    // 删除指定用户的身份证信息
    // DELETE http://localhost:8080/api/users/{userId}/idcard
    @DeleteMapping("/{userId}/idcard")
    public ResponseEntity<Void> deleteIdCardByUserId(@PathVariable Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            IdCard idCard = user.getIdCard();

            if (idCard != null) {
                // 断开 User 与 IdCard 的关联
                user.setIdCard(null);
                // 因为 User 实体中 IdCard 关联设置了 orphanRemoval = true，断开关联会自动删除 IdCard
                userRepository.save(user); // 保存 User 来触发关联更新和 IdCard 删除

                // 如果没有设置 orphanRemoval=true，则需要手动删除 IdCard
                // idCardRepository.delete(idCard);

                return ResponseEntity.noContent().build(); // 返回 204 No Content
            } else {
                // 用户存在，但没有关联的 IdCard 需要删除
                return ResponseEntity.notFound().build(); // 或者 404 表示要删除的资源不存在
            }
        } else {
            // 用户不存在
            return ResponseEntity.notFound().build(); // 返回 404 状态码
        }
    }
}