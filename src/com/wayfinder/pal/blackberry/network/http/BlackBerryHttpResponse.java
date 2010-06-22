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
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.HttpConnection;

import com.wayfinder.pal.network.http.HttpEntity;
import com.wayfinder.pal.network.http.HttpResponse;
import com.wayfinder.pal.network.http.StatusLine;

public class BlackBerryHttpResponse implements HttpResponse, HttpEntity, StatusLine {
	
	private final HttpConnection m_conn;
	private InputStream m_inStream;
	
	/**
	 * Constructs a BlackBerryHttpResponse with the specified http connection
	 * 
	 * @param conn - the http connection
	 */
	BlackBerryHttpResponse(HttpConnection conn) {
        m_conn = conn;
        m_inStream = null;
    }

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpResponse#getEntity()
	 */
	public HttpEntity getEntity() {
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpResponse#getStatusLine()
	 */
	public StatusLine getStatusLine() {
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpMessage#addHeader(java.lang.String, java.lang.String)
	 */
	public void addHeader(String name, String value) {
		// Ignored for response
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpMessage#containsHeader(java.lang.String)
	 */
	public boolean containsHeader(String name) {
		try {
			return m_conn.getHeaderField(name) != null;
		} catch (IOException e) {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpMessage#getHeader(java.lang.String)
	 */
	public String getHeader(String name) {
		try {
			return m_conn.getHeaderField(name);
		} catch (IOException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpMessage#setHeader(java.lang.String, java.lang.String)
	 */
	public void setHeader(String name, String value) {
		// Ignored for response
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpEntity#finish()
	 */
	public void finish() throws IOException {
		if (m_inStream != null) {
            m_inStream.close();
        }
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpEntity#getContent()
	 */
	public InputStream getContent() throws IllegalStateException, IOException {
		if (m_inStream != null) {
            throw new IllegalStateException("InputStream has already been opened");
        }
        return (m_inStream = m_conn.openInputStream());
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpEntity#getContentEncoding()
	 */
	public String getContentEncoding() {
		return m_conn.getEncoding();
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpEntity#getContentLength()
	 */
	public long getContentLength() {
		return m_conn.getLength();
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpEntity#getContentType()
	 */
	public String getContentType() {
		return m_conn.getType();
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpEntity#isChunked()
	 */
	public boolean isChunked() {
		try {
			return "chunked".equalsIgnoreCase(m_conn.getHeaderField("Transfer-Encoding"));
		} catch (IOException e) {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.HttpEntity#writeTo(java.io.OutputStream)
	 */
	public void writeTo(OutputStream outstream) throws IOException {
		throw new IOException("Request already handled");
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.StatusLine#getReasonPhrase()
	 */
	public String getReasonPhrase() {
		try {
            return m_conn.getResponseMessage();
        } catch (IOException e) {
            return null;
        }
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.network.http.StatusLine#getStatusCode()
	 */
	public int getStatusCode() {
		try {
            return m_conn.getResponseCode();
        } catch (IOException e) {
            return -1;
        }
	}

}
