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

package com.aceql.jdbc.commons.main.util.json;

/**
 * @author Nicolas de Pomereu
 *
 */
public class SqlParameter {

    private int parameterIndex = -1;
    private String parameterType = null;
    private String parameterValue = null;


    /**
     * Constructor to add a Prepared Statement IN parameter.
     * @param parameterIndex	the index
     * @param parameterType	the SQL Type
     * @param parameterValue	the value
     */
    public SqlParameter(int parameterIndex, String parameterType, String parameterValue) {
	this.parameterIndex = parameterIndex;
	this.parameterType = parameterType;
	this.parameterValue = parameterValue;

	if (this.parameterValue == null) {
	    this.parameterValue = "NULL";
	}
    }

    /**
     * @return the parameterIndex
     */
    public int getParameterIndex() {
        return parameterIndex;
    }

    /**
     * @return the parameterType
     */
    public String getParameterType() {
        return parameterType;
    }

    /**
     * @return the parameterValue
     */
    public String getParameterValue() {
        return parameterValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "SqlParameter [parameterIndex=" + parameterIndex + ", parameterType=" + parameterType
		+ ", parameterValue=" + parameterValue + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + parameterIndex;
	result = prime * result + ((parameterType == null) ? 0 : parameterType.hashCode());
	result = prime * result + ((parameterValue == null) ? 0 : parameterValue.hashCode());
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	SqlParameter other = (SqlParameter) obj;
	if (parameterIndex != other.parameterIndex)
	    return false;
	if (parameterType == null) {
	    if (other.parameterType != null)
		return false;
	} else if (!parameterType.equals(other.parameterType))
	    return false;
	if (parameterValue == null) {
	    if (other.parameterValue != null)
		return false;
	} else if (!parameterValue.equals(other.parameterValue))
	    return false;
	return true;
    }



}
