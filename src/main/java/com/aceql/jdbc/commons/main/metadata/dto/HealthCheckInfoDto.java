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
package com.aceql.jdbc.commons.main.metadata.dto;

/**
 * Contains the health check info to send to client side.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class HealthCheckInfoDto {

    private String status = "OK";

    private long initMemory;
    private long usedMemory;
    private long maxMemory;
    private long committedMemory;

    /**
     * @return the status
     */
    public String getStatus() {
	return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
	this.status = status;
    }

    /**
     * @return the initMemory
     */
    public long getInitMemory() {
	return initMemory;
    }

    /**
     * @param initMemory the initMemory to set
     */
    public void setInitMemory(long initMemory) {
	this.initMemory = initMemory;
    }

    /**
     * @return the usedMemory
     */
    public long getUsedMemory() {
	return usedMemory;
    }

    /**
     * @param usedMemory the usedMemory to set
     */
    public void setUsedMemory(long usedMemory) {
	this.usedMemory = usedMemory;
    }

    /**
     * @return the maxMemory
     */
    public long getMaxMemory() {
	return maxMemory;
    }

    /**
     * @param maxMemory the maxMemory to set
     */
    public void setMaxMemory(long maxMemory) {
	this.maxMemory = maxMemory;
    }

    /**
     * @return the committedMemory
     */
    public long getCommittedMemory() {
	return committedMemory;
    }

    /**
     * @param committedMemory the committedMemory to set
     */
    public void setCommittedMemory(long committedMemory) {
	this.committedMemory = committedMemory;
    }

    @Override
    public String toString() {
	return "HealthCheckInfoDto [status=" + status + ", initMemory=" + initMemory + ", usedMemory=" + usedMemory
		+ ", maxMemory=" + maxMemory + ", committedMemory=" + committedMemory + "]";
    }

}
