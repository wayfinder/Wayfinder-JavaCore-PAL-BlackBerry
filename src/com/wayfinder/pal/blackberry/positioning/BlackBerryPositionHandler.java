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

import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

import com.wayfinder.pal.positioning.PositionProviderInterface;
import com.wayfinder.pal.positioning.UpdatesHandler;

/**
 *
 */
public class BlackBerryPositionHandler implements PositionProviderInterface,
        LocationListener {
    
    private final LocationProvider m_provider;
    private final int m_updateIntervalSeconds;
    private final int m_timeoutSeconds;
    private final int m_maxAgeSeconds;
    
    private UpdatesHandler m_updatesHandler;
    
    private Location m_lastLocation;

    public BlackBerryPositionHandler(
            LocationProvider provider, 
            int intervalSeconds, 
            int timeoutSeconds, 
            int maxAgeSeconds) {
        
        m_provider = provider;
        
        m_updateIntervalSeconds = intervalSeconds;
        m_timeoutSeconds = timeoutSeconds;
        m_maxAgeSeconds = maxAgeSeconds;
        
        m_updatesHandler = null;
    }

    /* (non-Javadoc)
     * @see com.wayfinder.pal.positioning.PositionProviderInterface#getType()
     */
    public int getType() {
        return PositionProviderInterface.TYPE_INTERNAL_GPS;
    }

    /* (non-Javadoc)
     * @see com.wayfinder.pal.positioning.PositionProviderInterface#resumeUpdates()
     */
    public void resumeUpdates() {
        m_provider.setLocationListener(this, 
                m_updateIntervalSeconds, m_timeoutSeconds, m_maxAgeSeconds);
    }

    /* (non-Javadoc)
     * @see com.wayfinder.pal.positioning.PositionProviderInterface#setUpdatesHandler(com.wayfinder.pal.positioning.UpdatesHandler)
     */
    public void setUpdatesHandler(UpdatesHandler coreHandler) {
        m_updatesHandler = coreHandler;
    }

    /* (non-Javadoc)
     * @see com.wayfinder.pal.positioning.PositionProviderInterface#stopUpdates()
     */
    public void stopUpdates() {
        m_provider.setLocationListener(null, -1, -1, -1);
    }

    /* (non-Javadoc)
     * @see javax.microedition.location.LocationListener#locationUpdated(javax.microedition.location.LocationProvider, javax.microedition.location.Location)
     */
    public void locationUpdated(LocationProvider provider, Location loc) {
        if (provider.getState() == LocationProvider.AVAILABLE
                && loc.isValid()) {
            if (m_updatesHandler != null) {
                m_updatesHandler.updateState(UpdatesHandler.PROVIDER_AVAILABLE);
            }
            QualifiedCoordinates qc = loc.getQualifiedCoordinates();
            float speed = UpdatesHandler.VALUE_UNDEF;
            float alt = UpdatesHandler.VALUE_UNDEF;
            int accuracy = UpdatesHandler.VALUE_UNDEF;
            float course = checkCourse(loc);
            
            if (!Float.isNaN(loc.getSpeed())) {
                speed = loc.getSpeed();
            }
            if (!Float.isNaN(qc.getAltitude())) { 
                alt = qc.getAltitude();
            }
            if (!Float.isNaN(qc.getHorizontalAccuracy())) { 
                accuracy = (int) qc.getHorizontalAccuracy();
            }
            
            if (m_updatesHandler != null) {
                m_updatesHandler.updatePosition(
                        qc.getLatitude(), 
                        qc.getLongitude(), 
                        speed, 
                        course, 
                        alt, 
                        accuracy, 
                        loc.getTimestamp());
            }
            
            m_lastLocation = loc;
        }
    }
    
    private float checkCourse(Location loc) {
        if (!Float.isNaN(loc.getCourse())) {
            return loc.getCourse();
        }
        else if (m_lastLocation != null) {
            return m_lastLocation.getQualifiedCoordinates().azimuthTo(loc.getQualifiedCoordinates());
        }
        else return UpdatesHandler.VALUE_UNDEF;
    }
    
    /* (non-Javadoc)
     * @see javax.microedition.location.LocationListener#providerStateChanged(javax.microedition.location.LocationProvider, int)
     */
    public void providerStateChanged(LocationProvider provider, int state) {
        int coreState = UpdatesHandler.PROVIDER_OUT_OF_SERVICE;
        
        switch (state) {
        case LocationProvider.AVAILABLE:
            coreState = UpdatesHandler.PROVIDER_AVAILABLE;
            break;
            
        case LocationProvider.OUT_OF_SERVICE:
            coreState = UpdatesHandler.PROVIDER_OUT_OF_SERVICE;
            break;
            
        case LocationProvider.TEMPORARILY_UNAVAILABLE:
            coreState = UpdatesHandler.PROVIDER_TEMPORARILY_UNAVAILABLE;
            resetProvider();
            break;
        }
        
        if (m_updatesHandler != null) {
            m_updatesHandler.updateState(coreState);
        }
    }
    
    private void resetProvider() {
        stopUpdates();
        m_provider.reset();
        resumeUpdates();
    }

}
