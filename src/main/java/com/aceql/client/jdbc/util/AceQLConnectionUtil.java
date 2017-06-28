/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.                                 
 * Copyright (C) 2017,  KawanSoft SAS
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
package com.aceql.client.jdbc.util;

import java.sql.Connection;
import java.sql.ResultSet;

public class AceQLConnectionUtil {

    protected AceQLConnectionUtil() {

    }

    public static int getTransactionIsolation(String level) {
    
        if (level == null) {
            return 0;
        }
    
        if (level.equals("read_uncommitted")) {
            return Connection.TRANSACTION_READ_COMMITTED;
        } else if (level.equals("read_committed")) {
            return Connection.TRANSACTION_READ_UNCOMMITTED;
        } else if (level.equals("repeatable_read")) {
            return Connection.TRANSACTION_REPEATABLE_READ;
        } else if (level.equals("aserializable")) {
            return Connection.TRANSACTION_SERIALIZABLE;
        } else {
            return 0;
        }
    }

    public static String getTransactionIsolationAsString(
            int transactionIsolationLevel) {
    
        if (transactionIsolationLevel == Connection.TRANSACTION_NONE) {
            return "none";
        } else if (transactionIsolationLevel == Connection.TRANSACTION_READ_COMMITTED) {
            return "read_committed";
        } else if (transactionIsolationLevel == Connection.TRANSACTION_READ_UNCOMMITTED) {
            return "read_uncommitted";
        } else if (transactionIsolationLevel == Connection.TRANSACTION_REPEATABLE_READ) {
            return "repeatable_read";
        } else if (transactionIsolationLevel == Connection.TRANSACTION_SERIALIZABLE) {
            return "serializable";
        } else {
            throw new IllegalArgumentException(
        	    "Unsupported transaction isolation level: "
        		    + transactionIsolationLevel);
        }
    }

    public static int getHoldability(String holdability) {
    
        if (holdability == null) {
            return 0;
        }
    
        if (holdability.equals("hold_cursors_over_commit")) {
            return ResultSet.HOLD_CURSORS_OVER_COMMIT;
        } else if (holdability.equals("close_cursors_at_commit")) {
            return ResultSet.CLOSE_CURSORS_AT_COMMIT;
        } else {
            return 0;
        }
    }

    public static String getHoldabilityAsString(int holdability) {
    
        if (holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT) {
            return "hold_cursors_over_commit";
        } else if (holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            return "close_cursors_at_commit";
        } else {
            throw new IllegalArgumentException("Unsupported Holdability: "
        	    + holdability);
        }
    }

}
