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
package com.wayfinder.pal.blackberry.debug;

import com.wayfinder.pal.debug.Level;
import com.wayfinder.pal.debug.LogHandler;
import com.wayfinder.pal.debug.LogMessage;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.EventLogger;

/**
 * This class handles the logging of errors.
 * 
 */
public final class Logger implements LogHandler {
    
    private final long m_guid;
    private final String m_appName;
    
    public Logger(long guid) {
        m_guid = guid;
        m_appName = ApplicationDescriptor.currentApplicationDescriptor().getName();
        // Registers the GUID. This must be called before any debug messages are 
        // written. If this is called more than once, the following calls
        // will have no effect. 
        EventLogger.register(guid, m_appName, EventLogger.VIEWER_STRING);
    }
    
    
    /* (non-Javadoc)
     * @see com.wayfinder.pal.debug.LogHandler#writeMessageToPlatformLog(com.wayfinder.pal.debug.LogMessage)
     */
    public void writeMessageToPlatformLog(LogMessage message) {
        final int bblevel = getBBLogLevel(message.getLevel());
        switch(message.getType()) {
        case LogMessage.TYPE_EXCEPTION:
           if(bblevel <= EventLogger.getMinimumLevel()) {
               String msgStr = message.getMethodName() + " threw exception";
               EventLogger.logEvent(m_guid, msgStr.getBytes(), bblevel);
               // will dump to log automatically
               message.getThrowable().printStackTrace();
           }
           break;
           
        case LogMessage.TYPE_MESSAGE:
            String msgStr = message.getMethodName() + "::" + message.getMessage();
            EventLogger.logEvent(m_guid, msgStr.getBytes(), bblevel);
            break;
        
        default:
            String errMsg = "Logger got unknown LogMessage type: " + message.getType();
            EventLogger.logEvent(m_guid, errMsg.getBytes(), EventLogger.ALWAYS_LOG);
        }
    }
    
    
    /**
     * Converts the PAL log level into the BlackBerry equivalent
     * 
     * @param level A {@link Level} object
     * @return One of the log levels from {@link EventLogger}
     */
    private static int getBBLogLevel(Level level) {
        switch(level.getIntValue()) {
        case Level.VALUE_TRACE:
        case Level.VALUE_DEBUG:
            return EventLogger.DEBUG_INFO;
        
        case Level.VALUE_INFO:
            return EventLogger.INFORMATION;
                
        case Level.VALUE_WARN:
            return EventLogger.WARNING;
            
        case Level.VALUE_ERROR:
            return EventLogger.ERROR;
            
        case Level.VALUE_FATAL:
            return EventLogger.SEVERE_ERROR;
        }
        return EventLogger.ALWAYS_LOG;
    }
    
    
    
    
    //-------------------------------------------------------------------------
    // Logging
    
    /**
     * Writes a debug message to the internal eventlog of the device.
     * <p>
     * Debug messages should only contain messages that are
     * "developers eyes only". This is also called from the Debug class in
     * polish to allow remote debugging.
     * <p>
     * These messages should NOT be left in the code. They should only be used
     * during development.
     * <p>
     * Debug messages are hidden by default in the event log.
     * 
     * @param message The message to write
     */
    public void debug(String message) {
        logErrorInternal(message, EventLogger.DEBUG_INFO);
    }
    
    
    /**
     * Writes an information message to the internal eventlog of the device.
     * <p>
     * Information messages should be VERY scarse and may contain general
     * information to allow us to better determine the customer's problems.
     * <p>
     * Example of info messages are decisions by the application that will
     * effect large things. For example what connection method is used.
     * <p>
     * Information messages are hidden by default in the event log.
     * 
     * @param message The message to write
     */
    public void info(String message) {
        logErrorInternal(message, EventLogger.INFORMATION);
    }
    
    
    /**
     * Writes a warning message to the internal eventlog of the device.
     * <p>
     * Warning messages should be logged whenever the application encounters a
     * problem or exception that it can handle and will not have any impact at
     * all on the user. 
     * <p>
     * For example the application could warn if a server URL
     * cannot be reached due to blockage in the network, but another server URL
     * works fine.
     * <p>
     * Warning messages are shown by default in the event log.
     * 
     * @param message The message to write
     */
    public void warning(String message) {
        logErrorInternal(message, EventLogger.WARNING);
    }
    
    
    /**
     * Writes an error message to the internal eventlog of the device.
     * <p>
     * Error messages should be logged whenever the application encounters a
     * problem or exception that it can handle, but will have a negative impact
     * on the user experience. This will show up in bold in the internal event
     * log.
     * <p>
     * A typical error message would be if the SMS system cannot be started due
     * to restrictions by the IT Policy. Since we cannot override the IT Policy
     * we cannot fix the error programmatically, but showing it will help 
     * customer support and the developers to track down the problem and 
     * explain it to the user.
     * <p>
     * Error messages are shown (in bold) by default in the event log.
     * 
     * @param message The message to write
     */
    public void error(String message) {
        logErrorInternal(message, EventLogger.ERROR);
    }
    
    
    /**
     * Writes a severe error message to the internal eventlog of the device.
     * <p>
     * Severe error messages should be logged whenever the application
     * encounters a problem that it cannot recover from and that will most
     * likely crash the entire program.
     * <p>
     * A typical severe error message would be an application bug that causes
     * a system to shut down.
     * <p>
     * Severe messages are shown (in bold) by default in the event log.
     * 
     * @param message The message to write
     */
    public void severe(String message) {
        logErrorInternal(message, EventLogger.SEVERE_ERROR);
    }
    
    
    private void logErrorInternal(String message, int type) {
        System.out.println(m_appName +  ": " + getReadableErrorLevel(type) + message);
        EventLogger.logEvent(0L,
                             message.getBytes(),
                             type);
    }
    
    private static String getReadableErrorLevel(int aType) {
        switch(aType) {
        case EventLogger.ALWAYS_LOG:
            return " ALWAYS: ";
            
        case EventLogger.DEBUG_INFO:
            return " DEBUG: ";
            
        case EventLogger.INFORMATION:
            return " INFO: ";
            
        case EventLogger.WARNING:
            return " WARNING: ";
            
        case EventLogger.ERROR:
            return " ERROR: ";
            
        case EventLogger.SEVERE_ERROR:
            return " *** CRITICAL ERROR *** ";
            
        default:
            return " UNKNOWN: ";
        
        }
    }


	public void startFileLogging() {
		// 
	} 
}
