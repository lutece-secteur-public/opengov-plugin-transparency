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
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * LobbyRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.VERSION_PATH + Constants.LOBBY_PATH )
public class LobbyRest
{
    private static final int VERSION_1 = 1;
    private final Logger _logger = Logger.getLogger( RestConstants.REST_LOGGER );
    
    /**
     * Get Lobby List
     * @param nVersion the API version
     * @return the Lobby List
     */
    @GET
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getLobbyList( @PathParam( Constants.VERSION ) Integer nVersion )
    {
        switch ( nVersion )
        {
            case VERSION_1:
                return getLobbyListV1( );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get Lobby List V1
     * @return the Lobby List for the version 1
     */
    private Response getLobbyListV1( )
    {
        List<Lobby> _listLobbys = LobbyHome.getLobbysList( );
        
        if( _listLobbys.isEmpty( ) )
        {
            return Response.status( Response.Status.NO_CONTENT )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) )
                .build( );
        }
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( _listLobbys ) ) )
                .build( );
    }
    
    /**
     * Create Lobby
     * @param nVersion the API version
     * @param name the name
     * @param national_id the national_id
     * @param national_id_type the national_id_type
     * @param url the url
     * @param json_data the json_data
     * @param version_date the version_date
     * @return the Lobby if created
     */
    @POST
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createLobby(
    @FormParam( Constants.LOBBY_ATTRIBUTE_NAME ) String name,
    @FormParam( Constants.LOBBY_ATTRIBUTE_NATIONAL_ID ) String national_id,
    @FormParam( Constants.LOBBY_ATTRIBUTE_NATIONAL_ID_TYPE ) String national_id_type,
    @FormParam( Constants.LOBBY_ATTRIBUTE_URL ) String url,
    @FormParam( Constants.LOBBY_ATTRIBUTE_JSON_DATA ) String json_data,
    @FormParam( Constants.LOBBY_ATTRIBUTE_VERSION_DATE ) String version_date,
    @PathParam( Constants.VERSION ) Integer nVersion )
    {
        switch ( nVersion )
        {
            case VERSION_1:
                return createLobbyV1( name, national_id, national_id_type, url, json_data, version_date );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Create Lobby V1
     * @param name the name
     * @param national_id the national_id
     * @param national_id_type the national_id_type
     * @param url the url
     * @param json_data the json_data
     * @param version_date the version_date
     * @return the Lobby if created for the version 1
     */
    private Response createLobbyV1( String name, String national_id, String national_id_type, String url, String json_data, String version_date )
    {
        if ( StringUtils.isEmpty( name ) || StringUtils.isEmpty( national_id ) || StringUtils.isEmpty( national_id_type ) || StringUtils.isEmpty( url ) || StringUtils.isEmpty( json_data ) || StringUtils.isEmpty( version_date ) )
        {
            _logger.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }
        
        Lobby _lobby = new Lobby( );
        _lobby.setName( name );
        _lobby.setNationalId( Integer.parseInt( national_id ) );
        _lobby.setNationalIdType( national_id_type );
        _lobby.setUrl( url );
        _lobby.setJsonData( json_data );
        _lobby.setVersionDate( version_date );
        LobbyHome.create( _lobby );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( _lobby ) ) )
                .build( );
    }
    
    /**
     * Modify Lobby
     * @param nVersion the API version
     * @param id the id
     * @param name the name
     * @param national_id the national_id
     * @param national_id_type the national_id_type
     * @param url the url
     * @param json_data the json_data
     * @param version_date the version_date
     * @return the Lobby if modified
     */
    @PUT
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response modifyLobby(
    @PathParam( Constants.ID ) Integer id,
    @FormParam( Constants.LOBBY_ATTRIBUTE_NAME ) String name,
    @FormParam( Constants.LOBBY_ATTRIBUTE_NATIONAL_ID ) String national_id,
    @FormParam( Constants.LOBBY_ATTRIBUTE_NATIONAL_ID_TYPE ) String national_id_type,
    @FormParam( Constants.LOBBY_ATTRIBUTE_URL ) String url,
    @FormParam( Constants.LOBBY_ATTRIBUTE_JSON_DATA ) String json_data,
    @FormParam( Constants.LOBBY_ATTRIBUTE_VERSION_DATE ) String version_date,
    @PathParam( Constants.VERSION ) Integer nVersion )
    {
        switch ( nVersion )
        {
            case VERSION_1:
                return modifyLobbyV1( id, name, national_id, national_id_type, url, json_data, version_date );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Modify Lobby V1
     * @param id the id
     * @param name the name
     * @param national_id the national_id
     * @param national_id_type the national_id_type
     * @param url the url
     * @param json_data the json_data
     * @param version_date the version_date
     * @return the Lobby if modified for the version 1
     */
    private Response modifyLobbyV1( Integer id, String name, String national_id, String national_id_type, String url, String json_data, String version_date )
    {
        if ( StringUtils.isEmpty( name ) || StringUtils.isEmpty( national_id ) || StringUtils.isEmpty( national_id_type ) || StringUtils.isEmpty( url ) || StringUtils.isEmpty( json_data ) || StringUtils.isEmpty( version_date ) )
        {
            _logger.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }
        
        Lobby _lobby = LobbyHome.findByPrimaryKey( id );
        if ( _lobby == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        _lobby.setName( name );
        _lobby.setNationalId( Integer.parseInt( national_id ) );
        _lobby.setNationalIdType( national_id_type );
        _lobby.setUrl( url );
        _lobby.setJsonData( json_data );
        _lobby.setVersionDate( version_date );
        LobbyHome.update( _lobby );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( _lobby ) ) )
                .build( );
    }
    
    /**
     * Delete Lobby
     * @param nVersion the API version
     * @param id the id
     * @return the Lobby List if deleted
     */
    @DELETE
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteLobby(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) Integer id )
    {
        switch ( nVersion )
        {
            case VERSION_1:
                return deleteLobbyV1( id );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Delete Lobby V1
     * @param id the id
     * @return the Lobby List if deleted for the version 1
     */
    private Response deleteLobbyV1( Integer id )
    {
        Lobby _lobby = LobbyHome.findByPrimaryKey( id );
        if ( _lobby == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        LobbyHome.remove( id );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) )
                .build( );
    }
    
    /**
     * Get Lobby
     * @param nVersion the API version
     * @param id the id
     * @return the Lobby
     */
    @GET
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getLobby(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) Integer id )
    {
        switch ( nVersion )
        {
            case VERSION_1:
                return getLobbyV1( id );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get Lobby V1
     * @param id the id
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
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( _lobby ) ) )
                .build( );
    }
}