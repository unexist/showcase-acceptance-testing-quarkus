/**
 * @package Showcase-Acceptance-Testing-Quarkus
 *
 * @file Todo fitness fixture
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TodoEndpointFitnesseFixture {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private RequestSpecification requestSpec;
    private TodoBase todoBase;
    private DueDate dueDate;

    /* Slim lifecycle (http://fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM.DecisionTable) */

    /**
     * This method is called once after the constructor and receives a list of all cells
     **/

    public void table(List<List<String>> table) {
    }

    /**
     * This method is called after the table method for e.g. initialization
     **/

    public void beginTable() {
        this.todoBase = new TodoBase();
        this.dueDate = new DueDate();

        this.requestSpec = new RequestSpecBuilder()
                .setPort(8081)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    /**
     * This method is called once for each row
     **/

    public void reset() {
       this.todoBase = new TodoBase();
    }

    /**
     * This method is called once for each row after all set functions
     **/

    public void execute() {
    }

    /**
     * This method is called after the last row has been processed
     **/

    public void endTable() {
    }

    /* Tests */

    /**
     * Set the title
     *
     * @param  title  The title to set
     **/

    public void setTitle(String title) {
        this.todoBase.setTitle(title);
    }

    /**
     * Set the description
     *
     * @param  description  The description to set
     **/

    public void setDescription(String description) {
        this.todoBase.setDescription(description);
    }

    /**
     * Get the status id of the newly added {@link Todo}
     *
     * @return The id
     **/

    public int id() {
        String location = given(this.requestSpec)
            .when()
                .body(this.todoBase)
                .post("/todo")
            .then()
                .statusCode(201)
            .and()
                .extract().header("location");

        return Integer.parseInt(location.substring(location.lastIndexOf("/") + 1));
    }
}
