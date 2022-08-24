/**
 * @package Showcase-Acceptance-Testing-Quarkus
 *
 * @file Todo cucumber steps
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.quarkus.arc.Unremovable;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ApplicationScoped
@Unremovable
public class TodoSteps {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private RequestSpecification requestSpec;
    private TodoBase todoBase;
    private DueDate dueDate;

    @Before
    public void beforeScenario() {
        this.requestSpec = new RequestSpecBuilder()
                .setPort(8081)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    @Given("I create a todo")
    public void given_create_todo() {
        this.todoBase = new TodoBase();
        this.dueDate = new DueDate();
    }

    /* Scenario 1 */

    @When("its title is {string}")
    public void when_set_title(String title) {
        this.todoBase.setTitle(title);
    }

    @And("its description is {string}")
    public void and_set_description(String description) {
        this.todoBase.setDescription(description);
    }

    @Then("its id should be {int}")
    public void then_get_id(int id) {
        String location = given(this.requestSpec)
            .when()
                .body(this.todoBase)
                .post("/todo")
            .then()
                .statusCode(201)
            .and()
                .extract().header("location");

        assertThat(location.substring(location.lastIndexOf("/") + 1))
                .isEqualTo(Integer.toString(id));
    }

    /* Scenario 2 */

    @When("it starts on {string}")
    public void when_set_start_date(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.dueDate.setStart(LocalDate.parse(datestr, this.dtf));
        }
    }

    @And("it ends on {string}")
    public void and_set_due_date(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.dueDate.setDue(LocalDate.parse(datestr, this.dtf));
        }
    }

    @Then("it should be marked as {status}")
    public void then_get_status(boolean status) {
        this.todoBase.setDueDate(this.dueDate);

        assertThat(status).isEqualTo(this.todoBase.getDone());
    }

    @ParameterType("done|undone")
    public boolean status(String status) {
        return "done".equalsIgnoreCase(status);
    }
}