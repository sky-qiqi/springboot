package org.example.t3.mapper;

import org.apache.ibatis.annotations.*;
import org.example.t3.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users (username, email) VALUES (#{username}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Select("SELECT user_id, username, email, created_at FROM users WHERE user_id = #{userId}")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "createdAt", column = "created_at"), // 添加这一行
            @Result(property = "idCard", column = "user_id",
                    one = @One(select = "org.example.t3.mapper.IdCardMapper.findByUserId"))
    })
    User findById(int userId);

    @Select("SELECT user_id, username, email, created_at FROM users")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "createdAt", column = "created_at"), // 添加这一行
            @Result(property = "idCard", column = "user_id",
                    one = @One(select = "org.example.t3.mapper.IdCardMapper.findByUserId"))
    })
    List<User> findAll();

    @Update("UPDATE users SET username = #{username}, email = #{email} WHERE user_id = #{userId}")
    int update(User user);

    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    int deleteById(int userId);
}