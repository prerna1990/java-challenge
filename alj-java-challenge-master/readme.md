### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)
- Install Redis from home brew before running this application in local( https://redis.io/docs/getting-started/installation/install-redis-on-mac-os/)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Instructions

- download the zip file of this project
- create a repository in your own github named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues

### Enhancement done by me
- Added code in Java 8 wherever possible.
- Added Exception advice and application ready state check.
  - I have added some error code by default to pass for exception.
- Separated the Employee request and Domain , so that in Future UI and backend changes should not affect with each other.
- Removed all unnecessary setters even though Autowired was present.
- Removed all extra variables and declarations
- Added logger statements as much possible.
- Added pagination and sorting while retrieving the employee List.
  - By default page size is 10 but could be configurable.
- Added Redis cache and Basic Authentication to use the API .
- Used all possible Spring Boot annotations to enhance the readability of the code.
- For /PUT call, as it's an EMPLOYEE update , so It could be done in two ways.
  - If EmployeeId does not exist then throw the exception , else update the records( Implemented)
  - Or,If Employee does not exist then add a new record , else update the records.( Implemented but commented in code)
- Username/Password for all the end points are present in securityConfig.java
  - Created two users with view and editor access and their roles.
    - USERS and Password
      - User - read_password
      - Admin- editor_password
    - Role->  VIEWER,EDITOR
    - All the /get end points have VIEWER role whereas PUT/DELETE/POST have "EDITOR".


### Future scope (if time permits)
- Take out all the configs and keep in Spring cloud like security,Redis, etc..
- API security can be enhanced by using Oauth2 or any token mechanism instead of Basic auth.
- Customized Key-value map Redis implementations.
- Pagination could be improved at next level like for below cases.
  - http://localhost:8080/employees?pageSize=5&pageNo=1&sortBy=name
  - http://localhost:8080/employees?pageSize=5&pageNo=2
  - and so on..
- can be added actuator for health check monitoring
- More Test coverage

#### Your experience in Java

Please let us know more about your Java experience in a few sentences. For example:

- I have 7 years experience in Java and started to use Java 8 and Spring Boot for 3.5 years.


