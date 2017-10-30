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
 * This class provides instances management methods (create, find, ...) for Appointment objects
 */
public final class AppointmentHome
{
    // Static variable pointed at the DAO instance
    private static IAppointmentDAO _dao = SpringContextService.getBean( "transparency.appointmentDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "transparency" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private AppointmentHome(  )
    {
    }

    /**
     * Create an instance of the appointment class
     * @param appointment The instance of the Appointment which contains the informations to store
     * @return The  instance of appointment which has been created with its primary key.
     */
    public static Appointment create( Appointment appointment )
    {
        _dao.insert( appointment, _plugin );

        return appointment;
    }

    /**
     * Update of the appointment which is specified in parameter
     * @param appointment The instance of the Appointment which contains the data to store
     * @return The instance of the  appointment which has been updated
     */
    public static Appointment update( Appointment appointment )
    {
        _dao.store( appointment, _plugin );

        return appointment;
    }

    /**
     * Remove the appointment whose identifier is specified in parameter
     * @param nKey The appointment Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a appointment whose identifier is specified in parameter
     * @param nKey The appointment primary key
     * @return an instance of Appointment
     */
    public static Appointment findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }
    
    /**
     * Returns an instance of a appointment whose identifier is specified in parameter
     * @param nId
     * @return an instance of Appointment
     */
    public static Appointment getFullAppointmentById( int nId )
    {
        Appointment appointment = _dao.load( nId , _plugin ) ;
                
        if ( appointment != null ) 
        {
            // get lobbies and elected officials
            appointment.setLobbyList( LobbyHome.getLobbiesListByAppointment( appointment.getId( ) ) ) ;
            appointment.setElectedOfficialList( ElectedOfficialHome.getElectedOfficialsListByAppointment( appointment.getId( ) ) ) ;
            
        }
            
        return appointment ;       
    }

    /**
     * Load the data of all the appointment objects and returns them as a list
     * @return the list which contains the data of all the appointment objects
     */
    public static List<Appointment> getAppointmentsList( )
    {
        return _dao.selectAppointmentsList( _plugin );
    }
    
    /**
     * Load the data of all the appointment objects and returns them as a list
     * @return the list which contains the data of all the appointment objects
     */
    public static List<Appointment> getFullAppointmentsList( )
    {
        return getFullAppointmentsList( null ) ;
    }

    /**
     * Load the data of all the filtred appointment objects and returns them as a list
     * @param filter
     * @return the list which contains the data of all the appointment objects
     */
    public static List<Appointment> getFullAppointmentsList( AppointmentFilter filter )
    {
        List<Appointment> list = _dao.selectAppointmentsList( filter, _plugin );
        for (Appointment appointment : list) 
        {
            appointment.setElectedOfficialList( ElectedOfficialHome.getElectedOfficialsListByAppointment( appointment.getId( ) ) ) ;
            appointment.setLobbyList( LobbyHome.getLobbiesListByAppointment( appointment.getId( ) ) ) ;
            
        }
        return list;
    }

    
    /**
     * Load the data of all the appointment objects associated to the delegation of the user 
     * and returns them as a list
     * @return the list which contains the data of all the appointment objects
     */
    public static List<Appointment> getFullAppointmentsListByDelegation( int idUser )
    {
        List<Appointment> list = _dao.selectAppointmentsListByDelegation( idUser, _plugin );
        for (Appointment appointment : list) 
        {
            appointment.setElectedOfficialList( ElectedOfficialHome.getElectedOfficialsListByAppointment( appointment.getId( ) ) ) ;
            appointment.setLobbyList( LobbyHome.getLobbiesListByAppointment( appointment.getId( ) ) ) ;
            
        }
        return list;
    }
    
    /**
     * Load the id of all the appointment objects and returns them as a list
     * @return the list which contains the id of all the appointment objects
     */
    public static List<Integer> getIdAppointmentsList( )
    {
        return _dao.selectIdAppointmentsList( _plugin );
    }
    
    /**
     * Load the data of all the appointment objects and returns them as a referenceList
     * @return the referenceList which contains the data of all the appointment objects
     */
    public static ReferenceList getAppointmentsReferenceList( )
    {
        return _dao.selectAppointmentsReferenceList(_plugin );
    }
}

