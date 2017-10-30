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
public class AppointmentFilter {
    
    private String _strLobbyName ;
    private String _strElectedOfficialName ; 
    private int _nNumberOfDays ; // appointments of the last N days
    private String _strOrderBy ; // sort records
    private boolean _bAsc ; // sort records order (ASC = true)

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
    public void setNumberOfDays(int numberOfDays ) 
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
    public void setOrderBy(String orderBy ) 
    {
        this._strOrderBy = orderBy;
    }

    /**
     * get the Asc order
     * 
     * @return 
     */
    public boolean isAsc() {
        return _bAsc;
    }

    /**
     * set the Asc order
     * 
     * @param _bAsc 
     */
    public void setAsc(boolean _bAsc) 
    {
        this._bAsc = _bAsc;
    }

    /**
     * get Lobby name
     * @return  lobby name
     */
    public String getLobbyName() 
    {
        return _strLobbyName;
    }

    /**
     * set Lobby name
     * @param _strLobbyName 
     */
    public void setLobbyName(String _strLobbyName) 
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
     * @param _strElectedOfficialName
     */
    public void setElectedOfficialName(String _strElectedOfficialName) 
    {
        this._strElectedOfficialName = _strElectedOfficialName;
    }
    
    
    
}
