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

package fr.paris.lutece.plugins.transparency.web;

import fr.paris.lutece.plugins.transparency.business.Appointment;
import fr.paris.lutece.plugins.transparency.business.AppointmentFilter;
import fr.paris.lutece.plugins.transparency.business.AppointmentHome;
import fr.paris.lutece.plugins.transparency.business.ElectedOfficialHome;
import fr.paris.lutece.plugins.transparency.business.LobbyHome;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.string.StringUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage Appointment xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "publicmeeting", pageTitleI18nKey = "transparency.xpage.appointment.pageTitle", pagePathI18nKey = "transparency.xpage.appointment.pagePathLabel" )
public class AppointmentPublicXPage extends MVCApplication
{
    // Templates
    private static final String TEMPLATE_MANAGE_APPOINTMENTS = "/skin/plugins/transparency/manage_public_appointments.html";
    private static final String TEMPLATE_DETAIL_APPOINTMENT = "/skin/plugins/transparency/detail_public_appointment.html";

    // Parameters
    private static final String PARAMETER_ID_APPOINTMENT = "id";
    private static final String PARAMETER_SEARCH_PERIOD = "search_period";
    private static final String PARAMETER_SEARCH_ELECTED_OFFICIAL = "search_elected_official";
    private static final String PARAMETER_SEARCH_LOBBY = "search_lobby";
    private static final String PARAMETER_SEARCH_TITLE = "search_title";
    private static final String PARAMETER_SORTED_ATTRIBUTE_NAME = "sorted_attribute_name";
    private static final String PARAMETER_START_DATE = "start_date";
    private static final String PARAMETER_ASC = "asc_sort";

    // Markers
    private static final String MARK_APPOINTMENT_LIST = "appointment_list";
    private static final String MARK_APPOINTMENT = "appointment";
    private static final String MARK_BASE_URL = "base_url";
    private static final String MARK_LOBBY_REFERENCE_START_URL = "lobbyReferenceStartUrl";
    private static final String MARK_PAGINATOR = "paginator" ;
    private static final String MARK_SEARCH_FILTER = "search_filter" ;

    // Properties
    private static final String PROPERTY_LOBBY_REFERENCE_START_URL_KEY = "lobby.json.detail.startUrl";

    // Views
    private static final String VIEW_MANAGE_APPOINTMENTS = "manageAppointments";
    private static final String VIEW_DETAIL_APPOINTMENT = "detailAppointment";

    // Session variable to store working values
    private Appointment _appointment;
    private List<Integer> _appointmentIdsList;
    private AppointmentFilter _filter = new AppointmentFilter( );

    private int _nItemsPerPage = 10;
    private String _strCurrentPageIndex = "1";
    
    // Constants
    private static int CONSTANT_DEFAULT_SEARCH_PERIOD = 92 ;
    


    /**
     * Build the Manage View
     *
     * @param request
     *            The HTTP request
     * @return The Xpage
     */
    @View( value = VIEW_MANAGE_APPOINTMENTS, defaultView = true )
    public XPage getManageAppointments( HttpServletRequest request )
    {
        _appointment = null;
        List<Appointment> appointmentList = null;
        Paginator<Integer> paginator ;
        
        // check type of request : paginating / sorting / new search
        if ( request.getParameter( Paginator.PARAMETER_PAGE_INDEX ) != null && _appointmentIdsList != null ) 
        {
            // paginate list
            _strCurrentPageIndex = request.getParameter( Paginator.PARAMETER_PAGE_INDEX );
            paginator = new Paginator<Integer>( _appointmentIdsList, _nItemsPerPage, getViewFullUrl( VIEW_MANAGE_APPOINTMENTS ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
            
            _filter.setListIds( paginator.getPageItems( ) );
            
            // get full appointments corresponding to the Ids
            appointmentList = AppointmentHome.getFullAppointmentsList( _filter );
        }
        else if ( request.getParameter( PARAMETER_SORTED_ATTRIBUTE_NAME ) != null && _appointmentIdsList != null )
        {
            // sort list
            if ( request.getParameter( PARAMETER_SORTED_ATTRIBUTE_NAME ).equals( PARAMETER_START_DATE ) )
            {
                if ( request.getParameter( PARAMETER_ASC ) != null && request.getParameter( PARAMETER_ASC ).equals( "true" ) )
                {
                    _filter.setOrderBy( PARAMETER_START_DATE + " ASC " );
                }
                else
                {
                    _filter.setOrderBy( PARAMETER_START_DATE + " DESC " );
                }
            }
            
            // reinitialize
            _strCurrentPageIndex = "1" ;
            _filter.setListIds( null ) ;
            
            // search all Ids whith the same filter
            _appointmentIdsList = AppointmentHome.getAppointmentIdsList( _filter );
            
            paginator = new Paginator<Integer>( _appointmentIdsList, _nItemsPerPage, getViewFullUrl( VIEW_MANAGE_APPOINTMENTS ) ,
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
            
            _filter.setListIds(  paginator.getPageItems( ) );
            
            // get full appointments corresponding to the Ids
            appointmentList = AppointmentHome.getFullAppointmentsList( _filter );
        }
        else
        {
            // new search 
            String strSearchPeriod = request.getParameter( PARAMETER_SEARCH_PERIOD );
            String strSearchElectedOfficial = request.getParameter( PARAMETER_SEARCH_ELECTED_OFFICIAL );
            String strSearchLobby = request.getParameter( PARAMETER_SEARCH_LOBBY );
            String strSearchTitle = request.getParameter( PARAMETER_SEARCH_TITLE );

            
            if ( strSearchPeriod != null ) _filter.setNumberOfDays( StringUtil.getIntValue( strSearchPeriod, CONSTANT_DEFAULT_SEARCH_PERIOD ) );
            _filter.setLobbyName( strSearchLobby );
            _filter.setElectedOfficialName( strSearchElectedOfficial );
            _filter.setTitle( strSearchTitle );
            
            // reinitialize
            _strCurrentPageIndex = "1" ;
            _filter.setListIds( null ) ;
            _filter.setOrderBy( null );
            
            // search all Ids 
            _appointmentIdsList = AppointmentHome.getAppointmentIdsList( _filter );
            
            paginator = new Paginator<Integer>( _appointmentIdsList, _nItemsPerPage, getViewFullUrl( VIEW_MANAGE_APPOINTMENTS ) ,
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
            
            _filter.setListIds( paginator.getPageItems( ) );
            
            // get full appointments corresponding to the Ids
            appointmentList = AppointmentHome.getFullAppointmentsList( _filter );
            
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_APPOINTMENT_LIST, appointmentList );
        model.put( MARK_PAGINATOR, paginator);
        model.put( MARK_SEARCH_FILTER, _filter);
        model.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOBBY_REFERENCE_START_URL, AppPropertiesService.getProperty( PROPERTY_LOBBY_REFERENCE_START_URL_KEY ) );

        return getXPage( TEMPLATE_MANAGE_APPOINTMENTS, request.getLocale( ), model );
    }

    /**
     * Returns the form to update info about a appointment
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_DETAIL_APPOINTMENT )
    public XPage getDetailAppointment( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_APPOINTMENT ) );

        if ( _appointment == null || ( _appointment.getId( ) != nId ) )
        {
            _appointment = AppointmentHome.findByPrimaryKey( nId );
        }

        _appointment.setElectedOfficialList( ElectedOfficialHome.getElectedOfficialsListByAppointment( _appointment.getId( ) ) );
        _appointment.setLobbyList( LobbyHome.getLobbiesListByAppointment( _appointment.getId( ) ) );

        Map<String, Object> model = getModel( );
        model.put( MARK_APPOINTMENT, _appointment );
        model.put( MARK_LOBBY_REFERENCE_START_URL, AppPropertiesService.getProperty( PROPERTY_LOBBY_REFERENCE_START_URL_KEY ) );

        return getXPage( TEMPLATE_DETAIL_APPOINTMENT, request.getLocale( ), model );
    }

}
