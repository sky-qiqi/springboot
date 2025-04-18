package org.example.t3.service.impl;

import org.example.t3.entity.IdCard;
import org.example.t3.mapper.IdCardMapper;
import org.example.t3.service.IdCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdCardServiceImpl implements IdCardService {
    @Autowired
    private IdCardMapper idCardMapper;

    @Override
    public int add(IdCard idCard) {
        return idCardMapper.insert(idCard);
    }

    @Override
    public IdCard getById(int id) {
        return idCardMapper.findById(id);
    }

    @Override
    public List<IdCard> getAll() {
        return idCardMapper.findAll();
    }

    @Override
    public int update(IdCard idCard) {
        return idCardMapper.update(idCard);
    }

    @Override
    public int deleteById(int id) {
        return idCardMapper.deleteById(id);
    }

    @Override
    public IdCard getByUserId(int userId) {
        return idCardMapper.findByUserId(userId);
    }
}