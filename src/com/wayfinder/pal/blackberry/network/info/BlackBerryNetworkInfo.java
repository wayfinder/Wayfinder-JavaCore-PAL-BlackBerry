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
package com.wayfinder.pal.blackberry.network.info;

import com.wayfinder.pal.network.info.NetworkInfo;
import com.wayfinder.pal.network.info.TGPPInfo;

import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.WLANInfo;

public class BlackBerryNetworkInfo implements NetworkInfo {

    public TGPPInfo get3GPPInfo() throws IllegalStateException {
        if (getNetworkWAF() == WAF_3GPP) {
            return new BlackBerry3GPPInfo();
        }
        throw new IllegalStateException("Not a 3GPP network");
    }

    /* (non-Javadoc)
     * @see com.wayfinder.network.NetworkInfo#getNetworkWAF()
     */
    public int getNetworkWAF() {
        if (RadioInfo.getState() == RadioInfo.STATE_ON) {
            switch (RadioInfo.getNetworkType()) {
	            case RadioInfo.NETWORK_GPRS:
	            case RadioInfo.NETWORK_UMTS:
	                return WAF_3GPP;
	                
	            case RadioInfo.NETWORK_CDMA:
	                return WAF_CDMA;
	                
	            case RadioInfo.NETWORK_IDEN:
	                return WAF_iDEN;
	
	            default:
	                return WAF_UNKNOWN;
            }
        }
        return WAF_NONE;
    }
    

    public int getRoamingState() {
        if ((RadioInfo.getNetworkService() & RadioInfo.NETWORK_SERVICE_ROAMING) != 0) {
            return ROAMING_STATE_ROAMING;
        }
        return ROAMING_STATE_HOME;
    }
    

    public int getRadioState() {
        if (RadioInfo.getState() == RadioInfo.STATE_ON) {
            return RADIO_STATE_ON;
        }
        return RADIO_STATE_OFF;
    }
    

    public int getSignalStrength() {
        final int level = RadioInfo.getSignalLevel();
        if (level == RadioInfo.LEVEL_NO_COVERAGE) {
            return SIGNAL_STRENGTH_UNKNOWN;
        }
        return level;
    }

    /**
     * There is no a specific airplane mode for BB, what is possible
     * for the user is to disable all connections.
     * @return true if both mobile network and Wi-Fi are disabled  
     */
	public boolean isAirplaneMode() {
		return (RadioInfo.getState() == RadioInfo.STATE_OFF) && 
			   (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_DISCONNECTED);  
	}

}
