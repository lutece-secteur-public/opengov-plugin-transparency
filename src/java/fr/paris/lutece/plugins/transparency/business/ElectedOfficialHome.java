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
 * This class provides instances management methods (create, find, ...) for ElectedOfficial objects
 */
public final class ElectedOfficialHome
{
    // Static variable pointed at the DAO instance
    private static IElectedOfficialDAO _dao = SpringContextService.getBean( "transparency.electedOfficialDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "transparency" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ElectedOfficialHome( )
    {
    }

    /**
     * Returns an instance of a electedOfficial whose identifier is specified in parameter
     * 
     * @param strKey
     *            The electedOfficial primary key
     * @return an instance of ElectedOfficial
     */
    public static ElectedOfficial findByPrimaryKey( String strKey )
    {
        return _dao.load( strKey, _plugin );
    }

    /**
     * Load the data of all the electedOfficial objects and returns them as a list
     * 
     * @return the list which contains the data of all the electedOfficial objects
     */
    public static List<ElectedOfficial> getElectedOfficialsList( )
    {
        return _dao.selectElectedOfficialsList( _plugin );
    }

    /**
     * Load the data of all the electedOfficial objects and returns them as a list
     * 
     * @param idAdminUser
     * @return the list which contains the data of all the electedOfficial objects
     */
    public static List<ElectedOfficial> getElectedOfficialsListByDelegation( int idAdminUser )
    {
        return _dao.selectElectedOfficialsListByDelegation( idAdminUser, _plugin );
    }

    /**
     * Load the data of all the electedOfficial objects and returns them as a list
     * 
     * @return the list which contains the data of all the electedOfficial objects
     */
    public static List<ElectedOfficial> getElectedOfficialsListByAppointment( int idAppointment )
    {
        return _dao.selectElectedOfficialsListByAppointment( idAppointment, _plugin );
    }

    /**
     * Load the id of all the electedOfficial objects and returns them as a list
     * 
     * @return the list which contains the id of all the electedOfficial objects
     */
    public static List<String> getIdElectedOfficialsList( )
    {
        return _dao.selectIdElectedOfficialsList( _plugin );
    }

    /**
     * Load the data of all the electedOfficial objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the electedOfficial objects
     */
    public static ReferenceList getElectedOfficialsReferenceList( )
    {
        return _dao.selectElectedOfficialsReferenceList( _plugin );
    }

    /**
     * Load the data of all the electedOfficial objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the electedOfficial objects
     */
    public static ReferenceList getElectedOfficialsReferenceListByDelegation( String idUser )
    {
        return _dao.selectElectedOfficialsReferenceListByDelegation( idUser, _plugin );
    }

}
