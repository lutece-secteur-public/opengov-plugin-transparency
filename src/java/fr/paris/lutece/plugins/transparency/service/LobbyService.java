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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.transparency.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.transparency.business.Lobby;
import fr.paris.lutece.plugins.transparency.business.LobbyHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import java.io.IOException;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Lobby tools
 */
public final class LobbyService
{

    // Plugin properties
    private static final String PROPERTY_URL_LOBBY_LIST_REFERENCE = "lobby.json.list.url";

    // Msg
    private static final String MSG_SYNCHRO_KEY = "transparency.message.synchro";
    private static final String MSG_ERROR_GET_JSON = "transparency.message.synchro.error";

    // constants (for synchro)
    private static final String CONSTANT_KEY_PUBLICATIONS = "publications";
    private static final String CONSTANT_KEY_DENOMINATION = "denomination";
    private static final String CONSTANT_KEY_IDENTIFIANTNATIONAL = "identifiantNational";
    private static final String CONSTANT_KEY_TYPEIDENTIFIANTNATIONAL = "typeIdentifiantNational";
    private static final String CONSTANT_KEY_LIENSITEWEB = "lienSiteWeb";

    /**
     * Refresh the lobby data base
     *
     * @param locale
     * @return The Jsp URL of the process result
     */
    public static String synchronizeLobbies( Locale locale )
    {

        int nbLobby = 0;
        int nbLobbyCreated = 0;

        try
        {
            String strUri = AppPropertiesService.getProperty( PROPERTY_URL_LOBBY_LIST_REFERENCE );

            HttpAccess ha = new HttpAccess( );

            Map<String, String> headersRequest = new HashMap<>( );
            Map<String, String> headersResponse = new HashMap<>( );

            String strJson = ha.doGet( strUri, null, null, headersRequest, headersResponse );

            if ( strJson == null )
            {
                String msg = I18nService.getLocalizedString( MSG_ERROR_GET_JSON, locale );
                msg = MessageFormat.format( msg, strUri );

                return msg;
            }

            ObjectMapper mapper = new ObjectMapper( );
            JsonNode jsonNode = null;

            try
            {
                jsonNode = mapper.readTree( strJson );
            }
            catch( IOException e )
            {
                String msg = I18nService.getLocalizedString( MSG_ERROR_GET_JSON, locale );
                msg = MessageFormat.format( msg, strUri );

                AppLogService.error( e );
                return msg;
            }

            // Parse lobbies
            Iterator<JsonNode> lobbyList = jsonNode.path( CONSTANT_KEY_PUBLICATIONS ).elements( );

            while ( lobbyList.hasNext( ) )
            {
                nbLobby++;
                Lobby lobby = jsonToLobby( lobbyList.next( ) );

                Lobby existingLobby = LobbyHome.getByNationalId( lobby.getNationalId( ) );

                if ( existingLobby != null )
                {
                    // update existing lobby
                    lobby.setId( existingLobby.getId( ) );
                    LobbyHome.update( lobby );
                }
                else
                {
                    // insert new lobby
                    LobbyHome.create( lobby );
                    nbLobbyCreated++;
                }
            }

            String msg = I18nService.getLocalizedString( MSG_SYNCHRO_KEY, locale );
            msg = MessageFormat.format( msg, nbLobby, nbLobbyCreated, nbLobby - nbLobbyCreated );

            return msg;

        }
        catch( HttpAccessException e )
        {
            AppLogService.error( e );
            return e.getLocalizedMessage( );
        }
    }

    /**
     * Parse Json to populate a Lobby bean
     * 
     * @param jsonLobby
     * @return the lobby bean
     */
    private static Lobby jsonToLobby( JsonNode jsonLobby )
    {
        Lobby lobby = new Lobby( );

        lobby.setName( jsonLobby.get( CONSTANT_KEY_DENOMINATION ).asText( ) );
        lobby.setNationalId( jsonLobby.get( CONSTANT_KEY_IDENTIFIANTNATIONAL ).asText( ) );
        if ( jsonLobby.has( CONSTANT_KEY_TYPEIDENTIFIANTNATIONAL ) )
            lobby.setNationalIdType( jsonLobby.get( CONSTANT_KEY_TYPEIDENTIFIANTNATIONAL ).asText( ) );
        if ( jsonLobby.has( CONSTANT_KEY_LIENSITEWEB ) )
            lobby.setUrl( jsonLobby.get( CONSTANT_KEY_LIENSITEWEB ).asText( ) );

        lobby.setVersionDate( new Date( ( new java.util.Date( ) ).getTime( ) ) );

        lobby.setJsonData( jsonLobby.toString( ) );

        return lobby;
    }

}
