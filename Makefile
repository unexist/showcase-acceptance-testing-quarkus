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

# Courgette
courgette:
	mvn -f todo-service-courgette/pom.xml test

# Cucumber
cucumber:
	mvn -f todo-service-cucumber/pom.xml test

# Cluecumber
cluecumber-report: cucumber
	mvn -f todo-service-cucumber/pom.xml cluecumber-report:reporting

# Serenity
serenity-cucumber:
	mvn -f todo-service-cucumber-with-serenity/pom.xml test

serenity-cucumber-report: serenity-cucumber
	mvn -f todo-service-cucumber-with-serenity/pom.xml serenity:reports -Dserenity.reports=single-page-html,navigator serenity:aggregate

serenity-cucumber-open:
	open todo-service-cucumber-with-serenity/target/site/serenity/index.html

serenity-jbehave:
	mvn -f todo-service-jbehave-with-serenity/pom.xml test

serenity-jbehave-report: serenity-jbehave
	mvn -f todo-service-cucumber-with-serenity/pom.xml serenity:reports -Dserenity.reports=single-page-html,navigator serenity:aggregate

serenity-jbehave-open:
	open todo-service-jbehave-with-serenity/target/site/serenity/index.html

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

jbehave-report:
	open todo-service-jbehave/target/jbehave/view/reports.html
