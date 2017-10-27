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

public class DelegationBusinessTest extends LuteceTestCase
{
    private final static int IDADMINUSER1 = 1;
    private final static int IDADMINUSER2 = 2;
    private final static int IDELECTEDOFFICIAL1 = 1;
    private final static int IDELECTEDOFFICIAL2 = 2;
	private final static Date DATECREATION1 = new Date( 1000000l );
    private final static Date DATECREATION2 = new Date( 2000000l );

    public void testBusiness(  )
    {
        // Initialize an object
        Delegation delegation = new Delegation();
        delegation.setIdAdminUser( IDADMINUSER1 );
        delegation.setIdElectedOfficial( IDELECTEDOFFICIAL1 );
        delegation.setDateCreation( DATECREATION1 );

        // Create test
        DelegationHome.create( delegation );
        Delegation delegationStored = DelegationHome.findByPrimaryKey( delegation.getId( ) );
        assertEquals( delegationStored.getIdAdminUser() , delegation.getIdAdminUser( ) );
        assertEquals( delegationStored.getIdElectedOfficial() , delegation.getIdElectedOfficial( ) );
        assertEquals( delegationStored.getDateCreation() , delegation.getDateCreation( ) );

        // Update test
        delegation.setIdAdminUser( IDADMINUSER2 );
        delegation.setIdElectedOfficial( IDELECTEDOFFICIAL2 );
        delegation.setDateCreation( DATECREATION2 );
        DelegationHome.update( delegation );
        delegationStored = DelegationHome.findByPrimaryKey( delegation.getId( ) );
        assertEquals( delegationStored.getIdAdminUser() , delegation.getIdAdminUser( ) );
        assertEquals( delegationStored.getIdElectedOfficial() , delegation.getIdElectedOfficial( ) );
        assertEquals( delegationStored.getDateCreation() , delegation.getDateCreation( ) );

        // List test
        DelegationHome.getDelegationsList();

        // Delete test
        DelegationHome.remove( delegation.getId( ) );
        delegationStored = DelegationHome.findByPrimaryKey( delegation.getId( ) );
        assertNull( delegationStored );
        
    }

}