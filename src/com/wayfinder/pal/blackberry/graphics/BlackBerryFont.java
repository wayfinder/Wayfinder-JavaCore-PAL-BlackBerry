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

import net.rim.device.api.ui.Font;

import com.wayfinder.pal.graphics.WFFont;

/**
 * Blackberry implementation of the WFFont interface. 
 *
 */
public class BlackBerryFont implements WFFont {

	private Font m_Font;
    
    BlackBerryFont(Font aFont) {
        m_Font = aFont;
    }
    
    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFFont#getFontHeight()
     */
    public int getFontHeight() {
        return m_Font.getHeight();
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFFont#getStringWidth(java.lang.String)
     */
    public int getStringWidth(String str) {
        return m_Font.getAdvance(str);
    }

    /*
     * (non-Javadoc)
     * @see com.wayfinder.pal.graphics.WFFont#getStyle()
     */
    public int getStyle() {
        final int realStyle = m_Font.getStyle();
        if(realStyle == Font.PLAIN) {
            return STYLE_PLAIN;
        }
        
        int wfstyle = 0;
        if((realStyle & Font.BOLD) == Font.BOLD) {
            wfstyle |= STYLE_BOLD;
        }
        if((realStyle & Font.UNDERLINED) == Font.UNDERLINED) {
            wfstyle |= STYLE_UNDERLINE;
        }
        if((realStyle & Font.ITALIC) == Font.ITALIC) {
            wfstyle |= STYLE_ITALIC;
        }
        
        return wfstyle;
    }
    
    Font getWrappedFont() {
        return m_Font;
    }
	
}
