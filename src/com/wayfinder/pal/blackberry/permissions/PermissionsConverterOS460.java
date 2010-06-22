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

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.system.CoverageInfo;

/**
 * This class handles the conversion between the permissions used in the
 * application and the native permissions on the BlackBerry smartphone. This
 * class handles the available permissions for version 4.6.0 of the device
 * software.
 * <p>
 * The following permissions are available:
 * 
 * <p><code>PERMISSION_AUTHENTICATOR_API</code><br>
 * Unknown</p>
 * 
 * <p><code>PERMISSION_APPLICATION_MANAGEMENT</code><br>
 * PALPermission to create, read and handle code modules as well as manage the 
 * installation or deletion of applications..</p>
 * 
 * <p><code>PERMISSION_BLUETOOTH</code><br>
 * This permission controls an application's ability to send and receive data 
 * using Bluetooth, as well as access to the supported Bluetooth profiles</p>
 * 
 * <p><code>PERMISSION_BROWSER_FILTER</code><br>
 * PALPermission to register browser filters in the handheld browser. Browser
 * filters allow the application to intercept, handle and alter URL:s going
 * to specific domains. (Like the websites of our competitors... hehehe)</p>
 * 
 * <p><code>PERMISSION_CROSS_APPLICATION_COMMUNICATION</code><br>
 * PALPermission for the application to communicate with other processes. This 
 * means going through either the <code>RuntimeStore</code>, the 
 * <code>GlobalEventListener</code> or the <code>StringPatternRepository. This
 * permission also affects the applications permissions to save things in the
 * PersistentStore.</code>.
 * </p>
 * 
 * <p><code>PERMISSION_DEVICE_SETTINGS</code><br>
 * PALPermission for the application to change the device settings like the 
 * Backlight.</p>
 * 
 * <p><code>PERMISSION_EMAIL</code><br>
 * PALPermission for the application to read and send emails.</p>
 * 
 * <p><code>PERMISSION_FILE_API</code><br>
 * PALPermission for the application to use the JSR-075 File Connection API</p>
 * 
 * <p><code>PERMISSION_IDLE_TIMER</code><br>
 * PALPermission to reset the backlight timer to keep the screen lit. Added in
 * PBr4.2.1_rel91_PL2.3.0.53_A4.2.1.59.</p>
 * 
 * <p><code>PERMISSION_INPUT_SIMULATION</code><br>
 * PALPermission for the application to inject events into the handheld OS. An
 * example of an event is a keypress or roll on the trackwheel/trackball. Note
 * that this permission is very sensitive and is almost always set to "deny".
 * Do not use this... it will scare the user. :)</p>
 * 
 * <p><code>PERMISSION_INTERNET</code><br>
 * PALPermission for the application to use external connections. Currently, these
 * are WAP connections, direct TCP connections, SMS connections and the
 * public MDS of the BlackBerry Internet Service.<br>
 * NOTE! Before OS 4.2, the public MDS of BIS was considered an internal
 * connection.</p>
 * 
 * <p><code>PERMISSION_LOCATION_DATA</code><br>
 * PALPermission for the application to use the JSR-179 Location API. This includes
 * both the positioning part and the landmark part.</p>
 * 
 * <p><code>PERMISSION_MEDIA</code><br>
 * This permission controls an application's ability to execute and manage 
 * multimedia files.</p>
 * 
 * <p><code>PERMISSION_ORGANIZER_DATA</code><br>
 * This permission controls an application's ability to access the PIM API and 
 * access to personal information management stores such as contacts, tasks and 
 * events.</p>
 * 
 * <p><code>PERMISSION_PHONE</code><br>
 * PALPermission for the application to initiate calls or monitor calls
 * in progress. Also allows access to phone logs</p>
 * 
 * <p><code>PERMISSION_RECORDING</code><br>
 * PALPermission for the application to take an automatic screenshot. This will
 * later be used for Lumberjack.</p>
 * 
 * <p><code>PERMISSION_SECURITY_DATA</code><br>
 * PALPermission to for the application to read crypto keys from the the keystore.
 * This is only needed when using direct commuication with company specific APIs
 * through the BES.</p>
 * 
 * <p><code>PERMISSION_SERVER_NETWORK</code><br>
 * PALPermission for the application to use internal connections. Currently, the
 * the private Mobile Data Service (MDS) of a BlackBerry Enterprise Server are 
 * considered internal connections.</p>
 * 
 * <p><code>PERMISSION_THEMES</code><br>
 * PALPermission for the application to read the theme data. This is mostly used
 * for GUI purposes.</p>
 * 
 * <p><code>PERMISSION_USB</code><br>
 * PALPermission for the application to use connections though the USB or serial
 * ports.</p>
 * 
 * <p><code>PERMISSION_WIFI</code><br>
 * PALPermission for the application to access Internet through the WiFi.</p>
 * 
 * This class breaks from the earlier conversions since RIM remade the 
 * permissionconstants in 4.6.0. The changes are:
 * <table border="1">
 * <tr><td><b>OS 4.2.1 - OS 4.5.0</b></td><td><b>OS 4.6.0</b></td></tr>
 * <tr><td>PERMISSION_CHANGE_DEVICE_SETTINGS</td><td>PERMISSION_DEVICE_SETTINGS</td></tr>
 * <tr><td>PERMISSION_CODE_MODULE_MANAGEMENT</td><td>PERMISSION_APPLICATION_MANAGEMENT</td></tr>
 * <tr><td>PERMISSION_EVENT_INJECTOR</td><td>PERMISSION_INPUT_SIMULATION</td></tr>
 * <tr><td>PERMISSION_EXTERNAL_CONNECTIONS</td><td>PERMISSION_INTERNET</td></tr>
 * <tr><td>PERMISSION_HANDHELD_KEYSTORE</td><td>PERMISSION_SECURITY_DATA</td></tr>
 * <tr><td>PERMISSION_INTER_PROCESS_COMMUNICATION </td><td>PERMISSION_CROSS_APPLICATION_COMMUNICATION</td></tr>
 * <tr><td>PERMISSION_INTERNAL_CONNECTIONS</td><td>PERMISSION_SERVER_NETWORK</td></tr>
 * <tr><td>PERMISSION_KEYSTORE_MEDIUM_SECURITY</td><td><b>REMOVED</b></td></tr>
 * <tr><td>PERMISSION_LOCAL_CONNECTIONS</td><td>PERMISSION_USB</td></tr>
 * <tr><td>PERMISSION_LOCATION_API</td><td>PERMISSION_LOCATION_DATA</td></tr>
 * <tr><td>PERMISSION_PIM</td><td>PERMISSION_ORGANIZER_DATA</td></tr>
 * <tr><td>PERMISSION_SCREEN_CAPTURE</td><td>PERMISSION_RECORDING</td></tr>
 * <tr><td>PERMISSION_THEME_DATA</td><td>PERMISSION_THEMES</td></tr>
 * </table>
 * <p>
 * 
 */
class PermissionsConverterOS460 implements PermissionsConverter {

    
    /* (non-Javadoc)
     * @see com.wayfinder.pal.blackberry.permissions.PermissionsConverter#convertToApplicationPermissions(short)
     */
    public int[] convertToApplicationPermissions(short palPermission) {
        switch(palPermission) {
        case PALPermissions.PERMISSION_LOCATION_INTERNAL_GPS:
        case PALPermissions.PERMISSION_LOCATION_NETWORK:
            return new int[] {
                    ApplicationPermissions.PERMISSION_LOCATION_DATA,
                    // the RIM implementation uses persistant storage to save
                    // some kind of information
                    ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION
            };
            
        case PALPermissions.PERMISSION_FILES_EXTERNAL_STORAGE:
        case PALPermissions.PERMISSION_FILES_INTERNAL_STORAGE:
            int[] value;
            // a check due to a flaw in the RIM OS. If a device does not have
            // MDS installed, PERMISSION_SERVER_NETWORK cannot be approved...
            if(CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS)) {
                value = new int[] { 
                        ApplicationPermissions.PERMISSION_FILE_API,
                        // if on bes, a file connection is considered to be
                        // an internal connection :/
                        ApplicationPermissions.PERMISSION_SERVER_NETWORK
                };
            } else {
                value = new int[] { 
                        ApplicationPermissions.PERMISSION_FILE_API,
                };
            }
            return value;
            
        case PALPermissions.PERMISSION_PIM_DATA:
            return new int[] { ApplicationPermissions.PERMISSION_ORGANIZER_DATA };
            
            
        case PALPermissions.PERMISSION_CONNECTION_CARRIER:
        case PALPermissions.PERMISSION_SMS:
            return new int[] { ApplicationPermissions.PERMISSION_INTERNET };
            
            
        case PALPermissions.PERMISSION_CONNECTION_COMPANY:
            return new int[] { 
                    ApplicationPermissions.PERMISSION_APPLICATION_MANAGEMENT,
                    ApplicationPermissions.PERMISSION_SERVER_NETWORK };
            
        case PALPermissions.PERMISSION_INTERPROCESS:
            return new int[] { ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION };
            
        case PALPermissions.PERMISSION_CODEMODULE_MANAGEMENT:
            return new int[] { ApplicationPermissions.PERMISSION_APPLICATION_MANAGEMENT };
            
        case PALPermissions.PERMISSION_BLUETOOTH_SPP:
            return new int[] { 
                    ApplicationPermissions.PERMISSION_INTERNET,
                    ApplicationPermissions.PERMISSION_BLUETOOTH 
            };
        
        case PALPermissions.PERMISSION_MEDIA_PLAY:
        case PALPermissions.PERMISSION_MEDIA_RECORD:
            return new int[] { ApplicationPermissions.PERMISSION_MEDIA };
            
        case PALPermissions.PERMISSION_CHANGE_DEVICE_SETTINGS:
            return new int[] { ApplicationPermissions.PERMISSION_DEVICE_SETTINGS };
            
        }
        
        return new int[] {};
    }

    
    /* (non-Javadoc)
     * @see com.wayfinder.pal.blackberry.permissions.PermissionsConverter#getRIMPermissionName(int)
     */
    public String getRIMPermissionName(int rimPermission) {
        switch(rimPermission) {
        case ApplicationPermissions.PERMISSION_AUTHENTICATOR_API:
            return "ApplicationPermissions.PERMISSION_AUTHENTICATOR_API";
            
        case ApplicationPermissions.PERMISSION_BLUETOOTH:
            return "ApplicationPermissions.PERMISSION_BLUETOOTH";
            
        case ApplicationPermissions.PERMISSION_BROWSER_FILTER:
            return "ApplicationPermissions.PERMISSION_BROWSER_FILTER";
            
        case ApplicationPermissions.PERMISSION_DEVICE_SETTINGS:
            return "ApplicationPermissions.PERMISSION_DEVICE_SETTINGS";
            
        case ApplicationPermissions.PERMISSION_APPLICATION_MANAGEMENT:
            return "ApplicationPermissions.PERMISSION_APPLICATION_MANAGEMENT";
            
        case ApplicationPermissions.PERMISSION_EMAIL:
            return "ApplicationPermissions.PERMISSION_EMAIL";
            
        case ApplicationPermissions.PERMISSION_INPUT_SIMULATION:
            return "ApplicationPermissions.PERMISSION_INPUT_SIMULATION";
            
        case ApplicationPermissions.PERMISSION_INTERNET:
            return "ApplicationPermissions.PERMISSION_INTERNET";
            
        case ApplicationPermissions.PERMISSION_FILE_API:
            return "ApplicationPermissions.PERMISSION_FILE_API";
            
        case ApplicationPermissions.PERMISSION_SECURITY_DATA:
            return "ApplicationPermissions.PERMISSION_SECURITY_DATA";
            
        case ApplicationPermissions.PERMISSION_IDLE_TIMER:
            return "ApplicationPermissions.PERMISSION_IDLE_TIMER";
            
        case ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION:
            return "ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION";
            
        case ApplicationPermissions.PERMISSION_SERVER_NETWORK:
            return "ApplicationPermissions.PERMISSION_SERVER_NETWORK";
            
        case ApplicationPermissions.PERMISSION_USB:
            return "ApplicationPermissions.PERMISSION_USB";
            
        case ApplicationPermissions.PERMISSION_LOCATION_DATA:
            return "ApplicationPermissions.PERMISSION_LOCATION_DATA";
            
        case ApplicationPermissions.PERMISSION_PHONE:
            return "ApplicationPermissions.PERMISSION_PHONE";
            
        case ApplicationPermissions.PERMISSION_ORGANIZER_DATA:
            return "ApplicationPermissions.PERMISSION_ORGANIZER_DATA";
            
        case ApplicationPermissions.PERMISSION_RECORDING:
            return "ApplicationPermissions.PERMISSION_RECORDING";
            
        case ApplicationPermissions.PERMISSION_THEMES:
            return "ApplicationPermissions.PERMISSION_THEMES";
            
        case ApplicationPermissions.PERMISSION_WIFI:
            return "ApplicationPermissions.PERMISSION_WIFI";
            
        case ApplicationPermissions.PERMISSION_MEDIA:
            return "ApplicationPermissions.PERMISSION_MEDIA";
        }
        return "UNKNOWN RIM PERMISSION";
    }
}
