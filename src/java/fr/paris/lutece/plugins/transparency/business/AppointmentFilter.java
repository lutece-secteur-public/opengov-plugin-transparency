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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.transparency.business;

/**
 *
 * @author leridons
 */
public class AppointmentFilter
{

    private String _strLobbyName;
    private String _strElectedOfficialName;
    private String _strUserId;
    private String _strTitle;
    private int _nNumberOfDays; // appointments of the last N days
    private String _strOrderBy; // sort records
    private int _nIdAppointment = -1;
    private boolean _bAsc; // sort records order (ASC = true)

    /**
     * get NumberOfDays
     * 
     * @return
     */
    public int getNumberOfDays( )
    {
        return _nNumberOfDays;
    }

    /**
     * set NumberOfDays
     * 
     * @param numberOfDays
     */
    public void setNumberOfDays( int numberOfDays )
    {
        this._nNumberOfDays = numberOfDays;
    }

    /**
     * get OrderBy
     * 
     * @return the order by statement
     */
    public String getOrderBy( )
    {
        return _strOrderBy;
    }

    /**
     * set OrderBy statement
     * 
     * @param orderBy
     */
    public void setOrderBy( String orderBy )
    {
        this._strOrderBy = orderBy;
    }

    /**
     * get the Asc order
     * 
     * @return
     */
    public boolean isAsc( )
    {
        return _bAsc;
    }

    /**
     * set the Asc order
     * 
     * @param _bAsc
     */
    public void setAsc( boolean _bAsc )
    {
        this._bAsc = _bAsc;
    }

    /**
     * get Lobby name
     * 
     * @return lobby name
     */
    public String getLobbyName( )
    {
        return _strLobbyName;
    }

    /**
     * set Lobby name
     * 
     * @param _strLobbyName
     */
    public void setLobbyName( String _strLobbyName )
    {
        this._strLobbyName = _strLobbyName;
    }

    /**
     * get Elected Official name
     * 
     * @return
     */
    public String getElectedOfficialName( )
    {
        return _strElectedOfficialName;
    }

    /**
     * set elected official name
     * 
     * @param _strElectedOfficialName
     */
    public void setElectedOfficialName( String _strElectedOfficialName )
    {
        this._strElectedOfficialName = _strElectedOfficialName;
    }

    /**
     * get user id
     * 
     * @return the user id
     */
    public String getUserId( )
    {
        return _strUserId;
    }

    /**
     * set user id
     * 
     * @param _strUserId
     */
    public void setUserId( String _strUserId )
    {
        this._strUserId = _strUserId;
    }

    /**
     * get Id Appointment
     * 
     * @return the appointment id
     */
    public int getIdAppointment( )
    {
        return _nIdAppointment;
    }

    /**
     * set the appointment id
     * 
     * @param _nIdAppointment
     */
    public void setIdAppointment( int _nIdAppointment )
    {
        this._nIdAppointment = _nIdAppointment;
    }

    /**
     * get title
     * 
     * @return title
     */
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * set title
     * 
     * @param _strTitle
     */
    public void setTitle( String _strTitle )
    {
        this._strTitle = _strTitle;
    }

}
