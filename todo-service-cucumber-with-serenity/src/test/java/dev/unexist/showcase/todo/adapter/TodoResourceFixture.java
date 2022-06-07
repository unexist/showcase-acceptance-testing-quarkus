/**
 * @package Showcase-Acceptance-Testing-Quarkus
 *
 * @file Stupid integration test
 * @copyright 2019 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.adapter;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static io.restassured.RestAssured.given;

@RunWith(JUnit4.class)
public class TodoResourceFixture {
    private RequestSpecification requestSpec;

    @Before
    public void before() {
        this.requestSpec = new RequestSpecBuilder()
                .setPort(8081)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    @Test
    public void shouldGetEmptyResult() {
        given(this.requestSpec)
          .when().get("/todo")
          .then()
             .statusCode(204);
    }
}