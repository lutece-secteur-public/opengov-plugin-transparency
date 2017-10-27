/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.transparency.rs;

/**
 * Rest Constants
 */
public final class Constants 
{
    public static final String API_PATH = "transparency/api";
    public static final String VERSION_PATH = "/v{" + Constants.VERSION + "}";
    public static final String ID_PATH = "/{" + Constants.ID + "}";
    public static final String VERSION = "version";
    public static final String ID = "id";
    public static final String SEARCH_PATH = "/search={" + Constants.TEXT + "}";
    public static final String TEXT = "text";
    
    public static final String SWAGGER_DIRECTORY_PATH = "/plugins/";
    public static final String SWAGGER_PATH = "/swagger";
    public static final String SWAGGER_VERSION_PATH = "/v";
    public static final String SWAGGER_REST_PATH = "rest/";
    public static final String SWAGGER_JSON = "/swagger.json";
    
    public static final String EMPTY_OBJECT = "{}";
    public static final String ERROR_NOT_FOUND_VERSION = "Version not found";
    public static final String ERROR_NOT_FOUND_RESOURCE = "Resource not found";
    public static final String ERROR_BAD_REQUEST_EMPTY_PARAMETER = "Empty parameter";
    
    public static final String ELECTEDOFFICIAL_PATH = "/electedofficials";
    public static final String ELECTEDOFFICIAL_ATTRIBUTE_FIRST_NAME = "first_name";
    public static final String ELECTEDOFFICIAL_ATTRIBUTE_LAST_NAME = "last_name";
    public static final String ELECTEDOFFICIAL_ATTRIBUTE_TITLE = "title";
    
    public static final String LOBBY_PATH = "/lobbies";
    public static final String LOBBY_ATTRIBUTE_NAME = "name";
    public static final String LOBBY_ATTRIBUTE_NATIONAL_ID = "national_id";
    public static final String LOBBY_ATTRIBUTE_NATIONAL_ID_TYPE = "national_id_type";
    public static final String LOBBY_ATTRIBUTE_URL = "url";
    public static final String LOBBY_ATTRIBUTE_JSON_DATA = "json_data";
    public static final String LOBBY_ATTRIBUTE_VERSION_DATE = "version_date";
    
    public static final String JSON_AUTOCOMPLETE_ID_KEY = "id";
    public static final String JSON_AUTOCOMPLETE_VALUE_KEY = "value";
    public static final String JSON_AUTOCOMPLETE_LABEL_KEY = "label";
    
    public static final String MESSAGE_NO_PROPOSAL_I18N_KEY = "transparency.msg.no_proposal" ;
}
