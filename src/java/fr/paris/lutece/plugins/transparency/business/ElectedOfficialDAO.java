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

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for ElectedOfficial objects
 */
public final class ElectedOfficialDAO implements IElectedOfficialDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT core_role.role, role_description FROM core_role ";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT core_role.role FROM core_role";

    private static final String SQL_WHERECLAUSE_BY_APPOINTMENT = " LEFT JOIN transparency_elected_official_appointment ON transparency_elected_official_appointment.role_key = core_role.role WHERE transparency_elected_official_appointment.id_appointment  = ? ";
    private static final String SQL_WHERECLAUSE_BY_DELEGATION = " LEFT JOIN mylutece_database_user_role ON mylutece_database_user_role.role_key = core_role.role LEFT JOIN mylutece_database_user ON mylutece_database_user_role.mylutece_database_user_id = mylutece_database_user.mylutece_database_user_id WHERE login = ? ";
    private static final String SQL_WHERECLAUSE_BY_ID = " WHERE role = ? ";

    private static final String SQL_ORDER_BY = " ORDER BY role_description ";

    

    /**
     * {@inheritDoc }
     */
    @Override
    public ElectedOfficial load( String strKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_WHERECLAUSE_BY_ID, plugin );
        daoUtil.setString( 1, strKey );
        daoUtil.executeQuery( );
        ElectedOfficial electedOfficial = null;

        if ( daoUtil.next( ) )
        {
            electedOfficial = new ElectedOfficial( );
            int nIndex = 1;

            electedOfficial.setId( daoUtil.getString( nIndex++ ) );
            electedOfficial.setLastName( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );
        return electedOfficial;
    }

    
    

    /**
     * {@inheritDoc }
     */
    @Override
    public List<ElectedOfficial> selectElectedOfficialsList( Plugin plugin )
    {
        List<ElectedOfficial> electedOfficialList = new ArrayList<ElectedOfficial>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_ORDER_BY, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            ElectedOfficial electedOfficial = new ElectedOfficial( );
            int nIndex = 1;

            electedOfficial.setId( daoUtil.getString(nIndex++ ) );
            electedOfficial.setLastName( daoUtil.getString( nIndex++ ) );
            
            electedOfficialList.add( electedOfficial );
        }

        daoUtil.free( );
        return electedOfficialList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<ElectedOfficial> selectElectedOfficialsListByAppointment( int idAppointment, Plugin plugin )
    {
        List<ElectedOfficial> electedOfficialList = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_WHERECLAUSE_BY_APPOINTMENT + SQL_ORDER_BY, plugin );
        daoUtil.setInt( 1, idAppointment );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            ElectedOfficial electedOfficial = new ElectedOfficial( );
            int nIndex = 1;

            electedOfficial.setId( daoUtil.getString(nIndex++ ) );
            electedOfficial.setLastName( daoUtil.getString( nIndex++ ) );
            
            electedOfficialList.add( electedOfficial );
        }

        daoUtil.free( );
        return electedOfficialList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<ElectedOfficial> selectElectedOfficialsListByDelegation( int idAdminUser, Plugin plugin )
    {
        List<ElectedOfficial> electedOfficialList = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_WHERECLAUSE_BY_DELEGATION + SQL_ORDER_BY, plugin );
        daoUtil.setInt( 1, idAdminUser );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            ElectedOfficial electedOfficial = new ElectedOfficial( );
            int nIndex = 1;

            electedOfficial.setId( daoUtil.getString(nIndex++ ) );
            electedOfficial.setLastName( daoUtil.getString( nIndex++ ) );
            
            electedOfficialList.add( electedOfficial );
        }

        daoUtil.free( );
        return electedOfficialList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> selectIdElectedOfficialsList( Plugin plugin )
    {
        List<String> electedOfficialList = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID + SQL_ORDER_BY, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            electedOfficialList.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free( );
        return electedOfficialList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectElectedOfficialsReferenceList( Plugin plugin )
    {
        ReferenceList electedOfficialList = new ReferenceList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_ORDER_BY, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            electedOfficialList.addItem( 
                    daoUtil.getString( 1 ), 
                    daoUtil.getString( 2 ) 
            );
        }

        daoUtil.free( );
        return electedOfficialList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectElectedOfficialsReferenceListByDelegation( String idUser, Plugin plugin )
    {
        ReferenceList electedOfficialList = new ReferenceList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_WHERECLAUSE_BY_DELEGATION + SQL_ORDER_BY, plugin );
        daoUtil.setString( 1, idUser );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            electedOfficialList.addItem( 
                    daoUtil.getString( 1 ) , 
                    daoUtil.getString( 2 ) 
            );
        }

        daoUtil.free( );
        return electedOfficialList;
    }

}
