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

import com.wayfinder.pal.persistence.SettingsConnection;
import com.wayfinder.pal.persistence.WFFileConnection;

public class BlackBerrySettingsConnection implements SettingsConnection{
	
	private static final String SETTINGS_FOLDER_NAME = "settings/";
	private final String m_settingsPath;
	
	private int m_NextRecordNbr;
	private int m_CurrentUsedRecordID;
	private BlackBerryPersistenceLayer m_persistenceLayer;
	private String m_settingsType;
	
	public BlackBerrySettingsConnection(String settingsType, BlackBerryPersistenceLayer pLayer) {		
		m_settingsType = settingsType;
		m_persistenceLayer = pLayer;
		m_NextRecordNbr = -1;
		m_CurrentUsedRecordID = -2;
		m_settingsPath = m_persistenceLayer.getBaseFileDirectory()+SETTINGS_FOLDER_NAME;
	}

	public DataInputStream getDataInputStream(int recordId) throws IOException {
		WFFileConnection fc = m_persistenceLayer.openFile(m_settingsPath+m_settingsType+recordId);
		return fc.openDataInputStream();
	}

	public int getMaxSize() {
		return Integer.MAX_VALUE;
	}

	public int getNextRecordId() {
		
		if(m_NextRecordNbr == -1)
			loadNextRecordID();
		
		return m_NextRecordNbr;
	}

	public DataOutputStream getOutputStream(int recordId) throws IOException {
		m_CurrentUsedRecordID = recordId;
		WFFileConnection fc = m_persistenceLayer.openFile(m_settingsPath+m_settingsType+recordId);
		return fc.openDataOutputStream();
	}

	public void close() throws IOException {		
		if(m_CurrentUsedRecordID == m_NextRecordNbr) {
			WFFileConnection fc = m_persistenceLayer.openFile(m_settingsPath+m_settingsType+".info");
			DataOutputStream out = fc.openDataOutputStream();	
			m_NextRecordNbr++;
			out.writeInt(m_NextRecordNbr);
			out.close();
		}		
	}
	
	/*
	 * Load the next record ID from file 
	 */
	private void loadNextRecordID() {
		try {
			WFFileConnection fc = m_persistenceLayer.openFile(m_settingsPath+m_settingsType+".info");
			m_NextRecordNbr = 1;			
			if(fc.exists() && fc.fileSize() > 0) {
				DataInputStream in = fc.openDataInputStream();
				m_NextRecordNbr = in.readInt();
				in.close();
			}			
		} catch (Exception e) {
			m_NextRecordNbr = 1;
		}
	}
}
