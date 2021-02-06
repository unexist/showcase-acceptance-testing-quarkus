/**
 * @package Quarkus-Testing-Showcase
 *
 * @file Todo cucumber test
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv2.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo.cucumber;

import io.quarkus.test.junit.QuarkusTest;

import java.lang.reflect.Field;
import java.net.URI;
import java.time.Clock;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.eventbus.EventBus;
import io.cucumber.core.feature.FeatureParser;
import io.cucumber.core.options.CommandlineOptionsParser;
import io.cucumber.core.options.RuntimeOptions;
import io.cucumber.core.options.RuntimeOptionsBuilder;
import io.cucumber.core.plugin.PluginFactory;
import io.cucumber.core.plugin.Plugins;
import io.cucumber.core.plugin.PrettyFormatter;
import io.cucumber.core.runner.Runner;
import io.cucumber.core.runtime.CucumberExecutionContext;
import io.cucumber.core.runtime.ExitStatus;
import io.cucumber.core.runtime.FeaturePathFeatureSupplier;
import io.cucumber.core.runtime.FeatureSupplier;
import io.cucumber.core.runtime.ObjectFactorySupplier;
import io.cucumber.core.runtime.ScanningTypeRegistryConfigurerSupplier;
import io.cucumber.core.runtime.TimeServiceEventBus;
import io.cucumber.core.runtime.TypeRegistryConfigurerSupplier;
import io.cucumber.java.JavaBackendProviderService;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestStepFinished;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

@QuarkusTest
public abstract class CucumberTest {
    private static String[] mainArgs = new String[]{};

    @TestFactory
    List<DynamicNode> getTests() {
        try {
            // We run in a different ClassLoader then "main", so we need to grab any cli arguments from the SystemClassLoader
            Class<?> aClass = ClassLoader.getSystemClassLoader().loadClass(CucumberTest.class.getName());
            Field aClassDeclaredField = aClass.getDeclaredField("mainArgs");
            aClassDeclaredField.setAccessible(true);
            CucumberTest.mainArgs = (String[]) aClassDeclaredField.get(aClass);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        EventBus eventBus = new TimeServiceEventBus(Clock.systemUTC(), UUID::randomUUID);

        final FeatureParser parser = new FeatureParser(eventBus::generateId);

        RuntimeOptionsBuilder commandlineOptionsParser = new CommandlineOptionsParser(System.out).parse(mainArgs);

        RuntimeOptionsBuilder runtimeOptionsBuilder = new RuntimeOptionsBuilder();
        runtimeOptionsBuilder.addDefaultFeaturePathIfAbsent();
        runtimeOptionsBuilder.addDefaultGlueIfAbsent();
        runtimeOptionsBuilder.addDefaultFormatterIfAbsent();
        runtimeOptionsBuilder.addDefaultSummaryPrinterIfAbsent();

        runtimeOptionsBuilder.addGlue(URI.create("classpath:/" + TodoSteps.class.getPackage().getName().replace(".", "/")));

        RuntimeOptions runtimeOptions = runtimeOptionsBuilder.build(commandlineOptionsParser.build());
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        FeatureSupplier featureSupplier = new FeaturePathFeatureSupplier(() -> contextClassLoader, runtimeOptions,
                parser);

        final Plugins plugins = new Plugins(new PluginFactory(), runtimeOptions);
        plugins.addPlugin(new PrettyFormatter(System.out));

        final ExitStatus exitStatus = new ExitStatus(runtimeOptions);
        plugins.addPlugin(exitStatus);
        if (runtimeOptions.isMultiThreaded()) {
            plugins.setSerialEventBusOnEventListenerPlugins(eventBus);
        } else {
            plugins.setEventBusOnEventListenerPlugins(eventBus);
        }
        ObjectFactory objectFactory = new CdiObjectFactory();

        ObjectFactorySupplier objectFactorySupplier = () -> objectFactory;

        TypeRegistryConfigurerSupplier typeRegistryConfigurerSupplier = new ScanningTypeRegistryConfigurerSupplier(() -> contextClassLoader,
                runtimeOptions);

        Runner runner = new Runner(eventBus,
                Collections.singleton(new JavaBackendProviderService().create(objectFactorySupplier.get(),
                        objectFactorySupplier.get(),
                        () -> contextClassLoader)),
                objectFactorySupplier.get(),
                typeRegistryConfigurerSupplier.get(),
                runtimeOptions);

        CucumberExecutionContext context = new CucumberExecutionContext(eventBus, exitStatus, () -> runner);

        List<DynamicNode> features = new LinkedList<>();
        features.add(DynamicTest.dynamicTest("Start Cucumber", context::startTestRun));

        featureSupplier.get().forEach(f -> {
            List<DynamicTest> tests = new LinkedList<>();
            tests.add(DynamicTest.dynamicTest("Start Feature", () -> context.beforeFeature(f)));
            f.getPickles().forEach(p -> tests.add(DynamicTest.dynamicTest(p.getName(), () -> {
                AtomicReference<TestStepFinished> resultAtomicReference = new AtomicReference<>();
                EventHandler<TestStepFinished> handler = event -> {
                    if (event.getResult().getStatus() != Status.PASSED) {
                        // save the first failed test step, so that we can get the line number of the cucumber file
                        resultAtomicReference.compareAndSet(null, event);
                    }
                };
                eventBus.registerHandlerFor(TestStepFinished.class, handler);
                context.runTestCase(r -> {
                    ClassLoader old = Thread.currentThread().getContextClassLoader();
                    Thread.currentThread().setContextClassLoader(contextClassLoader);
                    try {
                        r.runPickle(p);
                    } finally {
                        Thread.currentThread().setContextClassLoader(old);
                    }
                });
                eventBus.removeHandlerFor(TestStepFinished.class, handler);

                if (mainArgs.length == 0) {
                    // if we have no main arguments, we are running as part of a junit test suite, we need to fail the junit test explicitly
                    if (resultAtomicReference.get() != null) {
                        Assertions.fail(
                                "failed in " + f.getUri() + " at line " + ((PickleStepTestStep) resultAtomicReference.get().getTestStep()).getStep()
                                        .getLocation().getLine(),
                                resultAtomicReference.get().getResult().getError());
                    }
                }
            })));
            features.add(DynamicContainer.dynamicContainer(f.getName().orElse(f.getSource()), tests.stream()));
        });

        features.add(DynamicTest.dynamicTest("Finish Cucumber", context::finishTestRun));

        return features;
    }

    public static class CdiObjectFactory implements ObjectFactory {

        public CdiObjectFactory() {
        }

        public void start() {

        }

        public void stop() {

        }

        public boolean addClass(Class<?> clazz) {
            return true;
        }

        public <T> T getInstance(Class<T> type) {
            Instance<T> selected = CDI.current().select(type);
            if (selected.isUnsatisfied()) {
                throw new IllegalArgumentException(type.getName() + " is no CDI bean.");
            } else {
                return selected.get();
            }
        }
    }
}