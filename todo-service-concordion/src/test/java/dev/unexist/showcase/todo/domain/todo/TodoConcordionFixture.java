/**
 * @package Showcase-Test-Quarkus
 *
 * @file Todo concordion test
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/
 
package dev.unexist.showcase.todo.domain.todo;

import dev.unexist.showcase.todo.infrastructure.persistence.ListTodoRepository;
import org.apache.commons.lang3.BooleanUtils;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class TodoConcordionFixture {

    TodoRepository todoRepository = new ListTodoRepository();

    public TodoBase create(final String title, final String description) {
        TodoBase base = new Todo();

        base.setTitle(title);
        base.setDescription(description);

        return base;
    }

    public Todo save(TodoBase base) {
        Todo todo = new Todo(base);

        this.todoRepository.add(todo);

        return todo;
    }

    public Todo createAndSave(final String title, final String description) {
        return this.save(this.create(title, description));
    }

    public String isDone(final Todo todo) {
        return BooleanUtils.isTrue(todo.getDone()) ? "yes" : "no";
    }
}
