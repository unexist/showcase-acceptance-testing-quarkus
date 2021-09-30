/**
 * @package Showcase-Testing-Quarkus
 *
 * @file Test Suite
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/
 
package dev.unexist.showcase.todo;

import dev.unexist.showcase.todo.adapter.TodoResourceFixture;
import dev.unexist.showcase.todo.domain.todo.TodoCucumberFixture;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TodoResourceFixture.class,
        TodoCucumberFixture.class
})
public class TestSuite {
}
