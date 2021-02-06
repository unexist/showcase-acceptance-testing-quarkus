/**
 * @package Quarkus-Testing-Showcase
 *
 * @file Todo cucumber steps
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv2.
 * See the file COPYING for details.
 **/

package dev.unexist.showcase.todo.domain.todo.cucumber;

import dev.unexist.showcase.todo.domain.todo.DueDate;
import dev.unexist.showcase.todo.domain.todo.TodoBase;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;

@ApplicationScoped
public class TodoSteps {
    private TodoBase todoBase;
    private Response response;
    private DateTimeFormatter dtf;
    private RequestSpecification requestSpec;

    @Before
    public void beforeScenario() {
        this.todoBase = new TodoBase();
        this.dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.requestSpec = new RequestSpecBuilder()
                .setPort(8081)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    private DueDate getOrCreateDueDate() {
        if (null == this.todoBase.getDueDate()) {
            this.todoBase.setDueDate(new DueDate());
        }

        return this.todoBase.getDueDate();
    }

    @Given("I imagine a todo {string}")
    public void given_set_title(String title) {
        this.todoBase.setTitle(title);
    }

    @And("a description of {string}")
    public void given_set_description(String description) {
        this.todoBase.setDescription(description);
    }

    @And("starting on {string}")
    public void given_set_start_date(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.getOrCreateDueDate()
                    .setStart(LocalDate.parse(datestr, this.dtf));
        }
    }

    @And("lasting no longer than {string}")
    public void given_set_due_date(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.getOrCreateDueDate()
                    .setDue(LocalDate.parse(datestr, this.dtf));
        }
    }

    @ParameterType(value = "true|True|TRUE|false|False|FALSE")
    public Boolean booleanValue(String value) {
        return Boolean.valueOf(value);
    }

    @And("still not {string}")
    public void given_set_done(String isDone) {
        this.todoBase.setDone(Boolean.valueOf(isDone));
    }

    @When("I ask for the status code")
    public void when_get_status() {
        response = given(this.requestSpec)
                .when()
                    .body(todoBase)
                    .post("/todo");
    }

    @Then("I should be told {string}")
    public void then_get_status_code(String status) {
        response.then()
                .statusCode(Integer.parseInt(status));
    }
}
