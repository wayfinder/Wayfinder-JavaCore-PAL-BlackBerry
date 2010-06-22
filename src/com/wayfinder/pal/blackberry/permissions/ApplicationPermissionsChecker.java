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
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;

final class ApplicationPermissionsChecker {

    private final PermissionsConverter m_permConverter;
    private final ApplicationPermissionsManager m_permMan;
    
    ApplicationPermissionsChecker(PermissionsConverter converter) {
        m_permConverter = converter;
        m_permMan = ApplicationPermissionsManager.getInstance();
    }
    
    
    boolean permissionsAllow(short palPermission) {
        int[] perms = m_permConverter.convertToApplicationPermissions(palPermission);
        for (int i = 0; i < perms.length; i++) {
            if(m_permMan.getPermission(perms[i]) != ApplicationPermissions.VALUE_ALLOW) {
                return false;
            }
        }
        return true;
    }
    
    
    boolean permissionsMayAllow(short palPermission) {
        int[] perms = m_permConverter.convertToApplicationPermissions(palPermission);
        for (int i = 0; i < perms.length; i++) {
            if(m_permMan.getPermission(perms[i]) == ApplicationPermissions.VALUE_DENY) {
                return false;
            }
        }
        return true;
    }
    
    
    void requestPermissions(int[] permArray) {
        ApplicationPermissions appPerms = new ApplicationPermissions();
        addRaisablePermissions(permArray, appPerms);
        // check to see if something actually needs to be requested
        int[] difference = appPerms.difference(m_permMan.getApplicationPermissions());
        if(difference.length > 0) {
            // something is not approved, pop the request
            m_permMan.invokePermissionsRequest(appPerms);
        }
    }
    
    
    int[] convertPALPermToRIMPerms(short palPerm) {
        return m_permConverter.convertToApplicationPermissions(palPerm);
    }
    
    
    
    private void addRaisablePermissions(int[] array, ApplicationPermissions perms) {
        for (int i = 0; i < array.length; i++) {
            final int perm = array[i];
            // we have to check if the permission is blocked, since the permissions
            // dialog will not do this before it's popped. However, we also have
            // to add all permissions regardless if they are already approved or
            // not. Failure to do so may result in an 'xor' handling of the
            // permissions (eg the requested permissions are approved, while the
            // the previously approved permissions are revoked again)
            if(!rimPermissionIsBlocked(perm)) {
                perms.addPermission(perm);
            }
        }
    }
    
    
    /**
     * Checks to see if the maximum level of the permission is locked to something
     * below VALUE_ALLOW
     * 
     * @param rimPermission The RIM permission to check
     * @return true if the permission is locked below VALUE_ALLOW
     */
    private boolean rimPermissionIsBlocked(int rimPermission) {
        return m_permMan.getMaxAllowable(rimPermission) != ApplicationPermissions.VALUE_ALLOW;
    }
    
}
