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
 package fr.paris.lutece.plugins.transparency.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Lobby objects
 */
public final class LobbyHome
{
    // Static variable pointed at the DAO instance
    private static ILobbyDAO _dao = SpringContextService.getBean( "transparency.lobbyDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "transparency" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private LobbyHome(  )
    {
    }

    /**
     * Create an instance of the lobby class
     * @param lobby The instance of the Lobby which contains the informations to store
     * @return The  instance of lobby which has been created with its primary key.
     */
    public static Lobby create( Lobby lobby )
    {
        _dao.insert( lobby, _plugin );

        return lobby;
    }

    /**
     * Update of the lobby which is specified in parameter
     * @param lobby The instance of the Lobby which contains the data to store
     * @return The instance of the  lobby which has been updated
     */
    public static Lobby update( Lobby lobby )
    {
        _dao.store( lobby, _plugin );

        return lobby;
    }

    /**
     * Remove the lobby whose identifier is specified in parameter
     * @param nKey The lobby Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a lobby whose identifier is specified in parameter
     * @param nKey The lobby primary key
     * @return an instance of Lobby
     */
    public static Lobby findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Load the data of all the lobby objects and returns them as a list
     * @return the list which contains the data of all the lobby objects
     */
    public static List<Lobby> getLobbiesList( )
    {
        return _dao.selectLobbiesList( null, _plugin );
    }

    /**
     * Load the data of all the lobby objects with Name like %strLikeName% and returns them as a list
     * @return the list which contains the data of all the lobby objects
     */
    public static List<Lobby> getLobbiesListNamedLike( String strLikeName )
    {
        return _dao.selectLobbiesList( strLikeName, _plugin );
    }

    /**
     * Load the data of all the lobby objects and returns them as a list
     * @param idAppointment
     * @return the list which contains the data of all the lobby objects
     */
    public static List<Lobby> getLobbiesListByAppointment( int idAppointment )
    {
        return _dao.selectLobbiesListByAppointment( idAppointment, _plugin );
    }
    
    /**
     * Load the id of all the lobby objects and returns them as a list
     * @return the list which contains the id of all the lobby objects
     */
    public static List<Integer> getIdLobbieslist( )
    {
        return _dao.selectIdLobbiesList( _plugin );
    }
    
    /**
     * Load the data of all the lobby objects and returns them as a referenceList
     * @return the referenceList which contains the data of all the lobby objects
     */
    public static ReferenceList getLobbiesReferenceList( )
    {
        return _dao.selectLobbiesReferenceList(_plugin );
    }
}

