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
package com.aceql.client.jdbc.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Stores the session Id per serverUrl/username/database triplet in order to
 * get new AceQL Connection with /get_connection without new login action.
 *
 * @author Nicolas de Pomereu
 *
 */
public class UserLoginStore {

    private String serverUrl = null;
    private String username = null;
    private String database = null;

    /** The map of logged users (serverUrl/username/database, session_id) */
    private static Map<String, String> loggedUsers = new HashMap<>();

    /**
     * Constructor
     * @param serverUrl	the AceQL server URL
     * @param username	the client username
     * @param database	the database to which users wants to connect
     */
    public UserLoginStore(String serverUrl, String username, String database) {
	this.serverUrl = Objects.requireNonNull(serverUrl, "serverUrl cannot be null!");
	this.username = Objects.requireNonNull(username, "username cannot be null!");
	this.database = Objects.requireNonNull(database, "database cannot be null!");
    }

    /**
     * Says if user is already logged (ie. it exist a session_if for (serverUrl, username, database) triplet.
     * @return true if user is already logged
     */
    public boolean isAlreadyLogged() {
	String key = buildKey();
	return loggedUsers.containsKey(key);
    }

    /**
     * Returns the session Id of logged user with (serverUrl, username, database) triplet.
     * @return	the stored session Id for the (serverUrl, username, database) triplet.
     */
    public String getSessionId() {
	String key = buildKey();
	String sessionId = loggedUsers.get(key);
	return sessionId;
    }

    /**
     * Stores the session Id of a logged user with (serverUrl, username, database) triplet.
     * @param sessionId  the session Id of a logged user
     */
    public void setSessionId(String sessionId) {
	String key = buildKey();
	loggedUsers.put(key, sessionId);
    }

    /**
     * Removes (serverUrl, username, database) triplet. This is to be called at /logout API.
     */
    public void remove() {
	String key = buildKey();
	loggedUsers.remove(key);
    }

    /**
     * Builds the Map key for the (serverUrl, username, database) triplet key.
     * @return the built (serverUrl, username, database) triplet key.
     */
    private String buildKey() {
	return serverUrl + "/" + username + "/" + database;
    }

}
