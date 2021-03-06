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

/**
 * This class provides instances management methods (create, find, ...) for LobbyAppointment objects
 */
public final class LobbyAppointmentHome
{
    // Static variable pointed at the DAO instance
    private static ILobbyAppointmentDAO _dao = SpringContextService.getBean( "transparency.lobbyAppointmentDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "transparency" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private LobbyAppointmentHome( )
    {
    }

    /**
     * Create an instance of the lobbyAppointment class
     * 
     * @param lobbyAppointment
     *            The instance of the LobbyAppointment which contains the informations to store
     * @return The instance of lobbyAppointment which has been created with its primary key.
     */
    public static LobbyAppointment create( LobbyAppointment lobbyAppointment )
    {
        _dao.insert( lobbyAppointment, _plugin );

        return lobbyAppointment;
    }

    /**
     * Remove the lobby associations of the appointment
     * 
     * @param nIdAppointment
     */
    public static void removeByAppointmentId( int nIdAppointment )
    {
        _dao.deleteByAppointmentId( nIdAppointment, _plugin );
    }

}
