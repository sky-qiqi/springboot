package org.example.t3.service;

import org.example.t3.entity.IdCard;

import java.util.List;

public interface IdCardService {
    int add(IdCard idCard);
    IdCard getById(int id);
    List<IdCard> getAll();
    int update(IdCard idCard);
    int deleteById(int id);
    IdCard getByUserId(int userId);
}