package com.example.controllers;

import com.example.entities.Note;
import com.example.entities.User;
import com.example.repos.NoteRepo;
import com.example.repos.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.errors.APIMessage;

import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

@RestController
public class NoteController {
    @Autowired
    NoteRepo noteRepo;
    @Autowired
    UserRepo userRepo;
    @GetMapping("/api/notes")
    public Object notes(HttpSession session) throws JsonProcessingException {
        if (session.getAttribute("UserLogin") == null ) {
            return APIMessage.Unauthorized;
        }
        User user = userRepo.findByLogin(session.getAttribute("UserLogin").toString());
        return APIMessage.OK( new ObjectMapper().writeValueAsString(user.getNotes()));
    }
//  Получение заметки по id
    @GetMapping("/api/note/{id}")
    public Object note(@PathVariable Long id , HttpSession session) throws JsonProcessingException {
        if (session.getAttribute("UserLogin") == null ) {
            return APIMessage.Unauthorized;
        }
        Note note;
        try {
            note = noteRepo.findById(id).get();     //Поиск заметки в базе данных по id
        } catch (NoSuchElementException e) {
            return APIMessage.NotFound;     //Не найдено
        }

        //Проверка на принадлежность заметки пользователю
        if (!session.getAttribute("UserLogin").toString().equals(note.getUser().getLogin())) {
            return APIMessage.Forbidden;    //Отправляем отказ
        }
        return APIMessage.OK( new ObjectMapper().writeValueAsString(note));
    }
//  Добавление заметки
    @GetMapping("/api/addnote")
    public Object addNote(@RequestParam String text , HttpSession session) {
        if (session.getAttribute("UserLogin") == null) {
            return APIMessage.Unauthorized; // Не авторизован
        }
        Note note  = new Note(text, userRepo.findByLogin(session.getAttribute("UserLogin").toString()));
        noteRepo.save(note);    // Сохраняем в базу данных
        return APIMessage.OK(note.getId().toString());
    }

    @GetMapping("/api/updatenote/{id}")
    public Object updateNote(@PathVariable Long id, @RequestParam String text , HttpSession session) {

        if (session.getAttribute("UserLogin") == null) {
            return APIMessage.Unauthorized;
        }
        Note note;
        try {
            note = noteRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            return APIMessage.NotFound;
        }

        if (!session.getAttribute("UserLogin").toString().equals(note.getUser().getLogin())) {
            return APIMessage.Forbidden;
        }
        note.setText(text);
        noteRepo.save(note);
        return APIMessage.OK;
    }

    @GetMapping("/api/deletenote/{id}")
    public Object delete_note(@PathVariable Long id , HttpSession session) {
        if (session.getAttribute("UserLogin") == null ) {
            return APIMessage.Unauthorized;
        }
        Note note;
        try {
            note = noteRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            return APIMessage.NotFound;
        }

        if (!session.getAttribute("UserLogin").toString().equals(note.getUser().getLogin())) {
            return APIMessage.Forbidden;
        }
        noteRepo.delete(note);
        return APIMessage.OK;
    }
}