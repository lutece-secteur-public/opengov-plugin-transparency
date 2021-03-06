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

import fr.paris.lutece.plugins.transparency.business.Lobby;
import fr.paris.lutece.plugins.transparency.business.LobbyHome;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * LobbyRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.VERSION_PATH + Constants.LOBBY_PATH )
public class LobbyRest
{
    private static final int VERSION_1 = 1;
    private final Logger _logger = Logger.getLogger( RestConstants.REST_LOGGER );

    /**
     * Get Lobby List (filtred by names like TEXT if present)
     * 
     * @param nVersion
     *            the API version
     * @param strLikeText
     * @return the Lobby List
     */
    @GET
    @Path( Constants.SEARCH_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getLobbyListLike( @PathParam( Constants.VERSION ) Integer nVersion, @PathParam( Constants.TEXT ) String strLikeText )
    {
        switch( nVersion )
        {
            case VERSION_1:
                return getLobbyListV1( strLikeText );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) ).build( );
    }

    /**
     * Get Lobby List
     * 
     * @param nVersion
     *            the API version
     * @return the Lobby List
     */
    @GET
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getLobbyList( @PathParam( Constants.VERSION ) Integer nVersion )
    {
        switch( nVersion )
        {
            case VERSION_1:
                return getLobbyListV1( "" );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) ).build( );
    }

    /**
     * Get Lobby List V1
     * 
     * @return the Lobby List for the version 1
     */
    private Response getLobbyListV1( String strLikeText )
    {
        List<Lobby> _listLobbies = LobbyHome.getLobbiesListNamedLike( "%" + strLikeText + "%" );

        if ( _listLobbies.isEmpty( ) )
        {
            JSONObject jsonNoProposal = new JSONObject( );

            jsonNoProposal.put( Constants.JSON_AUTOCOMPLETE_ID_KEY, -1 );
            jsonNoProposal.put( Constants.JSON_AUTOCOMPLETE_VALUE_KEY,
                    I18nService.getLocalizedString( Constants.MESSAGE_NO_PROPOSAL_I18N_KEY, I18nService.getDefaultLocale( ) ) );

            return Response.status( Response.Status.NO_CONTENT ).entity( jsonNoProposal.toString( ) ).build( );
        }

        JSONArray listJsonLobbies = new JSONArray( );
        for ( Lobby lobby : _listLobbies )
        {
            JSONObject jsonLobby = new JSONObject( );

            jsonLobby.put( Constants.JSON_AUTOCOMPLETE_ID_KEY, lobby.getId( ) );
            jsonLobby.put( Constants.JSON_AUTOCOMPLETE_VALUE_KEY, lobby.getName( ) );
            jsonLobby.put( Constants.JSON_AUTOCOMPLETE_LABEL_KEY, lobby.getName( ) );

            listJsonLobbies.put( jsonLobby );
        }
        return Response.status( Response.Status.OK ).entity( listJsonLobbies.toString( ) ).build( );
    }

    /**
     * Get Lobby
     * 
     * @param nVersion
     *            the API version
     * @param id
     *            the id
     * @return the Lobby
     */
    @GET
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getLobby( @PathParam( Constants.VERSION ) Integer nVersion, @PathParam( Constants.ID ) Integer id )
    {
        switch( nVersion )
        {
            case VERSION_1:
                return getLobbyV1( id );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) ).build( );
    }

    /**
     * Get Lobby V1
     * 
     * @param id
     *            the id
     * @return the Lobby for the version 1
     */
    private Response getLobbyV1( Integer id )
    {
        Lobby _lobby = LobbyHome.findByPrimaryKey( id );
        if ( _lobby == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }

        return Response.status( Response.Status.OK ).entity( JsonUtil.buildJsonResponse( new JsonResponse( _lobby ) ) ).build( );
    }
}
