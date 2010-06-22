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

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import net.rim.device.api.io.http.HttpProtocolConstants;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.Branding;
import net.rim.device.api.system.CodeModuleManager;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.UnsupportedOperationException;


/**
 * This represents a transport type for the BlackBerry device that is used to
 * send and receive network data.
 * 
 */
public abstract class TransportType {
	
	// don't make the following strings final... blackberry specific
    
    /**
     * The UA Profile header information used when sending data through an MDS
     */
    static String RIM_MDS_UAPROF_HEADER;
    static String RIM_MDS_UAPROF_VALUE;
    
    /**
     * The UA Profile WAP header information used when sending data through an 
     * MDS
     */
    static String RIM_MDS_UAPROF_WAP_HEADER;
    static String RIM_MDS_UAPROF_WAP_VALUE;
    
    private static String RIM_USER_AGENT_SOFTWARE_VERSION;
    private static String RIM_USER_AGENT_VENDORID;
    private static String RIM_USER_AGENT_TRANSPORT;
    
    public static final byte TRANSPORT_MDS              = 0;
    public static final byte TRANSPORT_BIS              = 1;
    public static final byte TRANSPORT_SETTINGS_APN     = 2;
    public static final byte TRANSPORT_CARRIER_CONFIG   = 3;
    public static final byte TRANSPORT_BRANDED          = 4;
    
    static {
        String model = DeviceInfo.getDeviceName();
        String codeversion = getHandheldCodeVersion();
        String modelandcode = model + "/" + codeversion;
        
        RIM_USER_AGENT_VENDORID = " VendorID/" + Branding.getVendorId();

        String userAgent = "BlackBerry" + modelandcode + " ";
        RIM_USER_AGENT_SOFTWARE_VERSION = userAgent;
        
        // the RIM UAProf as described in technical database, article DB-00518
        // do NOT change this at all, or the MDS will transcode the data
        String uaProf = "http://www.blackberry.net/go/mobile/profiles/uaprof/" 
            + modelandcode.substring(0, modelandcode.lastIndexOf('.')) + ".rdf";
        
        RIM_MDS_UAPROF_HEADER = "profile";
        RIM_MDS_UAPROF_VALUE = uaProf;
        RIM_MDS_UAPROF_WAP_HEADER = "x-wap-profile";
        RIM_MDS_UAPROF_WAP_VALUE = "\"" + uaProf + "\"";
        RIM_USER_AGENT_TRANSPORT = " Transport/";
    }
    
    TransportType() {}

    
    //-------------------------------------------------------------------------
    // General information about the transport type

    
    /**
     * This is a string that identifies the connection. This will be used for
     * the internal logsystem and Lumberjack to show what transport type
     * the user is running.
     * 
     * @return The name of the connection
     */
    public abstract String getTransportDebugName();
    
    
    /**
     * Retrieves a "user friendly" name that can be displayed to the user as
     * the current transport type.
     * 
     * @return A user friendly name of the connection
     */
    public abstract String getTransportUserFriendlyName();
    
    
    public String toString() {
        return getTransportUserFriendlyName();
    }

    /**
     * The current identifier of the transport type. This will be used to save
     * the transport type to persistant memory and later load it on startup.
     * <p>
     * Transport types that are based on ServiceRecords from the service book
     * should create their identifiers based on the information in the record
     * to allow the application to detect later if the record has changed.
     * <p>
     * Transport types that can have multiple instances (like WPTCP), should
     * have information in the identifier that is unique to that particular
     * instance (such as CID/UID or the service record).
     * 
     * @see getRecordID()
     * @return A unique identifier.
     */
    abstract String getIdentifier();
    
    
    //-------------------------------------------------------------------------
    // Checks for usability
    
    public abstract byte getTransportTestConstant();
    
    /**
     * A check to see if the device has sufficient coverage for this type
     * to be used.
     * <p>
     * The following two conditions must be true before a device is considered 
     * to be in sufficient coverage for a transport type:
     * <ol>
     * <li>The device has the required software modules installed to handle
     * the transport type.</li>
     * <li>All service book records required for that transport type are 
     * routable.</li> 
     * </ol>
     * Note that being in coverage for a transport type is not a guarantee that 
     * such a transport attempt will be successful. For example, if the 
     * BlackBerry device is not properly provisioned for a transport type, then 
     * transport attempts of that type will fail even though there may be 
     * proper coverage.
     * <p>
     * Also, unresponsive destination servers can cause transport attempts to 
     * fail, even though the BlackBerry device may have sufficient coverage for 
     * the transport is it attempting. 
     * 
     * @return true if the device has sufficient coverage for this type to be
     * used
     */
    public abstract boolean hasSufficientCoverage();
    
    
    /**
     * Returns the permission this transport type MUST have to work.
     * 
     * @return One of the PERMISSION_RIM_TRANSPORT_ constants in {@link PermissionsDetector}
     */
    public abstract short getRequiredPermission();
    
    
    /**
     * Checks to see if this transport type supports sockets
     * 
     * @return true if and only if the transport type supports sockets
     */
    public abstract boolean supportSockets();
    
    
    //-------------------------------------------------------------------------
    // Connection parameters and HTTP headers
    
    
    /**
     * These are the connection parameters that will be added to the end
     * of all URL's for HTTP connections. This method will never return null.
     * <p>
     * This method should not be used for socket or HTTPS connections.

     * @see getSocketConnectionParameters()
     * @see getSocketSecureConnectionParameters()
     * @see getHTTPSecureConnectionParameters()
     * @return The parameters used for HTTP Connections.
     */
    public abstract String getHTTPConnectionParameters();
    
    
    /**
     * These are the connection parameters that will be added to the end
     * of all URL's for secure HTTP (HTTPS) connections. 
     * This method will never return null.
     * <p>
     * This method should not be used for socket or non-secure HTTP connections.

     * @see getSocketConnectionParameters()
     * @see getSocketSecureConnectionParameters()
     * @see getHTTPConnectionParameters()
     * @return The parameters used for HTTPS Connections.
     */
    public String getHTTPSecureConnectionParameters() {
        return getHTTPConnectionParameters() + ";EndToEndRequired";
    }
    
    /**
     * Returns a string that can be used as HTTP User-Agent header.
     * <p>
     * The string will begin with information about the BlackBerry model and
     * device software and end with information about the vendor id and the
     * currently used transport method.
     * <p>
     * If the parameter <code>aUsrAgnt</code> is null or a String with zero 
     * length, a custom string "[clienttype/application version]" will be used 
     * in it's place.
     * <p>
     * The header will be in the form:<br>
     * "[device and software info] [aUsrAgnt] [vendor and transport info"
     * <p>
     * <b>Example:</b><br>
     * <i>If called with aUsrAgnt parameter = "MyUserAgentString"</i><br>
     * "BlackBerry8800/4.2.1.108 MyUserAgentString VendorID/120 BES/MDS"
     * 
     * @param aUsrAgnt The existing user agent string or null if a generic string
     * should be used instead
     * @param aDebugName The debug name of the currently used transport
     * @return The string that can be used as HTTP User-Agent header
     */
    private static String getUserAgent(String aUsrAgnt, String aDebugName) {
        if(aUsrAgnt == null || aUsrAgnt.length() == 0) {
            aUsrAgnt = "";
        }
        
        // normal Java phones append their device signature before the
        // User Agent string set in the application. This does not seem to
        // be the case in the BlackBerry, so we set it ourselves
        aUsrAgnt = RIM_USER_AGENT_SOFTWARE_VERSION.concat(aUsrAgnt);
        
        // we add the vendor ID of the device to identify what carrier
        // the user has.
        aUsrAgnt = aUsrAgnt.concat(RIM_USER_AGENT_VENDORID);
        
        // add the current network the device is using
        aUsrAgnt = aUsrAgnt.concat(getNetworkString());
        
        // we also add the transport method used
        aUsrAgnt = aUsrAgnt.concat(RIM_USER_AGENT_TRANSPORT + aDebugName);

        return aUsrAgnt;
    }
    
    
    
    /**
     * Sets BlackBerry specific HTTP headers in a {@link HttpConnection} object.
     * <p>
     * This method will do the following:
     * <ol>
     * <li>If no user agent is previously set, it will add a user agent header
     * in the form of <code>"BlackBerry[model]/[device software version] 
     * [clienttype/application version] VendorID/[vendor ID]"</code></li>
     * <li>If a user agent is already set, it will append the string 
     * <code>"BlackBerry[model]/[device software version] "</code> before the
     * already set user agent.</li>
     * <li>For the current transport type, it will add or overwrite any
     * required headers for this transport type to work.</li>
     * 
     * @param aConn An HTTPConnection object
     * @throws IOException if the connection is in the connected state.
     */
    public final void setHTTPHeaders(HttpConnection aConn) throws IOException {
        String usrAgnt = aConn.getRequestProperty(HttpProtocolConstants.HEADER_USER_AGENT);
        aConn.setRequestProperty(HttpProtocolConstants.HEADER_USER_AGENT, getUserAgent(usrAgnt, getTransportDebugName()));
        appendSubHTTPHeaders(aConn);
    }
    
    
    
    /**
     * Adds any required headers for the transport type to work.
     * 
     * @param aConn An HTTPConnection object
     * @throws IOException if the connection is in the connected state.
     */
    abstract void appendSubHTTPHeaders(HttpConnection aConn) throws IOException;
    
    
    /**
     * Adds BlackBerry specific headers to a HashTable. This method is intended
     * to be used by socket connections and should NOT be used for ordinary
     * HTTP connections (eg direct use of a HTTPConnection object).
     * 
     * @param aHashTable The hashtable to add the headers to
     * @param aUsrAgnt A custom user agent. This string works in the same way
     * as when calling getUserAgent(aUsrAgnt)
     */
    public final void setHttpHeaders(Hashtable aHashTable, String aUsrAgnt) {
        aHashTable.put(HttpProtocolConstants.HEADER_USER_AGENT, getUserAgent(aUsrAgnt, getTransportDebugName()));
        appendSubHTTPHeaders(aHashTable);
    }
    
    abstract void appendSubHTTPHeaders(Hashtable aHashTable);
    
    
    
    /**
     * If this method returns true, the transport path will either unzip or
     * destroy data that is sent from the server as "Content-Encoding: gzip".
     * <p>
     * It is recommended that the connection system either refrains from
     * requesting gzipped data or uses an alternate string to identify the
     * encoding.
     * 
     * @return true if gzipped data will be altered or destroyed by the
     * transport type
     */
    public abstract boolean requiresAlternateGZIPEncoding();
    
    
    
    /**
     * These are the connection parameters that will be added to the end
     * of all URL's for socket connections. This method will never return null.
     * <p>
     * This method should not be used for HTTP, HTTPS or unencrypted socket 
     * connections.
     * 
     * @see getHTTPConnectionParameters()
     * @see getHTTPSecureConnectionParameters()
     * @see getSocketSecureConnectionParameters()
     * @return The parameters used for socket connections.
     * @throws UnsupportedOperationException if sockets are not supported by
     * this transport type
     */
    public abstract String getSocketConnectionParameters() throws UnsupportedOperationException;
    
    
    
    
    /**
     * These are the connection parameters that will be added to the end
     * of all URL's for secure socket connections. 
     * This method will never return null.
     * <p>
     * This method should not be used for unencrypted sockets or any HTTP 
     * connections.
     *
     * @see getSocketConnectionParameters()
     * @see getHTTPConnectionParameters()
     * @return The parameters used for HTTPS Connections.
     */
    public String getSocketSecureConnectionParameters() {
        return getSocketConnectionParameters() + ";EndToEndRequired";
    }
    
    
    
    //-------------------------------------------------------------------------
    // Utility methods
    
    
    /**
     * A simple check to see if the record sent as a parameter is valid and not
     * disabled
     * 
     * @param aRecord A service record to check
     * @throws NullPointerException if aRecord is null
     */
    static boolean isValidRecord(ServiceRecord aRecord) {        
        return aRecord != null && aRecord.isValid() && !aRecord.isDisabled();
    }
    
    
    /**
     * A simple check to see if a code module is installed on the device. This
     * can be used to see if a device is capable of supporting a set transport
     * type.
     * 
     * @param aModuleHandle The handle of the module to check
     * @return true if and only if the module is installed on the device
     */
    static boolean isModuleInstalledOnDevice(String aModuleHandle) {
        return CodeModuleManager.getModuleHandle(aModuleHandle) != 0;
    }
    
    
    /**
     * Returns a unique string for the service record sent as a parameter. If
     * the parameter is null, this method will return the string "null".
     * 
     * @param aRecord The service record to create an ID for.
     * @return A generated record ID or the String "null" if the parameter 
     * aRecord was null
     */
    static String getRecordID(ServiceRecord aRecord) {
        if(aRecord != null) {
            return aRecord.getCid() + aRecord.getUid();
        }
        return "null";
    }
    
    
    /**
     * Retrieves the version of the handheld code installed on the BlackBerry.
     * Please note that this will do a search for it every time so it's not
     * very efficient.
     * 
     * @return The version of the handheld code
     */
    private static String getHandheldCodeVersion() {
        //get the ApplicationManager
        ApplicationManager appMan = ApplicationManager.getApplicationManager();

        //grab the running applications
        ApplicationDescriptor[] appDes = appMan.getVisibleApplications();
        
        //check for the version of a standard RIM app. I like to use the ribbon
        //app but you can check the version of any RIM module they will all
        //be the same.
        final int size = appDes.length;
        for (int i = 0; i < size; i++){
            if ((appDes[i].getModuleName()).equals("net_rim_bb_ribbon_app")){
                return appDes[i].getVersion();
            }
        }
        return null;
    }
    
    
    private static String getNetworkString() {
        String networkID;
        try {
            int currentNetworkID = RadioInfo.getNetworkId(RadioInfo.getCurrentNetworkIndex());
            // Network Id representation of MCC and MNC (i.e. 0x0MNC0MCC).
            networkID = Integer.toHexString(currentNetworkID);
        } catch(Exception e) {
            networkID = "unknown";
        }
        
        String networkName;
        try {
            networkName = RadioInfo.getCurrentNetworkName();
        } catch(Exception e) {
            networkName = null;
        }
        if(networkName == null) {
            networkName = "unknown";
        }
        
        return " Network/" + networkName + '/' + networkID;
    }
    
    //-------------------------------------------------------------------------
    // Debug
    
    void printDebug() {
        System.out.println("*****************");
        System.out.println("Name: " + getTransportDebugName());
    }
}
