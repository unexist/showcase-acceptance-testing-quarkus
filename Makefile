.DEFAULT_GOAL := update-tests

define JSON_TODO
curl -X 'POST' \
  'http://localhost:8080/todo' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "description": "string",
  "done": true,
  "dueDate": {
    "due": "2021-05-07",
    "start": "2021-05-07"
  },
  "title": "string"
}'
endef
export JSON_TODO

# Tools
todo:
	@echo $$JSON_TODO | bash

# Concordion
concordion:
	mvn -f todo-service-concordion/pom.xml test

# Cucumber
cucumber:
	mvn -f todo-service-cucumber/pom.xml test

cucumber-report: cucumber
	mvn -f todo-service-cucumber/pom.xml cluecumber-report:reporting

# Serenity
serenity:
	mvn -f todo-service-cucumber-with-serenity/pom.xml test

serenity-report: serenity
	mvn -f todo-service-cucumber-with-serenity/pom.xml serenity:reports -Dserenity.reports=single-page-html,navigator serenity:aggregate

# FitNesse
fitnesse-server:
	mvn -f todo-service-fitnesse/pom.xml test

fitnesse-open:
	open http://localhost:8888/FrontPage

fitnesse-quarkus:
	mvn -f todo-service-fitnesse/pom.xml quarkus:dev

fitnesse-update:
	mvn -f todo-service-fitnesse/pom.xml test-compile

# DB Fitnesse
dbfitnesse:
	mvn -f todo-service-fitnesse/pom.xml -Pdbfitnesse compile

# JBehave
jbehave:
	mvn -f todo-service-jbehave/pom.xml test
