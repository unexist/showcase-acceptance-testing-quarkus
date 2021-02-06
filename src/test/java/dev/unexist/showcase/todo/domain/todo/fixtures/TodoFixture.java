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

package dev.unexist.showcase.todo.domain.todo.fixtures;

import dev.unexist.showcase.todo.domain.todo.TodoBase;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class TodoFixture {
    private TodoBase todoBase;
    private RequestSpecification requestSpec;

    public void setTitle(String title) {
        requestSpec = new RequestSpecBuilder()
                .setPort(8081)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

        this.todoBase = new TodoBase();

        this.todoBase.setTitle(title);
    }

    public int status() {
        Response response = given(requestSpec)
                .when()
                .body(this.todoBase)
                .post("/todo");

        return response.getStatusCode();
    }
}
