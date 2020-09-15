# Coherence-DataSource-MySQl

This project is an example to integrate Coherence Cache Server With MySql. You can use any other database.
In this demo we will be using JPA (Eclipselink) with Coherence to interact with database. Coherence comes with sample implementation CacheLoader  and CacheStore. This sample are located in theCOHERENCE_HOME\lib\coherence-jpa.jar. 

Class Name | Description
---------- | -----------
JpaCacheLoader | A JPA implementation of the Coherence CacheLoader interface. Use this class as a load-only implementation. It can use any JPA implementation to load entities from a data store. Use the JpaCacheStore class for a full load and store implementation.
JpaCacheStore | A JPA implementation of the Coherence CacheStore interface. Use this class as a full load and store implementation. It can use any JPA implementation to load and store entities to and from a data store. Note: The persistence unit is assumed to be set to use RESOURCE_LOCAL transactions.

We will use JpaCacheStore here. 

Lets being.

## MySql

Start mysql server and create a table.

``` 
CREATE TABLE CAR (id VARCHAR(25) PRIMARY KEY, value VARCHAR(25)); 
INSERT INTO CAR VALUES('car-1', 'BMW');
INSERT INTO CAR VALUES('car-2', 'AUDI');


SELECT * FROM CAR; 

+-------+-------+
| id    | value |
+-------+-------+
| car-1 | BMW   |
| car-2 | AUDI  |
+-------+-------+
2 rows in set (0.00 sec)
```

## Entity

Now create a simple entity class for Car. This class must implement Serializable and annotate with @Entity. The primary field should be annotate with @Id.
The sample class can be found under sre/com/troy. Class contain two variable similar to database schema id and value. Id is the primary key, and define necessary  getter, setter method.

Note : Keep the class Name and Database table name same.

Next is to configure the JPA persistence settings. Sample persistence.xml placed in the project.

```
<persistence xmlns:xsi="http://www.w3.org/2001/XMLSchemainstance" version="1.0" xmlns="http://java.sun.com/xml/ns/persistence">
<persistence-unit name="CarUnit" transaction-type="RESOURCE_LOCAL"> 
    <provider>
        org.eclipse.persistence.jpa.PersistenceProvider
    </provider>
    <class>com.troy.Car</class>
    <properties>
        <property name="eclipselink.jdbc.driver" value="com.mysql.jdbc.Driver"/>
        <property name="eclipselink.jdbc.url" value="jdbc:mysql://localhost:3306/txxy"/>
        <property name="eclipselink.jdbc.user" value="xxxx"/>
        <property name="eclipselink.jdbc.password" value="xxxx"/>
    </properties>
</persistence-unit>
</persistence>
```

Unit-name = This is the name for this entity mapping. Same will be refer to coherence configuration.
transaction-type= Enable to ‘RESOURCE_LOCAL’.
provider = Eclipse jpa provide.
Class = The entity bean created above.


Define your database details inside properties tag.

Once Completed, compile and package Car.java in jar, and place the persistence.xml in META-INF. Lets call this jar car.jar


## Coherence
Last step is to configure Coherence. Sample troy-db-store-cache.xml is uploaded. Inside distributed schema the ‘<cachestore-scheme>’ is defined. The <class-name> is example implementation JpaCacheStore mention at the beginning. The init-param take the cache-name, entity class and the entity unit-name define in the persistence.xml.

Required jars:
* Eclipselink.jar
* javax.persistence-api-2.2.jar
* toplink-grid.jar  //can be found inside oracle_common/
* mysql-connector-java-5.1.38.jar  //Database driver.
* car.jar

Now start the Coherence server with above jars in classpath and with the cache-config.

``` java -cp lib\coherence.jar;lib\car.jar;lib\mysql-connector-java-5.1.38.jar;lib\eclipselink.jar;lib\toplink-grid.jar;lib\javax.persistence-api-2.2.jar;lib\coherence-jpa.jar -Dcoherence.cacheconfig=troy-db-store-cache.xml  com.tangosol.net.DefaultCacheServer ```


After starting the server, use coherence client to connect and put a new key (car-3) value (‘Jeep’). And check at the database, same values must be inserted to database. Similar way if a get operation perfom on cache the same record retrieve from database nad update to cache. 
Note : During the startup, there won’t be any loggin related to JPA.

```
mysql> select * from car;
+-------+--------+
| id    | value  |
+-------+--------+
| car-1 | BMW    |
| car-2 | AUDI   |
| car-3 | Jeep   |
+-------+--------+
3 rows in set (0.02 sec)
```
