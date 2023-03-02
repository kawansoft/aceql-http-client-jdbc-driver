/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (c) 2023,  KawanSoft SAS
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
package com.aceql.jdbc.commons.main.advanced.caller;

import java.sql.Array;

import com.aceql.jdbc.commons.main.advanced.jdbc.AceQLArray;
import com.aceql.jdbc.commons.main.metadata.util.GsonWsUtil;

/**
 * Allows to get an Array using Reflection.
 * @author Nicolas de Pomereu
 *
 */
public class ArrayGetter {

    public Array getArray(String value) {
	Array array  = GsonWsUtil.fromJson(value, AceQLArray.class);
	return array;
    }

}
