# Caching with Redis and Spring Boot

This project highlights how to implement caching for a web service with the use of **Redis** and **Spring Boot**.

### What is Redis?

Redis is an open-source, in-memory data structure store used as a database, cache, and message broker. It is known for its exceptional performance, versatility, and wide range of use cases. It  is widely used by developers and organizations for various purposes, including caching, session management, real-time analytics, message queues, leaderboard management, and more.

Official documentation is at https://redis.io/

### Prerequisites
Before running the application, make sure the following list is installed/configured on your machine:

* Java version 17 or above
* Maven
* Redis
* PostgreSQL
* pgAdmin (optional)
* Postman (optional)

### Running the Application

1. Clone the project using the command `git clone https://github.com/ruviniramawickrama/demo-caching-with-redis-and-spring-boot.git`
2. Go to the cloned project's root directory and open a command prompt
3. Build the project using the command `mvn clean install`
4. Run the project using the command `mvn spring-boot:run`
5. Use the Postman collection `DemoRedisWithSpringBoot.postman_collection.json` to invoke the REST end points (`\src\main\resources\DemoRedisWithSpringBoot.postman_collection.json`)

### Application Details

`application.yml`
- Contains the properties related to caching instance (Redis), database connection (Postgres) and JPA.

`schema.sql`
- Contains create table SQL statement to create the `Book` table.
- When the application starts, Spring Boot automatically runs the contents of this file based on the two properties mentioned in the `application.yml` file: **generate-ddl** and **hibernate.ddl-auto**.

`DemoCachingWithRedisAndSpringBootApplication.java` 
- Contains the annotation `@EnableCaching` to enable caching across the application. It also contains two bean configurations for **RedisCacheManagerBuilderCustomizer** and **KeyGenerator**.
- **RedisCacheManagerBuilderCustomizer** bean is used to customize caching properties for different types of cache data. For example, in this application, we are storing all book cache under the name "bookCache" with a `time to live` of 30 seconds. Similarly, we can add other cache names with different `time to live` values. 
- **KeyGenerator** bean is used to provide a custom key generator to store each cache data.

`BookCacheKeyGenerator.java`
- Creates a unique key using the Book entity unique id to store cache data.

`BookController.java`
- REST Controller which contains CRUD methods for the Book entity.
- `createBook` method: Creates a new Book resource in the database.
- `getBook` method: Retrieves a Book using the provided id. Caching is enabled for this method with the annotation `@Cacheable(keyGenerator = "bookCacheKeyGenerator")`. Initially, when a Book is retrieved, the retrieved data will be put into the cache with the name "bookCache" under a unique key (a key will be generated using the class `BookCacheKeyGenerator.java`). Once the data is put into the cache, if the same request is made again, the data will be accessed through the cache without making a call to the database. Data in the cache will expire once the `time to live` value we have configured exceeds.
- `updateBook` method: Updates a book with the provided details. Here we are using the annotation `@CachePut(keyGenerator = "bookCacheKeyGenerator")`. With this annotation, if we find the entity we are updating in the database already in the cache, the cache entry will also be updated to avoid any inconsistencies.
- `deleteBook` method: Deletes a book with the provided id. Here we are using the annotation `@CacheEvict(keyGenerator = "bookCacheKeyGenerator", beforeInvocation = true, allEntries = true)`. With this annotation, if we find the entity we are deleting in the database already in the cache, the cache entry will also be deleted to avoid any inconsistencies. 
- At the beginning of the Controller class, we have defined the annotation `@CacheConfig(cacheNames = "bookCache")`. We can use this annotation to include any common properties. For example, all cache related to the requests in BookController will be stored under the cache name "bookCache". If there's a need to use different cache names for different requests in the controller, then we can include `cacheNames` under separate annotations without including it under @CacheConfig annotation. For example, for a Get request, we can define it as `@Cacheable(cacheNames="bookCache", keyGenerator = "bookCacheKeyGenerator")`. 
- If we do not want to use custom keys to store cached data, instead of using a KeyGenerator, we can directly use the unique id coming in the request as the key. For example, for the Get request, we can define the annotation as `@Cacheable(cacheNames="bookCache", key = "#id")`.

`BookService.java`
- Implemented by `BookServiceImpl.java` which is responsible for communicating with the repository class to perform CRUD operations on the Book entity.

`BookRepository.java` 
- Implements Spring Data JpaRepository which provides ready made methods to communicate with the database and perform CRUD operations. It uses `Book.java` as the entity which maps with the respective database table.
