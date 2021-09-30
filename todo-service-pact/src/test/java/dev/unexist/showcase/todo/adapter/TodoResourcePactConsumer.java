/**
 * @package Showcase-Testing-Quarkus
 *
 * @file Pact test consumer
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.adapter;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.unexist.showcase.todo.domain.todo.DueDate;
import dev.unexist.showcase.todo.domain.todo.TodoBase;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "Todo-Provider")
public class TodoResourcePactConsumer extends AbstractPactTest {

    @Pact(consumer = "Todo-Consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
                .given("a running server")
                .uponReceiving("POST /todo")
                .path("/todo")
                .method("POST")
                .body(new PactDslJsonBody()
                    .stringValue("title", "string")
                    .stringValue("description", "string")
                    .booleanType("done", true)
                    .object("dueDate")
                        .stringValue("due", "2021-05-07")
                        .stringValue("start", "2021-05-07")
                )
                .willRespondWith()
                .status(201)
                .toPact();
    }

    @Test
    void todoCheckPactTest(MockServer mockServer) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String endpoint = mockServer.getUrl() + "/todo";

        HttpResponse response = Request.Post(endpoint)
                .bodyString(mapper.writeValueAsString(createTodo()), ContentType.APPLICATION_JSON)
                .execute().returnResponse();

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201);
    }

    private TodoBase createTodo() {
        TodoBase todo = new TodoBase();

        todo.setTitle("string");
        todo.setDescription("string");
        todo.setDone(true);

        DueDate due = new DueDate();

        due.setStart(LocalDate.parse("2021-05-07"));
        due.setDue(LocalDate.parse("2021-05-07"));

        todo.setDueDate(due);

        return todo;
    }
}