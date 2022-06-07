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

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnit4StoryRunner;
import org.jbehave.core.junit.JUnitStories;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(JUnit4StoryRunner.class)
@Configure
@UsingEmbedder(generateViewAfterStories = true,
        ignoreFailureInStories = true, ignoreFailureInView = false)
@UsingSteps
public class TodoJBehaveFixture extends JUnitStories {

    public List<String> storyPaths() {
        return new
                StoryFinder().findPaths(CodeLocations.codeLocationFromPath("src/test/resources"),
                "stories/*.story", "");
    }
}
