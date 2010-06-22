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
package com.wayfinder.pal.blackberry.network;

import com.wayfinder.pal.blackberry.network.http.BlackBerryHttpClient;
import com.wayfinder.pal.blackberry.network.info.BlackBerryNetworkInfo;
import com.wayfinder.pal.blackberry.network.transport.TransportManager;
import com.wayfinder.pal.network.NetworkLayer;
import com.wayfinder.pal.network.http.HttpClient;
import com.wayfinder.pal.network.info.NetworkInfo;

public class BlackBerryNetworkLayer implements NetworkLayer {
	
	private final BlackBerryHttpClient m_httpClient;
    private final BlackBerryNetworkInfo m_networkInfo;
    
    /**
     * Constructs a BlackBerryNetworkLayer with the specified transport manager
     * 
     * @param transportManager - the transport manager
     */
    public BlackBerryNetworkLayer(TransportManager transportManager) {
    	m_httpClient = new BlackBerryHttpClient(transportManager);
		m_networkInfo = new BlackBerryNetworkInfo();
    }

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.NetworkLayer#getHttpClient()
	 */
	public HttpClient getHttpClient() {
		return m_httpClient;
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.NetworkLayer#getNetworkInfo()
	 */
	public NetworkInfo getNetworkInfo() {
		return m_networkInfo;
	}

}
