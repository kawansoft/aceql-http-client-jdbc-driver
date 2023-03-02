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

package com.aceql.jdbc.commons.main.util.framework;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Our UUID builder for PC side because old UUID idOne = UUID.randomUUID() is too slow...
 * @author Nicolas de Pomereu
 *
 */
public class UniqueIDBuilder {

    private static final AtomicInteger atomicValue = new AtomicInteger(1);
    
    /**
     * Build a unique string
     *
     * @return a unique string
     */
    public static String getUniqueId() {
        // Old version was : UUID idOne = UUID.randomUUID();
	return new SimpleDateFormat("yyyy_MM_dd_HHmm_ss").format(new Date())  + "_" + atomicValue.getAndIncrement();
    }



}
