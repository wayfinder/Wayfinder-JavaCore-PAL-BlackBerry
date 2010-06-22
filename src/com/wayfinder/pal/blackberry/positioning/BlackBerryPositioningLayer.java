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
package com.wayfinder.pal.blackberry.positioning;

import javax.microedition.location.Criteria;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationProvider;

import com.wayfinder.pal.blackberry.permissions.PALPermissions;
import com.wayfinder.pal.blackberry.permissions.PermissionsHandler;
import com.wayfinder.pal.positioning.PositionProviderInterface;
import com.wayfinder.pal.positioning.PositioningLayer;

/**
 *
 */
public class BlackBerryPositioningLayer implements PositioningLayer {
    
    public static final int PROVIDER_DEFAULT = -1;
    
    public static final int GPS_UPDATE_INTERVAL_S = 1;  //in seconds
    
    public static final int GPS_TIMEOUT_S = 1;          //in seconds
    
    public static final int GPS_MAX_AGE_S = 1;          //in seconds
    
    private final PermissionsHandler m_permissions;
    
    private BlackBerryPositionHandler[] m_posProviders;
    
    private BlackBerryPositionHandler m_jsr179Handler;
    
    public BlackBerryPositioningLayer(PermissionsHandler perm) {
        m_permissions = perm;
        
        if (m_permissions.checkMAYHavePALPermission(
                PALPermissions.PERMISSION_LOCATION_INTERNAL_GPS)) {
            Criteria c = new Criteria();
            c.setHorizontalAccuracy(Criteria.NO_REQUIREMENT);
            c.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
            c.setSpeedAndCourseRequired(true);
            c.setAltitudeRequired(true); // to decide 3D fix
            c.setAddressInfoRequired(false); // RIM requires this to always be false
            
            try {
                LocationProvider provider = LocationProvider.getInstance(c);
                m_jsr179Handler = new BlackBerryPositionHandler(provider, 
                        GPS_UPDATE_INTERVAL_S, 
                        GPS_TIMEOUT_S, 
                        GPS_MAX_AGE_S);
                m_posProviders = new BlackBerryPositionHandler[] {m_jsr179Handler};
            } catch (LocationException e) {
                // All providers are out of service, so all location providers
                // are permanently unavailable. Permanent unavailability means 
                // that the method is unavailable and the implementation is not
                // able to expect that this situation would change in the near 
                // future.
                m_posProviders = new BlackBerryPositionHandler[0];
            }
        }
        else {
            // no permissions
            m_posProviders = new BlackBerryPositionHandler[0];
        }
    }

    /* (non-Javadoc)
     * @see com.wayfinder.pal.positioning.PositioningLayer#getPositionProviders()
     */
    public PositionProviderInterface[] getPositionProviders() {
        return m_posProviders;
    }

    /* (non-Javadoc)
     * @see com.wayfinder.pal.positioning.PositioningLayer#resumeUpdates()
     */
    public void resumeUpdates() {
        if (m_jsr179Handler != null) {
            m_jsr179Handler.resumeUpdates();
        }
    }

    /* (non-Javadoc)
     * @see com.wayfinder.pal.positioning.PositioningLayer#stopUpdates()
     */
    public void stopUpdates() {
        if (m_jsr179Handler != null) {
            m_jsr179Handler.stopUpdates();
        }
    }

}
