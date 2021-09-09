/**
 * @package Showcase-Testing-Quarkus
 *
 * @file Todo application
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.application;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;


@QuarkusMain
public class TodoMain {
    @SuppressWarnings("checkstyle:UncommentedMain")
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}