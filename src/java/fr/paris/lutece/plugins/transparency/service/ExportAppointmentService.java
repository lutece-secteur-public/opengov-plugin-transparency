/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.transparency.service;

import fr.paris.lutece.plugins.mylutece.business.attribute.IAttribute;
import fr.paris.lutece.plugins.mylutece.business.attribute.MyLuteceUserField;
import fr.paris.lutece.plugins.mylutece.business.attribute.MyLuteceUserFieldHome;
import fr.paris.lutece.plugins.mylutece.modules.database.authentication.business.DatabaseHome;
import fr.paris.lutece.plugins.mylutece.modules.database.authentication.service.DatabasePlugin;
import fr.paris.lutece.plugins.mylutece.service.MyLutecePlugin;
import fr.paris.lutece.plugins.transparency.business.Appointment;
import fr.paris.lutece.portal.business.xsl.XslExport;
import fr.paris.lutece.portal.business.xsl.XslExportHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.xsl.XslExportService;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.xml.XmlUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author leridons
 */
public class ExportAppointmentService {
    
    // properties
    private static final int  PROPERTY_XSL_EXPORT_ID =129;
    private static final String PROPERTY_DEFAULT_OUTPUT_FILE_NAME = "export-rdv" ;
    
    // XML constants
    private static final String CONSTANT_XML_APPOINTMENTS = "appointments";
    private static final String CONSTANT_XML_APPOINTMENT = "appointment";
    private static final String CONSTANT_XML_ID = "id";
    private static final String CONSTANT_XML_TITLE = "title" ;
    private static final String CONSTANT_XML_DESCRIPTION = "description";
    private static final String CONSTANT_XML_ELECTEDOFFICIALS = "electedofficials";
    private static final String CONSTANT_XML_LOBBIES = "lobbies";
    private static final String CONSTANT_XML_CONTACTS = "contacts";
    private static final String CONSTANT_XML_STARTDATE = "startdate" ;
    
    // export type constants
    private static final String CONSTANT_MIME_TYPE_CSV = "application/csv";
    private static final String CONSTANT_MIME_TYPE_XML = "application/xml";
    private static final String CONSTANT_MIME_TYPE_TEXT_CSV = "text/csv";
    private static final String CONSTANT_MIME_TYPE_OCTETSTREAM = "application/octet-stream";
    private static final String CONSTANT_EXTENSION_CSV_FILE = ".csv";
    private static final String CONSTANT_EXTENSION_XML_FILE = ".xml";
    private static final String CONSTANT_QUOTE = "\"";
    private static final String CONSTANT_ATTACHEMENT_FILE_NAME = "attachement; filename=\"";
    private static final String CONSTANT_ATTACHEMENT_DISPOSITION = "Content-Disposition";
    
    public static void exportAppointmentToCSV (HttpServletRequest request, HttpServletResponse response, List<Appointment> list ) 
      throws IOException
    { 
        int nIdXslExport = PROPERTY_XSL_EXPORT_ID;

        XslExport xslExport = XslExportHome.findByPrimaryKey( nIdXslExport );

        
        StringBuffer sbXml = new StringBuffer( XmlUtil.getXmlHeader(  ) );
        
        XmlUtil.beginElement( sbXml, CONSTANT_XML_APPOINTMENTS );
        //TODO : add headers ? 
        for ( Appointment appointment : list )
        {
            sbXml.append( getXmlFromAppointment( appointment ) );
        }
        XmlUtil.endElement( sbXml, CONSTANT_XML_APPOINTMENTS );

        String strXml = StringUtil.replaceAccent( sbXml.toString(  ) );
        
        String strExportedAppointments = XslExportService.exportXMLWithXSL( nIdXslExport, strXml );

        if (response != null) 
        {
            if ( CONSTANT_MIME_TYPE_CSV.contains( xslExport.getExtension(  ) ) )
            {
                response.setContentType( CONSTANT_MIME_TYPE_CSV );
            }
            else if ( CONSTANT_EXTENSION_XML_FILE.contains( xslExport.getExtension(  ) ) )
            {
                response.setContentType( CONSTANT_MIME_TYPE_XML );
            }
            else
            {
                response.setContentType( CONSTANT_MIME_TYPE_OCTETSTREAM );
            }

            String strFileName = PROPERTY_DEFAULT_OUTPUT_FILE_NAME + "." + xslExport.getExtension(  );
            response.setHeader( CONSTANT_ATTACHEMENT_DISPOSITION,
                CONSTANT_ATTACHEMENT_FILE_NAME + strFileName + CONSTANT_QUOTE );

        
            PrintWriter out = response.getWriter(  );
            out.write( strExportedAppointments );
            out.flush(  );
            out.close(  );
        }

    }

    /**
     * Get a XML string describing a given user
     * @param appointment
     * @return A string of XML with the information of the user.
     */
    public static String getXmlFromAppointment( Appointment appointment )
    {

        StringBuffer sbXml = new StringBuffer(  );
        DateFormat dateFormat = new SimpleDateFormat(  );

        XmlUtil.beginElement( sbXml, CONSTANT_XML_APPOINTMENT );
        
        XmlUtil.addElement( sbXml, CONSTANT_XML_ID, appointment.getId());
        XmlUtil.addElement( sbXml, CONSTANT_XML_TITLE, appointment.getTitle( ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_DESCRIPTION, appointment.getDescription( ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_STARTDATE, dateFormat.format( appointment.getStartDate( ) ) );
        
        List<String> electedOfficialsList = 
                appointment.getElectedOfficialList()
                    .stream()
                    .map(e -> e.getLastName())
                    .collect(Collectors.toList( ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_ELECTEDOFFICIALS, (!electedOfficialsList.isEmpty()?electedOfficialsList.get(0):"") );
        
        List<String> lobbyList = 
                appointment.getLobbyList()
                    .stream()
                    .map(e -> e.getName())
                    .collect(Collectors.toList( ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_LOBBIES, (!lobbyList.isEmpty()?lobbyList.get(0):"") );
        XmlUtil.addElement( sbXml, CONSTANT_XML_CONTACTS, appointment.getContacts( ) );
        
        XmlUtil.endElement( sbXml, CONSTANT_XML_APPOINTMENT );

        return sbXml.toString(  );
    }
}
