# Requirements

* Java 11
* A (postgres) database (configurable)
  * If the db user does not have superuser permissions (it shouldn't), run `CREATE EXTENSION btree_gist;` in the database as a superuser before setup.
* Maven

Build by running `mvn package` (jar will be in the target directory).

Run with `SPRING_CONFIG_LOCATION=CONFIG_DIR_HERE java -jar -Dspring.profiles.active=production election-0.0.1-SNAPSHOT.jar`, 
with the `SPRING_CONFIG_LOCATION` variable pointing to a directory containing an `application.yml` file. Check `src/main/resources/` for an example.
The application should now be hosted at port 8080 (configurable).

