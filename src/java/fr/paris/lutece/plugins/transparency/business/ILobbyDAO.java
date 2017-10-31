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
import fr.paris.lutece.util.ReferenceList;
import java.util.List;

/**
 * ILobbyDAO Interface
 */
public interface ILobbyDAO
{
    /**
     * Insert a new record in the table.
     * 
     * @param lobby
     *            instance of the Lobby object to insert
     * @param plugin
     *            the Plugin
     */
    void insert( Lobby lobby, Plugin plugin );

    /**
     * Update the record in the table
     * 
     * @param lobby
     *            the reference of the Lobby
     * @param plugin
     *            the Plugin
     */
    void store( Lobby lobby, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param nKey
     *            The identifier of the Lobby to delete
     * @param plugin
     *            the Plugin
     */
    void delete( int nKey, Plugin plugin );

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * 
     * @param nKey
     *            The identifier of the lobby
     * @param plugin
     *            the Plugin
     * @return The instance of the lobby
     */
    Lobby load( int nKey, Plugin plugin );

    /**
     * find the data by national Id from the table
     * 
     * @param strNationalId
     * @param plugin
     *            the Plugin
     * @return The instance of the lobby
     */
    Lobby loadByNationalId( String strNationalId, Plugin plugin );

    /**
     * Load the data of all the lobby objects with a name like %strLikeText% (if not null) and returns them as a list
     * 
     * @param strLikeText
     * @param plugin
     *            the Plugin
     * @return The list which contains the data of all the lobby objects
     */
    List<Lobby> selectLobbiesList( String strLikeText, Plugin plugin );

    /**
     * Load the data of all the lobby objects and returns them as a list
     * 
     * @param idAppointment
     * @param plugin
     *            the Plugin
     * @return The list which contains the data of all the lobby objects
     */
    List<Lobby> selectLobbiesListByAppointment( int idAppointment, Plugin plugin );

    /**
     * Load the id of all the lobby objects and returns them as a list
     * 
     * @param plugin
     *            the Plugin
     * @return The list which contains the id of all the lobby objects
     */
    List<Integer> selectIdLobbiesList( Plugin plugin );

    /**
     * Load the data of all the lobby objects and returns them as a referenceList
     * 
     * @param plugin
     *            the Plugin
     * @return The referenceList which contains the data of all the lobby objects
     */
    ReferenceList selectLobbiesReferenceList( Plugin plugin );
}
