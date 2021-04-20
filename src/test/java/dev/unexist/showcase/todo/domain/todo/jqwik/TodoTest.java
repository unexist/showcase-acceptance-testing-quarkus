/**
 * @package Quarkus-Testing-Showcase
 *
 * @file Contrived tests with PBT
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv3.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo.jqwik;

import dev.unexist.showcase.todo.domain.todo.DueDate;
import dev.unexist.showcase.todo.domain.todo.Todo;
import dev.unexist.showcase.todo.domain.todo.TodoBase;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.PropertyDefaults;
import net.jqwik.api.constraints.IntRange;
import org.assertj.core.api.Condition;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;

@PropertyDefaults(tries = 10)
public class TodoTest {
    private static final int FUTURE_TIME = 1741598467;

    @Property
    public void testCreateTodo(@ForAll String anyStr) {
        Todo todo = new Todo();

        todo.setTitle(anyStr);
        todo.setDescription(anyStr);

        assertThat(todo.getTitle()).isNotNull();
        assertThat(todo.getDescription()).isNotNull();
    }

    @Property
    public void testCreateTodoWithDate(@ForAll String anyStr,
                                       @ForAll @IntRange(min = TodoTest.FUTURE_TIME) int unixtime)
    {
        Todo todo = new Todo();

        todo.setTitle(anyStr);
        todo.setDescription(anyStr);

        DueDate dueDate = new DueDate();

        dueDate.setStart(LocalDate.now());
        dueDate.setDue(Instant.ofEpochMilli(unixtime * 1000L)
                .atZone(ZoneId.systemDefault()).toLocalDate());

        todo.setDueDate(dueDate);

        /* Arbitrary and contrived test */
        Condition<Todo> cond1 = new Condition<>(t ->
                t.getDueDate().getStart().isBefore(t.getDueDate().getDue()),
                "Start date is before due date");
        Condition<Todo> cond2 = new Condition<>(TodoBase::getDone,
                "Todo must not be done");

        assertThat(todo).is(allOf(cond1, cond2));
    }
}
