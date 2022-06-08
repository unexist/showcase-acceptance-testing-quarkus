Create a todo

Meta:
@category basic

Narrative:

Create various todo entries to test the endpoint.

Scenario: Create a todo with title and description and check the id.

Given I create a todo
When its title is "<title>"
And its description is "<description>"
Then its id should be <id>

Examples:
      | title  | description  | id |
      | title1 | description1 | 1  |
      | title2 | description2 | 2  |

Scenario: Create a todo with start and due dates and check the status.

Given I create a todo
When it starts on "<start>"
And it ends on "<due>"
Then it should be marked as <status>

Examples:
      | start      | due        | status  |
      | 2021-09-10 | 2022-09-10 | undone  |
      | 2021-09-10 | 2021-09-09 | done    |