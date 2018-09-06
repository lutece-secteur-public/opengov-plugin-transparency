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

package fr.paris.lutece.plugins.transparency.business;

import fr.paris.lutece.test.LuteceTestCase;

import java.sql.Date;
import java.util.Calendar;
import org.apache.commons.lang.time.DateUtils;

public class AppointmentBusinessTest extends LuteceTestCase
{
    private final static String TITLE1 = "Title1";
    private final static String TITLE2 = "Title2";
    private final static String DESCRIPTION1 = "Description1";
    private final static String DESCRIPTION2 = "Description2";
    private final static Date STARTDATE1 = new Date( 1000000l );
    private final static Date STARTDATE2 = new Date( 2000000l );
    private final static Date ENDDATE1 = new Date( 1000000l );
    private final static Date ENDDATE2 = new Date( 2000000l );
    private final static int TYPEID1 = 1;
    private final static int TYPEID2 = 2;
    private final static String TYPELABEL1 = "TypeLabel1";
    private final static String TYPELABEL2 = "TypeLabel2";
    private final static String URL1 = "Url1";
    private final static String URL2 = "Url2";

    public void testBusiness( )
    {
        // Initialize an object
        Appointment appointment = new Appointment( );
        appointment.setTitle( TITLE1 );
        appointment.setDescription( DESCRIPTION1 );
        appointment.setStartDate( STARTDATE1 );
        appointment.setEndDate( ENDDATE1 );
        appointment.setTypeId( TYPEID1 );
        appointment.setTypeLabel( TYPELABEL1 );
        appointment.setUrl( URL1 );

        // Create test
        AppointmentHome.create( appointment );
        Appointment appointmentStored = AppointmentHome.findByPrimaryKey( appointment.getId( ) );
        assertEquals( appointmentStored.getTitle( ), appointment.getTitle( ) );
        assertEquals( appointmentStored.getDescription( ), appointment.getDescription( ) );
        assertEquals( DateUtils.truncate(appointmentStored.getStartDate( ),Calendar.DATE),
             DateUtils.truncate(appointment.getStartDate( ),Calendar.DATE) );
        assertEquals( DateUtils.truncate(appointmentStored.getEndDate( ),Calendar.DATE),
             DateUtils.truncate(appointment.getEndDate( ),Calendar.DATE) );
        assertEquals( appointmentStored.getTypeId( ), appointment.getTypeId( ) );
        assertEquals( appointmentStored.getTypeLabel( ), appointment.getTypeLabel( ) );
        assertEquals( appointmentStored.getUrl( ), appointment.getUrl( ) );

        // Update test
        appointment.setTitle( TITLE2 );
        appointment.setDescription( DESCRIPTION2 );
        appointment.setStartDate( STARTDATE2 );
        appointment.setEndDate( ENDDATE2 );
        appointment.setTypeId( TYPEID2 );
        appointment.setTypeLabel( TYPELABEL2 );
        appointment.setUrl( URL2 );
        AppointmentHome.update( appointment );
        appointmentStored = AppointmentHome.findByPrimaryKey( appointment.getId( ) );
        assertEquals( appointmentStored.getTitle( ), appointment.getTitle( ) );
        assertEquals( appointmentStored.getDescription( ), appointment.getDescription( ) );
        assertEquals( DateUtils.truncate(appointmentStored.getStartDate( ),Calendar.DATE),
             DateUtils.truncate(appointment.getStartDate( ),Calendar.DATE) );
        assertEquals( DateUtils.truncate(appointmentStored.getEndDate( ),Calendar.DATE),
             DateUtils.truncate(appointment.getEndDate( ),Calendar.DATE) );
        assertEquals( appointmentStored.getTypeId( ), appointment.getTypeId( ) );
        assertEquals( appointmentStored.getTypeLabel( ), appointment.getTypeLabel( ) );
        assertEquals( appointmentStored.getUrl( ), appointment.getUrl( ) );

        // List test
        AppointmentHome.getAppointmentsList( );

        // Delete test
        AppointmentHome.remove( appointment.getId( ) );
        appointmentStored = AppointmentHome.findByPrimaryKey( appointment.getId( ) );
        assertNull( appointmentStored );

    }

}
