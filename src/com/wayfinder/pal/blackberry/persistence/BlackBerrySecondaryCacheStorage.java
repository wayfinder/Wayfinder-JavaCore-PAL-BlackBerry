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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import com.wayfinder.pal.persistence.SecondaryCacheStorage;

public class BlackBerrySecondaryCacheStorage implements SecondaryCacheStorage {

	private RecordStore m_recordStore;
	private String m_name;
	
	public BlackBerrySecondaryCacheStorage(String name) throws IOException {
		m_name = name;
		try {
			m_recordStore = RecordStore.openRecordStore(name, true);
		} catch (RecordStoreException e) {			
			throw new IOException("Unable to open NAME: "+name);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.SecondaryCacheStorage#close()
	 */
	public boolean close() {
		try {
			m_recordStore.closeRecordStore();
		} catch (RecordStoreException e) {
			return false;
		}		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.SecondaryCacheStorage#delete()
	 */
	public boolean delete() {
		try {
			m_recordStore.closeRecordStore();
			RecordStore.deleteRecordStore(m_name);
		} catch (RecordStoreException e) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.SecondaryCacheStorage#getDataInputStream()
	 */
	public DataInputStream getDataInputStream() throws IOException {		
		try {
			if(m_recordStore.getNextRecordID() > 1) {
				byte []data = m_recordStore.getRecord(1);
				ByteArrayInputStream bin = new ByteArrayInputStream(data);
				return new DataInputStream(bin);
			}
		} catch(RecordStoreException e) {
			throw new IOException("Unable to read from "+m_name);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.SecondaryCacheStorage#getMaxPageSize()
	 */
	public int getMaxPageSize() {
		return 64000;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.SecondaryCacheStorage#size()
	 */
	public int size() {		
		int size = 0;
		try {
			if(m_recordStore.getNextRecordID() > 1)			
				size = m_recordStore.getRecordSize(1);
		} catch (RecordStoreException e) {}		
		
		return size;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.SecondaryCacheStorage#writeToStorage(byte[], int, int)
	 */
	public boolean writeToStorage(byte[] data, int offset, int length) throws IOException {
		try {
			if(m_recordStore.getNextRecordID() > 1) {
				m_recordStore.setRecord(1, data, offset, length);
			} else {
				m_recordStore.addRecord(data, offset, length);
			}
		} catch(RecordStoreException e) {
			throw new IOException("Unable to write to "+m_name);
		}		
		return true;
	}

}
