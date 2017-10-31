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
 * This class provides instances management methods (create, find, ...) for Delegation objects
 */
public final class DelegationHome
{
    // Static variable pointed at the DAO instance
    private static IDelegationDAO _dao = SpringContextService.getBean( "transparency.delegationDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "transparency" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DelegationHome( )
    {
    }

    /**
     * Create an instance of the delegation class
     * 
     * @param delegation
     *            The instance of the Delegation which contains the informations to store
     * @return The instance of delegation which has been created with its primary key.
     */
    public static Delegation create( Delegation delegation )
    {
        _dao.insert( delegation, _plugin );

        return delegation;
    }

    /**
     * Update of the delegation which is specified in parameter
     * 
     * @param delegation
     *            The instance of the Delegation which contains the data to store
     * @return The instance of the delegation which has been updated
     */
    public static Delegation update( Delegation delegation )
    {
        _dao.store( delegation, _plugin );

        return delegation;
    }

    /**
     * Remove the delegation whose identifier is specified in parameter
     * 
     * @param nKey
     *            The delegation Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a delegation whose identifier is specified in parameter
     * 
     * @param nKey
     *            The delegation primary key
     * @return an instance of Delegation
     */
    public static Delegation findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the delegation objects and returns them as a list
     * 
     * @return the list which contains the data of all the delegation objects
     */
    public static List<Delegation> getDelegationsList( )
    {
        return _dao.selectDelegationsList( _plugin );
    }

    /**
     * Load the id of all the delegation objects and returns them as a list
     * 
     * @return the list which contains the id of all the delegation objects
     */
    public static List<Integer> getIdDelegationsList( )
    {
        return _dao.selectIdDelegationsList( _plugin );
    }

    /**
     * Load the data of all the delegation objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the delegation objects
     */
    public static ReferenceList getDelegationsReferenceList( )
    {
        return _dao.selectDelegationsReferenceList( _plugin );
    }
}
