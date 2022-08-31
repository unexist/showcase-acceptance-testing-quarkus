Feature: Create a todo

  Background:
    * url 'http://localhost:8081'

  Scenario Outline: Create a todo with title and description and check the id.
    Given path 'todo'
    And request
    """
    {
      "description": <description>,
      "done": true,
      "dueDate": {
        "due": "2021-05-07",
        "start": "2021-05-07"
      },
      "title": <title>
    }
    """
    When method post
    Then match header location ==  "#regex .*/todo/<id>"

    Examples:
      | title    | description    | id |
      | 'title1' | 'description1' | 1  |
      | 'title2' | 'description2' | 2  |

  Scenario Outline: Create a todo with start and due dates and check the status.
    Given def createTodo =
    """
    function(args) {
      var TodoType = Java.type("dev.unexist.showcase.todo.domain.todo.Todo");
      var DueDateType = Java.type("dev.unexist.showcase.todo.domain.todo.DueDate");
      var DateTimeFormatterType = Java.type("java.time.format.DateTimeFormatter");
      var LocalDateType = Java.type("java.time.LocalDate");

      var dtf = DateTimeFormatterType.ofPattern("yyyy-MM-dd");

      var dueDate = new DueDateType();

      dueDate.setStart(LocalDateType.parse(args.startDate, dtf));
      dueDate.setDue(LocalDateType.parse(args.dueDate, dtf));

      var todo = new TodoType();

      todo.setDueDate(dueDate);

      return todo.getDone() ? "done" : "undone";
    }
    """
    When def result = call createTodo { startDate: <start>, dueDate: <due> }
    Then match result == "<status>"

    Examples:
      | start      | due        | status |
      | 2021-09-10 | 2022-09-10 | undone |
      | 2021-09-10 | 2021-09-09 | done   |