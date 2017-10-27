<jsp:useBean id="managedelegationDelegation" scope="session" class="fr.paris.lutece.plugins.transparency.web.DelegationJspBean" />
<% String strContent = managedelegationDelegation.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
