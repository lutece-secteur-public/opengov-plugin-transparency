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

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * This is the business class for the object Delegation
 */
public class Delegation implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;

    private String _strIdUser;
    private String _strAdminUserName;

    private int _nIdElectedOfficial;
    private String _strElectedOfficialName;

    private Date _dateDateCreation;

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Id user
     * 
     * @return The Iduser
     */
    public String getIdUser( )
    {
        return _strIdUser;
    }

    /**
     * Sets the Id user
     * 
     * @param strIdUser
     *            The Id user
     */
    public void setIdUser( String strIdUser )
    {
        _strIdUser = strIdUser;
    }

    /**
     * Returns the IdElectedofficial
     * 
     * @return The IdElectedofficial
     */
    public int getIdElectedOfficial( )
    {
        return _nIdElectedOfficial;
    }

    /**
     * Sets the IdElectedofficial
     * 
     * @param nIdElectedOfficial
     */
    public void setIdElectedOfficial( int nIdElectedOfficial )
    {
        _nIdElectedOfficial = nIdElectedOfficial;
    }

    /**
     * Returns the DateCreation
     * 
     * @return The DateCreation
     */
    public Date getDateCreation( )
    {
        return _dateDateCreation;
    }

    /**
     * Sets the DateCreation
     * 
     * @param dateDateCreation
     *            The DateCreation
     */
    public void setDateCreation( Date dateDateCreation )
    {
        _dateDateCreation = dateDateCreation;
    }

    /**
     * get AdminUser Name
     * 
     * @return the name
     */
    public String getAdminUserName( )
    {
        return _strAdminUserName;
    }

    /**
     * set Admin User Name
     * 
     * @param _strAdminUserName
     */
    public void setAdminUserName( String _strAdminUserName )
    {
        this._strAdminUserName = _strAdminUserName;
    }

    /**
     * get Elected Official Name
     * 
     * @return the name
     */
    public String getElectedOfficialName( )
    {
        return _strElectedOfficialName;
    }

    /**
     * set Elected Official Name
     * 
     * @param _strElectedOfficialName
     */
    public void setElectedOfficialName( String _strElectedOfficialName )
    {
        this._strElectedOfficialName = _strElectedOfficialName;
    }

}
