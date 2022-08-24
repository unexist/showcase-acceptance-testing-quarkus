Feature: Create a todo
  Create various todo entries to test the endpoint.

  * def todo =
    """
    {
      "description": "string",
      "done": true,
      "dueDate": {
        "due": "2021-05-07",
        "start": "2021-05-07"
      },
      "title": "string"
    }
    """

  * table todos
    | title    | description    | id |
    | 'title1' | 'description1' | 1  |
    | 'title2' | 'description2' | 2  |

  Background:
    * url 'http://localhost:8080'

  Scenario Outline: Create a todo with title and description and check the id.
    Given path 'todo'
    And request todo
    When method post
    Then status 200
