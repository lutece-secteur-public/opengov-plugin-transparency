<jsp:useBean id="manageappointementsAppointment" scope="session" class="fr.paris.lutece.plugins.transparency.web.AppointmentJspBean" />
<% String strContent = manageappointementsAppointment.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
