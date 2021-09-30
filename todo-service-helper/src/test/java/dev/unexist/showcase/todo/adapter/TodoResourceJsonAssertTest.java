/**
 * @package Showcase-Testing-Quarkus
 *
 * @file Stupid integration test
 * @copyright 2019 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.adapter;

import com.github.javafaker.Faker;
import dev.unexist.showcase.todo.domain.todo.DueDate;
import dev.unexist.showcase.todo.domain.todo.TodoBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@QuarkusTest
public class TodoResourceJsonAssertTest {

    @Test
    public void testTodoPostToEndpoint() {
        Faker faker = new Faker();
        TodoBase todo = new TodoBase();

        todo.setTitle(faker.beer().hop());
        todo.setDescription(faker.beer().malt());

        DueDate dd = new DueDate();

        dd.setStart(convertDateToLocalDate(faker.date().birthday()));
        dd.setDue(convertDateToLocalDate(faker.date().birthday()));

        todo.setDueDate(dd);

        given()
          .when()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(todo)
                .post("/todo")
          .then()
             .statusCode(201);
    }

    @Test
    public void testTodoGetFromEndpoint() {
        String jsonOut = given()
            .when()
                .accept(MediaType.APPLICATION_JSON)
                .get("/todo")
            .then()
                .statusCode(200)
                .extract()
                .asString();

        /* Check media type */
        assertThatJson(jsonOut)
                .inPath("mediaType")
                    .isObject()
                        .containsEntry("type", "application")
                        .containsEntry("subtype", "json");

        /* Check id */
        assertThatJson(jsonOut)
                .inPath("entity")
                    .isArray()
                    .isNotEmpty()
                    .first()
                        .hasFieldOrPropertyWithValue("id", BigDecimal.valueOf(1));

        System.out.println(jsonOut);
    }

    private LocalDate convertDateToLocalDate(final Date date) {
        return date.toInstant().atZone(
                ZoneId.systemDefault()).toLocalDate();

    }
}
