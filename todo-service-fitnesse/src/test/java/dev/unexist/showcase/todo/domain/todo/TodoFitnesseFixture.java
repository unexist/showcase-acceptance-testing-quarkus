/**
 * @package Showcase-Testing-Quarkus
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
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TodoFitnesseFixture {
    private TodoBase todoBase;
    private DateTimeFormatter dtf;
    private RequestSpecification requestSpec;

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
        this.dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.requestSpec = new RequestSpecBuilder()
                .setPort(8080)
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
     * Set the start date as string
     *
     * @param  datestr  The date string, this is parsed and converted to {@link LocalDate}
     **/


    public void setStartDate(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.getOrCreateDueDate()
                    .setStart(LocalDate.parse(datestr, this.dtf));
        }
    }

    /**
     * Set the due date as string
     *
     * @param  datestr  The date string, this is parsed and converted to {@link LocalDate}
     **/

    public void setDueDate(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.getOrCreateDueDate()
                    .setDue(LocalDate.parse(datestr, this.dtf));
        }
    }

    /**
     * Set the done state
     *
     * @param  isDone  The state to set, whether it is done or not
     **/

    public void setDone(Boolean isDone) {
        this.todoBase.setDone(isDone);
    }

    /**
     * Get the status code of the call
     *
     * @return The status code
     **/

    public int status() {
        Response response = given(requestSpec)
                .when()
                .body(this.todoBase)
                .post("/todo");

        return response.getStatusCode();
    }

    /**
     * Convenience method to get a valid {@link DueDate}
     *
     * @return Either an existing instance or creates a new one
     **/

    private DueDate getOrCreateDueDate() {
        if (null == this.todoBase.getDueDate()) {
            this.todoBase.setDueDate(new DueDate());
        }

        return this.todoBase.getDueDate();
    }
}
