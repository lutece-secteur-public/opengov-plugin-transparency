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
 * This class provides Data Access methods for Lobby objects
 */
public final class LobbyDAO implements ILobbyDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT transparency_lobby.id_lobby, name, national_id, national_id_type, url, json_data, version_date FROM transparency_lobby ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO transparency_lobby ( name, national_id, national_id_type, url, json_data, version_date ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM transparency_lobby WHERE id_lobby = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE transparency_lobby SET id_lobby = ?, name = ?, national_id = ?, national_id_type = ?, url = ?, json_data = ?, version_date = ? WHERE id_lobby = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_lobby FROM transparency_lobby";

    private static final String SQL_WHERE_NAME_LIKE = " WHERE name like ? " ;
    private static final String SQL_WHERECLAUSE_BY_APPOINTMENT = " LEFT JOIN transparency_lobby_appointment on transparency_lobby_appointment.id_lobby = transparency_lobby.id_lobby WHERE id_appointment = ? ";
    private static final String SQL_WHERECLAUSE_BY_ID = " WHERE id_lobby = ? ";
    private static final String SQL_WHERECLAUSE_BY_NATIONAL_ID = " WHERE national_id = ? ";
    
    private static final String SQL_ORDER_BY = " ORDER BY name " ;
            
    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Lobby lobby, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin );
        try
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , lobby.getName( ) );
            daoUtil.setString(    nIndex++ , lobby.getNationalId( ) );
            daoUtil.setString( nIndex++ , lobby.getNationalIdType( ) );
            daoUtil.setString( nIndex++ , lobby.getUrl( ) );
            daoUtil.setString( nIndex++ , lobby.getJsonData( ) );
            daoUtil.setDate(   nIndex++ , lobby.getVersionDate( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                lobby.setId( daoUtil.getGeneratedKeyInt( 1 ) );
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
    public Lobby load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_WHERECLAUSE_BY_ID , plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );
        Lobby lobby = null;

        if ( daoUtil.next( ) )
        {
            lobby = new Lobby();
            int nIndex = 1;
            
            lobby.setId( daoUtil.getInt( nIndex++ ) );
            lobby.setName( daoUtil.getString( nIndex++ ) );
            lobby.setNationalId( daoUtil.getString( nIndex++ ) );
            lobby.setNationalIdType( daoUtil.getString( nIndex++ ) );
            lobby.setUrl( daoUtil.getString( nIndex++ ) );
            lobby.setJsonData( daoUtil.getString( nIndex++ ) );
            lobby.setVersionDate( daoUtil.getDate( nIndex++ ) );
        }

        daoUtil.free( );
        return lobby;
    }

        /**
     * {@inheritDoc }
     */
    @Override
    public Lobby loadByNationalId( String strNationalId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_WHERECLAUSE_BY_NATIONAL_ID , plugin );
        daoUtil.setString( 1 , strNationalId );
        daoUtil.executeQuery( );
        Lobby lobby = null;

        if ( daoUtil.next( ) )
        {
            lobby = new Lobby();
            int nIndex = 1;
            
            lobby.setId( daoUtil.getInt( nIndex++ ) );
            lobby.setName( daoUtil.getString( nIndex++ ) );
            lobby.setNationalId( daoUtil.getString( nIndex++ ) );
            lobby.setNationalIdType( daoUtil.getString( nIndex++ ) );
            lobby.setUrl( daoUtil.getString( nIndex++ ) );
            lobby.setJsonData( daoUtil.getString( nIndex++ ) );
            lobby.setVersionDate( daoUtil.getDate( nIndex++ ) );
        }

        daoUtil.free( );
        return lobby;
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
    public void store( Lobby lobby, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , lobby.getId( ) );
        daoUtil.setString( nIndex++ , lobby.getName( ) );
        daoUtil.setString( nIndex++ , lobby.getNationalId( ) );
        daoUtil.setString( nIndex++ , lobby.getNationalIdType( ) );
        daoUtil.setString( nIndex++ , lobby.getUrl( ) );
        daoUtil.setString( nIndex++ , lobby.getJsonData( ) );
        daoUtil.setDate( nIndex++ , lobby.getVersionDate( ) );
        daoUtil.setInt( nIndex , lobby.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Lobby> selectLobbiesList( String strLikeText, Plugin plugin )
    {
        List<Lobby> lobbyList = new ArrayList<Lobby>(  );
        String strSQL = SQL_QUERY_SELECT;
        if ( strLikeText != null ) strSQL += SQL_WHERE_NAME_LIKE ;
        strSQL += SQL_ORDER_BY ;
        
        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        if ( strLikeText != null ) daoUtil.setString(1, strLikeText );
        
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Lobby lobby = new Lobby(  );
            int nIndex = 1;
            
            lobby.setId( daoUtil.getInt( nIndex++ ) );
            lobby.setName( daoUtil.getString( nIndex++ ) );
            lobby.setNationalId( daoUtil.getString( nIndex++ ) );
            lobby.setNationalIdType( daoUtil.getString( nIndex++ ) );
            lobby.setUrl( daoUtil.getString( nIndex++ ) );
            lobby.setJsonData( daoUtil.getString( nIndex++ ) );
            lobby.setVersionDate( daoUtil.getDate( nIndex++ ) );

            lobbyList.add( lobby );
        }

        daoUtil.free( );
        return lobbyList;
    }
   
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Lobby> selectLobbiesListByAppointment( int idAppointment,  Plugin plugin )
    {
        List<Lobby> lobbyList = new ArrayList<Lobby>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_WHERECLAUSE_BY_APPOINTMENT + SQL_ORDER_BY, plugin );
        daoUtil.setInt( 1 , idAppointment );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Lobby lobby = new Lobby(  );
            int nIndex = 1;
            
            lobby.setId( daoUtil.getInt( nIndex++ ) );
            lobby.setName( daoUtil.getString( nIndex++ ) );
            lobby.setNationalId( daoUtil.getString( nIndex++ ) );
            lobby.setNationalIdType( daoUtil.getString( nIndex++ ) );
            lobby.setUrl( daoUtil.getString( nIndex++ ) );
            lobby.setJsonData( daoUtil.getString( nIndex++ ) );
            lobby.setVersionDate( daoUtil.getDate( nIndex++ ) );

            lobbyList.add( lobby );
        }

        daoUtil.free( );
        return lobbyList;
    }


    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdLobbiesList( Plugin plugin )
    {
        List<Integer> lobbyList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            lobbyList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return lobbyList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectLobbiesReferenceList( Plugin plugin )
    {
        ReferenceList lobbyList = new ReferenceList();
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_ORDER_BY, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            lobbyList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return lobbyList;
    }
}
