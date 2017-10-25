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

import fr.paris.lutece.test.LuteceTestCase;

import java.sql.Date;

public class LobbyBusinessTest extends LuteceTestCase
{
    private final static String NAME1 = "Name1";
    private final static String NAME2 = "Name2";
    private final static int NATIONALID1 = 1;
    private final static int NATIONALID2 = 2;
    private final static String NATIONALIDTYPE1 = "NationalIdType1";
    private final static String NATIONALIDTYPE2 = "NationalIdType2";
    private final static String URL1 = "Url1";
    private final static String URL2 = "Url2";
    private final static String JSONDATA1 = "JsonData1";
    private final static String JSONDATA2 = "JsonData2";
	private final static Date VERSIONDATE1 = new Date( 1000000l );
    private final static Date VERSIONDATE2 = new Date( 2000000l );

    public void testBusiness(  )
    {
        // Initialize an object
        Lobby lobby = new Lobby();
        lobby.setName( NAME1 );
        lobby.setNationalId( NATIONALID1 );
        lobby.setNationalIdType( NATIONALIDTYPE1 );
        lobby.setUrl( URL1 );
        lobby.setJsonData( JSONDATA1 );
        lobby.setVersionDate( VERSIONDATE1 );

        // Create test
        LobbyHome.create( lobby );
        Lobby lobbyStored = LobbyHome.findByPrimaryKey( lobby.getId( ) );
        assertEquals( lobbyStored.getName() , lobby.getName( ) );
        assertEquals( lobbyStored.getNationalId() , lobby.getNationalId( ) );
        assertEquals( lobbyStored.getNationalIdType() , lobby.getNationalIdType( ) );
        assertEquals( lobbyStored.getUrl() , lobby.getUrl( ) );
        assertEquals( lobbyStored.getJsonData() , lobby.getJsonData( ) );
        assertEquals( lobbyStored.getVersionDate() , lobby.getVersionDate( ) );

        // Update test
        lobby.setName( NAME2 );
        lobby.setNationalId( NATIONALID2 );
        lobby.setNationalIdType( NATIONALIDTYPE2 );
        lobby.setUrl( URL2 );
        lobby.setJsonData( JSONDATA2 );
        lobby.setVersionDate( VERSIONDATE2 );
        LobbyHome.update( lobby );
        lobbyStored = LobbyHome.findByPrimaryKey( lobby.getId( ) );
        assertEquals( lobbyStored.getName() , lobby.getName( ) );
        assertEquals( lobbyStored.getNationalId() , lobby.getNationalId( ) );
        assertEquals( lobbyStored.getNationalIdType() , lobby.getNationalIdType( ) );
        assertEquals( lobbyStored.getUrl() , lobby.getUrl( ) );
        assertEquals( lobbyStored.getJsonData() , lobby.getJsonData( ) );
        assertEquals( lobbyStored.getVersionDate() , lobby.getVersionDate( ) );

        // List test
        LobbyHome.getLobbysList();

        // Delete test
        LobbyHome.remove( lobby.getId( ) );
        lobbyStored = LobbyHome.findByPrimaryKey( lobby.getId( ) );
        assertNull( lobbyStored );
        
    }

}