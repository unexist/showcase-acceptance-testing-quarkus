/**
 * @package Showcase-Acceptance-Testing-Quarkus
 *
 * @file Todo JBehave steps
 * @copyright 2021-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import io.quarkus.arc.Unremovable;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.annotations.AsParameterConverter;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ApplicationScoped
@Unremovable
public class TodoStories {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setPort(8081)
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .build();

    ThreadLocal<TodoBase> todoBase;
    ThreadLocal<DueDate> dueDate;

    @BeforeScenario
    public void beforeScenario() {
        this.todoBase = new ThreadLocal<>();
        this.dueDate = new ThreadLocal<>();
    }

    @Given("I create a todo")
    public void given_create_todo() {
        this.todoBase.set(new TodoBase());
        this.dueDate.set(new DueDate());
    }

    /* Scenario 1 */

    @When("its title is <title>")
    public void when_set_title(@Named("title") String title) {
        this.todoBase.get().setTitle(title);
    }

    @When("its description is <description>")
    public void and_set_description(@Named("description") String description) {
        this.todoBase.get().setDescription(description);
    }

    @Then("its id should be <id>")
    public void then_get_id(@Named("id") int id) {
        String location = given(this.requestSpec)
                .when()
                .body(this.todoBase.get())
                .post("/todo")
                .then()
                .statusCode(201)
                .and()
                .extract().header("location");

        assertThat(location.substring(location.lastIndexOf("/") + 1))
                .isEqualTo(Integer.toString(id));
    }

    /* Scenario 2 */

    @When("it starts on <start>")
    public void when_set_start_date(@Named("start") String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.dueDate.get().setStart(LocalDate.parse(datestr, this.dtf));
        }
    }

    @When("it ends on <due>")
    public void and_set_due_date(@Named("due") String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.dueDate.get().setDue(LocalDate.parse(datestr, this.dtf));
        }
    }

    @Then("it should be marked as <status>")
    public void then_get_status(@Named("status") boolean status) {
        this.todoBase.get().setDueDate(this.dueDate.get());

        assertThat(status).isEqualTo(this.todoBase.get().getDone());
    }

    @AsParameterConverter
    public boolean status(String status) {
        return "done".equalsIgnoreCase(status);
    }
}