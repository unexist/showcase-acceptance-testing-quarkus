/**
 * @package Showcase-Acceptance-Testing-Quarkus
 *
 * @file Todo JBehave extension
 * @copyright 2021-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import net.serenitybdd.jbehave.SerenityStories;
import net.serenitybdd.jbehave.runners.SerenityReportingRunner;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.runner.RunWith;

import java.util.List;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;

@RunWith(SerenityReportingRunner.class)
public class TodoJBehaveFixture extends SerenityStories {

    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withDefaultFormats()
                        .withFormats(CONSOLE, HTML));
    }

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
