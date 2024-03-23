/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 *
 */

// Write your code here
package com.example.todo.service;

import com.example.todo.repository.TodoRepository;
import com.example.todo.model.TodoRowMapper;
import com.example.todo.model.Todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

@Service
public class TodoH2Service implements TodoRepository {

    @Autowired
    public JdbcTemplate db;

    @Override
    public ArrayList<Todo> getTodoList() {
        List<Todo> todoList = db.query("SELECT * FROM todolist", new TodoRowMapper());
        ArrayList<Todo> todos = new ArrayList<>(todoList);
        return todos;
    }

    @Override
    public Todo getTodo(int id) {
        try {
            Todo todo = db.queryForObject("SELECT * FROM todolist WHERE id = ?", new TodoRowMapper(), id);
            return todo;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Todo addTodo(Todo todo) {
        db.update("INSERT INTO todolist(todo, status, priority) VALUES(?,?,?)", todo.getTodo(), todo.getStatus(),
                todo.getPriority());
        Todo savedTodo = db.queryForObject("SELECT * FROM todolist WHERE todo = ? and status = ?", new TodoRowMapper(),
                todo.getTodo(), todo.getStatus());
        return savedTodo;
    }

    @Override
    public Todo updateTodo(int id, Todo todo) {
        if (todo.getTodo() != null) {
            db.update("UPDATE todolist SET todo = ? WHERE id = ?", todo.getTodo(), id);
        }
        if (todo.getStatus() != null) {
            db.update("UPDATE todolist SET status = ? WHERE id =?", todo.getStatus(), id);
        }
        if (todo.getPriority() != null) {
            db.update("UPDATE todolist SET priority = ? WHERE id = ?", todo.getPriority(), id);
        }
        return getTodo(id);
    }

    @Override
    public void deleteTodo(int id) {
        db.update("DELETE FROM todolist WHERE id = ?", id);
    }

}
