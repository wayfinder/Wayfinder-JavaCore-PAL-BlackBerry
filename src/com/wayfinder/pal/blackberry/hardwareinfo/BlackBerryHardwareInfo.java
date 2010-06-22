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
package com.wayfinder.pal.blackberry.hardwareinfo;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;

import net.rim.device.api.bluetooth.BluetoothSerialPort;
import net.rim.device.api.system.CDMAInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.IDENInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.SIMCardException;
import net.rim.device.api.system.SIMCardInfo;

import com.wayfinder.pal.blackberry.permissions.PALPermissions;
import com.wayfinder.pal.blackberry.permissions.PermissionsHandler;
import com.wayfinder.pal.hardwareinfo.HardwareInfo;

public class BlackBerryHardwareInfo implements HardwareInfo {
    
    private final PermissionsHandler m_permHandler;
    
    public BlackBerryHardwareInfo(PermissionsHandler permHandler) {
        m_permHandler = permHandler;
    }

	public String getBlackBerryPIN() {
		String pin = null;
		int pinNumber = DeviceInfo.getDeviceId();
		
		// make sure we don't send the pin if the number is INVALID_DEVICE_ID
        // in that case it's a device running BlackBerry Built-In and not a
        // "true" BlackBerry
		if (pinNumber == DeviceInfo.INVALID_DEVICE_ID) {
			pin = checkValidString(Integer.toHexString(DeviceInfo.getDeviceId()));
		}
		
		return pin;
	}

	public String getBluetoothMACAddress() {
		String btMacAddress = null;
		if(m_permHandler.checkHasPALPermission(PALPermissions.PERMISSION_BLUETOOTH_SPP)) {
		    if (BluetoothSerialPort.isSupported() && LocalDevice.isPowerOn()) {            
		        try {
		            btMacAddress = LocalDevice.getLocalDevice().getBluetoothAddress();
		        } catch(BluetoothStateException e) {
		            btMacAddress = null;
		        }
		    }
		}
        
        return btMacAddress;
	}

	public String getESN() {
		if (RadioInfo.areWAFsSupported(RadioInfo.WAF_CDMA)) {
			return checkValidString(Integer.toString(CDMAInfo.getESN()));
		}
		return null;
	}

	public String getIMEI() {
		if (RadioInfo.areWAFsSupported(RadioInfo.WAF_3GPP)) {
			return GPRSInfo.imeiToString(GPRSInfo.getIMEI());
		} else if (RadioInfo.areWAFsSupported(RadioInfo.WAF_IDEN)) {
			return IDENInfo.imeiToString(IDENInfo.getIMEI());
		}
		return null;
	}

	public String getIMSI() {
		byte[] imsiArray = null;
		String imsi = null;
		
		if(m_permHandler.checkHasPALPermission(PALPermissions.PERMISSION_INFO_NETWORK)) {
		    if (RadioInfo.areWAFsSupported(RadioInfo.WAF_CDMA)) {
		        imsiArray = CDMAInfo.getIMSI();
		    } else if (RadioInfo.areWAFsSupported(RadioInfo.WAF_3GPP) ||
		            (RadioInfo.areWAFsSupported(RadioInfo.WAF_IDEN))) {
		        try {
		            imsiArray = SIMCardInfo.getIMSI();
		        } catch (SIMCardException e) {
		            imsiArray = null;
		        }
		    }

		    if (imsiArray != null && imsiArray.length > 0) {
		        try {
		            imsi = convertBCDtoString(imsiArray);
		        } catch(NumberFormatException e) {
		            imsi = null;
		        }
		    }
		}
		
		return imsi;
	}
	
	private static String checkValidString(String str) {
        if(str != null) {
            str = str.trim();
            if(str.length() > 0) {
                return str;
            }
        }
        return null;
    }
	
	// don't touch this method... it was given to us by RIM
    private static String convertBCDtoString(byte[] buf) throws NumberFormatException {
        char[] chars = new char[buf.length];
        for (int i = 0; i < buf.length; i++) {
            chars[i] = (char) (buf[i] + 0x30);
            if (!Character.isDigit(chars[i])) {
                // in case the string would contain garbage (as in the case
                // the imsi from the CDMA simulator), we throw a NFE to prevent
                // this from being sent to the server
                throw new NumberFormatException();
            }
        }
        return new String(chars);
    }

}
