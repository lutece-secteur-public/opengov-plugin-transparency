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
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage Lobby xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "lobby", pageTitleI18nKey = "transparency.xpage.lobby.pageTitle", pagePathI18nKey = "transparency.xpage.lobby.pagePathLabel" )
public class LobbyXPage extends MVCApplication
{
    // Templates
    private static final String TEMPLATE_MANAGE_LOBBIES = "/skin/plugins/transparency/manage_lobbies.html";
    private static final String TEMPLATE_DETAIL_LOBBY = "/skin/plugins/transparency/detail_lobby.html";

    // Parameters
    private static final String PARAMETER_ID_LOBBY = "id";

    // Markers
    private static final String MARK_LOBBY_LIST = "lobby_list";
    private static final String MARK_LOBBY = "lobby";
    private static final String MARK_LOBBY_REFERENCE_START_URL = "lobbyReferenceStartUrl";

    // Views
    private static final String VIEW_MANAGE_LOBBIES = "manageLobbies";
    private static final String VIEW_DETAIL_LOBBY = "detailLobby";

    // Properties
    private static final String PROPERTY_LOBBY_REFERENCE_START_URL_KEY = "lobby.json.detail.startUrl";

    // Session variable to store working values
    private Lobby _lobby;

    /**
     * Build the Manage View
     *
     * @param request
     *            The HTTP request
     * @return The Xpage
     */
    @View( value = VIEW_MANAGE_LOBBIES, defaultView = true )
    public XPage getManageLobbies( HttpServletRequest request )
    {
        _lobby = null;
        Map<String, Object> model = getModel( );
        model.put( MARK_LOBBY_LIST, LobbyHome.getLobbiesList( ) );

        return getXPage( TEMPLATE_MANAGE_LOBBIES, request.getLocale( ), model );
    }

    /**
     * Returns the form to update info about a lobby
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_DETAIL_LOBBY )
    public XPage getDetailLobby( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_LOBBY ) );

        if ( _lobby == null || ( _lobby.getId( ) != nId ) )
        {
            _lobby = LobbyHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_LOBBY, _lobby );

        model.put( MARK_LOBBY_REFERENCE_START_URL, AppPropertiesService.getProperty( PROPERTY_LOBBY_REFERENCE_START_URL_KEY ) );

        return getXPage( TEMPLATE_DETAIL_LOBBY, request.getLocale( ), model );
    }

}
