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
package com.wayfinder.pal.blackberry.network.http;

import com.wayfinder.pal.network.http.HttpHost;

public class BlackBerryHttpHost implements HttpHost {
	
	private final String m_scheme;
	private final String m_hostName;
	private final int m_port;
	
	/**
	 * Constructs a HttpPost with the specified host name and port
	 * 
	 * @param hostName - the host name
	 * @param port - the port
	 */
	BlackBerryHttpHost(String hostName, int port) {
		m_scheme = "http";
        m_hostName = hostName;
        m_port = port;
    }

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpHost#getHostName()
	 */
	public String getHostName() {
		return m_hostName;
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpHost#getPort()
	 */
	public int getPort() {
		return m_port;
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpHost#getSchemeName()
	 */
	public String getSchemeName() {
		return m_scheme;
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpHost#toURI()
	 */
	public String toURI() {
		return m_scheme + "://" + m_hostName + ":" + m_port;
	}

}
