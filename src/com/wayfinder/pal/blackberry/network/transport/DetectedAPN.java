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

import net.rim.device.api.util.StringUtilities;

abstract class DetectedAPN extends DirectTCP {
    
    private String iAPN;
    
    DetectedAPN(String anAPN) {
        if(anAPN != null) {
            iAPN = anAPN.trim();
        }
    }
    
    String getSubTransportDebugName() {
        return getDetectionDebugType() + "-APN-" + iAPN;
    }
    
    abstract String getDetectionDebugType();
    
    public String getTransportUserFriendlyName() {
        return iAPN;
    }
    
    String getIdentifier() {
        return "DetectedAPN" + iAPN;
    }
    
    String getSubHTTPParameters() {
        return ";apn=" + iAPN;
    }
    
    final boolean isValidDirectTCPType() {
        
        return iAPN != null && iAPN.length() > 0 && 
        !StringUtilities.strEqualIgnoreCase(iAPN, "none") && isValidAPN();
    }
    
    abstract boolean isValidAPN();
    
    String getAPN() {
        return iAPN;
    }
}
