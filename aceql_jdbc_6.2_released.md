# AceQL Client JDBC Driver 6.2 Released

We are excited to announce the release of [AceQL Client JDBC Driver](https://github.com/kawansoft/aceql-http-client-jdbc-driver) 6.2.

The revision improves the support of SQL transactions  with the full implementation of [Savepoints](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html#set_roll_back_savepoints):

- [releaseSavePoint(Savepoint savepoint)](https://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html#releaseSavepoint-java.sql.Savepoint-)
- [rollbackSavepoint(Savepoint savepoint)](https://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html#rollback-java.sql.Savepoint-)
- [setSavepoint()](https://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html#setSavepoint--)
- [setSavepoint(String name)](https://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html#setSavepoint-java.lang.String-)



## About AceQL Client JDBC Driver - JDBC Over HTTP

<img src="https://www.aceql.com/img/AceQL-Schema-min.jpg" alt="AceQL Draw"/>

The [AceQL Client JDBC Driver](https://github.com/kawansoft/aceql-http-client-jdbc-driver) is an open-source JDBC driver which allows query a relational database via HTTP. The driver connects to a middleware component - [AceQL HTTP](https://github.com/kawansoft/aceql-http/blob/master/README.md) Web Server-  which is in charge of exposing a database to the web and [manage all security aspects.](https://www.aceql.com/sql-over-http-database-security.html)

The JDBC Driver allows to program JDBC calls on main open-source and commercial SQL databases:

- MySQL.
- PostgreSQL
- Microsoft SQL Server.
- Oracle Database.

These 4 databases are fully supported. Other databases are supported through commercial support: DB2, Informix, Sybase & Teradata.

### Writing SQL calls with the JDBC Driver

The JDBC syntax is unmodified. The Driver may be used in applications without any source code update, just by specifying Driver Properties. 

Usage is straightforward for developers, without any learning curve: 

```java
	// The URL of the AceQL Server servlet
    // Port number is the port number used to start the Web Server:
    String url = "https://www.acme.com:9443/aceql";

    // The remote database to use:
    String database = "sampledb";

    // (user, password) for authentication on the server side.
    String user = "MyUsername";
    String password = "MySecret";

    // Register the Community Edition Driver
    DriverManager.registerDriver(new AceQLDriver());
    Class.forName(AceQLDriver.class.getName());

    Properties info = new Properties();
    info.put("user", user);
    info.put("password", password);
    info.put("database", database);

    Connection connection = DriverManager.getConnection(url, info);

    // We may now use regular JDBC syntax for all SQL calls:
    String sql = "select id, title, lname from customer where customer_id = 1";

    try (Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);) {
        while (rs.next()) {
            System.out.println();
            int i = 1;
            System.out.println("customer id   : " + rs.getInt(i++));
            System.out.println("customer title: " + rs.getString(i++));
            System.out.println("customer name : " + rs.getString(i++));
        }
    }
```

Read more at www.aceql.com and https://github.com/kawansoft/aceql-http-client-jdbc-driver.

_____________________

