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
import net.rim.device.api.system.ControlledAccessException;
import net.rim.device.api.util.IntIntHashtable;
import net.rim.device.api.util.IntVector;

import com.wayfinder.pal.blackberry.BlackBerryPAL;
import com.wayfinder.pal.blackberry.debug.Logger;
import com.wayfinder.pal.error.PermissionsException;


public final class PermissionsHandler {
    
    static final boolean ACTIVATE_DEBUG = true;
    
    // this can cause problems if the bes admin tries to update the device
    // during operation. Chances of this are small though unless the admin
    // is suffering from repetitive change syndrome...
    // in case it turns out to be a problem, either disable this caching
    // or have this class listen to global events for updates to the
    // the permissions and policies
    private final IntIntHashtable m_permCache;
    
    private final PolicyChecker m_policyChecker;
    private final ApplicationPermissionsChecker m_appPermChecker;

    
    /**
     * Internal PAL constructor. This should not be invoked outside of the PAL.
     * <p>
     * To obtain this class, call {@link BlackBerryPAL#getPermissionsHandler()}
     * instead.
     */
    public PermissionsHandler(Logger log) {
        m_policyChecker = new PolicyChecker(log);
        m_appPermChecker = new ApplicationPermissionsChecker(new PermissionsConverterOS460());
        m_permCache = new IntIntHashtable();
    }
    

    /**
     * Assertion method.
     * <p>
     * Checks to see if the provided permission is allowed by both IT policies
     * and Application Permissions. 
     * <p>
     * This method will only consider a permission as allowed if it's explicitly 
     * set to "ALLOW" in the Application Permissions system.
     * 
     * @param permission One of the PERMISSION constants from {@link PALPermissions}
     * @throws SecurityException If the permission is not set as ALLOW
     */
    public void assertHasPALPermission(short permission) 
    throws PermissionsException {
        if(!m_permCache.containsKey(permission)) {
            if(!m_policyChecker.policiesAllow(permission)) {
                throw new ControlledAccessException("Disallowed by IT Policies");
            }
            
            if(!m_appPermChecker.permissionsAllow(permission)) {
                throw new ControlledAccessException("Blocked by Application Permissions");
            }
            m_permCache.put(permission, permission);
        }
    }
    
    
    /**
     * Checks to see if the provided permission is allowed by both IT policies
     * and Application Permissions. 
     * <p>
     * This method will only consider a permission as allowed if it's explicitly 
     * set to "ALLOW" in the Application Permissions system.
     * 
     * @param permission One of the PERMISSION constants from {@link PALPermissions}
     * @return true if and only if the permission is granted by the OS
     */
    public boolean checkHasPALPermission(short permission) {
        if(!m_permCache.containsKey(permission)) {
            if(!m_policyChecker.policiesAllow(permission)) {
                return false;
            }
            
            if(!m_appPermChecker.permissionsAllow(permission)) {
                return false;
            }
            m_permCache.put(permission, permission);
        }
        return true;
    }
    
    
    /**
     * Assertion method.
     * <p>
     * Checks to see if the provided permission may be allowed by both IT policies
     * and Application Permissions.
     * <p>
     * This method will consider a permission as allowed if it's not set to
     * DENY in the Application Permissions system. Eg the permission will be
     * considered allowed if the user will be queried when the system requiring
     * the permission is invoked.
     * 
     * @param permission One of the PERMISSION constants from {@link PALPermissions}
     * @throws SecurityException If the permission is disallowed
     */
    public void assertMAYHavePALPermission(short permission)
    throws PermissionsException {
        if(!m_permCache.containsKey(permission)) {
            if(!m_policyChecker.policiesAllow(permission)) {
                throw new ControlledAccessException("Disallowed by IT Policies");
            }
            
            if(!m_appPermChecker.permissionsMayAllow(permission)) {
                throw new ControlledAccessException("Blocked by Application Permissions");
            }
            // don't cache this value - it may change when the user has decided
        }
    }
    
    
    /**
     * Checks to see if the provided permission may be allowed by both IT policies
     * and Application Permissions.
     * <p>
     * This method will consider a permission as allowed if it's not set to
     * DENY in the Application Permissions system. Eg the permission will be
     * considered allowed if the user will be queried when the system requiring
     * the permission is invoked.
     * 
     * @param permission One of the PERMISSION constants from {@link PALPermissions}
     * @throws SecurityException If the permission is disallowed
     */
    public boolean checkMAYHavePALPermission(short permission) {
        if(!m_permCache.containsKey(permission)) {
            if(!m_policyChecker.policiesAllow(permission)) {
                return false;
            }
            
            if(!m_appPermChecker.permissionsMayAllow(permission)) {
                return false;
            }
            // don't cache this value - it may change when the user has decided
        }
        return true;
    }
    

    /**
     * Returns an array containing the application permissions required by the
     * PAL to function normally.
     * <p>
     * This method will only return the permissions that are not blocked by
     * IT Policies
     * 
     * @return An array with constants from {@link ApplicationPermissions}
     * @see ApplicationPermissions
     */
    public int[] getRequestableApplicationPermissions() {
        short[] palPerms = getInternalPALPermissions();
        IntVector vect = new IntVector(palPerms.length);
        for (int i = 0; i < palPerms.length; i++) {
            final short perm = palPerms[i];
            if(m_policyChecker.policiesAllow(perm)) {
                int[] bbPerms = m_appPermChecker.convertPALPermToRIMPerms(perm);
                for (int j = 0; j < bbPerms.length; j++) {
                    vect.addElement(bbPerms[j]);
                }
            }
        }
        return vect.toArray();
    }
    
    
    /**
     * Requests the PAL to assemble, check and request non-granted permissions
     * from the user.
     * <p>
     * If all permissions are already granted, this method will do nothing.
     */
    public void requestPALPermissions() {
        requestPermissions(new int[0], true);
    }
    
    
    /**
     * Requests the PAL to request the specified permissions from the user.
     * <p>
     * This method can be used to bundle permissions used by the PAL together
     * with the permissions used by the application to avoid two separate
     * requests.
     * <p>
     * If all permissions are already granted, this method will have no effect
     * 
     * @param permArray An array of constants from {@link ApplicationPermissions}
     * @param autoIncludePALPerms true if the PAL permissions should be included
     * in the request.
     */
    public void requestPermissions(int[] permArray, boolean autoIncludePALPerms) {
        int[] reqArray;
        if(autoIncludePALPerms) {
            int[] palPerms = getRequestableApplicationPermissions();
            reqArray = new int[permArray.length + palPerms.length];
            System.arraycopy(permArray, 0, reqArray, 0, permArray.length);
            System.arraycopy(palPerms, 0, reqArray, permArray.length,
                    palPerms.length);
            
        } else {
            reqArray = permArray;
        }
        m_appPermChecker.requestPermissions(reqArray);
    }
    

    private static short[] getInternalPALPermissions() {
        return new short[] {
                PALPermissions.PERMISSION_CONNECTION_CARRIER,
                PALPermissions.PERMISSION_FILES_INTERNAL_STORAGE,
                PALPermissions.PERMISSION_INFO_NETWORK,
                PALPermissions.PERMISSION_LOCATION_INTERNAL_GPS,
                PALPermissions.PERMISSION_LOCATION_NETWORK,
                PALPermissions.PERMISSION_MEDIA_PLAY
        };
    }
    
}
