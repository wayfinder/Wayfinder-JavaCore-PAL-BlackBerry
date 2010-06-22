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

import java.io.IOException;

import com.wayfinder.pal.blackberry.network.transport.TransportManager;
import com.wayfinder.pal.network.http.HttpClient;
import com.wayfinder.pal.network.http.HttpFactory;
import com.wayfinder.pal.network.http.HttpHost;
import com.wayfinder.pal.network.http.HttpRequest;
import com.wayfinder.pal.network.http.HttpResponse;
import com.wayfinder.pal.network.http.ResponseHandler;

public class BlackBerryHttpClient implements HttpClient {
	
	private final BlackBerryHttpFactory m_httpFactory;
    
	/**
	 * Constructs a BlackBerryHttpClient with the specified transport manager
	 * 
	 * @param transportManager - the transport manager
	 */
    public BlackBerryHttpClient(TransportManager transportManager) {
        m_httpFactory = new BlackBerryHttpFactory(transportManager);
    }

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpClient#execute(com.wayfinder.pal.network.http.HttpHost, 
	 * com.wayfinder.pal.network.http.HttpRequest)
	 */
	public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
		if (request instanceof BlackBerryHttpRequest) {
            return ((BlackBerryHttpRequest) request).handleConnection(target);
        }
        throw new ClassCastException("Foreign implementation of HttpRequest " +
        		"not allowed. Use HttpFactory to create a proper request");
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpClient#execute(com.wayfinder.pal.network.http.HttpHost, 
	 * com.wayfinder.pal.network.http.HttpRequest, com.wayfinder.pal.network.http.ResponseHandler)
	 */
	public Object execute(HttpHost target, HttpRequest request,
			ResponseHandler responseHandler) throws IOException {
		return responseHandler.handleResponse(execute(target, request));
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpClient#getHttpFactory()
	 */
	public HttpFactory getHttpFactory() {
		return m_httpFactory;
	}

}
