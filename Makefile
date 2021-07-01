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

quarkus:
	mvn -Pquarkus quarkus:dev

fitnesse:
	mvn -Pfitnesse clean

dbfitnesse:
	mvn -Pdbfitnesse clean

cucumber:
	mvn -Pquarkus test

pact-verify:
	mvn -Ppact pact:verify -Dpact.verifier.publishResults=true

.PHONY: docker
docker:
	docker-compose -f docker/docker-compose.yml up

lazydocker:
	lazydocker -f docker/docker-compose.yml up

update-tests:
	mvn test-compile
