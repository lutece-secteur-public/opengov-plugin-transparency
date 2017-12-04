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
import fr.paris.lutece.plugins.transparency.business.ElectedOfficial;
import fr.paris.lutece.plugins.transparency.business.ElectedOfficialAppointment;
import fr.paris.lutece.plugins.transparency.business.ElectedOfficialAppointmentHome;
import fr.paris.lutece.plugins.transparency.business.ElectedOfficialHome;
import fr.paris.lutece.plugins.transparency.business.Lobby;
import fr.paris.lutece.plugins.transparency.business.LobbyAppointment;
import fr.paris.lutece.plugins.transparency.business.LobbyAppointmentHome;
import fr.paris.lutece.plugins.transparency.business.LobbyHome;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.util.ReferenceList;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.plexus.util.StringUtils;

/**
 * This class provides the user interface to manage Appointment xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "meeting", pageTitleI18nKey = "transparency.xpage.appointment.pageTitle", pagePathI18nKey = "transparency.xpage.appointment.pagePathLabel" )
public class AppointmentXPage extends MVCApplication
{
    // Templates
    private static final String TEMPLATE_MANAGE_APPOINTMENTS = "/skin/plugins/transparency/manage_appointments.html";
    private static final String TEMPLATE_DETAIL_APPOINTMENT = "/skin/plugins/transparency/detail_appointment.html";
    private static final String TEMPLATE_CREATE_APPOINTMENT = "/skin/plugins/transparency/create_appointment.html";
    private static final String TEMPLATE_MODIFY_APPOINTMENT = "/skin/plugins/transparency/modify_appointment.html";

    // JSP
    private static final String JSP_PAGE_PORTAL = "jsp/site/Portal.jsp";

    // Parameters
    private static final String PARAMETER_ID_APPOINTMENT = "id";
    private static final String PARAMETER_SEARCH_PERIOD = "search_period";
    private static final String PARAMETER_SEARCH_ELECTED_OFFICIAL = "search_elected_official";
    private static final String PARAMETER_SEARCH_LOBBY = "search_lobby";
    private static final String PARAMETER_SEARCH_TITLE = "search_title";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_PAGE = "page";
    private static final String PARAMETER_ID_ELECTED_OFFICIAL = "id_elected_official";
    private static final String PARAMETER_ID_LOBBY = "lobby_id";
    private static final String PARAMETER_SELECT_LOBBY = "lobby_select";
    private static final String PARAMETER_SORTED_ATTRIBUTE_NAME = "sorted_attribute_name";
    private static final String PARAMETER_START_DATE = "start_date";
    private static final String PARAMETER_ASC = "asc_sort";

    // Markers
    private static final String MARK_APPOINTMENT_LIST = "appointment_list";
    private static final String MARK_APPOINTMENT = "appointment";
    private static final String MARK_BASE_URL = "base_url";
    private static final String MARK_IS_AUTHENTICATED = "is_authenticated";
    private static final String MARK_ELECTEDOFFICIALS_LIST = "electedofficials_list";

    // Views
    private static final String VIEW_MANAGE_APPOINTMENTS = "manageAppointments";
    private static final String VIEW_DETAIL_APPOINTMENT = "detailAppointment";
    private static final String VIEW_CREATE_APPOINTMENT = "createAppointment";
    private static final String VIEW_MODIFY_APPOINTMENT = "modifyAppointment";

    // Actions
    private static final String ACTION_CREATE_APPOINTMENT = "createAppointment";
    private static final String ACTION_MODIFY_APPOINTMENT = "modifyAppointment";
    private static final String ACTION_REMOVE_APPOINTMENT = "removeAppointment";
    private static final String ACTION_CONFIRM_REMOVE_APPOINTMENT = "confirmRemoveAppointment";

    // Infos
    private static final String INFO_APPOINTMENT_CREATED = "transparency.info.appointment.created";
    private static final String INFO_APPOINTMENT_UPDATED = "transparency.info.appointment.updated";
    private static final String INFO_APPOINTMENT_REMOVED = "transparency.info.appointment.removed";
    private static final String INFO_ACCESS_DENIED = "transparency.info.appointment.accessdenied";

    // Message
    private static final String MESSAGE_CONFIRM_REMOVE_APPOINTMENT = "transparency.message.confirmRemoveAppointment";

    // Session variable to store working values
    private Appointment _appointment;
    private List<Appointment> _appointmentList;
    private AppointmentFilter _filter = new AppointmentFilter( );

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
        boolean isAuthenticated = false;

        if ( request.getParameter( PARAMETER_SORTED_ATTRIBUTE_NAME ) != null && _appointmentList != null )
        {
            // sort list
            if ( request.getParameter( PARAMETER_SORTED_ATTRIBUTE_NAME ).equals( PARAMETER_START_DATE ) )
            {
                if ( request.getParameter( PARAMETER_ASC ) != null && request.getParameter( PARAMETER_ASC ).equals( "true" ) )
                {
                    _appointmentList.sort( ( a1, a2 ) -> a1.getStartDate( ).compareTo( a2.getStartDate( ) ) );
                }
                else
                {
                    _appointmentList.sort( ( a1, a2 ) -> a2.getStartDate( ).compareTo( a1.getStartDate( ) ) );
                }
            }
        }
        else
        {
            // new search
            String strSearchPeriod = request.getParameter( PARAMETER_SEARCH_PERIOD );
            String strSearchElectedOfficial = request.getParameter( PARAMETER_SEARCH_ELECTED_OFFICIAL );
            String strSearchLobby = request.getParameter( PARAMETER_SEARCH_LOBBY );
            String strSearchTitle = request.getParameter( PARAMETER_SEARCH_TITLE );

            // check authentification or public mode
            String idUser = null;
            try
            {
                idUser = checkMyLuteceAuthentication( request );
                if ( idUser != null )
                    isAuthenticated = true; // if idUser is null, it should be because authentication is not enable
            }
            catch( UserNotSignedException e )
            {
                // catch the exception : the appointments will be presented in read only mode
                isAuthenticated = false;
            }

            _filter.setNumberOfDays( StringUtil.getIntValue( strSearchPeriod, -1 ) );
            _filter.setLobbyName( strSearchLobby );
            _filter.setElectedOfficialName( strSearchElectedOfficial );
            _filter.setUserId( idUser );
            _filter.setTitle( strSearchTitle );

            // search
            _appointmentList = AppointmentHome.getFullAppointmentsList( _filter );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_APPOINTMENT_LIST, _appointmentList );
        model.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_IS_AUTHENTICATED, isAuthenticated );

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

        return getXPage( TEMPLATE_DETAIL_APPOINTMENT, request.getLocale( ), model );
    }

    /**
     * Returns the form to create a appointment
     *
     * @param request
     *            The Http request
     * @return the html code of the appointment form
     * @throws fr.paris.lutece.portal.service.security.UserNotSignedException
     */
    @View( VIEW_CREATE_APPOINTMENT )
    public XPage getCreateAppointment( HttpServletRequest request ) throws UserNotSignedException
    {
        _appointment = ( _appointment != null ) ? _appointment : new Appointment( );

        // check authentification
        String idUser = checkMyLuteceAuthentication( request );

        ReferenceList electedOfficialsList = ElectedOfficialHome.getElectedOfficialsReferenceListByDelegation( idUser );

        Map<String, Object> model = getModel( );
        model.put( MARK_APPOINTMENT, _appointment );
        model.put( MARK_ELECTEDOFFICIALS_LIST, electedOfficialsList );
        model.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );

        return getXPage( TEMPLATE_CREATE_APPOINTMENT, request.getLocale( ), model );
    }

    /**
     * Process the data capture form of a new appointment
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     * @throws fr.paris.lutece.portal.service.security.UserNotSignedException
     */
    @Action( ACTION_CREATE_APPOINTMENT )
    public XPage doCreateAppointment( HttpServletRequest request ) throws UserNotSignedException
    {

        // check authentification
        String idUser = checkMyLuteceAuthentication( request );

        populate( _appointment, request, request.getLocale( ) );

        // Check constraints
        if ( !validateBean( _appointment ) )
        {
            return redirectView( request, VIEW_CREATE_APPOINTMENT );
        }

        AppointmentHome.create( _appointment );

        // add elected Official to the appointment
        String strIdElectedOfficial = request.getParameter( PARAMETER_ID_ELECTED_OFFICIAL );

        ElectedOfficial electedOfficial = ElectedOfficialHome.findByPrimaryKey( strIdElectedOfficial );
        if ( electedOfficial != null )
        {
            ElectedOfficialAppointmentHome.create( new ElectedOfficialAppointment( strIdElectedOfficial, _appointment.getId( ) ) );
            _appointment.getElectedOfficialList( ).add( electedOfficial );
        }

        // add Lobby to the appointment
        String strIdLobby = request.getParameter( PARAMETER_ID_LOBBY );
        String strSelectLobby = request.getParameter( PARAMETER_SELECT_LOBBY );

        int idLobby = StringUtil.getIntValue( strIdLobby, -1 );

        Lobby lobby = LobbyHome.findByPrimaryKey( idLobby );
        if ( idLobby > 0 && lobby != null )
        {
            LobbyAppointmentHome.create( new LobbyAppointment( lobby.getId( ), _appointment.getId( ) ) );
            _appointment.getLobbyList( ).add( lobby );
        }
        else
            if ( !StringUtils.isBlank( strSelectLobby ) )
            {
                Lobby newLobby = new Lobby( );
                newLobby.setName( strSelectLobby );
                newLobby.setVersionDate( new Date( ( new java.util.Date( ) ).getTime( ) ) );
                newLobby = LobbyHome.create( newLobby );

                LobbyAppointmentHome.create( new LobbyAppointment( newLobby.getId( ), _appointment.getId( ) ) );
                _appointment.getLobbyList( ).add( newLobby );

            }
        addInfo( INFO_APPOINTMENT_CREATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_APPOINTMENTS );
    }

    /**
     * Manages the removal form of a appointment whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException
     */
    @Action( ACTION_CONFIRM_REMOVE_APPOINTMENT )
    public XPage getConfirmRemoveAppointment( HttpServletRequest request ) throws SiteMessageException
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_APPOINTMENT ) );
        
        UrlItem url = new UrlItem( getActionFullUrl(ACTION_REMOVE_APPOINTMENT) );
        url.addParameter( PARAMETER_ID_APPOINTMENT, nId );
        
        SiteMessageService.setMessage( request, MESSAGE_CONFIRM_REMOVE_APPOINTMENT, SiteMessage.TYPE_CONFIRMATION, url.getUrl( ) );
        return null;
    }

    /**
     * Handles the removal form of a appointment
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage appointments
     */
    @Action( ACTION_REMOVE_APPOINTMENT )
    public XPage doRemoveAppointment( HttpServletRequest request ) throws UserNotSignedException
    {
        // check authentification
        String idUser = checkMyLuteceAuthentication( request );

        int nIdAppointment = Integer.parseInt( request.getParameter( PARAMETER_ID_APPOINTMENT ) );

        // check delegation
        AppointmentFilter filter = new AppointmentFilter( );
        filter.setIdAppointment( nIdAppointment );
        filter.setUserId( idUser );

        List<Appointment> listAppointment = AppointmentHome.getFullAppointmentsList( filter );

        if ( listAppointment == null || listAppointment.isEmpty( ) )
        {
            // => no delegation for this appointment
            addInfo( INFO_ACCESS_DENIED, getLocale( request ) );
            return redirectView( request, VIEW_MANAGE_APPOINTMENTS );
        }

        // access granted
        AppointmentHome.remove( nIdAppointment );
        addInfo( INFO_APPOINTMENT_REMOVED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_APPOINTMENTS );
    }

    /**
     * Returns the form to update info about a appointment
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     * @throws fr.paris.lutece.portal.service.security.UserNotSignedException
     */
    @View( VIEW_MODIFY_APPOINTMENT )
    public XPage getModifyAppointment( HttpServletRequest request ) throws UserNotSignedException
    {
        int nIdAppointment = Integer.parseInt( request.getParameter( PARAMETER_ID_APPOINTMENT ) );

        // check authentification
        String idUser = checkMyLuteceAuthentication( request );

        // check delegation
        AppointmentFilter filter = new AppointmentFilter( );
        filter.setIdAppointment( nIdAppointment );
        filter.setUserId( idUser );

        List<Appointment> listAppointment = AppointmentHome.getFullAppointmentsList( filter );

        if ( listAppointment == null || listAppointment.isEmpty( ) )
        {
            // => no delegation for this appointment
            addInfo( INFO_ACCESS_DENIED, getLocale( request ) );
            return redirectView( request, VIEW_MANAGE_APPOINTMENTS );
        }

        // access granted
        _appointment = listAppointment.get( 0 );

        ReferenceList electedOfficialsList = ElectedOfficialHome.getElectedOfficialsReferenceListByDelegation( idUser );

        Map<String, Object> model = getModel( );
        model.put( MARK_APPOINTMENT, _appointment );
        model.put( MARK_ELECTEDOFFICIALS_LIST, electedOfficialsList );
        model.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );

        return getXPage( TEMPLATE_MODIFY_APPOINTMENT, request.getLocale( ), model );
    }

    /**
     * Process the change form of a appointment
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     * @throws fr.paris.lutece.portal.service.security.UserNotSignedException
     */
    @Action( ACTION_MODIFY_APPOINTMENT )
    public XPage doModifyAppointment( HttpServletRequest request ) throws UserNotSignedException
    {
        // check authentification
        String idUser = checkMyLuteceAuthentication( request );

        populate( _appointment, request, request.getLocale( ) );

        // Check constraints
        if ( !validateBean( _appointment ) )
        {
            return redirect( request, VIEW_MODIFY_APPOINTMENT, PARAMETER_ID_APPOINTMENT, _appointment.getId( ) );
        }

        AppointmentHome.update( _appointment );

        // change Lobby to the appointment
        String strIdLobby = request.getParameter( PARAMETER_ID_LOBBY );
        String strSelectLobby = request.getParameter( PARAMETER_SELECT_LOBBY );

        int idLobby = StringUtil.getIntValue( strIdLobby, -1 );

        Lobby lobby = LobbyHome.findByPrimaryKey( idLobby );

        // check if it's not a new lobby to create
        if ( idLobby > 0 && lobby != null && lobby.getName( ).equals( strSelectLobby ) )
        {
            LobbyAppointmentHome.removeByAppointmentId( _appointment.getId( ) );
            LobbyAppointmentHome.create( new LobbyAppointment( lobby.getId( ), _appointment.getId( ) ) );
            _appointment.getLobbyList( ).add( lobby );
        }
        else
            if ( !StringUtils.isBlank( strSelectLobby ) )
            {
                Lobby newLobby = new Lobby( );
                newLobby.setName( strSelectLobby );
                newLobby.setVersionDate( new Date( ( new java.util.Date( ) ).getTime( ) ) );
                newLobby = LobbyHome.create( newLobby );

                LobbyAppointmentHome.removeByAppointmentId( _appointment.getId( ) );
                LobbyAppointmentHome.create( new LobbyAppointment( newLobby.getId( ), _appointment.getId( ) ) );
                _appointment.getLobbyList( ).add( newLobby );

            }

        addInfo( INFO_APPOINTMENT_UPDATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_APPOINTMENTS );
    }

    /**
     * check if there is a MyLuteceUser Authenticated
     * 
     * @param request
     * @return the Id of the lutece user
     * @throws UserNotSignedException
     */
    private String checkMyLuteceAuthentication( HttpServletRequest request ) throws UserNotSignedException
    {
        // check if authentication is enable
        if ( !SecurityService.isAuthenticationEnable( ) )
            return null;

        LuteceUser luteceUser = SecurityService.getInstance( ).getRegisteredUser( request );

        // user not registred
        if ( luteceUser == null )
            throw new UserNotSignedException( );

        return luteceUser.getName( );
    }

}
