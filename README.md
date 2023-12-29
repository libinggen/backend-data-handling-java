# backend-data-handling-java
```
mvn archetype:generate -DgroupId=com.example -DartifactId=myapi -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

cd myapi

mvn clean install

https://mvnrepository.com


mvn clean install -U
mvn clean package


docker-compose down -v

docker compose up -d java_db

docker compose logs 

docker ps -a

docker exec -it java_db psql -U postgres -d postgres

postgres=# \dt

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'java_db' AND pid <> pg_backend_pid();

DROP DATABASE "java_db";
CREATE DATABASE "java_db";

docker exec -it javaapp /bin/bash

mvn clean package -DskipTests
docker compose up --build


mvn clean package -DskipTests


```