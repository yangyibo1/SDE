## Database prepare
1.create database in mysql
  
create schema 'library' for application 

create schema 'library_test' for unit tests

2. change database conf

change src/main/resources/application.properties for application

```
spring.datasource.url=jdbc:mysql://localhost:3306/library
spring.datasource.username=root
spring.datasource.password=password
```

change src/test/resources/application.properties for unit test

```
spring.datasource.url=jdbc:mysql://localhost:3306/library_test
spring.datasource.username=root
spring.datasource.password=password
```

if you run mysql in local, then you don't need to change the url.
you only need to change the username and password.

### Run the application

After preparing the database, import the project in your IDE, and run in class ``Application``

