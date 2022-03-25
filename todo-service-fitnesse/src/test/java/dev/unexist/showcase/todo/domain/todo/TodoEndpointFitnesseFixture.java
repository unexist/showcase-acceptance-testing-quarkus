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

import io.quarkus.bootstrap.app.QuarkusBootstrap;
import io.quarkus.bootstrap.app.RunningQuarkusApplication;
import io.quarkus.bootstrap.model.PathsCollection;
import io.quarkus.test.common.PathTestHelper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.BindException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TodoEndpointFitnesseFixture {
    private static final Logger LOGGER = LoggerFactory.getLogger(TodoEndpointFitnesseFixture.class);
    private static RunningQuarkusApplication application;
    private RequestSpecification requestSpec;
    private TodoBase todoBase;

    /* Slim lifecycle (http://fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM.DecisionTable) */

    /**
     * This method is called once after the constructor and receives a list of all cells
     **/

    public void table(List<List<String>> table) {
        startApplication();
    }

    /**
     * This method is called after the table method for e.g. initialization
     **/

    public void beginTable() {
        this.todoBase = new TodoBase();

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

    public void endTable() throws Exception {
        stopApplication();
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

    private static void startApplication() {
        try {
            PathsCollection.Builder rootBuilder = PathsCollection.builder();

            Path testClassLocation = PathTestHelper.getTestClassesLocation(TodoStatusFitnesseFixture.class);

            /* Load step definitions */
            rootBuilder.add(testClassLocation);

            /* Load application classes */
            final Path appClassLocation = PathTestHelper.getAppClassLocationForTestLocation(
                    testClassLocation.toString());
            rootBuilder.add(appClassLocation);

            application = QuarkusBootstrap.builder()
                    .setIsolateDeployment(false)
                    .setMode(QuarkusBootstrap.Mode.TEST)
                    .setProjectRoot(Paths.get("").normalize().toAbsolutePath())
                    .setApplicationRoot(rootBuilder.build())
                    .build()
                    .bootstrap()
                    .createAugmentor()
                    .createInitialRuntimeApplication()
                    .run();
            LOGGER.info("Quarkus is starting..");
        } catch (BindException e) {
            /* If Quarkus is already running - fine */
            LOGGER.error("Address already in use - which is fine!", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void stopApplication() throws Exception {
        application.close();
    }
}
