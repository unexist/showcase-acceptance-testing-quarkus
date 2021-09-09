/**
 * @package Quarkus-Testing-Showcase
 *
 * @file Todo cucumber extension
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import io.cucumber.core.backend.ObjectFactory;
import io.quarkus.arc.Unremovable;
import io.quarkus.bootstrap.app.QuarkusBootstrap;
import io.quarkus.bootstrap.app.RunningQuarkusApplication;
import io.quarkus.bootstrap.model.PathsCollection;
import io.quarkus.test.common.PathTestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.net.BindException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CdiObjectFactory implements ObjectFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(CdiObjectFactory.class);
    private RunningQuarkusApplication application;

    public CdiObjectFactory() {
        startApplication();
    }

    public void start() {}

    public void stop() {}

    public boolean addClass(Class<?> stepClass) {
        if (stepClass.getAnnotation(ApplicationScoped.class) == null) {
            throw new RuntimeException(
                    "You need to register your step definitions with @ApplicationScoped, otherwise"
                    + "Arc container does not register them as beans."
            );
        }
        if (stepClass.getAnnotation(Unremovable.class) == null) {
            throw new RuntimeException(
                    "You need to register your step definitions with @Unremovable, otherwise Arc"
                    + "container may remove the beans at runtime"
            );
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> type) {
        if (null == this.application) {
            throw new RuntimeException("Application has not been successfully started");
        }
        try {
            return (T) this.application.instance(type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startApplication() {
        try {
            PathsCollection.Builder rootBuilder = PathsCollection.builder();

            Path testClassLocation = PathTestHelper.getTestClassesLocation(CdiObjectFactory.class);

            /* Load step definitions */
            rootBuilder.add(testClassLocation);

            /* Load application classes */
            final Path appClassLocation = PathTestHelper.getAppClassLocationForTestLocation(
                    testClassLocation.toString());
            rootBuilder.add(appClassLocation);

            this.application = QuarkusBootstrap.builder()
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