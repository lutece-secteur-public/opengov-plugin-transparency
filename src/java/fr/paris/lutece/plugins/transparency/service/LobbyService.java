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
public final class LobbyService {
    
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
                return msg ;
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
            return e.getLocalizedMessage( ) ;
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
