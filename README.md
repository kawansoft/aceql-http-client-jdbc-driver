![GitHub top language](https://img.shields.io/github/languages/top/kawansoft/aceql-http-client-jdbc-driver) ![GitHub issues](https://img.shields.io/github/issues/kawansoft/aceql-http-client-jdbc-driver) 
![GitHub](https://img.shields.io/github/license/kawansoft/aceql-http-client-jdbc-driver) ![Maven Central](https://img.shields.io/maven-central/v/com.aceql/aceql-http-client-jdbc-driver)
![GitHub last commit (branch)](https://img.shields.io/github/last-commit/kawansoft/aceql-http-client-jdbc-driver/master)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/1768a87595f74c9391933a85b90dc20c)](https://www.codacy.com/gh/kawansoft/aceql-http-client-jdbc-driver/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=kawansoft/aceql-http-client-jdbc-driver&amp;utm_campaign=Badge_Grade)
![GitHub contributors](https://img.shields.io/github/contributors/kawansoft/aceql-http-client-jdbc-driver)


# AceQL HTTP 

## AceQL Client JDBC Driver v9.3 - User Guide

## March 2, 2023

<img src="https://docs.aceql.com/img/AceQL-Schema-min.jpg" alt="AceQL Draw"/>

* [Fundamentals](#fundamentals)
   * [Technical operating environment](#technical-operating-environment)
   * [AceQL Server side compatibility](#aceql-server-side-compatibility)
      * [Main features](#main-features)
   * [Installation](#installation)
      * [Maven](#maven)
      * [Single Jar](#single-jar)
   * [Android Project settings](#android-project-settings)
   * [Data transport](#data-transport)
      * [Transport format](#transport-format)
      * [Content streaming and memory management](#content-streaming-and-memory-management)
   * [Best practices for fast response time](#best-practices-for-fast-response-time)
* [Using the AceQL Client JDBC Driver](#using-the-aceql-client-jdbc-driver)
   * [Connection creation](#connection-creation)
   * [Using a Proxy](#using-a-proxy)
   * [Handling Exceptions](#handling-exceptions)
      * [The error type](#the-error-type)
      * [Most common AceQL Server messages](#most-common-aceql-server-messages)
      * [HTTP Status Codes](#http-status-codes)
   * [Data types](#data-types)
   * [SQL Transactions and Connections modifiers](#sql-transactions-and-connections-modifiers)
   * [Using Stored Procedures](#using-stored-procedures)
      * [Using Oracle Database stored procedures with SELECT calls](#using-oracle-database-stored-procedures-with-select-calls)
   * [Batch management](#batch-management)
   * [BLOB management](#blob-management)
      * [Standard syntax](#standard-syntax)
         * [BLOB creation with standard syntax](#blob-creation-with-standard-syntax)
         * [BLOB reading with standard syntax](#blob-reading-with-standard-syntax)
      * [Advanced syntax with streaming techniques](#advanced-syntax-with-streaming-techniques)
         * [BLOB creation with stream syntax](#blob-creation-with-stream-syntax)
         * [BLOB reading with stream syntax](#blob-reading-with-stream-syntax)
         * [Using Progress Bars with Blobs](#using-progress-bars-with-blobs)
   * [HTTP session options](#http-session-options)
   * [Using the AceQL Metadata Query API](#using-the-aceql-metadata-query-api)
      * [Downloading database schema into a file](#downloading-database-schema-into-a-file)
      * [Accessing remote database main properties](#accessing-remote-database-main-properties)
      * [Getting Details of Tables and Columns](#getting-details-of-tables-and-columns)
   * [Using the native JDBC Metadata Query API](#using-the-native-jdbc-metadata-query-api)
      * [Code Sample](#code-sample)
      * [Using Database viewers with the AceQL Client JDBC Driver](#using-database-viewers-with-the-aceql-client-jdbc-driver)
      * [Disabling JDBC MetaData calls when not required](#disabling-jdbc-metadata-calls-when-not-required)
   * [Using outer authentication without a password and with an AceQL Session ID](#using-outer-authentication-without-a-password-and-with-an-aceql-session-id)
   * [Passing request headers for validation on server side](#passing-request-headers-for-validation-on-server-side)
* [Limitations](#limitations)


# Fundamentals 

This document describes how to use the AceQL Client JDBC Driver and gives some details about how it operates with the server side.

The AceQL Client JDBC Driver allows users to wrap the [AceQL HTTP APIs](https://github.com/kawansoft/aceql-http/blob/master/aceql-http-user-guide-api.md) and eliminate the tedious work of handling communication errors and parsing JSON results.

Android and Java Desktop application developers can access remote SQL databases and/or SQL databases in the cloud, simply by including standard JDBC calls in their code, just like they would for a local database.

The AceQL Server operation is described in [AceQL HTTP Server Installation and Configuration Guide](https://github.com/kawansoft/aceql-http/blob/master/README.md), whose content is sometimes referred in this User Guide. 

## Technical operating environment 

The AceQL Client JDBC Driver is entirely written in Java, and functions identically with Microsoft Windows, Linux, and all versions of UNIX supporting Java 8+.

The only required third party installation is a recent JVM:

| OS                                  | **JVM (Java Virtual Machine)** |
| ----------------------------------- | ------------------------------ |
| Android                             | Android 4.1+                   |
| Windows, UNIX/Linux, OS X  / mac OS | Java 8+                        |

## AceQL Server side compatibility

The Client JDBC Driver version is compatible with AceQL HTTP server side v12.2+  

### Main features

| AceQL Client JDBC Driver v9.3<br>Main Features               |
| :----------------------------------------------------------- |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;Works as a real [JDBC Driver](https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html) <br/>Plug & play without editing your app source code. |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;Main SQL types<br/>`Boolean`, `Integer`, `Short`, `Double`, `Float`, `BigDecimal`, `Long`, `String`, `Date`, `Time`, `Timestamp` |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;Connection  through HTTP Proxy |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;SQL Transactions<br/>`Connection.commit()`, `Connection.rollback()`, `Connection.setAutocomit(boolean autoCommit)`.<br/>[Savepoints](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html#set_roll_back_savepoints) are also fully supported: |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>Batch methods for `Statement` <br>and `PreparedStatement` |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;Open Source Databases<br/>MySQL, PostgreSQL, MariaDB |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;MS SQL Server |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;Oracle Database |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;[BLOB](https://docs.oracle.com/javase/tutorial/jdbc/basics/blob.html) Type - Size up to 4GB |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;IBM DB2 Database |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;[Array](https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/Array.html) Type |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;[Stored Procedures](https://docs.oracle.com/javase/tutorial/jdbc/basics/storedprocedures.html) |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;JDBC API Metadata<br/>`Connection.getMetadata()`, `ResultSet.getMetaData()` |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/>&nbsp;JDBC Clients Tools & GUI <br/>[DBeaver](https://dbeaver.io/), [DbVisualizer](https://www.dbvis.com/), [JetBrains DataGrip](https://www.jetbrains.com/datagrip/), [RazorSQL](https://razorsql.com/), [SQuirreL SQL](http://squirrel-sql.sourceforge.net/) |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/> [Allows outer authentication without a password](#using-outer-authentication-without-a-password-and-with-an-aceql-session-id) |
| <img src="https://download.aceql.com/images/check_20.png" alt="check!"/> [Allows passing request headers for validation on server](#passing-request-headers-for-validation-on-server-side) |

## Installation

### Maven

```xml
<dependency>
    <groupId>com.aceql</groupId>
    <artifactId>aceql-http-client-jdbc-driver</artifactId>
    <version>9.3</version>
</dependency>
```
### Single Jar 

For non Maven users: a single jar with all dependencies is available on the [download page](https://www.aceql.com/aceql-download-page.html).

## Android Project settings

 Add the following 3 lines to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

A full Android project sample is available on GitHub: [aceql-http-client-android-sample](https://github.com/kawansoft/aceql-http-client-android-sample).

## Data transport  

### Transport format 

AceQL transfers the least possible amount of meta-information:

- Request parameters are  transported in UTF-8 format.
- JSON format is used for data  and class transport (using `javax.json` package).

### Content streaming and memory management 

All requests are streamed: 

- Output requests (from the client side) are streamed directly from the socket to the server to avoid     buffering any content body.
- Input responses (for the client side) are streamed directly from the socket to the server to efficiently read the response body.

Large content (ResultSet, …) is transferred using files. It is never loaded in memory. Streaming techniques are always used to read and write this content.

## Best practices for fast response time

Every HTTP exchange between the client and server side is time-consuming, because the HTTP call is synchronous and waits for the server's response

Try to avoid coding JDBC calls inside loops, as this can reduce execution speed. Each JDBC call will send an HTTP request and wait for the response from the server. Always use  batch commands (`Statement.executeBatch()`,...)   when you have many rows to INSERT or UPDATE.

Note that AceQL is optimized as much as possible:

- A SELECT call returning a huge Result Set will not consume memory on the server or client side:  AceQL uses input stream and output stream I/O for `ResultSet` transfer.
- Result Set retrieval is as fast as possible:  
  - The `ResultSet` creation is done once on the server by the `executeQuery()`.
  - The rows are all dumped at once on the servlet output stream by the server.
  - The client side gets the `ResultSet` content as a file.
  - All `ResultSet` navigation commands are executed locally on the client side by navigating through the file:  `next()`, `prev(`), `first()`, `last()`, etc. 
- **It is highly recommended to always use  batch commands  when you have many rows to INSERT or UPDATE.**

_________



## *[Advertisement] 📢*

## *Transform Your Development with ChatMotor API! 🚀*

[ChatMotor](https://www.chatmotor.ai/) API is designed to make your development life easier by handling the complexities of OpenAI and ChatGPT:

- 🌟 **Seamless Integration**: No need to learn the intricacies of OpenAI APIs and their limitations. ChatMotor handles chunking, HTTP errors, and retry management.
- 📄 **Excel Handling**: Allow your end user to easily process and manipulate large Excel files using simple prompts. Much easier for them than endless VBA or painful Python coding.
- 📝 **Unlimited Input Sizes**: Seamlessly handle prompts that exceed 4096 tokens with automatic sequencing and parallel task threading for speed, allowing you to treat large inputs without dwelling on the details.
- 🎙️ **Hassle-Free Transcriptions**: Simply provide the audio files for transcription, regardless of format and size. ChatMotor handles everything, including gigantic files, and delivers a clean, formatted text file.
- 🌐 **Advanced Translation**: Handle documents of any size with ease. Just pass them to the API, and it will manage everything, delivering accurate and fast results.

**✨ Faster and Easier Delivery for Your End User: Save Time, Reduce Complexity, and Focus on What Matters!**

👉 Explore [**ChatMotor**](https://www.chatmotor.ai) and revolutionize your development workflow!



---

# Using the AceQL Client JDBC Driver

We will use the same `sampledb` database for all our code samples. 

The schema is available here: [sampledb.txt](https://docs.aceql.com/rest/soft_java_client/9.3/src/sampledb.txt). 

## Connection creation

Usage of the AceQL Client JDBC Driver is straightforward: it just requires to create a `Connection` as you would do with any other JDBC Driver. There 

The  `Connection` to the remote database is created using the standard [DriverManager.getConnection(String url, Properties info)](https://docs.oracle.com/javase/8/docs/api/java/sql/DriverManager.html#getConnection-java.lang.String-java.util.Properties-) JDBC method and passing the URL of the `ServerSqlManager` Servlet of your server configuration:

```java
    // The URL of the AceQL Server servlet
    // Port number is the port number used to start the Web Server:
    String url = "https://www.acme.com:9443/aceql";

    // The remote database to use:
    String database = "sampledb";

    // (user, password) for authentication on server side.
    String user = "MyUsername";
    String password = "MySecret";

    // Register Driver
    DriverManager.registerDriver(new AceQLDriver());
    Class.forName(AceQLDriver.class.getName());

    Properties info = new Properties();
    info.put("user", user);
    info.put("password", password);
    info.put("database", database);

    Connection connection = DriverManager.getConnection(url, info);
```
From now on, you can use the connection to execute updates and queries on the remote database, using standard and unmodified JDBC calls. 

## Using a Proxy

Communication via a proxy server is done using dedicated `properties`.  Proxy authentication is supported.

| Property Name   | Property Value                                               | Remarks                                                      |
| --------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `proxyType`     | The proxy type.  `DIRECT`, `HTTP` or `SOCKS`. Defaults to `DIRECT`. | if `DIRECT`, no proxy will be used.                          |
| `proxyHostname` | The hostname of the proxy. Defaults to `null`.               | Mandatory value if `proxyType` is not `DIRECT`.              |
| `proxyPort`     | The proxy port. Defaults to `0`.                             | Property must be set as a String.                            |
| `proxyUsername` | The username for an authenticated proxy. Defaults to `null`. | The proxy in use is an authenticated proxy if value is not `null`. |
| `proxyPassword` | The password for an authenticated proxy. Defaults to `null`. | Must be not `null` if `proxyUsername` is set.                |

Sample code:

```java
    // Proxy info. Port number is passed as a String
    info.put("proxyType", "HTTP");
    info.put("proxyHostname", "localhost");
    info.put("proxyPort", "8081"); 
    info.put("proxyUsername", "myProxyUsername");
    info.put("proxyPassword", "myProxyPassword");

    // Attempt to establish a connection to the remote database 
    // using an HTTP Proxy:
    Connection connection = DriverManager.getConnection(url, info);
```

## Handling Exceptions

Except for `NullPointerException`, exceptions thrown are always an instance of [AceQLException.](https://docs.aceql.com/rest/soft_java_client/9.3/javadoc/com/aceql/jdbc/commons/AceQLConnection.html)

The `AceQLException` contains 5 pieces of information :

| Info             | Description                              |
| ---------------- | ---------------------------------------- |
| Reason           | The error message. Retrieved with `getMessage()`. |
| Error Type       | See below for description. Retrieved with `getErrorCode()`. |
| Cause            | The `Throwable` cause, if any. Retrieved with `getCause()`. |
| Http Status Code | See below for description. Retrieved with `getHttpStatusCode(`). |
| Server Exception | The Exception Stack Trace thrown on the server  side, if any.  <br />Retrieved with `getRemoteStackTrace()`. |

### The error type

The error type allows users to get the type of error and where the error occurred. It is retrieved with `AceQLException.getErrorCode()`:

| Error Type  Value | Description                              |
| ----------------- | ---------------------------------------- |
| 0                 | The error occurred locally on the client  side.  See `getHttpStatusCode()` for more info.  Typical cases: no Internet connection, proxy  authentication required. |
| 1                 | The error is due to a JDBC Exception.  It was raised by the remote JDBC Driver and  is rerouted by AceQL as is.  The JDBC error message is accessible via `getMessage()`  Typical case: an error in the SQL statement.   Examples: wrong table or column name. |
| 2                 | The error was raised by the AceQL Server.  It means that the AceQL Server expected a  value or parameter that was not sent by the client side.  Typical cases: misspelling in URL parameter,  missing required request parameters,   JDBC Connection expiration, etc.  The detailed error message is accessible via `getMessage()`.   See below for the most common AceQL Server  error messages. |
| 3                 | The AceQL Server forbids the execution of the  SQL statement for a security reason.  For security reasons, `getMessage()` gives  access to voluntarily vague details. |
| 4                 | The AceQL Server is on failure and raised an  unexpected Java Exception.  The stack track is included and accessible  via `getRemoteStackTrace()`. |

### Most common AceQL Server messages

| AceQL  Sever Error Messages   (AceQLException.getErrorCode()  = 2) |
| :----------------------------------------------------------- |
| AceQL  main servlet not found in path                        |
| An error  occurred during Blob download                      |
| An error  occurred during Blob upload                        |
| Blob  directory defined in `DatabaseConfigurator.getBlobDirectory()` does not exist |
| Connection  is invalidated (probably expired).               |
| Database  does not exist                                     |
| Invalid  blob_id. Cannot be used to create a file            |
| Invalid  blob_id. No Blob corresponding to blob_id           |
| Invalid  session_id.                                         |
| Invalid  username or password.                               |
| No action  found in request.                                 |
| Unable to  get a `Connection`.                               |
| Unknown  SQL action or not supported by software             |

### HTTP Status Codes

The HTTP Status Code is accessible with `AceQLException.getHttpStatusCode()`.

The HTTP Status Code is 200 (OK) on successful completion calls.

When an error occurs: 

- If the error type is 0, the HTTP Status Code is returned by the client side and may take all possible values in a malformed HTTP call.

- If the error type is > 0, the HTTP Status Code can take one the following values returned by server side:


| HTTP  Status  Code          | Description                              |
| --------------------------- | ---------------------------------------- |
| 400 (BAD REQUEST)           | Missing element in URL path.<br>Missing request parameters.<br>All JDBC errors raised by the remote JDBC Driver. |
| 401 (UNAUTHORIZED)          | Invalid username or password in connect.<br />Invalid session_id.<br />The AceQL Server forbade the execution of the SQL statement for  security reasons. |
| 404 (NOT_FOUND)             | BLOB directory does not exist on server.<br />BLOB file not found on server. |
| 500 (INTERNAL_SERVER_ERROR) | The AceQL Server is on failure and  raised an unexpected Java Exception. |

## Data types 

The main JDBC data types for columns are supported: 

Boolean, Blob, Integer, Short, Double, Float, BigDecimal, Long,String, Date, Time, Timestamp, and Array.  

## SQL Transactions and Connections modifiers

The AceQL Client JDBC Driver support SQL transactions with:

- `Connection.commit()`
- `Connection.rollback()`
- Connection.`setAutoCommit(boolean autoCommit)`

The following Connections modifiers calls are supported in this version:

- `Connection.setHoldability(int holdability)`

- `Connection.setTransactionIsolation(int level)`

- `Connection.setReadOnly(boolean readOnly)`

## Using Stored Procedures

Stored procedures are supported through the native unmodified JDBC syntax. Database product dialects are not supported, as the AceQL JDBC Driver uses only the common JDBC syntax for stored procedures.

### Using Oracle Database stored procedures with SELECT calls

Oracle Database stored procedures with SELECT calls are supported starting AceQL HTTP server version 12.0.

Assuming this Oracle Database stored procedure that executes a SELECT call:

```plsql
-- ORACLE_SELECT_CUSTOMER stored procedure
-- Executes a SELECT on customer table with 
-- customer_id as IN parameter
create or replace PROCEDURE ORACLE_SELECT_CUSTOMER 
    (p_customer_id NUMBER, p_rc OUT sys_refcursor) AS 
BEGIN
    OPEN p_rc
    For select customer_id from customer where customer_id > p_customer_id;
END ORACLE_SELECT_CUSTOMER;
```

The JDBC syntax with a Connection using the Oracle JDBC Driver is:

```java
// Calling the ORACLE_SELECT_CUSTOMER store procedure
// Native Oracle JDBC syntax using an Oracle JDBC Driver:
CallableStatement callableStatement 
    = connection.prepareCall("{ call ORACLE_SELECT_CUSTOMER(?, ?) }");
callableStatement.setInt(1, 2);
callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
callableStatement.executeQuery();

ResultSet rs= (ResultSet) callableStatement.getObject(2);

while (rs.next()) {
    System.out.println(rs.getInt(1));
}
```

But the JDBC syntax with a Connection using the AceQL JDBC Driver is simplified and does not use `OracleTypes.CURSOR` nor the `(ResultSet) callableStatement.getObject()` cast:

```java
// Calling the ORACLE_SELECT_CUSTOMER stored procedure.
// JDBC syntax using the AceQL JDBC Driver
CallableStatement callableStatement 
    = connection.prepareCall("{ call ORACLE_SELECT_CUSTOMER(?, ?) }");
callableStatement.setInt(1, 2);
ResultSet rs = callableStatement.executeQuery();

while (rs.next()) {
    System.out.println(rs.getInt(1));
}
```

## Batch management

[Statement.executeBatch()](https://docs.oracle.com/javase/8/docs/api/java/sql/Statement.html#executeBatch--)  is supported.  `Statement` and `PreparedStatement` implementations are both supported.

Batch commands processing is optimized in order to run as fast as possible and consume fewer possible resources:

- All batch commands & parameters are send using only one upload at the start of the `Statement.executeBatch()`  processing. 
- The upload of the batch commands & parameters  is done using streaming techniques on the client-side. The incoming reading is also done in streaming on the server side.
- Transactions are supported, so as usual, it's much faster to run in autocommit off.

**It is highly recommended to always use  batch commands  when you have many rows to INSERT or UPDATE.**

Note that batch commands are supported with AceQL HTTP Server version 8.0 or higher on server side.

## BLOB management

The AceQL Client JDBC Driver supports BLOB creation and reading. Methods are implemented using streaming techniques to always keep memory consumption low on server side.

Trivial or unrelated code is skipped with `//...` comments and `null` values are not tested in the sample codes for the sake of clarity.

### Standard syntax

#### BLOB creation with standard syntax

```java
    // BLOB Creation 
    // 1) Syntax with PreparedStatement.setBytes
    String sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    File file = createMyBlobFile();
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    //...
    byte[] bytes = Files.readAllBytes(file.toPath());
    preparedStatement.setBytes(parameterIndex, bytes);
    //...
    preparedStatement.executeUpdate();
```

```java
    // BLOB Creation 
    // 2) Syntax with PreparedStatement.setBlob
    String sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    File file = createMyBlobFile();
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    //...
    Blob blob = connection.createBlob();
    byte[] bytes = Files.readAllBytes(file.toPath());
    blob.setBytes(1, bytes);
    preparedStatement.setBlob(parameterIndex, blob);
    //...
    preparedStatement.executeUpdate();
```

#### BLOB reading with standard syntax
```java
    // BLOB Reading
    // 1) Syntax with ResultSet.getBytes
    String sql = "select * from orderlog where customer_id = ? and item_id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    //...
    ResultSet rs = preparedStatement.executeQuery();
    if (rs.next()) {
        //...
        File file = myAppCreateBlobFile();
        byte[] bytes = rs.getBytes(columnIndex);
        InputStream in = new ByteArrayInputStream(bytes);
        Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
```
```java
    // BLOB Reading
    // 2) Syntax with ResultSet.getBlob
    String sql = "select * from orderlog where customer_id = ? and item_id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    //...
    ResultSet rs = preparedStatement.executeQuery();
    if (rs.next()) {
        //...
        File file = myAppCreateBlobFile();
        Blob blob = rs.getBlob(columnIndex);
        byte[] bytes = blob.getBytes(1, (int)blob.length());
        InputStream in = new ByteArrayInputStream(bytes);
        Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
```
### Advanced syntax with streaming techniques

The advanced syntax allows keeping memory consumption low on server side while uploading very large files (> 2 Gb).

#### BLOB creation with stream syntax

```java
    // BLOB Creation 
    // 1) Stream syntax with PreparedStatement.setBytes
    String sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    File file = myAppCreateBlobFile();
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    //...
    InputStream in = new FileInputStream(file); // Stream will be closed by the Driver
    preparedStatement.setBinaryStream(parameterIndex, in, file.length());
    //...
    preparedStatement.executeUpdate();
```

```java
    // BLOB Creation
    // 2) Stream syntax with PreparedStatement.setBlob
    String sql = "insert into orderlog values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    File file = myAppCreateBlobFile();
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    //...
    Blob blob = connection.createBlob();
    OutputStream out = blob.setBinaryStream(1);
    Files.copy(file.toPath(), out);
    preparedStatement.setBlob(parameterIndex, blob);
    //...
    preparedStatement.executeUpdate();
```

#### BLOB reading with stream syntax

```java
    // BLOB Reading
    // 1) Stream syntax with ResultSet.getBinaryStream
    String sql = "select * from orderlog where customer_id = ? and item_id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    //...
    ResultSet rs = preparedStatement.executeQuery();
    if (rs.next()) {
        //...
        File file = myAppCreateBlobFile();
        try (InputStream in = rs.getBinaryStream(columnIndex);) {
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
```
```java
    // BLOB Reading
    // 2) Stream syntax with ResultSet.getBlob
    String sql = "select * from orderlog where customer_id = ? and item_id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    //...
    ResultSet rs = preparedStatement.executeQuery();
    if (rs.next()) {
        //...
        File file = myAppCreateBlobFile();
        Blob blob = rs.getBlob(columnIndex);
        try (InputStream in = blob.getBinaryStream()) {
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
```
#### Using Progress Bars with Blobs

Using Progress Bar when inserting Blobs in a background engine requires two atomic variables:

- An `AtomicInteger` that represents the Blob transfer progress between 0 and 100.

- An `AtomicBoolean` that says if the end user has cancelled the Blob transfer. 


The atomic variables values will be shared by AceQL download/upload processes and by the Progress Monitor.

The values are to be initialized and passed to `AceQLConnection` before the JDBC actions with the static setters:

- [AceQLConnection.setProgress(AtomicInteger progress)](https://docs.aceql.com/rest/soft_java_client/9.3/javadoc/com/aceql/jdbc/commons/AceQLConnection.html#setProgress(java.util.concurrent.atomic.AtomicInteger))
- [AceQLConnection.setCancelled(AtomicBoolean cancelled)](https://docs.aceql.com/rest/soft_java_client/9.3/javadoc/com/aceql/jdbc/commons/AceQLConnection.html#setCancelled(java.util.concurrent.atomic.AtomicBoolean))


Values will then be updated and read:

- Progress value will be updated by the `AceQLConnection`.

- Canceled value will be updated to true if user cancels the task, and `AceQLConnection` will thus interrupt the Blob/Clob transfer.


Remember to always set the progress value to 100 at end of a successful or failed operation in order to close the Progress Monitor.

**Example:** 

The first step is to declare the 2 atomic variables: 

```java
    /** Progress value between 0 and 100. Will be used by progress monitor. */
    private AtomicInteger progress = new AtomicInteger();

    /** Says if user has cancelled the Blob/Clob upload or download */
    private AtomicBoolean cancelled = new AtomicBoolean();
```

The atomic variables will be passed to the `AceQLConnection` with their setter:

```java
    /**
     * SQL insert with BLOB column
     */
    private void doInsert() {
        try {
            // BEGIN MODIFY WITH YOUR VALUES
            String url = "http://localhost:9090/aceql";
            String database = "sampledb";
            String username = "username";
            String password = "password";
            String myFolder = "c:\\myFolder";
            File imageFile = new File(System.getProperty("user.home") 
                + File.separator + "image_1.jpg");
            // END MODIFY WITH YOUR VALUES

            // Register Driver 
            DriverManager.registerDriver(new AceQLDriverPro());
            Class.forName(AceQLDriverPro.class.getName());

            Properties info = new Properties();
            info.put("user", username);
            info.put("password", password);
            info.put("database", database);
            info.put("licenseKeyFolder", myFolder);

            // Open a connection
            Connection connection = DriverManager.getConnection(url, info);

            // Pass the mutable & sharable progress and canceled to the
            // underlying RemoteConnection.
            // - progress value will be updated by the RemoteConnection and
            // retrieved by SwingWorker to increment the progress.
            // - cancelled value will be updated to true if user cancels the
            // task and RemoteConnection will interrupt the Blob upload.

            ((AceQLConnection) connection).setProgress(progress);
            ((AceQLConnection) connection).setCancelled(cancelled);

            // Now run our insert
            BlobExample blobExample = new BlobExample(connection);
            // Delete if duplicate
            blobExample.deleteOrderlog(1, 1);
           	blobExample.insertOrderWithImage(1, 1, "description", 
		    	new BigDecimal("99.99"), imageFile);
            System.out.println("Blob upload done.");

        } catch (Exception e) {
            if (e instanceof SQLException && e.getCause() != null 
                && e.getCause() instanceof InterruptedException) {
                System.out.println(e.getMessage());
                return;
            }
            e.printStackTrace();
        } finally {
            // Always set progress to maximum/end value
            // to close the progress monitor
            progress.set(100);
        }
    }

```

Assuming hat you want to display a progress indicator using `SwingWorker`, you would start the preceding code as a Thread. To update the progress bar, the `SwingWorker.doInBackground()` method would be overridden as follows: 

```java
	@Override
	public Void doInBackground() {
	    cancelled.set(false);
	    progress.set(0);
	    
	    setProgress(0);
	    
	    while (progress.get() < 100) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignore) {
            }

            if (isCancelled()) {
                // If end user cancels the task, say it to mutable 
                    // & shareable cancelled. 
                    //cancelled will be read by AceQLConnection to
                // interrupt blob upload
                cancelled.set(true);
                break;
            }

            // Get the progress value between 0 and 100 that
            // is updated by doInsert in background thread
            setProgress(Math.min(progress.get(), 100));
	    }

	    return null;
	}
```

A complete example is available in [SqlProgressMonitorDemo.java](https://docs.aceql.com/rest/soft_java_client/9.3/src/SqlProgressMonitorDemo.java) and [BlobExample.java](https://docs.aceql.com/rest/soft_java_client/9.3/src/BlobExample.java) 

## HTTP session options 

You can set the http timeout values with the properties to pass at `Connection` creation:

| Property Name    | Property value                                               |
| ---------------- | ------------------------------------------------------------ |
| `connectTimeout` | Timeout value, in milliseconds, to be used when opening a communications link to the remote server. If the timeout expires before the connection can be established, a j`ava.net.SocketTimeoutException` is raised. A timeout of zero is interpreted as an infinite timeout. Defaults to `0` |
| `readTimeout`    | Read timeout to a specified timeout, in milliseconds. A non-zero value specifies the timeout when reading from Input stream when a connection is established to a resource. If the timeout expires before there is data available for read, a java.net.`SocketTimeoutException` is raised. A timeout of zero is interpreted as an infinite timeout. Defaults to `0`. |

## Using the AceQL Metadata Query API 

The AceQL Metadata Query API is an helper API that allows:

- downloading a remote database schema
  in HTML or text format,
- to get a remote database main properties,
- to get the list of tables, 
- to get the details of each table. 

It also allows wrapping remote tables, columns, indexes, etc. into easy to use provided Java classes: `Table`, `Index`, `Column`, etc.

First step is to get an instance of `RemoteDatabaseMetaData`:

```java
    RemoteDatabaseMetaData remoteDatabaseMetaData = 
        ((AceQLConnection) connection).getRemoteDatabaseMetaData();
```

### Downloading database schema into a file

Downloading a schema into a Java `File` is done through the method. See the `RemoteDatabaseMetaData` [javadoc](https://docs.aceql.com/rest/soft_java_client/9.3/javadoc/com/aceql/jdbc/commons/metadata/RemoteDatabaseMetaData.html).

```java
    File file = new File("db_schema.out.html");
    remoteDatabaseMetaData.dbSchemaDownload(file);
```

See an example of the built HTML schema:  [db_schema.out.html](https://docs.aceql.com/rest/soft_java_client/9.3/src/db_schema.out.html)

### Accessing remote database main properties

The [JdbcDatabaseMetaData](https://docs.aceql.com/rest/soft_java_client/9.3/javadoc/com/aceql/jdbc/commons/metadata/JdbcDatabaseMetaData.html) class wraps instance the main value retrieved by a remote JDBC call to `Connection.getMetaData`(): 

```java
    JdbcDatabaseMetaData jdbcDatabaseMetaData = remoteDatabaseMetaData.getJdbcDatabaseMetaData();
    System.out.println("Major Version: " + jdbcDatabaseMetaData.getDatabaseMajorVersion());
    System.out.println("Minor Version: " + jdbcDatabaseMetaData.getDatabaseMinorVersion());
    System.out.println("isReadOnly   : " + jdbcDatabaseMetaData.isReadOnly());
```

### Getting Details of Tables and Columns

See the [javadoc](https://docs.aceql.com/rest/soft_java_client/9.3/javadoc/com/aceql/jdbc/commons/metadata/package-summary.html) of the `com.aceql.jdbc.commons.metadata` package: 

```java
    System.out.println("Get the table names:");
    List<String> tableNames = remoteDatabaseMetaData.getTableNames();

    System.out.println("Print the details of each table:");
    for (String tableName : tableNames) {
        System.out.println();
        Table table = remoteDatabaseMetaData.getTable(tableName);

        System.out.println();
        System.out.println("Columns      : " + table.getColumns());
        System.out.println("Indexes      : " + table.getIndexes());
        System.out.println("Primary Keys : " + table.getPrimaryKeys());
        System.out.println("Exported Keys: " + table.getExportedforeignKeys());
        System.out.println("Imported Keys: " + table.getImportedforeignKeys());
    }
```

## Using the native JDBC Metadata Query API

### Code Sample

Standard JDBC [DatabaseMetadata](https://docs.aceql.com/rest/soft_java_client/9.3/javadoc/com/aceql/jdbc/commons/metadata/JdbcDatabaseMetaData.html)  and [ResultSetMetadata](https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSetMetaData.html) calls are fully supported.

`DatabaseMetaData` example:

```java
    // connection is an AceQL Connection
    Connection connection = DriverManager.getConnection(url, info);

    // Retrieves DatabaseMetaData
    String schema = null;
    String catalog = null;

    DatabaseMetaData databaseMetaData = connection.getMetaData();
    ResultSet rs = databaseMetaData.getColumns(schema, catalog, null, null);

    while (rs.next()) {
        String tableCat = rs.getString("TABLE_CAT");
        String tableShem = rs.getString("TABLE_SCHEM");
        String tableName = rs.getString("TABLE_NAME");
        String colName = rs.getString("COLUMN_NAME");
        String dataType = rs.getString("DATA_TYPE");

        System.out.println("TABLE_CAT  : " + tableCat);
        System.out.println("TABLE_SCHEM: " + tableShem);
        System.out.println("TABLE_NAME : " + tableName);
        System.out.println("COLUMN_NAME: " + colName);
        System.out.println("DATA_TYPE  : " + dataType);
    }
    rs.close();
```

`ResultSetMetaData` example:

```java
    // connection is an AceQL Connection
    Connection connection = DriverManager.getConnection(url, info);

    String sql = "select * from customer where customer_id = 1";
    Statement statement = connection.createStatement();
    statement.execute(sql);

    ResultSet rs = statement.getResultSet();

    // Retrieves the number, types and properties of this ResultSet object's columns.
    ResultSetMetaData resultSetMetaData = rs.getMetaData();

    int count = resultSetMetaData.getColumnCount();
    System.out.println("resultSetMetaData.getColumnCount(): " + count);

    for (int i = 1; i < count + 1; i++) {
        System.out.println();
        System.out.println("Column name     : " + resultSetMetaData.getColumnName(i));
        System.out.println("Column label    : " + resultSetMetaData.getColumnLabel(i));
        System.out.println("Column type     : " + resultSetMetaData.getColumnType(i));
        System.out.println("Column type name: " + resultSetMetaData.getColumnTypeName(i));
    }
```

### Using Database viewers with the AceQL Client JDBC Driver

The following JDBC Database viewers are supported :

[DBeaver](https://dbeaver.io/), [DbVisualizer](https://www.dbvis.com/), [JetBrains DataGrip](https://www.jetbrains.com/datagrip/), [RazorSQL](https://razorsql.com/), [SQuirreL SQL](http://squirrel-sql.sourceforge.net/)

These snapshots show usage of a remote AceQL Connection in DBeaver:

<img src="https://download.aceql.com/img/dbeaver_metadata.png" alt="DBeaver MetaData"/>



<img src="https://download.aceql.com/img/dbeaver_select.png" alt="DbVisualizer MetaData"/>

### Disabling JDBC MetaData calls when not required

`MetaData` data is automatically downloaded along with the `ResultSet` content. This default setting allows faster usage with database viewers (by eliminating separated server calls.)

If your application never calls `ResultSet.getMetaData()`, it's better to disallow the default behavior by passing the `resultSetMetaDataPolicy` property set to `off` value:

```java
    // Do not download the metadata along with ResultSet content:
    info.put("resultSetMetaDataPolicy ", "off"); 
    Connection connection = DriverManager.getConnection(url, info);
```

## Using outer authentication without a password and with an AceQL Session ID

Some working environments (Intranet, etc.) require that the client user authenticates himself *without* a password. Thus, it is not possible for this users to authenticate though the AceQL Client JDBC Driver.

In this case, you may use directly the native HTTP [login](https://github.com/kawansoft/aceql-http/blob/master/aceql-http-user-guide-api.md#login) API to authenticate the users and retrieve the `session_id` returned by the API.

The `session_id` value will be passed to the dedicated `sessionId` property:

```java
    Properties info = new Properties();
    info.put("user", user);
    info.put("database", database);
    info.put("licenseKeyFolder", "c:\\myFolder"); 

    String sessionId = getMySessionIdFromApiLogin();
    info.put("sessionId", sessionId); 

    Connection connection = DriverManager.getConnection(url, info);
```

## Passing request headers for validation on server side

You may pass any request headers to the AceQL server side for a validation process: just prefix the header name with the `request-property-` prefix and pass it as as a property to `DriverManager.getConnection(url, info)`. 

Example code that passes two request headers `token-1` & `token-2`:

```java
    info.put("request-property-token-1", "value_of_token_1");
    info.put("request-property-token-2", "value_of_token_2");

    Connection connection = DriverManager.getConnection(url, info);
```
The request headers will be intercepted on the server side by the `validate` method of your Java class that implements the [RequestHeadersAuthenticator](https://docs.aceql.com/rest/soft/11.0/javadoc/org/kawanfw/sql/api/server/auth/headers/RequestHeadersAuthenticator.html) interface.

See also the Headers Authentication sub-section in the [aceql-server.properties](https://docs.aceql.com/rest/soft/11.0/src/aceql-server.properties) file for details about declaring your `RequestHeadersAuthenticator` concrete implementation on server side.

# Limitations 

The following JDBC features are not supported nor implemented in this version. They will be added in future versions: 


- `ROWID` are not supported.
- Auto-generated keys are not supported.
- Some Advanced data types:   `Struct`, `NClob`, `SQLXML` and `Typemaps`.
- Some Statement methods: `getWarnings`, `isPoolable`/`setPoolable`, `getMoreResults`, `setCursorName`.
- Updatable Result Set.
- `RowSet` Objects.

_________