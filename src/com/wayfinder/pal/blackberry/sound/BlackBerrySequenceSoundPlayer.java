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
package com.wayfinder.pal.blackberry.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

import net.rim.device.api.io.IOUtilities;

import com.wayfinder.pal.sound.SoundException;
import com.wayfinder.pal.sound.SoundPlayer;

class BlackBerrySequenceSoundPlayer implements SoundPlayer, PlayerListener {
	
	private final String[] m_filePaths;
	private String m_filePathsString;
	private Player m_player = null;
	private boolean m_playerFinished = false;
	
	/**
	 * Constructs a BlackBerrySequenceSoundPlayer using the
	 * sounds at the specified file paths
	 * 
	 * @param filePaths
	 */
	public BlackBerrySequenceSoundPlayer(String[] filePaths) {
		m_filePaths = filePaths;
		for (int i=0; i<filePaths.length; i++) {
			m_filePathsString += filePaths[i] + " ";
		}
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.sound.SoundPlayer#getDuration()
	 */
	public int getDuration() {
		return (int) (m_player.getDuration() / 1000);
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.sound.SoundPlayer#play()
	 */
	public void play() throws SoundException, InterruptedException {
		if (m_player == null) {
			return;
		}
		
		try {
			m_player.addPlayerListener(this);
			m_player.start();
			waitToFinish();
		} catch (MediaException e) {
			throw new SoundException("Could not play sound " + m_filePathsString, e);
		} finally {
			unprepare();
		}
	}
	
	private synchronized void waitToFinish() throws InterruptedException {
		while (!m_playerFinished) {
			wait();
		}
	}
	
	private synchronized void notifyFinish() {
		m_playerFinished = true;
		notifyAll();
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.sound.SoundPlayer#prepare()
	 */
	public void prepare() throws SoundException, InterruptedException {
        InputStream[] is = new InputStream[m_filePaths.length];
        int totalLength = 0;
        byte[] soundBuffer;
        
        try {
	        // Get InputStreams for all sound files
	        for (int i=0; i<is.length; i++) {
	            is[i] = m_filePaths[i].getClass().getResourceAsStream(m_filePaths[i]);
	            totalLength += is[i].available();
	        }
	        
	        // Concatenate InputStreams
	        soundBuffer = new byte[totalLength];
	        int startOffset = 0;
	        for (int i=0; i<is.length; i++) {
	            int soundSize = is[i].available();
	            byte[] data = IOUtilities.streamToBytes(is[i]);
	            System.arraycopy(data, 0, soundBuffer, startOffset, data.length);
	            is[i].close();
	            startOffset += soundSize;
	        }
	        InputStream soundIs = new ByteArrayInputStream(soundBuffer);
	        
	        // Create and prefetch player, assuming mp3 format for now
	        m_player = Manager.createPlayer(soundIs, "audio/mpeg");
	        m_player.prefetch();
        } catch (IOException e) {
        	throw new SoundException("Could not prepare sound " + m_filePathsString, e);
        } catch (MediaException e) {
        	throw new SoundException("Could not prepare sound " + m_filePathsString, e);
        }
	}

	/* (non-Javadoc)
	 * @see com.wayfinder.pal.sound.SoundPlayer#unprepare()
	 */
	public void unprepare() {
		if (m_player.getState() != Player.CLOSED) {
            m_player.removePlayerListener(this);
            m_player.deallocate();
            m_player.close();
        }
	}

	public void playerUpdate(Player player, String event, Object eventData) {
		if (event.equals(PlayerListener.END_OF_MEDIA)) {
            notifyFinish();
        }
	}

}
