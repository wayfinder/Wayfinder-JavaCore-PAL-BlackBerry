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
 * 
 */
class DummyTransport extends TransportType {

    void appendSubHTTPHeaders(HttpConnection aConn) throws IOException {
        throw new UnsupportedOperationException(getErrorMessage());
    }

    void appendSubHTTPHeaders(Hashtable aHashTable) {
        throw new UnsupportedOperationException(getErrorMessage());
    }
    
    public String getHTTPConnectionParameters() {
        throw new UnsupportedOperationException(getErrorMessage());
    }

    String getIdentifier() {
        return "DummyTransport";
    }

    public short getRequiredPermission() {
    	return PALPermissions.PERMISSION_CONNECTION_COMPANY;
    }
    
    public String getSocketConnectionParameters() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(getErrorMessage());
    }

    public String getTransportDebugName() {
        return "Invalid";
    }

    public byte getTransportTestConstant() {
        return TRANSPORT_CARRIER_CONFIG;
    }

    public String getTransportUserFriendlyName() {
        return "Invalid";
    }
    
    public boolean hasSufficientCoverage() {
        return false;
    }

    public boolean requiresAlternateGZIPEncoding() {
        return false;
    }
    
    public boolean supportSockets() {
        return false;
    }
    
    
    private static String getErrorMessage() {
        return "Functionallity not available in current transport type";
    }

}
