quarkus:
	mvn -Pquarkus quarkus:dev

fitnesse:
	mvn -Pfitnesse clean

cucumber:
	mvn -Pquarkus test

.DEFAULT_GOAL := update-tests
update-tests:
	mvn test-compile
