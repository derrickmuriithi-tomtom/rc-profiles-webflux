

docker pull postgres:11
docker run --rm --name postgres -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 postgres:11
mvn flyway:migrate -Dflyway.url=jdbc:postgresql://localhost/postgres -Dflyway.user=postgres -Dflyway.password=postgres

useful links
https://github.com/eugenp/tutorials/blob/master/spring-5-reactive-modules/spring-r[…]m/baeldung/reactive/webclient/WebControllerIntegrationTest.java
https://www.baeldung.com/spring-5-webclient
https://www.baeldung.com/spring-webflux