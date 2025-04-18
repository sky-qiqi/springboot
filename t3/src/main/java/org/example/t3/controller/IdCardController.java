package org.example.t3.controller;

import org.example.t3.entity.IdCard;
import org.example.t3.service.IdCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/idcards")
public class IdCardController {
    @Autowired
    private IdCardService idCardService;

    @PostMapping
    public int addIdCard(@RequestBody IdCard idCard) {
        return idCardService.add(idCard);
    }

    @GetMapping("/{id}")
    public IdCard getIdCardById(@PathVariable int id) {
        return idCardService.getById(id);
    }

    @GetMapping
    public List<IdCard> getAllIdCards() {
        return idCardService.getAll();
    }

    @PutMapping("/{id}")
    public int updateIdCard(@PathVariable int id, @RequestBody IdCard idCard) {
        idCard.setCardId(id);
        return idCardService.update(idCard);
    }

    @DeleteMapping("/{id}")
    public int deleteIdCard(@PathVariable int id) {
        return idCardService.deleteById(id);
    }

    @GetMapping("/user/{userId}")
    public IdCard getIdCardByUserId(@PathVariable int userId) {
        return idCardService.getByUserId(userId);
    }
}