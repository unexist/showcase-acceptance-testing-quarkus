# Create a todo

This is an example specification, that demonstrates how to facilitate markdown
and [Concordion](https://concordion.org) fixtures.

### [Simple example](- "simple_example")

A todo is [created](- "#result = create(#title, #description)") with the simple 
title **[test](- "#title")** and the matching description 
**[test](- "#description")** and [saved](- "#result = save(#result)") as ID
[1](- "?=#result.getId").

### [Simple example with different notation](- "simple_example_modified")

A todo is [created][createdCmd] with the simple title **[test](- "#title")** and
the matching description **[test](- "#description")** and [saved][savedCmd] 
as ID [2](- "?=#result.getId").

[createdCmd]: - "#result = create(#title, #description)"
[savedCmd]: - "#result = save(#result)"

### [Simple table example](- "simple_table")

This example creates todos based on table values:

| [createAndSave][][Title][title] | [Description][description] | [ID][id] |
| ------------------------------- | -------------------------- | -------- |
| title1                          | description1               | 3        |
| title2                          | description2               | 4        |

[createAndSave]: - "#result = createAndSave(#title,#description)"
[title]: - "#title"
[description]: - "#description"
[id]: - "?=#result.getId"

### [Extended table example](- "extended_table")

This example combines ideas from the others ones:

| [createWithDate][][Start date][start] | [Due date][due] | [Is done?][done] |
| ------------------------------------- | --------------- | ---------------- |
| 2021-09-10                            | 2022-09-10      | undone           |
| 2021-09-10                            | 2021-09-09      | done             |

[createWithDate]: - "#result = createWithDate(#start,#due)"
[start]: - "#start"
[due]: - "#due"
[done]: - "?=isDone(#result)"