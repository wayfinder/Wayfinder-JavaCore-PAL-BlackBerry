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

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

import net.rim.device.api.io.FileNotFoundException;

import com.wayfinder.pal.blackberry.permissions.PermissionsHandler;
import com.wayfinder.pal.persistence.PersistenceLayer;
import com.wayfinder.pal.persistence.SecondaryCacheStorage;
import com.wayfinder.pal.persistence.SettingsConnection;
import com.wayfinder.pal.persistence.WFFileConnection;

public class BlackBerryPersistenceLayer implements PersistenceLayer {
	
	//TODO: Add permissions handling!
	
	private String m_BaseFileDirectory = null;
//	private PermissionsHandler m_permissionsHandler;
	
	public BlackBerryPersistenceLayer(String basePath, PermissionsHandler permissionsHandler) {
		m_BaseFileDirectory = basePath;
//		m_permissionsHandler = permissionsHandler;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.PersistenceLayer#getBaseFileDirectory()
	 */
	public String getBaseFileDirectory() {
		return m_BaseFileDirectory;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.PersistenceLayer#getResourceAsStream(java.lang.String)
	 */
	public InputStream getResourceAsStream(String resource) throws IOException {
		InputStream in = getClass().getResourceAsStream("/"+resource);
        if (in == null) {
            throw new FileNotFoundException("BlackberryPersistenceLayer.getResourceAsStream() could not find "+resource);
        }
        return in;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.PersistenceLayer#listFiles(java.lang.String, java.lang.String)
	 */
	public String[] listFilesAllRoot(String folder, String extension) {
		Enumeration rootEnum = null;
        try {
            rootEnum = FileSystemRegistry.listRoots();
        } catch (SecurityException se) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Vector allFiles = new Vector();
        if (rootEnum != null) {
            while(rootEnum.hasMoreElements()){
                //scan each root:
                sb.setLength(0);
                sb.append("file:///");
                sb.append((String)rootEnum.nextElement());
                sb.append(folder);
                listFilesInDir(sb.toString(), "*"+extension, allFiles);
                
            }
        }
        String[] files = new String[allFiles.size()];
        allFiles.copyInto(files);
        return files;
	}
	
	public String[] listFiles(String folder, String extension) {
        Vector allFiles = new Vector();
        listFilesInDir(folder, "*"+extension, allFiles);
        String[] files = new String[allFiles.size()];
        allFiles.copyInto(files);
        return files;
	}

	
	private static int listFilesInDir(String dirPath, String filter, Vector results) {
        
        FileConnection fconn = null;
        int count = 0;
        try {
        	fconn = (FileConnection)Connector.open(dirPath, Connector.READ);
            if (fconn.isDirectory()) {
                Enumeration fenum = null;
                if (filter != null) fenum = fconn.list(filter, false);
                else fenum = fconn.list();
                //ArrayList filePaths = new ArrayList();
                StringBuffer sb = new StringBuffer();
                while (fenum.hasMoreElements()) {
                    sb.setLength(0);
                    sb.append(dirPath);
                    sb.append((String)fenum.nextElement());
                    results.addElement(sb.toString());
                    count++;
                }
                return count;
            }
        }
        catch (IOException ioe) {
        	System.out.println("BlackberryPersistenceLayer.listFilesInDir() error: "+ioe);
        }
        catch (SecurityException se) {
        	System.out.println("BlackberryPersistenceLayer.listFilesInDir() error: "+se);
        }
        catch (Exception e) {
        	System.out.println("BlackberryPersistenceLayer.listFilesInDir() error: "+e);
        }
        finally {
            if (fconn != null) {
                try {
                    fconn.close();
                } catch (IOException ioe) {
                	System.out.println("BlackberryPersistenceLayer.listFilesInDir() error while closing file connection "+ioe);
                }
            }
        }
        return -1;
    }

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.PersistenceLayer#openFile(java.lang.String)
	 */
	public WFFileConnection openFile(String path) throws IOException {
		return new BlackBerryFileConnection(path);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.PersistenceLayer#openSecondaryCacheStorage(java.lang.String)
	 */
	public SecondaryCacheStorage openSecondaryCacheStorage(String name) throws IOException {
		return new BlackBerrySecondaryCacheStorage(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.PersistenceLayer#openSettingsConnection(java.lang.String)
	 */
	public SettingsConnection openSettingsConnection(String settingsType) {		
		return new BlackBerrySettingsConnection(settingsType, this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.persistence.PersistenceLayer#setBaseFileDirectory(java.lang.String)
	 */
	public void setBaseFileDirectory(String path) {
		m_BaseFileDirectory = path;
	}
}
