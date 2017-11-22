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

package fr.paris.lutece.plugins.transparency.web;

import fr.paris.lutece.plugins.transparency.business.Lobby;
import fr.paris.lutece.plugins.transparency.business.LobbyHome;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.url.UrlItem;
import java.io.IOException;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.Iterator;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;

/**
 * This class provides the user interface to manage Lobby features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageLobbies.jsp", controllerPath = "jsp/admin/plugins/transparency/", right = "TRANSPARENCY_LOBBIES_MANAGEMENT" )
public class LobbyJspBean extends AbstractManageLobbiesJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_LOBBIES = "/admin/plugins/transparency/manage_lobbies.html";
    private static final String TEMPLATE_CREATE_LOBBY = "/admin/plugins/transparency/create_lobby.html";
    private static final String TEMPLATE_MODIFY_LOBBY = "/admin/plugins/transparency/modify_lobby.html";

    // Parameters
    private static final String PARAMETER_ID_LOBBY = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_LOBBIES = "transparency.manage_lobbies.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_LOBBY = "transparency.modify_lobby.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_LOBBY = "transparency.create_lobby.pageTitle";

    // Plugin properties
    private static final String PROPERTY_URL_LOBBY_LIST_REFERENCE = "lobby.json.list.url";

    // Markers
    private static final String MARK_LOBBY_LIST = "lobby_list";
    private static final String MARK_LOBBY = "lobby";

    private static final String JSP_MANAGE_LOBBIES = "jsp/admin/plugins/transparency/ManageLobbies.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_LOBBY = "transparency.message.confirmRemoveLobby";
    private static final String MSG_SYNCHRO_KEY = "transparency.message.synchro";
    private static final String MSG_ERROR_GET_JSON = "transparency.message.synchro.error";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "transparency.model.entity.lobby.attribute.";

    // Views
    private static final String VIEW_MANAGE_LOBBIES = "manageLobbies";
    private static final String VIEW_CREATE_LOBBY = "createLobby";
    private static final String VIEW_MODIFY_LOBBY = "modifyLobby";

    // Actions
    private static final String ACTION_CREATE_LOBBY = "createLobby";
    private static final String ACTION_MODIFY_LOBBY = "modifyLobby";
    private static final String ACTION_REMOVE_LOBBY = "removeLobby";
    private static final String ACTION_CONFIRM_REMOVE_LOBBY = "confirmRemoveLobby";
    private static final String ACTION_SYNCHRONIZE_LOBBIES = "synchronizeLobbies";

    // Infos
    private static final String INFO_LOBBY_CREATED = "transparency.info.lobby.created";
    private static final String INFO_LOBBY_UPDATED = "transparency.info.lobby.updated";
    private static final String INFO_LOBBY_REMOVED = "transparency.info.lobby.removed";

    // constants (for synchro)
    private static final String CONSTANT_KEY_PUBLICATIONS = "publications";
    private static final String CONSTANT_KEY_DENOMINATION = "denomination";
    private static final String CONSTANT_KEY_IDENTIFIANTNATIONAL = "identifiantNational";
    private static final String CONSTANT_KEY_TYPEIDENTIFIANTNATIONAL = "typeIdentifiantNational";
    private static final String CONSTANT_KEY_LIENSITEWEB = "lienSiteWeb";

    // Session variable to store working values
    private Lobby _lobby;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_LOBBIES, defaultView = true )
    public String getManageLobbies( HttpServletRequest request )
    {
        _lobby = null;
        List<Lobby> listLobbies = LobbyHome.getLobbiesList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_LOBBY_LIST, listLobbies, JSP_MANAGE_LOBBIES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_LOBBIES, TEMPLATE_MANAGE_LOBBIES, model );
    }

    /**
     * Returns the form to create a lobby
     *
     * @param request
     *            The Http request
     * @return the html code of the lobby form
     */
    @View( VIEW_CREATE_LOBBY )
    public String getCreateLobby( HttpServletRequest request )
    {
        _lobby = ( _lobby != null ) ? _lobby : new Lobby( );

        Map<String, Object> model = getModel( );
        model.put( MARK_LOBBY, _lobby );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_LOBBY, TEMPLATE_CREATE_LOBBY, model );
    }

    /**
     * Process the data capture form of a new lobby
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_LOBBY )
    public String doCreateLobby( HttpServletRequest request )
    {
        // add a date format converter
        DateConverter converter = new DateConverter( null );
        converter.setPattern( I18nService.getDateFormatShortPattern( I18nService.getDefaultLocale( ) ) );
        ConvertUtils.register( converter, Date.class );

        populate( _lobby, request );

        // Check constraints
        if ( !validateBean( _lobby, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_LOBBY );
        }

        LobbyHome.create( _lobby );
        addInfo( INFO_LOBBY_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_LOBBIES );
    }

    /**
     * Manages the removal form of a lobby whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_LOBBY )
    public String getConfirmRemoveLobby( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_LOBBY ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_LOBBY ) );
        url.addParameter( PARAMETER_ID_LOBBY, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_LOBBY, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a lobby
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage lobbies
     */
    @Action( ACTION_REMOVE_LOBBY )
    public String doRemoveLobby( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_LOBBY ) );
        LobbyHome.remove( nId );
        addInfo( INFO_LOBBY_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_LOBBIES );
    }

    /**
     * Returns the form to update info about a lobby
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_LOBBY )
    public String getModifyLobby( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_LOBBY ) );

        if ( _lobby == null || ( _lobby.getId( ) != nId ) )
        {
            _lobby = LobbyHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_LOBBY, _lobby );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_LOBBY, TEMPLATE_MODIFY_LOBBY, model );
    }

    /**
     * Process the change form of a lobby
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_LOBBY )
    public String doModifyLobby( HttpServletRequest request )
    {
        // add a date format converter
        DateConverter converter = new DateConverter( null );
        converter.setPattern( I18nService.getDateFormatShortPattern( I18nService.getDefaultLocale( ) ) );
        ConvertUtils.register( converter, Date.class );

        populate( _lobby, request );

        // Check constraints
        if ( !validateBean( _lobby, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_LOBBY, PARAMETER_ID_LOBBY, _lobby.getId( ) );
        }

        LobbyHome.update( _lobby );
        addInfo( INFO_LOBBY_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_LOBBIES );
    }

    /**
     * Refresh the lobby data base
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_SYNCHRONIZE_LOBBIES )
    public String synchronizeLobbies( HttpServletRequest request )
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
                String msg = I18nService.getLocalizedString( MSG_ERROR_GET_JSON, getLocale( ) );
                msg = MessageFormat.format( msg, strUri );

                addError( msg );
                return redirectView( request, VIEW_MANAGE_LOBBIES );
            }

            ObjectMapper mapper = new ObjectMapper( );
            JsonNode jsonNode = null;

            try
            {
                jsonNode = mapper.readTree( strJson );
            }
            catch( IOException e )
            {
                String msg = I18nService.getLocalizedString( MSG_ERROR_GET_JSON, getLocale( ) );
                msg = MessageFormat.format( msg, strUri );

                addError( msg );
                return redirectView( request, VIEW_MANAGE_LOBBIES );
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

            String msg = I18nService.getLocalizedString( MSG_SYNCHRO_KEY, getLocale( ) );
            msg = MessageFormat.format( msg, nbLobby, nbLobbyCreated, nbLobby - nbLobbyCreated );

            addInfo( msg );

        }
        catch( HttpAccessException e )
        {
            addError( e.getLocalizedMessage( ) );
        }

        return redirectView( request, VIEW_MANAGE_LOBBIES );
    }

    /**
     * Parse Json to populate a Lobby bean
     * 
     * @param jsonLobby
     * @return the lobby bean
     */
    private Lobby jsonToLobby( JsonNode jsonLobby )
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
