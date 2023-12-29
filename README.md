# backend-data-handling-java
```
cmd+shift+p
Spring Initializr: Create a Maven Project
Spring Web
Spring Data JPA
PostgreSQL Driver

docker compose up -d java_db
docker compose logs
docker ps -a

docker exec -it java_db psql -U postgres -d postgres-java
postgres=# \dt
SELECT * FROM users;

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'postgres-java' AND pid <> pg_backend_pid();

DROP DATABASE "postgres-java";
CREATE DATABASE "postgres-java";

mvn clean package -DskipTests
docker compose up --build
docker compose up java_app

docker exec -it java_app /bin/bash
```