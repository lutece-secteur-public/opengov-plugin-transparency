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

import fr.paris.lutece.plugins.transparency.business.ElectedOfficial;
import fr.paris.lutece.plugins.transparency.business.ElectedOfficialHome;
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
 * ElectedOfficialRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.VERSION_PATH + Constants.ELECTEDOFFICIAL_PATH )
public class ElectedOfficialRest
{
    private static final int VERSION_1 = 1;
    private final Logger _logger = Logger.getLogger( RestConstants.REST_LOGGER );
    
    /**
     * Get ElectedOfficial List
     * @param nVersion the API version
     * @return the ElectedOfficial List
     */
    @GET
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getElectedOfficialList( @PathParam( Constants.VERSION ) Integer nVersion )
    {
        switch ( nVersion )
        {
            case VERSION_1:
                return getElectedOfficialListV1( );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get ElectedOfficial List V1
     * @return the ElectedOfficial List for the version 1
     */
    private Response getElectedOfficialListV1( )
    {
        List<ElectedOfficial> _listElectedOfficials = ElectedOfficialHome.getElectedOfficialsList( );
        
        if( _listElectedOfficials.isEmpty( ) )
        {
            return Response.status( Response.Status.NO_CONTENT )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) )
                .build( );
        }
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( _listElectedOfficials ) ) )
                .build( );
    }
    
    /**
     * Create ElectedOfficial
     * @param nVersion the API version
     * @param first_name the first_name
     * @param last_name the last_name
     * @param title the title
     * @return the ElectedOfficial if created
     */
    @POST
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createElectedOfficial(
    @FormParam( Constants.ELECTEDOFFICIAL_ATTRIBUTE_FIRST_NAME ) String first_name,
    @FormParam( Constants.ELECTEDOFFICIAL_ATTRIBUTE_LAST_NAME ) String last_name,
    @FormParam( Constants.ELECTEDOFFICIAL_ATTRIBUTE_TITLE ) String title,
    @PathParam( Constants.VERSION ) Integer nVersion )
    {
        switch ( nVersion )
        {
            case VERSION_1:
                return createElectedOfficialV1( first_name, last_name, title );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Create ElectedOfficial V1
     * @param first_name the first_name
     * @param last_name the last_name
     * @param title the title
     * @return the ElectedOfficial if created for the version 1
     */
    private Response createElectedOfficialV1( String first_name, String last_name, String title )
    {
        if ( StringUtils.isEmpty( first_name ) || StringUtils.isEmpty( last_name ) || StringUtils.isEmpty( title ) )
        {
            _logger.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }
        
        ElectedOfficial _electedofficial = new ElectedOfficial( );
        _electedofficial.setFirstName( first_name );
        _electedofficial.setLastName( last_name );
        _electedofficial.setTitle( title );
        ElectedOfficialHome.create( _electedofficial );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( _electedofficial ) ) )
                .build( );
    }
    
    /**
     * Modify ElectedOfficial
     * @param nVersion the API version
     * @param id the id
     * @param first_name the first_name
     * @param last_name the last_name
     * @param title the title
     * @return the ElectedOfficial if modified
     */
    @PUT
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response modifyElectedOfficial(
    @PathParam( Constants.ID ) Integer id,
    @FormParam( Constants.ELECTEDOFFICIAL_ATTRIBUTE_FIRST_NAME ) String first_name,
    @FormParam( Constants.ELECTEDOFFICIAL_ATTRIBUTE_LAST_NAME ) String last_name,
    @FormParam( Constants.ELECTEDOFFICIAL_ATTRIBUTE_TITLE ) String title,
    @PathParam( Constants.VERSION ) Integer nVersion )
    {
        switch ( nVersion )
        {
            case VERSION_1:
                return modifyElectedOfficialV1( id, first_name, last_name, title );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Modify ElectedOfficial V1
     * @param id the id
     * @param first_name the first_name
     * @param last_name the last_name
     * @param title the title
     * @return the ElectedOfficial if modified for the version 1
     */
    private Response modifyElectedOfficialV1( Integer id, String first_name, String last_name, String title )
    {
        if ( StringUtils.isEmpty( first_name ) || StringUtils.isEmpty( last_name ) || StringUtils.isEmpty( title ) )
        {
            _logger.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }
        
        ElectedOfficial _electedofficial = ElectedOfficialHome.findByPrimaryKey( id );
        if ( _electedofficial == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        _electedofficial.setFirstName( first_name );
        _electedofficial.setLastName( last_name );
        _electedofficial.setTitle( title );
        ElectedOfficialHome.update( _electedofficial );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( _electedofficial ) ) )
                .build( );
    }
    
    /**
     * Delete ElectedOfficial
     * @param nVersion the API version
     * @param id the id
     * @return the ElectedOfficial List if deleted
     */
    @DELETE
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteElectedOfficial(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) Integer id )
    {
        switch ( nVersion )
        {
            case VERSION_1:
                return deleteElectedOfficialV1( id );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Delete ElectedOfficial V1
     * @param id the id
     * @return the ElectedOfficial List if deleted for the version 1
     */
    private Response deleteElectedOfficialV1( Integer id )
    {
        ElectedOfficial _electedofficial = ElectedOfficialHome.findByPrimaryKey( id );
        if ( _electedofficial == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        ElectedOfficialHome.remove( id );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) )
                .build( );
    }
    
    /**
     * Get ElectedOfficial
     * @param nVersion the API version
     * @param id the id
     * @return the ElectedOfficial
     */
    @GET
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getElectedOfficial(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) Integer id )
    {
        switch ( nVersion )
        {
            case VERSION_1:
                return getElectedOfficialV1( id );
            default:
                break;
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get ElectedOfficial V1
     * @param id the id
     * @return the ElectedOfficial for the version 1
     */
    private Response getElectedOfficialV1( Integer id )
    {
        ElectedOfficial _electedofficial = ElectedOfficialHome.findByPrimaryKey( id );
        if ( _electedofficial == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( _electedofficial ) ) )
                .build( );
    }
}