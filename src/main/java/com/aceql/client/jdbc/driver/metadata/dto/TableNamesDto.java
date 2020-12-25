/*
 * This file is part of AceQL Client SDK.
 * AceQL Client SDK: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (C) 2020,  KawanSoft SAS
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
package com.aceql.client.jdbc.driver.metadata.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the list of tables of the database.
 * @author Nicolas de Pomereu
 *
 */
public class TableNamesDto {

    private String status = "OK";
    private List<String> tableNames = new ArrayList<>();

    public TableNamesDto(List<String> tableNames) {
	this.tableNames = tableNames;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    @Override
    public String toString() {
	return "TableNamesDto [status=" + status + ", tableNames=" + tableNames + "]";
    }

}
