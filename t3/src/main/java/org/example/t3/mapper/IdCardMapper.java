package org.example.t3.mapper;

import org.apache.ibatis.annotations.*;
import org.example.t3.entity.IdCard;

import java.util.List;

@Mapper
public interface IdCardMapper {
    @Insert("INSERT INTO id_cards (card_number, issue_date, user_id) VALUES (#{cardNumber}, #{issueDate}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "cardId")
    int insert(IdCard idCard);

    @Select("SELECT card_id, card_number, issue_date, user_id FROM id_cards WHERE card_id = #{cardId}")
    IdCard findById(int cardId);

    @Select("SELECT card_id, card_number, issue_date, user_id FROM id_cards WHERE user_id = #{userId}")
    IdCard findByUserId(int userId);

    @Select("SELECT card_id, card_number, issue_date, user_id FROM id_cards")
    List<IdCard> findAll();

    @Update("UPDATE id_cards SET card_number = #{cardNumber}, issue_date = #{issueDate}, user_id = #{userId} WHERE card_id = #{cardId}")
    int update(IdCard idCard);

    @Delete("DELETE FROM id_cards WHERE card_id = #{cardId}")
    int deleteById(int cardId);
}