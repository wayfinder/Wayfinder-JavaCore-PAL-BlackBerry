/*******************************************************************************
 * Copyright (c) 1999-2010, Vodafone Group Services
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 *     * Redistributions of source code must retain the above copyright 
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above 
 *       copyright notice, this list of conditions and the following 
 *       disclaimer in the documentation and/or other materials provided 
 *       with the distribution.
 *     * Neither the name of Vodafone Group Services nor the names of its 
 *       contributors may be used to endorse or promote products derived 
 *       from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
 * OF SUCH DAMAGE.
 ******************************************************************************/
package com.wayfinder.pal.blackberry.network.transport;

import java.io.EOFException;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.synchronization.ConverterUtilities;
import net.rim.device.api.util.DataBuffer;


/**
 * This class is intended to be an easy interface to use a specific Browser
 * on a BlackBerry device.
 * <p>
 * As of 2008-06-10 There are 5 different browsing models available:
 * <ul>
 * <li>WAP</li>
 * <li>BlackBerry Internet Service (BIS)</li>
 * <li>BlackBerry Enterprise Server (BES)</li> 
 * <li>WiFi</li>
 * <li>BlackBerry Unite!</li> 
 * </ul>
 * 
 * <ol>
 * <li><b>WAP Browser:</b><br> 
 * This forces the browser to go through the carrier WAP gateway
 * to access the Internet. Only use the WAP Browser if:
 * <ul>
 *   <li>You need to identify the carrier network that the user is coming
 *   from (the WAP gateway will add this info to the header)</li>
 *   <li>You want to access a site where the user will be able to stream
 *   content using the RTSP protocol</li>
 * </ul></li>
 * 
 * <li><b>BlackBerry Internet Service Browser:</b><br> 
 * This forces the browser to go through the RIM-hosted BIS
 * gateway to access the Internet. Use the BIS Browser if:
 * <ul>
 *   <li>The BIS browser offers a consistent experience across all
 *   networks and does not limit which sites can be accessed, therefore
 *   it should be used whenever possible</li>
 *   <li>You MUST use the BIS Browser if you are taking the user to a web
 *   page where they may subscribe to special services that are only
 *   available using BIS, such as "Web Signals"</li>
 * </ul>
 * Due to NDAs with RIM, the methods for extracting the BIS browser are not
 * included in this codebase. Please contact RIM for more information regarding
 * the BIS browser.
 * </li><p>
 * <li><b>BlackBerry (MDS) Browser:</b><br> 
 * If the user is a corporate user, this forces the browser
 * to go through their corporate BES (behind their IT firewall). Only
 * use the BES Browser if:
 * <ul>
 *   <li>The website you are addressing is inside the corporate firewall</li>
 *   <li>The BIS Browser service is not available</li>
 * </ul></li>
 * 
 * <li><b>Hotspot Browser:</b><br> 
 * This forces the browser to use the WiFi access point. Only
 * use the Unite Browser if:
 * <ul>
 *   <li>You are certain that the device has WiFi coverage</li>
 *   <li>You are certain that the currently connected WiFi network gives access
 *   to the website you are addressing</li>
 * </ul></li>
 *
 * <li><b>Unite Browser:</b><br>
 * This forces the browser to use the Unite gateway. Only
 * use the Unite Browser if:
 * <ul>
 *   <li>The website you are addressing is inside the Unite network</li>
 * </ul></li>
 * </ol>
 * 
 * Please note that the above applies to the underlying transport path. A device
 * may actually have an infinite number of browser icons on the home screen
 * since the virtual preload system uses the browser. While each of the icons 
 * will have a corresponding [BrowserConfig] service book and may look 
 * completely different, they will use either the BIS-B, a WAP or WPTCP
 * service book as the underlying transport path.
 * <p>
 */
public final class BrowserUtils {

    private BrowserUtils() {}
    
    //-------------------------------------------------------------------------
    // Constants


    /**
     * This forces the browser to go through the carrier WAP gateway
     * to access the Internet. Only use the WAP Browser if:
     * <ul>
     * <li>You need to identify the carrier network that the user is coming
     * from (the WAP gateway will add this info to the header)</li>
     * <li>You want to access a site where the user will be able to stream
     * content using the RTSP protocol</li>
     * </ul>
     * Please note that as there can only be one browser of this type on the
     * device, it's not specified here if the underlying transport is of type
     * WAP(UDP) or WPTCP(TCP).
     */
    public static final int BROWSER_WAP         = 0;


    /**
     * If the user is a corporate user, this forces the browser
     * to go through their corporate BES (behind their IT firewall). Only
     * use the BES Browser if:
     * <ul>
     * <li>The website you are addressing is inside the corporate firewall</li>
     * <li>The BIS Browser service is not available</li>
     */
    public static final int BROWSER_COMPANY  = 2;


    /**
     * This forces the browser to use the WiFi access point.
     * <p>
     * <b>This method only works if called on a device with WiFi radio parts.
     * It's the callers responsiblity to ensure that the device is connected
     * to a WiFi network before using the browser</b>
     */
    public static final int BROWSER_WIFI        = 3;


    /**
     * This forces the browser to use the Unite gateway.
     * <p>
     * It's the callers responsiblity to ensure that the device is connected
     * to a Unite network before using the browser</b>
     */
    public static final int BROWSER_UNITE       = 4;

    
    //-------------------------------------------------------------------------
    // Browser retrieval
    

    /**
     * This method will attempt to go through the supplied list of prefered
     * browsers and return the one it first encounters.
     * <p>
     * This method expects the supplied array to contain the browsers it should
     * try in order of priority. The browser at position 0 is the highest
     * prioritized with the priority becoming lower as the array position gets
     * higher.
     * <p>
     * It can be used as a way to avoid using "dangerous" browsers like the MDS
     * or Unite that are more often subject to firewalls and domain rules.
     * <p>
     * If this method cannot find any of the browsers in the supplied array,
     * this method returns the same as getPresetBrowser().
     * 
     * @param aBrowserTypeArray An array containing BROWSER constants from this 
     * class in order of priority
     * @return The best browser as prioritized in the array
     * @throws IllegalArgumentException If the browser type is not one of the
     * BROWSER constants in this class
     */
    public static BrowserSession getBrowserByPriority(int[] aBrowserTypeArray) {
        for (int i = 0; i < aBrowserTypeArray.length; i++) {
            final int thisBrowser = aBrowserTypeArray[i];
            BrowserSession session = getBrowserType(thisBrowser);
            if(session != null) {
                return session;
            }
        }
        return getUserPresetBrowser();
    }


    /**
     * Returns the browser that is preset in the device settings
     * 
     * @return The default browser set by the user
     */
    public static BrowserSession getUserPresetBrowser() {
        return Browser.getDefaultSession();
    }
    
    
    /**
     * Returns a specific browser specified by the aBrowserType parameter. If
     * the browser does not exist on the device, this method returns null
     * 
     * @param aBrowserType One of the BROWSER constants in this class
     * @return The specified browser or null if it does not exist
     * @throws IllegalArgumentException If the browser type is not one of the
     * BROWSER constants in this class
     */
    public static BrowserSession getBrowserType(int aBrowserType) {
        assertValidBrowserType(aBrowserType);
        ServiceRecord record = getServiceRecordForBrowserType(aBrowserType);
        if(record != null) {
            return Browser.getSession(record.getUid());
        }
        return null;
    }
    
    
    //-------------------------------------------------------------------------
    // Transport retrieval


    /**
     * Returns the transport record for a specific browser
     * <p>
     * This method will try to find the browser of the specified type and
     * return the ServiceRecord for the underlying transport. If the browser
     * does not exist or there is no underlying transport affiliated with the
     * browser, this method returns null.
     * 
     * @param aBrowserType One of the BROWSER_ constants in this class
     * @return The ServiceRecord containing the transport or null if it cannot
     * be found
     * @throws IllegalArgumentException If the browser type is not one of the
     * BROWSER constants in this class
     */
    public static ServiceRecord getTransportForBrowserType(int aBrowserType) {
        assertValidBrowserType(aBrowserType);
        ServiceRecord record = getServiceRecordForBrowserType(aBrowserType);
        if(record != null) {
            final String transportCID = getDataString(record, 3);
            final String transportUID = getDataString(record, 4);
            if(transportCID != null && transportUID != null) {
                ServiceBook sb = ServiceBook.getSB();
                return sb.getRecordByUidAndCid(transportUID, transportCID);
            }
        }
        return null;
    }


    //-------------------------------------------------------------------------
    // Private methods


    /**
     * Returns the ServiceRecord (with CID 'BrowserConfig') that defines a
     * specific browser type
     * 
     * @param aBrowserType One of the BROWSER constants in this class
     * @return The ServiceRecord of the browser or null if not found
     */
    private static ServiceRecord getServiceRecordForBrowserType(int aBrowserType) {
        // Find all Browser service books...these are used to invoke the web 
        // browser with the proper connection service
        ServiceBook sb = ServiceBook.getSB();
        ServiceRecord[] records = sb.findRecordsByCid(Browser.CID_BROWSERCONFIG);
        if(records != null) {
            for( int i = 0; i < records.length; i++ ) {
                ServiceRecord myRecord = records[i];
                if(serviceRecordIsValid(myRecord)) {
                    // we check if the underlying transport has the correct CID
                    // and UID for the browser type we want
                    String transportCID = getDataString(myRecord, 3);
                    String transportUID = getDataString(myRecord, 4);
                    if(isTransportCIDForType(transportCID, aBrowserType) 
                            && isTransportUIDForType(transportUID, aBrowserType)) {
                        
                        // it has the correct type. However, when running the
                        // device with twincards it's often seen that the
                        // underlying transport is actually only sent to the 
                        // device that has the main simcard. So we have to check
                        // that the transport is actually available on the
                        // device
                        
                        ServiceRecord transport = sb.getRecordByUidAndCid(transportUID, transportCID);
                        if(serviceRecordIsValid(transport)) {
                            // it's there :)
                            return myRecord;
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * Checks to see if the CID of the browser's underlying transport is matches
     * the CID of a specific browser type
     * 
     * @param aRecord The servicerecord of the Browser's underlying transport
     * @param aBrowserType One of the BROWSER constants in this class
     * @return true if the CID of the underlying transport matches the specified
     * browser type
     */
    private static boolean isTransportCIDForType(String aTransportCID, int aBrowserType) {
        if(aTransportCID != null) {
            aTransportCID = aTransportCID.toLowerCase();
            switch(aBrowserType) {
            case BROWSER_WAP:
            case BROWSER_WIFI:
                return contains(aTransportCID, "wptcp") || contains(aTransportCID, "wap"); 

            case BROWSER_COMPANY:
            case BROWSER_UNITE:
                return contains(aTransportCID, "ippp");
            }
        }
        return false;
    }


    /**
     * Checks to see if the UID of the browser's underlying transport is matches
     * the UID of a specific browser type
     *
     * @param aRecord The servicerecord of the Browser's underlying transport
     * @param aBrowserType One of the BROWSER constants in this class
     * @return true if the UID of the underlying transport matches the specified
     * browser type
     */
    private static boolean isTransportUIDForType(String aTransportUID, int aType) {
        if(aTransportUID != null) {
            aTransportUID = aTransportUID.toLowerCase();
            switch(aType) {
            case BROWSER_WAP:
                return contains(aTransportUID, "wap") &&
                      !contains(aTransportUID, "wifi") &&
                      !contains(aTransportUID, "mms");

            case BROWSER_COMPANY:
                return !contains(aTransportUID, "gpmds");

            case BROWSER_UNITE:
                return aTransportUID.startsWith("s") && !contains(aTransportUID, "wifi");

            case BROWSER_WIFI:
                return aTransportUID.startsWith("s tcp-wifi");
            }
        }
        return false;
    } 
    
    
    private static boolean serviceRecordIsValid(ServiceRecord aRecord) {
        return (aRecord != null) && aRecord.isValid() && !aRecord.isDisabled();
    }
    


    /**
     * Returns a field from the service record's application-specific data. The
     * field is expected to be a String.
     * 
     * @param aRecord The ServiceRecord that contains the app data
     * @param aFieldType The type of the field
     * @return The field as a String or null if the type was not found
     */
    private static String getDataString(ServiceRecord aRecord, int aFieldType) {
        byte[] data = aRecord.getApplicationData();
        if (data != null && data.length > 0) {
            DataBuffer buffer = new DataBuffer(data, 0, data.length, true);
            try {
                buffer.readByte();
            } catch (EOFException e1) {
                return null;
            }
            if (ConverterUtilities.findType(buffer, aFieldType)) {
                try {
                    return ConverterUtilities.readString(buffer);
                } catch (EOFException e) {
                    return null;
                }
            }
        }
        return null;
    }


    private static boolean contains(String str, String aSubStr) {
        // tired of writing "> -1"...
        return str.indexOf(aSubStr) > -1;
    }
    
    
    private static void assertValidBrowserType(int aBrowserType) {
        switch(aBrowserType) {
        case BROWSER_WAP:
        case BROWSER_COMPANY:
        case BROWSER_WIFI:
        case BROWSER_UNITE:
            //OK!
            return;
        }
        throw new IllegalArgumentException("Not a valid browser type");
    }
    
    
    private static String getDebugNameFor(int aBrowserType) {
        switch(aBrowserType) {
        case BROWSER_WAP:
            return "WAP Browser";
            
        case BROWSER_COMPANY:
            return "MDS Browser";
            
        case BROWSER_WIFI:
            return "WiFi Browser";
            
        case BROWSER_UNITE:
            return "Unite! Browser";
        }
        return ">>UNKNOWN BROWSER<<";
    }
}
