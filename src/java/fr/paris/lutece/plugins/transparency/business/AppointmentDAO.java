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
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for Appointment objects
 */
public final class AppointmentDAO implements IAppointmentDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT transparency_appointment.id_appointment, title, description, start_date, end_date, type_id, type_label, url, contacts FROM transparency_appointment ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO transparency_appointment ( title, description, start_date, end_date, type_id, type_label, url, contacts ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM transparency_appointment WHERE id_appointment = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE transparency_appointment SET id_appointment = ?, title = ?, description = ?, start_date = ?, end_date = ?, type_id = ?, type_label = ?, url = ?, contacts = ? WHERE id_appointment = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_appointment FROM transparency_appointment";

    private static final String SQL_WHERECLAUSE_BY_DELEGATION = " LEFT JOIN transparency_elected_official_appointment ON transparency_elected_official_appointment.id_appointment = transparency_appointment.id_appointment LEFT JOIN transparency_delegation on transparency_delegation.id_elected_official = transparency_elected_official_appointment.id_elected_official WHERE transparency_delegation.id_user = ?  ";
    private static final String SQL_WHERECLAUSE_BY_ID = " WHERE id_appointment = ?" ;
    private static final String SQL_DEFAULT_ORDER_BY = " ORDER BY start_date DESC ";
    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Appointment appointment, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin );
        try
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , appointment.getTitle( ) );
            daoUtil.setString( nIndex++ , appointment.getDescription( ) );
            daoUtil.setDate( nIndex++ , appointment.getStartDate( ) );
            daoUtil.setDate( nIndex++ , appointment.getEndDate( ) );
            daoUtil.setInt( nIndex++ , appointment.getTypeId( ) );
            daoUtil.setString( nIndex++ , appointment.getTypeLabel( ) );
            daoUtil.setString( nIndex++ , appointment.getUrl( ) );
            daoUtil.setString( nIndex++ , appointment.getContacts( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                appointment.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        finally
        {
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Appointment load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_WHERECLAUSE_BY_ID, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );
        Appointment appointment = null;

        if ( daoUtil.next( ) )
        {
            appointment = new Appointment();
            int nIndex = 1;
            
            appointment.setId( daoUtil.getInt( nIndex++ ) );
            appointment.setTitle( daoUtil.getString( nIndex++ ) );
            appointment.setDescription( daoUtil.getString( nIndex++ ) );
            appointment.setStartDate( daoUtil.getDate( nIndex++ ) );
            appointment.setEndDate( daoUtil.getDate( nIndex++ ) );
            appointment.setTypeId( daoUtil.getInt( nIndex++ ) );
            appointment.setTypeLabel( daoUtil.getString( nIndex++ ) );
            appointment.setUrl( daoUtil.getString( nIndex++ ) );
            appointment.setContacts( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );
        return appointment;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Appointment appointment, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , appointment.getId( ) );
        daoUtil.setString( nIndex++ , appointment.getTitle( ) );
        daoUtil.setString( nIndex++ , appointment.getDescription( ) );
        daoUtil.setDate( nIndex++ , appointment.getStartDate( ) );
        daoUtil.setDate( nIndex++ , appointment.getEndDate( ) );
        daoUtil.setInt( nIndex++ , appointment.getTypeId( ) );
        daoUtil.setString( nIndex++ , appointment.getTypeLabel( ) );
        daoUtil.setString( nIndex++ , appointment.getUrl( ) );
        daoUtil.setString( nIndex++ , appointment.getContacts( ) );
        daoUtil.setInt( nIndex , appointment.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Appointment> selectAppointmentsList( Plugin plugin )
    {
        List<Appointment> appointmentList = new ArrayList<>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_DEFAULT_ORDER_BY, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Appointment appointment = new Appointment(  );
            int nIndex = 1;
            
            appointment.setId( daoUtil.getInt( nIndex++ ) );
            appointment.setTitle( daoUtil.getString( nIndex++ ) );
            appointment.setDescription( daoUtil.getString( nIndex++ ) );
            appointment.setStartDate( daoUtil.getDate( nIndex++ ) );
            appointment.setEndDate( daoUtil.getDate( nIndex++ ) );
            appointment.setTypeId( daoUtil.getInt( nIndex++ ) );
            appointment.setTypeLabel( daoUtil.getString( nIndex++ ) );
            appointment.setUrl( daoUtil.getString( nIndex++ ) );
            appointment.setContacts( daoUtil.getString( nIndex++ ) );

            appointmentList.add( appointment );
        }

        daoUtil.free( );
        return appointmentList;
    }
    
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Appointment> selectAppointmentsListByDelegation( int idUser, Plugin plugin )
    {
        List<Appointment> appointmentList = new ArrayList<Appointment>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_WHERECLAUSE_BY_DELEGATION + SQL_DEFAULT_ORDER_BY, plugin );
        daoUtil.setInt( 1 , idUser );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Appointment appointment = new Appointment(  );
            int nIndex = 1;
            
            appointment.setId( daoUtil.getInt( nIndex++ ) );
            appointment.setTitle( daoUtil.getString( nIndex++ ) );
            appointment.setDescription( daoUtil.getString( nIndex++ ) );
            appointment.setStartDate( daoUtil.getDate( nIndex++ ) );
            appointment.setEndDate( daoUtil.getDate( nIndex++ ) );
            appointment.setTypeId( daoUtil.getInt( nIndex++ ) );
            appointment.setTypeLabel( daoUtil.getString( nIndex++ ) );
            appointment.setUrl( daoUtil.getString( nIndex++ ) );
            appointment.setContacts( daoUtil.getString( nIndex++ ) );

            appointmentList.add( appointment );
        }

        daoUtil.free( );
        return appointmentList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdAppointmentsList( Plugin plugin )
    {
        List<Integer> appointmentList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID + SQL_DEFAULT_ORDER_BY, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            appointmentList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return appointmentList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectAppointmentsReferenceList( Plugin plugin )
    {
        ReferenceList appointmentList = new ReferenceList();
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_DEFAULT_ORDER_BY, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            appointmentList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return appointmentList;
    }
}
