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
package com.wayfinder.pal.blackberry;

import com.wayfinder.pal.PAL;
import com.wayfinder.pal.blackberry.concurrency.BlackBerryConcurrencyLayer;
import com.wayfinder.pal.blackberry.debug.Logger;
import com.wayfinder.pal.blackberry.hardwareinfo.BlackBerryHardwareInfo;
import com.wayfinder.pal.blackberry.network.BlackBerryNetworkLayer;
import com.wayfinder.pal.blackberry.network.transport.BlackBerryTransportInterface;
import com.wayfinder.pal.blackberry.network.transport.TransportManager;
import com.wayfinder.pal.blackberry.permissions.PermissionsHandler;
import com.wayfinder.pal.blackberry.persistence.BlackBerryPersistenceLayer;
import com.wayfinder.pal.blackberry.positioning.BlackBerryPositioningLayer;
import com.wayfinder.pal.blackberry.softwareinfo.BlackBerrySoftwareInfo;
import com.wayfinder.pal.blackberry.sound.BlackBerrySoundLayer;
import com.wayfinder.pal.blackberry.util.BlackBerryUtilFactory;
import com.wayfinder.pal.concurrency.ConcurrencyLayer;
import com.wayfinder.pal.debug.LogHandler;
import com.wayfinder.pal.hardwareinfo.HardwareInfo;
import com.wayfinder.pal.network.NetworkLayer;
import com.wayfinder.pal.persistence.PersistenceLayer;
import com.wayfinder.pal.positioning.PositioningLayer;
import com.wayfinder.pal.softwareinfo.SoftwareInfo;
import com.wayfinder.pal.sound.SoundLayer;
import com.wayfinder.pal.util.UtilFactory;

public final class BlackBerryPAL extends PAL {
    
    private final PermissionsHandler m_permHandler;
    private final TransportManager m_transportManager;

    private BlackBerryPAL(
            LogHandler loghandler, 
            ConcurrencyLayer cInfo,
            NetworkLayer netLayer, 
            HardwareInfo hardwareInfo,
            SoftwareInfo softInfo, 
            PersistenceLayer perLayer,
            UtilFactory utilFactory, 
            SoundLayer soundLayer, 
            PermissionsHandler permHandler,
            PositioningLayer posLayer,
            TransportManager transportManager) {
        super(loghandler, cInfo, netLayer, hardwareInfo, softInfo, perLayer,
                utilFactory, soundLayer, posLayer);
        m_permHandler = permHandler;
        m_transportManager = transportManager;
    }
    
    
    /**
     * Creates an instance of the BlackBerryPAL.
     * <p>
     * 
     * @param guid a unique long that identifies the application
     * @param basePath a path to a folder on the phone or the memory card from where files 
     * should be saved to and loaded from. 
     * @return An instance of {@link BlackBerryPAL}
     */
    public static BlackBerryPAL createBlackBerryPAL(long guid, String basePath) {
        Logger logger = new Logger(guid);
        PermissionsHandler permHandler = new PermissionsHandler(logger);
        TransportManager transportManager = new TransportManager(permHandler);
        
        // fill in the blanks below. Don't forget to check permissions in the
        // PAL implementations!
        
        return new BlackBerryPAL(
                logger, 
                new BlackBerryConcurrencyLayer(),
                new BlackBerryNetworkLayer(transportManager),
                new BlackBerryHardwareInfo(permHandler),
                new BlackBerrySoftwareInfo(),
                new BlackBerryPersistenceLayer(basePath, permHandler),
                new BlackBerryUtilFactory(),
                new BlackBerrySoundLayer(),
                permHandler,
                new BlackBerryPositioningLayer(permHandler),
                transportManager
                );
    }
    

    /* (non-Javadoc)
     * @see com.wayfinder.pal.PAL#requestGC()
     */
    public void requestGC() {
        // never do GC on BlackBerry since it will pause the JVM
        // for up to 20 seconds
    }
    
    
    //-------------------------------------------------------------------------
    // BB PAL only
    
    /**
     * Returns the {@link PermissionsHandler}
     * <p>
     * This is intended to be used as a quick way of determining if a system
     * is blocked by either IT Policies or Application Permissions
     * 
     * @return The {@link PermissionsHandler}
     */
    public PermissionsHandler getPermissionsHandler() {
        return m_permHandler;
    }
    
    /**
     * Return the {@link BlackBerryTransportInterface}. 
     * 
     * @return
     */
    public BlackBerryTransportInterface getTransportManager() {
        return m_transportManager;
    }

}
