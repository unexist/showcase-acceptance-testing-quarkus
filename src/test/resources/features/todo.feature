Feature: Create a todo
  I want to create a new todo

  Scenario Outline: First todo
    Given New title is "<title>"
    When I ask to create a todo
    Then I should be told "<result>"

    Examples:
      | title | result |
      | Test  | 400    |
      |       | 400    |