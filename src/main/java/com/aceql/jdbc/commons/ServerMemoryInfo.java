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
 * Contains health check Java memory info of the AceQL server running instance. 
 * @author Nicolas de Pomereu
 *
 */
public class ServerMemoryInfo {

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
     * Returns the amount of memory in bytes that the Java virtual machine
     * initially requests from the operating system for memory management.
     * This method returns <tt>-1</tt> if the initial memory size is undefined.
     *
     * @return the initial size of memory in bytes;
     */
    public long getInitMemory() {
        return initMemory;
    }
    
    /**
     * Returns the amount of used memory in bytes.
     *
     * @return the amount of used memory in bytes.
     *
     */
    public long getUsedMemory() {
        return usedMemory;
    }
    
    /**
     * Returns the maximum amount of memory in bytes that can be
     * used for memory management.  This method returns <tt>-1</tt>
     * if the maximum memory size is undefined.
     *
     * <p> This amount of memory is not guaranteed to be available
     * for memory management if it is greater than the amount of
     * committed memory.  The Java virtual machine may fail to allocate
     * memory even if the amount of used memory does not exceed this
     * maximum size.
     *
     * @return the maximum amount of memory in bytes;
     * <tt>-1</tt> if undefined.
     */
    public long getMaxMemory() {
        return maxMemory;
    }
    
    /**
     * Returns the amount of memory in bytes that is committed for
     * the Java virtual machine to use.  This amount of memory is
     * guaranteed for the Java virtual machine to use.
     *
     * @return the amount of committed memory in bytes.
     *
     */
    
    public long getCommittedMemory() {
        return committedMemory;
    }
    
    @Override
    public String toString() {
	return "ServerMemoryInfo [initMemory=" + initMemory + ", usedMemory=" + usedMemory + ", maxMemory=" + maxMemory
		+ ", committedMemory=" + committedMemory + "]";
    }
    

}
