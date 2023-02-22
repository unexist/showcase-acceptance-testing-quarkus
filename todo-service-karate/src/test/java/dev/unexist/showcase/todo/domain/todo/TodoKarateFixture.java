/**
 * @package Showcase-Acceptance-Testing-Quarkus
 *
 * @file Todo karate extension
 * @copyright 2021-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import com.intuit.karate.junit5.Karate;

public class TodoKarateFixture {

    @Karate.Test
    Karate shouldValidateTodo() {
        return Karate.run("todo").relativeTo(getClass());
    }
}
