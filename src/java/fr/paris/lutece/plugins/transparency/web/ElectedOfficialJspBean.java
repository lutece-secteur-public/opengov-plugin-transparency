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

import fr.paris.lutece.plugins.transparency.business.ElectedOfficial;
import fr.paris.lutece.plugins.transparency.business.ElectedOfficialHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage ElectedOfficial features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageElectedOfficials.jsp", controllerPath = "jsp/admin/plugins/transparency/", right = "TRANSPARENCY_ELECTED_OFFICIALS_MANAGEMENT" )
public class ElectedOfficialJspBean extends AbstractManageElectedOfficialsJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_ELECTEDOFFICIALS = "/admin/plugins/transparency/manage_electedofficials.html";
    private static final String TEMPLATE_CREATE_ELECTEDOFFICIAL = "/admin/plugins/transparency/create_electedofficial.html";
    private static final String TEMPLATE_MODIFY_ELECTEDOFFICIAL = "/admin/plugins/transparency/modify_electedofficial.html";

    // Parameters
    private static final String PARAMETER_ID_ELECTEDOFFICIAL = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ELECTEDOFFICIALS = "transparency.manage_electedofficials.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ELECTEDOFFICIAL = "transparency.modify_electedofficial.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_ELECTEDOFFICIAL = "transparency.create_electedofficial.pageTitle";

    // Markers
    private static final String MARK_ELECTEDOFFICIAL_LIST = "electedofficial_list";
    private static final String MARK_ELECTEDOFFICIAL = "electedofficial";

    private static final String JSP_MANAGE_ELECTEDOFFICIALS = "jsp/admin/plugins/transparency/ManageElectedOfficials.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_ELECTEDOFFICIAL = "transparency.message.confirmRemoveElectedOfficial";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "transparency.model.entity.electedofficial.attribute.";

    // Views
    private static final String VIEW_MANAGE_ELECTEDOFFICIALS = "manageElectedOfficials";
    private static final String VIEW_CREATE_ELECTEDOFFICIAL = "createElectedOfficial";
    private static final String VIEW_MODIFY_ELECTEDOFFICIAL = "modifyElectedOfficial";

    // Actions
    private static final String ACTION_CREATE_ELECTEDOFFICIAL = "createElectedOfficial";
    private static final String ACTION_MODIFY_ELECTEDOFFICIAL = "modifyElectedOfficial";
    private static final String ACTION_REMOVE_ELECTEDOFFICIAL = "removeElectedOfficial";
    private static final String ACTION_CONFIRM_REMOVE_ELECTEDOFFICIAL = "confirmRemoveElectedOfficial";

    // Infos
    private static final String INFO_ELECTEDOFFICIAL_CREATED = "transparency.info.electedofficial.created";
    private static final String INFO_ELECTEDOFFICIAL_UPDATED = "transparency.info.electedofficial.updated";
    private static final String INFO_ELECTEDOFFICIAL_REMOVED = "transparency.info.electedofficial.removed";

    // Session variable to store working values
    private ElectedOfficial _electedofficial;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_ELECTEDOFFICIALS, defaultView = true )
    public String getManageElectedOfficials( HttpServletRequest request )
    {
        _electedofficial = null;
        List<ElectedOfficial> listElectedOfficials = ElectedOfficialHome.getElectedOfficialsList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_ELECTEDOFFICIAL_LIST, listElectedOfficials, JSP_MANAGE_ELECTEDOFFICIALS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_ELECTEDOFFICIALS, TEMPLATE_MANAGE_ELECTEDOFFICIALS, model );
    }

    /**
     * Returns the form to create a electedofficial
     *
     * @param request
     *            The Http request
     * @return the html code of the electedofficial form
     */
    @View( VIEW_CREATE_ELECTEDOFFICIAL )
    public String getCreateElectedOfficial( HttpServletRequest request )
    {
        _electedofficial = ( _electedofficial != null ) ? _electedofficial : new ElectedOfficial( );

        Map<String, Object> model = getModel( );
        model.put( MARK_ELECTEDOFFICIAL, _electedofficial );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_ELECTEDOFFICIAL, TEMPLATE_CREATE_ELECTEDOFFICIAL, model );
    }

    /**
     * Process the data capture form of a new electedofficial
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_ELECTEDOFFICIAL )
    public String doCreateElectedOfficial( HttpServletRequest request )
    {
        populate( _electedofficial, request );

        // Check constraints
        if ( !validateBean( _electedofficial, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_ELECTEDOFFICIAL );
        }

        ElectedOfficialHome.create( _electedofficial );
        addInfo( INFO_ELECTEDOFFICIAL_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_ELECTEDOFFICIALS );
    }

    /**
     * Manages the removal form of a electedofficial whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_ELECTEDOFFICIAL )
    public String getConfirmRemoveElectedOfficial( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ELECTEDOFFICIAL ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_ELECTEDOFFICIAL ) );
        url.addParameter( PARAMETER_ID_ELECTEDOFFICIAL, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_ELECTEDOFFICIAL, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a electedofficial
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage electedofficials
     */
    @Action( ACTION_REMOVE_ELECTEDOFFICIAL )
    public String doRemoveElectedOfficial( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ELECTEDOFFICIAL ) );
        ElectedOfficialHome.remove( nId );
        addInfo( INFO_ELECTEDOFFICIAL_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_ELECTEDOFFICIALS );
    }

    /**
     * Returns the form to update info about a electedofficial
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_ELECTEDOFFICIAL )
    public String getModifyElectedOfficial( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ELECTEDOFFICIAL ) );

        if ( _electedofficial == null || ( _electedofficial.getId( ) != nId ) )
        {
            _electedofficial = ElectedOfficialHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_ELECTEDOFFICIAL, _electedofficial );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_ELECTEDOFFICIAL, TEMPLATE_MODIFY_ELECTEDOFFICIAL, model );
    }

    /**
     * Process the change form of a electedofficial
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_ELECTEDOFFICIAL )
    public String doModifyElectedOfficial( HttpServletRequest request )
    {
        populate( _electedofficial, request );

        // Check constraints
        if ( !validateBean( _electedofficial, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_ELECTEDOFFICIAL, PARAMETER_ID_ELECTEDOFFICIAL, _electedofficial.getId( ) );
        }

        ElectedOfficialHome.update( _electedofficial );
        addInfo( INFO_ELECTEDOFFICIAL_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_ELECTEDOFFICIALS );
    }
}
