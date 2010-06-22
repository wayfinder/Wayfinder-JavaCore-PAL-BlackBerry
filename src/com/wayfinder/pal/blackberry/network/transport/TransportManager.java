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

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import com.wayfinder.pal.blackberry.permissions.PermissionsHandler;

import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.Branding;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.RadioException;
import net.rim.device.api.system.RadioInfo;

public class TransportManager implements BlackBerryTransportInterface {

    public static final int TRANSPORT_HTTP      = 0;
    public static final int TRANSPORT_SOCKETS   = 1;
	
	private PermissionsHandler m_PermissionsHandler;
    private TransportType[] m_AllPossibleTypes;
	
	
    public TransportManager(PermissionsHandler permissionsHandler) {
    	m_PermissionsHandler = permissionsHandler;        
    }
    
    /*
     * Not exposed in the first version of the Blackberry transport
     * system. 
     */
    TransportType[] getTypesSupportedByDevice() {
    	m_AllPossibleTypes = createSupportedTypesList();
    	
        if(m_AllPossibleTypes == null) {
            return new TransportType[0];
        }
        TransportType[] returnArray = new TransportType[m_AllPossibleTypes.length];
        System.arraycopy(m_AllPossibleTypes, 0, returnArray, 0, m_AllPossibleTypes.length);
        return returnArray;
    }
    
    // ---------------------------------------------------------
    
    public void setTransportType(int type) throws IllegalArgumentException, SecurityException {    	
    	m_transportType = intenalSetTransportType(type);    	
    	
    	if(m_transportType == null)
    		throw new IllegalArgumentException("Transport type not supported"); 
    	else if(m_PermissionsHandler.checkMAYHavePALPermission(m_transportType.getRequiredPermission())) {
    	    throw new SecurityException("Not allowed to use specified transport");
    	}
    }
    
    public TransportType intenalSetTransportType(int type) {
    	
    	TransportType transportType = null;
    	
    	ServiceBook sb; 
        try {
            sb = ServiceBook.getSB();
        } catch(Exception e) {
            sb = null;
        }
        
    	switch(type) {
    		case TRANSPORT_TYPE_WAP2:
    			transportType = createTransportTypeWap2(sb);
    			break;
    			
    		case TRANSPORT_TYPE_DIRECT:
    		    transportType = createTransportTypeDirectTCP();
    		    break;
    			
			default:
				throw new IllegalArgumentException("Not a valid transport type");
    	}
    	
    	return transportType;
    }

    
    private TransportType createTransportTypeWap2(ServiceBook sb) {
		// WPTCP is WAP2 gateways provided by the
        // carriers. While we are at it, we can also try to use these
        // records to do a Direct TCP connection. The WPTCP service book
        // does not support sockets, but the underlying APN may do so
        // grab the record from the browser, it's usually correct
        ServiceRecord sr = BrowserUtils.getTransportForBrowserType(BrowserUtils.BROWSER_WAP);
        if(sr != null && sr.getCid().equalsIgnoreCase("WPTCP")) {
        	// found the book from the browser
        	return new WPTCP(sr);
        } else if(sb != null) {    	        	
        	ServiceRecord[] recordsWPTCP = sb.findRecordsByCid( "WPTCP" );
            if(recordsWPTCP != null) {
	        	for(int i=0; i < recordsWPTCP.length; i++){
	        	    //Search through all service records to find the valid non-Wi-Fi and non-MMS
	        	    //WAP 2.0 Gateway Service Record.
	        	    if (recordsWPTCP[i].isValid() && !recordsWPTCP[i].isDisabled()) {	
	        	        if (recordsWPTCP[i].getUid() != null && recordsWPTCP[i].getUid().length() != 0) {
	        	            if ((recordsWPTCP[i].getUid().toLowerCase().indexOf("wifi") == -1) &&
	        	                (recordsWPTCP[i].getUid().toLowerCase().indexOf("mms") == -1)) {
	        	            		return new WPTCP(recordsWPTCP[i]);
	        	            }
	        	        }
	        	    }
	        	}
            }    	        	      	
        }
        throw new IllegalArgumentException("WAP 2 not supported on this device");
    }
    
    
    private TransportType createTransportTypeDirectTCP() {
        return new SettingsAPN();
    }
    
    
    public TransportType getCurrentTransportType() {
    	return m_transportType;
    }
    
    //-------------------------------------------------------------------------
    // static stuff
    
    /**
     * Creates a list of all available connection types on this device.
     * 
     * @return
     */
    private static TransportType[] createSupportedTypesList() {
        ServiceBook sb; 
        try {
            sb = ServiceBook.getSB();
        } catch(Exception e) {
            sb = null;
        }
        
        Vector v = new Vector();
        Hashtable hash = new Hashtable();
        // to avoid picking up the RIM APN ("blackberry.net"). This is not a
        // real APN, more like a symbolic link
        String bbApnIdent = new DetectedNetworkAPN(BLACKBERRY_APN).getIdentifier();
        hash.put(bbApnIdent, bbApnIdent);
        
        
        if(DeviceInfo.isSimulator()) {
            addStaticAPNs(v, hash);
            insertSingleType(v, hash, new MDS(null));
        
        } else if(sb != null && !sb.getSerialInjectionInProgress()) {
            
            // The IPPP service represents the data channel for MDS
            ServiceRecord[] recordsIPPP = sb.findRecordsByCid( "IPPP" );
            if(recordsIPPP != null) {
                // Ok, new rules. We start off by inserting the MDS.
                // There are no guarantees that it will work, but it's local
                // and is included in the user's subscription.
                insertSingleType(v, hash, new MDS(recordsIPPP));
            }
            
            // the APN set by the user in TCP settings and the branded main apn
            addStaticAPNs(v, hash);
            
            // Carrier APN's detected in the WAP 2 service books
            ServiceRecord[] recordsWPTCP = sb.findRecordsByCid( "WPTCP" );
            addRecordAPNs(v, hash, recordsWPTCP);
            
            // Carrier APN's detected in the WAP 1 service books
            ServiceRecord[] recordsWAP = sb.findRecordsByCid( "WAP" );
            addRecordAPNs(v, hash, recordsWAP);
            
            // APNs detected in the network
            addNetworkAPNs(v, hash);
            
            // let's do WPTCP next. WPTCP is WAP2 gateways provided by the
            // carriers. While we are at it, we can also try to use these
            // records to do a Direct TCP connection. The WPTCP service book
            // does not support sockets, but the underlying APN may do so
            addWPTCPTypes(v, hash, sb);
            
        } else if (isBlackBerryAPNConnected()) {
            // we can't inspect the actual conditions on the device, so we
            // have to take a chance. We just add the three most commonly used
            // transports (MDS, APN)
            insertSingleType(v, hash, new MDS(null));
            addStaticAPNs(v, hash);
            addNetworkAPNs(v, hash);
        } else {
            // no blackberry network active. Just use the settings APN
            // and whatever we find in the network
            addStaticAPNs(v, hash);
            addNetworkAPNs(v, hash);
        }
        
        TransportType[] array = new TransportType[v.size()];
        v.copyInto(array);
        
        return array;
    }
    
    static void addWPTCPTypes(Vector v, Hashtable hash, ServiceBook sb) {
    	
    	// WPTCP is WAP2 gateways provided by the
        // carriers. While we are at it, we can also try to use these
        // records to do a Direct TCP connection. The WPTCP service book
        // does not support sockets, but the underlying APN may do so
        // grab the record from the browser, it's usually correct
        ServiceRecord sr = BrowserUtils.getTransportForBrowserType(BrowserUtils.BROWSER_WAP);
        if(sr != null) {
            // found the book from the browser
            insertSingleType(v, hash, new WPTCP(sr));
        } else if(sb != null) {
            // incase there is a separate book for it            
        	// final resort, add what can be found
        	ServiceRecord[] recordsWPTCP = sb.findRecordsByCid( "WPTCP" );
            if(recordsWPTCP != null) {
                int size = recordsWPTCP.length;
                for(int i = 0; i < size; i++) {
                    insertSingleType(v, hash, new WPTCP(recordsWPTCP[i]));
                }
            }        	
        }       
    }
    
    private static void addRecordAPNs(Vector v, Hashtable aHash, ServiceRecord[] aRecords) {
        if(aRecords != null) {
            int size = aRecords.length;
            for(int i = 0; i < size; i++) {
                ServiceRecord record = aRecords[i];
                try {
                    if(record != null) {
                        insertSingleType(v, aHash, new DetectedServiceRecordAPN(aRecords[i]));
                    }
                } catch(Exception e) {
                	// if exception, the transport cannot be used...
                }
            }
        }
    }
    
    private static void addNetworkAPNs(Vector v, Hashtable aHash) {
        // a bit ugly to go from 0 to 100 and catch the exception, but 
        // unfortunately there is no way to get the "highest apn number"
        for(int i = 0; i < 100; i++) {
            try {
                String name = RadioInfo.getAccessPointName(i);
                if(name != null) {
                    insertSingleType(v, aHash, new DetectedNetworkAPN(name));
                }
            } catch(RadioException e) {
                // nothing more to do but return
                return;
            }
        }
    }
    
    private static void insertSingleType(Vector v, Hashtable aHash, TransportType ct) {
        if(aHash != null) {
            String id = ct.getIdentifier();
			
            if(ct.hasSufficientCoverage() && !aHash.containsKey(id)) {
            	v.addElement(ct);
                aHash.put(id, id);
            }
        } else if(ct.hasSufficientCoverage()) {
        	v.addElement(ct);
        }
    }
    
    private static void addStaticAPNs(Vector v, Hashtable aHash) {
        insertSingleType(v, aHash, new SettingsAPN());
        if(Branding.getVendorId() != -1) {
            String apn = createBrandingData(Branding.TCP_APN_DEFAULT_APNNAME);
            if(apn != null) {
                String userName = createBrandingData(Branding.TCP_APN_DEFAULT_USERNAME);
                String passWord = createBrandingData(Branding.TCP_APN_DEFAULT_PASSWORD);
                TransportType type;
                if(userName != null && passWord != null) {
                    type = new PredecidedAPN("Branding", apn, true, userName, passWord);
                } else {
                    type = new PredecidedAPN("Branding", apn);
                }
                insertSingleType(v, aHash, type);
            }
        }
    }
    
    private static String createBrandingData(int aVariable) {
        try {
            byte[] buf = Branding.getData(aVariable);
            if(buf != null && buf.length > 0) {
                return new String(buf);
            }
        } catch(Exception e) {}
        
        return null;
    }
    
    public static void printCurrentlyAvailableMethods() {
        TransportType[] ctArray = createSupportedTypesList();
        for(int i = 0; i < ctArray.length; i++) {
            ctArray[i].printDebug();
        }
    }
    
    //-------------------------------------------------------------------------
    // Utility
    
    private static String BLACKBERRY_APN = "blackberry.net";
    
    public static boolean isBlackBerryAPNConnected() {
        try {
            int apnIndex = RadioInfo.getAccessPointNumber(BLACKBERRY_APN);
            if(apnIndex >= 0) {
                // Not sure if the apn index could go negative if the apn
                // is not found. secured with an "if" just in case...
                return RadioInfo.isPDPContextActive(apnIndex);
            }
        } catch (RadioException e) {
            // thrown when? Guessing if the apn could not be found...
        }
        return false;
    }
    
    public static final int RETRY_ATTEMPTS = 10;

	private TransportType m_transportType;
    
    public static boolean shouldTryToConnectAgain(IOException e) {
        String message = e.getMessage();
        if(message == null) {
            message = e.toString();
        }
        if(message != null) {
            message = message.toLowerCase();
            
            if( contains(message, "malformed address") 
                    || contains(message, "malformed adress")
                    || contains(message, "returning null connection")){
                // If the DNS is unable to resolve the address, an IOException
                // with the message "Malformed Address Returning null connection."
                // is thrown.
                return true;
            } 

            if(contains(message, "bad dns")) {
                // for device software 4.2.1, any connection using Direct TCP
                // may fail with an error message stating "bad dns".
                // this seems only true for the first few attempts, so if we 
                // encounter this error, let's try again. However, after a reboot
                // the problem will arise again.
                return true;
            }
            
            if(contains(message, "already bound")) {
                // SiMobil has an issue with their network where making several
                // connections seems to cause some kind of collision between
                // the old connection and the new one. The new connection tries
                // to use the same local port as the old one which in turn cause
                // the device to pop a dialog...
                return true;
            }
        }
        return false;
    }
    
    private static boolean contains(String aMessage, String aSubpart) {
        return aMessage.indexOf(aSubpart) != -1;
    }
}
