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
 * This class provides the user interface to manage ElectedOfficial xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "electedofficial", pageTitleI18nKey = "transparency.xpage.electedofficial.pageTitle", pagePathI18nKey = "transparency.xpage.electedofficial.pagePathLabel" )
public class ElectedOfficialXPage extends MVCApplication
{
    // Templates
    private static final String TEMPLATE_MANAGE_ELECTEDOFFICIALS = "/skin/plugins/transparency/manage_electedofficials.html";
    private static final String TEMPLATE_CREATE_ELECTEDOFFICIAL = "/skin/plugins/transparency/create_electedofficial.html";
    private static final String TEMPLATE_MODIFY_ELECTEDOFFICIAL = "/skin/plugins/transparency/modify_electedofficial.html";

    // JSP
    private static final String JSP_PAGE_PORTAL = "jsp/site/Portal.jsp";

    // Parameters
    private static final String PARAMETER_ID_ELECTEDOFFICIAL = "id";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_PAGE = "page";

    // Markers
    private static final String MARK_ELECTEDOFFICIAL_LIST = "electedofficial_list";
    private static final String MARK_ELECTEDOFFICIAL = "electedofficial";

    // Message
    private static final String MESSAGE_CONFIRM_REMOVE_ELECTEDOFFICIAL = "transparency.message.confirmRemoveElectedOfficial";

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
     * @return The Xpage
     */
    @View( value = VIEW_MANAGE_ELECTEDOFFICIALS, defaultView = true )
    public XPage getManageElectedOfficials( HttpServletRequest request )
    {
        _electedofficial = null;
        Map<String, Object> model = getModel( );
        model.put( MARK_ELECTEDOFFICIAL_LIST, ElectedOfficialHome.getElectedOfficialsList( ) );

        return getXPage( TEMPLATE_MANAGE_ELECTEDOFFICIALS, request.getLocale( ), model );
    }

    /**
     * Returns the form to create a electedofficial
     *
     * @param request
     *            The Http request
     * @return the html code of the electedofficial form
     */
    @View( VIEW_CREATE_ELECTEDOFFICIAL )
    public XPage getCreateElectedOfficial( HttpServletRequest request )
    {
        _electedofficial = ( _electedofficial != null ) ? _electedofficial : new ElectedOfficial( );

        Map<String, Object> model = getModel( );
        model.put( MARK_ELECTEDOFFICIAL, _electedofficial );

        return getXPage( TEMPLATE_CREATE_ELECTEDOFFICIAL, request.getLocale( ), model );
    }

    /**
     * Process the data capture form of a new electedofficial
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_ELECTEDOFFICIAL )
    public XPage doCreateElectedOfficial( HttpServletRequest request )
    {
        populate( _electedofficial, request );

        // Check constraints
        if ( !validateBean( _electedofficial ) )
        {
            return redirectView( request, VIEW_CREATE_ELECTEDOFFICIAL );
        }

        ElectedOfficialHome.create( _electedofficial );
        addInfo( INFO_ELECTEDOFFICIAL_CREATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_ELECTEDOFFICIALS );
    }

    /**
     * Manages the removal form of a electedofficial whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException
     */
    @Action( ACTION_CONFIRM_REMOVE_ELECTEDOFFICIAL )
    public XPage getConfirmRemoveElectedOfficial( HttpServletRequest request ) throws SiteMessageException
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ELECTEDOFFICIAL ) );
        UrlItem url = new UrlItem( JSP_PAGE_PORTAL );
        url.addParameter( PARAM_PAGE, MARK_ELECTEDOFFICIAL );
        url.addParameter( PARAM_ACTION, ACTION_REMOVE_ELECTEDOFFICIAL );
        url.addParameter( PARAMETER_ID_ELECTEDOFFICIAL, nId );

        SiteMessageService.setMessage( request, MESSAGE_CONFIRM_REMOVE_ELECTEDOFFICIAL, SiteMessage.TYPE_CONFIRMATION, url.getUrl( ) );
        return null;
    }

    /**
     * Handles the removal form of a electedofficial
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage electedofficials
     */
    @Action( ACTION_REMOVE_ELECTEDOFFICIAL )
    public XPage doRemoveElectedOfficial( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ELECTEDOFFICIAL ) );
        ElectedOfficialHome.remove( nId );
        addInfo( INFO_ELECTEDOFFICIAL_REMOVED, getLocale( request ) );

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
    public XPage getModifyElectedOfficial( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ELECTEDOFFICIAL ) );

        if ( _electedofficial == null || ( _electedofficial.getId( ) != nId ) )
        {
            _electedofficial = ElectedOfficialHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_ELECTEDOFFICIAL, _electedofficial );

        return getXPage( TEMPLATE_MODIFY_ELECTEDOFFICIAL, request.getLocale( ), model );
    }

    /**
     * Process the change form of a electedofficial
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_ELECTEDOFFICIAL )
    public XPage doModifyElectedOfficial( HttpServletRequest request )
    {
        populate( _electedofficial, request );

        // Check constraints
        if ( !validateBean( _electedofficial ) )
        {
            return redirect( request, VIEW_MODIFY_ELECTEDOFFICIAL, PARAMETER_ID_ELECTEDOFFICIAL, _electedofficial.getId( ) );
        }

        ElectedOfficialHome.update( _electedofficial );
        addInfo( INFO_ELECTEDOFFICIAL_UPDATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_ELECTEDOFFICIALS );
    }
}
