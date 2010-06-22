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

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Ui;

import com.wayfinder.pal.graphics.WFFont;
import com.wayfinder.pal.graphics.WFGraphicsFactory;
import com.wayfinder.pal.graphics.WFImage;

/**
 * Blackberry implementation of the WFGraphicsFactory interface. 
 */
public class BlackBerryGraphicsFactory implements WFGraphicsFactory {
	
	//TODO: Adjust the font size depending on the dpi of the screen. This is 
	// easier to do ones a test client is up and running. 
	// import net.rim.device.api.system.Display;
    // Display.getVerticalResolution(); // Pixel per meter
    // Display.getHorizontalResolution(); // Pixel per meter
	private static final int FONT_PIXEL_SIZE_SMALL = 15;
    private static final int FONT_PIXEL_SIZE_MEDIUM = 18;
    private static final int FONT_PIXEL_SIZE_LARGE = 21;
    private static final int FONT_PIXEL_SIZE_VERY_LARGE = 40;
	
	private FontFamily m_FontFamily;
	
	public BlackBerryGraphicsFactory() {
		m_FontFamily = Font.getDefault().getFontFamily();
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.graphics.WFGraphicsFactory#createWFImage(byte[], int, int)
	 */
	public WFImage createWFImage(byte[] buf, int offset, int length) {
        EncodedImage encodedImage = EncodedImage.createEncodedImage(buf,offset,length );
        encodedImage.setDecodeMode(EncodedImage.DECODE_ALPHA);
        return new BlackBerryImage(encodedImage.getBitmap());
    }

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.graphics.WFGraphicsFactory#createWFImage(int, int)
	 */
    public WFImage createWFImage(int width, int height) {
        return new BlackBerryImage(new Bitmap(width, height));
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphicsFactory#createWFImage(int, int, int)
     */
    public WFImage createWFImage(int width, int height, int color) {
        return new BlackBerryImage(width, height, color);
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphicsFactory#createWFImage(java.lang.String)
     */
    public WFImage createWFImage(String resourceName) {
        Bitmap b = Bitmap.getBitmapResource(
                ApplicationDescriptor.currentApplicationDescriptor().getModuleName(),
                resourceName);
        if(b != null) {
            return new BlackBerryImage(b);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphicsFactory#createWFImage(int[], int, int, boolean)
     */
	public WFImage createWFImage(int[] rgb, int width, int height, boolean processAlpha) {
		Bitmap b = new Bitmap(width, height);
        if(processAlpha) {
            b.createAlpha(Bitmap.ALPHA_BITDEPTH_8BPP);
        }
        b.setARGB(rgb, 0, width, 0, 0, width, height);
        return new BlackBerryImage(b);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.graphics.WFGraphicsFactory#getWFFont(int, int)
	 */
	public WFFont getWFFont(int size, int style) throws IllegalArgumentException {		
		int pxSize = convertToPixelSize(size);
        if(Display.getHorizontalResolution() >= 8547) {
            pxSize += 10;
        }
        int rimStyle;
        if(size == WFFont.SIZE_VERY_LARGE) {
            rimStyle = Font.EXTRA_BOLD;
        } else {
            rimStyle = convertToRIMStyle(style);
        }
        
        Font f = m_FontFamily.getFont(rimStyle, pxSize, Ui.UNITS_px);
        f = f.derive(rimStyle, pxSize, Ui.UNITS_px, Font.ANTIALIAS_STANDARD, 0);
        return new BlackBerryFont(f);
	}
	
	private static int convertToPixelSize(int wfSize) {
        switch (wfSize) {
            case WFFont.SIZE_SMALL:       return FONT_PIXEL_SIZE_SMALL;
            case WFFont.SIZE_MEDIUM:      return FONT_PIXEL_SIZE_MEDIUM;
            case WFFont.SIZE_LARGE:       return FONT_PIXEL_SIZE_LARGE;
            case WFFont.SIZE_VERY_LARGE:  return FONT_PIXEL_SIZE_VERY_LARGE;
        }
        throw new IllegalArgumentException("WFont size " + wfSize + " is not a proper size");
    }
	
	private static int convertToRIMStyle(int wfStyle) {
        if(wfStyle == WFFont.STYLE_PLAIN) {
            return Font.PLAIN;
        }
        int bbStyle = 0;
        if((wfStyle & WFFont.STYLE_BOLD) == WFFont.STYLE_BOLD) {
            bbStyle |= Font.BOLD;
        }
        if((wfStyle & WFFont.STYLE_UNDERLINE) == WFFont.STYLE_UNDERLINE) {
            bbStyle |= Font.UNDERLINED;
        }
        if((wfStyle & WFFont.STYLE_ITALIC) == WFFont.STYLE_ITALIC) {
            bbStyle |= Font.ITALIC;
        }
        return bbStyle;
    }

}
