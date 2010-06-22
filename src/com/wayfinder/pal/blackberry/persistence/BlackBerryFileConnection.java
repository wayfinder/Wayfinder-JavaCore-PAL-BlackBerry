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
package com.wayfinder.pal.blackberry.persistence;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import com.wayfinder.pal.persistence.WFFileConnection;

public class BlackBerryFileConnection implements WFFileConnection {
	
	private FileConnection m_FileConnection = null;
	
	public BlackBerryFileConnection(String path) throws IOException {
		m_FileConnection = (FileConnection)Connector.open(path);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.WFFileConnection#close()
	 */
	public void close() throws IOException {
		if(m_FileConnection != null)
			m_FileConnection.close();		
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.WFFileConnection#delete()
	 */
	public boolean delete() {
		try {
			m_FileConnection.delete();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.WFFileConnection#exists()
	 */
	public boolean exists() {
		return m_FileConnection.exists();
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.WFFileConnection#fileSize()
	 */
	public int fileSize() {
		int size = -1;
		try {
			size = (int)m_FileConnection.fileSize();
		} catch (IOException e) {}
		return size;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.WFFileConnection#openDataInputStream()
	 */
	public DataInputStream openDataInputStream() throws IOException {
		return m_FileConnection.openDataInputStream();
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.WFFileConnection#openDataOutputStream()
	 */
	public DataOutputStream openDataOutputStream() throws IOException {
		if (!m_FileConnection.exists()) {
			m_FileConnection.create();
		}
		return m_FileConnection.openDataOutputStream();
	}
	
	public DataOutputStream openDataOutputStream(boolean append) throws IOException {
		if (!m_FileConnection.exists()) {
			m_FileConnection.create();
		}
		if (append) {
			return new DataOutputStream(
				m_FileConnection.openOutputStream(Integer.MAX_VALUE));
		} else {
			return m_FileConnection.openDataOutputStream();
		}
	}
}
