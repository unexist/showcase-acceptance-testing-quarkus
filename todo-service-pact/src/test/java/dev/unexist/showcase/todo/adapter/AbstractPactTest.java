/**
 * @package Showcase-Testing-Quarkus
 *
 * @file Helper for pact tests
 * @copyright 2021 Christoph Kappel <unexist@subforge.org>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.adapter;

import au.com.dius.pact.core.support.expressions.ValueResolver;
import io.quarkus.bootstrap.app.QuarkusBootstrap;
import io.quarkus.bootstrap.app.RunningQuarkusApplication;
import io.quarkus.bootstrap.model.PathsCollection;
import io.quarkus.test.common.PathTestHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.BindException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AbstractPactTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPactTest.class);
    public static final String PACT_URL = "https://localhost:9292";
    public static final String SYSENV_PACTBROKER_URL = "pactbroker.url";

    private static RunningQuarkusApplication APP;

    protected static void startApplication() {
        try {
            PathsCollection.Builder rootBuilder = PathsCollection.builder();

            Path testClassLocation = PathTestHelper.getTestClassesLocation(AbstractPactTest.class);

            rootBuilder.add(testClassLocation);

            /* Load application classes */
            final Path appClassLocation = PathTestHelper.getAppClassLocationForTestLocation(testClassLocation.toString());
            rootBuilder.add(appClassLocation);

            QuarkusBootstrap.builder()
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

    public static class PactValueResolver implements ValueResolver {
        @Override
        public boolean propertyDefined(@NotNull String key) {
            return key.startsWith(SYSENV_PACTBROKER_URL);
        }

        @Nullable
        @Override
        public String resolveValue(@Nullable String key) {
            String value = null;

            if (null != key && key.startsWith(SYSENV_PACTBROKER_URL)) {
                String envValue = System.getenv(SYSENV_PACTBROKER_URL);

                value = (null != envValue ? envValue : PACT_URL);
            }

            return value;
        }

        @Nullable
        @Override
        public String resolveValue(@Nullable String key, @Nullable String s1) {
            return this.resolveValue(key);
        }
    }
}
