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
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Graphics;

import com.wayfinder.pal.graphics.WFFont;
import com.wayfinder.pal.graphics.WFGraphics;
import com.wayfinder.pal.graphics.WFImage;

/**
 * Blackberry implementation of the WFGraphics interface. 
 * 
 */
public class BlackBerryGraphics implements WFGraphics {

	private Graphics m_Graphics;
    private int m_OriginalClipStackSize;
	
    public BlackBerryGraphics() {}
    
    public BlackBerryGraphics(Graphics g) {
        setGraphics(g);
    }
    
    // -------------------------------------------------------------------------
    // Images
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#drawImage(com.wayfinder.pal.graphics.WFImage, int, int, int)
     */
    public void drawImage(WFImage img, int x, int y, int anchor) {  
        int width = img.getWidth();
        if ((anchor & ANCHOR_RIGHT) == ANCHOR_RIGHT ) {
            x -= width;
        } else if ((anchor & ANCHOR_HCENTER) == ANCHOR_HCENTER ) {
            x -= width / 2;
        }
        int height = img.getHeight();
        if ((anchor & ANCHOR_BOTTOM) == ANCHOR_BOTTOM ) {
            y -= height;
        } else if ((anchor & ANCHOR_VCENTER) == ANCHOR_VCENTER ) {
            y -= height / 2;
        }
        m_Graphics.drawBitmap(x, y, width, height, (Bitmap) img.getNativeImage(), 0, 0);
    }
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#drawRGB(int[], int, int, int, int, int, int, boolean)
     */
    public void drawRGB(int[] argbData, int offset, int scanlength, int x, int y, 
    		int width, int height, boolean processAlpha) {

        if(processAlpha) {
            m_Graphics.drawARGB(argbData, offset, scanlength, x, y, width, height);
        } else {
            m_Graphics.drawRGB(argbData, offset, scanlength, x, y, width, height);
        }

    }
    
    // -------------------------------------------------------------------------
    // Lines
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#drawLine(int, int, int, int, int)
     */
    public void drawLine(int startX, int startY, int endX, int endY,int thickness) {
        if(thickness == 1) {
            m_Graphics.drawLine(startX, startY, endX, endY);
            lineX[1] = endX;
            lineY[1] = endY;
        } else {
            lineX[0] = startX;
            lineX[1] = endX;
            
            lineY[0] = startY;
            lineY[1] = endY;

            m_Graphics.setStrokeWidth(thickness);
            m_Graphics.drawPathOutline(lineX, lineY, null, null, false);
            m_Graphics.setStrokeWidth(1);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#drawConnectedLine(int, int, int)
     */
    public void drawConnectedLine(int endX, int endY, int aThickness) {
        drawLine(lineX[1], lineY[1], endX, endY, aThickness);
    }
    
    private final int[] lineX = new int[2];
    private final int[] lineY = new int[2];
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#supportsPath()
     */
    public boolean supportsPath() {
        return true;
    }
    
    /* (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#drawPath(int[], int[], int, int)
     */
    public void drawPath(int[] xCoords, int[] yCoords, int nbrCoords, int width) {
    	int[] xPts = new int[nbrCoords];
    	int[] yPts = new int[nbrCoords];
    	for (int i=0; i<nbrCoords; i++) {
    		xPts[i] = xCoords[i];
    		yPts[i] = yCoords[i];
    	}
        m_Graphics.setStrokeWidth(width);
        m_Graphics.drawPathOutline(xPts, yPts, null, null, false);
        m_Graphics.setStrokeWidth(1);
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#allowAntialias(boolean)
     */
    public void allowAntialias(boolean allow) {
        m_Graphics.setDrawingStyle(Graphics.DRAWSTYLE_AALINES, allow);
        m_Graphics.setDrawingStyle(Graphics.DRAWSTYLE_AAPOLYGONS, allow);
    }
    
    //-------------------------------------------------------------------------
    // Shapes
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#supportsPolygon()
     */
    public boolean supportsPolygon() {
        return true;
    }
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#drawPolygon(int[], int[])
     */
    public void drawPolygon(int[] x, int[] y) {
        m_Graphics.drawFilledPath(x, y, null, null);
    }
    
    
    private int xPositions [] = new int[3];
    private int yPositions [] = new int[3];
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#fillTriangle(int, int, int, int, int, int)
     */
    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        xPositions[0] = x1;
        xPositions[1] = x2;
        xPositions[2] = x3;
        yPositions[0] = y1;
        yPositions[1] = y2;
        yPositions[2] = y3;
        
        m_Graphics.drawFilledPath(xPositions, yPositions, null, null );
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#fillPolygon(int[], int[])
     */
    public void fillPolygon(int[] x, int[] y, int length) {
    	int[] xPts = new int[length];
    	int[] yPts = new int[length];
    	for (int i = 0; i < length; i++) {
    		xPts[i] = x[i];
    		yPts[i] = y[i];
    	}
        m_Graphics.drawFilledPath(xPts, yPts, null, null );
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#drawRect(int, int, int, int)
     */
    public void drawRect(int x, int y, int width, int height) {
        m_Graphics.drawRect(x, y, width, height);
    }
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#fillRect(int, int, int, int)
     */
    public void fillRect(int x, int y, int width, int height) {
        m_Graphics.fillRect(x, y, width, height);
    }
    
    //-------------------------------------------------------------------------
    // Text
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#drawText(java.lang.String, int, int, int, int)
     */
    public void drawText(String str, int x, int y, int maxWidth, int anchor) {
        if (( anchor & ANCHOR_RIGHT ) == ANCHOR_RIGHT ) {
            x -= m_Graphics.getFont().getAdvance( str );
        } else if (( anchor & ANCHOR_HCENTER ) == ANCHOR_HCENTER ) {
            x -= m_Graphics.getFont().getAdvance( str ) / 2;
        }
        
        if(( anchor & ANCHOR_BOTTOM) == ANCHOR_BOTTOM) {
            y -= m_Graphics.getFont().getHeight();
        } else if (( anchor & ANCHOR_BASELINE) == ANCHOR_BASELINE) {
            y -= m_Graphics.getFont().getBaseline();
        }
        
        m_Graphics.drawText(str, x, y, DrawStyle.TOP | DrawStyle.LEFT, maxWidth);
    }
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#drawText(java.lang.String, int, int, int)
     */
    public void drawText(String str, int x, int y, int anchor) {
        this.drawText(str, x, y, Integer.MAX_VALUE, anchor);
    }
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#drawText(java.lang.String, int, int, int, int, java.lang.String)
     */
    public void drawText(String str, int x, int y, int maxWidth, int anchor, String suffix) {
    	
    	//TODO: Check this implementation when a test client is up and running. 
    	
    	if ((anchor & ANCHOR_RIGHT) == ANCHOR_RIGHT ) {
            x -= m_Graphics.getFont().getAdvance( str );
        } else if ((anchor & ANCHOR_HCENTER) == ANCHOR_HCENTER ) {
            x -= m_Graphics.getFont().getAdvance( str ) / 2;
        }
        
        if((anchor & ANCHOR_BOTTOM) == ANCHOR_BOTTOM) {
            y -= m_Graphics.getFont().getHeight();
        } else if ((anchor & ANCHOR_BASELINE) == ANCHOR_BASELINE) {
            y -= m_Graphics.getFont().getBaseline();
        }
        
        final int strWidth = m_Graphics.getFont().getAdvance(str);
        if(strWidth > maxWidth) {
        	if(suffix != null) {
        		maxWidth -= m_Graphics.getFont().getAdvance(suffix);
        		if(maxWidth <= 0)
        			throw new IllegalArgumentException("maxWidth are less then the size of the suffix!");
        		
        		int index = m_Graphics.drawText(str, x, y, DrawStyle.TOP | DrawStyle.LEFT, maxWidth);
        		int sw = m_Graphics.getFont().getAdvance(str.substring(0, index));
        		m_Graphics.drawText(suffix, x+sw, y, DrawStyle.TOP | DrawStyle.LEFT, maxWidth);        		        		
        	} else {
        		m_Graphics.drawText(str, x, y, DrawStyle.TOP | DrawStyle.LEFT, maxWidth);
        	}        	
        } else {
        	m_Graphics.drawText(str, x, y, DrawStyle.TOP | DrawStyle.LEFT, maxWidth);
        }        
	}

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#supportRotatedTexts()
     */
	public boolean supportRotatedTexts() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.graphics.WFGraphics#drawRotatedText(java.lang.String, int, int, double)
	 */
	public void drawRotatedText(String str, int x, int y, double tanTheta) {
		// Not supported for BlackBerry
	}
	
    //-------------------------------------------------------------------------
    // Clip

	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.graphics.WFGraphics#getClipHeight()
	 */
    public int getClipHeight() {
        return m_Graphics.getClippingRect().height;
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#getClipWidth()
     */
    public int getClipWidth() {
        return m_Graphics.getClippingRect().width;
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#getClipX()
     */
    public int getClipX() {
        return m_Graphics.getClippingRect().x;
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#getClipY()
     */
    public int getClipY() {
        return m_Graphics.getClippingRect().y;
    }
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#setClip(int, int, int, int)
     */
    public void setClip(int x, int y, int width, int height) {
        checkStackSizeAndPop();
        m_Graphics.pushContext(x, y, width, height, 0, 0 );
    }

    //-------------------------------------------------------------------------
    // Misc
    
	/*
	 * (non-Javadoc)
	 * @see com.wayfinder.pal.graphics.WFGraphics#setColor(int)
	 */
    public void setColor(int color) {
        m_Graphics.setColor(color);
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#setFont(com.wayfinder.pal.graphics.WFFont)
     */
    public void setFont(WFFont font) {
        m_Graphics.setFont(((BlackBerryFont)font).getWrappedFont());
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFGraphics#getColor()
     */
    public int getColor() {
        return m_Graphics.getColor();
    }
    
    //-------------------------------------------------------------------------
    // Private / protected
    
    void setGraphics(Graphics g) {
        m_Graphics = g;
        if(g != null) {
            m_OriginalClipStackSize = g.getContextStackSize();
        }
    }
    
    void clearGraphics() {
        checkStackSizeAndPop();
        m_Graphics = null;        
    }
    
    
    Graphics getNativeGraphics() {
        return m_Graphics;
    }

    private void checkStackSizeAndPop() {
        if(m_OriginalClipStackSize < m_Graphics.getContextStackSize()) {
            m_Graphics.popContext();
        }
    }

}
