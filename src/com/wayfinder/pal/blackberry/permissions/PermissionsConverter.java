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
 * Interface intended for converting a permission from the com.wayfinder system
 * into permissions from the BlackBerry ApplicationPermissions system.
 * 
 * 
 */
interface PermissionsConverter {
    
    
    /**
     * Converts a permission from the com.wayfinder system into one or many
     * permissions from the BlackBerry system.
     * <p>
     * This method will never return null
     * 
     * @param palPermission One of the PERMISSION constants from 
     * com.wayfinder.permissions.PermissionsDetector
     * @return An array containing PERMISSION constants from
     * net.rim.device.api.applicationcontrol.ApplicationPermissions
     * 
     * @see com.wayfinder.permissions.PermissionsDetector
     * @see net.rim.device.api.applicationcontrol.ApplicationPermissions
     */
    int[] convertToApplicationPermissions(short palPermission);
    
    
    /**
     * Returns the name of a PERMISSION constant in 
     * net.rim.device.api.applicationcontrol.ApplicationPermissions
     * <p>
     * This method is only used for debugging
     * 
     * @param rimPermission A PERMISSION constant from
     * net.rim.device.api.applicationcontrol.ApplicationPermissions
     * @return The name of the constant as a String
     */
    String getRIMPermissionName(int rimPermission);

}
