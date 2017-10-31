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

import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

/**
 * SwaggerRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.VERSION_PATH )
public class SwaggerRest
{
    private final Logger _logger = Logger.getLogger( RestConstants.REST_LOGGER );
    private final static String BASE_INFOS_SCHEMES = "schemes";
    private final static String BASE_INFOS_HOST = "host";
    private final static String BASE_INFOS_BASE_PATH = "basePath";

    /**
     * Get Swagger.json
     * 
     * @param request
     * @param strVersion
     * @return the swagger.json
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     */
    @GET
    @Path( Constants.SWAGGER_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getSwagger( @Context HttpServletRequest request, @PathParam( Constants.VERSION ) String strVersion ) throws MalformedURLException,
            IOException
    {
        File fileJson = new File( getJsonFilePath( strVersion ) );
        if ( fileJson.exists( ) )
        {
            Map<String, String> mapBaseInfos = getBaseInfos( AppPathService.getBaseUrl( request ), strVersion );

            ObjectMapper mapper = new ObjectMapper( );
            ObjectNode objectNode = mapper.readValue( fileJson, ObjectNode.class );

            if ( objectNode.path( BASE_INFOS_HOST ).isMissingNode( ) )
            {
                objectNode.put( BASE_INFOS_HOST, mapBaseInfos.get( BASE_INFOS_HOST ) );
            }
            if ( objectNode.path( BASE_INFOS_SCHEMES ).isMissingNode( ) )
            {
                objectNode.putArray( BASE_INFOS_SCHEMES ).add( mapBaseInfos.get( BASE_INFOS_SCHEMES ) );
            }
            if ( objectNode.path( BASE_INFOS_BASE_PATH ).isMissingNode( ) )
            {
                objectNode.put( BASE_INFOS_BASE_PATH, mapBaseInfos.get( BASE_INFOS_BASE_PATH ) );
            }
            String strSwaggerJson = mapper.writerWithDefaultPrettyPrinter( ).writeValueAsString( objectNode );
            return Response.status( Response.Status.OK ).entity( strSwaggerJson ).build( );
        }
        _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) ).build( );
    }

    /**
     * Get the swagger.json file path
     * 
     * @param strVersion
     * @return
     */
    private String getJsonFilePath( String strVersion )
    {
        return AppPathService.getWebAppPath( ) + Constants.SWAGGER_DIRECTORY_PATH + Constants.API_PATH + Constants.SWAGGER_PATH
                + Constants.SWAGGER_VERSION_PATH + strVersion + Constants.SWAGGER_JSON;
    }

    /**
     * Get the base informations (host, scheme, baseUrl)
     * 
     * @param strBaseUrl
     * @param strVersion
     * @return
     * @throws MalformedURLException
     */
    private Map<String, String> getBaseInfos( String strBaseUrl, String strVersion ) throws MalformedURLException
    {
        Map<String, String> map = new HashMap<>( );
        URL url = new URL( strBaseUrl );

        String strScheme = url.getProtocol( );
        String strHost = url.getHost( );
        String strBasePath = url.getPath( );
        int nPort = url.getPort( );

        if ( nPort != -1 )
        {
            strHost += ":" + nPort;
        }

        strBasePath = strBasePath + Constants.SWAGGER_REST_PATH + Constants.API_PATH + Constants.SWAGGER_VERSION_PATH + strVersion;

        map.put( BASE_INFOS_SCHEMES, strScheme );
        map.put( BASE_INFOS_HOST, strHost );
        map.put( BASE_INFOS_BASE_PATH, strBasePath );

        return map;
    }
}
