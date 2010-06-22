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

import com.wayfinder.pal.network.info.NetworkException;
import com.wayfinder.pal.network.info.TGPPInfo;

import net.rim.device.api.system.ControlledAccessException;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.GPRSInfo.GPRSCellInfo;

class BlackBerry3GPPInfo implements TGPPInfo {
    
    private static final int NET_3GPP_HMCC   = 0;
    private static final int NET_3GPP_HMNC   = 1;
    private static final int NET_3GPP_CMCC   = 2;
    private static final int NET_3GPP_CMNC   = 3;
    private static final int NET_3GPP_LAC    = 4;
    private static final int NET_3GPP_CELLID = 5;
    
    private boolean iHazPermissions;
    
    /**
     * Constructs an instance of BlackBerry3GPPInfo
     */
    BlackBerry3GPPInfo() {
        try {
            GPRSInfo.getCellInfo().getCellId();
            iHazPermissions = true;
        } catch(ControlledAccessException cae) {
            iHazPermissions = false;
        } catch(Exception uso) {
            iHazPermissions = false;
        }
    }
    
    
    //-------------------------------------------------------------------------
    // check methods, everything is supported as long as WAF is 3GPP
    

    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#supportsCurrentMCC()
     */
    public boolean supportsCurrentMCC() {
        if (canExtractNetInfo()) {
            //XXX: Sometimes RadioInfo.getCurrentNetworkIndex() returns some invalid
            //value causing an IllegalArgumetException to be thrown when trying to
            //get the MCC (and probably the MNC too, but MCC is requested first and 
            //it fails at that point). Check if we get a reasonable network index
            //first.
            if (RadioInfo.getCurrentNetworkIndex() < 0) return false;

            //not sure about this check though, RadioInfo.getNumberOfNetworks() could
            //have the same problems as RadioInfo.getCurrentNetworkIndex()
            if (RadioInfo.getCurrentNetworkIndex() 
                    >= RadioInfo.getNumberOfNetworks()) return false;
            
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#supportsCurrentMNC()
     */
    public boolean supportsCurrentMNC() {
        if (canExtractNetInfo()) {
            if (RadioInfo.getCurrentNetworkIndex() < 0) {
            	return false;
            }
            if (RadioInfo.getCurrentNetworkIndex() 
                    >= RadioInfo.getNumberOfNetworks()) {
            	return false;
            }
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#supportsLAC()
     */
    public boolean supportsLAC() {
        return canExtractNetInfo();
    }
    
    
    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#supportsCellID()
     */
    public boolean supportsCellID() {
        return canExtractNetInfo();
    }
    

    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#supportsHomeMCC()
     */
    public boolean supportsHomeMCC() {
        return canExtractNetInfo();
    }


    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#supportsHomeMNC()
     */
    public boolean supportsHomeMNC() {
        return canExtractNetInfo();
    }
    
    private boolean canExtractNetInfo() {
        return iHazPermissions && is3GPPNetwork();
    }
    
    
    //-------------------------------------------------------------------------
    // extraction of information methods
    
    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#getCurrentMCC()
     */
    public String getCurrentMCC() {
        return getValue(NET_3GPP_CMCC);
    }
    
    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#getCurrentMNC()
     */
    public String getCurrentMNC() {
        return getValue(NET_3GPP_CMNC);
    }
    
    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#getLAC()
     */
    public String getLAC() {
        return getValue(NET_3GPP_LAC);
    }
    
    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#getCellID()
     */
    public String getCellID() {
        return getValue(NET_3GPP_CELLID);
    }


    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#getHomeMCC()
     */
    public String getHomeMCC() throws NetworkException {
        return getValue(NET_3GPP_HMCC);
    }
    
    
    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#getHomeMNC()
     */
    public String getHomeMNC() throws NetworkException {
        return getValue(NET_3GPP_HMNC);
    }
    
    
    private String getValue(int aType) {
        if(canExtractNetInfo()) {
            try {
                GPRSCellInfo cellInfo = GPRSInfo.getCellInfo();
                switch(aType) {
                case NET_3GPP_CMCC:
                    // since GPRSCellInfo.getMNC() is broken we might as well use the
                    // same methods for both values >_<
                    //return Integer.toHexString(getCellInfo().getMCC());
                    return Integer.toHexString(RadioInfo.getMCC(RadioInfo.getCurrentNetworkIndex()));

                case NET_3GPP_CMNC:
                    // GPRSCellInfo.getMNC() is broken, it returns a value that is way
                    // higher than what it should be. It seems possible to use the getMNC
                    // method in RadioInfo though =/
                    //return Integer.toHexString(getCellInfo().getMNC());
                    return Integer.toHexString(RadioInfo.getMNC(RadioInfo.getCurrentNetworkIndex()));

                case NET_3GPP_LAC:
                    return Integer.toHexString(cellInfo.getLAC());

                case NET_3GPP_CELLID:
                    int cell = cellInfo.getCellId();
                    if(isUMTS()) {
                        // in a 3G network the BSIC is considered to be part
                        // of the cell id. RIM decided to not do this however...
                        cell |= (cellInfo.getBSIC() << 16);
                    }
                    return Integer.toHexString(cell);

                case NET_3GPP_HMCC:
                    return Integer.toHexString(GPRSInfo.getHomeMCC());

                case NET_3GPP_HMNC:
                    return Integer.toHexString(GPRSInfo.getHomeMNC());
                    
                default:
                    throw new IllegalArgumentException("Unsupported network variable");
                }
            } catch(ControlledAccessException cae) {
                if (iHazPermissions) {
                    iHazPermissions = false;
                }
                return "";
            }
        }
        throw new NetworkException("Not a supported variable on this platform");
    }
    

    /* (non-Javadoc)
     * @see com.wayfinder.pal.network.info.TGPPInfo#getNetworkType()
     */
    public int getNetworkType() {
        if(is3GPPNetwork()) {
            if(isUMTS()) {
                return TYPE_3GPP_UMTS;
            }
            return TYPE_3GPP_GPRS;
        }
        return TYPE_3GPP_UNKNOWN;
    }
    
    
    
    //-------------------------------------------------------------------------
    // Help methods
    
    
    private static boolean is3GPPNetwork() {
        switch(RadioInfo.getNetworkType()) {
        case RadioInfo.NETWORK_GPRS:
        case RadioInfo.NETWORK_UMTS:
            return true;
        }
        return false;
    }
    
    
    private static boolean isUMTS() {
        return (RadioInfo.getNetworkService() & RadioInfo.NETWORK_SERVICE_UMTS) != 0;
    }
    
}
