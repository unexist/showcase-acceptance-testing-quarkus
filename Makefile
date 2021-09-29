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

cucumber-serenity:
	mvn -f todo-service-cucumber-with-serenity/pom.xml test

# FitNesse
fitnesse:
	mvn -f todo-service-fitnesse/pom.xml compile

fitnesse-update-tests:
	mvn -f todo-service-fitnesse/pom.xml test-compile

dbfitnesse:
	mvn -f todo-service-fitnesse/pom.xml -Pdbfitnesse compile

# Helper
helper:
	mvn -f todo-service-helper/pom.xml test

# Pact
pact:
	mvn -f todo-service-helper/pom.xml test

pact-verify:
	mvn -f todo-service-pact/pom.xml pact:verify -Dpact.verifier.publishResults=true

pact-publish:
	mvn  -f todo-service-pact/pom.xml pact:publish

.PHONY: docker
docker:
	docker-compose -f docker/docker-compose-pact.yml up

lazydocker:
	lazydocker -f docker/docker-compose-pact.yml up

