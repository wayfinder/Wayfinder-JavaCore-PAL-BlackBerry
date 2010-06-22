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
package com.wayfinder.pal.blackberry.network.transport;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import com.wayfinder.pal.blackberry.permissions.PALPermissions;

import net.rim.device.api.system.UnsupportedOperationException;


/**
 * This class contain all that is common for so called "external transports",
 * eg transports that are not part of the BES company network.
 * <p>
 * Almost all external transports are set up and maintained by the carrier 
 * employed by the company for access to the wireless network.
 * <p>
 * The BlackBerry will consider all these transports as "non-secure" (eg not
 * subject to encryption and inspection by the BES rules).
 *
 */
abstract class ExternalTransport extends TransportType {
	
    public String getSocketConnectionParameters() {
        if(supportSockets()) {
            return getHTTPConnectionParameters();
        }
        
        throw new UnsupportedOperationException("Sockets not supported by " + getTransportUserFriendlyName());
    }
    
    void appendSubHTTPHeaders(HttpConnection aConn) throws IOException {}
    
    void appendSubHTTPHeaders(Hashtable aHashTable) {}
    
    public short getRequiredPermission() {
        return PALPermissions.PERMISSION_CONNECTION_CARRIER;
    }
    
    public boolean requiresAlternateGZIPEncoding() {
        return false;
    }
    
    public byte getTransportTestConstant() {
        return TRANSPORT_CARRIER_CONFIG;
    }
}
