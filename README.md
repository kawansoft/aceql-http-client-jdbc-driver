# AceQL HTTP 

## Java Client SDK v4.0.2 - April 2020, 7

<img src="https://www.aceql.com/favicon.png" alt="AceQ HTTP Icon"/>

  * [Fundamentals](#fundamentals)
      * [Technical operating environment](#technical-operating-environment)
      * [License](#license)
      * [AceQL Server side compatibility](#aceql-server-side-compatibility)
      * [SDK instead of JDBC Driver](#sdk-instead-of-jdbc-driver)
      * [AceQL Java Client SDK installation](#aceql-java-client-sdk-installation)
         * [Maven](#maven)
         * [Single Jar](#single-jar)
         * [Android Project settings](#android-project-settings)
      * [Data transport](#data-transport)
         * [Transport format](#transport-format)
         * [Content streaming and memory management](#content-streaming-and-memory-management)
      * [Best practices for fast response time](#best-practices-for-fast-response-time)
   * [Using the AceQL Java Client SDK](#using-the-aceql-java-client-sdk)
      * [Connection creation](#connection-creation)
      * [Using a Proxy](#using-a-proxy)
      * [Handling Exceptions](#handling-exceptions)
         * [The error type](#the-error-type)
         * [Most common AceQL Server messages](#most-common-aceql-server-messages)
         * [HTTP Status Codes](#http-status-codes)
      * [Data types](#data-types)
      * [SQL Transactions and Connections modifiers](#sql-transactions-and-connections-modifiers)
      * [BLOB management](#blob-management)
         * [BLOB creation](#blob-creation)
         * [BLOB reading](#blob-reading)
         * [Using Progress Bars with Blobs](#using-progress-bars-with-blobs)
      * [HTTP session options](#http-session-options)
   * [Using the Metadata Query API](#using-the-metadata-query-api)
      * [Downloading database schema into a file](#downloading-database-schema-into-a-file)
      * [Accessing remote database main properties](#accessing-remote-database-main-properties)
      * [Getting Details of Tables and Columns](#getting-details-of-tables-and-columns)
   * [Limitations](#limitations)


# Fundamentals 

This document describes how to use the AceQL Java Client SDK and gives some details about how it operates with the server side.

The AceQL Java Client SDK allows users to wrap the [AceQL HTTP APIs](https://github.com/kawansoft/aceql-http/blob/master/aceql-http-5.0.1-user-guide-api.md) and eliminate the tedious work of handling communication errors and parsing JSON results.

Android and Java Desktop application developers can access remote SQL databases and/or SQL databases in the cloud, simply by including standard JDBC calls in their code, just like they would for a local database.

The AceQL Server operation is described in [AceQL HTTP Server Installation and Configuration Guide](https://github.com/kawansoft/aceql-http/blob/master/README.md), whose content is sometimes referred in this User Guide. 

## Technical operating environment 

The AceQL Java Client SDK is entirely written in Java, and functions identically with Microsoft Windows, Linux, and all versions of UNIX supporting Java 7+.

The only required third party installation is a recent JVM:

| OS                                       | **JVM (Java Virtual Machine)** |
| ---------------------------------------- | ------------------------------ |
| Android                                  | Android 4.1+                   |
| Windows <br>UNIX/Linux<br>OS X  / mac OS | Java 7+                        |

## License

The SDK is licensed with the liberal [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) license.

## AceQL Server side compatibility

This 4.0.x SDK version is compatible with AceQL HTTP server side v2.1, v3.0, v3.1 and v4.0. 

AceQL HTTP v4.0+ is required for metadata calls.

## SDK instead of JDBC Driver

Note that the SDK is *not *a real JDBC Driver, because it lacks important metadata call capabilities:

- `Connection.getMetaData()`.
- `ResultSet.getMetaData()`.

Because Metadata calls are not supported, we decided not to package the SDK as a real JDBC Driver. It could not be used with third party database query tools, thus it would be misleading to present it as a real JDBC Driver.

Note that we will soon release a real JDBC Driver. Please contact us at [contact@kawansoft.com](mailto:contact@kawansoft.com) if you would like more information.

## AceQL Java Client SDK installation

### Maven

```xml
<dependency>
    <groupId>com.aceql</groupId>
    <artifactId>aceql-http-client-sdk</artifactId>
    <version>4.0.2</version>
</dependency>
```
### Single Jar 

For non Maven users: a single jar with all dependencies is available on the [download page](https://www.aceql.com/aceql-download-page.html).

### Android Project settings

 Add the following3 lines to your AndroidManifest.xml:

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



Large content (ResultSet, Blobs/Clobs…) is transferred using files. It is never loaded in memory. Streaming techniques are always used to read and write this content.

## Best practices for fast response time

Every HTTP exchange between the client and server side is time-consuming, because the HTTP call is synchronous and waits for the server's response

Try to avoid coding JDBC calls inside loops, as this can reduce execution speed. Each JDBC call will send an HTTP request and wait for the response from the server.

Note that AceQL is optimized as much as possible:

- A SELECT call returning a huge Result Set will not consume memory on the server or client side:  AceQL uses input stream and output stream I/O for ResultSet transfer.

- Result Set retrieval is as fast as possible:  
  - The `ResultSet` creation is done once on the server by the `executeQuery()`.
  - The rows are all dumped at once on the servlet output stream by the server.
  - The client side gets the ResultSet content as a file.
  - All `ResultSet` navigation commands are executed locally on the client side by navigating through the file:  `next()`, `prev(`), `first()`, `last()`, etc. 

# Using the AceQL Java Client SDK

We will use the same `sampledb` database for all our code samples. 

The schema is available here: [sampledb.txt](http://www.aceql.com/rest/soft/5.0.1/src/sampledb.txt). 

## Connection creation

The  `Connection` to the remote database is created using AceQL’s [AceQLConnection](https://www.aceql.com/rest/soft/5.0.1/javadoc_sdk/com/aceql/client/jdbc/AceQLConnection.html) class and passing the URL of the `ServerSqlManager` Servlet of your server configuration:

```java
  // The URL of the AceQL Server servlet
  // Port number is the port number used to start the Web Server:
  String url = "https://www.acme.com:9443/aceql";

  // The remote database to use:
  String database = "sampledb";

  // (username, password) for authentication on server side.
  // No authentication will be done for our Quick Start:
  String username = "MyUsername";
  char [] password = { 'M', 'y', 'S', 'e', 'c', 'r', 'e', 't'}; 

  // Attempt to establish a connection to the remote database:
  Connection connection = new AceQLConnection(url, database, username,
      password);
```
From now on, you can use the connection to execute updates and queries on the remote database, using standard and unmodified JDBC calls. 

## Using a Proxy

Communication via a proxy server is done using a `java.net.Proxy` instance.

If proxy requires authentication, pass the credentials using  a `java.net.PasswordAuthentication` instance:

```java
  // Proxy info
  String proxyHost = "localhost";
  int proxyPort = 8080;
  String proxyUsername = "myProxyUsername";
  char[] proxyPassword = { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };

  Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
  proxyHost, proxyPort));

  PasswordAuthentication authentication = new PasswordAuthentication(
  proxyUsername, proxyPassword);

  // Attempt to establish a connection to the remote database using a
  // Proxy
  Connection connection = new AceQLConnection(url, database, username,
  password, proxy, authentication);
```

## Handling Exceptions

Except for `NullPointerException`, exceptions thrown are always an instance of [AceQLException](https://www.aceql.com/rest/soft/5.0.1/javadoc_sdk/com/aceql/client/jdbc/AceQLException.html).

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

Boolean, Blob/Clob, Integer, Short, Double, Float, BigDecimal, Long,String, Date, Time, Timestamp, URL and Array.  

## SQL Transactions and Connections modifiers

The AceQLSDK support SQL transactions with:

- `commit()`
- `rollback()`
- `setAutoCommit(boolean autoCommit)`

The following Connections modifiers calls are supported in this version:

- `setHoldability(int holdability)`

- `setTransactionIsolation(int level)`

- `setReadOnly(boolean readOnly)`


## BLOB management

The AceQL SDK supports BLOB creation and reading. Methods are implemented using streaming techniques to keep memory consumption low, both on the client and server sides.

CLOB are not supported in this version.

### BLOB creation

BLOB creation is supported through `PreparedStatement.setBinaryStream()`:

```java
    /**
     * An INSERT example with a Blob.
     */
    public void insertOrderWithImage(int customerId, int itemNumber,
	    String itemDescription, BigDecimal itemCost, File imageFile)
	    throws SQLException, IOException {

	// Some databases require to be in a transaction for BLOB actions
	connection.setAutoCommit(false);

	try {

	    String sql = "insert into orderlog "
		    + "values ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	    // We will insert a Blob (the image of the product).
	    // The transfer will be done in streaming both on the client
	    // and on the Server: we can upload/download very big files.
	    InputStream in = new BufferedInputStream(new FileInputStream(
		    imageFile));

	    // Create a new Prepared Statement
	    PreparedStatement prepStatement = connection.prepareStatement(sql);

	    int i = 1;
	    long theTime = new java.util.Date().getTime();
	    java.sql.Date theDate = new java.sql.Date(theTime);
	    Timestamp theTimestamp = new Timestamp(theTime);

	    prepStatement.setInt(i++, customerId);
	    prepStatement.setInt(i++, itemNumber);
	    prepStatement.setString(i++, itemDescription);
	    prepStatement.setBigDecimal(i++, itemCost);
	    prepStatement.setDate(i++, theDate);
	    prepStatement.setTimestamp(i++, theTimestamp);
	    prepStatement.setBinaryStream(i++, in, (int) imageFile.length());
	    prepStatement.setInt(i++, 0);
	    prepStatement.setInt(i++, 1);

	    prepStatement.executeUpdate();
	    prepStatement.close();
	} catch (Exception e) {
	    connection.rollback();
	    throw e;
	} finally {
	    connection.setAutoCommit(true);
	}
  }
```

### BLOB reading 

BLOB reading is supported through `PreparedStatement.setBinaryStream()`:

```java
   /**
     * A SELECT example with a BLOB.
     */
    public void selectOrdersForCustomerWithImage(int customerId, int itemId,
	    File imageFile) throws SQLException, IOException {

	// Some databases require to be in a transaction for BLOB actions
	connection.setAutoCommit(false);

	try {

	    String sql = "select customer_if, order_id, jpeg_image "
	    	+ "from orderlog where customer_id = ? and item_id = ?";

	    PreparedStatement prepStatement = connection.prepareStatement(sql);
	    int i = 1;
	    prepStatement.setInt(i++, customerId);
	    prepStatement.setInt(i++, itemId);

	    ResultSet rs = prepStatement.executeQuery();

	    if (rs.next()) {
		int customer_id = rs.getInt("customer_id");
		int item_id = rs.getInt("item_id");

		// Get BLOB from remote server and store it on disk:
		try (InputStream in = rs.getBinaryStream("jpeg_image")) {
		    Files.copy(in, imageFile.toPath());
		}

		System.out.println();
		System.out.println("customer_id : " + customer_id);
		System.out.println("item_id     : " + item_id);
		System.out.println("jpeg_image  : " + imageFile);

	    }

	    prepStatement.close();
	    rs.close();
	} catch (Exception e) {
	    connection.rollback();
	    throw e;
	} finally {
	    connection.setAutoCommit(true);
	}
 }
```

### Using Progress Bars with Blobs

Using Progress Bar when inserting Blobs in a background engine requires two atomic variables:

- An `AtomicInteger` that represents the Blob transfer progress between 0 and 100.

- An `AtomicBoolean` that says if the end user has cancelled the Blob transfer. 


The atomic variables values will be shared by AceQL download/upload processes and by the Progress Monitor.

The values are to be initialized and passed to `AceQLConnection` before the JDBC actions with the static setters:

- [AceQLConnection.setProgress(AtomicInteger progress)](https://www.aceql.com/rest/soft/5.0.1/javadoc_sdk/com/aceql/client/jdbc/AceQLConnection.html#setProgress(java.util.concurrent.atomic.AtomicInteger))

- [AceQLConnection.setCancelled(AtomicBoolean cancelled)](https://www.aceql.com/rest/soft/5.0.1/javadoc_sdk/com/aceql/client/jdbc/AceQLConnection.html#setCancelled(java.util.concurrent.atomic.AtomicBoolean))


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
     *SQL insert with BLOB column
     */
    private void doInsert() {
	try {
	    // BEGIN MODIFY WITH YOUR VALUES
	    String userHome = System.getProperty("user.home");

	    // Port number is the port number used to start the Web Server:
	    String url = "https://www.acme.com:9443/aceql";
	    String database = "kawansoft_example";
	    String username = "username";
	    char [] password = { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
	    File imageFile = 
		    new File(userHome + File.separator + "image_1.jpg");
	    // END MODIFY WITH YOUR VALUES

	    // Attempts to establish a connection to the remote database:
	    Connection connection = new AceQLConnection(url, database,
		    username, password);

	    // Pass the mutable & shareable progress and canceled to the
	    // underlying AceQLConnection.
	    // - progress value will be updated by the AceQLConnection and
	    // retrieved by SwingWorker to increment the progress.
	    // - cancelled value will be updated to true if user cancels the
	    // task and AceQLConnection will interrupt the Blob upload.

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
	    // Always set progress to maximum/end value to close the progress
	    // monitor
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

A complete example is available in [SqlProgressMonitorDemo.java](http://www.aceql.com/rest/soft/5.0.1/src/SqlProgressMonitorDemo.java) and [BlobExample.java](https://www.aceql.com/rest/soft/5.0.1/src/BlobExample.java) 

## HTTP session options 

You can set the http timeout values with the static setters to be called before `AceQLConnection` creation:

- [AceQLConnection.setConnectTimeout(int connectTimeout)](https://www.aceql.com/rest/soft/5.0.1/javadoc_sdk/com/aceql/client/jdbc/AceQLConnection.html#setConnectTimeout(int))
- [AceQLConnection.setReadTimeout(int readTimeout)](https://www.aceql.com/rest/soft/5.0.1/javadoc_sdk/com/aceql/client/jdbc/AceQLConnection.html#setReadTimeout(int))

# Using the Metadata Query API 

The metadata API allows:

- downloading a remote database schema
  in HTML or text format
- to get a remote database main properties.
- to get the list of tables, 
- to get the details of each table. 

It also allows wrapping remote tables, columns, indexes, etc. into
easy to use provided Java classes: Table, Index, Column, etc.

First step is to get an instance of `RemoteDatabaseMetaData`:

```java
RemoteDatabaseMetaData remoteDatabaseMetaData = 
    ((AceQLConnection) connection).getRemoteDatabaseMetaData();
```

## Downloading database schema into a file

Downloading a schema into a Java `File` is done through the method. See the `RemoteDatabaseMetaData` [javadoc](https://www.aceql.com/rest/soft/5.0.1/javadoc_sdk/com/aceql/client/metadata/RemoteDatabaseMetaData.html): 

```java
File file = new File("db_schema.out.html");
remoteDatabaseMetaData.dbSchemaDownload(file);
```
See an example of the built HTML schema:  [db_schema.out.html](https://www.aceql.com/rest/soft/5.0.1/src/db_schema.out.html)

## Accessing remote database main properties

The [JdbcDatabaseMetaData](https://www.aceql.com/rest/soft/5.0.1/javadoc_sdk/com/aceql/client/metadata/JdbcDatabaseMetaData.html) class wraps instance the main value retrieved by a remote JDBC call to `Connection.getMetaData`(): 

```java
JdbcDatabaseMetaData jdbcDatabaseMetaData = remoteDatabaseMetaData.getJdbcDatabaseMetaData();
	System.out.println("Major Version: " + jdbcDatabaseMetaData.getDatabaseMajorVersion());
	System.out.println("Minor Version: " + jdbcDatabaseMetaData.getDatabaseMinorVersion());
	System.out.println("isReadOnly   : " + jdbcDatabaseMetaData.isReadOnly());
```

## Getting Details of Tables and Columns

See the [javadoc](https://www.aceql.com/rest/soft/5.0.1javadoc_sdk/com/aceql/client/metadata/package-summary.html) of the `com.aceql.client.metadata` package: 

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
# Limitations 

The following JDBC features are not supported nor implemented in this version: 

- Metadata calls are not supported:
  - `Connection.getMetaData()`
  - `ResultSet.getMetaData()`.
- Savepoints are not supported.
- Batch methods are not supported.
- BLOB syntax is limited in `PreparedStatement` and in `ResultSet`.
- There are no `java.sql.Blob` and `java.sql.Clob` interface implementation.
- CLOB are not supported.


- `ROWID` are not supported.
- Auto-generated keys are not supported.
- Advanced data types:   `Struct`, `NClob`, `SQLXML` and `Typemaps`.
- Some Statement methods: `getWarnings`, `isPoolable`/`setPoolable`, `getMoreResults`, `setCursorName`.
- Updatable Result Set.
- `RowSet` Objects.

_________
