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

abstract class DirectTCP extends ExternalTransport {
    
    public final String getTransportDebugName() {
        return "TCP-".concat(getSubTransportDebugName());
    }
    
    
    /**
     * Retrieves the subtype for this DirectTCP connection. 
     * <p>
     * The identifier will be used for debugging/logging purposes
     * 
     * @return the subtype as a String
     */
    abstract String getSubTransportDebugName();

    
    public final String getHTTPConnectionParameters() {
        return ";deviceside=true".concat(getSubHTTPParameters());
    }
    
    /**
     * Retrieves the subparameters for this DirectTCP connection.
     * <p>
     * The implementing class can assume that the base tcp parameters will be
     * appended before the subparameters.
     * 
     * @return The subparameters as a string.
     */
    abstract String getSubHTTPParameters();

    
    public final boolean hasSufficientCoverage() {
        return tcpSupportedByOS() && isValidDirectTCPType();
    }
    
    
    private boolean tcpSupportedByOS() {
        // for OS 4.3.0 and lower, the cldc implementation was split into several
        // "optional" packages
        return  isModuleInstalledOnDevice("net_rim_cldc_io_tcp") 
        // for OS 4.5.0 and above, they where all merged into one
        // this should mean that all future berries supports DirectTCP
        || isModuleInstalledOnDevice("net_rim_cldc");
    }
    
    
    /**
     * A check to see if the device has sufficient coverage for this Direct TCP 
     * type to be used.
     * <p>
     * The following condition must be true before a device is considered 
     * to be in sufficient coverage for this Direct TCP type:
     * <ol>
     * <li>All service book records required for that transport type are 
     * routable.</li> 
     * </ol>
     * @return true if the device has sufficient coverage for this type to be
     * used
     */
    abstract boolean isValidDirectTCPType();

    public boolean supportSockets() {
        return true;
    }


}
