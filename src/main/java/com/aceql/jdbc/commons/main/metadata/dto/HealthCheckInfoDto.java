/*
 * Copyright (c)2022 KawanSoft S.A.S. All rights reserved.
 * 
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file in the project's root directory.
 *
 * Change Date: 2026-09-01
 *
 * On the date above, in accordance with the Business Source License, use
 * of this software will be governed by version 2.0 of the Apache License.
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
