/**
 * @package Showcase-Testing-Quarkus
 *
 * @file Contrived tests with PBT
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoFakerTest {

    @Test
    public void testCreateTodo() {
        Faker faker = new Faker();
        Todo todo = new Todo();

        todo.setTitle(faker.beer().hop());
        todo.setDescription(faker.beer().malt());

        assertThat(todo.getTitle()).isNotNull();
        assertThat(todo.getDescription()).isNotNull();
    }

    @Test
    public void testCreateTodoWithDate() {
        Faker faker = new Faker();
        Todo todo = new Todo();

        DueDate dd = new DueDate();

        dd.setStart(convertDateToLocalDate(faker.date().birthday()));
        dd.setDue(convertDateToLocalDate(faker.date().birthday()));

        todo.setDueDate(dd);

        assertThat(todo.getDueDate()).isNotNull();
    }

    private LocalDate convertDateToLocalDate(final Date date) {
        return date.toInstant().atZone(
                ZoneId.systemDefault()).toLocalDate();
    }
}
