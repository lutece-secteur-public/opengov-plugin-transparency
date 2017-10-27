<jsp:useBean id="managelobbiesLobby" scope="session" class="fr.paris.lutece.plugins.transparency.web.LobbyJspBean" />
<% String strContent = managelobbiesLobby.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
