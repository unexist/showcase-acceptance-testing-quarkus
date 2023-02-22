/**
 * @package Showcase-Acceptance-Testing-Quarkus
 *
 * @file Test Suite
 * @copyright 2021-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/
 
package dev.unexist.showcase.todo;

import dev.unexist.showcase.todo.adapter.TodoResourceFixture;
import dev.unexist.showcase.todo.domain.todo.TodoKarateFixture;
import io.quarkus.bootstrap.app.QuarkusBootstrap;
import io.quarkus.bootstrap.app.RunningQuarkusApplication;
import io.quarkus.bootstrap.model.PathsCollection;
import io.quarkus.test.common.PathTestHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.BindException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Suite
@SelectClasses({ TodoResourceFixture.class, TodoKarateFixture.class })
public class TestSuite {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestSuite.class);
    private static RunningQuarkusApplication application;

    @BeforeAll
    public static void setUp() {
        startApplication();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        application.close();
    }

    private static void startApplication() {
        try {
            PathsCollection.Builder rootBuilder = PathsCollection.builder();

            Path testClassLocation = PathTestHelper.getTestClassesLocation(
                    TodoKarateFixture.class);

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
