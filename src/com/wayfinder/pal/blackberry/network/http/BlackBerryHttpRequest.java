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
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.wayfinder.pal.blackberry.network.transport.TransportType;
import com.wayfinder.pal.network.http.HttpEntity;
import com.wayfinder.pal.network.http.HttpEntityEnclosingRequest;
import com.wayfinder.pal.network.http.HttpHost;
import com.wayfinder.pal.network.http.HttpResponse;

public class BlackBerryHttpRequest implements HttpEntityEnclosingRequest {
	
	private final String m_requestURI;
	private final Hashtable m_headers;
	private HttpEntity m_entity;
	private TransportType m_transportType;
	
	/**
	 * Constructs a BlackBerryHttpRequest with the specified uri and transport type
	 * 
	 * @param requestURI - the request uri
	 * @param transportType - the transport type
	 */
	public BlackBerryHttpRequest(String requestURI, TransportType transportType) {
		m_requestURI = requestURI;
		m_transportType = transportType;
		m_headers = new Hashtable();
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpRequest#getRequestMethod()
	 */
	public String getRequestMethod() {
		if (m_entity == null) {
            return "GET";
        }
        return "POST";
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpRequest#getRequestUri()
	 */
	public String getRequestUri() {
		return m_requestURI;
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpMessage#addHeader(java.lang.String, java.lang.String)
	 */
	public void addHeader(String name, String value) {
		m_headers.put(name, value);
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpMessage#containsHeader(java.lang.String)
	 */
	public boolean containsHeader(String name) {
		return m_headers.containsKey(name);
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpMessage#getHeader(java.lang.String)
	 */
	public String getHeader(String name) {
		return (String) m_headers.get(name);
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpMessage#setHeader(java.lang.String, java.lang.String)
	 */
	public void setHeader(String name, String value) {
		if (m_headers.containsKey(name)) {
			m_headers.remove(name);
		}
		addHeader(name, value);
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpEntityEnclosingRequest#getEntity()
	 */
	public HttpEntity getEntity() {
		return m_entity;
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpEntityEnclosingRequest#setEntity(com.wayfinder.pal.network.http.HttpEntity)
	 */
	public void setEntity(HttpEntity entity) {
		m_entity = entity;
	}
	
	/**
     * Services the request.
     * 
     * @param host The {@link HttpHost} for the target server
     * @return An {@link HttpResponse} with the reply from the server
     * @throws IOException if something went wrong during the network
     * communication
     */
    final HttpResponse handleConnection(HttpHost host) throws IOException {
        // open the connection, this will resolve the URL as well
        String url = host.toURI() + m_requestURI;
        // get transport for http
        url += m_transportType.getHTTPConnectionParameters();
        HttpConnection conn = (HttpConnection) Connector.open(url);

        // set method and headers
        conn.setRequestMethod(getRequestMethod());
        Enumeration keysEnum = m_headers.keys();
        while (keysEnum.hasMoreElements()) {
        	String key = (String) keysEnum.nextElement();
        	String value = (String) m_headers.get(key);
        	conn.setRequestProperty(key, value);
        }
        
        if (m_entity != null) {
            // if we have an entity attached, send that information
            // set the connection in output mode
            
            // send extra headers
            String encoding = m_entity.getContentEncoding();
            if (encoding != null) {
                conn.setRequestProperty("Content-Encoding", encoding);
            }
            String type = m_entity.getContentType();
            if (type != null) {
                conn.setRequestProperty("Content-Type", type);
            }
            
            OutputStream os = null;
            try {
                os = conn.openOutputStream();
                m_entity.writeTo(os);
            } finally {
                os.close();
            }
        }

        m_transportType.setHTTPHeaders(conn);
        conn.getResponseCode(); // trigger transaction
        
        return new BlackBerryHttpResponse(conn);
    }

}
