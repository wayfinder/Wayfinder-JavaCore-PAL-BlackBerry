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
package com.wayfinder.pal.blackberry.permissions;


/**
 * This class contains convenience permissions.
 * 
 *
 */
public final class PALPermissions {
    
    // not a real class, only a collection of permissions constants.
    // This class is used to ensure that all the required permissions
    // are checked/requested, since using the constants from the
    // ApplicationPermissions class does not always work as intended.
    // Some systems may require more (and often not selfexplanatory)
    // permissions.
    
    private PALPermissions() {}
    
    
    // connection permissions
    
    /**
     * Allows access to systems that accquire satellite based positions.
     * <p>
     * Affected systems include (but may not be limited to):
     * <ul>
     * <li>Internal GPS via the javax.microedition.location package</li>
     * <li>Internal GPS via the net.rim.device.api.gps package</li>
     * </ul>
     */
    public static final short PERMISSION_LOCATION_INTERNAL_GPS = 10;
    
    
    /**
     * Allows access to systems that accquire network based positions.
     * <p>
     * Affected systems include (but may not be limited to):
     * <ul>
     * <li>Cell ID positioning via the javax.microedition.location package</li>
     * <li>Cell ID positioning via the net.rim.device.api.gps package</li>
     * </ul>
     */
    public static final short PERMISSION_LOCATION_NETWORK      = 11;
    
    
    /**
     * Allows the sending and receiving of messages using the
     * Short Message Service (SMS).
     * <p>
     * Affected systems include (but may not be limited to):
     * <p>
     * <ul>
     * <li>SMS connectivity methods in the javax.microedition.io package</li>
     * <li>SMS connectivity methods in the net.rim.blackberry.api.sms package</li>
     * </ul>
     */
    public static final short PERMISSION_SMS = 30;
    
    
    /**
     * Allows establishing of bluetooth connections via the Serial Port Profile
     * (SPP)
     * <p>
     * Affected systems include (but may not be limited to):
     * <p>
     * <ul>
     * <li>Bluetooth connectivity methods in the javax.bluetooth package</li>
     * <li>Bluetooth connectivity methods in the net.rim.device.api.bluetooth  package</li>
     * </ul>
     */
    public static final short PERMISSION_BLUETOOTH_SPP = 40;
    
    
    /**
     * Allows the ability to establish network connections using external 
     * network transports. External transports currently include:
     * <ul>
     * <li>The Blackberry Internet Service transport (also known as BIS-B)</li>
     * <li>TCP Cellular transport also known as Direct TCP (APN only)</li>
     * <li>The WIFI transport type</li>
     * <li>WAP 1.0 and WAP 1.1 transports</li>
     * <li>WAP 2.0</li>
     * </ul>
     * Affected systems include (but may not be limited to):
     * <p>
     * <ul>
     * <li>Network connectivity methods in the javax.microedition.io package</li>
     * <li>Network connectivity methods in the net.rim.device.api.io package</li>
     * <li>Network connectivity methods in the net.rim.device.api.io.http package</li>
     * </ul>
     * <p>
     */
    public static final short PERMISSION_CONNECTION_CARRIER  = 50;
    
    
    /**
     * Allows the ability to establish network connections using internal 
     * network transports. External transports currently include:
     * <ul>
     * <li>The Mobile Data Service (MDS) of the BlackBerry Enterprise Server
     * </ul>
     * Affected systems include (but may not be limited to):
     * <p>
     * <ul>
     * <li>Network connectivity methods in the javax.microedition.io package</li>
     * <li>Network connectivity methods in the net.rim.device.api.io package</li>
     * <li>Network connectivity methods in the net.rim.device.api.io.http package</li>
     * </ul>
     */
    public static final short PERMISSION_CONNECTION_COMPANY  = 51;
    
    
    // device permissions
    
    
    /**
     * Allows read/write access to files on the internal flash drive of the
     * BlackBerry device.
     * <p>
     * Affected systems include (but may not be limited to):
     * <p>
     * <ul>
     * <li>File handling in the javax.microedition.io.file package</li>
     * <li>File handling in the net.rim.device.api.io package</li>
     * <li>File handling in the net.rim.device.api.io.file package</li>
     * </ul>
     */
    public static final short PERMISSION_FILES_INTERNAL_STORAGE = 1000;
    
    
    /**
     * Allows read/write access to files on external SDCards.
     * <p>
     * Affected systems include (but may not be limited to):
     * <p>
     * <ul>
     * <li>File handling in the javax.microedition.io.file package</li>
     * <li>File handling in the net.rim.device.api.io package</li>
     * <li>File handling in the net.rim.device.api.io.file package</li>
     * </ul>
     */
    public static final short PERMISSION_FILES_EXTERNAL_STORAGE = 1001;


    /**
     * Allows the playback of audio and video.
     * <p>
     * Affected systems include (but may not be limited to):
     * <p>
     * <ul>
     * <li>Playback of media via the javax.microedition.media package</li>
     * <li>Playback of media via the net.rim.device.api.media package</li>
     * <li>Playback of media via the javax.microedition.amms package</li>
     * <li>Playback of media via the net.rim.plazmic.mediaengine package</li>
     * </ul>
     */
    public static final short PERMISSION_MEDIA_PLAY     = 1010;
    
    
    /**
     * Allows the recording of audio and video.
     * <p>
     * Affected systems include (but may not be limited to):
     * <p>
     * <ul>
     * <li>Recording of media via the javax.microedition.media package</li>
     * <li>Recording of media via the net.rim.device.api.media package</li>
     * </ul>
     */
    public static final short PERMISSION_MEDIA_RECORD   = 1011;
    
    
    /**
     * Allows the reading and writing of the organizer data on the data. This
     * includes:
     * <ul>
     * <li>Phone book</li>
     * <li>Calendar</li>
     * <li>Memos</li>
     * <li>ToDo list</li>
     * </ul>
     * Affected systems include (but may not be limited to):
     * <ul>
     * <li>Access to PIM data via the javax.microedition.pim package</li>
     * <li>Access to PIM data via the net.rim.blackberry.api.pdap package</li>
     * </ul>
     */
    public static final short PERMISSION_PIM_DATA       = 1020;
    
    
    /**
     * Allows the reading of information related to the carrier network
     * operation.
     * <p>
     * Affected systems include (but may not be limited to):
     * <li>Network information via the following classes:</li>
     * <ul>
     * <li>net.rim.device.api.system.RadioInfo</li>
     * <li>net.rim.device.api.system.GPRSInfo</li>
     * <li>net.rim.device.api.system.CDMAInfo</li>
     * <li>net.rim.device.api.system.IDENInfo</li>
     * </ul>
     * <li>Access to reading the IMSI number via the 
     * net.rim.device.api.system.SIMCardInfo API</li>
     */
    public static final short PERMISSION_INFO_NETWORK   = 1030;
    
    
    /**
     * This permission gives the application permission to use interprocess
     * communication (IPC), eg use methods and means to share information 
     * between several independent processes in the device.
     * <p>
     * Affected systems include (but may not be limited to):
     * <pre>
     * net.rim.device.api.system.RuntimeStore.get(long)
     * net.rim.device.api.system.RuntimeStore.put(long)
     * net.rim.device.api.system.Application.addGlobalEventListener()
     * net.rim.device.api.system.Application.removeGlobalEventListener()
     * net.rim.device.api.system.ApplicationManager.postGlobalEvent()
     * </pre>
     */
    public static final short PERMISSION_INTERPROCESS           = 1040;
    
    
    /**
     * Allows the application to manpulate it's own or other installations
     * on the device. This can for example be used by an installer application
     * to download and install other applications.
     * <p>
     * Affected systems include (but may not be limited to):
     * <ul>
     * <li>net.rim.device.api.system.CodeModuleGroup</li>
     * <li>net.rim.device.api.system.CodeModuleGroupManager</li>
     * <li>net.rim.device.api.system.CodeModuleManager</li>
     * </ul>
     */
    public static final short PERMISSION_CODEMODULE_MANAGEMENT  = 1050;
    
    
    /**
     * Allows an application to initiate and monitor phone calls. This also
     * gives the permission to inspect the phone logs on the device.
     * <p>
     * Affected systems include (but may not be limited to):
     * <ul>
     * <li>Phone handling via the net.rim.blackberry.api.phone package</li>
     * <li>Log inspection via the net.rim.blackberry.api.phone.phonelogs package</li>
     * </ul>
     */
    public static final short PERMISSION_PHONE_CALL  = 1060;
    
    
    /**
     * Allows an application to change the current settings in the device.
     */
    public static final short PERMISSION_CHANGE_DEVICE_SETTINGS = 1070;
    
    
}
