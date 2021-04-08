/**
 * @package Quarkus-Testing-Showcase
 *
 * @file Contrived tests with PBT
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv2.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo.faker;

import com.github.javafaker.Faker;
import dev.unexist.showcase.todo.domain.todo.Todo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoTest {

    @Test
    public void testCreateTodo() {
        Faker faker = new Faker();
        Todo todo = new Todo();

        todo.setTitle(faker.beer().hop());
        todo.setDescription(faker.beer().malt());

        assertThat(todo.getTitle()).isNotNull();
        assertThat(todo.getDescription()).isNotNull();
    }
}
