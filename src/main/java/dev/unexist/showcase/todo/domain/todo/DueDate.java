/**
 * @package Quarkus-Testing-Showcase
 *
 * @file DueDate class
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv3.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.unexist.showcase.todo.infrastructure.serializer.DateSerializer;

import java.time.LocalDate;

public class DueDate {
    @JsonSerialize(using = DateSerializer.class)
    private LocalDate start;

    @JsonSerialize(using = DateSerializer.class)
    private LocalDate due;

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }
}
