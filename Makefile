quarkus:
	mvn -Pstart-quarkus quarkus:dev

fitnesse:
	mvn -Pstart-fitnesse clean

.DEFAULT_GOAL := update-tests
update-tests:
	mvn test-compile
