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
package com.wayfinder.pal.blackberry.graphics;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Graphics;

import com.wayfinder.pal.graphics.WFGraphics;
import com.wayfinder.pal.graphics.WFImage;

/**
 * Blackberry implementation of WFImage. 
 */
public class BlackBerryImage extends WFImage {
	
	private Bitmap m_Bitmap;
	
	public BlackBerryImage(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());
        m_Bitmap = bitmap;
    }
    
    BlackBerryImage(EncodedImage img) {
        this(img.getBitmap());
    }
    
    BlackBerryImage(int[] argb, int width, int height) {
        super(argb, width, height);
    }
    
    BlackBerryImage(int width, int height, int color) {
        super(width, height, color);
    }
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFImage#drawNativeImage(com.wayfinder.pal.graphics.WFGraphics, int, int)
     */
    public void drawNativeImage(WFGraphics g, int x, int y) {
        g.drawImage(this, x, y, 0);
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFImage#getNativeARGBData(int[], int, int, int, int, int, int)
     */
    protected void getNativeARGBData(int[] rgbData, int offset,
            int scanlength, int x, int y, int width, int height) {
        m_Bitmap.getARGB(rgbData, offset, scanlength, x, y, width, height);
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFImage#getNativeImage()
     */
    public Object getNativeImage() {
        return m_Bitmap;
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFImage#getWFGraphics()
     */
    public WFGraphics getWFGraphics() {
        return new BlackBerryGraphics(new Graphics(m_Bitmap));
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFImage#hasNativeImage()
     */
    public boolean hasNativeImage() {
        return (m_Bitmap != null);
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFImage#isWritable()
     */
    public boolean isWritable() {
        return m_Bitmap.isWritable();
    }
}
