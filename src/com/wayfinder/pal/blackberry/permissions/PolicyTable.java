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

import net.rim.device.api.itpolicy.ITPolicy;


/**
 * This is an index of the available IT policies that are of interest for our
 * PAL implementation.
 *
 * @version BES 5.0 beta 1
 */
final class PolicyTable {
    
    private PolicyTable() {}
    
    
    // the policies we are interested in
    // taken from itpolicytemplate2 database from BES 5.0
    
    
    /**
     * Standalone policies. These should not be retrieved using a group ID
     */
    static final class STANDALONE {
        
        /**
         * Specify whether or not the phone functionality on the BlackBerry 
         * device is available to the user.
         * <p>
         * Set this IT policy rule to False to prevent users from making any 
         * phone calls except emergency calls from their BlackBerry devices. 
         * The phone icon is still visible to users on their BlackBerry 
         * devices.
         * <p>
         * If you do not set this rule, a default value of True will be used.
         * @see ITPolicy#getBoolean(int, boolean)
         */
        static final int ALLOW_PHONE = 1;
        
        
        /**
         * Specify whether the user can use the BlackBerry Browser included on 
         * the BlackBerry device.
         * <p>
         * If you do not set this rule, a default value of Yes will be used
         * 
         * @see ITPolicy#getBoolean(int, boolean)
         */
        static final int ALLOW_BROWSER = 2;
        
        
        /**
         * Name of the IT Policy currently installed on the device
         * <p>
         * This can be used to determine if the device is provisioned with
         * IT Policies at all
         * 
         * @see ITPolicy#getString(int)
         */
        static final int ITPOLICY_NAME = 5;
        
        
        /**
         * Specify whether or not the BlackBerry device permits sending 
         * Short Message Service (SMS) messages (text messaging).
         * <p>
         * Set this rule to False to hide text messaging functionality on the 
         * BlackBerry device.
         * <p>
         * Note: To block incoming text, or SMS, messages, set the Firewall 
         * Block Incoming Messages IT policy rule in the Security Policy Group.
         * <p>
         * If you do not set this rule, a default value of True will be used.
         * 
         * @see ITPolicy#getBoolean(int, boolean)
         */
        static final int ALLOW_SMS = 15;
    }
    
    // grouped policies
    
    /**
     * Contains IT policy rules that apply to BlackBerry device security
     */
    static final class GROUP_SECURITY_POLICY {
        
        static final int GROUP_ID = 24;
        
        
        /**
         * Specify whether or not the BlackBerry device permits the user to 
         * download applications authored by third-parties (in other words, 
         * not authored by Research In Motion (RIM)).
         * <p>
         * If you do not set this rule, a default value of False will be used.
         * 
         * @see ITPolicy#getBoolean(int, int, boolean)
         */
        static final int DISALLOW_3RD_PARTY_DOWNLOAD = 11;
        
        
        /**
         * Specify whether or not applications can initiate internal connections 
         * (for example, to BlackBerry MDS Services) on the BlackBerry device.
         * <p>
         * If you do not set this rule, a default value of True will be used.
         * 
         * @see ITPolicy#getBoolean(int, int, boolean)
         */
        static final int ALLOW_INTERNAL_CONNECTIONS  = 19;
        
        
        /**
         * Specify whether or not applications can initiate external connections 
         * (for example, to WAP, SMS, or other public gateways) on the 
         * BlackBerry device.
         * <p>
         * If you do not set this rule, a default value of True will be used.
         * 
         * @see ITPolicy#getBoolean(int, int, boolean)
         */
        static final int ALLOW_EXTERNAL_CONNECTIONS  = 20;
        
        
        /**
         * Specify whether the GPS functionality on the BlackBerry device is 
         * disabled.
         * <p>
         * If you do not set this rule, a default value of No will be used
         * 
         * @see ITPolicy#getBoolean(int, int, boolean)
         */
        static final int DISABLE_GPS = 52;
        
        
        /**
         * Specify whether or not to disable the expandable memory (microSD) 
         * feature on applicable BlackBerry devices.
         * <p>
         * If you do not set this rule, a default value of False will be used.
         * <p>
         * @since Java-based BlackBerry devices version 4.2.0 and higher.
         * @see ITPolicy#getBoolean(int, int, boolean)
         */
        static final int DISABLE_EXTERNAL_MEMORY     = 58;
        
        
        /**
         * Specify whether to prevent the USB Mass Storage feature or the 
         * Media Transfer Protocol feature from working on supported BlackBerry 
         * devices.
         * <p>
         * If you set this IT policy rule to Yes, the BlackBerry device cannot 
         * use an external file system connected to the USB port. This means 
         * that the ability to transfer files to an external file system using 
         * the Media Manager with BlackBerry Desktop Manager Version 4.2.2 and 
         * 4.3 is turned off.
         * <p>
         * If you do not set this rule, a default value of No will be used
         * 
         * @since Java-based BlackBerry devices version 4.2.0 and higher.
         * @see ITPolicy#getBoolean(int, int, boolean)
         */
        static final int DISABLE_USB_MASS_STORAGE = 59;
        
        
        /**
         * Specify whether the BlackBerry will allow third party applications 
         * to reset the device's idle timer, bypassing the security timeout.
         * <p>
         * <b>If you do not set this rule, a default value of No will be used</b>
         * 
         * @see ITPolicy#getBoolean(int, int, boolean)
         */
        static final int ALLOW_RESET_IDLE_TIMER = 76;
        
    }
    
    
    /**
     * Contains IT policy rules that apply to SIM cards.
     */
    static final class GROUP_SIM_APP_TOOLKIT {
        
        static final int GROUP_ID = 31;
        
        
        /**
         * 
         * Specify whether to prevent the network or SIM from querying the 
         * BlackBerry device for certain location-related information.
         * <p>
         * Set to Yes to limit the query to current network and cell identities, 
         * the device IMEI, the date and time, and some measurement results.
         * <p>
         * If you do not set this rule, a default value of No will be used.
         * 
         * @see ITPolicy#getBoolean(int, int, boolean)
         */
        static final int DISABLE_NETWORK_LOCATION_QUERY = 2;
        
    }
    
    
    
    
    /**
     * Bluetooth Policy Group
     */
    static final class GROUP_BLUETOOTH {
        
        static final int GROUP_ID = 34;
        
        
        /**
         * Specify whether or not to turn off support for Bluetooth technology 
         * on the BlackBerry device.
         * <p>
         * If you do not set this rule, a default value of False will be used.
         * 
         * @see ITPolicy#getBoolean(int, int, boolean)
         */
        static final int DISABLE_BLUETOOTH = 1;
        
        
        /**
         * Specify whether or not a Bluetooth-enabled BlackBerry device can 
         * establish a relationship (in other words, pair) with another 
         * Bluetooth device.
         * <p>
         * Note: Set this rule to True to prevent the BlackBerry device user 
         * from pairing with subsequent Bluetooth devices after the BlackBerry 
         * device pairs with an approved Bluetooth device 
         * (for example a headset).
         * <p>
         * If you do not set this rule, a default value of False will be used.
         * 
         * @see ITPolicy#getBoolean(int, int, boolean)
         */
        static final int DISABLE_PAIRING   = 2;
        
        
        /**
         * Specify whether or not a Bluetooth-enabled BlackBerry device can use 
         * the Bluetooth Serial Port Profile (SPP) required to establish a 
         * serial connection between the BlackBerry device and a Bluetooth 
         * peripheral using a serial port interface.
         * <p>
         * If you do not set this rule, a default value of False will be used.
         * 
         * @see ITPolicy#getBoolean(int, int, boolean)
         */
        static final int DISABLE_SERIAL_PORT_PROFILE = 3;
    }

}
