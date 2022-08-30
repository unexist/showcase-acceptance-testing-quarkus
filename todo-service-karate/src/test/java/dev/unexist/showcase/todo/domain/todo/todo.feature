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
    Then status 201
    And match header location ==  "http://localhost:8081/todo/<id>"

    Examples:
      | title    | description    | id |
      | 'title1' | 'description1' | 1  |
      | 'title2' | 'description2' | 2  |
