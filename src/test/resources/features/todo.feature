Feature: Create a todo
  I want to create a new todo

  Scenario Outline: Dream of a todo
    Given I imagine a todo "<title>"
    And a description of "<description>"
    And starting on "<start>"
    And lasting no longer than "<due>"
    And still not "<done>"
    When I would ask for the status code
    Then I should be told "<status>"

    Examples:
      | title | description | start      | due        | done  | status |
      | Test  | Test        | 2021-01-01 | 2021-02-01 | false | 201    |
      |       | Test        | 2021-01-01 | 2021-02-01 | false | 201    |
      | Test  |             | 2021-01-01 | 2021-02-01 | false | 201    |
      | Test  | Test        |            | 2021-02-01 | false | 201    |
      | Test  | Test        | 2021-01-01 |            | false | 201    |
      | Test  | Test        | 2021-01-01 | 2021-02-01 |       | 201    |
      |       |             |            |            |       | 400    |
