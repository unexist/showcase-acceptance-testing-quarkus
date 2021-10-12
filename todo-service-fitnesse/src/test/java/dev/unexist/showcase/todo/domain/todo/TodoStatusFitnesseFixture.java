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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.BindException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TodoStatusFitnesseFixture {
    private static final Logger LOGGER = LoggerFactory.getLogger(TodoStatusFitnesseFixture.class);
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static RunningQuarkusApplication application;
    private TodoBase todoBase;
    private DueDate dueDate;

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
    }

    /**
     * This method is called once for each row
     **/

    public void reset() {
       this.todoBase = new TodoBase();
       this.dueDate = new DueDate();
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
        this.application.close();
    }

    /* Tests */

    public void setStart(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.dueDate.setStart(LocalDate.parse(datestr, this.dtf));
        }
    }

    /**
     * Set the due date as string
     *
     * @param  datestr  The date string, this is parsed and converted to {@link LocalDate}
     **/

    public void setDue(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.dueDate.setDue(LocalDate.parse(datestr, this.dtf));
        }
    }

    /**
     * Get the status code of the call
     *
     * @return The status code
     **/

    public String status() {
        this.todoBase.setDueDate(this.dueDate);

        return this.todoBase.getDone() ? "done" : "undone";
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
        } catch (BindException e) {
            /* If Quarkus is already running - fine */
            LOGGER.error("Address already in use - which is fine!", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
