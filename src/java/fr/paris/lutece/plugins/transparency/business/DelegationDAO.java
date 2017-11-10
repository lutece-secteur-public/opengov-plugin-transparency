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
import java.sql.Date;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for Delegation objects
 */
public final class DelegationDAO implements IDelegationDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_delegation, transparency_delegation.id_user, transparency_delegation.id_elected_official, date_creation , core_admin_user.last_name as admin_user_name, transparency_elected_official.last_name as elected_official_name FROM transparency_delegation LEFT JOIN core_admin_user on core_admin_user.id_user = transparency_delegation.id_user LEFT JOIN transparency_elected_official on transparency_elected_official.id_elected_official = transparency_delegation.id_elected_official ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO transparency_delegation ( id_user, id_elected_official, date_creation ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM transparency_delegation WHERE id_delegation = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE transparency_delegation SET id_delegation = ?, id_user = ?, id_elected_official = ? WHERE id_delegation = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_delegation FROM transparency_delegation";

    private static final String SQL_WHERE_CLAUSE_BY_ID = " WHERE id_delegation = ? ";
    private static final String SQL_DEFAULT_ORDER_BY = " ORDER BY admin_user_name ";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Delegation delegation, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin );
        try
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, delegation.getIdUser( ) );
            daoUtil.setInt( nIndex++, delegation.getIdElectedOfficial( ) );
            daoUtil.setDate( nIndex++, new Date( ( new java.util.Date( ) ).getTime( ) ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                delegation.setId( daoUtil.getGeneratedKeyInt( 1 ) );
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
    public Delegation load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_WHERE_CLAUSE_BY_ID, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );
        Delegation delegation = null;

        if ( daoUtil.next( ) )
        {
            delegation = new Delegation( );
            int nIndex = 1;

            delegation.setId( daoUtil.getInt( nIndex++ ) );
            delegation.setIdUser( daoUtil.getString( nIndex++ ) );
            delegation.setIdElectedOfficial( daoUtil.getInt( nIndex++ ) );
            delegation.setDateCreation( daoUtil.getDate( nIndex++ ) );
            delegation.setAdminUserName( daoUtil.getString( nIndex++ ) );
            delegation.setElectedOfficialName( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );
        return delegation;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Delegation delegation, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, delegation.getId( ) );
        daoUtil.setString( nIndex++, delegation.getIdUser( ) );
        daoUtil.setInt( nIndex++, delegation.getIdElectedOfficial( ) );
        daoUtil.setInt( nIndex, delegation.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Delegation> selectDelegationsList( Plugin plugin )
    {
        List<Delegation> delegationList = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_DEFAULT_ORDER_BY, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            Delegation delegation = new Delegation( );
            int nIndex = 1;

            delegation.setId( daoUtil.getInt( nIndex++ ) );
            delegation.setIdUser( daoUtil.getString( nIndex++ ) );
            delegation.setIdElectedOfficial( daoUtil.getInt( nIndex++ ) );
            delegation.setDateCreation( daoUtil.getDate( nIndex++ ) );
            delegation.setAdminUserName( daoUtil.getString( nIndex++ ) );
            delegation.setElectedOfficialName( daoUtil.getString( nIndex++ ) );

            delegationList.add( delegation );
        }

        daoUtil.free( );
        return delegationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdDelegationsList( Plugin plugin )
    {
        List<Integer> delegationList = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID + SQL_DEFAULT_ORDER_BY, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            delegationList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return delegationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectDelegationsReferenceList( Plugin plugin )
    {
        ReferenceList delegationList = new ReferenceList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_DEFAULT_ORDER_BY, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            delegationList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return delegationList;
    }
}
