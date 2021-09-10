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
import org.apache.commons.lang3.StringUtils;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RunWith(ConcordionRunner.class)
public class TodoConcordionFixture {
    TodoRepository todoRepository = new ListTodoRepository();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Create new {@link TodoBase}
     *
     * @param  title        The title of the entry
     * @param  description  The description of the entry
     *
     * @return A newly created {@link TodoBase}
     **/

    public TodoBase create(final String title, final String description) {
        TodoBase base = new Todo();

        base.setTitle(title);
        base.setDescription(description);

        return base;
    }

    /**
     * Save a {@link TodoBase} into the repository
     *
     * @param  base  A {@link TodoBase}
     *
     * @return A newly created {@link Todo}
     **/

    public Todo save(TodoBase base) {
        Todo todo = new Todo(base);

        this.todoRepository.add(todo);

        return todo;
    }


    /**
     * Convenience method to create a save a {@link Todo}
     *
     * @param  title        The title of the entry
     * @param  description  The description of the entry
     *
     * @return A newly created {@link Todo}
     **/

    public Todo createAndSave(final String title, final String description) {
        return this.save(this.create(title, description));
    }

    /**
     * Convenience method to create a {@link Todo} from dates
     *
     * @param  start  The start date of the entry
     * @param  due    The due date of the entry
     *
     * @return A newly created {@link Todo}
     **/

    public Todo createWithDate(final String start, final String due) {
        Todo todo = new Todo();

        todo.setTitle("Test");
        todo.setDescription("Test");

        DueDate dueDate = new DueDate();

        dueDate.setStart(LocalDate.parse(StringUtils.strip(start), this.dtf));
        dueDate.setDue(LocalDate.parse(StringUtils.strip(due), this.dtf));

        todo.setDueDate(dueDate);

        return todo;
    }

    /**
     * Convert done state to yes or no
     * @param  todo  The {@link Todo} to check
     *
     * @return Either {@code yes} if the entry is done; otherwise {@code no}
     **/

    public String isDone(final Todo todo) {
        return BooleanUtils.isTrue(todo.getDone()) ? "yes" : "no";
    }
}
