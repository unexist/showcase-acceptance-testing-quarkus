/**
 * @package Showcase-Acceptance-Testing-Quarkus
 *
 * @file Todo cucumber extension
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnit4StoryRunner;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(JUnit4StoryRunner.class)
public class TodoJBehaveFixture extends JUnitStories {

    public TodoJBehaveFixture() {
        configuredEmbedder().embedderControls().doGenerateViewAfterStories(true).doIgnoreFailureInStories(true)
                .doIgnoreFailureInView(true).useThreads(2);
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration();
    }

    /*@Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass = this.getClass();
        LoadFromClasspath resourceLoader = new LoadFromClasspath(embeddableClass);
        TableTransformers tableTransformers = new TableTransformers();
        ParameterControls parameterControls = new ParameterControls();
        // Start from default ParameterConverters instance
        ParameterConverters parameterConverters = new ParameterConverters(resourceLoader, tableTransformers);
        // factory to allow parameter conversion and loading from external resources (used by StoryParser too)
        ExamplesTableFactory examplesTableFactory = new ExamplesTableFactory(resourceLoader,tableTransformers);
        // add custom converters
        parameterConverters.addConverters(new ParameterConverters.DateConverter(new SimpleDateFormat("yyyy-MM-dd")),
                new ParameterConverters.ExamplesTableConverter(examplesTableFactory));
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(embeddableClass))
                .useStoryParser(new RegexStoryParser(examplesTableFactory))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass))
                        .withDefaultFormats()
                        .withFormats(CONSOLE, TXT, HTML, XML))
                .useParameterConverters(parameterConverters)
                .useParameterControls(parameterControls)
                .useTableTransformers(tableTransformers);
    }*/

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new TodoStories());
    }

    @Override
    public List<String> storyPaths() {
        return new
                StoryFinder().findPaths(CodeLocations.codeLocationFromPath("src/test/resources"),
                "stories/*.story", "");
    }
}
