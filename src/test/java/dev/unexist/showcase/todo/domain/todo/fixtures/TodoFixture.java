/**
 * @package Quarkus-Testing-Showcase
 *
 * @file Todo fitness fixture
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv2.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo.fixtures;

import dev.unexist.showcase.todo.domain.todo.DueDate;
import dev.unexist.showcase.todo.domain.todo.TodoBase;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;

public class TodoFixture {
    private TodoBase todoBase;
    private DateTimeFormatter dtf;
    private RequestSpecification requestSpec;

    /* Slim Lifecycle http://fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM.DecisionTable */

    public void beginTable() {
        this.todoBase = new TodoBase();
        this.dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.requestSpec = new RequestSpecBuilder()
                .setPort(8080)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public void endTable() {
    }

    public void execute() {

    }

    public void reset() {
       this.todoBase = new TodoBase();
    }

    /* Tests */

    private DueDate getOrCreateDueDate() {
        if (null == this.todoBase.getDueDate()) {
            this.todoBase.setDueDate(new DueDate());
        }

        return this.todoBase.getDueDate();
    }

    public void setTitle(String title) {
        this.todoBase.setTitle(title);
    }

    public void setDescription(String description) {
        this.todoBase.setDescription(description);
    }


    public void setStartDate(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.getOrCreateDueDate()
                    .setStart(LocalDate.parse(datestr, this.dtf));
        }
    }

    public void setDueDate(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.getOrCreateDueDate()
                    .setDue(LocalDate.parse(datestr, this.dtf));
        }
    }

    public void setDone(Boolean isDone) {
        this.todoBase.setDone(isDone);
    }

    public int status() {
        Response response = given(requestSpec)
                .when()
                .body(this.todoBase)
                .post("/todo");

        return response.getStatusCode();
    }
}
