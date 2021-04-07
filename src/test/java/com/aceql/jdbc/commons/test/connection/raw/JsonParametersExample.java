/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
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

import java.io.StringWriter;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

/**
 * @author Nicolas de Pomereu
 *
 */
public class JsonParametersExample {

    public static String buildJsonForInsertOrderlogNew(String blobId, int customerId, int itemId) {
        /*
         * CREATE TABLE orderlog ( customer_id integer not null, item_id
         * integer not null, description varchar(64) not null, item_cost
         * numeric null, date_placed date not null, date_shipped timestamp null,
         * jpeg_image longblob null, is_delivered numeric null, quantity integer
         * not null, PRIMARY KEY(customer_id, item_id) );
         */

        StringWriter sw = new StringWriter();
        JsonGenerator jsonGen = Json.createGenerator(sw);

        jsonGen.writeStartArray();

        int i = 1;
        jsonGen.writeStartObject();
        jsonGen.write("param_index", i++);
        jsonGen.write("param_type", "INTEGER");
        jsonGen.write("param_value", customerId);
        jsonGen.writeEnd();

        jsonGen.writeStartObject();
        jsonGen.write("param_index", i++);
        jsonGen.write("param_type", "INTEGER");
        jsonGen.write("param_value", itemId);
        jsonGen.writeEnd();

        jsonGen.writeStartObject();
        jsonGen.write("param_index", i++);
        jsonGen.write("param_type", "VARCHAR");
        jsonGen.write("param_value", "description_" + itemId);
        jsonGen.writeEnd();

        jsonGen.writeStartObject();
        jsonGen.write("param_index", i++);
        jsonGen.write("param_type", "NUMERIC");
        jsonGen.write("param_value", "" + (itemId * 10000));
        jsonGen.writeEnd();

        jsonGen.writeStartObject();
        jsonGen.write("param_index", i++);
        jsonGen.write("param_type", "DATE");
        jsonGen.write("param_value", System.currentTimeMillis());
        jsonGen.writeEnd();

        jsonGen.writeStartObject();
        jsonGen.write("param_index", i++);
        jsonGen.write("param_type", "TIMESTAMP");
        jsonGen.write("param_value", System.currentTimeMillis());
        jsonGen.writeEnd();

        jsonGen.writeStartObject();
        jsonGen.write("param_index", i++);
        jsonGen.write("param_type", "BLOB");
        jsonGen.write("param_value", blobId);
        jsonGen.writeEnd();

        jsonGen.writeStartObject();
        jsonGen.write("param_index", i++);
        jsonGen.write("param_type", "NUMERIC");
        jsonGen.write("param_value", 1);
        jsonGen.writeEnd();

        jsonGen.writeStartObject();
        jsonGen.write("param_index", i++);
        jsonGen.write("param_type", "INTEGER");
        jsonGen.write("param_value", itemId * 1000);
        jsonGen.writeEnd();

        jsonGen.writeEnd();
        jsonGen.close();

        String jsonPrepStmtParams = sw.toString();
        return jsonPrepStmtParams;
    }

}
