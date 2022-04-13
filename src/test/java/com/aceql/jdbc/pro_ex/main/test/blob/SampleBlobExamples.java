/**
 * 
 */
package com.aceql.jdbc.pro_ex.main.test.blob;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.aceql.jdbc.pro_ex.main.test.DriverProLoader;

/**
 * @author Nicolas de Pomereu
 *
 */
public class SampleBlobExamples {

    private static Connection connection = null;
    private static int parameterIndex = 1;
    private static int columnIndex = 1;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	 
	connection = DriverProLoader.getConnection();
	blobCreationStandard();
	blobCreationStandard2();

	blobReadingStandard();
	blobReadingStandard2();

	blobCreationStreaming();
	blobCreationStreaming2();

	blobReadingStreaming();
	blobReadingStreaming2();
    }

    private static void blobCreationStandard() throws Exception {
	// BLOB Creation
	// 1) Syntax with PreparedStatement.setBytes
	String sql = "insert into my_table values (?, ?, ?)";
	File file = myAppCreateBlobFile();
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	// ...
	byte[] bytes = Files.readAllBytes(file.toPath());
	preparedStatement.setBytes(parameterIndex, bytes);
	// ...
	preparedStatement.executeUpdate();

    }

    private static void blobCreationStandard2() throws Exception {
	// BLOB Creation
	// 2) Syntax with PreparedStatement.setBlob
	String sql = "insert into my_table values (?, ?, ?)";
	File file = myAppCreateBlobFile();
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	// ...
	Blob blob = connection.createBlob();
	byte[] bytes = Files.readAllBytes(file.toPath());
	blob.setBytes(1, bytes);
	preparedStatement.setBlob(parameterIndex, blob);
	// ...
	preparedStatement.executeUpdate();

    }

    private static void blobReadingStandard() throws Exception {
	// BLOB Reading
	// 1) Syntax with ResultSet.getBytes
	String sql = "select * from my_table where my_key = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	// ...
	ResultSet rs = preparedStatement.executeQuery();
	if (rs.next()) {
	    // ...
	    File file = myAppCreateBlobFile();
	    byte[] bytes = rs.getBytes(columnIndex);
	    InputStream in = new ByteArrayInputStream(bytes);
	    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
    }

    private static void blobReadingStandard2() throws Exception {
	// BLOB Reading
	// 2) Syntax with ResultSet.getBlob
	String sql = "select * from my_table where my_key = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	// ...
	ResultSet rs = preparedStatement.executeQuery();
	if (rs.next()) {
	    // ...
	    File file = myAppCreateBlobFile();
	    Blob blob = rs.getBlob(columnIndex);
	    byte[] bytes = blob.getBytes(1, (int) blob.length());
	    InputStream in = new ByteArrayInputStream(bytes);
	    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
    }

    private static void blobCreationStreaming() throws Exception {
	// BLOB Creation
	// 1) Syntax with PreparedStatement.setBytes
	String sql = "insert into my_table values (?, ?, ?)";
	File file = myAppCreateBlobFile();
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	// ...
	InputStream in = new FileInputStream(file); // Stream will be closed by the Driver
	preparedStatement.setBinaryStream(parameterIndex, in, file.length());
	// ...
	preparedStatement.executeUpdate();
    }

    private static void blobCreationStreaming2() throws Exception {
	// BLOB Creation
	// 2) Syntax with PreparedStatement.setBlob
	String sql = "insert into my_table values (?, ?, ?)";
	File file = myAppCreateBlobFile();
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	// ...
	Blob blob = connection.createBlob();
	OutputStream out = blob.setBinaryStream(1);
	Files.copy(file.toPath(), out);
	preparedStatement.setBlob(parameterIndex, blob);
	// ...
	preparedStatement.executeUpdate();
    }

    private static void blobReadingStreaming() throws Exception {
	// BLOB Reading
	// 1) Syntax with ResultSet.getBinaryStream
	String sql = "select * from my_table where my_key = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	// ...
	ResultSet rs = preparedStatement.executeQuery();
	if (rs.next()) {
	    // ...
	    File file = myAppCreateBlobFile();
	    try (InputStream in = rs.getBinaryStream(columnIndex);) {
		Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    }
	}
    }

    private static void blobReadingStreaming2() throws Exception {
	// BLOB Reading
	// 2) Syntax with ResultSet.getBlob
	String sql = "select * from my_table where my_key = ?";
	PreparedStatement preparedStatement = connection.prepareStatement(sql);
	// ...
	ResultSet rs = preparedStatement.executeQuery();
	if (rs.next()) {
	    // ...
	    File file = myAppCreateBlobFile();
	    Blob blob = rs.getBlob(columnIndex);
	    try (InputStream in = blob.getBinaryStream()) {
		Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    }
	}
    }

    private static File myAppCreateBlobFile() {
	// TODO Auto-generated method stub
	return null;
    }

}
