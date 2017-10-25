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
import fr.paris.lutece.plugins.transparency.business.AppointmentHome;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;

import java.util.Map;
import javax.servlet.http.HttpServletRequest; 

/**
 * This class provides the user interface to manage Appointment xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "appointment" , pageTitleI18nKey = "transparency.xpage.appointment.pageTitle" , pagePathI18nKey = "transparency.xpage.appointment.pagePathLabel" )
public class AppointmentXPage extends MVCApplication
{
    // Templates
    private static final String TEMPLATE_MANAGE_APPOINTMENTS="/skin/plugins/transparency/manage_appointments.html";
    private static final String TEMPLATE_CREATE_APPOINTMENT="/skin/plugins/transparency/create_appointment.html";
    private static final String TEMPLATE_MODIFY_APPOINTMENT="/skin/plugins/transparency/modify_appointment.html";
    
    // JSP
    private static final String JSP_PAGE_PORTAL = "jsp/site/Portal.jsp";
    
    // Parameters
    private static final String PARAMETER_ID_APPOINTMENT="id";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_PAGE = "page";
    
    // Markers
    private static final String MARK_APPOINTMENT_LIST = "appointment_list";
    private static final String MARK_APPOINTMENT = "appointment";
    
    // Message
    private static final String MESSAGE_CONFIRM_REMOVE_APPOINTMENT = "transparency.message.confirmRemoveAppointment";
    
    // Views
    private static final String VIEW_MANAGE_APPOINTMENTS = "manageAppointments";
    private static final String VIEW_CREATE_APPOINTMENT = "createAppointment";
    private static final String VIEW_MODIFY_APPOINTMENT = "modifyAppointment";

    // Actions
    private static final String ACTION_CREATE_APPOINTMENT = "createAppointment";
    private static final String ACTION_MODIFY_APPOINTMENT= "modifyAppointment";
    private static final String ACTION_REMOVE_APPOINTMENT = "removeAppointment";
    private static final String ACTION_CONFIRM_REMOVE_APPOINTMENT = "confirmRemoveAppointment";

    // Infos
    private static final String INFO_APPOINTMENT_CREATED = "transparency.info.appointment.created";
    private static final String INFO_APPOINTMENT_UPDATED = "transparency.info.appointment.updated";
    private static final String INFO_APPOINTMENT_REMOVED = "transparency.info.appointment.removed";
    
    // Session variable to store working values
    private Appointment _appointment;
    
    /**
     * Build the Manage View
     *
     * @param request The HTTP request
     * @return The Xpage
     */
    @View( value = VIEW_MANAGE_APPOINTMENTS, defaultView = true )
    public XPage getManageAppointments( HttpServletRequest request )
    {
        _appointment = null;
        Map<String, Object> model = getModel(  );
        model.put( MARK_APPOINTMENT_LIST, AppointmentHome.getAppointmentsList(  ) );

        return getXPage( TEMPLATE_MANAGE_APPOINTMENTS, request.getLocale(  ), model );
    }

    /**
     * Returns the form to create a appointment
     *
     * @param request The Http request
     * @return the html code of the appointment form
     */
    @View( VIEW_CREATE_APPOINTMENT )
    public XPage getCreateAppointment( HttpServletRequest request )
    {
        _appointment = ( _appointment != null ) ? _appointment : new Appointment(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_APPOINTMENT, _appointment );
           
        return getXPage( TEMPLATE_CREATE_APPOINTMENT, request.getLocale(  ), model );
    }

    /**
     * Process the data capture form of a new appointment
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_APPOINTMENT )
    public XPage doCreateAppointment( HttpServletRequest request )
    {
        populate( _appointment, request );

        // Check constraints
        if ( !validateBean( _appointment ) )
        {
            return redirectView( request, VIEW_CREATE_APPOINTMENT );
        }

        AppointmentHome.create( _appointment );
        addInfo( INFO_APPOINTMENT_CREATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_APPOINTMENTS );
    }

    /**
     * Manages the removal form of a appointment whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException
     */
    @Action( ACTION_CONFIRM_REMOVE_APPOINTMENT )
    public XPage getConfirmRemoveAppointment( HttpServletRequest request ) throws SiteMessageException
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_APPOINTMENT ) );
        UrlItem url = new UrlItem( JSP_PAGE_PORTAL );
        url.addParameter( PARAM_PAGE, MARK_APPOINTMENT );
        url.addParameter( PARAM_ACTION, ACTION_REMOVE_APPOINTMENT );
        url.addParameter( PARAMETER_ID_APPOINTMENT, nId );
        
        SiteMessageService.setMessage(request, MESSAGE_CONFIRM_REMOVE_APPOINTMENT, SiteMessage.TYPE_CONFIRMATION, url.getUrl(  ));
        return null;
    }

    /**
     * Handles the removal form of a appointment
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage appointments
     */
    @Action( ACTION_REMOVE_APPOINTMENT )
    public XPage doRemoveAppointment( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_APPOINTMENT ) );
        AppointmentHome.remove( nId );
        addInfo( INFO_APPOINTMENT_REMOVED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_APPOINTMENTS );
    }

    /**
     * Returns the form to update info about a appointment
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_APPOINTMENT )
    public XPage getModifyAppointment( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_APPOINTMENT ) );

        if ( _appointment == null  || ( _appointment.getId( ) != nId ))
        {
            _appointment = AppointmentHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_APPOINTMENT, _appointment );
        
        return getXPage( TEMPLATE_MODIFY_APPOINTMENT, request.getLocale(  ), model );
    }

    /**
     * Process the change form of a appointment
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_APPOINTMENT )
    public XPage doModifyAppointment( HttpServletRequest request )
    {
        populate( _appointment, request );

        // Check constraints
        if ( !validateBean( _appointment ) )
        {
            return redirect( request, VIEW_MODIFY_APPOINTMENT, PARAMETER_ID_APPOINTMENT, _appointment.getId( ) );
        }

        AppointmentHome.update( _appointment );
        addInfo( INFO_APPOINTMENT_UPDATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_APPOINTMENTS );
    }
}
