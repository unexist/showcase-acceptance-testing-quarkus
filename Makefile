quarkus:
	mvn -Pquarkus quarkus:dev

fitnesse:
	mvn -Pfitnesse clean

dbfitnesse:
	mvn -Pdbfitnesse clean

cucumber:
	mvn -Pquarkus test

docker:
	docker-compose -f src/main/docker/docker-compose.yml up

.DEFAULT_GOAL := update-tests
update-tests:
	mvn test-compile
