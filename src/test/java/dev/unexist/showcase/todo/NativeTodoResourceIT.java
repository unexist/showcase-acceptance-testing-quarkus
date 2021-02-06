/**
 * @package Quarkus-Testing-Showcase
 *
 * @file Stupid native test
 * @copyright 2019 Christoph Kappel <unexist@subforge.org>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv2.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo;

import dev.unexist.showcase.todo.application.TodoResourceTest;
import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeTodoResourceIT extends TodoResourceTest {

    // Execute the same tests but in native mode.
}