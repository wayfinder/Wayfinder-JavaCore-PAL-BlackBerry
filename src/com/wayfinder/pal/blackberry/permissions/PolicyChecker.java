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

import com.wayfinder.pal.blackberry.debug.Logger;

import net.rim.device.api.itpolicy.ITPolicy;


/**
 * This class acts as a frontend for the internal IT Policy database in the
 * device
 * 
 */
final class PolicyChecker {
    
    private final Logger m_log;
    
    PolicyChecker(Logger log) {
        m_log = log;
    }
    
    /**
     * Does an internal lookup in the device IT Policy database to see if a
     * permission is allowed by IT policies.
     * <p>
     * Each permission can be affected by more than one IT Policy, both
     * directly and indirectly. This method will sum up all of them into a
     * response.
     * <p>
     * If this method returns false, there is no way the application or the
     * user can get the permission allowed on his or her own. The only person
     * who can allow it is the BES administrator.
     * 
     * @param palPermission One of the PERMISSION constants in
     * {@link PermissionsDetector}
     * @return true if the permission is not blocked by IT Policies
     */
    boolean policiesAllow(short palPermission) {
        final String fname;
        final String realName;
        if(PermissionsHandler.ACTIVATE_DEBUG) {
            fname = "PolicyChecker.policiesAllow() ";
            realName = "constant " + String.valueOf(palPermission);
            m_log.debug(fname + "checking ITPolicies for " + realName);
        }
        
        boolean response;
        switch(palPermission) {
        case PALPermissions.PERMISSION_LOCATION_INTERNAL_GPS:
            response = !checkBooleanPolicy(PolicyTable.GROUP_SECURITY_POLICY.GROUP_ID, 
                                           PolicyTable.GROUP_SECURITY_POLICY.DISABLE_GPS, false); 
            break;
            
        case PALPermissions.PERMISSION_BLUETOOTH_SPP:
            // bluetooth is counted as an external connection
            response = checkBooleanPolicy(PolicyTable.GROUP_SECURITY_POLICY.GROUP_ID, 
                                          PolicyTable.GROUP_SECURITY_POLICY.ALLOW_EXTERNAL_CONNECTIONS, true);

            // is bluetooth disabled?
            response &= !checkBooleanPolicy(PolicyTable.GROUP_BLUETOOTH.GROUP_ID, 
                                            PolicyTable.GROUP_BLUETOOTH.DISABLE_BLUETOOTH, false);
            
            // is the SPP active?
            response &= !checkBooleanPolicy(PolicyTable.GROUP_BLUETOOTH.GROUP_ID, 
                                            PolicyTable.GROUP_BLUETOOTH.DISABLE_SERIAL_PORT_PROFILE, false);
            break;
        
        case PALPermissions.PERMISSION_CONNECTION_COMPANY:
        case PALPermissions.PERMISSION_FILES_INTERNAL_STORAGE:
            response = checkBooleanPolicy(PolicyTable.GROUP_SECURITY_POLICY.GROUP_ID, 
                                          PolicyTable.GROUP_SECURITY_POLICY.ALLOW_INTERNAL_CONNECTIONS, true);
            break;

        case PALPermissions.PERMISSION_SMS:
            // is SMS allowed?
            response = checkBooleanPolicy(PolicyTable.STANDALONE.ALLOW_SMS, true);
            
            // SMS is counted as an external connection
            response = checkBooleanPolicy(PolicyTable.GROUP_SECURITY_POLICY.GROUP_ID, 
                                          PolicyTable.GROUP_SECURITY_POLICY.ALLOW_EXTERNAL_CONNECTIONS, true);

            // firewall not really needed anymore
            break;

        case PALPermissions.PERMISSION_CONNECTION_CARRIER:
            response = checkBooleanPolicy(PolicyTable.GROUP_SECURITY_POLICY.GROUP_ID, 
                                          PolicyTable.GROUP_SECURITY_POLICY.ALLOW_EXTERNAL_CONNECTIONS, true);
            break;
            
        case PALPermissions.PERMISSION_PHONE_CALL:
            response = checkBooleanPolicy(PolicyTable.STANDALONE.ALLOW_PHONE, true);
            break;
            
        default:
            if(PermissionsHandler.ACTIVATE_DEBUG) {
                m_log.debug(fname + "NO HANDLING OF " + realName);
            }
            response = true;
        }
        
        if(PermissionsHandler.ACTIVATE_DEBUG) {
            if(response) {
                m_log.debug(fname + "ITPolicies allow " + realName);
            } else {
                m_log.debug(fname + "ITPolicies DOES NOT ALLOW " + realName);
            }
        }
        
        return response;
    }
    
    
    private boolean checkBooleanPolicy(int group, int policy, boolean defaultValue) {
        if(PermissionsHandler.ACTIVATE_DEBUG) {
            m_log.debug("PolicyChecker.checkBooleanPolicy() checking group " 
                    + group + " policy " + policy);
        }
        return ITPolicy.getBoolean(group, policy, defaultValue);
    }
    
    
    private boolean checkBooleanPolicy(int policy, boolean defaultValue) {
        if(PermissionsHandler.ACTIVATE_DEBUG) {
            m_log.debug("PolicyChecker.checkBooleanPolicy() checking " +
        		"standalone " + policy);
        }
        
        return ITPolicy.getBoolean(policy, defaultValue);
    }
}
