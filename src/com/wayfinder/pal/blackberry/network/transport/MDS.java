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

import net.rim.device.api.servicebook.ServiceRecord;

class MDS extends InternalTransport {

    MDS(ServiceRecord[] aRecordArray) {
        if(aRecordArray != null) {
            for(int i = 0; i < aRecordArray.length; i++) {
                ServiceRecord record = aRecordArray[i];
                if(isValidRecord(record) && record.isSecureService()) {
                    iRecord = record;
                    return;
                }
            }
        } else {
            iTakeAChance = true;
        }
    }
    
    public String getTransportUserFriendlyName() {
        return "Mobile Data Service";
    }
    
    public String getTransportDebugName() {
        return "MDS";
    }
    
    String getIdentifier() {
        return getTransportDebugName() + getRecordID(iRecord);
    }

    public String getHTTPConnectionParameters() {
        return ";deviceside=false";
    }

    public String getSocketConnectionParameters() {
        return getHTTPConnectionParameters().concat(";ConnectionTimeout=1200000");
    }
    
    private boolean iTakeAChance;
    private ServiceRecord iRecord;

    public boolean hasSufficientCoverage() {
        if(iRecord != null) {
            return isValidRecord(iRecord);
        }
        return iTakeAChance; // it's take a chance day!
    }

    public boolean supportSockets() {
        return true;
    }
    
    private static String RIM_MDS_TRANSCODER_HEADER = "x-rim-transcode-content";
    private static String RIM_MDS_TRANSCODER_VALUE_NONE = "none";
    
    void appendSubHTTPHeaders(HttpConnection aConn) throws IOException {
        // if there is an MDS in the other end, it will need the profile
        // rdf file to determine what the device is capable of. Without
        // this, the connection may fail.
        aConn.setRequestProperty(RIM_MDS_UAPROF_HEADER, RIM_MDS_UAPROF_VALUE);
        aConn.setRequestProperty(RIM_MDS_UAPROF_WAP_HEADER, RIM_MDS_UAPROF_WAP_VALUE);
        // finally, disable the transcoding components
        aConn.setRequestProperty(RIM_MDS_TRANSCODER_HEADER, RIM_MDS_TRANSCODER_VALUE_NONE);
    }
    
    void appendSubHTTPHeaders(Hashtable aHashTable) {
        // if there is an MDS in the other end, it will need the profile
        // rdf file to determine what the device is capable of. Without
        // this, the connection may fail.
        aHashTable.put(RIM_MDS_UAPROF_HEADER, RIM_MDS_UAPROF_VALUE);
        aHashTable.put(RIM_MDS_UAPROF_WAP_HEADER, RIM_MDS_UAPROF_WAP_VALUE); 
        // finally, disable the transcoding components
        aHashTable.put(RIM_MDS_TRANSCODER_HEADER, RIM_MDS_TRANSCODER_VALUE_NONE);
    }
    
    public boolean requiresAlternateGZIPEncoding() {
        return true;
    }

    public byte getTransportTestConstant() {
        return TRANSPORT_MDS;
    }

}
