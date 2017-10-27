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

import fr.paris.lutece.plugins.transparency.business.Delegation;
import fr.paris.lutece.plugins.transparency.business.DelegationHome;
import fr.paris.lutece.plugins.transparency.business.ElectedOfficialHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage Delegation features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDelegations.jsp", controllerPath = "jsp/admin/plugins/transparency/", right = "TRANSPARENCY_DELEGATION_MANAGEMENT" )
public class DelegationJspBean extends AbstractManageDelegationJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_DELEGATIONS = "/admin/plugins/transparency/manage_delegations.html";
    private static final String TEMPLATE_CREATE_DELEGATION = "/admin/plugins/transparency/create_delegation.html";
    private static final String TEMPLATE_MODIFY_DELEGATION = "/admin/plugins/transparency/modify_delegation.html";

    // Parameters
    private static final String PARAMETER_ID_DELEGATION = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DELEGATIONS = "transparency.manage_delegations.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DELEGATION = "transparency.modify_delegation.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DELEGATION = "transparency.create_delegation.pageTitle";

    // Markers
    private static final String MARK_DELEGATION_LIST = "delegation_list";
    private static final String MARK_DELEGATION = "delegation";
    private static final String MARK_ELECTEDOFFICIALS_LIST = "electedofficials_list";

    private static final String JSP_MANAGE_DELEGATIONS = "jsp/admin/plugins/transparency/ManageDelegations.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_DELEGATION = "transparency.message.confirmRemoveDelegation";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "transparency.model.entity.delegation.attribute.";

    // Views
    private static final String VIEW_MANAGE_DELEGATIONS = "manageDelegations";
    private static final String VIEW_CREATE_DELEGATION = "createDelegation";
    private static final String VIEW_MODIFY_DELEGATION = "modifyDelegation";

    // Actions
    private static final String ACTION_CREATE_DELEGATION = "createDelegation";
    private static final String ACTION_MODIFY_DELEGATION = "modifyDelegation";
    private static final String ACTION_REMOVE_DELEGATION = "removeDelegation";
    private static final String ACTION_CONFIRM_REMOVE_DELEGATION = "confirmRemoveDelegation";

    // Infos
    private static final String INFO_DELEGATION_CREATED = "transparency.info.delegation.created";
    private static final String INFO_DELEGATION_UPDATED = "transparency.info.delegation.updated";
    private static final String INFO_DELEGATION_REMOVED = "transparency.info.delegation.removed";
    
    // Session variable to store working values
    private Delegation _delegation;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DELEGATIONS, defaultView = true )
    public String getManageDelegations( HttpServletRequest request )
    {
        _delegation = null;
        List<Delegation> listDelegations = DelegationHome.getDelegationsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_DELEGATION_LIST, listDelegations, JSP_MANAGE_DELEGATIONS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DELEGATIONS, TEMPLATE_MANAGE_DELEGATIONS, model );
    }

    /**
     * Returns the form to create a delegation
     *
     * @param request The Http request
     * @return the html code of the delegation form
     */
    @View( VIEW_CREATE_DELEGATION )
    public String getCreateDelegation( HttpServletRequest request )
    {
        _delegation = ( _delegation != null ) ? _delegation : new Delegation(  );

        ReferenceList electedOfficialsList = ElectedOfficialHome.getElectedOfficialsReferenceList( );
        
        Map<String, Object> model = getModel(  );
        model.put( MARK_DELEGATION, _delegation );
        model.put( MARK_ELECTEDOFFICIALS_LIST, electedOfficialsList );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_DELEGATION, TEMPLATE_CREATE_DELEGATION, model );
    }

    /**
     * Process the data capture form of a new delegation
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_DELEGATION )
    public String doCreateDelegation( HttpServletRequest request )
    {
        populate( _delegation, request );

        // Check constraints
        if ( !validateBean( _delegation, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_DELEGATION );
        }

        DelegationHome.create( _delegation );
        addInfo( INFO_DELEGATION_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DELEGATIONS );
    }

    /**
     * Manages the removal form of a delegation whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DELEGATION )
    public String getConfirmRemoveDelegation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DELEGATION ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DELEGATION ) );
        url.addParameter( PARAMETER_ID_DELEGATION, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_DELEGATION, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a delegation
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage delegations
     */
    @Action( ACTION_REMOVE_DELEGATION )
    public String doRemoveDelegation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DELEGATION ) );
        DelegationHome.remove( nId );
        addInfo( INFO_DELEGATION_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DELEGATIONS );
    }

    /**
     * Returns the form to update info about a delegation
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DELEGATION )
    public String getModifyDelegation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DELEGATION ) );

        if ( _delegation == null || ( _delegation.getId(  ) != nId ))
        {
            _delegation = DelegationHome.findByPrimaryKey( nId );
        }

        ReferenceList electedOfficialsList = ElectedOfficialHome.getElectedOfficialsReferenceList( );
        
        
        Map<String, Object> model = getModel(  );
        model.put( MARK_DELEGATION, _delegation );
        model.put( MARK_ELECTEDOFFICIALS_LIST, electedOfficialsList );
        
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DELEGATION, TEMPLATE_MODIFY_DELEGATION, model );
    }

    /**
     * Process the change form of a delegation
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_DELEGATION )
    public String doModifyDelegation( HttpServletRequest request )
    {
        populate( _delegation, request );

        // Check constraints
        if ( !validateBean( _delegation, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_DELEGATION, PARAMETER_ID_DELEGATION, _delegation.getId( ) );
        }

        DelegationHome.update( _delegation );
        addInfo( INFO_DELEGATION_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DELEGATIONS );
    }
}