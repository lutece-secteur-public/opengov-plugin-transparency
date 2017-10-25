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
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;

import java.util.Map;
import javax.servlet.http.HttpServletRequest; 

/**
 * This class provides the user interface to manage Lobby xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "lobby" , pageTitleI18nKey = "transparency.xpage.lobby.pageTitle" , pagePathI18nKey = "transparency.xpage.lobby.pagePathLabel" )
public class LobbyXPage extends MVCApplication
{
    // Templates
    private static final String TEMPLATE_MANAGE_LOBBYS="/skin/plugins/transparency/manage_lobbys.html";
    private static final String TEMPLATE_CREATE_LOBBY="/skin/plugins/transparency/create_lobby.html";
    private static final String TEMPLATE_MODIFY_LOBBY="/skin/plugins/transparency/modify_lobby.html";
    
    // JSP
    private static final String JSP_PAGE_PORTAL = "jsp/site/Portal.jsp";
    
    // Parameters
    private static final String PARAMETER_ID_LOBBY="id";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_PAGE = "page";
    
    // Markers
    private static final String MARK_LOBBY_LIST = "lobby_list";
    private static final String MARK_LOBBY = "lobby";
    
    // Message
    private static final String MESSAGE_CONFIRM_REMOVE_LOBBY = "transparency.message.confirmRemoveLobby";
    
    // Views
    private static final String VIEW_MANAGE_LOBBYS = "manageLobbys";
    private static final String VIEW_CREATE_LOBBY = "createLobby";
    private static final String VIEW_MODIFY_LOBBY = "modifyLobby";

    // Actions
    private static final String ACTION_CREATE_LOBBY = "createLobby";
    private static final String ACTION_MODIFY_LOBBY= "modifyLobby";
    private static final String ACTION_REMOVE_LOBBY = "removeLobby";
    private static final String ACTION_CONFIRM_REMOVE_LOBBY = "confirmRemoveLobby";

    // Infos
    private static final String INFO_LOBBY_CREATED = "transparency.info.lobby.created";
    private static final String INFO_LOBBY_UPDATED = "transparency.info.lobby.updated";
    private static final String INFO_LOBBY_REMOVED = "transparency.info.lobby.removed";
    
    // Session variable to store working values
    private Lobby _lobby;
    
    /**
     * Build the Manage View
     *
     * @param request The HTTP request
     * @return The Xpage
     */
    @View( value = VIEW_MANAGE_LOBBYS, defaultView = true )
    public XPage getManageLobbys( HttpServletRequest request )
    {
        _lobby = null;
        Map<String, Object> model = getModel(  );
        model.put( MARK_LOBBY_LIST, LobbyHome.getLobbysList(  ) );

        return getXPage( TEMPLATE_MANAGE_LOBBYS, request.getLocale(  ), model );
    }

    /**
     * Returns the form to create a lobby
     *
     * @param request The Http request
     * @return the html code of the lobby form
     */
    @View( VIEW_CREATE_LOBBY )
    public XPage getCreateLobby( HttpServletRequest request )
    {
        _lobby = ( _lobby != null ) ? _lobby : new Lobby(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_LOBBY, _lobby );
           
        return getXPage( TEMPLATE_CREATE_LOBBY, request.getLocale(  ), model );
    }

    /**
     * Process the data capture form of a new lobby
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_LOBBY )
    public XPage doCreateLobby( HttpServletRequest request )
    {
        populate( _lobby, request );

        // Check constraints
        if ( !validateBean( _lobby ) )
        {
            return redirectView( request, VIEW_CREATE_LOBBY );
        }

        LobbyHome.create( _lobby );
        addInfo( INFO_LOBBY_CREATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_LOBBYS );
    }

    /**
     * Manages the removal form of a lobby whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException
     */
    @Action( ACTION_CONFIRM_REMOVE_LOBBY )
    public XPage getConfirmRemoveLobby( HttpServletRequest request ) throws SiteMessageException
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_LOBBY ) );
        UrlItem url = new UrlItem( JSP_PAGE_PORTAL );
        url.addParameter( PARAM_PAGE, MARK_LOBBY );
        url.addParameter( PARAM_ACTION, ACTION_REMOVE_LOBBY );
        url.addParameter( PARAMETER_ID_LOBBY, nId );
        
        SiteMessageService.setMessage(request, MESSAGE_CONFIRM_REMOVE_LOBBY, SiteMessage.TYPE_CONFIRMATION, url.getUrl(  ));
        return null;
    }

    /**
     * Handles the removal form of a lobby
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage lobbys
     */
    @Action( ACTION_REMOVE_LOBBY )
    public XPage doRemoveLobby( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_LOBBY ) );
        LobbyHome.remove( nId );
        addInfo( INFO_LOBBY_REMOVED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_LOBBYS );
    }

    /**
     * Returns the form to update info about a lobby
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_LOBBY )
    public XPage getModifyLobby( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_LOBBY ) );

        if ( _lobby == null  || ( _lobby.getId( ) != nId ))
        {
            _lobby = LobbyHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_LOBBY, _lobby );
        
        return getXPage( TEMPLATE_MODIFY_LOBBY, request.getLocale(  ), model );
    }

    /**
     * Process the change form of a lobby
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_LOBBY )
    public XPage doModifyLobby( HttpServletRequest request )
    {
        populate( _lobby, request );

        // Check constraints
        if ( !validateBean( _lobby ) )
        {
            return redirect( request, VIEW_MODIFY_LOBBY, PARAMETER_ID_LOBBY, _lobby.getId( ) );
        }

        LobbyHome.update( _lobby );
        addInfo( INFO_LOBBY_UPDATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_LOBBYS );
    }
}
