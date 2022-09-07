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
package com.aceql.jdbc.commons;

/**
 * Contains health check info about running AceQL HTTP on remote server.
 * @author Nicolas de Pomereu
 *
 */
public class HealthCheckInfo {

    private long initMemory = -1;
    private long usedMemory = -1;
    private long maxMemory = -1;
    private long committedMemory = -1;
    /**
     * @param initMemory the initMemory to set
     */
    void setInitMemory(long initMemory) {
        this.initMemory = initMemory;
    }
    /**
     * @param usedMemory the usedMemory to set
     */
    void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }
    /**
     * @param maxMemory the maxMemory to set
     */
    void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }
    /**
     * @param committedMemory the committedMemory to set
     */
     void setCommittedMemory(long committedMemory) {
        this.committedMemory = committedMemory;
    }
    /**
     * @return the initMemory
     */
    public long getInitMemory() {
        return initMemory;
    }
    /**
     * @return the usedMemory
     */
    public long getUsedMemory() {
        return usedMemory;
    }
    /**
     * @return the maxMemory
     */
    public long getMaxMemory() {
        return maxMemory;
    }
    /**
     * @return the committedMemory
     */
    public long getCommittedMemory() {
        return committedMemory;
    }
    @Override
    public String toString() {
	return "HealthCheckInfo [initMemory=" + initMemory + ", usedMemory=" + usedMemory + ", maxMemory=" + maxMemory
		+ ", committedMemory=" + committedMemory + "]";
    }
    

}
