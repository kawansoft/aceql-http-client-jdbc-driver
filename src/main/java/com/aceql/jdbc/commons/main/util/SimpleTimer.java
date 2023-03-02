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
package com.aceql.jdbc.commons.main.util;

/**
 * Allows easy performances measures in milliseconds.
 * <br>
 * <ul>
 * <li>Create instance for timing start.</li>
 * <li>call stop().</li>
 * <li>call getElapsedMs() for the result.</li>
 * </ul>
 * @author Nicolas de Pomereu
 *
 */
public class SimpleTimer {

    private long begin = 0;
    private long end = 0;

    /**
     * Constructor.Begins the time measure.
     */
    public SimpleTimer() {
	this.end = 0;
	this.begin = System.currentTimeMillis();
    }

    /**
     * Returns SimpleTimer
     * @return the elapsed time in milliseconds between constructor creation and stop() call.
     */
    public long getElapsedMs() {
	end = System.currentTimeMillis();
	if (begin > end) {
	    // If time not stopped, no measurement can be done
	    return -1;
	}

	return (end - begin);
    }


    @Override
    public String toString() {
	return "SimpleTimer [begin=" + begin + ", end=" + end + "]";
    }

}
