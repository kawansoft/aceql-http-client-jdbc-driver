/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.                                 
 * Copyright (C) 2021,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.                                
 *                                                                               
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.aceql.jdbc.commons.test.connection.raw;


/**
 * @author Nicolas de Pomereu
 *
 */
public class JsonResultSetExample {

//    /**
//     * @param args
//     */
//    
//    public static void main(String[] args) throws Exception {
//
//	String pathname = "c:\\test\\out\\orderlog.txt";
//	//pathname = "c:\\test\\json_tests\\jsonError.txt";
//	//pathname = "c:\\test\\json_tests\\jsonNoRows.txt";
//	
//	File file = new File(pathname);
//	AceQLResultSet aceQLResultSet = new AceQLResultSet(file, null);
//	aceQLResultSet.setTraceOn(false);
//	
//	int customer_id;
//	int item_id;
//	String description;
//	BigDecimal item_cost;
//	Date date_placed;
//	Timestamp date_shipped;
//	String jpeg_image = null;
//	boolean is_delivered;
//	int quantity;
//	
//	while (aceQLResultSet.next()) {
//	    customer_id = aceQLResultSet.getInt("customer_id");
//	    item_id = aceQLResultSet.getInt("item_id");
//	    description = aceQLResultSet.getString("description");
//	    item_cost = aceQLResultSet.getBigDecimal("item_cost");
//	    date_placed = aceQLResultSet.getDate("date_placed");
//	    date_shipped = aceQLResultSet.getTimestamp("date_shipped");
//	    jpeg_image = aceQLResultSet.getString("jpeg_image");
//	    is_delivered = aceQLResultSet.getBoolean("is_delivered");
//	    quantity = aceQLResultSet.getInt("quantity");
//	    
//	    System.out.println();
//	    System.out.println("column customer_id     : " + customer_id);
//	    System.out.println("column item_id         : " + item_id);
//	    System.out.println("column description     : " + description);
//	    System.out.println("column item_cost      : " + item_cost);
//	    System.out.println("column date_placed     : " + date_placed);
//	    System.out.println("column date_shipped    : " + date_shipped);
//	    System.out.println("column jpeg_image      : " + jpeg_image);
//	    System.out.println("column is_delivered    : " + is_delivered);
//	    System.out.println("column quantity        : " + quantity);
//	    
//	    int i = 1;
//	    customer_id = aceQLResultSet.getInt(i++);
//	    item_id = aceQLResultSet.getInt(i++);
//	    description = aceQLResultSet.getString(i++);
//	    item_cost = aceQLResultSet.getBigDecimal(i++);
//	    date_placed = aceQLResultSet.getDate(i++);
//	    date_shipped = aceQLResultSet.getTimestamp(i++);
//	    jpeg_image = aceQLResultSet.getString(i++);
//	    is_delivered = aceQLResultSet.getBoolean(i++);
//	    quantity = aceQLResultSet.getInt(i++);
//	    
//	    System.out.println();
//	    System.out.println("index customer_id      : " + customer_id);
//	    System.out.println("index item_id          : " + item_id);
//	    System.out.println("index description      : " + description);
//	    System.out.println("index item_cost       : " + item_cost);
//	    System.out.println("index date_placed      : " + date_placed);
//	    System.out.println("index date_shipped     : " + date_shipped);
//	    System.out.println("index jpeg_image       : " + jpeg_image);
//	    System.out.println("index is_delivered     : " + is_delivered);
//	    System.out.println("index quantity         : " + quantity);
//	    
//	    
//	}
//	
//	aceQLResultSet.close();
//    }

}
