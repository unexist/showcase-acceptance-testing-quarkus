/**
 * @package Quarkus-Testing-Showcase
 *
 * @file Todo cucumber
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv2.
 * See the file COPYING for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class TodoCucumberSteps {
    private String title;
    private Response response;
    private RequestSpecification requestSpec;

    @Before
    public void beforeScenario() {
        requestSpec = new RequestSpecBuilder().build();
    }

    @Given("New title is {string}")
    public void given_I_set_new_title(String title) {
        this.title = title;
    }

    @When("I ask to create a todo")
    public void when_I_ask_to_create_a_todo() {
        TodoBase base = new TodoBase();

        base.setTitle(this.title);

        response = given(requestSpec)
                .when()
                    .body(base)
                    .post("/todo");
    }

    @Then("I should be told {string}")
    public void then_I_should_be_told(String string) {
        response.then()
                .statusCode(Integer.parseInt(string));
    }
}
