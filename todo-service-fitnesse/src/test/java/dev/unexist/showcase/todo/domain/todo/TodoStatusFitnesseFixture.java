/**
 * @package Showcase-Acceptance-Testing-Quarkus
 *
 * @file Todo fitness fixture
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TodoStatusFitnesseFixture {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private TodoBase todoBase;
    private DueDate dueDate;

    /* Slim lifecycle (http://fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM.DecisionTable) */

    /**
     * This method is called once after the constructor and receives a list of all cells
     **/

    public void table(List<List<String>> table) {
    }

    /**
     * This method is called after the table method for e.g. initialization
     **/

    public void beginTable() {
    }

    /**
     * This method is called once for each row
     **/

    public void reset() {
       this.todoBase = new TodoBase();
       this.dueDate = new DueDate();
    }

    /**
     * This method is called once for each row after all set functions
     **/

    public void execute() {
    }

    /**
     * This method is called after the last row has been processed
     **/

    public void endTable() throws Exception {
    }

    /* Tests */

    public void setStart(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.dueDate.setStart(LocalDate.parse(datestr, this.dtf));
        }
    }

    /**
     * Set the due date as string
     *
     * @param  datestr  The date string, this is parsed and converted to {@link LocalDate}
     **/

    public void setDue(String datestr) {
        if (StringUtils.isNotEmpty(datestr)) {
            this.dueDate.setDue(LocalDate.parse(datestr, this.dtf));
        }
    }

    /**
     * Get the status code of the call
     *
     * @return The status code
     **/

    public String status() {
        this.todoBase.setDueDate(this.dueDate);

        return this.todoBase.getDone() ? "done" : "undone";
    }
}
