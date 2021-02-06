/**
 * @package Quarkus-Testing-Showcase
 *
 * @file Todo application
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv2.
 * See the file COPYING for details.
 **/

package dev.unexist.showcase.todo.application;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class TodoMain {
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}