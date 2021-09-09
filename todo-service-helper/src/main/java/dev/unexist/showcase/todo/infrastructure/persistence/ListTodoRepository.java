/**
 * @package Showcase-Testing-Quarkus
 *
 * @file Todo repository
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.persistence;

import dev.unexist.showcase.todo.domain.todo.Todo;
import dev.unexist.showcase.todo.domain.todo.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ListTodoRepository implements TodoRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListTodoRepository.class);

    private final List<Todo> list;

    /**
     * Constructor
     **/

    ListTodoRepository() {
        this.list = new ArrayList<>();
    }

    @Override
    public boolean add(final Todo todo) {
        todo.setId(this.list.size() + 1);

        return this.list.add(todo);
    }

    @Override
    public boolean update(final Todo todo) {
        boolean ret = false;

        try {
            this.list.set(todo.getId(), todo);

            ret = true;
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("update: id={} not found", todo.getId());
        }

        return ret;
    }

    @Override
    public boolean deleteById(int id) {
        boolean ret = false;

        try {
            this.list.remove(id);

            ret = true;
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("deleteById: id={} not found", id);
        }

        return ret;
    }

    @Override
    public List<Todo> getAll() {
        return Collections.unmodifiableList(this.list);
    }

    @Override
    public Optional<Todo> findById(int id) {
        return this.list.stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }
}